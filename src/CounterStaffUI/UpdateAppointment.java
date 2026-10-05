package CounterStaffUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import UTILS.FileUtil;
import UI.Components.*;

public class UpdateAppointment extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtAppointmentID, txtCustomerID, txtDate, txtTask, txtDuration;
    private JTextField txtTechnicianID, txtTechnicianName;
    private JComboBox<String> serviceBox, statusBox;
    private JLabel statusIndicator;
    private JTable table;
    private DefaultTableModel tableModel;

    public UpdateAppointment(String counterID) {
        this.counterID = counterID;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(760, 560));
        card.setLayout(null);

        JLabel title = new JLabel("UPDATE APPOINTMENT", SwingConstants.CENTER);
        title.setBounds(0, 15, 760, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        JLabel searchLbl = new JLabel("Appointment ID:");
        searchLbl.setBounds(50, 60, 130, 28);
        searchLbl.setForeground(Color.WHITE);
        card.add(searchLbl);

        txtAppointmentID = new JTextField();
        txtAppointmentID.setBounds(185, 60, 180, 32);
        styleField(txtAppointmentID);
        card.add(txtAppointmentID);

        JButton loadBtn = new ModernButton("Load");
        loadBtn.setBounds(375, 60, 100, 32);
        card.add(loadBtn);

        statusIndicator = new JLabel("No appointment loaded");
        statusIndicator.setBounds(490, 60, 230, 28);
        statusIndicator.setForeground(new Color(150, 150, 170));
        statusIndicator.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        card.add(statusIndicator);

        JSeparator sep = new JSeparator();
        sep.setBounds(50, 103, 660, 2);
        sep.setForeground(new Color(80, 80, 110));
        card.add(sep);

        addLabel(card, "Customer ID:", 50, 120);
        txtCustomerID = addField(card, 185, 120);

        addLabel(card, "Date:", 50, 162);
        txtDate = addField(card, 185, 162);

        addLabel(card, "Service Type:", 50, 204);
        serviceBox = new JComboBox<>(new String[]{"Normal", "Major"});
        styleCombo(serviceBox, 185, 204);
        card.add(serviceBox);

        addLabel(card, "Status:", 50, 246);
        statusBox = new JComboBox<>(new String[]{"Unpaid", "Paid", "Cancel"});
        styleCombo(statusBox, 185, 246);
        card.add(statusBox);

        addLabel(card, "Task:", 50, 288);
        txtTask = addField(card, 185, 288);

        addLabel(card, "Duration (hrs):", 50, 330);
        txtDuration = addField(card, 185, 330);
        txtDuration.setEditable(false);
        txtDuration.setForeground(new Color(0, 200, 255));

        JLabel techSectionLbl = new JLabel("Technician (optional)");
        techSectionLbl.setBounds(400, 120, 280, 22);
        techSectionLbl.setForeground(new Color(120, 120, 150));
        techSectionLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        card.add(techSectionLbl);

        addLabel(card, "Tech ID:", 400, 150);
        txtTechnicianID = addField(card, 530, 150);

        addLabel(card, "Tech Name:", 400, 192);
        txtTechnicianName = addField(card, 530, 192);

        JButton updateBtn = new ModernButton("Save Update");
        JButton clearBtn  = new ModernButton("Clear");
        JButton backBtn   = new ModernButton("Back");

        updateBtn.setBounds(530, 246, 180, 38);
        clearBtn.setBounds(530, 296, 180, 38);
        backBtn.setBounds(530, 346, 180, 38);

        card.add(updateBtn);
        card.add(clearBtn);
        card.add(backBtn);

        String[] columns = {"APP ID", "CUST", "DATE", "SERVICE", "STATUS", "JOB", "TECH"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        setupTableStyle();

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 410, 660, 120);
        sp.getViewport().setBackground(new Color(28, 28, 45)); 
        sp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(sp);

        serviceBox.addActionListener(e -> updateDuration());
        loadBtn.addActionListener(e -> loadAppointment());
        updateBtn.addActionListener(e -> updateAppointment());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> goBack());

        updateDuration();
        loadTableData();
        add(card);
    }


    private void loadAppointment() {
        String id = txtAppointmentID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Appointment ID."); return; }

        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d[0].equals(id)) {
                txtCustomerID.setText(d[1]);
                txtDate.setText(d[2]);
                serviceBox.setSelectedItem(d[3]);
                statusBox.setSelectedItem(d[4]);
                txtTask.setText(d[6]);
                txtDuration.setText(d[7]);
                txtTechnicianID.setText(d[8].equals("NA") ? "" : d[8]);
                statusIndicator.setText("Loaded: " + id + " [" + d[5] + "]");
                statusIndicator.setForeground(new Color(100, 220, 100));
                return;
            }
        }
        statusIndicator.setText("Not found: " + id);
        statusIndicator.setForeground(new Color(255, 100, 100));
        new ModernDialog("Appointment not found.");
    }

    private void updateAppointment() {
        String id = txtAppointmentID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Appointment ID."); return; }

        List<String> lines = FileUtil.readFile("appointment.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (data[0].equals(id)) {
                if (data[5].equalsIgnoreCase("Done")) { new ModernDialog("Done task cannot be updated."); return; }

                String newTechID = txtTechnicianID.getText().trim();
                String newTechName = txtTechnicianName.getText().trim();
                if (!newTechID.isEmpty() && !newTechName.isEmpty()) {
                    if (!statusBox.getSelectedItem().toString().equalsIgnoreCase("Paid")) {
                        new ModernDialog("Only PAID appointments can assign technician."); return;
                    }
                    if (!isTechnicianValid(newTechID, newTechName)) return;
                    data[8] = newTechID;
                }

                String updatedLine = id + "," + txtCustomerID.getText() + "," + txtDate.getText() + ","
                                   + serviceBox.getSelectedItem() + "," + statusBox.getSelectedItem() + ","
                                   + data[5] + "," + txtTask.getText() + "," + txtDuration.getText() + "," + data[8];

                lines.set(i, updatedLine);
                FileUtil.writeFile("appointment.txt", lines);
                new ModernDialog("Appointment updated successfully.");
                loadTableData();
                return;
            }
        }
        new ModernDialog("Appointment not found.");
    }

    private void clearFields() {
        txtAppointmentID.setText(""); txtCustomerID.setText(""); txtDate.setText("");
        txtTask.setText(""); txtTechnicianID.setText(""); txtTechnicianName.setText("");
        serviceBox.setSelectedIndex(0); statusBox.setSelectedItem("Unpaid");
        statusIndicator.setText("No appointment loaded");
        statusIndicator.setForeground(new Color(150, 150, 170));
        updateDuration();
    }

    private void goBack() {
        CounterStaffMenu.instance.rightContainer.add(new Appointment(counterID), "APPOINTMENT");
        CounterStaffMenu.instance.showRightPage("APPOINTMENT");
    }

    private void updateDuration() {
        txtDuration.setText(serviceBox.getSelectedItem().equals("Normal") ? "1" : "3");
    }


    private boolean isTechnicianValid(String techID, String techName) {
        for (String line : FileUtil.readFile("technician.txt")) {
            String[] d = line.split(",");
            if (d[0].equals(techID) && d[1].equalsIgnoreCase(techName)) {
                if (countPendingJobs(techID) >= 3) { new ModernDialog("Technician already has 3 pending tasks."); return false; }
                return true;
            }
        }
        new ModernDialog("Technician ID and Name do not match.");
        return false;
    }

    private int countPendingJobs(String techID) {
        int count = 0;
        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d[8].equals(techID) && (d[5].equalsIgnoreCase("Pending") || d[5].equalsIgnoreCase("In Progress"))) count++;
        }
        return count;
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
        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d.length >= 9) tableModel.addRow(new Object[]{d[0], d[1], d[2], d[3], d[4], d[5], d[8]});
        }
    }

    private void addLabel(JPanel card, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 130, 25);
        lbl.setForeground(Color.WHITE);
        card.add(lbl);
    }

    private JTextField addField(JPanel card, int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 180, 30);
        styleField(tf);
        card.add(tf);
        return tf;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(55, 55, 75));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private void styleCombo(JComboBox<String> box, int x, int y) {
        box.setBounds(x, y, 180, 32);
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
}