package UI;

import UI.Components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MainUI extends JFrame {
    private static final long serialVersionUID = 1L;

    public static MainUI instance; 
    public CardLayout cardLayout;  
    public JPanel mainContainer;   

    public MainUI() {
        instance = this; 

        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);
        int height = (int) (screenSize.height * 0.8);
        
        setSize(width, height);
        setLocationRelativeTo(null); 

        JPanel titleBar = createTitleBar();
        add(titleBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        add(mainContainer, BorderLayout.CENTER);

        LogInUI loginPanel = new LogInUI();
        mainContainer.add(loginPanel, "LOGIN");

        SignInUI registerPanel = new SignInUI();
        mainContainer.add(registerPanel, "REGISTER");

        cardLayout.show(mainContainer, "MAIN_MENU");

        setupWindowResizing();

        setVisible(true);
    }

    public void showPage(String pageName) {
        cardLayout.show(mainContainer, pageName);
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(20, 20, 35)); 
        titleBar.setPreferredSize(new Dimension(getWidth(), 35));

        JLabel titleLabel = new JLabel("   APU Automotive Service Centre");
        titleLabel.setForeground(new Color(220, 220, 220));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleBar.add(titleLabel, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controls.setOpaque(false);

        controls.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 5));

        JButton minimizeBtn = createControlButton(" – ", new Color(60, 60, 80));
        JButton maximizeBtn = createControlButton(" ▢ ", new Color(60, 60, 80));
        JButton closeBtn = createControlButton(" X ", new Color(220, 50, 50));

        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));

        maximizeBtn.addActionListener(e -> {
            setExtendedState(getExtendedState() == JFrame.MAXIMIZED_BOTH ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
        });

        closeBtn.addActionListener(e -> System.exit(0));

        controls.add(minimizeBtn);
        controls.add(maximizeBtn);
        controls.add(closeBtn);
        titleBar.add(controls, BorderLayout.EAST);

        final Point[] dragPoint = new Point[1];
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (getCursor().getType() != Cursor.DEFAULT_CURSOR) return;

                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - dragPoint[0].x, currCoords.y - dragPoint[0].y);
            }
        });

        return titleBar;
    }

    private JButton createControlButton(String text, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(20, 20, 25));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(20, 20, 25));
            }
        });

        return btn;
    }

    private void setupWindowResizing() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            private int cursor = Cursor.DEFAULT_CURSOR;
            private Point startPos = null;
            private Rectangle startBounds = null;
            private final int BORDER = 6; 

            @Override
            public void eventDispatched(AWTEvent event) {
                if (!(event instanceof MouseEvent)) return;
                MouseEvent me = (MouseEvent) event;
                
                Window win = SwingUtilities.getWindowAncestor(me.getComponent());
                if (win != MainUI.this) return;

                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    if (getCursor().getType() != Cursor.DEFAULT_CURSOR) setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return; 
                }

                Point p = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), MainUI.this);

                if (me.getID() == MouseEvent.MOUSE_MOVED) {
                    int w = getWidth();
                    int h = getHeight();
                    cursor = Cursor.DEFAULT_CURSOR;

                    if (p.x < BORDER && p.y < BORDER) cursor = Cursor.NW_RESIZE_CURSOR;
                    else if (p.x > w - BORDER && p.y < BORDER) cursor = Cursor.NE_RESIZE_CURSOR;
                    else if (p.x < BORDER && p.y > h - BORDER) cursor = Cursor.SW_RESIZE_CURSOR;
                    else if (p.x > w - BORDER && p.y > h - BORDER) cursor = Cursor.SE_RESIZE_CURSOR;
                    else if (p.x < BORDER) cursor = Cursor.W_RESIZE_CURSOR;
                    else if (p.x > w - BORDER) cursor = Cursor.E_RESIZE_CURSOR;
                    else if (p.y < BORDER) cursor = Cursor.N_RESIZE_CURSOR;
                    else if (p.y > h - BORDER) cursor = Cursor.S_RESIZE_CURSOR;

                    if (getCursor().getType() != cursor) setCursor(Cursor.getPredefinedCursor(cursor));
                } 
                else if (me.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (cursor != Cursor.DEFAULT_CURSOR) {
                        startPos = me.getLocationOnScreen();
                        startBounds = getBounds();
                        me.consume(); 
                    }
                }
                else if (me.getID() == MouseEvent.MOUSE_DRAGGED && cursor != Cursor.DEFAULT_CURSOR && startBounds != null) {
                    Point currentPos = me.getLocationOnScreen();
                    int dx = currentPos.x - startPos.x;
                    int dy = currentPos.y - startPos.y;
                    
                    Rectangle bounds = new Rectangle(startBounds);


                    if (cursor == Cursor.E_RESIZE_CURSOR || cursor == Cursor.NE_RESIZE_CURSOR || cursor == Cursor.SE_RESIZE_CURSOR) {
                        bounds.width = Math.max(800, startBounds.width + dx);
                    }
                    if (cursor == Cursor.W_RESIZE_CURSOR || cursor == Cursor.NW_RESIZE_CURSOR || cursor == Cursor.SW_RESIZE_CURSOR) { bounds.x += dx; bounds.width -= dx; }
                    if (cursor == Cursor.S_RESIZE_CURSOR || cursor == Cursor.SW_RESIZE_CURSOR || cursor == Cursor.SE_RESIZE_CURSOR) bounds.height += dy;
                    if (cursor == Cursor.N_RESIZE_CURSOR || cursor == Cursor.NW_RESIZE_CURSOR || cursor == Cursor.NE_RESIZE_CURSOR) { bounds.y += dy; bounds.height -= dy; }

                    if (bounds.width >= 800 && bounds.height >= 550) {
                        setBounds(bounds);
                        validate();
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    public void checkManager() {
        int count = 0;
        try {
            File file = new File("manager.txt");
            if (!file.exists()) {
                new SignInUI();
                dispose();
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (br.readLine() != null) count++;
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (count == 0) {
            new SignInUI();
            dispose();
        } else {
            new ModernDialog("Manager Account already Exists!");
        }
    }

    public static void main(String[] args) {
        new MainUI();
    }
}