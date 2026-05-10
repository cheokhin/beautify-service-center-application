package CounterStaffUI;

import javax.swing.*;
import java.util.List;
import UTILS.FileUtil;

public class ManageAppointment extends JFrame {

    private static final long serialVersionUID = 1L;
    private String counterID;
    private String mode;

    // ── UI Components ──────────────────────────────────────────────
    private JTextField txtAppointmentID, txtCustomerID, txtDate, txtTask, txtDuration;
    private JTextField txtTechnicianID, txtTechnicianName;
    private JComboBox<String> serviceBox, statusBox;
    private JTextArea textArea;

    // Technician labels kept as fields so mode config can show/hide them
    private JLabel lblTechID, lblTechName;

    // ── Constructor ────────────────────────────────────────────────
    public ManageAppointment(String counterID, String mode) {
        this.counterID = counterID;
        this.mode = mode;

        setTitle("Manage Appointment - " + mode);
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        applyModeConfig();
        updateDuration();
        setVisible(true);
    }

    // ── UI Initialization ──────────────────────────────────────────
    private void initComponents() {
        JLabel title = new JLabel("MANAGE APPOINTMENT - " + mode);
        title.setBounds(280, 15, 250, 30);
        add(title);

        // Input fields
        addLabel("Appointment ID:",    40, 60);
        txtAppointmentID = addField(180, 60);

        addLabel("Customer ID:",       40, 100);
        txtCustomerID = addField(180, 100);

        addLabel("Date (YYYY-MM-DD):", 40, 140);
        txtDate = addField(180, 140);

        addLabel("Service Type:",      40, 180);
        serviceBox = new JComboBox<>(new String[]{"Normal", "Major"});
        serviceBox.setBounds(180, 180, 220, 25);
        add(serviceBox);

        addLabel("Status:",            40, 220);
        statusBox = new JComboBox<>(new String[]{"Unpaid", "Paid", "Cancel"});
        statusBox.setBounds(180, 220, 220, 25);
        add(statusBox);

        addLabel("Task:",              40, 260);
        txtTask = addField(180, 260);

        addLabel("Duration:",          40, 300);
        txtDuration = addField(180, 300);
        txtDuration.setEditable(false);

        // Technician fields
        lblTechID = new JLabel("Technician ID:");
        lblTechID.setBounds(40, 340, 130, 25);
        add(lblTechID);
        txtTechnicianID = addField(180, 340);

        lblTechName = new JLabel("Technician Name:");
        lblTechName.setBounds(40, 380, 130, 25);
        add(lblTechName);
        txtTechnicianName = addField(180, 380);

        // Buttons
        JButton actionBtn          = new JButton(mode);
        JButton loadBtn            = new JButton("Load by ID");
        JButton viewAppointmentsBtn = new JButton("View Appointments");
        JButton viewTechBtn        = new JButton("View Technicians");
        JButton clearBtn           = new JButton("Clear");
        JButton backBtn            = new JButton("Back");

        actionBtn.setBounds(500,           70, 180, 30);
        loadBtn.setBounds(500,            110, 180, 30);
        viewAppointmentsBtn.setBounds(500, 150, 180, 30);
        viewTechBtn.setBounds(500,        190, 180, 30);
        clearBtn.setBounds(500,           230, 180, 30);
        backBtn.setBounds(500,            270, 180, 30);

        add(actionBtn);
        add(loadBtn);
        add(viewAppointmentsBtn);
        add(viewTechBtn);
        add(clearBtn);
        add(backBtn);

        // Text area
        textArea = new JTextArea();
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(40, 440, 700, 100);
        add(sp);

        // ── Action Listeners ───────────────────────────────────────
        serviceBox.addActionListener(e -> updateDuration());
        actionBtn.addActionListener(e          -> performAction());
        loadBtn.addActionListener(e            -> loadAppointment());
        viewAppointmentsBtn.addActionListener(e -> viewAppointments());
        viewTechBtn.addActionListener(e        -> viewTechnicians());
        clearBtn.addActionListener(e           -> clearFields());
        backBtn.addActionListener(e -> {
            new Appointment(counterID);
            dispose();
        });
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 130, 25);
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
            case "UPDATE":
                txtCustomerID.setEditable(true);
                txtDate.setEditable(true);
                txtTask.setEditable(true);
                txtDuration.setEditable(false);
                serviceBox.setEnabled(true);
                statusBox.setEnabled(true);
                txtTechnicianID.setEditable(true);
                txtTechnicianName.setEditable(true);
                break;

