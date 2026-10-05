package CounterStaffUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import UTILS.FileUtil;
import UI.Components.*;

public class ReadCustomer extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtID; 
    private JLabel valName, valUsername, valPassword, valEmail, valPhone;
    private JLabel valVehicleModel, valRegNo, valYear, valType;
    private JTable table;
    private DefaultTableModel tableModel;

    public ReadCustomer(String counterID) {
        this.counterID = counterID;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(960, 560));
        card.setLayout(null);

        JLabel title = new JLabel("VIEW CUSTOMER DETAILS", SwingConstants.CENTER);
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

        addLabel(card, "Name:", startX, 110);           valName = addValueLabel(card, fieldX, 110);
        addLabel(card, "Username:", startX, 150);       valUsername = addValueLabel(card, fieldX, 150);
        addLabel(card, "Password:", startX, 190);       valPassword = addValueLabel(card, fieldX, 190);
        addLabel(card, "Email:", startX, 230);          valEmail = addValueLabel(card, fieldX, 230);
        addLabel(card, "Phone:", startX, 270);          valPhone = addValueLabel(card, fieldX, 270);
        addLabel(card, "Vehicle Model:", startX, 310);  valVehicleModel = addValueLabel(card, fieldX, 310);
        addLabel(card, "Reg. No:", startX, 350);        valRegNo = addValueLabel(card, fieldX, 350);
        addLabel(card, "Vehicle Year:", startX, 390);   valYear = addValueLabel(card, fieldX, 390);
        addLabel(card, "Type:", startX, 430);           valType = addValueLabel(card, fieldX, 430);

        JButton clearBtn = new ModernButton("Clear");
        JButton backBtn  = new ModernButton("Back");

        clearBtn.setBounds(65, 480, 80, 35);
        backBtn.setBounds(165, 480, 80, 35);
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
                tableModel.addRow(new Object[]{
                    d[0], d[1], formatPhone(d[5]), d[6], d[7], d[9]
                });
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
                new ModernDialog("Customer data loaded."); 
                return; 
            }
        }
        new ModernDialog("Customer not found.");
    }

    private void fillFields(String[] data) { 
        txtID.setText(data[0]); 
        valName.setText(data[1]); 
        valUsername.setText(data[2]); 
        valPassword.setText(data[3]); 
        valEmail.setText(data[4]); 
        valPhone.setText(formatPhone(data[5])); 
        valVehicleModel.setText(data[6]); 
        valRegNo.setText(data[7]); 
        valYear.setText(data[8]); 
        valType.setText(data[9]); 
    }

    private void clearFields() { 
        txtID.setText(""); 
        valName.setText("-"); 
        valUsername.setText("-"); 
        valPassword.setText("-"); 
        valEmail.setText("-"); 
        valPhone.setText("-"); 
        valVehicleModel.setText("-"); 
        valRegNo.setText("-"); 
        valYear.setText("-"); 
        valType.setText("-"); 
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

    private String formatPhone(String p) {
        String clean = p.replaceAll("\\D", ""); 
        if (clean.length() == 10) return clean.substring(0,3) + "-" + clean.substring(3,6) + " " + clean.substring(6);
        if (clean.length() == 11) return clean.substring(0,3) + "-" + clean.substring(3,7) + " " + clean.substring(7);
        return p; 
    }

    private void goBack() { 
        CounterStaffMenu.instance.rightContainer.add(new ManagementMenu(counterID), "MANAGEMENT"); 
        CounterStaffMenu.instance.showRightPage("MANAGEMENT"); 
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

    private JLabel addValueLabel(JPanel card, int x, int y) { 
        JLabel lbl = new JLabel("-"); 
        lbl.setBounds(x, y, 170, 30); 
        lbl.setForeground(new Color(0, 200, 255)); 
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        card.add(lbl); 
        return lbl; 
    }
}