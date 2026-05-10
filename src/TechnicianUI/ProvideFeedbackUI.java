package TechnicianUI;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class ProvideFeedbackUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private String technicianID;

    JComboBox<String> appointmentBox;
    JTextArea txtFeedback;

    public ProvideFeedbackUI(String id) {

        this.technicianID = id;

        setTitle("Provide Feedback");

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
        // Main Card
        // =====================================================
        JPanel card = new RoundedPanel(
                30,
                new Color(35,35,50)
        );

        card.setPreferredSize(
                new Dimension(420,380)
        );

        card.setLayout(null);

        // =====================================================
        // Title
        // =====================================================
        JLabel lblTitle = new JLabel(
                "PROVIDE FEEDBACK",
                SwingConstants.CENTER
        );

        lblTitle.setBounds(80,20,260,35);

        lblTitle.setForeground(Color.WHITE);

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        card.add(lblTitle);

        // =====================================================
        // Appointment Label
        // =====================================================
        JLabel lblApp = new JLabel("Appointment:");

        lblApp.setBounds(40,90,120,30);

        lblApp.setForeground(Color.WHITE);

        lblApp.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        card.add(lblApp);

        // =====================================================
        // ComboBox
        // =====================================================
        appointmentBox = new JComboBox<>();

        appointmentBox.setBounds(160,90,200,32);

        appointmentBox.setBackground(Color.WHITE);

        appointmentBox.setForeground(Color.BLACK);

        appointmentBox.setFocusable(false);

        card.add(appointmentBox);

        // =====================================================
        // Feedback Label
        // =====================================================
        JLabel lblFeedback = new JLabel("Feedback:");

        lblFeedback.setBounds(40,140,120,30);

        lblFeedback.setForeground(Color.WHITE);

        lblFeedback.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        card.add(lblFeedback);

        // =====================================================
        // TextArea
        // =====================================================
        txtFeedback = new JTextArea();

        txtFeedback.setLineWrap(true);

        txtFeedback.setWrapStyleWord(true);

        txtFeedback.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane scroll =
                new JScrollPane(txtFeedback);

        scroll.setBounds(160,140,200,100);

        card.add(scroll);

        // =====================================================
        // Save Button
        // =====================================================
        Icon saveIcon = resizeIcon(
                UIManager.getIcon(
                        "FileView.floppyDriveIcon"
                ),
                18,
                18
        );

        JButton saveBtn =
                new ModernButton(
                        " Save",
                        saveIcon
                );

        saveBtn.setBounds(70,290,120,42);

        card.add(saveBtn);

        // =====================================================
        // Back Button
        // =====================================================
        Icon backIcon = resizeIcon(
                UIManager.getIcon(
                        "OptionPane.errorIcon"
                ),
                18,
                18
        );

        JButton backBtn =
                new ModernButton(
                        " Back",
                        backIcon
                );

        backBtn.setBounds(220,290,120,42);

        card.add(backBtn);

        // =====================================================
        // Load Data
        // =====================================================
        loadAppointments();

        appointmentBox.addActionListener(
                e -> loadExistingFeedback()
        );

        saveBtn.addActionListener(
                e -> saveFeedback()
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
    // Load Appointment
    // =========================================================
    private void loadAppointments() {

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "appointment.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data =
                        line.split(",");

                if (data.length >= 9 &&
                    data[8].equals(technicianID) &&
                    data[5].equalsIgnoreCase("Done")) {

                    appointmentBox.addItem(data[0]);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // Load Existing Feedback
    // =========================================================
    private void loadExistingFeedback() {

        String selectedApp =
                (String) appointmentBox.getSelectedItem();

        txtFeedback.setText("");

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "feedback.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data =
                        line.split(",");

                if (data.length >= 3 &&
                    data[0].equals(selectedApp) &&
                    data[1].equals(technicianID)) {

                    if (data.length >= 4) {

                        txtFeedback.setText(
                                data[2]
                        );

                    } else {

                        txtFeedback.setText(
                                data[2]
                        );
                    }

                    return;
                }
            }

        } catch (Exception e) {

            // ignore
        }
    }

    // =========================================================
    // Save Feedback
    // =========================================================
    private void saveFeedback() {

        String selectedApp =
                (String) appointmentBox.getSelectedItem();

        String feedback =
                txtFeedback.getText().trim();

        if (selectedApp == null ||
            feedback.isEmpty()) {

            new ModernDialog(
                    this,
                    "Feedback cannot be empty!"
            );

            return;
        }

        String today =
                LocalDate.now().toString();

        ArrayList<String> list =
                new ArrayList<>();

        boolean found = false;

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "feedback.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data =
                        line.split(",");

                if (data.length >= 2 &&
                    data[0].equals(selectedApp) &&
                    data[1].equals(technicianID)) {

                    line =
                            selectedApp + "," +
                            technicianID + "," +
                            feedback + "," +
                            today;

                    found = true;
                }

                list.add(line);
            }

        } catch (Exception e) {

            // ignore
        }

        // add new
        if (!found) {

            list.add(
                    selectedApp + "," +
                    technicianID + "," +
                    feedback + "," +
                    today
            );
        }

        // write file
        try (BufferedWriter bw =
                     new BufferedWriter(
                             new FileWriter(
                                     "feedback.txt"
                             )
                     )) {

            for (String s : list) {

                bw.write(s);

                bw.newLine();
            }

            new ModernDialog(
                    this,
                    "Feedback Saved!"
            );

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
                    SwingConstants.CENTER
            );

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

            // Main
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
                            (int)(120 * scale[0]);

                    int h =
                            (int)(200 * scale[0]);

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