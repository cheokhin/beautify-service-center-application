package CounterStaffUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import UTILS.FileUtil;
import UI.Components.*;

public class CreateAppointment extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtAppointmentID, txtCustomerID, txtDate, txtTask, txtDuration;
    private JComboBox<String> serviceBox, statusBox;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTotalApps, lblLastID, lblPending;

    public CreateAppointment(String counterID) {
        this.counterID = counterID;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(760, 560));
        card.setLayout(null);

        JLabel title = new JLabel("CREATE APPOINTMENT", SwingConstants.CENTER);
        title.setBounds(0, 15, 760, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 200, 255));
        card.add(title);

        addLabel(card, "Appointment ID:", 50, 70);
        txtAppointmentID = addField(card, 200, 70, "e.g. A001");

        addLabel(card, "Customer ID:", 50, 115);
        txtCustomerID = addField(card, 200, 115, "e.g. C001");

        addLabel(card, "Date:", 50, 160);
        txtDate = addField(card, 200, 160, "YYYY-MM-DD");

        addLabel(card, "Service Type:", 50, 205);
        serviceBox = new JComboBox<>(new String[]{"Normal", "Major"});
        styleCombo(serviceBox, 200, 205);
        card.add(serviceBox);

        addLabel(card, "Status:", 50, 250);
        statusBox = new JComboBox<>(new String[]{"Unpaid", "Paid", "Cancel"});
        styleCombo(statusBox, 200, 250);
        card.add(statusBox);

        addLabel(card, "Task Description:", 50, 295);
        txtTask = addField(card, 200, 295, "Describe the task");

        addLabel(card, "Duration (hrs):", 50, 340);
        txtDuration = addField(card, 200, 340, "Auto-filled");
        txtDuration.setEditable(false);
        txtDuration.setForeground(new Color(0, 200, 255));

        JButton createBtn = new ModernButton("Create");
        JButton clearBtn  = new ModernButton("Clear");
        JButton backBtn   = new ModernButton("Back");

        createBtn.setBounds(510, 70, 200, 42);
        clearBtn.setBounds(510, 130, 200, 42);
        backBtn.setBounds(510, 190, 200, 42);

        card.add(createBtn);
        card.add(clearBtn);
        card.add(backBtn);

        JPanel infoBox = new JPanel();
        infoBox.setBackground(new Color(25, 25, 40));
        infoBox.setBounds(510, 250, 200, 122);
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 255, 60), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 10)
        ));

        JLabel dashTitle = new JLabel("QUICK STATS");
        dashTitle.setForeground(new Color(0, 200, 255));
        dashTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lblTotalApps = infoLabel("Total appointment: 0");
        lblLastID    = infoLabel("Current appointment ID: N/A");
        lblPending   = infoLabel("Pending jobs: 0");

        infoBox.add(dashTitle);
        infoBox.add(Box.createVerticalStrut(10));
        infoBox.add(lblTotalApps);
        infoBox.add(Box.createVerticalStrut(5));
        infoBox.add(lblLastID);
        infoBox.add(Box.createVerticalStrut(5));
        infoBox.add(lblPending);
        card.add(infoBox);

        String[] columns = {"APP ID", "CUST", "DATE", "SERVICE", "STATUS", "JOB", "TECH", "STAFF"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        setupTableStyle();

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 390, 660, 150);
        sp.getViewport().setBackground(new Color(28, 28, 45)); 
        sp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(sp);

        serviceBox.addActionListener(e -> updateDuration());
        createBtn.addActionListener(e -> createAppointment());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> goBack());

        updateDuration();
        loadTableData();
        add(card);
    }


    private void setupTableStyle() {
        table.setBackground(new Color(28, 28, 45)); 
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setGridColor(new Color(130, 130, 160));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(Color.decode("#057487"));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(28, 28, 45)); 
        centerRenderer.setForeground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        int totalCount = 0;
        int pendingCount = 0;
        String lastID = "N/A";

        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d.length >= 9) {
                String staffID = (d.length >= 10) ? d[9] : "N/A";
                tableModel.addRow(new Object[]{d[0], d[1], d[2], d[3], d[4], d[5], d[8], staffID});
                totalCount++;
                lastID = d[0];
                if (d[5].equalsIgnoreCase("Pending")) pendingCount++;
            }
        }
        lblTotalApps.setText("Total appointment: " + totalCount);
        lblLastID.setText("Current appointment ID: " + lastID);
        lblPending.setText("Pending jobs: " + pendingCount);
    }


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

        String line = id + "," + customerID + "," + date + "," + serviceBox.getSelectedItem() + ","
                    + statusBox.getSelectedItem() + ",Pending," + task + "," + txtDuration.getText() + ",NA," + this.counterID;

        FileUtil.appendFile("appointment.txt", line);
        new ModernDialog("Appointment created successfully.");
        SystemLogger.log(this.counterID, "Counter Staff", "Created new appointment: " + id);
        loadTableData();
        clearFields();
    }

    private void clearFields() {
        txtAppointmentID.setText("");
        txtCustomerID.setText("");
        txtDate.setText("");
        txtTask.setText("");
        serviceBox.setSelectedIndex(0);
        statusBox.setSelectedItem("Unpaid");
        updateDuration();
    }

    private void goBack() {
        CounterStaffMenu.instance.rightContainer.add(new Appointment(counterID), "APPOINTMENT");
        CounterStaffMenu.instance.showRightPage("APPOINTMENT");
    }

    private void updateDuration() {
        txtDuration.setText(serviceBox.getSelectedItem().equals("Normal") ? "1" : "3");
    }


    private boolean customerExists(String id) {
        for (String l : FileUtil.readFile("customer.txt")) {
            if (l.split(",")[0].equals(id)) return true;
        }
        return false;
    }

    private boolean appointmentIDExists(String id) {
        for (String l : FileUtil.readFile("appointment.txt")) {
            if (l.split(",")[0].equals(id)) return true;
        }
        return false;
    }


    private void addLabel(JPanel card, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 140, 25);
        lbl.setForeground(Color.WHITE);
        card.add(lbl);
    }

    private JTextField addField(JPanel card, int x, int y, String ph) {
        JTextField tf = new JTextField(ph);
        tf.setBounds(x, y, 230, 32);
        tf.setBackground(new Color(55, 55, 75));
        tf.setForeground(new Color(180, 180, 200));
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(ph)) {
                    tf.setText("");
                    tf.setForeground(Color.WHITE);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(ph);
                    tf.setForeground(new Color(180, 180, 200));
                }
            }
        });
        card.add(tf);
        return tf;
    }

    private void styleCombo(JComboBox<String> box, int x, int y) {
        box.setBounds(x, y, 230, 32);
        box.setBackground(new Color(55, 55, 75));
        box.setForeground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(Color.decode("#057487"), 1));
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

    private JLabel infoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(160, 160, 190));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }
}