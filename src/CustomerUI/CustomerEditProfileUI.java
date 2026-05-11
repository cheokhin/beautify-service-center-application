package CustomerUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import UI.Components.*; 

public class CustomerEditProfileUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String customerID;
    private JTextField txtID, txtName, txtUsername, txtEmail, txtPhone, txtModel, txtReg, txtYear;
    private JPasswordField txtPassword;
    private JComboBox<String> typeBox;

    public CustomerEditProfileUI(String id) {
        this.customerID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(650, 620)); 
        card.setLayout(null);

        JLabel title = new JLabel("EDIT CUSTOMER PROFILE", SwingConstants.CENTER);
        title.setBounds(0, 20, 650, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        int y = 80;
        int col1 = 50, col2 = 150, col3 = 350, col4 = 440;

        addLabel(card, "ID:", col1, y); txtID = addField(card, col2, y, 160); txtID.setEditable(false);
        addLabel(card, "Name:", col3, y); txtName = addField(card, col4, y, 160); y += 45;

        addLabel(card, "Username:", col1, y); txtUsername = addField(card, col2, y, 160);
        addLabel(card, "Password:", col3, y);
        
        txtPassword = new JPasswordField();
        styleField(txtPassword);
        txtPassword.setBounds(col4, y, 100, 30);
        txtPassword.setEchoChar('•');
        card.add(txtPassword);

        JButton eyeBtn = new JButton("Show");
        eyeBtn.setBounds(550, y, 50, 30);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setBackground(new Color(60, 60, 80));
        eyeBtn.setForeground(Color.WHITE);
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        eyeBtn.setMargin(new Insets(0,0,0,0));
        eyeBtn.addActionListener(e -> {
            if (txtPassword.getEchoChar() == (char)0) { txtPassword.setEchoChar('•'); eyeBtn.setText("Show"); }
            else { txtPassword.setEchoChar((char)0); eyeBtn.setText("Hide"); }
        });
        card.add(eyeBtn); y += 45;

        addLabel(card, "Email:", col1, y); txtEmail = addField(card, col2, y, 160);
        addLabel(card, "Phone:", col3, y); txtPhone = addField(card, col4, y, 160); y += 45;

        addLabel(card, "Model:", col1, y); txtModel = addField(card, col2, y, 160);
        addLabel(card, "Reg No:", col3, y); txtReg = addField(card, col4, y, 160); y += 45;

        addLabel(card, "Year:", col1, y); txtYear = addField(card, col2, y, 160);
        addLabel(card, "Type:", col3, y);
        typeBox = new JComboBox<>(new String[]{"Walk-in", "Booking"});
        typeBox.setBounds(col4, y, 160, 30);
        typeBox.setBackground(Color.WHITE);
        typeBox.setEnabled(false); // Type is usually fixed
        card.add(typeBox); y += 60;

        // Centered Button
        JButton updateBtn = new ModernButton(" Update Profile");
        updateBtn.setBounds(250, y, 150, 40);
        card.add(updateBtn);

        loadProfile();
        updateBtn.addActionListener(e -> updateProfile());
        add(card);
    }

    private void addLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 90, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label);
    }

    private JTextField addField(JPanel panel, int x, int y, int width) {
        JTextField field = new JTextField();
        styleField(field);
        field.setBounds(x, y, width, 30);
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
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void loadProfile() {
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(customerID)) {
                    txtID.setText(data[0]); txtName.setText(data[1]); txtUsername.setText(data[2]); txtPassword.setText(data[3]);
                    txtEmail.setText(data[4]); txtPhone.setText(data[5]); txtModel.setText(data[6]); txtReg.setText(data[7]);
                    txtYear.setText(data[8]); typeBox.setSelectedItem(data[9]); break;
                }
            }
        } catch (Exception e) { new ModernDialog("Error loading profile!"); }
    }

    private void updateProfile() {
        String name = txtName.getText().trim(), username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim(), email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim(), model = txtModel.getText().trim(), reg = txtReg.getText().trim(), year = txtYear.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || model.isEmpty() || reg.isEmpty() || year.isEmpty()) {
            new ModernDialog("All fields must be filled!"); return;
        }
        if (!email.contains("@")) { new ModernDialog("Email must contain @"); return; }
        if (!phone.matches("\\d+")) { new ModernDialog("Phone must be numeric!"); return; }
        if (!year.matches("\\d{4}")) { new ModernDialog("Year must be 4 digits!"); return; }

        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(customerID)) {
                    line = txtID.getText() + "," + name + "," + username + "," + password + "," + email + "," + phone + "," + model + "," + reg + "," + year + "," + typeBox.getSelectedItem();
                }
                list.add(line);
            }
        } catch (Exception e) { new ModernDialog("Error updating profile!"); }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("customer.txt"))) {
            for (String s : list) { bw.write(s); bw.newLine(); }
            new ModernDialog("Profile Updated Successfully!");
        } catch (Exception e) { new ModernDialog("Error saving profile!"); }
    }
}