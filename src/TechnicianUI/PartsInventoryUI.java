package TechnicianUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import UI.Components.*;

public class PartsInventoryUI extends JPanel {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel resModel, invModel;
    private JTable resTable, invTable;
    private JTextField resField1, resField3, resField4;
    private JLabel welcomeLabel;
    private String currentTechID;
    private String techName = "Technician";

    public PartsInventoryUI() {
        this("Unknown");
    }

    public PartsInventoryUI(String techID) {
        this.currentTechID = techID;
        fetchTechName();

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(850, 650));
        card.setLayout(null);

        welcomeLabel = new JLabel("Logged in as: " + techName + " (" + currentTechID + ")");
        welcomeLabel.setBounds(40, 5, 400, 20);
        welcomeLabel.setForeground(Color.GRAY);
        welcomeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        card.add(welcomeLabel);

        resModel = new DefaultTableModel(new String[]{"Appointment ID", "Customer ID", "Reserved Part", "Quantity"}, 0);
        invModel = new DefaultTableModel(new String[]{"Part Name", "Total Reserved"}, 0);

        card.add(createTableTitle("PARTS REQUISITION LIST", 25));

        resTable = new JTable(resModel);
        resTable.setRowHeight(25);
        resTable.setBackground(new Color(28, 28, 45));
        resTable.setForeground(Color.WHITE);
        resTable.setGridColor(new Color(50, 50, 70));
        resTable.getTableHeader().setBackground(new Color(255, 180, 0));
        resTable.getTableHeader().setForeground(Color.WHITE);
        resTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        resTable.setSelectionBackground(new Color(255, 180, 0));
        resTable.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer resCenter = new DefaultTableCellRenderer();
        resCenter.setHorizontalAlignment(JLabel.CENTER);
        resCenter.setBackground(new Color(28, 28, 45));
        resCenter.setForeground(Color.WHITE);
        for (int i = 0; i < resTable.getColumnCount(); i++)
            resTable.getColumnModel().getColumn(i).setCellRenderer(resCenter);

