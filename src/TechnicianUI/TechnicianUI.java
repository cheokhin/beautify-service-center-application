package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import UI.MainUI;
import UI.UserDashboard;

public class TechnicianUI extends JFrame implements UserDashboard {

    private static final long serialVersionUID = 1L;

    private String technicianID;

    // Empty constructor for polymorphism
    public TechnicianUI() {}

    public TechnicianUI(String id) {

        this.technicianID = id;

        setTitle("Technician Dashboard");
        setSize(750, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ================= BACKGROUND =================

        JPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());

        add(bg);

        // ================= SIDEBAR =================

        JPanel sidebar = new RoundedPanel(
                25,
                new Color(35, 30, 15)
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
                g2.setColor(new Color(255, 200, 0, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());

                // Circle
                g2.setColor(new Color(255, 200, 0));
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

        // ================= TECHNICIAN INFO =================

        String technicianName = getTechnicianName(technicianID);

        JLabel nameLabel = new JLabel(technicianName);
        nameLabel.setBounds(30, 130, 140, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(technicianID);
        idLabel.setBounds(30, 160, 140, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("TECHNICIAN");
        roleLabel.setBounds(30, 195, 140, 25);
        roleLabel.setForeground(new Color(255, 200, 0));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(roleLabel);

        JLabel welcomeLabel = new JLabel("Ready For Work!");
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

        JPanel centerWrapper = new JPanel(
                new GridBagLayout()
        );

        centerWrapper.setOpaque(false);

        bg.add(centerWrapper, BorderLayout.CENTER);

        // ================= MENU CARD =================

        JPanel card = new RoundedPanel(
                25,
                new Color(45, 40, 20)
        );

        card.setPreferredSize(new Dimension(360, 350));
        card.setLayout(null);

        centerWrapper.add(card);

        // ================= TITLE =================

        JLabel title = new JLabel(
                " TECHNICIAN MENU",
                SwingConstants.CENTER
        );

        title.setBounds(70, 20, 220, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        card.add(title);

        // ================= BUTTONS =================

        JButton jobBtn = createButton(
                " View Assigned Jobs",
                70
        );

        JButton updateBtn = createButton(
                " Update Job Status",
                115
        );

        JButton profileBtn = createButton(
                " Edit Profile",
                160
        );

        JButton feedbackBtn = createButton(
                " Provide Feedback",
                205
        );

        JButton commentBtn = createButton(
                " View Comment",
                250
        );

        JButton logoutBtn = createButton(
                " Logout",
                295
        );

        card.add(jobBtn);
        card.add(updateBtn);
        card.add(profileBtn);
        card.add(feedbackBtn);
        card.add(commentBtn);
        card.add(logoutBtn);

        // ================= LOGIC =================

        jobBtn.addActionListener(e -> {
            new TechnicianJobUI(technicianID);
            dispose();
        });

        updateBtn.addActionListener(e -> {
            new UpdateJobStatusUI(technicianID);
            dispose();
        });

        profileBtn.addActionListener(e -> {
            new EditTechnicianProfileUI(technicianID);
            dispose();
        });

        feedbackBtn.addActionListener(e -> {
            new ProvideFeedbackUI(technicianID);
            dispose();
        });

        commentBtn.addActionListener(e -> {
            new ViewCommentUI(technicianID);
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            new MainUI();
            dispose();
        });

        setVisible(true);
    }

    // ================= GET TECHNICIAN NAME =================

    private String getTechnicianName(String id) {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader("technician.txt")
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

        return "Technician";
    }

    // ================= INTERFACE =================

    @Override
    public void openMenu(String id) {

        new TechnicianUI(id);
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
                    0,
                    0,
                    new Color(30, 25, 10),

                    getWidth(),
                    getHeight(),

                    new Color(255, 200, 0)
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
                new Color(80, 70, 30);

        private Color hoverColor =
                new Color(255, 200, 0);

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