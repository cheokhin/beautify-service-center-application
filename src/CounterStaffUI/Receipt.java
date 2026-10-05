package CounterStaffUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.print.PrinterException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import UTILS.FileUtil;
import UI.Components.*; 

public class Receipt extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtReceiptID, txtPaymentID;
    private JLabel lblTotalReceipts, lblLastID, lblTotalRev;
    private JTable table;
    private DefaultTableModel tableModel;

    public Receipt(String counterID) {
        this.counterID = counterID;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(28, 28, 45)); 
        card.setPreferredSize(new Dimension(760, 560));
        card.setLayout(null);

        JLabel title = new JLabel("GENERATE RECEIPT", SwingConstants.CENTER);
        title.setBounds(0, 15, 760, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE); 
        card.add(title);

        addLabel(card, "Receipt ID:", 50, 70); 
        txtReceiptID = addField(card, 160, 70);

        addLabel(card, "Payment ID:", 50, 120); 
        txtPaymentID = addField(card, 160, 120);

        JButton generateBtn = new ModernButton("Generate");
        JButton exportBtn   = new ModernButton("Export (A4)");
        JButton clearBtn    = new ModernButton("Clear");
        JButton backBtn     = new ModernButton("Back");

        generateBtn.setBounds(50, 180, 155, 35);
        exportBtn.setBounds(215, 180, 155, 35);
        clearBtn.setBounds(50, 230, 155, 35);
        backBtn.setBounds(215, 230, 155, 35);
        
        card.add(generateBtn); 
        card.add(exportBtn); 
        card.add(clearBtn); 
        card.add(backBtn);

        JPanel infoBox = new JPanel();
        infoBox.setBackground(new Color(25, 25, 40));
        infoBox.setBounds(450, 70, 260, 120); 
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 255, 60), 1), 
            BorderFactory.createEmptyBorder(15, 15, 15, 10)
        ));

        JLabel dashTitle = new JLabel("RECEIPT STATS");
        dashTitle.setForeground(new Color(0, 200, 255));
        dashTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblTotalReceipts = infoLabel("Total Receipts: 0");
        lblLastID        = infoLabel("Last Receipt ID: N/A");
        lblTotalRev      = infoLabel("Total Revenue: RM 0.00");

        infoBox.add(dashTitle); 
        infoBox.add(Box.createVerticalStrut(10)); 
        infoBox.add(lblTotalReceipts); 
        infoBox.add(Box.createVerticalStrut(5)); 
        infoBox.add(lblLastID); 
        infoBox.add(Box.createVerticalStrut(5)); 
        infoBox.add(lblTotalRev);
        card.add(infoBox);

        String[] columns = {"RECEIPT ID", "PAYMENT ID", "APP ID", "CUST ID", "AMOUNT", "METHOD", "DATE"};
        tableModel = new DefaultTableModel(columns, 0) { 
            @Override
            public boolean isCellEditable(int row, int column) { return false; } 
        };
        
        table = new JTable(tableModel);
        setupTableStyle();
        
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 300, 660, 220); 
        sp.getViewport().setBackground(new Color(28, 28, 45));                       
        sp.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createLineBorder(Color.decode("#057487"), 2), 
        	    BorderFactory.createLineBorder(Color.WHITE, 1)));
        card.add(sp);

        generateBtn.addActionListener(e -> generateReceipt());
        exportBtn.addActionListener(e -> exportReceiptToPDF());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> { 
            CounterStaffMenu.instance.rightContainer.add(new PaymentMenu(counterID), "PAYMENT"); 
            CounterStaffMenu.instance.showRightPage("PAYMENT"); 
        });

        loadTableData(); 
        add(card);
    }


    private void loadTableData() {
        tableModel.setRowCount(0);
        int totalReceipts = 0;
        double totalRevenue = 0.0;
        String lastID = "N/A";
        
        for (String line : FileUtil.readFile("receipt.txt")) {
            String[] d = line.split(",");
            if (d.length >= 7) {
                tableModel.addRow(new Object[]{d[0], d[1], d[2], d[3], "RM " + d[4], d[5], d[6]});
                totalReceipts++; 
                lastID = d[0];
                try { 
                    totalRevenue += Double.parseDouble(d[4]); 
                } catch (Exception ignored) {}
            }
        }
        lblTotalReceipts.setText("Total Receipts: " + totalReceipts);
        lblLastID.setText("Last Receipt ID: " + lastID);
        lblTotalRev.setText(String.format("Total Revenue: RM %.2f", totalRevenue));
    }

    private void generateReceipt() {
        String receiptID = txtReceiptID.getText().trim();
        String paymentID = txtPaymentID.getText().trim();

        if (receiptID.isEmpty() || paymentID.isEmpty()) { 
            new ModernDialog("Please enter Receipt ID and Payment ID."); 
            return; 
        }
        if (!receiptID.matches("R\\d{3}")) { 
            new ModernDialog("Receipt ID must be like R001."); 
            return; 
        }
        if (receiptIDExists(receiptID)) { 
            new ModernDialog("Receipt ID already exists."); 
            return; 
        }

        for (String line : FileUtil.readFile("payment.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(paymentID)) {
                String[] appDetails = getAppointmentDetails(data[1]);
                String receiptLine = receiptID + "," + paymentID + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5];
                FileUtil.appendFile("receipt.txt", receiptLine);
                
                showPOSPreview(buildReceiptDisplay(receiptID, paymentID, data[1], data[2], data[3], data[4], appDetails[0], appDetails[1], appDetails[2]));
                loadTableData(); 
                return;
            }
        }
        new ModernDialog("Payment not found. Receipt unavailable if not yet paid.");
    }

    private void exportReceiptToPDF() {
        String receiptID = txtReceiptID.getText().trim();
        if (receiptID.isEmpty()) { 
            new ModernDialog("Enter a Receipt ID to export."); 
            return; 
        }

        String targetReceiptData = null;
        for (String line : FileUtil.readFile("receipt.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(receiptID)) {
                String[] appDetails = getAppointmentDetails(data[2]);
                targetReceiptData = buildReceiptDisplay(data[0], data[1], data[2], data[3], data[4], data[5], appDetails[0], appDetails[1], appDetails[2]);
                break;
            }
        }

        if (targetReceiptData == null) { 
            new ModernDialog("Receipt ID not found. Generate it first."); 
            return; 
        }

        JTextArea printArea = new JTextArea(); 
        printArea.setFont(new Font("Monospaced", Font.BOLD, 14)); 
        printArea.setText("\n\n\n" + targetReceiptData);
        
        try { 
            if (printArea.print(null, null, true, null, null, true)) {
                new ModernDialog("Export successfully initiated.");
            } 
        } catch (PrinterException pe) { 
            new ModernDialog("Export failed: " + pe.getMessage()); 
        }
    }


    private String buildReceiptDisplay(String rID, String pID, String appID, String custID, String amt, String meth, String service, String task, String techID) { 
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a"); 
        String currentDate = LocalDate.now().format(dateFormatter); 
        String currentTime = LocalTime.now().format(timeFormatter).toUpperCase(); 

        StringBuilder sb = new StringBuilder(); 
        sb.append("            APU AUTOMOTIVE SERVICE CENTER            \n"); 
        sb.append("---------------------------------------------------\n"); 
        sb.append(String.format("%-25s %25s\n", "RECEIPT", rID)); 
        sb.append(String.format("COUNTER STAFF : %-15s %19s\n", this.counterID, currentDate)); 
        sb.append(String.format("TECHNICIAN    : %-15s %19s\n", techID, currentTime)); 
        sb.append(String.format("CUSTOMER ID   : %-15s\n", custID)); 
        sb.append("---------------------------------------------------\n"); 
        sb.append(String.format("%-28s %3s %8s %9s\n", "Service", "Qty", "Price", "Total")); 
        sb.append("---------------------------------------------------\n"); 
        sb.append(String.format("%-28s %3s %8s %9s\n", service, "1", amt, amt)); 
        sb.append(String.format("  %-49s\n", task)); 
        sb.append("---------------------------------------------------\n"); 
        sb.append(String.format("%-39s %11s\n", "NET TOTAL", amt)); 
        sb.append(String.format("%-39s %11s\n", "PAYMENT METHOD", meth)); 
        sb.append("===================================================\n"); 
        sb.append("            Thank you for your business!            \n"); 
        sb.append("        Please keep this receipt for records.      \n"); 
        return sb.toString(); 
    }

    private void showPOSPreview(String content) {
        JTextArea previewArea = new JTextArea(content);
        previewArea.setFont(new Font("Consolas", Font.PLAIN, 13)); 
        previewArea.setBackground(new Color(20, 20, 30)); 
        previewArea.setForeground(new Color(0, 200, 255)); 
        previewArea.setEditable(false); 
        previewArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JOptionPane.showMessageDialog(this, previewArea, "Receipt Successfully Generated", JOptionPane.PLAIN_MESSAGE);
    }

    private String[] getAppointmentDetails(String appID) { 
        for (String line : FileUtil.readFile("appointment.txt")) { 
            String[] data = line.split(","); 
            if (data[0].equals(appID)) {
                return new String[]{data[3], data[6], data[8]}; 
            }
        } 
        return new String[]{"N/A", "N/A", "N/A"}; 
    }

    private boolean receiptIDExists(String receiptID) { 
        for (String line : FileUtil.readFile("receipt.txt")) {
            if (line.split(",")[0].equals(receiptID)) return true; 
        }
        return false; 
    }


    private void setupTableStyle() {
        table.setBackground(new Color(28, 28, 45));          
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setGridColor(new Color(50, 50, 70));           
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
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    private void addLabel(JPanel card, String text, int x, int y) { 
        JLabel label = new JLabel(text); 
        label.setBounds(x, y, 100, 32); 
        label.setForeground(Color.WHITE); 
        label.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        card.add(label); 
    }

    private JTextField addField(JPanel card, int x, int y) { 
        JTextField tf = new JTextField(); 
        tf.setBounds(x, y, 210, 32); 
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
    
    private JLabel infoLabel(String text) { 
        JLabel l = new JLabel(text); 
        l.setForeground(new Color(160, 160, 190)); 
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12)); 
        return l; 
    }
    
    private void clearFields() { 
        txtReceiptID.setText(""); 
        txtPaymentID.setText(""); 
        table.clearSelection(); 
    }
}