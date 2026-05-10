package CustomerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerProvideCommentUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private String customerID;

    JComboBox<String> appointmentBox;
    JComboBox<String> roleBox;
    JComboBox<Integer> ratingBox;

    JTextArea txtComment;

    public CustomerProvideCommentUI(String id) {

        this.customerID = id;

        setTitle("Provide Comment");

        setSize(700, 550);

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
                new Dimension(560,430)
        );

        card.setLayout(null);

        // =====================================================
        // Title
        // =====================================================
        JLabel title = new JLabel(
                "PROVIDE COMMENT",
                SwingConstants.CENTER
        );

        title.setBounds(130,20,300,35);

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        Icon titleIcon = resizeIcon(
                UIManager.getIcon(
                        "OptionPane.informationIcon"
                ),
                24,
                24
        );

        title.setIcon(titleIcon);

        title.setIconTextGap(10);

        card.add(title);

        // =====================================================
        // Appointment
        // =====================================================
        JLabel lblApp = createLabel("Appointment:");

        lblApp.setBounds(50,90,120,30);

        card.add(lblApp);

        appointmentBox = new JComboBox<>();

        appointmentBox.setBounds(180,90,280,35);

        styleComboBox(appointmentBox);

        card.add(appointmentBox);

        // =====================================================
        // Role
        // =====================================================
        JLabel lblRole = createLabel("Comment To:");

        lblRole.setBounds(50,140,120,30);

        card.add(lblRole);

        roleBox = new JComboBox<>(
                new String[]{
                        "Technician",
                        "CounterStaff"
                }
        );

        roleBox.setBounds(180,140,280,35);

        styleComboBox(roleBox);

        card.add(roleBox);

        // =====================================================
        // Comment
        // =====================================================
        JLabel lblComment = createLabel("Comment:");

        lblComment.setBounds(50,190,120,30);

        card.add(lblComment);

        txtComment = new JTextArea();

        txtComment.setBackground(
                new Color(45,45,60)
        );

        txtComment.setForeground(Color.WHITE);

        txtComment.setCaretColor(Color.WHITE);

        txtComment.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtComment.setLineWrap(true);

        txtComment.setWrapStyleWord(true);

        JScrollPane sp =
                new JScrollPane(txtComment);

        sp.setBounds(180,190,280,100);

        card.add(sp);

        // =====================================================
        // Rating
        // =====================================================
        JLabel lblRating = createLabel("Rating:");

        lblRating.setBounds(50,310,120,30);

        card.add(lblRating);

        ratingBox = new JComboBox<>(
                new Integer[]{1,2,3,4,5}
        );

        ratingBox.setBounds(180,310,280,35);

        styleComboBox(ratingBox);

        card.add(ratingBox);

        // =====================================================
        // Buttons
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

        saveBtn.setBounds(120,370,140,40);

        card.add(saveBtn);

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

        backBtn.setBounds(300,370,140,40);

        card.add(backBtn);

        // =====================================================
        // Load Appointments
        // =====================================================
        loadAppointments();

        appointmentBox.addActionListener(
                e -> loadExistingComment()
        );

        roleBox.addActionListener(
                e -> loadExistingComment()
        );

        saveBtn.addActionListener(
                e -> saveComment()
        );

        backBtn.addActionListener(e -> {

            new CustomerUI(customerID);

            dispose();
        });

        // =====================================================
        // Add Card
        // =====================================================
        bg.add(card);

        setVisible(true);
    }

    // =========================================================
    // Load Appointments
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

                String[] data = line.split(",");

                if (data.length >= 5 &&
                    data[1].trim().equals(customerID) &&
                    data[5].trim().equalsIgnoreCase("Done")) {

                    appointmentBox.addItem(data[0]);
                }
            }

        } catch (Exception e) {

            new ModernDialog(
                    this,
                    "Error loading appointments!"
            );
        }
    }

    // =========================================================
    // Load Existing Comment
    // =========================================================
    private void loadExistingComment() {

        String appID =
                (String) appointmentBox.getSelectedItem();

        String role =
                (String) roleBox.getSelectedItem();

        txtComment.setText("");

        ratingBox.setSelectedIndex(0);

        File file = new File("comment.txt");

        if (!file.exists())
            return;

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(file)
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 4 &&
                    data[0].equals(appID) &&
                    data[1].equals(customerID) &&
                    data[2].equals(role)) {

                    txtComment.setText(data[3]);

                    // Load Rating
                    if (data.length >= 5) {

                        try {

                            ratingBox.setSelectedItem(
                                    Integer.parseInt(data[4])
                            );

                        } catch (Exception e) {

                            ratingBox.setSelectedIndex(0);
                        }
                    }

                    return;
                }
            }

        } catch (Exception e) {

            new ModernDialog(
                    this,
                    "Error loading comment!"
            );
        }
    }

    // =========================================================
    // Save Comment
    // =========================================================
    private void saveComment() {

        String appID =
                (String) appointmentBox.getSelectedItem();

        String role =
                (String) roleBox.getSelectedItem();

        String comment =
                txtComment.getText().trim();

        int rating =
                (int) ratingBox.getSelectedItem();

        if (appID == null || comment.isEmpty()) {

            new ModernDialog(
                    this,
                    "Comment cannot be empty!"
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
                                     "comment.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 3 &&
                    data[0].equals(appID) &&
                    data[1].equals(customerID) &&
                    data[2].equals(role)) {

                    // Update Existing
                    line =
                            appID + "," +
                            customerID + "," +
                            role + "," +
                            comment + "," +
                            rating + "," +
                            today;

                    found = true;
                }

                list.add(line);
            }

        } catch (Exception e) {
            // file might not exist
        }

        // =====================================================
        // Add New
        // =====================================================
        if (!found) {

            list.add(
                    appID + "," +
                    customerID + "," +
                    role + "," +
                    comment + "," +
                    rating + "," +
                    today
            );
        }

        // =====================================================
        // Save File
        // =====================================================
        try (BufferedWriter bw =
                     new BufferedWriter(
                             new FileWriter(
                                     "comment.txt"
                             )
                     )) {

            for (String s : list) {

                bw.write(s);

                bw.newLine();
            }

            new ModernDialog(
                    this,
                    "Comment Saved Successfully!"
            );

        } catch (Exception e) {

            new ModernDialog(
                    this,
                    "Error saving comment!"
            );
        }
    }

    // =========================================================
    // Create Label
    // =========================================================
    private JLabel createLabel(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setForeground(Color.WHITE);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        return lbl;
    }

    // =========================================================
    // Style ComboBox
    // =========================================================
    private void styleComboBox(JComboBox<?> box) {

        // 主体
        box.setBackground(Color.WHITE);
        box.setForeground(Color.BLACK);

        box.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        // 下拉选项颜色
        UIManager.put("ComboBox.selectionBackground",
                new Color(0,200,255));

        UIManager.put("ComboBox.selectionForeground",
                Color.BLACK);

        // renderer
        DefaultListCellRenderer renderer =
                new DefaultListCellRenderer();

        renderer.setBackground(Color.WHITE);

        renderer.setForeground(Color.BLACK);

        box.setRenderer(renderer);

        // 箭头按钮颜色
        box.setBorder(
                BorderFactory.createLineBorder(
                        new Color(180,180,180)
                )
        );
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

            JLabel iconLabel = new JLabel(
                    resizeIcon(icon,30,30)
            );

            iconLabel.setBounds(20,30,30,30);

            panel.add(iconLabel);

            JLabel msg = new JLabel(
                    "<html>" + message + "</html>"
            );

            msg.setBounds(60,20,220,40);

            msg.setForeground(Color.WHITE);

            msg.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            panel.add(msg);

            JButton okBtn = new JButton("OK");

            okBtn.setBounds(70,80,100,30);

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

        Image img = ((ImageIcon) icon)
                .getImage()
                .getScaledInstance(
                        w,
                        h,
                        Image.SCALE_SMOOTH
                );

        return new ImageIcon(img);
    }
}