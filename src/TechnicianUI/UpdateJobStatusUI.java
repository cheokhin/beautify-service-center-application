package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class UpdateJobStatusUI extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField appField;
    JComboBox<String> statusBox;

    String technicianID;

    public UpdateJobStatusUI(String id) {

        this.technicianID = id;

        setTitle("Update Job Status");

        setSize(600,420);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =====================================================
        // Background
        // =====================================================
        JPanel bg = new GradientPanel();

        bg.setLayout(new GridBagLayout());

        add(bg);

        // =====================================================
        // Main Card
        // =====================================================
        JPanel card = new RoundedPanel(
                30,
                new Color(35,35,50)
        );

        card.setPreferredSize(
                new Dimension(420,280)
        );

        card.setLayout(null);

        // =====================================================
        // Title
        // =====================================================
        JLabel title = new JLabel(
                "UPDATE JOB STATUS",
                SwingConstants.CENTER
        );

        title.setBounds(60,20,300,35);

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
        // Appointment ID
        // =====================================================
        JLabel appLabel =
                new JLabel("Appointment ID:");

        appLabel.setBounds(40,90,120,25);

        appLabel.setForeground(Color.WHITE);

        appLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        card.add(appLabel);

        appField = new JTextField();

        appField.setBounds(170,90,180,30);

        styleField(appField);

        card.add(appField);

        // =====================================================
        // Status
        // =====================================================
        JLabel statusLabel =
                new JLabel("Job Status:");

        statusLabel.setBounds(40,140,120,25);

        statusLabel.setForeground(Color.WHITE);

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        card.add(statusLabel);

        String[] status = {
                "Pending",
                "In Progress",
                "Done"
        };

        statusBox =
                new JComboBox<>(status);

        statusBox.setBounds(170,140,180,30);

        statusBox.setBackground(Color.WHITE);

        statusBox.setForeground(Color.BLACK);

        statusBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        card.add(statusBox);

        // =====================================================
        // Buttons
        // =====================================================
        Icon saveIcon = resizeIcon(
                UIManager.getIcon("FileView.floppyDriveIcon"),
                18,
                18
        );

        JButton updateBtn =
                new ModernButton(" Update", saveIcon);

        updateBtn.setBounds(60,210,130,38);

        card.add(updateBtn);

        Icon backIcon = resizeIcon(
                UIManager.getIcon("OptionPane.errorIcon"),
                18,
                18
        );

        JButton backBtn =
                new ModernButton(" Back", backIcon);

        backBtn.setBounds(230,210,130,38);

        card.add(backBtn);

        // =====================================================
        // Actions
        // =====================================================
        updateBtn.addActionListener(
                e -> updateStatus()
        );

        backBtn.addActionListener(e -> {

            new TechnicianUI(technicianID);

            dispose();
        });

        // =====================================================
        // Add Card
        // =====================================================
        bg.add(card);

        setVisible(true);
    }

    // =========================================================
    // Field Style
    // =========================================================
    private void styleField(
            JTextField tf
    ) {

        tf.setBackground(Color.WHITE);

        tf.setForeground(Color.BLACK);

        tf.setCaretColor(Color.BLACK);

        tf.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tf.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(180,180,180)
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );
    }

    // =========================================================
    // Update Status
    // =========================================================
    private void updateStatus() {

        if (appField.getText().trim().isEmpty()) {

            new ModernDialog(
                    this,
                    "Please enter Appointment ID!"
            );

            return;
        }

        ArrayList<String> list =
                new ArrayList<>();

        boolean found = false;

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "appointment.txt"
                             )
                     )) {

            String line;

            String targetID =
                    appField.getText().trim();

            while ((line = br.readLine()) != null) {

                if (line.startsWith(
                        "AppointmentID"
                )) {

                    list.add(line);

                    continue;
                }

                String[] data =
                        line.split(",");

                // data[8] = TechnicianID
                if (data[0].equals(targetID)
                        &&
                    data[8].equals(technicianID)) {

                    data[5] =
                            statusBox
                                    .getSelectedItem()
                                    .toString();

                    line =
                            String.join(",", data);

                    found = true;
                }

                list.add(line);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        try (BufferedWriter bw =
                     new BufferedWriter(
                             new FileWriter(
                                     "appointment.txt"
                             )
                     )) {

            for (String s : list) {

                bw.write(s);

                bw.newLine();
            }

            if (found) {

                new ModernDialog(
                        this,
                        "Status Updated!"
                );

            } else {

                new ModernDialog(
                        this,
                        "Appointment Not Found!"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // Gradient Background
    // =========================================================
    class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g;

            GradientPaint gp =
                    new GradientPaint(

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

        public ModernButton(String text, Icon icon) {

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

            setHorizontalAlignment(SwingConstants.CENTER);

            setIconTextGap(10);

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

            // Shadow
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

            // Main Color
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
    // Modern Dialog
    // =========================================================
    class ModernDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        public ModernDialog(
                JFrame parent,
                String message
        ) {

            super(parent, true);

            setUndecorated(true);

            setSize(320,160);

            setLocationRelativeTo(parent);

            setOpacity(0f);

            JPanel panel = new JPanel();

            panel.setLayout(null);

            panel.setBackground(
                    new Color(30,30,30)
            );

            panel.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(0,200,255),
                            2
                    )
            );

            add(panel);

            Icon icon =
                    UIManager.getIcon(
                            "OptionPane.informationIcon"
                    );

            JLabel iconLabel =
                    new JLabel(
                            resizeIcon(
                                    icon,
                                    30,
                                    30
                            )
                    );

            iconLabel.setBounds(
                    20,
                    30,
                    30,
                    30
            );

            panel.add(iconLabel);

            JLabel msg =
                    new JLabel(
                            "<html>" +
                            message +
                            "</html>"
                    );

            msg.setBounds(
                    60,
                    20,
                    220,
                    40
            );

            msg.setForeground(Color.WHITE);

            msg.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            panel.add(msg);

            JButton okBtn =
                    new JButton("OK");

            okBtn.setBounds(
                    70,
                    80,
                    100,
                    30
            );

            okBtn.setFocusPainted(false);

            okBtn.setBackground(
                    new Color(60,60,80)
            );

            okBtn.setForeground(Color.WHITE);

            okBtn.addActionListener(
                    e -> dispose()
            );

            panel.add(okBtn);

            animate();

            setVisible(true);
        }

        // =====================================================
        // Animation
        // =====================================================
        private void animate() {

            Timer timer =
                    new Timer(15, null);

            final float[] opacity = {0f};

            final double[] scale = {0.8};

            timer.addActionListener(e -> {

                if (opacity[0] < 1f) {

                    opacity[0] += 0.08f;

                    scale[0] += 0.03;

                    setOpacity(
                            Math.min(
                                    opacity[0],
                                    1f
                            )
                    );

                    int w =
                            (int)(200 * scale[0]);

                    int h =
                            (int)(120 * scale[0]);

                    setSize(w,h);

                    setLocationRelativeTo(
                            getParent()
                    );

                } else {

                    timer.stop();
                }
            });

            timer.start();
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