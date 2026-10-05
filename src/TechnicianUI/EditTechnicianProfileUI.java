package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import UI.Components.*;

public class EditTechnicianProfileUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String technicianID;
    private JTextField txtName, txtUsername, txtEmail, txtPhone, txtSkill, txtExp;
    private JLabel txtID, txtDate; 
    private JPasswordField txtPassword;

    public EditTechnicianProfileUI(String id) {
        this.technicianID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(650, 620));
        card.setLayout(null);

        JLabel title = new JLabel("EDIT TECHNICIAN PROFILE", SwingConstants.CENTER);
        title.setBounds(0, 20, 650, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        JSeparator topSep = new JSeparator();
        topSep.setBounds(30, 63, 590, 1);
        topSep.setForeground(new Color(55, 55, 80));
        card.add(topSep);

        card.add(makeSectionLabel("ACCOUNT",  30, 73));
        card.add(makeSectionLabel("CONTACT",  30, 253));
        card.add(makeSectionLabel("DETAILS",  30, 363));

        int y = 90;
        card.add(makeHintLabel("Technician ID", 50, y));
        card.add(makeHintLabel("Full name",     350, y));
        y += 18;

        txtID = makeViewLabel(50, y, 250);
        card.add(txtID);

        txtName = addField(card, 350, y, 250);

        y += 55;
        card.add(makeHintLabel("Username", 50,  y));
        card.add(makeHintLabel("Password", 350, y));
        y += 18;
        txtUsername = addField(card, 50, y, 250);

        txtPassword = new JPasswordField();
        styleField(txtPassword);
        txtPassword.setBounds(350, y, 190, 34);
        txtPassword.setEchoChar('•');
        card.add(txtPassword);

        JButton eyeBtn = new JButton("Show");
        eyeBtn.setBounds(548, y, 52, 34);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setBackground(new Color(55, 55, 75));
        eyeBtn.setForeground(new Color(180, 180, 210));
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        eyeBtn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 110)));
        eyeBtn.addActionListener(e -> {
            if (txtPassword.getEchoChar() == (char) 0) {
                txtPassword.setEchoChar('•');
                eyeBtn.setText("Show");
            } else {
                txtPassword.setEchoChar((char) 0);
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
        txtEmail = addField(card, 50,  y, 250);
        txtPhone = addField(card, 350, y, 250);

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(30, 353, 590, 1);
        sep2.setForeground(new Color(55, 55, 80));
        card.add(sep2);

        y = 380;
        card.add(makeHintLabel("Skill",       50,  y));
        card.add(makeHintLabel("Experience (years)", 350, y));
        y += 18;
        txtSkill = addField(card, 50,  y, 250);
        txtExp   = addField(card, 350, y, 250);

        y += 55;
        card.add(makeHintLabel("Date joined", 50, y));
        y += 18;

        txtDate = makeViewLabel(50, y, 250);
        card.add(txtDate);

        JSeparator sep3 = new JSeparator();
        sep3.setBounds(30, 535, 590, 1);
        sep3.setForeground(new Color(55, 55, 80));
        card.add(sep3);

        JButton updateBtn = new ModernButton("Save changes");
        updateBtn.setBounds(250, 557, 150, 40);
        card.add(updateBtn);

        loadProfile();
        updateBtn.addActionListener(e -> updateProfile());
        add(card);
    }


    private JLabel makeViewLabel(int x, int y, int w) {
        JLabel l = new JLabel("");
        l.setBounds(x, y, w, 34);
        l.setForeground(new Color(210, 210, 235));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setOpaque(false);
        return l;
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


    private void loadProfile() {
        try (BufferedReader br = new BufferedReader(new FileReader("technician.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(technicianID)) {
                    txtID.setText(data[0]);       txtName.setText(data[1]);
                    txtUsername.setText(data[2]); txtPassword.setText(data[3]);
                    txtEmail.setText(data[4]);    txtPhone.setText(data[5]);
                    txtSkill.setText(data[6]);    txtExp.setText(data[7]);
                    txtDate.setText(data[8]);
                    break;
                }
            }
        } catch (Exception e) { new ModernDialog("Error loading profile!"); }
    }

    private void updateProfile() {
        String name     = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email    = txtEmail.getText().trim();
        String phone    = txtPhone.getText().trim();
        String skill    = txtSkill.getText().trim();
        String exp      = txtExp.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() ||
            phone.isEmpty() || skill.isEmpty() || exp.isEmpty()) {
            new ModernDialog("All fields must be filled!"); return;
        }
        if (!email.contains("@"))                     { new ModernDialog("Email must contain @"); return; }
        if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) { new ModernDialog("Phone format: 0XX-XXX-XXXX"); return; }
        if (!exp.matches("\\d+"))                     { new ModernDialog("Experience must be numeric!"); return; }

        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("technician.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(technicianID)) {
                    line = txtID.getText() + "," + name + "," + username + "," + password + ","
                         + email + "," + phone + "," + skill + "," + exp + "," + txtDate.getText();
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