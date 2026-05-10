package CounterStaffUI;

import javax.swing.*;
import java.util.List;
import UTILS.FileUtil;

public class Receipt extends JFrame {

    private static final long serialVersionUID = 1L;
    private String counterID;

    // ── UI Components ──────────────────────────────────────────────
    private JTextField txtReceiptID, txtPaymentID;
    private JTextArea receiptArea;

    // ── Constructor ────────────────────────────────────────────────
    public Receipt(String counterID) {
        this.counterID = counterID;

        setTitle("Generate Receipt");
        setSize(650, 520);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    // ── UI Initialization ──────────────────────────────────────────
    private void initComponents() {
        JLabel title = new JLabel("GENERATE RECEIPT");
        title.setBounds(240, 20, 170, 30);
        add(title);

        JLabel lblReceiptID = new JLabel("Receipt ID:");
        JLabel lblPaymentID = new JLabel("Payment ID:");

        lblReceiptID.setBounds(60,  80, 100, 25);
        lblPaymentID.setBounds(60, 120, 100, 25);

        add(lblReceiptID);
        add(lblPaymentID);

        txtReceiptID = new JTextField();
        txtPaymentID = new JTextField();

        txtReceiptID.setBounds(160,  80, 200, 25);
        txtPaymentID.setBounds(160, 120, 200, 25);

        add(txtReceiptID);
        add(txtPaymentID);

        JButton generateBtn = new JButton("Generate");
        JButton viewBtn     = new JButton("View Receipts");
        JButton backBtn     = new JButton("Back");

        generateBtn.setBounds(400, 100, 140, 30);
        viewBtn.setBounds(180,     170, 130, 30);
        backBtn.setBounds(340,     170, 130, 30);

        add(generateBtn);
        add(viewBtn);
        add(backBtn);

        receiptArea = new JTextArea();
        JScrollPane sp = new JScrollPane(receiptArea);
        sp.setBounds(60, 230, 500, 220);
        add(sp);

        // ── Action Listeners ───────────────────────────────────────
        generateBtn.addActionListener(e -> generateReceipt());
        viewBtn.addActionListener(e     -> viewReceipts());
        backBtn.addActionListener(e -> {
            new PaymentMenu(counterID);
            dispose();
        });
    }

    // ── Generate Receipt ───────────────────────────────────────────
    private void generateReceipt() {
        String receiptID = txtReceiptID.getText().trim();
        String paymentID = txtPaymentID.getText().trim();

        // 1. Empty field check
        if (receiptID.isEmpty() || paymentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Receipt ID and Payment ID.");
            return;
        }

        // 2. Receipt ID format check
        if (!receiptID.matches("R\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Receipt ID must be like R001.");
            return;
        }

        // 3. Duplicate receipt ID check
        if (receiptIDExists(receiptID)) {
            JOptionPane.showMessageDialog(this, "Receipt ID already exists.");
            return;
        }

        // 4. Find payment and generate receipt
        List<String> payments = FileUtil.readFile("payment.txt");
        for (String line : payments) {
            String[] data = line.split(",");
            if (data[0].equals(paymentID)) {
                String appointmentID = data[1];
                String customerID    = data[2];
                String amount        = data[3];
                String method        = data[4];
                String date          = data[5];

                String receiptLine = receiptID + "," + paymentID + "," + appointmentID + "," +
                                     customerID + "," + amount + "," + method + "," + date;
                FileUtil.appendFile("receipt.txt", receiptLine);

                receiptArea.setText(buildReceiptDisplay(
                    receiptID, paymentID, appointmentID, customerID, amount, method, date
                ));

                JOptionPane.showMessageDialog(this, "Receipt generated successfully.");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Payment not found. Receipt unavailable if not yet paid.");
    }

    // ── View Receipts ──────────────────────────────────────────────
    private void viewReceipts() {
        List<String> lines = FileUtil.readFile("receipt.txt");
        receiptArea.setText("=== RECEIPTS ===\n");
        for (String line : lines) {
            receiptArea.append(line + "\n");
        }
    }

    // ── Helpers & Validation ───────────────────────────────────────
    private boolean receiptIDExists(String receiptID) {
        List<String> lines = FileUtil.readFile("receipt.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(receiptID)) return true;
        }
        return false;
    }

    /**
     * Builds the formatted receipt display string for the text area.
     */
    private String buildReceiptDisplay(String receiptID, String paymentID, String appointmentID,
                                        String customerID, String amount, String method, String date) {
        return "========= RECEIPT =========\n" +
               "Receipt ID     : " + receiptID     + "\n" +
               "Payment ID     : " + paymentID     + "\n" +
               "Appointment ID : " + appointmentID + "\n" +
               "Customer ID    : " + customerID    + "\n" +
               "Amount         : RM " + amount     + "\n" +
               "Method         : " + method        + "\n" +
               "Date           : " + date          + "\n" +
               "===========================\n";
    }
}