        JScrollPane resSP = new JScrollPane(resTable);
        resSP.setBounds(40, 65, 770, 160);
        resSP.getViewport().setBackground(new Color(28, 28, 45));
        resSP.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 180, 0), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(resSP);

        card.add(createTableTitle("BILL OF MATERIALS (BOM)", 245));

        invTable = new JTable(invModel);
        invTable.setRowHeight(25);
        invTable.setBackground(new Color(28, 28, 45));
        invTable.setForeground(Color.WHITE);
        invTable.setGridColor(new Color(50, 50, 70));
        invTable.getTableHeader().setBackground(new Color(255, 180, 0));
        invTable.getTableHeader().setForeground(Color.WHITE);
        invTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        invTable.setSelectionBackground(new Color(255, 180, 0));
        invTable.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer invCenter = new DefaultTableCellRenderer();
        invCenter.setHorizontalAlignment(JLabel.CENTER);
        invCenter.setBackground(new Color(28, 28, 45));
        invCenter.setForeground(Color.WHITE);
        for (int i = 0; i < invTable.getColumnCount(); i++)
            invTable.getColumnModel().getColumn(i).setCellRenderer(invCenter);

        JScrollPane invSP = new JScrollPane(invTable);
        invSP.setBounds(40, 285, 770, 160);
        invSP.getViewport().setBackground(new Color(28, 28, 45));
        invSP.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 180, 0), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(invSP);

        resField1 = createInputField(40, 485, 200);
        resField3 = createInputField(320, 485, 250);
        resField4 = createInputField(630, 485, 180);

        card.add(createHintLabel("Appt ID (e.g. A001)", 40, 465));
        card.add(createHintLabel("Part Name (Letters only)", 320, 465));
        card.add(createHintLabel("Qty (Numbers only)", 630, 465));

        JButton btnUpdateAll = new ModernButton("Update Reservations & Inventory");
        btnUpdateAll.setBounds(275, 555, 300, 45);

        btnUpdateAll.addActionListener(e -> {
            String apptID = resField1.getText().trim();
            String partName = resField3.getText().trim();
            String qty = resField4.getText().trim();

            if (apptID.isEmpty() || partName.isEmpty() || qty.isEmpty()) {
                new ModernDialog("Please fill in all fields!");
                return;
            }

            if (!partName.matches("[a-zA-Z ]+")) {
                new ModernDialog("Part Name must contain letters only!");
                return;
            }

            if (!qty.matches("\\d+") || Integer.parseInt(qty) <= 0) {
                new ModernDialog("Quantity must be a positive number!");
                return;
            }

            if (!isAppointmentReady(apptID)) {
                new ModernDialog("Appointment ID invalid or not marked as Done!");
                return;
            }

            int newQty = Integer.parseInt(qty);
            String custID = apptID.replace("A", "C");

            boolean foundInRes = false;
            for (int i = 0; i < resModel.getRowCount(); i++) {
                if (resModel.getValueAt(i, 0).equals(apptID) &&
                    resModel.getValueAt(i, 2).equals(partName)) {
                    int existing = Integer.parseInt(resModel.getValueAt(i, 3).toString());
                    resModel.setValueAt(existing + newQty, i, 3);
                    foundInRes = true;
                    break;
                }
            }
            if (!foundInRes) {
                resModel.addRow(new Object[]{apptID, custID, partName, newQty});
            }

            boolean foundInInv = false;
            for (int i = 0; i < invModel.getRowCount(); i++) {
                if (invModel.getValueAt(i, 0).equals(partName)) {
                    int existing = Integer.parseInt(invModel.getValueAt(i, 1).toString());
                    invModel.setValueAt(existing + newQty, i, 1);
                    foundInInv = true;
                    break;
                }
            }
            if (!foundInInv) {
                invModel.addRow(new Object[]{partName, newQty});
            }

            saveToInventoryFile();
            welcomeLabel.setText("Logged in as: " + techName + " (" + currentTechID + ")");
            resField1.setText(""); resField3.setText(""); resField4.setText("");
            new ModernDialog("Inventory Updated Successfully!");
            SystemLogger.log(this.currentTechID, "Technician", "Added " + newQty + "x '" + partName + "' to Appt: " + apptID);
            
        });

        JButton btnDelete = new ModernButton("Delete Selected");
        btnDelete.setBounds(50, 555, 180, 45);

        btnDelete.addActionListener(e -> {
            int resRow = resTable.getSelectedRow();
            if (resRow == -1) {
                new ModernDialog("Please select a row from the Reserved Parts table to delete!");
                return;
            }

            String apptID = resModel.getValueAt(resRow, 0).toString();
            String partName = resModel.getValueAt(resRow, 2).toString();
            int qty = Integer.parseInt(resModel.getValueAt(resRow, 3).toString());

            resModel.removeRow(resRow);

            for (int i = 0; i < invModel.getRowCount(); i++) {
                if (invModel.getValueAt(i, 0).equals(partName)) {
                    int existing = Integer.parseInt(invModel.getValueAt(i, 1).toString());
                    int newQty = existing - qty;
                    if (newQty <= 0) {
                        invModel.removeRow(i);
                    } else {
                        invModel.setValueAt(newQty, i, 1);
                    }
                    break;
                }
            }

            saveToInventoryFile();
            new ModernDialog("Record Deleted Successfully!");
            SystemLogger.log(this.currentTechID, "Technician", "Deleted " + qty + "x '" + partName + "' from Appt: " + apptID);
        });

        card.add(resField1); card.add(resField3); card.add(resField4);
        card.add(btnDelete);
        card.add(btnUpdateAll);

        loadDataFromInventory();
        add(card);
    }

    private boolean isAppointmentReady(String aid) {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[0].trim().equals(aid)) {
                    return "Done".equalsIgnoreCase(data[5].trim());
                }
            }
        } catch (IOException e) { }
        return false;
    }

    private void saveToInventoryFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("inventory.txt", false))) {
            for (int i = 0; i < resModel.getRowCount(); i++) {
                String apptID = resModel.getValueAt(i, 0).toString();
                String partName = resModel.getValueAt(i, 2).toString();
                String qty = resModel.getValueAt(i, 3).toString();
                bw.write(apptID + "," + partName + "," + qty);
                bw.newLine();
            }
        } catch (IOException e) {
            new ModernDialog("File Error!");
        }
    }

    private void loadDataFromInventory() {
        File file = new File("inventory.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String apptID = data[0].trim();
                    String partName = data[1].trim();
                    int qty;
                    try { qty = Integer.parseInt(data[2].trim()); }
                    catch (NumberFormatException e) { continue; }
                    String custID = apptID.replace("A", "C");

                    boolean foundRes = false;
                    for (int i = 0; i < resModel.getRowCount(); i++) {
                        if (resModel.getValueAt(i, 0).equals(apptID) &&
                            resModel.getValueAt(i, 2).equals(partName)) {
                            int ex = Integer.parseInt(resModel.getValueAt(i, 3).toString());
                            resModel.setValueAt(ex + qty, i, 3);
                            foundRes = true;
                            break;
                        }
                    }
                    if (!foundRes) resModel.addRow(new Object[]{apptID, custID, partName, qty});

                    boolean foundInv = false;
                    for (int i = 0; i < invModel.getRowCount(); i++) {
                        if (invModel.getValueAt(i, 0).equals(partName)) {
                            int ex = Integer.parseInt(invModel.getValueAt(i, 1).toString());
                            invModel.setValueAt(ex + qty, i, 1);
                            foundInv = true;
                            break;
                        }
                    }
                    if (!foundInv) invModel.addRow(new Object[]{partName, qty});
                }
            }
        } catch (IOException e) { }
    }

    private void fetchTechName() {
        try (BufferedReader br = new BufferedReader(new FileReader("technician.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(currentTechID)) { this.techName = data[1]; break; }
            }
        } catch (Exception e) { }
    }

    private JLabel createTableTitle(String text, int y) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setBounds(0, y, 850, 30);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return l;
    }

    private JTextField createInputField(int x, int y, int w) {
        JTextField f = new JTextField();
        f.setBounds(x, y, w, 35);
        f.setHorizontalAlignment(JTextField.CENTER);
        f.setBackground(new Color(28, 28, 45));
        f.setForeground(new Color(200, 216, 240));
        f.setCaretColor(new Color(200, 216, 240));
        f.setBorder(BorderFactory.createLineBorder(new Color(255, 180, 0)));
        return f;
    }

    private JLabel createHintLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 220, 20);
        l.setForeground(Color.LIGHT_GRAY);
        l.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        return l;
    }
}