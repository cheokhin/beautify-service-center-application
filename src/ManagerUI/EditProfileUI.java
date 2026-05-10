package ManagerUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import UI.Components.*;

public class EditProfileUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String managerId;
    private JTextField nameField, userField, emailField, phoneField;
    private JPasswordField passField;

    public EditProfileUI(String id) {
        this.managerId = id; 
        buildUI();
    }

    private void buildUI() {
        setOpaque(false); 
        setLayout(new BorderLayout());

        JPanel bg = new JPanel();
        bg.setOpaque(false);
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(380, 400));
        card.setLayout(null);
        bg.add(card);

        // ================= TITLE =================
        JLabel title = new JLabel("EDIT PROFILE", SwingConstants.CENTER);
        title.setBounds(90, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        // ================= INPUT FIELDS =================
        addLabel(card, "Name:", 70); 
        nameField = addTextField(card, 70);
        
        addLabel(card, "Username:", 120); 
        userField = addTextField(card, 120);
        
        addLabel(card, "Password:", 170); 
        passField = new JPasswordField();
        passField.setBounds(150, 168, 180, 30);
        styleField(passField);
        card.add(passField);
        
        addLabel(card, "Email:", 220); 
        emailField = addTextField(card, 220);
        
        addLabel(card, "Phone:", 270); 
        phoneField = addTextField(card, 270);

        // ================= BUTTONS =================
        JButton saveBtn = new ModernButton("Save Changes");
        saveBtn.setBounds(110, 330, 160, 35); // 🔥 Centered in the 380px wide card
        card.add(saveBtn);

        // ================= ACTIONS =================
        loadProfileData();

        saveBtn.addActionListener(e -> saveProfile());
    }

    // ================= LOGIC =================

    private void loadProfileData() {
        try (BufferedReader br = new BufferedReader(new FileReader("manager.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(managerId)) {
                    nameField.setText(data[1]);
                    userField.setText(data[2]);
                    passField.setText(data[3]);
                    emailField.setText(data[4]);
                    phoneField.setText(data[5]);
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            new ModernDialog("Fields cannot be empty!");
            return;
        }

        if (!email.contains("@")) {
            new ModernDialog("Invalid email format!");
            return;
        }

        if (!phone.matches("\\d{3}-\\d{3}-\\d{4}")) {
            new ModernDialog("Phone must be in format XXX-XXX-XXXX");
            return;
        }
        
        ArrayList<String> fileData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("manager.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(managerId)) {
                    line = managerId + "," + name + "," + username + "," + 
                           password + "," + email + "," + phone + "," + data[6];
                }
                fileData.add(line);
            }
        } catch (Exception e) { e.printStackTrace(); }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("manager.txt", false))) {
            for (String s : fileData) {
                bw.write(s);
                bw.newLine();
            }
            new ModernDialog("Profile Updated Successfully!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ================= UI HELPERS & COMPONENTS =================

    private void addLabel(JPanel card, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(40, y, 100, 25);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(label);
    }

    private JTextField addTextField(JPanel card, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(150, y - 2, 180, 30);
        styleField(tf);
        card.add(tf);
        return tf;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
}