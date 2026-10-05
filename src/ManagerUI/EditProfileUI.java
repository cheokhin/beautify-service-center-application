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
    private JLabel idLabel, dateLabel;

    public EditProfileUI(String id) {
        this.managerId = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(650, 540));
        card.setLayout(null);

        JLabel title = new JLabel("EDIT MANAGER PROFILE", SwingConstants.CENTER);
        title.setBounds(0, 20, 650, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        JSeparator topSep = new JSeparator();
        topSep.setBounds(30, 63, 590, 1);
        topSep.setForeground(new Color(55, 55, 80));
        card.add(topSep);

        card.add(makeSectionLabel("ACCOUNT", 30,  73));
        card.add(makeSectionLabel("CONTACT", 30, 253));
        card.add(makeSectionLabel("DETAILS", 30, 363));

        int y = 90;
        card.add(makeHintLabel("Manager ID", 50, y));
        card.add(makeHintLabel("Full name",  350, y));
        y += 18;

        idLabel = new JLabel();
        idLabel.setBounds(50, y, 250, 34);
        idLabel.setForeground(new Color(210, 210, 235));
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setOpaque(false);
        card.add(idLabel);

        nameField = addField(card, 350, y, 250);

        y += 55;
        card.add(makeHintLabel("Username", 50,  y));
        card.add(makeHintLabel("Password", 350, y));
        y += 18;
        userField = addField(card, 50, y, 250);

        passField = new JPasswordField();
        styleField(passField);
        passField.setBounds(350, y, 190, 34);
        passField.setEchoChar('•');
        card.add(passField);

        JButton eyeBtn = new JButton("Show");
        eyeBtn.setBounds(548, y, 52, 34);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setBackground(new Color(55, 55, 75));
        eyeBtn.setForeground(new Color(180, 180, 210));
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        eyeBtn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110)));
        eyeBtn.addActionListener(e -> {
            if (passField.getEchoChar() == (char) 0) {
                passField.setEchoChar('•');
                eyeBtn.setText("Show");
            } else {
                passField.setEchoChar((char) 0);
                eyeBtn.setText("Hide");
            }
        });
        card.add(eyeBtn);

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(30, 243, 590, 1);
        sep1.setForeground(new Color(55, 55, 80));
        card.add(sep1);

        y = 270;
        card.add(makeHintLabel("Email", 50,  y));
        card.add(makeHintLabel("Phone", 350, y));
        y += 18;
        emailField = addField(card, 50,  y, 250);
        phoneField = addField(card, 350, y, 250);

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(30, 353, 590, 1);
        sep2.setForeground(new Color(55, 55, 80));
        card.add(sep2);

        y = 380;
        card.add(makeHintLabel("Date joined", 50, y));
        y += 18;

        dateLabel = new JLabel();
        dateLabel.setBounds(50, y, 250, 34);
        dateLabel.setForeground(new Color(210, 210, 235));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setOpaque(false);
        card.add(dateLabel);

        JSeparator sep3 = new JSeparator();
        sep3.setBounds(30, 463, 590, 1);
        sep3.setForeground(new Color(55, 55, 80));
        card.add(sep3);

        JButton saveBtn = new ModernButton("Save changes");
        saveBtn.setBounds(250, 472, 150, 40);
        card.add(saveBtn);

        loadProfileData();
        saveBtn.addActionListener(e -> saveProfile());
        add(card);
    }


    private JLabel makeSectionLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 300, 14);
        l.setForeground(new Color(120, 120, 160));
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        return l;
    }

    private JLabel makeHintLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 200, 14);
        l.setForeground(new Color(150, 150, 185));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    private JTextField addField(JPanel panel, int x, int y, int w) {
        JTextField f = new JTextField();
        styleField(f);
        f.setBounds(x, y, w, 34);
        panel.add(f);
        return f;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(28, 28, 45));
        tf.setForeground(new Color(210, 210, 235));
        tf.setCaretColor(new Color(180, 180, 220));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 110)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }


    private void loadProfileData() {
        try (BufferedReader br = new BufferedReader(new FileReader("manager.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(managerId)) {
                    idLabel.setText(data[0]);
                    nameField.setText(data[1]);
                    userField.setText(data[2]);
                    passField.setText(data[3]);
                    emailField.setText(data[4]);
                    phoneField.setText(data[5]);
                    if (data.length > 6) dateLabel.setText(data[6]);
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveProfile() {
        String name     = nameField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            new ModernDialog("All fields must be filled!"); return;
        }
        if (!email.contains("@"))                     { new ModernDialog("Email must contain @"); return; }
        if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) { new ModernDialog("Phone format: 0XX-XXX-XXXX"); return; }

        ArrayList<String> fileData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("manager.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(managerId)) {
                    String date = data.length > 6 ? data[6] : dateLabel.getText();
                    line = managerId + "," + name + "," + username + "," + password + ","
                         + email + "," + phone + "," + date;
                }
                fileData.add(line);
            }
        } catch (Exception e) { e.printStackTrace(); }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("manager.txt", false))) {
            for (String s : fileData) { bw.write(s); bw.newLine(); }
            new ModernDialog("Profile Updated Successfully!");
        } catch (Exception e) { e.printStackTrace(); }
    }
}