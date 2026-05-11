package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import UTILS.FileUtil;
import UI.Components.*; 

public class EditCounterProfile extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JLabel txtID;
    private JTextField txtName, txtUsername, txtEmail, txtPhone;
    private JPasswordField txtPassword;

    public EditCounterProfile(String counterID) {
        this.counterID = counterID;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35,35,50));
        card.setPreferredSize(new Dimension(380,420));
        card.setLayout(null);

        JLabel title = new JLabel("EDIT PROFILE", SwingConstants.CENTER);
        title.setBounds(90,20,200,35);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        addLabel(card, "Counter ID:", 80);
        txtID = new JLabel();
        txtID.setBounds(150,80,180,28);
        txtID.setForeground(Color.WHITE);
        txtID.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(txtID);

        addLabel(card, "Name:", 120);
        txtName = addField(card,120);

        addLabel(card, "Username:", 160);
        txtUsername = addField(card,160);

        addLabel(card, "Password:", 200);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(150,200,150,30);
        styleField(txtPassword);
        txtPassword.setEchoChar('•');
        card.add(txtPassword);

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

        addLabel(card, "Email:", 240);
        txtEmail = addField(card,240);

        addLabel(card, "Phone:", 280);
        txtPhone = addField(card,280);

        // 🔥 Centered update button
        JButton updateBtn = new ModernButton("Update");
        updateBtn.setBounds(125,340,130,40); 
        card.add(updateBtn);

        loadProfile();

        updateBtn.addActionListener(e -> updateProfile());

        add(card);
    }

    private void addLabel(JPanel panel, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(40,y,100,25);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label);
    }

    private JTextField addField(JPanel panel, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(150,y,180,30);
        styleField(tf);
        panel.add(tf);
        return tf;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(60,60,80));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

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
        new ModernDialog("Counter profile not found!");
    }

    private void updateProfile() {
        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            new ModernDialog("Please fill in all fields.");
            return;
        }

        if (!email.contains("@")) {
            new ModernDialog("Invalid email.");
            return;
        }

        if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) {
            new ModernDialog("Phone format: 0XX-XXX-XXXX");
            return;
        }

        List<String> lines = FileUtil.readFile("counter.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (data[0].equals(counterID)) {
                lines.set(i, counterID + "," + name + "," + username + "," + password + "," + email + "," + phone);
                FileUtil.writeFile("counter.txt", lines);
                new ModernDialog("Profile Updated Successfully!");
                return;
            }
        }
        new ModernDialog("Update Failed!");
    }
}