package CounterStaffUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import UTILS.FileUtil;

public class ManageCustomerMenu extends JFrame {

    private static final long serialVersionUID = 1L;
    private String counterID;
    private String mode;

    // ── UI Components ──────────────────────────────────────────────
    private JTextField txtID, txtName, txtUsername, txtPassword, txtEmail, txtPhone;
    private JTextField txtVehicleModel, txtRegNo, txtYear;
    private JComboBox<String> typeBox;
    private JTextArea textArea;

    // ── Constructor ────────────────────────────────────────────────
    public ManageCustomerMenu(String counterID, String mode) {
        this.counterID = counterID;
        this.mode = mode;

        setTitle("Manage Customer - " + mode);
        setSize(760, 560);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        applyModeConfig();
        setVisible(true);
    }

    // ── UI Initialization ──────────────────────────────────────────
    private void initComponents() {
        JLabel title = new JLabel("MANAGE CUSTOMER - " + mode);
        title.setBounds(260, 10, 220, 30);
        add(title);

        // Input fields
        addLabel("Customer ID:",      40,  60);
        txtID = addField(180, 60);

        addLabel("Name:",             40, 100);
        txtName = addField(180, 100);

        addLabel("Username:",         40, 140);
        txtUsername = addField(180, 140);

        addLabel("Password:",         40, 180);
        txtPassword = addField(180, 180);

        addLabel("Email:",            40, 220);
        txtEmail = addField(180, 220);

        addLabel("Phone:",            40, 260);
        txtPhone = addField(180, 260);

        addLabel("Vehicle Model:",    40, 300);
        txtVehicleModel = addField(180, 300);

        addLabel("Registration No:",  40, 340);
        txtRegNo = addField(180, 340);

        addLabel("Vehicle Year:",     40, 380);
        txtYear = addField(180, 380);

        addLabel("Type:",             40, 420);
        typeBox = new JComboBox<>(new String[]{"Walk-in", "Booking"});
        typeBox.setBounds(180, 420, 220, 25);
        add(typeBox);

        // Buttons
        JButton loadBtn    = new JButton("Load by ID");
        JButton viewAllBtn = new JButton("View All");
        JButton clearBtn   = new JButton("Clear");
        JButton backBtn    = new JButton("Back");

        loadBtn.setBounds(470,    110, 180, 30);
        viewAllBtn.setBounds(470, 150, 180, 30);
        clearBtn.setBounds(470,   190, 180, 30);
        backBtn.setBounds(470,    230, 180, 30);

        add(loadBtn);
        add(viewAllBtn);
        add(clearBtn);
        add(backBtn);

        // Text area
        textArea = new JTextArea();
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(430, 290, 270, 170);
        add(sp);

        // ── Action Listeners ───────────────────────────────────────
        loadBtn.addActionListener(e    -> loadCustomerByID());
        viewAllBtn.addActionListener(e -> viewAllCustomers());
        clearBtn.addActionListener(e   -> clearFields());
        backBtn.addActionListener(e -> {
            new ManagementMenu(counterID);
            dispose();
        });
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        add(label);
    }

