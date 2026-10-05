package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import UI.Components.*;

public class SignInUI extends JPanel {
    private static final long serialVersionUID = 1L;

    JTextField idField, nameField, userField, emailField, phoneField;
    JPasswordField passField;
    JPanel mainCard;
    
    private String nextCustomerID = "C001"; 

    public SignInUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        mainCard = new RoundedPanel(25, new Color(35, 35, 50));
        mainCard.setPreferredSize(new Dimension(380, 420));
        mainCard.setLayout(null);
        bg.add(mainCard);

        buildRegistrationForm();
    }


    private void buildRegistrationForm() {
        mainCard.removeAll();

        JLabel title = new JLabel(" CUSTOMER REGISTRATION", SwingConstants.CENTER);
        title.setBounds(70, 15, 240, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Color.WHITE);
        mainCard.add(title);

        addLabel(mainCard, "Assigned ID:", 60);
        idField = addField(mainCard, 60);
        idField.setEditable(false); 
        idField.setFocusable(false);
        idField.setForeground(new Color(0, 200, 255)); 
        refreshCustomerID(); 

        addLabel(mainCard, "Full Name:", 95);
        nameField = addField(mainCard, 95);

        addLabel(mainCard, "Username:", 130);
        userField = addField(mainCard, 130);

        addLabel(mainCard, "Password:", 165);
        passField = new JPasswordField();
        passField.setBounds(140, 165, 140, 30);
        styleField(passField);
        passField.setEchoChar('•');
        mainCard.add(passField);

        JButton eyeBtn = new JButton("Show");
        eyeBtn.setBounds(280, 165, 60, 30);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        eyeBtn.setBackground(new Color(60, 60, 80));
        eyeBtn.setForeground(Color.WHITE);
        eyeBtn.setMargin(new Insets(0, 0, 0, 0));
        eyeBtn.addActionListener(e -> {
            if (passField.getEchoChar() == (char) 0) {
                passField.setEchoChar('•');
                eyeBtn.setText("Show");
            } else {
                passField.setEchoChar((char) 0);
                eyeBtn.setText("Hide");
            }
        });
        mainCard.add(eyeBtn);

        addLabel(mainCard, "Email:", 200);
        emailField = addField(mainCard, 200);

        addLabel(mainCard, "Phone:", 235);
        phoneField = addField(mainCard, 235);
        
        JButton regBtn = new ModernButton(" Register");
        regBtn.setBounds(40, 300, 140, 40);

        JButton backBtn = new ModernButton(" Return");
        backBtn.setBounds(200, 300, 140, 40);

        mainCard.add(regBtn);
        mainCard.add(backBtn);

        JLabel managerLbl = new JLabel("<html><u>Manager Setup</u></html>", SwingConstants.CENTER);
        managerLbl.setBounds(115, 365, 150, 25);
        managerLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        managerLbl.setForeground(new Color(100, 100, 120));
        managerLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        managerLbl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { managerLbl.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { managerLbl.setForeground(new Color(100, 100, 120)); }
            public void mouseClicked(MouseEvent e) { 
                attemptManagerRegistration();
            }
        });
        mainCard.add(managerLbl);

        regBtn.addActionListener(e -> registerCustomer());
        backBtn.addActionListener(e -> {
            clearFields();
            MainUI.instance.showPage("LOGIN");
        });

        mainCard.revalidate();
        mainCard.repaint();
    }


    private void attemptManagerRegistration() {
        boolean hasManager = false;
        File mFile = new File("manager.txt");
        if (mFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(mFile))) {
                if (br.readLine() != null) hasManager = true;
            } catch (Exception e) {}
        }
        
        if (hasManager) {
            new ModernDialog("Manager has already been created.");
            return;
        }
        
        buildManagerSetupForm();
    }

    private void buildManagerSetupForm() {
        mainCard.removeAll();

        JLabel title = new JLabel("MANAGER SETUP", SwingConstants.CENTER);
        title.setBounds(70, 15, 240, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(new Color(255, 180, 0)); 
        mainCard.add(title);

        addLabel(mainCard, "Manager ID:", 60);
        JTextField mIdField = addField(mainCard, 60);
        mIdField.setText("M001");
        mIdField.setEditable(false);
        mIdField.setFocusable(false);
        mIdField.setForeground(new Color(255, 180, 0));

        addLabel(mainCard, "Full Name:", 95);
        JTextField mNameField = addField(mainCard, 95);

        addLabel(mainCard, "Username:", 130);
        JTextField mUserField = addField(mainCard, 130);

        addLabel(mainCard, "Password:", 165);
        JPasswordField mPassField = new JPasswordField();
        mPassField.setBounds(140, 165, 140, 30);
        styleField(mPassField);
        mPassField.setEchoChar('•');
        mainCard.add(mPassField);

        JButton eyeBtn = new JButton("Show");
        eyeBtn.setBounds(280, 165, 60, 30);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        eyeBtn.setBackground(new Color(60, 60, 80));
        eyeBtn.setForeground(Color.WHITE);
        eyeBtn.setMargin(new Insets(0, 0, 0, 0));
        eyeBtn.addActionListener(e -> {
            if (mPassField.getEchoChar() == (char) 0) {
                mPassField.setEchoChar('•');
                eyeBtn.setText("Show");
            } else {
                mPassField.setEchoChar((char) 0);
                eyeBtn.setText("Hide");
            }
        });
        mainCard.add(eyeBtn);

        addLabel(mainCard, "Email:", 200);
        JTextField mEmailField = addField(mainCard, 200);

        addLabel(mainCard, "Phone:", 235);
        JTextField mPhoneField = addField(mainCard, 235);

        JButton createBtn = new ModernButton(" Initialize");
        createBtn.setBounds(40, 300, 140, 40);

        JButton cancelBtn = new ModernButton(" Cancel");
        cancelBtn.setBounds(200, 300, 140, 40);

        mainCard.add(createBtn);
        mainCard.add(cancelBtn);

        createBtn.addActionListener(e -> {
            String name = mNameField.getText().trim();
            String user = mUserField.getText().trim();
            String pass = new String(mPassField.getPassword()).trim();
            String email = mEmailField.getText().trim();
            String phone = mPhoneField.getText().trim();
            
            if (name.isEmpty() || user.isEmpty() || pass.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                new ModernDialog("All fields must be filled!");
                return;
            }

            if (!email.contains("@")) {
                new ModernDialog("Invalid email format! Must contain '@'.");
                return;
            }

            if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) {
                new ModernDialog("Phone format: 0XX-XXX-XXXX");
                return;
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("manager.txt", true))) {
                bw.write("M001," + name + "," + user + "," + pass + "," + email + "," + phone + "," + LocalDate.now().toString());
                bw.newLine();
                
                SystemLogger.log("M001", "Manager", "Manager account initialized.");
                new ModernDialog("Manager Account Initialized!");
            
                MainUI.instance.showPage("LOGIN");
                buildRegistrationForm(); 
                
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        cancelBtn.addActionListener(e -> buildRegistrationForm());

        mainCard.revalidate();
        mainCard.repaint();
    }


    private void addLabel(JPanel panel, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(40, y, 100, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label);
    }

    private JTextField addField(JPanel panel, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(140, y, 200, 30);
        styleField(tf);
        panel.add(tf);
        return tf;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }
    
    private void clearFields() {
        refreshCustomerID(); 
        nameField.setText("");
        userField.setText("");
        passField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    
    private void refreshCustomerID() {
        ArrayList<Integer> idNumbers = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].startsWith("C")) {
                    try {
                        int num = Integer.parseInt(data[0].substring(1).trim());
                        idNumbers.add(num);
                    } catch (NumberFormatException ex) {}
                }
            }
        } catch (Exception e) {}
        
        if (idNumbers.isEmpty()) {
            nextCustomerID = "C001";
        } else {
            int maxId = Collections.max(idNumbers);
            nextCustomerID = String.format("C%03d", maxId + 1);
        }
        
        if (idField != null) idField.setText(nextCustomerID);
    }


    private void registerCustomer() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            new ModernDialog("All fields must be filled!");
            return;
        }

        if (!email.contains("@")) {
            new ModernDialog("Invalid email format! Must contain '@'.");
            return;
        }

        if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) {
            new ModernDialog("Phone format: 0XX-XXX-XXXX");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[2].equals(username)) {
                    new ModernDialog("Username already taken! Please choose another.");
                    return;
                }
            }
        } catch (Exception e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("customer.txt", true))) {
            bw.write(id + "," + name + "," + username + "," + password + "," + email + "," + phone + ",N/A,N/A,N/A,Unassigned");
            bw.newLine();

            SystemLogger.log(id, "Customer", "Registered a new account via Public Portal");

            showSuccessPage(id, name, username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void showSuccessPage(String genId, String genName, String genUser) {
        mainCard.removeAll();

        JLabel title = new JLabel("REGISTRATION SUCCESSFUL!", SwingConstants.CENTER);
        title.setBounds(20, 50, 340, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0, 220, 150)); 
        mainCard.add(title);
        
        JLabel subtitle = new JLabel("Welcome to APU Auto Service, " + genName + ".", SwingConstants.CENTER);
        subtitle.setBounds(20, 90, 340, 20);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.WHITE);
        mainCard.add(subtitle);
        
        JPanel infoBox = new RoundedPanel(15, new Color(45, 45, 65));
        infoBox.setBounds(65, 140, 250, 120);
        infoBox.setLayout(null);
        
        JLabel l1 = new JLabel("Your Customer ID :");
        l1.setBounds(20, 25, 120, 20);
        l1.setForeground(new Color(180, 180, 200));
        infoBox.add(l1);
        
        JLabel v1 = new JLabel(genId);
        v1.setBounds(140, 25, 100, 20);
        v1.setForeground(new Color(0, 200, 255));
        v1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoBox.add(v1);
        
        JLabel l2 = new JLabel("Your Username :");
        l2.setBounds(20, 75, 120, 20);
        l2.setForeground(new Color(180, 180, 200));
        infoBox.add(l2);
        
        JLabel v2 = new JLabel(genUser);
        v2.setBounds(140, 75, 100, 20);
        v2.setForeground(Color.WHITE);
        v2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoBox.add(v2);
        
        mainCard.add(infoBox);

        JButton loginBtn = new ModernButton(" Proceed to Login");
        loginBtn.setBounds(90, 300, 200, 40);
        loginBtn.addActionListener(e -> {
            clearFields();
            buildRegistrationForm(); 
            MainUI.instance.showPage("LOGIN");
        });
        mainCard.add(loginBtn);
        
        mainCard.revalidate();
        mainCard.repaint();
    }
}