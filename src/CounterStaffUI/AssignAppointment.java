package CounterStaffUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import UTILS.FileUtil;
import UI.Components.*;

public class AssignAppointment extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtAppointmentID;
    private JTextField txtTechnicianID;
    private JTextField txtTechnicianName;
    private JLabel statusDot;

    private JTable appTable;
    private JTable techTable;
    private DefaultTableModel appTableModel;
    private DefaultTableModel techTableModel;

    public AssignAppointment(String counterID) {
        this.counterID = counterID;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(760, 560));
        card.setLayout(null);

        JLabel title = new JLabel("ASSIGN TECHNICIAN", SwingConstants.CENTER);
        title.setBounds(0, 15, 760, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        sectionLabel(card, "STEP 1 — Find Appointment", 50, 60);

        addLabel(card, "Appointment ID:", 50, 85);
        txtAppointmentID = addField(card, 185, 85, 160);

        JButton loadBtn = new ModernButton("Search");
        loadBtn.setBounds(355, 84, 120, 33);
        card.add(loadBtn);

        statusDot = new JLabel("●  not loaded");
        statusDot.setBounds(490, 85, 220, 30);
        statusDot.setForeground(new Color(180, 60, 60));
        statusDot.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(statusDot);

        String[] appCols = {"APP ID", "CUS ID", "DATE", "SERVICE", "PAYMENT", "JOB STATUS", "TECH"};
        appTableModel = new DefaultTableModel(appCols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        appTable = new JTable(appTableModel);
        setupTableStyle(appTable);

        JScrollPane appSP = new JScrollPane(appTable);
        appSP.setBounds(50, 125, 660, 110);
        appSP.getViewport().setBackground(new Color(28, 28, 45)); 
        appSP.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(appSP);

        sectionLabel(card, "STEP 2 — Assign Technician", 50, 245);

        addLabel(card, "Technician ID:", 50, 275);
        txtTechnicianID = addField(card, 185, 275, 200);

        addLabel(card, "Technician Name:", 50, 320);
        txtTechnicianName = addField(card, 185, 320, 200);

        JButton clearBtn = new ModernButton("Clear");
        clearBtn.setBounds(400, 275, 170, 35);
        card.add(clearBtn);

        JButton assignBtn = new ModernButton("Assign");
        assignBtn.setBounds(400, 320, 170, 35);
        card.add(assignBtn);

        JButton backBtn = new ModernButton("Back");
        backBtn.setBounds(585, 320, 120, 35);
        card.add(backBtn);

        String[] techCols = {"TECH ID", "TECHNICIAN NAME", "SKILL", "WORKLOAD", "ACTIVE APPS"};
        techTableModel = new DefaultTableModel(techCols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        techTable = new JTable(techTableModel);
        setupTableStyle(techTable);

        techTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        techTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        techTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        techTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        techTable.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane techSP = new JScrollPane(techTable);
        techSP.setBounds(50, 370, 660, 160);
        techSP.getViewport().setBackground(new Color(28, 28, 45)); 
        techSP.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(techSP);

        loadBtn.addActionListener(e -> loadAppointment());
        assignBtn.addActionListener(e -> assignTechnician());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> goBack());

        loadAppTableData();
        loadTechTableData();

        add(card);
    }

    private void setupTableStyle(JTable table) {
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

    private void loadAppTableData() {
        appTableModel.setRowCount(0);
        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d.length >= 9) appTableModel.addRow(new Object[]{d[0], d[1], d[2], d[3], d[4], d[5], d[8]});
        }
    }

    private void loadTechTableData() {
        techTableModel.setRowCount(0);
        for (String line : FileUtil.readFile("technician.txt")) {
            String[] d = line.split(",");
            if (d.length >= 2) {
                String skill = (d.length > 6) ? d[6] : "General";
                int pendingCount = countPendingJobs(d[0]);
                String workload = pendingCount + " / 3";
                String activeApps = getActiveAppointmentIDs(d[0]);
                techTableModel.addRow(new Object[]{d[0], d[1], skill, workload, activeApps});
            }
        }
    }

    private String getActiveAppointmentIDs(String techID) {
        StringBuilder apps = new StringBuilder();
        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d.length >= 9 && d[8].equals(techID)) {
                if (d[5].equalsIgnoreCase("Pending") || d[5].equalsIgnoreCase("In Progress")) {
                    if (apps.length() > 0) apps.append(", ");
                    apps.append(d[0]);
                }
            }
        }
        return apps.length() > 0 ? apps.toString() : "None";
    }

    private int countPendingJobs(String techID) {
        int count = 0;
        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d.length >= 9 && d[8].equals(techID) &&
                (d[5].equalsIgnoreCase("Pending") || d[5].equalsIgnoreCase("In Progress"))) count++;
        }
        return count;
    }

    private void loadAppointment() {
        String id = txtAppointmentID.getText().trim();
        if (id.isEmpty()) { new ModernDialog("Enter Appointment ID."); return; }

        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] d = line.split(",");
            if (d[0].equals(id)) {
                boolean isPaid = d[4].equalsIgnoreCase("Paid");
                statusDot.setText(isPaid ? "●  Ready to assign" : "●  Not PAID — cannot assign");
                statusDot.setForeground(isPaid ? new Color(80, 220, 100) : new Color(220, 80, 80));

                for (int i = 0; i < appTable.getRowCount(); i++) {
                    if (appTable.getValueAt(i, 0).equals(id)) {
                        appTable.setRowSelectionInterval(i, i);
                        appTable.scrollRectToVisible(appTable.getCellRect(i, 0, true));
                        break;
                    }
                }
                return;
            }
        }
        appTable.clearSelection();
        statusDot.setText("●  not found");
        statusDot.setForeground(new Color(220, 80, 80));
        new ModernDialog("Appointment not found.");
    }

    private void assignTechnician() {
        String appID = txtAppointmentID.getText().trim();
        String techID = txtTechnicianID.getText().trim();
        String techName = txtTechnicianName.getText().trim();

        if (appID.isEmpty() || techID.isEmpty() || techName.isEmpty()) {
            new ModernDialog("Fill in Appointment ID, Tech ID, and Tech Name."); return;
        }

        List<String> appointments = FileUtil.readFile("appointment.txt");
        for (int i = 0; i < appointments.size(); i++) {
            String[] data = appointments.get(i).split(",");
            if (data[0].equals(appID)) {
                if (!data[4].equalsIgnoreCase("Paid")) { new ModernDialog("Only PAID appointments can be assigned."); return; }
                if (!isTechnicianValid(techID, techName)) return;

                data[8] = techID;
                appointments.set(i, String.join(",", data));
                FileUtil.writeFile("appointment.txt", appointments);
                new ModernDialog("Technician assigned successfully.");
                SystemLogger.log(this.counterID, "Counter Staff", "Assigned Technician " + techID + " to Appointment " + appID);
                loadAppTableData();
                loadTechTableData();
                return;
            }
        }
        new ModernDialog("Appointment not found.");
    }

    private void clearFields() {
        txtAppointmentID.setText(""); txtTechnicianID.setText(""); txtTechnicianName.setText("");
        statusDot.setText("●  not loaded"); statusDot.setForeground(new Color(180, 60, 60));
        appTable.clearSelection(); techTable.clearSelection();
    }

    private void goBack() {
        CounterStaffMenu.instance.rightContainer.add(new Appointment(counterID), "APPOINTMENT");
        CounterStaffMenu.instance.showRightPage("APPOINTMENT");
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

    private void sectionLabel(JPanel card, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 400, 22);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 220, 140));
        card.add(lbl);
    }

    private void addLabel(JPanel card, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 130, 25);
        lbl.setForeground(Color.WHITE);
        card.add(lbl);
    }

    private JTextField addField(JPanel card, int x, int y, int width) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, width, 32);
        tf.setBackground(new Color(55, 55, 75));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        card.add(tf);
        return tf;
    }
}