package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import UI.UserDashboard;

public class CounterStaffMenu extends JFrame implements UserDashboard {

    private static final long serialVersionUID = 1L;

    private String counterID;

    // Empty constructor for polymorphism
    public CounterStaffMenu() {}

    // ================= CONSTRUCTOR =================

    public CounterStaffMenu(String id) {

        this.counterID = id;

        setTitle("Counter Staff Dashboard");
        setSize(750, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ================= BACKGROUND =================

        JPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());

        add(bg);

        // ================= SIDEBAR =================

        JPanel sidebar = new RoundedPanel(
                25,
                new Color(12, 28, 32)
        );

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

                // Glow
                g2.setColor(new Color(0, 220, 255, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());

                // Circle
                g2.setColor(new Color(0, 220, 255));
                g2.fillOval(5, 5, getWidth() - 10, getHeight() - 10);

                // Head
                g2.setColor(Color.WHITE);
                g2.fillOval(32, 22, 26, 26);

                // Body
                g2.fillRoundRect(24, 50, 42, 28, 20, 20);
            }
        };

        avatar.setBounds(55, 30, 90, 90);
        avatar.setOpaque(false);

        sidebar.add(avatar);

        // ================= USER INFO =================

        String counterName = getCounterName(this.counterID);

        JLabel nameLabel = new JLabel(counterName);
        nameLabel.setBounds(30, 130, 140, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(this.counterID);
        idLabel.setBounds(30, 160, 140, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("COUNTER STAFF");
        roleLabel.setBounds(20, 195, 160, 25);
        roleLabel.setForeground(new Color(0, 220, 255));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(roleLabel);

        JLabel welcomeLabel = new JLabel("Front Desk Ready!");
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

        // ================= CENTER =================

        JPanel centerWrapper =
                new JPanel(new GridBagLayout());

        centerWrapper.setOpaque(false);

        bg.add(centerWrapper, BorderLayout.CENTER);

        // ================= MENU CARD =================

        JPanel card = new RoundedPanel(
                25,
                new Color(18, 40, 45)
        );

        card.setPreferredSize(new Dimension(360, 320));
        card.setLayout(null);

        centerWrapper.add(card);

        // ================= TITLE =================

        JLabel title = new JLabel(
                " COUNTER STAFF MENU",
                SwingConstants.CENTER
        );

        title.setBounds(50, 20, 260, 30);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        title.setForeground(Color.WHITE);

        card.add(title);

        // ================= BUTTONS =================

        JButton managementBtn =
                createButton(" Management", 70);

        JButton profileBtn =
                createButton(" Edit Profile", 115);

        JButton appointmentBtn =
                createButton(" Appointments", 160);

        JButton paymentBtn =
                createButton(" Payment", 205);

        JButton logoutBtn =
                createButton(" Logout", 250);

        card.add(managementBtn);
        card.add(profileBtn);
        card.add(appointmentBtn);
        card.add(paymentBtn);
        card.add(logoutBtn);

        // ================= ACTIONS =================

        managementBtn.addActionListener(e -> {

            new ManagementMenu(this.counterID);

            dispose();
        });

        profileBtn.addActionListener(e -> {

            new EditCounterProfile(this.counterID);

            dispose();
        });

        appointmentBtn.addActionListener(e -> {

            new Appointment(this.counterID);

            dispose();
        });

        paymentBtn.addActionListener(e -> {

            new PaymentMenu(this.counterID);

            dispose();
        });

        logoutBtn.addActionListener(e -> {

            new UI.MainUI();

            dispose();
        });

        setVisible(true);
    }

    // ================= GET NAME =================

    private String getCounterName(String id) {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader("counter.txt")
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

        return "Counter Staff";
    }

    // ================= INTERFACE =================

    @Override
    public void openMenu(String id) {

        new CounterStaffMenu(id);
    }

    // ================= BUTTON CREATOR =================

    private JButton createButton(String text, int y) {

        JButton btn = new ModernButton(text);

        btn.setBounds(70, y, 220, 35);

        return btn;
    }

    // ================= GRADIENT PANEL =================

    class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(
                    0,
                    0,
                    new Color(10, 20, 25),

                    getWidth(),
                    getHeight(),

                    new Color(0, 220, 255)
            );

            g2.setPaint(gp);

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
        }
    }

    // ================= ROUNDED PANEL =================

    class RoundedPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private int radius;
        private Color bgColor;

        public RoundedPanel(
                int radius,
                Color color
        ) {

            this.radius = radius;
            this.bgColor = color;

            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

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

        private Color bgColor =
                new Color(25, 60, 68);

        private Color hoverColor =
                new Color(0, 220, 255);

        public ModernButton(String text) {

            super(text);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            setForeground(Color.WHITE);

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            14
                    )
            );

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

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            // Shadow
            if (!pressed) {

                g2.setColor(
                        new Color(0, 0, 0, 80)
                );

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

                g2.setColor(
                        bgColor.darker()
                );

            else if (hovering)

                g2.setColor(
                        hoverColor
                );

            else

                g2.setColor(
                        bgColor
                );

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
}