package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import UTILS.FileUtil;
import UI.Components.*; 

public class ManageCustomerMenu extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;
    private String mode;

    private JTextField txtID, txtName, txtUsername, txtPassword, txtEmail, txtPhone;
    private JTextField txtVehicleModel, txtRegNo, txtYear;
    private JComboBox<String> typeBox;
    private JTextArea textArea;

    public ManageCustomerMenu(String counterID, String mode) {
        this.counterID = counterID;
        this.mode = mode;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(740, 520));
        card.setLayout(null);

        initComponents(card);
        applyModeConfig(card);
        
        add(card);
    }

    private void initComponents(JPanel card) {
        JLabel title = new JLabel("MANAGE CUSTOMER - " + mode, SwingConstants.CENTER);
        title.setBounds(0, 10, 740, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        addLabel(card, "Customer ID:", 40,  60);
        txtID = addField(card, 180, 60);

        addLabel(card, "Name:", 40, 100);
        txtName = addField(card, 180, 100);

        addLabel(card, "Username:", 40, 140);
        txtUsername = addField(card, 180, 140);

        addLabel(card, "Password:", 40, 180);
        txtPassword = addField(card, 180, 180);

        addLabel(card, "Email:", 40, 220);
        txtEmail = addField(card, 180, 220);

        addLabel(card, "Phone:", 40, 260);
        txtPhone = addField(card, 180, 260);

        addLabel(card, "Vehicle Model:", 40, 300);
        txtVehicleModel = addField(card, 180, 300);

        addLabel(card, "Registration No:", 40, 340);
        txtRegNo = addField(card, 180, 340);

        addLabel(card, "Vehicle Year:", 40, 380);
        txtYear = addField(card, 180, 380);

        addLabel(card, "Type:", 40, 420);
        typeBox = new JComboBox<>(new String[]{"Walk-in", "Booking"});
        typeBox.setBounds(180, 420, 220, 30);
        card.add(typeBox);

        JButton loadBtn    = new ModernButton("Load by ID");
        JButton viewAllBtn = new ModernButton("View All");
        JButton clearBtn   = new ModernButton("Clear");
        JButton backBtn    = new ModernButton("Back");

        loadBtn.setBounds(470,    110, 180, 35);
        viewAllBtn.setBounds(470, 160, 180, 35);
        clearBtn.setBounds(470,   210, 180, 35);
        backBtn.setBounds(470,    260, 180, 35);

        card.add(loadBtn);
        card.add(viewAllBtn);
        card.add(clearBtn);
        card.add(backBtn);

        textArea = new JTextArea();
        textArea.setBackground(new Color(20, 20, 30));
        textArea.setForeground(new Color(0, 200, 255));
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(440, 320, 260, 160);
        card.add(sp);

        loadBtn.addActionListener(e    -> loadCustomerByID());
        viewAllBtn.addActionListener(e -> viewAllCustomers());
        clearBtn.addActionListener(e   -> clearFields());
        
        // 🔥 SPA Back Router
        backBtn.addActionListener(e -> {
            CounterStaffMenu.instance.rightContainer.add(new ManagementMenu(counterID), "MANAGEMENT");
            CounterStaffMenu.instance.showRightPage("MANAGEMENT");
        });
    }

    private void addLabel(JPanel card, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        label.setForeground(Color.WHITE);
        card.add(label);
    }

    private JTextField addField(JPanel card, int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 220, 30);
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        card.add(tf);
        return tf;
    }

    private void applyModeConfig(JPanel card) {
        switch (mode) {
            case "READ":
            case "DELETE":
                setFieldsEditable(false);
                txtID.setEditable(true);
                break;
            default: // CREATE, UPDATE
                JButton actionBtn = new ModernButton(mode);
                actionBtn.setBounds(470, 60, 180, 35);
                card.add(actionBtn);
                actionBtn.addActionListener(e -> performAction());
                break;
        }
    }

    private void setFieldsEditable(boolean editable) {
        txtName.setEditable(editable);
        txtUsername.setEditable(editable);
        txtPassword.setEditable(editable);
        txtEmail.setEditable(editable);
        txtPhone.setEditable(editable);
        txtVehicleModel.setEditable(editable);
        txtRegNo.setEditable(editable);
        txtYear.setEditable(editable);
        typeBox.setEnabled(editable);
    }

    private void performAction() {
        switch (mode) {
            case "CREATE": createCustomer(); break;
            case "READ":   readCustomer();   break;
            case "UPDATE": updateCustomer(); break;
            case "DELETE": deleteCustomer(); break;
        }
    }

    private void createCustomer() {
        if (!validateCustomerFields()) return;
        String id = txtID.getText().trim();
        if (customerIDExists(id)) { new ModernDialog("Customer ID already exists."); return; }

        FileUtil.appendFile("customer.txt", buildCustomerLine(id));
        new ModernDialog("Customer created successfully.");
        viewAllCustomers();
        clearFields();
    }

    private void readCustomer() {
        String id = txtID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Customer ID."); return; }

        for (String line : FileUtil.readFile("customer.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(id)) {
                fillFields(data);
                new ModernDialog("Customer found.");
                return;
            }
        }
        new ModernDialog("Customer not found.");
    }

    private void updateCustomer() {
        if (!validateCustomerFields()) return;
        String id = txtID.getText().trim();

        List<String> lines = FileUtil.readFile("customer.txt");
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).split(",")[0].equals(id)) {
                lines.set(i, buildCustomerLine(id));
                FileUtil.writeFile("customer.txt", lines);
                new ModernDialog("Customer updated successfully.");
                viewAllCustomers();
                return;
            }
        }
        new ModernDialog("Customer ID not found.");
    }

    private void deleteCustomer() {
        String id = txtID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Customer ID."); return; }

        List<String> lines = FileUtil.readFile("customer.txt");
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            if (line.split(",")[0].equals(id)) found = true;
            else newLines.add(line);
        }

        if (found) {
            FileUtil.writeFile("customer.txt", newLines);
            new ModernDialog("Customer deleted successfully.");
            clearFields();
            viewAllCustomers();
        } else {
            new ModernDialog("Customer not found.");
        }
    }

    private void loadCustomerByID() {
        String id = txtID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Customer ID first."); return; }

        for (String line : FileUtil.readFile("customer.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(id)) { fillFields(data); return; }
        }
        new ModernDialog("Customer not found.");
    }

    private void viewAllCustomers() {
        textArea.setText("=== CUSTOMERS ===\n");
        for (String line : FileUtil.readFile("customer.txt")) textArea.append(line + "\n");
        textArea.setCaretPosition(0);
    }

    private void clearFields() {
        txtID.setText(""); txtName.setText(""); txtUsername.setText(""); txtPassword.setText("");
        txtEmail.setText(""); txtPhone.setText(""); txtVehicleModel.setText("");
        txtRegNo.setText(""); txtYear.setText(""); typeBox.setSelectedIndex(0);
    }

    private void fillFields(String[] data) {
        txtID.setText(data[0]); txtName.setText(data[1]); txtUsername.setText(data[2]); txtPassword.setText(data[3]);
        txtEmail.setText(data[4]); txtPhone.setText(data[5]); txtVehicleModel.setText(data[6]);
        txtRegNo.setText(data[7]); txtYear.setText(data[8]); typeBox.setSelectedItem(data[9]);
    }

    private String buildCustomerLine(String id) {
        return id + "," + txtName.getText().trim() + "," + txtUsername.getText().trim() + "," +
               txtPassword.getText().trim() + "," + txtEmail.getText().trim() + "," + txtPhone.getText().trim() + "," +
               txtVehicleModel.getText().trim() + "," + txtRegNo.getText().trim() + "," + txtYear.getText().trim() + "," +
               typeBox.getSelectedItem().toString();
    }

    private boolean customerIDExists(String id) {
        for (String line : FileUtil.readFile("customer.txt")) {
            if (line.split(",")[0].equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private boolean validateCustomerFields() {
        String id = txtID.getText().trim();
        if (!id.matches("C\\d{3}")) { new ModernDialog("Customer ID must be like C001."); return false; }
        if (txtName.getText().trim().isEmpty() || txtUsername.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty() || 
            txtEmail.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty() || txtVehicleModel.getText().trim().isEmpty() || 
            txtRegNo.getText().trim().isEmpty() || txtYear.getText().trim().isEmpty()) {
            new ModernDialog("Please fill in all fields."); return false;
        }
        if (!txtEmail.getText().trim().contains("@")) { new ModernDialog("Invalid email format."); return false; }
        if (!txtPhone.getText().trim().matches("\\d+")) { new ModernDialog("Phone must contain numbers only."); return false; }
        if (!txtYear.getText().trim().matches("\\d{4}")) { new ModernDialog("Vehicle year must be 4 digits."); return false; }
        return true;
    }
}