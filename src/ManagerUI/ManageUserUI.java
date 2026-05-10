package ManagerUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import UI.MainUI;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import UI.Components.*;

public class ManageUserUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String managerId;
    private JComboBox<String> roleSelect;
    private JTextArea displayArea;

    public ManageUserUI(String id) {
        this.managerId = id;

        buildUI();

        // 🔥 SPA Routing
        MainUI.instance.mainContainer.add(this, "MANAGE_USERS");
        MainUI.instance.showPage("MANAGE_USERS");
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(620, 460));
        card.setLayout(null);
        bg.add(card);

        // ================= TITLE =================
        JLabel title = new JLabel("USER MANAGEMENT", SwingConstants.CENTER);
        title.setBounds(160, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        // ================= ROLE SELECTOR =================
        String[] roles = {"Manager", "Counter Staff", "Technician"};
        roleSelect = new JComboBox<>(roles);
        roleSelect.setBounds(40, 70, 200, 30);
        roleSelect.setBackground(new Color(60, 60, 80));
        roleSelect.setForeground(Color.WHITE);
        roleSelect.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleSelect.setFocusable(false);
        card.add(roleSelect);

        // ================= DISPLAY AREA =================
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(20, 20, 30));
        displayArea.setForeground(new Color(0, 200, 255));
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        displayArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBounds(40, 120, 540, 250);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        scrollPane.getVerticalScrollBar().setBackground(new Color(35, 35, 50));
        card.add(scrollPane);

        // ================= BUTTONS =================
        JButton addBtn = createButton("Add User", 40, 390, 110);
        JButton editBtn = createButton("Edit User", 160, 390, 110);
        JButton delBtn = createButton("Delete User", 280, 390, 130);
        JButton backBtn = createButton("Return", 460, 390, 120);

        card.add(addBtn);
        card.add(editBtn);
        card.add(delBtn);
        card.add(backBtn);

        // ================= ACTIONS =================
        roleSelect.addActionListener(e -> loadUsers());
        addBtn.addActionListener(e -> showAddUserDialog());
        editBtn.addActionListener(e -> showEditUserDialog());
        delBtn.addActionListener(e -> deleteUser());

        backBtn.addActionListener(e -> {
            MainUI.instance.showPage("MANAGER_DASHBOARD");
            MainUI.instance.mainContainer.remove(this); // Clean up memory
        });

        loadUsers(); // Load initially
    }

    private String getFileName() {
        String role = (String) roleSelect.getSelectedItem();
        if (role.equals("Manager")) return "manager.txt";
        if (role.equals("Counter Staff")) return "counter.txt";
        return "technician.txt";
    }

    private void loadUsers() {
        displayArea.setText("");
        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            boolean hasUsers = false;
            while ((line = br.readLine()) != null) {
                hasUsers = true;
                String[] data = line.split(",");
                displayArea.append(String.format("ID: %-8s | Name: %-18s | User: %s\n", data[0], data[1], data[2]));
            }
            if(!hasUsers) {
                 displayArea.setText("No users found in " + getFileName());
            }
        } catch (Exception ex) {
            displayArea.setText("No users found in " + getFileName());
        }
    }

    // ==========================================
    // 🔥 CREATE FUNCTION
    // ==========================================
    private void showAddUserDialog() {
        String role = (String) roleSelect.getSelectedItem();
        
        // Styled Form Dialog
        JDialog dialog = new JDialog(MainUI.instance, "Add " + role, true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 550);
        dialog.setLocationRelativeTo(MainUI.instance);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(35, 35, 50));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));
        dialog.add(panel);

        JLabel titleLabel = new JLabel("ADD " + role.toUpperCase(), SwingConstants.CENTER);
        titleLabel.setBounds(0, 15, 400, 25);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(titleLabel);

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 10, 15));
        formGrid.setOpaque(false);
        formGrid.setBounds(30, 60, 340, 380);
        panel.add(formGrid);

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField dateField = new JTextField(); 
        JTextField skillsField = new JTextField();
        JTextField expField = new JTextField();

        addFormField(formGrid, "ID (e.g. M001):", idField);
        addFormField(formGrid, "Name:", nameField);
        addFormField(formGrid, "Username:", userField);
        addFormField(formGrid, "Password:", passField);
        addFormField(formGrid, "Email:", emailField);
        addFormField(formGrid, "Phone (0XX-XXX-XXXX):", phoneField);
        addFormField(formGrid, "Employment (YYYY-MM-DD):", dateField);
        
        if (role.equals("Technician")) {
            addFormField(formGrid, "Skills:", skillsField);
            addFormField(formGrid, "Experience (Yrs):", expField);
            dialog.setSize(400, 620); // Expand dialog for extra fields
        }

        JButton saveBtn = new ModernButton("Save Record");
        saveBtn.setBounds(70, dialog.getHeight() - 70, 120, 35);
        panel.add(saveBtn);

        JButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setBounds(210, dialog.getHeight() - 70, 120, 35);
        cancelBtn.addActionListener(e -> dialog.dispose());
        panel.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String date = dateField.getText().trim();
            
            String skills = "";
            String exp = "";

            if (role.equals("Technician")) {
                skills = skillsField.getText().trim();
                exp = expField.getText().trim();
            }
            
            if (id.isEmpty() || name.isEmpty() || username.isEmpty() ||
                password.isEmpty() || email.isEmpty() || phone.isEmpty() || date.isEmpty()) {
                new ModernDialog("All fields must be filled!");
                return;
            }

            if (role.equals("Manager") && !id.matches("M\\d{3}")) {
                new ModernDialog("Manager ID must be MXXX");
                return;
            }

            if (role.equals("Counter Staff") && !id.matches("CS\\d{3}")) {
                new ModernDialog("Counter Staff ID must be CSXXX");
                return;
            }

            if (role.equals("Technician") && !id.matches("T\\d{3}")) {
                new ModernDialog("Technician ID must be TXXX");
                return;
            }

            if (!email.contains("@")) {
                new ModernDialog("Invalid email format!");
                return;
            }

            if (!phone.matches("0\\d{2}-\\d{3}-\\d{4}")) {
                new ModernDialog("Phone must be 0XX-XXX-XXXX");
                return;
            }

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                new ModernDialog("Date must be YYYY-MM-DD");
                return;
            }

            if (role.equals("Technician")) {
                if (skills.isEmpty()) skills = "-";
                if (!exp.matches("\\d+")) {
                    new ModernDialog("Experience must be a number!");
                    return;
                }
            }
            
            // Avoid Duplicate ID
            try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equals(id)) {
                        new ModernDialog("ID already exists!");
                        return;
                    }
                }
            } catch (Exception ex) { }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), true))) {
                String record = id + "," + name + "," + username + "," +
                                password + "," + email + "," + phone;

                if (role.equals("Technician")) record += "," + skills + "," + exp + "," + date + ",0";
                else record += "," + date;

                bw.write(record);
                bw.newLine();

                new ModernDialog("User added successfully!");
                dialog.dispose();
                loadUsers();

            } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        dialog.setVisible(true);
    }

    // ==========================================
    // 🔥 UPDATE FUNCTION
    // ==========================================
    private void showEditUserDialog() {
        String idToEdit = showModernInputDialog("Enter User ID to Edit:");
        if (idToEdit == null || idToEdit.isEmpty()) return;

        ArrayList<String> fileData = new ArrayList<>();
        String[] userData = null;
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(idToEdit)) {
                    userData = data;
                    found = true;
                }
                fileData.add(line);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        if (!found) {
            new ModernDialog("User ID not found.");
            return;
        }

        JDialog dialog = new JDialog(MainUI.instance, true);
        dialog.setUndecorated(true);
        dialog.setSize(380, 380);
        dialog.setLocationRelativeTo(MainUI.instance);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(35, 35, 50));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));
        dialog.add(panel);

        JLabel titleLabel = new JLabel("EDIT USER: " + idToEdit, SwingConstants.CENTER);
        titleLabel.setBounds(0, 15, 380, 25);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(titleLabel);

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 10, 15));
        formGrid.setOpaque(false);
        formGrid.setBounds(30, 60, 320, 200);
        panel.add(formGrid);

        JTextField userField = new JTextField(userData[2]);
        JPasswordField passField = new JPasswordField(userData[3]);
        JTextField emailField = new JTextField(userData[4]);
        JTextField phoneField = new JTextField(userData[5]);

        addFormField(formGrid, "Username:", userField);
        addFormField(formGrid, "Password:", passField);
        addFormField(formGrid, "Email:", emailField);
        addFormField(formGrid, "Phone:", phoneField);

        JButton saveBtn = new ModernButton("Update");
        saveBtn.setBounds(60, 300, 110, 35);
        panel.add(saveBtn);

        JButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setBounds(210, 300, 110, 35);
        cancelBtn.addActionListener(e -> dialog.dispose());
        panel.add(cancelBtn);
        
        saveBtn.addActionListener(e -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), false))) {
                for (String line : fileData) {
                    String[] data = line.split(",");
                    if (data[0].equals(idToEdit)) {
                        String updatedLine = data[0] + "," + data[1] + "," + userField.getText().trim() + "," +
                                new String(passField.getPassword()).trim() + "," + emailField.getText().trim() + "," +
                                phoneField.getText().trim();
                        
                        for(int i = 6; i < data.length; i++) updatedLine += "," + data[i];
                        bw.write(updatedLine);
                    } else {
                        bw.write(line);
                    }
                    bw.newLine();
                }
                new ModernDialog("User updated successfully!");
                dialog.dispose();
                loadUsers();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        dialog.setVisible(true);
    }

    // ==========================================
    // 🔥 DELETE FUNCTION
    // ==========================================
    private void deleteUser() {
        String idToDelete = showModernInputDialog("Enter User ID to Delete:");
        if (idToDelete == null || idToDelete.isEmpty()) return;

        if (idToDelete.equals(managerId)) {
            new ModernDialog("You cannot delete your own account!");
            return;
        }

        ArrayList<String> fileData = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(idToDelete)) fileData.add(line);
                else found = true;
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), false))) {
                for (String s : fileData) {
                    bw.write(s);
                    bw.newLine();
                }
                new ModernDialog("User successfully deleted.");
                loadUsers(); 
            } catch (Exception ex) { ex.printStackTrace(); }
        } else {
            new ModernDialog("User ID not found.");
        }
    }

    // ==========================================
    // 🔥 UI HELPERS & COMPONENTS
    // ==========================================
    
    private void addFormField(JPanel parent, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(label);
        
        styleField(field);
        parent.add(field);
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private JButton createButton(String text, int x, int y, int width) {
        JButton btn = new ModernButton(text);
        btn.setBounds(x, y, width, 35);
        return btn;
    }

    // Custom Input Dialog to replace JOptionPane.showInputDialog
    private String showModernInputDialog(String message) {
        JDialog dialog = new JDialog(MainUI.instance, true);
        dialog.setUndecorated(true);
        dialog.setSize(320, 160);
        dialog.setLocationRelativeTo(MainUI.instance);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(35, 35, 50));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));
        dialog.add(panel);

        JLabel msg = new JLabel(message);
        msg.setForeground(Color.WHITE);
        msg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        msg.setBounds(30, 20, 260, 30);
        panel.add(msg);

        JTextField input = new JTextField();
        input.setBounds(30, 60, 260, 30);
        styleField(input);
        panel.add(input);

        String[] result = new String[1]; // Array hack to get value out of lambda

        JButton okBtn = new ModernButton("OK");
        okBtn.setBounds(40, 110, 100, 30);
        okBtn.addActionListener(e -> {
            result[0] = input.getText().trim();
            dialog.dispose();
        });
        panel.add(okBtn);

        JButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setBounds(180, 110, 100, 30);
        cancelBtn.addActionListener(e -> {
            result[0] = null;
            dialog.dispose();
        });
        panel.add(cancelBtn);

        dialog.setVisible(true);
        return result[0];
    }
}