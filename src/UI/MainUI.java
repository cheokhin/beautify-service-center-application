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