            case "ASSIGN":
                txtCustomerID.setEditable(false);
                txtDate.setEditable(false);
                txtTask.setEditable(false);
                txtDuration.setEditable(false);
                serviceBox.setEnabled(false);
                statusBox.setEnabled(false);
                txtTechnicianID.setEditable(true);
                txtTechnicianName.setEditable(true);
                break;

            default: // CREATE
                lblTechID.setVisible(false);
                lblTechName.setVisible(false);
                txtTechnicianID.setVisible(false);
                txtTechnicianName.setVisible(false);
                break;
        }
    }

    // ── Action Dispatcher ──────────────────────────────────────────
    private void performAction() {
        switch (mode) {
            case "CREATE": createAppointment(); break;
            case "UPDATE": updateAppointment(); break;
            case "ASSIGN": assignTechnician();  break;
        }
    }

    // ── Create Appointment ─────────────────────────────────────────
    private void createAppointment() {
        String id            = txtAppointmentID.getText().trim();
        String customerID    = txtCustomerID.getText().trim();
        String date          = txtDate.getText().trim();
        String serviceType   = serviceBox.getSelectedItem().toString();
        String paymentStatus = statusBox.getSelectedItem().toString();
        String task          = txtTask.getText().trim();
        String duration      = txtDuration.getText().trim();

        // 1. Empty field check
        if (id.isEmpty() || customerID.isEmpty() || date.isEmpty() || task.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        // 2. Appointment ID format check
        if (!id.matches("A\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Appointment ID must be like A001.");
            return;
        }

        // 3. Customer existence check
        if (!customerExists(customerID)) {
            JOptionPane.showMessageDialog(this, "Customer ID does not exist.");
            return;
        }

        // 4. Date format check
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date must be YYYY-MM-DD.");
            return;
        }

        // 5. Duplicate appointment ID check
        if (appointmentIDExists(id)) {
            JOptionPane.showMessageDialog(this, "Appointment ID already exists.");
            return;
        }

        // 6. Save record
        String line = id + "," + customerID + "," + date + "," + serviceType + "," +
                      paymentStatus + ",Pending," + task + "," + duration + ",NA";
        FileUtil.appendFile("appointment.txt", line);

        JOptionPane.showMessageDialog(this, "Appointment created successfully.");
        viewAppointments();
        clearFields();
    }

    // ── Update Appointment ─────────────────────────────────────────
    private void updateAppointment() {
        String id = txtAppointmentID.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Appointment ID.");
            return;
        }

        String customerID  = txtCustomerID.getText().trim();
        String date        = txtDate.getText().trim();
        String serviceType = serviceBox.getSelectedItem().toString();
        String status      = statusBox.getSelectedItem().toString();
        String task        = txtTask.getText().trim();
        String duration    = txtDuration.getText().trim();

        if (customerID.isEmpty() || date.isEmpty() || task.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        List<String> lines = FileUtil.readFile("appointment.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (data[0].equals(id)) {

                // 1. Job status check
                if (data[5].equalsIgnoreCase("Done")) {
                    JOptionPane.showMessageDialog(this, "Done task cannot be updated.");
                    return;
                }

                // 2. Optional technician assignment during update
                String newTechID   = txtTechnicianID.getText().trim();
                String newTechName = txtTechnicianName.getText().trim();

                if (!newTechID.isEmpty() && !newTechName.isEmpty()) {
                    if (!status.equalsIgnoreCase("Paid")) {
                        JOptionPane.showMessageDialog(this, "Only PAID appointments can assign technician.");
                        return;
                    }

                    if (!isTechnicianValid(newTechID, newTechName)) return;
                    data[8] = newTechID;
                }

                // 3. Save updated record
                lines.set(i,
                    id + "," + customerID + "," + date + "," + serviceType + "," +
                    status + "," + data[5] + "," + task + "," + duration + "," + data[8]);

                FileUtil.writeFile("appointment.txt", lines);
                JOptionPane.showMessageDialog(this, "Appointment updated successfully.");
                viewAppointments();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Appointment not found.");
    }

    // ── Assign Technician ──────────────────────────────────────────
    private void assignTechnician() {
        String appointmentID  = txtAppointmentID.getText().trim();
        String technicianID   = txtTechnicianID.getText().trim();
        String technicianName = txtTechnicianName.getText().trim();

        // 1. Empty field check
        if (appointmentID.isEmpty() || technicianID.isEmpty() || technicianName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Appointment ID, Technician ID, and Technician Name.");
            return;
        }

        // 2. Find appointment
        List<String> appointments = FileUtil.readFile("appointment.txt");
        int appointmentIndex      = -1;
        String[] appointmentData  = null;

        for (int i = 0; i < appointments.size(); i++) {
            String[] data = appointments.get(i).split(",");
            if (data[0].equals(appointmentID)) {
                appointmentIndex = i;
                appointmentData  = data;
                break;
            }
        }

        if (appointmentData == null) {
            JOptionPane.showMessageDialog(this, "Appointment not found.");
            return;
        }

        // 3. Payment status check
        if (!appointmentData[4].equalsIgnoreCase("Paid")) {
            JOptionPane.showMessageDialog(this, "Only PAID appointments can be assigned.");
            return;
        }

        // 4. Validate technician
        if (!isTechnicianValid(technicianID, technicianName)) return;

        // 5. Save updated appointment with technician
        appointments.set(appointmentIndex,
            appointmentData[0] + "," + appointmentData[1] + "," + appointmentData[2] + "," +
            appointmentData[3] + "," + appointmentData[4] + "," + appointmentData[5] + "," +
            appointmentData[6] + "," + appointmentData[7] + "," + technicianID);

        FileUtil.writeFile("appointment.txt", appointments);
        JOptionPane.showMessageDialog(this, "Technician assigned successfully.");
        viewAppointments();
    }

    // ── Load Appointment ───────────────────────────────────────────
    private void loadAppointment() {
        String id = txtAppointmentID.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Appointment ID.");
            return;
        }

        List<String> lines = FileUtil.readFile("appointment.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(id)) {
                txtCustomerID.setText(data[1]);
                txtDate.setText(data[2]);
                serviceBox.setSelectedItem(data[3]);
                statusBox.setSelectedItem(data[4]);
                txtTask.setText(data[6]);
                txtDuration.setText(data[7]);
                txtTechnicianID.setText(data[8].equals("NA") ? "" : data[8]);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Appointment not found.");
    }

    // ── View ───────────────────────────────────────────────────────
    private void viewAppointments() {
        List<String> lines = FileUtil.readFile("appointment.txt");
        textArea.setText("=== APPOINTMENTS ===\n");
        for (String line : lines) {
            textArea.append(line + "\n");
        }
    }

    private void viewTechnicians() {
        List<String> lines = FileUtil.readFile("technician.txt");
        textArea.setText("=== TECHNICIANS ===\n");
        for (String line : lines) {
            textArea.append(line + "\n");
        }
    }

    // ── Clear Fields ───────────────────────────────────────────────
    private void clearFields() {
        txtAppointmentID.setText("");
        txtCustomerID.setText("");
        txtDate.setText("");
        txtTask.setText("");
        if (txtTechnicianID != null)   txtTechnicianID.setText("");
        if (txtTechnicianName != null) txtTechnicianName.setText("");
        serviceBox.setSelectedIndex(0);
        statusBox.setSelectedItem("Unpaid");
        updateDuration();
    }

    // ── Helpers & Validation ───────────────────────────────────────
    private void updateDuration() {
        txtDuration.setText(serviceBox.getSelectedItem().equals("Normal") ? "1" : "3");
    }

    private boolean customerExists(String customerID) {
        List<String> lines = FileUtil.readFile("customer.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(customerID)) return true;
        }
        return false;
    }

    private boolean appointmentIDExists(String appointmentID) {
        List<String> lines = FileUtil.readFile("appointment.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(appointmentID)) return true;
        }
        return false;
    }

    /**
     * Checks technician ID + name match and that they have fewer than 3 pending jobs.
     * Shows dialog and returns false if any check fails.
     */
    private boolean isTechnicianValid(String technicianID, String technicianName) {
        List<String> technicians = FileUtil.readFile("technician.txt");
        for (String techLine : technicians) {
            String[] techData = techLine.split(",");
            if (techData[0].equals(technicianID) && techData[1].equalsIgnoreCase(technicianName)) {
                if (countPendingJobs(technicianID) >= 3) {
                    JOptionPane.showMessageDialog(this, "Technician already has 3 pending tasks.");
                    return false;
                }
                return true;
            }
        }
        JOptionPane.showMessageDialog(this, "Technician ID and Name do not match.");
        return false;
    }

    private int countPendingJobs(String technicianID) {
        int count = 0;
        List<String> lines = FileUtil.readFile("appointment.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[8].equals(technicianID) &&
               (data[5].equalsIgnoreCase("Pending") || data[5].equalsIgnoreCase("In Progress"))) {
                count++;
            }
        }
        return count;
    }
}