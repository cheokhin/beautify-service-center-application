package CustomerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class CustomerEditProfileUI extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField nameField, userField, emailField, phoneField;
    JTextField carField, plateField, yearField;

    JPasswordField passField;

    JLabel idLabel, typeLabel;

    String customerID;

    public CustomerEditProfileUI(String id) {

        this.customerID = id;

        setTitle("Edit Profile");

        setSize(750,650);

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
                new Dimension(580,540)
        );

        card.setLayout(null);

        // =====================================================
        // Title
        // =====================================================
        JLabel title = new JLabel(
                "EDIT PROFILE",
                SwingConstants.CENTER
        );

        title.setBounds(150,20,280,35);

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
                        "FileView.fileIcon"
                ),
                24,
                24
        );

        title.setIcon(titleIcon);

        title.setIconTextGap(10);

        card.add(title);

        // =====================================================
        // Labels & Fields
        // =====================================================
        addLabel(card, "ID:", 80);

        idLabel = createValueLabel();

        idLabel.setBounds(220,80,250,30);

        card.add(idLabel);

        addLabel(card, "Name:", 120);

        nameField = addField(card,120);

        addLabel(card, "Username:", 160);

        userField = addField(card,160);

        addLabel(card, "Password:", 200);

        passField = new JPasswordField();
        styleField(passField);
        passField.setBounds(220,200,215,35);  // 缩短宽度留位给眼睛按钮
        card.add(passField);

        JButton eyeBtn = new JButton("👁");
        eyeBtn.setBounds(438,200,32,35);      // 紧贴在 passField 右边
        eyeBtn.setFocusPainted(false);
        eyeBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        eyeBtn.addActionListener(e -> {
            if (passField.getEchoChar() == (char)0) {
                passField.setEchoChar('•');
            } else {
                passField.setEchoChar((char)0);
            }
        });
        card.add(eyeBtn);

        passField.setBounds(220,200,250,35);

        card.add(passField);

        addLabel(card, "Email:", 240);

        emailField = addField(card,240);

        addLabel(card, "Phone:", 280);

        phoneField = addField(card,280);

        addLabel(card, "Car Model:", 320);

        carField = addField(card,320);

        addLabel(card, "Plate No:", 360);

        plateField = addField(card,360);

        addLabel(card, "Year:", 400);

        yearField = addField(card,400);

        addLabel(card, "Type:", 440);

        typeLabel = createValueLabel();

        typeLabel.setBounds(220,440,250,30);

        card.add(typeLabel);

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

        saveBtn.setBounds(140,490,130,40);

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

        backBtn.setBounds(310,490,130,40);

        card.add(backBtn);

        // =====================================================
        // Load Data
        // =====================================================
        loadData();

        // =====================================================
        // Button Actions
        // =====================================================
        saveBtn.addActionListener(
                e -> updateData()
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

        JTextField tf = new JTextField();

        styleField(tf);

        tf.setBounds(220,y,250,35);

        panel.add(tf);

        return tf;
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
    // Create Value Label
    // =========================================================
    private JLabel createValueLabel() {

        JLabel lbl = new JLabel();

        lbl.setForeground(Color.WHITE);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        return lbl;
    }

    // =========================================================
    // Load Data
    // =========================================================
    private void loadData() {

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "customer.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data[0].equals(customerID)) {

                    idLabel.setText(data[0]);

                    nameField.setText(data[1]);

                    userField.setText(data[2]);

                    passField.setText(data[3]);

                    emailField.setText(data[4]);

                    phoneField.setText(data[5]);

                    carField.setText(data[6]);

                    plateField.setText(data[7]);

                    yearField.setText(data[8]);

                    typeLabel.setText(data[9]);

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
    // Update Data
    // =========================================================
    private void updateData() {
    	
    	String name = nameField.getText().trim();

    	String username = userField.getText().trim();

    	String password =
    	        new String(
    	                passField.getPassword()
    	        ).trim();

    	String email =
    	        emailField.getText().trim();

    	String phone =
    	        phoneField.getText().trim();

    	String car =
    	        carField.getText().trim();

    	String plate =
    	        plateField.getText().trim();

    	String year =
    	        yearField.getText().trim();

    	// ======================================
    	// Empty Validation
    	// ======================================
    	if (name.isEmpty() ||
    	    username.isEmpty() ||
    	    password.isEmpty() ||
    	    email.isEmpty() ||
    	    phone.isEmpty() ||
    	    car.isEmpty() ||
    	    plate.isEmpty() ||
    	    year.isEmpty()) {

    	    new ModernDialog(
    	            this,
    	            "All fields are required!"
    	    );

    	    return;
    	}

    	// ======================================
    	// Email Validation
    	// ======================================
    	if (!email.contains("@")) {

    	    new ModernDialog(
    	            this,
    	            "Email must contain @"
    	    );

    	    return;
    	}

    	// ======================================
    	// Phone Validation
    	// Format: 0XX-XXX-XXXX
    	// ======================================
    	if (!phone.matches(
    	        "0\\d{2}-\\d{3}-\\d{4}"
    	)) {

    	    new ModernDialog(
    	            this,
    	            "Phone format: 0XX-XXX-XXXX"
    	    );

    	    return;
    	}

    	// ======================================
    	// Year Validation
    	// 4 digits only
    	// ======================================
    	if (!year.matches("\\d{4}")) {

    	    new ModernDialog(
    	            this,
    	            "Year must be 4 digits"
    	    );

    	    return;
    	}
    	
        ArrayList<String> list =
                new ArrayList<>();

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(
                                     "customer.txt"
                             )
                     )) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data[0].equals(customerID)) {

                    line =
                            data[0] + "," +
                            name + "," +
                            username + "," +
                            password + "," +
                            email + "," +
                            phone + "," +
                            car + "," +
                            plate + "," +
                            year +  "," +
                            data[9];
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
                                     "customer.txt"
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