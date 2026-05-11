package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import UTILS.FileUtil;
import UI.Components.*;

public class ManageAppointment extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;
    private String mode;

    private JTextField txtAppointmentID, txtCustomerID, txtDate, txtTask, txtDuration;
    private JTextField txtTechnicianID, txtTechnicianName;
    private JComboBox<String> serviceBox, statusBox;
    private JTextArea textArea;
    private JLabel lblTechID, lblTechName;

    public ManageAppointment(String counterID, String mode) {
        this.counterID = counterID;
        this.mode = mode;

        setOpaque(false); // 🔥 Transparent for SPA
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(760, 560));
        card.setLayout(null);

        initComponents(card);
        applyModeConfig(card);
        updateDuration();
        
        add(card);
    }

    private void initComponents(JPanel card) {
        JLabel title = new JLabel("MANAGE APPOINTMENT - " + mode, SwingConstants.CENTER);
        title.setBounds(0, 15, 760, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        addLabel(card, "Appointment ID:", 40, 60);
        txtAppointmentID = addField(card, 180, 60);

        addLabel(card, "Customer ID:", 40, 100);
        txtCustomerID = addField(card, 180, 100);

        addLabel(card, "Date (YYYY-MM-DD):", 40, 140);
        txtDate = addField(card, 180, 140);

        addLabel(card, "Service Type:", 40, 180);
        serviceBox = new JComboBox<>(new String[]{"Normal", "Major"});
        serviceBox.setBounds(180, 180, 220, 30);
        card.add(serviceBox);

        addLabel(card, "Status:", 40, 220);
        statusBox = new JComboBox<>(new String[]{"Unpaid", "Paid", "Cancel"});
        statusBox.setBounds(180, 220, 220, 30);
        card.add(statusBox);

        addLabel(card, "Task:", 40, 260);
        txtTask = addField(card, 180, 260);

        addLabel(card, "Duration:", 40, 300);
        txtDuration = addField(card, 180, 300);
        txtDuration.setEditable(false);

        lblTechID = new JLabel("Technician ID:");
        lblTechID.setBounds(40, 340, 130, 25);
        lblTechID.setForeground(Color.WHITE);
        card.add(lblTechID);
        txtTechnicianID = addField(card, 180, 340);

        lblTechName = new JLabel("Technician Name:");
        lblTechName.setBounds(40, 380, 130, 25);
        lblTechName.setForeground(Color.WHITE);
        card.add(lblTechName);
        txtTechnicianName = addField(card, 180, 380);

        // 🔥 ModernButtons applied
        JButton actionBtn          = new ModernButton(mode);
        JButton loadBtn            = new ModernButton("Load by ID");
        JButton viewAppointmentsBtn = new ModernButton("View Appointments");
        JButton viewTechBtn        = new ModernButton("View Technicians");
        JButton clearBtn           = new ModernButton("Clear");
        JButton backBtn            = new ModernButton("Back");

        actionBtn.setBounds(500,           70, 180, 35);
        loadBtn.setBounds(500,            115, 180, 35);
        viewAppointmentsBtn.setBounds(500, 160, 180, 35);
        viewTechBtn.setBounds(500,        205, 180, 35);
        clearBtn.setBounds(500,           250, 180, 35);
        backBtn.setBounds(500,            295, 180, 35);

        card.add(actionBtn);
        card.add(loadBtn);
        card.add(viewAppointmentsBtn);
        card.add(viewTechBtn);
        card.add(clearBtn);
        card.add(backBtn);

        textArea = new JTextArea();
        textArea.setBackground(new Color(20, 20, 30));
        textArea.setForeground(new Color(0, 200, 255));
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(40, 440, 680, 100);
        card.add(sp);

        serviceBox.addActionListener(e -> updateDuration());
        actionBtn.addActionListener(e -> performAction());
        loadBtn.addActionListener(e -> loadAppointment());
        viewAppointmentsBtn.addActionListener(e -> viewAppointments());
        viewTechBtn.addActionListener(e -> viewTechnicians());
        clearBtn.addActionListener(e -> clearFields());
        
        // 🔥 SPA Back Router
        backBtn.addActionListener(e -> {
            CounterStaffMenu.instance.rightContainer.add(new Appointment(counterID), "APPOINTMENT");
            CounterStaffMenu.instance.showRightPage("APPOINTMENT");
        });
    }

    private void addLabel(JPanel card, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 130, 25);
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
            case "UPDATE":
                txtCustomerID.setEditable(true);
                txtDuration.setEditable(false);
                break;
            case "ASSIGN":
                txtCustomerID.setEditable(false);
                txtDate.setEditable(false);
                txtTask.setEditable(false);
                txtDuration.setEditable(false);
                serviceBox.setEnabled(false);
                statusBox.setEnabled(false);
                break;
            default: // CREATE
                lblTechID.setVisible(false);
                lblTechName.setVisible(false);
                txtTechnicianID.setVisible(false);
                txtTechnicianName.setVisible(false);
                break;
        }
    }

    private void performAction() {
        switch (mode) {
            case "CREATE": createAppointment(); break;
            case "UPDATE": updateAppointment(); break;
            case "ASSIGN": assignTechnician();  break;
        }
    }

    // 🔥 Replaced JOptionPane with beautiful ModernDialogs
    private void createAppointment() {
        String id = txtAppointmentID.getText().trim();
        String customerID = txtCustomerID.getText().trim();
        String date = txtDate.getText().trim();
        String task = txtTask.getText().trim();

        if (id.isEmpty() || customerID.isEmpty() || date.isEmpty() || task.isEmpty()) {
            new ModernDialog("Please fill in all required fields.");
            return;
        }
        if (!id.matches("A\\d{3}")) {
            new ModernDialog("Appointment ID must be like A001.");
            return;
        }
        if (!customerExists(customerID)) {
            new ModernDialog("Customer ID does not exist.");
            return;
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            new ModernDialog("Date must be YYYY-MM-DD.");
            return;
        }
        if (appointmentIDExists(id)) {
            new ModernDialog("Appointment ID already exists.");
            return;
        }

        String line = id + "," + customerID + "," + date + "," + serviceBox.getSelectedItem() + "," +
                      statusBox.getSelectedItem() + ",Pending," + task + "," + txtDuration.getText() + ",NA";
        FileUtil.appendFile("appointment.txt", line);

        new ModernDialog("Appointment created successfully.");
        viewAppointments();
        clearFields();
    }

    private void updateAppointment() {
        String id = txtAppointmentID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Appointment ID."); return; }

        List<String> lines = FileUtil.readFile("appointment.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (data[0].equals(id)) {
                if (data[5].equalsIgnoreCase("Done")) {
                    new ModernDialog("Done task cannot be updated.");
                    return;
                }

                String newTechID = txtTechnicianID.getText().trim();
                String newTechName = txtTechnicianName.getText().trim();

                if (!newTechID.isEmpty() && !newTechName.isEmpty()) {
                    if (!statusBox.getSelectedItem().toString().equalsIgnoreCase("Paid")) {
                        new ModernDialog("Only PAID appointments can assign technician.");
                        return;
                    }
                    if (!isTechnicianValid(newTechID, newTechName)) return;
                    data[8] = newTechID;
                }

                lines.set(i, id + "," + txtCustomerID.getText() + "," + txtDate.getText() + "," + 
                             serviceBox.getSelectedItem() + "," + statusBox.getSelectedItem() + "," + 
                             data[5] + "," + txtTask.getText() + "," + txtDuration.getText() + "," + data[8]);

                FileUtil.writeFile("appointment.txt", lines);
                new ModernDialog("Appointment updated successfully.");
                viewAppointments();
                return;
            }
        }
        new ModernDialog("Appointment not found.");
    }

    private void assignTechnician() {
        String appID = txtAppointmentID.getText().trim();
        String techID = txtTechnicianID.getText().trim();
        String techName = txtTechnicianName.getText().trim();

        if (appID.isEmpty() || techID.isEmpty() || techName.isEmpty()) {
            new ModernDialog("Enter Appointment ID, Tech ID, and Tech Name.");
            return;
        }

        List<String> appointments = FileUtil.readFile("appointment.txt");
        for (int i = 0; i < appointments.size(); i++) {
            String[] data = appointments.get(i).split(",");
            if (data[0].equals(appID)) {
                if (!data[4].equalsIgnoreCase("Paid")) {
                    new ModernDialog("Only PAID appointments can be assigned.");
                    return;
                }
                if (!isTechnicianValid(techID, techName)) return;

                data[8] = techID;
                appointments.set(i, String.join(",", data));
                FileUtil.writeFile("appointment.txt", appointments);
                
                new ModernDialog("Technician assigned successfully.");
                viewAppointments();
                return;
            }
        }
        new ModernDialog("Appointment not found.");
    }

    private void loadAppointment() {
        String id = txtAppointmentID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Appointment ID."); return; }

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
        new ModernDialog("Appointment not found.");
    }

    private void viewAppointments() {
        List<String> lines = FileUtil.readFile("appointment.txt");
        textArea.setText("=== APPOINTMENTS ===\n");
        for (String line : lines) textArea.append(line + "\n");
        textArea.setCaretPosition(0);
    }

    private void viewTechnicians() {
        List<String> lines = FileUtil.readFile("technician.txt");
        textArea.setText("=== TECHNICIANS ===\n");
        for (String line : lines) textArea.append(line + "\n");
        textArea.setCaretPosition(0);
    }

    private void clearFields() {
        txtAppointmentID.setText("");
        txtCustomerID.setText("");
        txtDate.setText("");
        txtTask.setText("");
        if (txtTechnicianID != null) txtTechnicianID.setText("");
        if (txtTechnicianName != null) txtTechnicianName.setText("");
        serviceBox.setSelectedIndex(0);
        statusBox.setSelectedItem("Unpaid");
        updateDuration();
    }

    private void updateDuration() {
        txtDuration.setText(serviceBox.getSelectedItem().equals("Normal") ? "1" : "3");
    }

    private boolean customerExists(String customerID) {
        for (String line : FileUtil.readFile("customer.txt")) {
            if (line.split(",")[0].equals(customerID)) return true;
        }
        return false;
    }

    private boolean appointmentIDExists(String appointmentID) {
        for (String line : FileUtil.readFile("appointment.txt")) {
            if (line.split(",")[0].equals(appointmentID)) return true;
        }
        return false;
    }

    private boolean isTechnicianValid(String technicianID, String technicianName) {
        for (String techLine : FileUtil.readFile("technician.txt")) {
            String[] techData = techLine.split(",");
            if (techData[0].equals(technicianID) && techData[1].equalsIgnoreCase(technicianName)) {
                if (countPendingJobs(technicianID) >= 3) {
                    new ModernDialog("Technician already has 3 pending tasks.");
                    return false;
                }
                return true;
            }
        }
        new ModernDialog("Technician ID and Name do not match.");
        return false;
    }

    private int countPendingJobs(String technicianID) {
        int count = 0;
        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] data = line.split(",");
            if (data[8].equals(technicianID) && (data[5].equalsIgnoreCase("Pending") || data[5].equalsIgnoreCase("In Progress"))) {
                count++;
            }
        }
        return count;
    }
}