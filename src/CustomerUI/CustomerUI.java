package CustomerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import UI.UserDashboard;

public class CustomerUI extends JFrame implements UserDashboard {

    private static final long serialVersionUID = 1L;

    String customerID;

    // Empty constructor for polymorphism
    public CustomerUI() {}

    public CustomerUI(String customerID) {

        this.customerID = customerID;

        setTitle("Customer Dashboard");
        setSize(750, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ================= BACKGROUND =================

        JPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        add(bg);

        // ================= SIDEBAR =================

        JPanel sidebar = new RoundedPanel(25, new Color(25, 25, 40));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(null);

        bg.add(sidebar, BorderLayout.WEST);

        // ================= AVATAR =================

        JPanel avatar = new JPanel() {

            private static final long serialVersionUID = 1L;

            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                // Outer Glow
                g2.setColor(new Color(0, 200, 255, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());

                // Inner Circle
                g2.setColor(new Color(0, 200, 255));
                g2.fillOval(5, 5, getWidth() - 10, getHeight() - 10);

                // Person Icon
                g2.setColor(Color.WHITE);

                // Head
                g2.fillOval(32, 22, 26, 26);

                // Body
                g2.fillRoundRect(24, 50, 42, 28, 20, 20);
            }
        };

        avatar.setBounds(55, 30, 90, 90);
        avatar.setOpaque(false);

        sidebar.add(avatar);

        // ================= USER INFO =================

        String customerName = getCustomerName(customerID);
        JLabel nameLabel = new JLabel(customerName);
        nameLabel.setBounds(30, 130, 140, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(customerID);
        idLabel.setBounds(30, 160, 140, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("CUSTOMER");
        roleLabel.setBounds(30, 195, 140, 25);
        roleLabel.setForeground(new Color(0, 200, 255));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(roleLabel);

        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setBounds(20, 235, 160, 25);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(welcomeLabel);

        // ================= TIME =================

        JLabel timeLabel = new JLabel();
        timeLabel.setBounds(20, 320, 160, 25);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(timeLabel);

        Timer timer = new Timer(1000, e -> {

            SimpleDateFormat sdf =
                    new SimpleDateFormat("hh:mm:ss a");

            timeLabel.setText(sdf.format(new Date()));
        });

        timer.start();

        // ================= MENU CARD =================

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        bg.add(centerWrapper, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(360, 350));
        card.setLayout(null);

        centerWrapper.add(card);

        // ================= TITLE =================

        JLabel title = new JLabel(" CUSTOMER MENU", SwingConstants.CENTER);
        title.setBounds(70, 20, 220, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        card.add(title);

        // ================= BUTTONS =================

        JButton profileBtn = createButton(" Edit Profile", 70);
        JButton historyBtn = createButton(" Service History", 115);
        JButton paymentBtn = createButton(" Payment History", 160);
        JButton feedbackBtn = createButton(" View Feedback", 205);
        JButton commentBtn = createButton(" Give Comment", 250);
        JButton logoutBtn = createButton(" Logout", 295);

        card.add(profileBtn);
        card.add(historyBtn);
        card.add(paymentBtn);
        card.add(feedbackBtn);
        card.add(commentBtn);
        card.add(logoutBtn);

        // ================= LOGIC =================

        profileBtn.addActionListener(e -> {
            new CustomerEditProfileUI(customerID);
            dispose();
        });

        historyBtn.addActionListener(e -> {
            new CustomerServiceHistoryUI(customerID);
            dispose();
        });

        paymentBtn.addActionListener(e -> {
            new CustomerPaymentHistoryUI(customerID);
            dispose();
        });

        feedbackBtn.addActionListener(e -> {
            new CustomerViewFeedbackUI(customerID);
            dispose();
        });

        commentBtn.addActionListener(e -> {
            new CustomerProvideCommentUI(customerID);
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            new UI.MainUI();
            dispose();
        });

        setVisible(true);
    }

    // ================= INTERFACE =================

    @Override
    public void openMenu(String id) {
        new CustomerUI(id);
    }

    // ================= BUTTON CREATOR =================

    private JButton createButton(String text, int y) {

        JButton btn = new ModernButton(text);

        btn.setBounds(70, y, 220, 35);

        return btn;
    }

    // ================= GRADIENT BACKGROUND =================

    class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(20, 20, 40),
                    getWidth(), getHeight(),
                    new Color(0, 200, 255)
            );

            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ================= ROUNDED PANEL =================

    class RoundedPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color color) {

            this.radius = radius;
            this.bgColor = color;

            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // ================= MODERN BUTTON =================

    class ModernButton extends JButton {

        private static final long serialVersionUID = 1L;

        private boolean hovering = false;
        private boolean pressed = false;

        private Color bgColor = new Color(60, 60, 80);
        private Color hoverColor = new Color(0, 200, 255);

        public ModernButton(String text) {

            super(text);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));

            addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hovering = false;
                    pressed = false;
                    repaint();
                }

                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            // Shadow
            if (!pressed) {

                g2.setColor(new Color(0, 0, 0, 80));

                g2.fillRoundRect(
                        4,
                        4,
                        w - 4,
                        h - 4,
                        20,
                        20
                );
            }

            // Button Color
            if (pressed)
                g2.setColor(bgColor.darker());

            else if (hovering)
                g2.setColor(hoverColor);

            else
                g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,
                    0,
                    w - 4,
                    h - 4,
                    20,
                    20
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
    
    private String getCustomerName(String id) {

        try {

            java.io.BufferedReader br =
                    new java.io.BufferedReader(
                            new java.io.FileReader("customer.txt")
                    );

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data[0].equals(id)) {

                    br.close();

                    return data[1];
                }
            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "Customer";
    }
}