package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Appointment extends JFrame {

    private static final long serialVersionUID = 1L;

    private String counterID;

    // =========================================================
    // Constructor
    // =========================================================
    public Appointment(String counterID) {

        this.counterID = counterID;

        setTitle("Appointment Menu");

        setSize(600,500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =====================================================
        // Background
        // =====================================================
        JPanel bg = new GradientPanel();

        bg.setLayout(new GridBagLayout());

        add(bg);

        // =====================================================
        // Card
        // =====================================================
        JPanel card = new RoundedPanel(
                30,
                new Color(35,35,50)
        );

        card.setPreferredSize(
                new Dimension(380,350)
        );

        card.setLayout(null);

        // =====================================================
        // Title
        // =====================================================
        JLabel title = new JLabel(
                "APPOINTMENTS",
                SwingConstants.CENTER
        );

        title.setBounds(60,25,250,35);

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        card.add(title);

        // =====================================================
        // Icons
        // =====================================================
        Icon createIcon = resizeIcon(
                UIManager.getIcon("FileChooser.newFolderIcon"),
                18,
                18
        );

        Icon updateIcon = resizeIcon(
                UIManager.getIcon("FileView.floppyDriveIcon"),
                18,
                18
        );

        Icon assignIcon = resizeIcon(
                UIManager.getIcon("FileView.computerIcon"),
                18,
                18
        );

        Icon returnIcon = resizeIcon(
                UIManager.getIcon("OptionPane.informationIcon"),
                18,
                18
        );

        // =====================================================
        // Buttons
        // =====================================================
        JButton createBtn =
                new ModernButton(
                        " Create Appointment",
                        createIcon
                );

        JButton updateBtn =
                new ModernButton(
                        " Update Appointment",
                        updateIcon
                );

        JButton assignBtn =
                new ModernButton(
                        " Assign Technician",
                        assignIcon
                );

        JButton returnBtn =
                new ModernButton(
                        " Return",
                        returnIcon
                );

        createBtn.setBounds(80,90,220,42);

        updateBtn.setBounds(80,145,220,42);

        assignBtn.setBounds(80,200,220,42);

        returnBtn.setBounds(80,270,220,42);

        card.add(createBtn);

        card.add(updateBtn);

        card.add(assignBtn);

        card.add(returnBtn);

        // =====================================================
        // Actions
        // =====================================================
        createBtn.addActionListener(e -> {

            // 🔥 FIXED counterID
            new ManageAppointment(this.counterID, "CREATE");

            dispose();
        });

        updateBtn.addActionListener(e -> {

            // 🔥 FIXED counterID
            new ManageAppointment(this.counterID, "UPDATE");

            dispose();
        });

        assignBtn.addActionListener(e -> {

            // 🔥 FIXED counterID
            new ManageAppointment(this.counterID, "ASSIGN");

            dispose();
        });

        returnBtn.addActionListener(e -> {

            // 🔥 FIXED counterID
            new CounterStaffMenu(this.counterID);

            dispose();
        });

        bg.add(card);

        setVisible(true);
    }

    // =========================================================
    // Gradient Background
    // =========================================================
    class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(
                    0,
                    0,
                    new Color(20,20,40),

                    getWidth(),
                    getHeight(),

                    new Color(0,200,255)
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

    // =========================================================
    // Rounded Panel
    // =========================================================
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

    // =========================================================
    // Modern Button
    // =========================================================
    class ModernButton extends JButton {

        private static final long serialVersionUID = 1L;

        private boolean hovering = false;

        private boolean pressed = false;

        private Color bgColor =
                new Color(60,60,80);

        private Color hoverColor =
                new Color(0,200,255);

        public ModernButton(
                String text,
                Icon icon
        ) {

            super(text, icon);

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

            setHorizontalAlignment(
                    SwingConstants.LEFT
            );

            setIconTextGap(12);

            addMouseListener(
                    new MouseAdapter() {

                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            hovering = true;

                            repaint();
                        }

                        public void mouseExited(
                                MouseEvent e
                        ) {

                            hovering = false;

                            pressed = false;

                            repaint();
                        }

                        public void mousePressed(
                                MouseEvent e
                        ) {

                            pressed = true;

                            repaint();
                        }

                        public void mouseReleased(
                                MouseEvent e
                        ) {

                            pressed = false;

                            repaint();
                        }
                    }
            );
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

            if (!pressed) {

                g2.setColor(
                        new Color(0,0,0,80)
                );

                g2.fillRoundRect(
                        4,
                        4,
                        w-4,
                        h-4,
                        20,
                        20
                );
            }

            if (pressed)
                g2.setColor(bgColor.darker());

            else if (hovering)
                g2.setColor(hoverColor);

            else
                g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,
                    0,
                    w-4,
                    h-4,
                    20,
                    20
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================================
    // Resize Icon
    // =========================================================
    private Icon resizeIcon(
            Icon icon,
            int w,
            int h
    ) {

        Image img =
                ((ImageIcon) icon)
                        .getImage()
                        .getScaledInstance(
                                w,
                                h,
                                Image.SCALE_SMOOTH
                        );

        return new ImageIcon(img);
    }
}