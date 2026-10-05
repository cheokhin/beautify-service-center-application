package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import CustomerUI.CustomerUI;
import ManagerUI.ManagerUI;
import CounterStaffUI.CounterStaffMenu;
import TechnicianUI.TechnicianUI;
import UI.Components.*; 

public class LogInUI extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextField idField, userField;
    JPasswordField passField;

    public LogInUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout()); 
        add(bg, BorderLayout.CENTER);

        JPanel topCard = new RoundedPanel(25, new Color(35, 35, 50));
        topCard.setPreferredSize(new Dimension(380, 100));
        topCard.setLayout(null);

        try {
            Image originalLogo = new ImageIcon("logo.png").getImage();
            
            JPanel logoPanel = new JPanel() {
                private static final long serialVersionUID = 1L;
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2.drawImage(originalLogo, 0, 0, getWidth(), getHeight(), this);
                }
            };
            
            logoPanel.setBounds(40, 25, 50, 50);
            logoPanel.setOpaque(false); 
            topCard.add(logoPanel);
            
        } catch (Exception e) {
            System.out.println("Logo not found. Make sure logo.png is in the root Beautify folder.");
        }

        JLabel title = new JLabel("APU AUTO SERVICE");
        title.setBounds(110, 20, 220, 25);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        topCard.add(title);

        JLabel slogan = new JLabel("Quality Service You Can Trust");
        slogan.setBounds(110, 45, 220, 20);
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        slogan.setForeground(new Color(0, 200, 255));
        topCard.add(slogan);

        JPanel loginCard = new RoundedPanel(25, new Color(35, 35, 50));
        loginCard.setPreferredSize(new Dimension(380, 290)); 
        loginCard.setLayout(null);

        addLabel(loginCard, "ID:", 35);
        idField = addTextField(loginCard, 35);

        addLabel(loginCard, "Username:", 80);
        userField = addTextField(loginCard, 80);

        addLabel(loginCard, "Password:", 125);

        passField = new JPasswordField();
        passField.setBounds(130, 125, 140, 30);
        styleField(passField);
        passField.setEchoChar('•');
        loginCard.add(passField);

        JButton eyeBtn = new JButton("Show");
        eyeBtn.setBounds(280, 125, 60, 30);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        eyeBtn.setBackground(new Color(60, 60, 80));
        eyeBtn.setForeground(Color.WHITE);
        eyeBtn.setMargin(new Insets(0, 0, 0, 0));
        
        eyeBtn.addActionListener(e -> {
            if (passField.getEchoChar() == (char) 0) {
                passField.setEchoChar('•');
                eyeBtn.setText("Show");
            } else {
                passField.setEchoChar((char) 0);
                eyeBtn.setText("Hide");
            }
        });
        loginCard.add(eyeBtn);

        JButton loginBtn = new ModernButton(" Login");
        loginBtn.setBounds(40, 185, 140, 40);
        
        JButton exitBtn = new ModernButton(" Exit");
        exitBtn.setBounds(200, 185, 140, 40);

        loginCard.add(loginBtn);
        loginCard.add(exitBtn);

        JLabel registerLbl = new JLabel("<html><u>Register for an account</u></html>", SwingConstants.CENTER);
        registerLbl.setBounds(115, 245, 150, 25);
        registerLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerLbl.setForeground(new Color(0, 200, 255));
        registerLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        registerLbl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { registerLbl.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { registerLbl.setForeground(new Color(0, 200, 255)); }
            public void mouseClicked(MouseEvent e) { 
                MainUI.instance.showPage("REGISTER");
            }
        });
        loginCard.add(registerLbl);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); 
        bg.add(topCard, gbc);

        gbc.gridy = 1;
        bg.add(loginCard, gbc);

        loginBtn.addActionListener(e -> login());
        exitBtn.addActionListener(e -> System.exit(0));

        KeyAdapter enterAction = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) loginBtn.doClick(); 
            }
        };

        idField.addKeyListener(enterAction);
        userField.addKeyListener(enterAction);
        passField.addKeyListener(enterAction);
    }

    private void addLabel(JPanel card, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(40, y, 90, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(label);
    }

    private JTextField addTextField(JPanel card, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(130, y, 210, 30);
        styleField(tf);
        card.add(tf);
        return tf;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    public void login() {
        String id = idField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        String fileName = getFileByID(id);

        if (fileName == null) {
            new ModernDialog("Invalid ID format!"); 
            return;
        }

        boolean idFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(id)) {
                    idFound = true;

                    if (!data[2].equals(username)) {
                        new ModernDialog("Username Invalid!");
                        return;
                    }

                    if (!data[3].equals(password)) {
                        new ModernDialog("Password Invalid!");
                        return;
                    }

                    showLoadingAndOpen(id);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!idFound) {
            new ModernDialog("ID does not exist!");
        }
    }

    private void showLoadingAndOpen(String id) {
        JDialog loading = new JDialog(MainUI.instance, true);
        loading.setUndecorated(true);
        loading.setSize(250, 120);
        loading.setLocationRelativeTo(MainUI.instance);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));

        JLabel label = new JLabel("Loading System...", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setForeground(new Color(0, 200, 255));
        bar.setBackground(new Color(60, 60, 80));

        panel.add(label, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.SOUTH);

        loading.add(panel);

        new Thread(() -> {
            try { Thread.sleep(800); } catch (Exception ignored) {}

            SwingUtilities.invokeLater(() -> {
                UserDashboard dashboard = null;

                if (id.startsWith("M")) dashboard = new ManagerUI();
                else if (id.startsWith("CS")) dashboard = new CounterStaffMenu();
                else if (id.startsWith("T")) dashboard = new TechnicianUI();
                else if (id.startsWith("C")) dashboard = new CustomerUI();

                if (dashboard != null) {
                    dashboard.openMenu(id);

                    String role = id.startsWith("M") ? "Manager" : id.startsWith("CS") ? "Counter Staff" : id.startsWith("T") ? "Technician" : "Customer";
                    SystemLogger.log(id, role, "Logged into the system");
                }

                loading.dispose();
                
                idField.setText("");
                userField.setText("");
                passField.setText("");
            });

        }).start();

        loading.setVisible(true);
    }

    private String getFileByID(String id) {
        if (id.matches("M\\d{3}")) return "manager.txt";
        if (id.matches("CS\\d{3}")) return "counter.txt";
        if (id.matches("T\\d{3}")) return "technician.txt";
        if (id.matches("C\\d{3}")) return "customer.txt";
        return null;
    }
    
}