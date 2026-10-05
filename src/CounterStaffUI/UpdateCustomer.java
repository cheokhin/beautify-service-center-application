package CounterStaffUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import UTILS.FileUtil;
import UI.Components.*;

public class UpdateCustomer extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtID, txtName, txtUsername, txtPassword, txtEmail, txtPhone;
    private JTextField txtVehicleModel, txtRegNo, txtYear;
    private JComboBox<String> typeBox;
    private JTable table;
    private DefaultTableModel tableModel;

    public UpdateCustomer(String counterID) {
        this.counterID = counterID;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(960, 560));
        card.setLayout(null);

        JLabel title = new JLabel("UPDATE CUSTOMER", SwingConstants.CENTER);
        title.setBounds(0, 15, 960, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        int startX = 30, fieldX = 130;

        addLabel(card, "Customer ID:", startX, 70);
        txtID = addField(card, fieldX, 70, 85);

        JButton loadBtn = new ModernButton("Load");
        loadBtn.setBounds(220, 70, 80, 30);
        card.add(loadBtn);

        addLabel(card, "Name:", startX, 110);          txtName = addField(card, fieldX, 110, 170);
        addLabel(card, "Username:", startX, 150);      txtUsername = addField(card, fieldX, 150, 170);
        addLabel(card, "Password:", startX, 190);      txtPassword = addField(card, fieldX, 190, 170);
        addLabel(card, "Email:", startX, 230);         txtEmail = addField(card, fieldX, 230, 170);

        addLabel(card, "Phone:", startX, 270);
        txtPhone = addField(card, fieldX, 270, 170);
        txtPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                txtPhone.setText(formatPhone(txtPhone.getText()));
            }
        });

        addLabel(card, "Vehicle Model:", startX, 310); txtVehicleModel = addField(card, fieldX, 310, 170);
        addLabel(card, "Reg. No:", startX, 350);       txtRegNo = addField(card, fieldX, 350, 170);
        addLabel(card, "Vehicle Year:", startX, 390);  txtYear = addField(card, fieldX, 390, 170);
        addLabel(card, "Type:", startX, 430);

        typeBox = new JComboBox<>(new String[]{"Walk-in", "Booking"});
        styleCombo(typeBox, fieldX, 430, 170);
        card.add(typeBox);

        JButton actionBtn = new ModernButton("Update");
        JButton clearBtn  = new ModernButton("Clear");
        JButton backBtn   = new ModernButton("Back");

        actionBtn.setBounds(30, 480, 85, 35);
        clearBtn.setBounds(125, 480, 80, 35);
        backBtn.setBounds(215, 480, 85, 35);

        card.add(actionBtn);
        card.add(clearBtn);
        card.add(backBtn);

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setBounds(320, 70, 2, 445);
        sep.setForeground(new Color(80, 80, 110));
        card.add(sep);

        String[] columns = {"ID", "NAME", "PHONE", "VEHICLE", "REG", "TYPE"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        setupTableStyle();

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(340, 70, 590, 445);
        sp.getViewport().setBackground(new Color(28, 28, 45)); 
        sp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#057487"), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(sp);

        actionBtn.addActionListener(e -> updateCustomer());
        loadBtn.addActionListener(e -> loadCustomerByID());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> goBack());

        loadTableData();
        add(card);
    }


    private void loadTableData() {
        tableModel.setRowCount(0);
        for (String line : FileUtil.readFile("customer.txt")) {
            String[] d = line.split(",");
            if (d.length >= 10) {
                tableModel.addRow(new Object[]{d[0], d[1], formatPhone(d[5]), d[6], d[7], d[9]});
            }
        }
    }

    private void loadCustomerByID() {
        String id = txtID.getText().trim();
        if (id.isEmpty()) {
            new ModernDialog("Enter Customer ID first.");
            return;
        }
        for (String line : FileUtil.readFile("customer.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(id)) {
                fillFields(data);
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
                SystemLogger.log(this.counterID, "Counter Staff", "Updated customer profile: " + txtID.getText().trim());
                loadTableData();
                return;
            }
        }
        new ModernDialog("Customer ID not found.");
    }


    private void fillFields(String[] data) {
        txtID.setText(data[0]);
        txtName.setText(data[1]);
        txtUsername.setText(data[2]);
        txtPassword.setText(data[3]);
        txtEmail.setText(data[4]);
        txtPhone.setText(formatPhone(data[5]));
        txtVehicleModel.setText(data[6]);
        txtRegNo.setText(data[7]);
        txtYear.setText(data[8]);
        typeBox.setSelectedItem(data[9]);
    }

    private String buildCustomerLine(String id) {
        return id + "," +
               txtName.getText().trim() + "," +
               txtUsername.getText().trim() + "," +
               txtPassword.getText().trim() + "," +
               txtEmail.getText().trim() + "," +
               txtPhone.getText().trim() + "," +
               txtVehicleModel.getText().trim() + "," +
               txtRegNo.getText().trim() + "," +
               txtYear.getText().trim() + "," +
               typeBox.getSelectedItem().toString();
    }

    private boolean validateCustomerFields() {
        String id = txtID.getText().trim();
        if (!id.matches("C\\d{3}")) {
            new ModernDialog("Customer ID must be like C001.");
            return false;
        }

        if (txtName.getText().trim().isEmpty() ||
            txtUsername.getText().trim().isEmpty() ||
            txtPassword.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtPhone.getText().trim().isEmpty() ||
            txtVehicleModel.getText().trim().isEmpty() ||
            txtRegNo.getText().trim().isEmpty() ||
            txtYear.getText().trim().isEmpty()) {
            new ModernDialog("Please fill in all fields.");
            return false;
        }

        if (!txtEmail.getText().trim().contains("@")) {
            new ModernDialog("Invalid email format.");
            return false;
        }

        String phoneCheck = txtPhone.getText().replaceAll("\\D", "");
        if (phoneCheck.length() < 10 || phoneCheck.length() > 11) {
            new ModernDialog("Phone must be 10 or 11 digits.");
            return false;
        }

        if (!txtYear.getText().trim().matches("\\d{4}")) {
            new ModernDialog("Vehicle year must be 4 digits.");
            return false;
        }
        return true;
    }

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

    private void goBack() {
        CounterStaffMenu.instance.rightContainer.add(new ManagementMenu(counterID), "MANAGEMENT");
        CounterStaffMenu.instance.showRightPage("MANAGEMENT");
    }

    private String formatPhone(String p) {
        String clean = p.replaceAll("\\D", "");
        if (clean.length() == 10) return clean.substring(0,3) + "-" + clean.substring(3,6) + " " + clean.substring(6);
        if (clean.length() == 11) return clean.substring(0,3) + "-" + clean.substring(3,7) + " " + clean.substring(7);
        return p;
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

    private void addLabel(JPanel card, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 95, 30);
        label.setForeground(Color.WHITE);
        card.add(label);
    }

    private JTextField addField(JPanel card, int x, int y, int w) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, 30);
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

    private void styleCombo(JComboBox<String> box, int x, int y, int w) {
        box.setBounds(x, y, w, 30);
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