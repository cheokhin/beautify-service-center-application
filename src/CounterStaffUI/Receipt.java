package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import UTILS.FileUtil;
import UI.Components.*; 

public class Receipt extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtReceiptID, txtPaymentID;
    private JTextArea receiptArea;

    public Receipt(String counterID) {
        this.counterID = counterID;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(650, 520));
        card.setLayout(null);

        initComponents(card);
        add(card);
    }

    private void initComponents(JPanel card) {
        JLabel title = new JLabel("GENERATE RECEIPT", SwingConstants.CENTER);
        title.setBounds(0, 20, 650, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        JLabel lblReceiptID = new JLabel("Receipt ID:");
        JLabel lblPaymentID = new JLabel("Payment ID:");

        lblReceiptID.setBounds(60, 80, 100, 25);
        lblReceiptID.setForeground(Color.WHITE);
        lblPaymentID.setBounds(60, 130, 100, 25);
        lblPaymentID.setForeground(Color.WHITE);

        card.add(lblReceiptID);
        card.add(lblPaymentID);

        txtReceiptID = new JTextField();
        txtPaymentID = new JTextField();

        txtReceiptID.setBounds(160, 80, 200, 30);
        txtPaymentID.setBounds(160, 130, 200, 30);
        
        styleField(txtReceiptID);
        styleField(txtPaymentID);

        card.add(txtReceiptID);
        card.add(txtPaymentID);

        JButton generateBtn = new ModernButton("Generate");
        JButton viewBtn     = new ModernButton("View Receipts");
        JButton backBtn     = new ModernButton("Back");

        generateBtn.setBounds(400, 100, 160, 40);
        viewBtn.setBounds(160, 190, 160, 40);
        backBtn.setBounds(340, 190, 160, 40);

        card.add(generateBtn);
        card.add(viewBtn);
        card.add(backBtn);

        receiptArea = new JTextArea();
        receiptArea.setBackground(new Color(20, 20, 30));
        receiptArea.setForeground(new Color(0, 200, 255));
        receiptArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        
        JScrollPane sp = new JScrollPane(receiptArea);
        sp.setBounds(60, 250, 520, 220);
        card.add(sp);

        generateBtn.addActionListener(e -> generateReceipt());
        viewBtn.addActionListener(e -> viewReceipts());

        backBtn.addActionListener(e -> {
            CounterStaffMenu.instance.rightContainer.add(new PaymentMenu(counterID), "PAYMENT");
            CounterStaffMenu.instance.showRightPage("PAYMENT");
        });
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
    }

    private void generateReceipt() {
        String receiptID = txtReceiptID.getText().trim();
        String paymentID = txtPaymentID.getText().trim();

        if (receiptID.isEmpty() || paymentID.isEmpty()) { new ModernDialog("Please enter Receipt ID and Payment ID."); return; }
        if (!receiptID.matches("R\\d{3}")) { new ModernDialog("Receipt ID must be like R001."); return; }
        if (receiptIDExists(receiptID)) { new ModernDialog("Receipt ID already exists."); return; }

        for (String line : FileUtil.readFile("payment.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(paymentID)) {
                String receiptLine = receiptID + "," + paymentID + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5];
                FileUtil.appendFile("receipt.txt", receiptLine);
                receiptArea.setText(buildReceiptDisplay(receiptID, paymentID, data[1], data[2], data[3], data[4], data[5]));
                new ModernDialog("Receipt generated successfully.");
                return;
            }
        }
        new ModernDialog("Payment not found. Receipt unavailable if not yet paid.");
    }

    private void viewReceipts() {
        receiptArea.setText("=== RECEIPTS ===\n");
        for (String line : FileUtil.readFile("receipt.txt")) receiptArea.append(line + "\n");
        receiptArea.setCaretPosition(0);
    }

    private boolean receiptIDExists(String receiptID) {
        for (String line : FileUtil.readFile("receipt.txt")) {
            if (line.split(",")[0].equals(receiptID)) return true;
        }
        return false;
    }

    private String buildReceiptDisplay(String rID, String pID, String appID, String custID, String amt, String meth, String date) {
        return "========= RECEIPT =========\n" +
               "Receipt ID     : " + rID + "\n" +
               "Payment ID     : " + pID + "\n" +
               "Appointment ID : " + appID + "\n" +
               "Customer ID    : " + custID + "\n" +
               "Amount         : RM " + amt + "\n" +
               "Method         : " + meth + "\n" +
               "Date           : " + date + "\n" +
               "===========================\n";
    }
}