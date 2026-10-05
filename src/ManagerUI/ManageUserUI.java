package ManagerUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import UI.Components.*;

public class ManageUserUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String managerId;

    private JComboBox<String> roleSelect;
    private JTextField txtID, txtName, txtUsername, txtEmail, txtPhone, txtDate;
    private JPasswordField txtPassword;
    private JTextField txtSkills, txtExp;
    private JLabel lblSkills, lblExp;
    
    private JTable table;
    private DefaultTableModel tableModel;

    public ManageUserUI(String id) {
        this.managerId = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(960, 580));
        card.setLayout(null);

        JLabel title = new JLabel("MANAGE USERS", SwingConstants.CENTER);
        title.setBounds(0, 15, 960, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        int startX = 30, fieldX = 140, fieldW = 180;
        int y = 70;

        addLabel(card, "Role:", startX, y);
        roleSelect = new JComboBox<>(new String[]{"Manager", "Counter Staff", "Technician"});
        styleCombo(roleSelect, fieldX, y, fieldW);
        card.add(roleSelect);
        y += 40;

        addLabel(card, "User ID:", startX, y);
        txtID = addField(card, fieldX, y, fieldW);
        y += 40;

        addLabel(card, "Name:", startX, y);
        txtName = addField(card, fieldX, y, fieldW);
        y += 40;

        addLabel(card, "Username:", startX, y);
        txtUsername = addField(card, fieldX, y, fieldW);
        y += 40;

        addLabel(card, "Password:", startX, y);
        txtPassword = new JPasswordField();
        styleField(txtPassword, fieldX, y, fieldW);
        card.add(txtPassword);
        y += 40;

        addLabel(card, "Email:", startX, y);
        txtEmail = addField(card, fieldX, y, fieldW);
        y += 40;

        addLabel(card, "Phone:", startX, y);
        txtPhone = addField(card, fieldX, y, fieldW);
        y += 40;

        addLabel(card, "Date Joined:", startX, y);
        txtDate = addField(card, fieldX, y, fieldW);
        y += 40;

        lblSkills = addLabel(card, "Skills:", startX, y);
        txtSkills = addField(card, fieldX, y, fieldW);
        y += 40;

        lblExp = addLabel(card, "Experience:", startX, y);
        txtExp = addField(card, fieldX, y, fieldW);

        toggleTechFields(false);

        JButton createBtn = new ModernButton("Create");
        JButton updateBtn = new ModernButton("Update");
        JButton deleteBtn = new ModernButton("Delete");
        JButton clearBtn  = new ModernButton("Clear");

        createBtn.setBounds(30, 480, 135, 35);
        updateBtn.setBounds(185, 480, 135, 35);
        deleteBtn.setBounds(30, 525, 135, 35);
        clearBtn.setBounds(185, 525, 135, 35);

        card.add(createBtn);
        card.add(updateBtn);
        card.add(deleteBtn);
        card.add(clearBtn);

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBounds(340, 70, 2, 490);
        sep.setForeground(new Color(80, 80, 110));
        card.add(sep);

        String[] columns = {"ID", "NAME", "USERNAME", "EMAIL", "PHONE", "JOINED"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        setupTableStyle();

        table.getColumnModel().getColumn(0).setPreferredWidth(60);  
        table.getColumnModel().getColumn(1).setPreferredWidth(120); 
        table.getColumnModel().getColumn(2).setPreferredWidth(100); 
        table.getColumnModel().getColumn(3).setPreferredWidth(140); 
        table.getColumnModel().getColumn(4).setPreferredWidth(100); 
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(360, 70, 570, 490);
        sp.getViewport().setBackground(new Color(25, 25, 35));
        sp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 50, 80), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(sp);

        roleSelect.addActionListener(e -> {
            boolean isTech = roleSelect.getSelectedItem().equals("Technician");
            toggleTechFields(isTech);
            clearFields();
            loadTableData();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                String selectedId = table.getValueAt(table.getSelectedRow(), 0).toString();
                loadUserDataIntoForm(selectedId);
            }
        });

        createBtn.addActionListener(e -> createUser());
        updateBtn.addActionListener(e -> updateUser());
        deleteBtn.addActionListener(e -> deleteUser());
        clearBtn.addActionListener(e -> clearFields());

        loadTableData();
        add(card);
    }


    private void setupTableStyle() {
        table.setBackground(new Color(25, 25, 35));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setGridColor(new Color(100, 100, 130));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        table.setSelectionBackground(new Color(180, 35, 55));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(new Color(255, 50, 80)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private String getFileName() {
        String role = (String) roleSelect.getSelectedItem();
        if (role.equals("Manager")) return "manager.txt";
        if (role.equals("Counter Staff")) return "counter.txt";
        return "technician.txt";
    }

    private void toggleTechFields(boolean visible) {
        lblSkills.setVisible(visible);
        txtSkills.setVisible(visible);
        lblExp.setVisible(visible);
        txtExp.setVisible(visible);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        boolean isTech = roleSelect.getSelectedItem().equals("Technician");

        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 6) {
                    String dateJoined = "";
                    if (isTech) {
                        dateJoined = (d.length >= 9) ? d[8] : "";
                    } else {
                        dateJoined = (d.length >= 7) ? d[6] : ""; 
                    }
                    tableModel.addRow(new Object[]{d[0], d[1], d[2], d[4], d[5], dateJoined});
                }
            }
        } catch (Exception e) {}
    }

    private void loadUserDataIntoForm(String id) {
        boolean isTech = roleSelect.getSelectedItem().equals("Technician");
        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].equals(id)) {
                    txtID.setText(d[0]);
                    txtName.setText(d[1]);
                    txtUsername.setText(d[2]);
                    txtPassword.setText(d[3]);
                    txtEmail.setText(d[4]);
                    txtPhone.setText(d[5]);
                    
                    if (isTech) {
                        txtSkills.setText(d.length >= 7 ? d[6] : "");
                        txtExp.setText(d.length >= 8 ? d[7] : "");
                        txtDate.setText(d.length >= 9 ? d[8] : "");
                    } else {
                        txtDate.setText(d.length >= 7 ? d[6] : "");
                        txtSkills.setText("");
                        txtExp.setText("");
                    }
                    break;
                }
            }
        } catch (Exception e) {}
    }


    private void createUser() {
        if (!validateFields()) return;
        
        String id = txtID.getText().trim();
        if (idExists(id)) {
            new ModernDialog("ID already exists!");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), true))) {
            bw.write(buildRecordString());
            bw.newLine();
        } catch (Exception e) { e.printStackTrace(); }
        
        new ModernDialog("User created successfully!");
        SystemLogger.log(managerId, "Manager", "Created new " + roleSelect.getSelectedItem() + " account for ID: " + id);
        clearFields();
        loadTableData();
    }

    private void updateUser() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !idExists(id)) {
            new ModernDialog("Select a valid user to update.");
            return;
        }
        if (!validateFields()) return;

        ModernDialog confirm = new ModernDialog("Are you sure you want to update " + id + "?", true);
        if (!confirm.isConfirmed()) return; 

        ArrayList<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(id)) {
                    records.add(buildRecordString()); 
                } else {
                    records.add(line);
                }
            }
        } catch (Exception e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), false))) {
            for (String r : records) {
                bw.write(r);
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        new ModernDialog("User updated successfully!");
        SystemLogger.log(managerId, "Manager", "Updated account details for ID: " + id);
        loadTableData();
    }

    private void deleteUser() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !idExists(id)) {
            new ModernDialog("Select a valid user to delete.");
            return;
        }
        if (id.equals(managerId)) {
            new ModernDialog("You cannot delete your own account!");
            return;
        }

        ModernDialog confirm = new ModernDialog("Are you sure you want to completely delete user " + id + "?", true);
        if (!confirm.isConfirmed()) return; 

        ArrayList<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.split(",")[0].equals(id)) records.add(line);
            }
        } catch (Exception e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), false))) {
            for (String r : records) {
                bw.write(r);
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        new ModernDialog("User deleted successfully!");
        SystemLogger.log(managerId, "Manager", "Deleted account ID: " + id);
        clearFields();
        loadTableData();
    }

    private boolean validateFields() {
        String role = (String) roleSelect.getSelectedItem();
        String id = txtID.getText().trim();
        
        if (id.isEmpty() || txtName.getText().trim().isEmpty() || txtUsername.getText().trim().isEmpty() ||
            new String(txtPassword.getPassword()).trim().isEmpty() || txtEmail.getText().trim().isEmpty() || 
            txtPhone.getText().trim().isEmpty() || txtDate.getText().trim().isEmpty()) {
            new ModernDialog("All base fields must be filled!");
            return false;
        }

        if (role.equals("Manager") && !id.matches("M\\d{3}")) { new ModernDialog("Manager ID must be MXXX"); return false; }
        if (role.equals("Counter Staff") && !id.matches("CS\\d{3}")) { new ModernDialog("Counter Staff ID must be CSXXX"); return false; }
        if (role.equals("Technician") && !id.matches("T\\d{3}")) { new ModernDialog("Technician ID must be TXXX"); return false; }

        if (!txtEmail.getText().trim().contains("@")) { new ModernDialog("Invalid email format!"); return false; }
        if (!txtPhone.getText().trim().matches("0\\d{2}-\\d{3}-\\d{4}")) { new ModernDialog("Phone must be 0XX-XXX-XXXX"); return false; }
        if (!txtDate.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) { new ModernDialog("Date must be YYYY-MM-DD"); return false; }

        if (role.equals("Technician")) {
            if (txtSkills.getText().trim().isEmpty()) txtSkills.setText("-");
            if (!txtExp.getText().trim().matches("\\d+")) {
                new ModernDialog("Experience must be a numeric value!");
                return false;
            }
        }
        return true;
    }

    private String buildRecordString() {
        boolean isTech = roleSelect.getSelectedItem().equals("Technician");
        String base = txtID.getText().trim() + "," + txtName.getText().trim() + "," + txtUsername.getText().trim() + "," +
                      new String(txtPassword.getPassword()).trim() + "," + txtEmail.getText().trim() + "," + txtPhone.getText().trim();
        
        if (isTech) {
            return base + "," + txtSkills.getText().trim() + "," + txtExp.getText().trim() + "," + txtDate.getText().trim() + ",0";
        } else {
            return base + "," + txtDate.getText().trim();
        }
    }

    private boolean idExists(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(id)) return true;
            }
        } catch (Exception e) {}
        return false;
    }

    private void clearFields() {
        txtID.setText("");
        txtName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtDate.setText("");
        txtSkills.setText("");
        txtExp.setText("");
        table.clearSelection();
    }


    private JLabel addLabel(JPanel card, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 100, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(label);
        return label;
    }

    private JTextField addField(JPanel card, int x, int y, int w) {
        JTextField tf = new JTextField();
        styleField(tf, x, y, w);
        card.add(tf);
        return tf;
    }

    private void styleField(JTextField tf, int x, int y, int w) {
        tf.setBounds(x, y, w, 30);
        tf.setBackground(new Color(55, 55, 75));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 50, 80), 1), 
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private void styleCombo(JComboBox<String> box, int x, int y, int w) {
        box.setBounds(x, y, w, 30);
        box.setBackground(new Color(55, 55, 75));
        box.setForeground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(new Color(255, 50, 80), 1));
        box.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▼");
                btn.setBackground(new Color(55, 55, 75));
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                return btn;
            }
        });
    }
}