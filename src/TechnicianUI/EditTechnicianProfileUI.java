package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class EditTechnicianProfileUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private String technicianID;

    JTextField txtID, txtName, txtUsername;
    JTextField txtEmail, txtPhone, txtSkill, txtExp, txtDate;

    JPasswordField txtPassword;

    public EditTechnicianProfileUI(String id) {

        this.technicianID = id;

        setTitle("Edit Technician Profile");

        setSize(750, 700);

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
                new Dimension(580,620)
        );

        card.setLayout(null);

        // =====================================================
        // Title
        // =====================================================
        JLabel title = new JLabel(
                "EDIT TECHNICIAN PROFILE",
                SwingConstants.CENTER
        );

        title.setBounds(90,20,400,35);

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
                        "FileView.computerIcon"
                ),
                24,
                24
        );

        title.setIcon(titleIcon);

        title.setIconTextGap(10);

        card.add(title);

        int y = 90;

        // =====================================================
        // ID
        // =====================================================
        addLabel(card, "ID:", y);

        txtID = addField(card, y);

        txtID.setEditable(false);

        y += 50;

        // =====================================================
        // Name
        // =====================================================
        addLabel(card, "Name:", y);

        txtName = addField(card, y);

        y += 50;

        // =====================================================
        // Username
        // =====================================================
        addLabel(card, "Username:", y);

        txtUsername = addField(card, y);

        y += 50;

        // =====================================================
        // Password
        // =====================================================
        addLabel(card, "Password:", y);

        txtPassword = new JPasswordField();

        styleField(txtPassword);

        txtPassword.setBounds(220,y,250,35);

        txtPassword.setEchoChar('•');

        card.add(txtPassword);

        // 👁 Show / Hide Button
        JButton eyeBtn = new JButton("Show");

        eyeBtn.setBounds(480,y,70,35);

        eyeBtn.setFocusPainted(false);

        eyeBtn.setBackground(
                new Color(60,60,80)
        );

        eyeBtn.setForeground(Color.WHITE);

        eyeBtn.setBorderPainted(false);

        eyeBtn.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        eyeBtn.addActionListener(e -> {

            if (txtPassword.getEchoChar() == (char)0) {

                txtPassword.setEchoChar('•');

                eyeBtn.setText("Show");

            } else {

                txtPassword.setEchoChar((char)0);

                eyeBtn.setText("Hide");
            }
        });

        card.add(eyeBtn);

        y += 50;

        // =====================================================
        // Email
        // =====================================================
        addLabel(card, "Email:", y);

        txtEmail = addField(card, y);

        y += 50;

        // =====================================================
        // Phone
        // =====================================================
        addLabel(card, "Phone:", y);

        txtPhone = addField(card, y);

        y += 50;

        // =====================================================
        // Skill
        // =====================================================
        addLabel(card, "Skill:", y);

        txtSkill = addField(card, y);

        y += 50;

        // =====================================================
        // Experience
        // =====================================================
        addLabel(card, "Experience:", y);

        txtExp = addField(card, y);

        y += 50;

        // =====================================================
        // Date Joined
        // =====================================================
        addLabel(card, "Date Joined:", y);

        txtDate = addField(card, y);

        txtDate.setEditable(false);

        y += 70;

        // =====================================================
        // Buttons
        // =====================================================
        Icon updateIcon = resizeIcon(
                UIManager.getIcon(
                        "FileView.floppyDriveIcon"
                ),
                18,
                18
        );

        JButton updateBtn =
                new ModernButton(
                        " Update",
                        updateIcon
                );

        updateBtn.setBounds(140,y,130,40);

        card.add(updateBtn);

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

        backBtn.setBounds(320,y,130,40);

        card.add(backBtn);

        // =====================================================
        // Load Profile
        // =====================================================
        loadProfile();

        // =====================================================
        // Button Actions
        // =====================================================
        updateBtn.addActionListener(
                e -> updateProfile()
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
    // Add Label
    // =========================================================
    private void addLabel(
            JPanel panel,
            String text,
            int y
    ) {

        JLabel label = new JLabel(text);

        label.setBounds(70,y,120,30);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        panel.add(label);
    }

    // =========================================================
    // Add TextField
    // =========================================================
    private JTextField addField(
            JPanel panel,
            int y
    ) {

        JTextField field = new JTextField();

        styleField(field);

        field.setBounds(220,y,250,35);

        panel.add(field);

        return field;
    }

    // =========================================================
    // Style Field
    // =========================================================
    private void styleField(JTextField tf) {

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
    // Load Profile
    // =========================================================
    private void loadProfile() {

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "technician.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data[0].equals(technicianID)) {

                    txtID.setText(data[0]);

                    txtName.setText(data[1]);

                    txtUsername.setText(data[2]);

                    txtPassword.setText(data[3]);

                    txtEmail.setText(data[4]);

                    txtPhone.setText(data[5]);

                    txtSkill.setText(data[6]);

                    txtExp.setText(data[7]);

                    txtDate.setText(data[8]);

                    break;
                }
            }

        } catch (Exception e) {

            new ModernDialog(
                    this,
                    "Error loading profile!"
            );
        }
    }

    // =========================================================
    // Update Profile
    // =========================================================
    private void updateProfile() {

        String name =
                txtName.getText().trim();

        String username =
                txtUsername.getText().trim();

        String password =
                new String(
                        txtPassword.getPassword()
                ).trim();

        String email =
                txtEmail.getText().trim();

        String phone =
                txtPhone.getText().trim();

        String skill =
                txtSkill.getText().trim();

        String exp =
                txtExp.getText().trim();

        // =====================================================
        // Empty Validation
        // =====================================================
        if (name.isEmpty() ||
            username.isEmpty() ||
            password.isEmpty() ||
            email.isEmpty() ||
            phone.isEmpty() ||
            skill.isEmpty() ||
            exp.isEmpty()) {

            new ModernDialog(
                    this,
                    "All fields must be filled!"
            );

            return;
        }

        // =====================================================
        // Email Validation
        // =====================================================
        if (!email.contains("@")) {

            new ModernDialog(
                    this,
                    "Email must contain @"
            );

            return;
        }

        // =====================================================
        // Phone Validation
        // =====================================================
        if (!phone.matches(
                "0\\d{2}-\\d{3}-\\d{4}"
        )) {

            new ModernDialog(
                    this,
                    "Phone format: 0XX-XXX-XXXX"
            );

            return;
        }

        // =====================================================
        // Experience Validation
        // =====================================================
        if (!exp.matches("\\d+")) {

            new ModernDialog(
                    this,
                    "Experience must be numeric!"
            );

            return;
        }

        ArrayList<String> list =
                new ArrayList<>();

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "technician.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data[0].equals(technicianID)) {

                    line =
                            txtID.getText() + "," +
                            name + "," +
                            username + "," +
                            password + "," +
                            email + "," +
                            phone + "," +
                            skill + "," +
                            exp + "," +
                            txtDate.getText();
                }

                list.add(line);
            }

        } catch (Exception e) {

            new ModernDialog(
                    this,
                    "Error updating profile!"
            );
        }

        // =====================================================
        // Save File
        // =====================================================
        try (BufferedWriter bw =
                     new BufferedWriter(
                             new FileWriter(
                                     "technician.txt"
                             )
                     )) {

            for (String s : list) {

                bw.write(s);

                bw.newLine();
            }

            new ModernDialog(
                    this,
                    "Profile Updated Successfully!"
            );

        } catch (Exception e) {

            new ModernDialog(
                    this,
                    "Error saving profile!"
            );
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