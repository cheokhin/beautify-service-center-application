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

        JPanel mainMenuPanel = createMainMenuPanel();
        mainContainer.add(mainMenuPanel, "MAIN_MENU");

        LogInUI loginPanel = new LogInUI();
        mainContainer.add(loginPanel, "LOGIN");

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

        // Added a 5px margin to the top and right so the buttons don't touch the exact edge
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

        // Automatically apply the hover effect
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

    private JPanel createMainMenuPanel() {
        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout()); 

        JPanel topCard = new RoundedPanel(25, new Color(35, 35, 50));
        topCard.setPreferredSize(new Dimension(320, 100));
        topCard.setMaximumSize(new Dimension(320, 100));
        topCard.setLayout(null);

        Icon baseIcon = UIManager.getIcon("FileView.computerIcon");
        Image img = ((ImageIcon) baseIcon).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setBounds(20, 25, 50, 50);

        logoLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Image bigger = ((ImageIcon) baseIcon).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(bigger));
                logoLabel.setBounds(15, 20, 60, 60);
            }
            public void mouseExited(MouseEvent e) {
                Image normal = ((ImageIcon) baseIcon).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(normal));
                logoLabel.setBounds(20, 25, 50, 50);
            }
        });

        topCard.add(logoLabel);

        JLabel shopName = new JLabel("APU AUTO SERVICE");
        shopName.setBounds(90, 20, 220, 25);
        shopName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        shopName.setForeground(Color.WHITE);
        topCard.add(shopName);

        JLabel slogan = new JLabel("Quality Service You Can Trust");
        slogan.setBounds(90, 45, 220, 20);
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        slogan.setForeground(new Color(0, 200, 255)); 
        topCard.add(slogan);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(320, 250));
        card.setMaximumSize(new Dimension(320, 250));
        card.setLayout(null);

        JLabel title = new JLabel(" MAIN MENU", SwingConstants.CENTER);
        title.setBounds(50, 20, 220, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        Icon titleIcon = resizeIcon(UIManager.getIcon("OptionPane.informationIcon"), 20, 20);
        title.setIcon(titleIcon);
        title.setIconTextGap(10);
        card.add(title);

        Icon loginIcon = resizeIcon(UIManager.getIcon("FileView.directoryIcon"), 18, 18);
        Icon signInIcon = resizeIcon(UIManager.getIcon("FileView.fileIcon"), 18, 18);
        Icon exitIcon = resizeIcon(UIManager.getIcon("OptionPane.errorIcon"), 18, 18);

        JButton loginBtn = new ModernButton(" Log In", loginIcon);
        loginBtn.setBounds(50, 70, 220, 40);
        
        JButton signInBtn = new ModernButton(" Sign In", signInIcon);
        signInBtn.setBounds(50, 120, 220, 40);
        
        JButton exitBtn = new ModernButton(" Exit", exitIcon);
        exitBtn.setBounds(50, 170, 220, 40);

        card.add(loginBtn);
        card.add(signInBtn);
        card.add(exitBtn);

        loginBtn.addActionListener(e -> {
            MainUI.instance.showPage("LOGIN");
        });

        signInBtn.addActionListener(e -> checkManager());
        exitBtn.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        bg.add(topCard, gbc);

        gbc.gridy = 1;
        bg.add(card, gbc);

        return bg;
    }

    private void setupWindowResizing() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            private int cursor = Cursor.DEFAULT_CURSOR;
            private Point startPos = null;
            private Rectangle startBounds = null;
            private final int BORDER = 6; // Thickness of the invisible resizing border

            @Override
            public void eventDispatched(AWTEvent event) {
                if (!(event instanceof MouseEvent)) return;
                MouseEvent me = (MouseEvent) event;
                
                // Only process events for our MainUI frame
                Window win = SwingUtilities.getWindowAncestor(me.getComponent());
                if (win != MainUI.this) return;

                // Disable resizing if window is maximized
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    if (getCursor().getType() != Cursor.DEFAULT_CURSOR) setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return; 
                }

                Point p = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), MainUI.this);

                if (me.getID() == MouseEvent.MOUSE_MOVED) {
                    int w = getWidth();
                    int h = getHeight();
                    cursor = Cursor.DEFAULT_CURSOR;

                    // Check corners and edges
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
                        me.consume(); // Prevents clicking the X button underneath the resize zone
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

                    // Prevent window from being sized too small
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

    private Icon resizeIcon(Icon icon, int w, int h) {
        Image img = ((ImageIcon) icon).getImage();
        return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    public static void main(String[] args) {
        new MainUI();
    }
}