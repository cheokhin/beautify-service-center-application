package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import UTILS.FileUtil;

public class EditCounterProfile extends JFrame {

    private static final long serialVersionUID = 1L;

    private String counterID;

    // 🔥 Components
    private JLabel txtID;
    private JTextField txtName, txtUsername, txtEmail, txtPhone;
    private JPasswordField txtPassword;

    public EditCounterProfile(String counterID) {

        this.counterID = counterID;

        setTitle("Edit Counter Profile");
        setSize(520, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🔥 Background
        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg);

        // 🔥 Main Card
        JPanel card = new RoundedPanel(30, new Color(35,35,50));
        card.setPreferredSize(new Dimension(380,420));
        card.setLayout(null);

        JLabel title = new JLabel("EDIT PROFILE", SwingConstants.CENTER);
        title.setBounds(90,20,200,35);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        card.add(title);

        // 🔥 Counter ID
        addLabel(card, "Counter ID:", 80);

        txtID = new JLabel();
        txtID.setBounds(150,80,180,28);
        txtID.setForeground(Color.WHITE);
        txtID.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(txtID);

        // 🔥 Name
        addLabel(card, "Name:", 120);
        txtName = addField(card,120);

        // 🔥 Username
        addLabel(card, "Username:", 160);
        txtUsername = addField(card,160);

        // 🔥 Password
        addLabel(card, "Password:", 200);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150,200,150,30);

        styleField(txtPassword);

        // 🔥 Default hidden
        txtPassword.setEchoChar('•');

        card.add(txtPassword);

        // 👁 Show Password
        JButton eyeBtn = new JButton("...");
        eyeBtn.setBounds(305,200,45,30);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        eyeBtn.setBackground(new Color(60,60,80));
        eyeBtn.setForeground(Color.WHITE);

        eyeBtn.addActionListener(e -> {

            if (txtPassword.getEchoChar() == (char)0) {
                txtPassword.setEchoChar('•');
            } else {
                txtPassword.setEchoChar((char)0);
            }
        });

        card.add(eyeBtn);

        // 🔥 Email
        addLabel(card, "Email:", 240);
        txtEmail = addField(card,240);

        // 🔥 Phone
        addLabel(card, "Phone:", 280);
        txtPhone = addField(card,280);

        // 🔥 Buttons
        JButton updateBtn = new ModernButton("Update");
        updateBtn.setBounds(50,340,130,40);

        JButton backBtn = new ModernButton("Back");
        backBtn.setBounds(200,340,130,40);

        card.add(updateBtn);
        card.add(backBtn);

        // 🔥 Load profile
        loadProfile();

        // 🔥 Actions
        updateBtn.addActionListener(e -> updateProfile());

        backBtn.addActionListener(e -> {
            new CounterStaffMenu(counterID);
            dispose();
        });

        bg.add(card);

        setVisible(true);
    }

    // 🔥 Add Label
    private void addLabel(JPanel panel, String text, int y) {

        JLabel label = new JLabel(text);

        label.setBounds(40,y,100,25);

        label.setForeground(Color.WHITE);

        label.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panel.add(label);
    }

    // 🔥 Add Field
    private JTextField addField(JPanel panel, int y) {

        JTextField tf = new JTextField();

        tf.setBounds(150,y,180,30);

        styleField(tf);

        panel.add(tf);

        return tf;
    }

    // 🔥 Field Style
    private void styleField(JTextField field) {

        field.setBackground(new Color(60,60,80));
        field.setForeground(Color.WHITE);

        field.setCaretColor(Color.WHITE);

        field.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    // 🔥 Load Profile
    private void loadProfile() {

        List<String> lines = FileUtil.readFile("counter.txt");

        for (String line : lines) {

            String[] data = line.split(",");

            if (data[0].equals(counterID)) {

                txtID.setText(data[0]);
                txtName.setText(data[1]);
                txtUsername.setText(data[2]);
                txtPassword.setText(data[3]);
                txtEmail.setText(data[4]);
                txtPhone.setText(data[5]);

                return;
            }
        }

        new ModernDialog(this,
                "Counter profile not found!");
    }

    // 🔥 Update Profile
    private void updateProfile() {

        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        // Validation
        if (name.isEmpty() ||
            username.isEmpty() ||
            password.isEmpty() ||
            email.isEmpty() ||
            phone.isEmpty()) {

            new ModernDialog(this,
                    "Please fill in all fields.");

            return;
        }

        if (!email.contains("@")) {

            new ModernDialog(this,
                    "Invalid email.");

            return;
        }

        if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) {

            new ModernDialog(this,
                    "Phone format: 0XX-XXX-XXXX");

            return;
        }

        List<String> lines = FileUtil.readFile("counter.txt");

        for (int i = 0; i < lines.size(); i++) {

            String[] data = lines.get(i).split(",");

            if (data[0].equals(counterID)) {

                lines.set(i,
                        counterID + "," +
                        name + "," +
                        username + "," +
                        password + "," +
                        email + "," +
                        phone
                );

                FileUtil.writeFile("counter.txt", lines);

                new ModernDialog(this,
                        "Profile Updated Successfully!");

                return;
            }
        }

        new ModernDialog(this,
                "Update Failed!");
    }

    // 🔥 Gradient Background
    class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(
                    0,0,new Color(20,20,40),
                    getWidth(),getHeight(),
                    new Color(0,200,255)
            );

            g2.setPaint(gp);

            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }

    // 🔥 Rounded Panel
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

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,0,getWidth(),getHeight(),
                    radius,radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // 🔥 Modern Button
    class ModernButton extends JButton {

        private static final long serialVersionUID = 1L;

        private boolean hovering = false;
        private boolean pressed = false;

        private Color bgColor =
                new Color(60,60,80);

        private Color hoverColor =
                new Color(0,200,255);

        public ModernButton(String text) {

            super(text);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            setForeground(Color.WHITE);

            setFont(new Font("Segoe UI",
                    Font.PLAIN,14));

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

            if (!pressed) {

                g2.setColor(new Color(0,0,0,80));

                g2.fillRoundRect(
                        4,4,w-4,h-4,
                        20,20
                );
            }

            if (pressed)
                g2.setColor(bgColor.darker());

            else if (hovering)
                g2.setColor(hoverColor);

            else
                g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,0,w-4,h-4,
                    20,20
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // 🔥 Modern Dialog
    class ModernDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        public ModernDialog(JFrame parent,
                            String message) {

            super(parent,true);

            setUndecorated(true);

            setSize(320,160);

            setLocationRelativeTo(parent);

            JPanel panel = new JPanel();

            panel.setLayout(null);

            panel.setBackground(
                    new Color(30,30,30));

            panel.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(0,200,255),2
                    )
            );

            add(panel);

            JLabel msg = new JLabel(
                    "<html>"+message+"</html>"
            );

            msg.setBounds(40,30,240,40);

            msg.setForeground(Color.WHITE);

            msg.setFont(new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    13
            ));

            panel.add(msg);

            JButton okBtn = new JButton("OK");

            okBtn.setBounds(100,90,100,30);

            okBtn.setFocusPainted(false);

            okBtn.setBackground(
                    new Color(60,60,80)
            );

            okBtn.setForeground(Color.WHITE);

            okBtn.addActionListener(e -> dispose());

            panel.add(okBtn);

            setVisible(true);
        }
    }
}