package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import UI.Components.*; 

public class EditTechnicianProfileUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String technicianID;
    private JTextField txtID, txtName, txtUsername, txtEmail, txtPhone, txtSkill, txtExp, txtDate;
    private JPasswordField txtPassword;

    public EditTechnicianProfileUI(String id) {
        this.technicianID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(580, 580)); 
        card.setLayout(null);

        JLabel title = new JLabel("EDIT TECHNICIAN PROFILE", SwingConstants.CENTER);
        title.setBounds(0, 20, 580, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        int y = 80;
        addLabel(card, "ID:", y); txtID = addField(card, y); txtID.setEditable(false); y += 45;
        addLabel(card, "Name:", y); txtName = addField(card, y); y += 45;
        addLabel(card, "Username:", y); txtUsername = addField(card, y); y += 45;

        addLabel(card, "Password:", y);
        txtPassword = new JPasswordField();
        styleField(txtPassword);
        txtPassword.setBounds(220, y, 170, 30);
        txtPassword.setEchoChar('•');
        card.add(txtPassword);

        JButton eyeBtn = new JButton("Show");
        eyeBtn.setBounds(400, y, 70, 30);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setBackground(new Color(60, 60, 80));
        eyeBtn.setForeground(Color.WHITE);
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        eyeBtn.addActionListener(e -> {
            if (txtPassword.getEchoChar() == (char)0) {
                txtPassword.setEchoChar('•');
                eyeBtn.setText("Show");
            } else {
                txtPassword.setEchoChar((char)0);
                eyeBtn.setText("Hide");
            }
        });
        card.add(eyeBtn); y += 45;

        addLabel(card, "Email:", y); txtEmail = addField(card, y); y += 45;
        addLabel(card, "Phone:", y); txtPhone = addField(card, y); y += 45;
        addLabel(card, "Skill:", y); txtSkill = addField(card, y); y += 45;
        addLabel(card, "Experience:", y); txtExp = addField(card, y); y += 45;
        addLabel(card, "Date Joined:", y); txtDate = addField(card, y); txtDate.setEditable(false); y += 60;

        // Centered Button
        JButton updateBtn = new ModernButton(" Update");
        updateBtn.setBounds(225, y, 130, 40);
        card.add(updateBtn);

        loadProfile();
        updateBtn.addActionListener(e -> updateProfile());
        add(card);
    }

    private void addLabel(JPanel panel, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(70, y, 120, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(label);
    }

    private JTextField addField(JPanel panel, int y) {
        JTextField field = new JTextField();
        styleField(field);
        field.setBounds(220, y, 250, 30);
        panel.add(field);
        return field;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
        tf.setCaretColor(Color.BLACK);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void loadProfile() {
        try (BufferedReader br = new BufferedReader(new FileReader("technician.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(technicianID)) {
                    txtID.setText(data[0]); txtName.setText(data[1]); txtUsername.setText(data[2]);
                    txtPassword.setText(data[3]); txtEmail.setText(data[4]); txtPhone.setText(data[5]);
                    txtSkill.setText(data[6]); txtExp.setText(data[7]); txtDate.setText(data[8]);
                    break;
                }
            }
        } catch (Exception e) { new ModernDialog("Error loading profile!"); }
    }

    private void updateProfile() {
        String name = txtName.getText().trim(), username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim(), email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim(), skill = txtSkill.getText().trim(), exp = txtExp.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || skill.isEmpty() || exp.isEmpty()) {
            new ModernDialog("All fields must be filled!"); return;
        }
        if (!email.contains("@")) { new ModernDialog("Email must contain @"); return; }
        if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) { new ModernDialog("Phone format: 0XX-XXX-XXXX"); return; }
        if (!exp.matches("\\d+")) { new ModernDialog("Experience must be numeric!"); return; }

        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("technician.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(technicianID)) {
                    line = txtID.getText() + "," + name + "," + username + "," + password + "," + email + "," + phone + "," + skill + "," + exp + "," + txtDate.getText();
                }
                list.add(line);
            }
        } catch (Exception e) { new ModernDialog("Error updating profile!"); }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("technician.txt"))) {
            for (String s : list) { bw.write(s); bw.newLine(); }
            new ModernDialog("Profile Updated Successfully!");
        } catch (Exception e) { new ModernDialog("Error saving profile!"); }
    }
}