    private JTextField addField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 220, 25);
        add(tf);
        return tf;
    }

    // ── Mode Configuration ─────────────────────────────────────────
    private void applyModeConfig() {
        switch (mode) {
            case "READ":
            case "DELETE":
                setFieldsEditable(false);
                txtID.setEditable(true);
                break;

            default: // CREATE, UPDATE
                JButton actionBtn = new JButton(mode);
                actionBtn.setBounds(470, 70, 180, 30);
                add(actionBtn);
                actionBtn.addActionListener(e -> performAction());
                break;
        }
    }

    private void setFieldsEditable(boolean editable) {
        txtID.setEditable(editable);
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

    // ── Action Dispatcher ──────────────────────────────────────────
    private void performAction() {
        switch (mode) {
            case "CREATE": createCustomer(); break;
            case "READ":   readCustomer();   break;
            case "UPDATE": updateCustomer(); break;
            case "DELETE": deleteCustomer(); break;
        }
    }

    // ── Create Customer ────────────────────────────────────────────
    private void createCustomer() {
        // 1. Validate fields
        if (!validateCustomerFields()) return;

        String id = txtID.getText().trim();

        // 2. Duplicate ID check
        if (customerIDExists(id)) {
            JOptionPane.showMessageDialog(this, "Customer ID already exists.");
            return;
        }

        // 3. Save record
        FileUtil.appendFile("customer.txt", buildCustomerLine(id));
        JOptionPane.showMessageDialog(this, "Customer created successfully.");
        viewAllCustomers();
        clearFields();
    }

    // ── Read Customer ──────────────────────────────────────────────
    private void readCustomer() {
        String id = txtID.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Customer ID.");
            return;
        }

        List<String> lines = FileUtil.readFile("customer.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(id)) {
                fillFields(data);
                JOptionPane.showMessageDialog(this, "Customer found.");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Customer not found.");
    }

    // ── Update Customer ────────────────────────────────────────────
    private void updateCustomer() {
        // 1. Validate fields
        if (!validateCustomerFields()) return;

        String id = txtID.getText().trim();

        // 2. Find and update record
        List<String> lines = FileUtil.readFile("customer.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (data[0].equals(id)) {
                lines.set(i, buildCustomerLine(id));
                FileUtil.writeFile("customer.txt", lines);
                JOptionPane.showMessageDialog(this, "Customer updated successfully.");
                viewAllCustomers();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Customer ID not found.");
    }

    // ── Delete Customer ────────────────────────────────────────────
    private void deleteCustomer() {
        String id = txtID.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Customer ID.");
            return;
        }

        List<String> lines    = FileUtil.readFile("customer.txt");
        List<String> newLines = new ArrayList<>();
        boolean found         = false;

        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(id)) {
                found = true;
            } else {
                newLines.add(line);
            }
        }

        if (found) {
            FileUtil.writeFile("customer.txt", newLines);
            JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
            clearFields();
            viewAllCustomers();
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found.");
        }
    }

    // ── Load Customer ──────────────────────────────────────────────
    private void loadCustomerByID() {
        String id = txtID.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Customer ID first.");
            return;
        }

        List<String> lines = FileUtil.readFile("customer.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(id)) {
                fillFields(data);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Customer not found.");
    }

    // ── View All ───────────────────────────────────────────────────
    private void viewAllCustomers() {
        List<String> lines = FileUtil.readFile("customer.txt");
        textArea.setText("=== CUSTOMERS ===\n");
        for (String line : lines) {
            textArea.append(line + "\n");
        }
    }

    // ── Clear Fields ───────────────────────────────────────────────
    private void clearFields() {
        txtID.setText("");
        txtName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtVehicleModel.setText("");
        txtRegNo.setText("");
        txtYear.setText("");
        typeBox.setSelectedIndex(0);
    }

    // ── Helpers & Validation ───────────────────────────────────────
    private void fillFields(String[] data) {
        txtID.setText(data[0]);
        txtName.setText(data[1]);
        txtUsername.setText(data[2]);
        txtPassword.setText(data[3]);
        txtEmail.setText(data[4]);
        txtPhone.setText(data[5]);
        txtVehicleModel.setText(data[6]);
        txtRegNo.setText(data[7]);
        txtYear.setText(data[8]);
        typeBox.setSelectedItem(data[9]);
    }

    /**
     * Builds the CSV line from current field values.
     * Call only after validateCustomerFields() passes.
     */
    private String buildCustomerLine(String id) {
        return id + "," +
               txtName.getText().trim()         + "," +
               txtUsername.getText().trim()      + "," +
               txtPassword.getText().trim()      + "," +
               txtEmail.getText().trim()         + "," +
               txtPhone.getText().trim()         + "," +
               txtVehicleModel.getText().trim()  + "," +
               txtRegNo.getText().trim()         + "," +
               txtYear.getText().trim()          + "," +
               typeBox.getSelectedItem().toString();
    }

    private boolean customerIDExists(String id) {
        List<String> lines = FileUtil.readFile("customer.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private boolean validateCustomerFields() {
        String id    = txtID.getText().trim();
        String name  = txtName.getText().trim();
        String user  = txtUsername.getText().trim();
        String pass  = txtPassword.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String model = txtVehicleModel.getText().trim();
        String reg   = txtRegNo.getText().trim();
        String year  = txtYear.getText().trim();

        // 1. ID format check
        if (!id.matches("C\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Customer ID must be like C001.");
            return false;
        }

        // 2. Empty field check
        if (name.isEmpty() || user.isEmpty() || pass.isEmpty() || email.isEmpty() ||
            phone.isEmpty() || model.isEmpty() || reg.isEmpty() || year.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return false;
        }

        // 3. Email format check
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return false;
        }

        // 4. Phone format check
        if (!phone.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Phone must contain numbers only.");
            return false;
        }

        // 5. Year format check
        if (!year.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Vehicle year must be 4 digits.");
            return false;
        }

        return true;
    }
}