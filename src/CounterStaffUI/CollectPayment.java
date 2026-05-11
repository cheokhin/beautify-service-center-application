package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import UTILS.FileUtil;
import UI.Components.*; 

public class CollectPayment extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    private JTextField txtPaymentID, txtAppointmentID, txtCustomerID, txtAmount, txtDate;
    private JComboBox<String> methodBox;
    private JTextArea textArea;

    public CollectPayment(String counterID) {
        this.counterID = counterID;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(760, 520));
        card.setLayout(null);

        JLabel title = new JLabel("COLLECT PAYMENT", SwingConstants.CENTER);
        title.setBounds(0, 10, 760, 35);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        card.add(title);

        addLabel(card, "Payment ID:", 60);
        txtPaymentID = addField(card, 60);

        addLabel(card, "Appointment ID:", 110);
        txtAppointmentID = addField(card, 110);
        txtAppointmentID.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) { autoLoadAppointment(); }
        });

        addLabel(card, "Customer ID:", 160);
        txtCustomerID = addField(card, 160);
        txtCustomerID.setEditable(false);

        addLabel(card, "Amount:", 210);
        txtAmount = addField(card, 210);
        txtAmount.setEditable(false);

        addLabel(card, "Method:", 260);
        methodBox = new JComboBox<>(new String[]{"Cash","Card","Online Banking"});
        methodBox.setBounds(220, 260, 220, 35);
        card.add(methodBox);

        addLabel(card, "Date:", 310);
        txtDate = addField(card, 310);
        txtDate.setText(LocalDate.now().toString());

        JButton loadBtn = new ModernButton("Load Appointment");
        loadBtn.setBounds(500, 90, 190, 40);

        JButton collectBtn = new ModernButton("Collect Payment");
        collectBtn.setBounds(500, 145, 190, 40);

        JButton viewBtn = new ModernButton("View Payments");
        viewBtn.setBounds(500, 200, 190, 40);

        JButton backBtn = new ModernButton("Back");
        backBtn.setBounds(500, 255, 190, 40);

        card.add(loadBtn);
        card.add(collectBtn);
        card.add(viewBtn);
        card.add(backBtn);

        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setBackground(new Color(20, 20, 30));
        textArea.setForeground(new Color(0, 200, 255));
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(50, 370, 650, 110);
        card.add(sp);

        loadBtn.addActionListener(e -> loadAppointment());
        collectBtn.addActionListener(e -> collectPayment());
        viewBtn.addActionListener(e -> viewPayments());
        
        // 🔥 SPA Back Router
        backBtn.addActionListener(e -> {
            CounterStaffMenu.instance.rightContainer.add(new PaymentMenu(this.counterID), "PAYMENT");
            CounterStaffMenu.instance.showRightPage("PAYMENT");
        });

        add(card);
    }

    private void addLabel(JPanel card, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(60, y, 140, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(label);
    }

    private JTextField addField(JPanel card, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(220, y, 220, 35);
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        card.add(tf);
        return tf;
    }

    private void loadAppointment() {
        String appointmentID = txtAppointmentID.getText().trim();
        if (appointmentID.isEmpty()) { new ModernDialog("Enter Appointment ID."); return; }

        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(appointmentID)) {
                if (!isAppointmentPayable(data)) return;
                txtCustomerID.setText(data[1]);
                txtAmount.setText(getServicePrice(data[3]));
                new ModernDialog("Appointment loaded.");
                return;
            }
        }
        new ModernDialog("Appointment not found.");
    }

    private void autoLoadAppointment() {
        String appointmentID = txtAppointmentID.getText().trim();
        if (appointmentID.isEmpty()) return;

        for (String line : FileUtil.readFile("appointment.txt")) {
            String[] data = line.split(",");
            if (data[0].equals(appointmentID)) {
                if (!isAppointmentPayable(data)) return;
                txtCustomerID.setText(data[1]);
                txtAmount.setText(getServicePrice(data[3]));
                return;
            }
        }
    }

    private boolean isAppointmentPayable(String[] data) {
        if (data[4].equalsIgnoreCase("Paid")) { new ModernDialog("This appointment is already paid."); return false; }
        if (data[4].equalsIgnoreCase("Cancel")) { new ModernDialog("Cancelled appointment cannot be paid."); return false; }
        return true;
    }

    private void collectPayment() {
        if (txtPaymentID.getText().trim().isEmpty() || txtAppointmentID.getText().trim().isEmpty() || txtCustomerID.getText().trim().isEmpty()) {
            new ModernDialog("Please load appointment first."); return;
        }
        if (!txtPaymentID.getText().trim().matches("P\\d{3}")) { new ModernDialog("Payment ID must be P001 format."); return; }
        if (!txtDate.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) { new ModernDialog("Date must be YYYY-MM-DD"); return; }
        if (paymentIDExists(txtPaymentID.getText().trim())) { new ModernDialog("Payment ID already exists."); return; }
        if (appointmentAlreadyPaid(txtAppointmentID.getText().trim())) { new ModernDialog("This appointment already paid."); return; }

        String paymentLine = txtPaymentID.getText().trim() + "," + txtAppointmentID.getText().trim() + "," + txtCustomerID.getText().trim() + "," +
                             txtAmount.getText().trim() + "," + methodBox.getSelectedItem() + "," + txtDate.getText().trim();
        FileUtil.appendFile("payment.txt", paymentLine);
        updateAppointmentStatus(txtAppointmentID.getText().trim());

        new ModernDialog("Payment collected successfully.");
        viewPayments();
    }

    private void updateAppointmentStatus(String appointmentID) {
        List<String> appointments = FileUtil.readFile("appointment.txt");
        for (int i = 0; i < appointments.size(); i++) {
            String[] data = appointments.get(i).split(",");
            if (data[0].equals(appointmentID)) {
                data[4] = "Paid";
                appointments.set(i, String.join(",", data));
                break;
            }
        }
        FileUtil.writeFile("appointment.txt", appointments);
    }

    private void viewPayments() {
        textArea.setText("=== PAYMENTS ===\n\n");
        for (String line : FileUtil.readFile("payment.txt")) textArea.append(line + "\n");
        textArea.setCaretPosition(0);
    }

    private boolean paymentIDExists(String paymentID) {
        for (String line : FileUtil.readFile("payment.txt")) {
            if (line.split(",")[0].equals(paymentID)) return true;
        }
        return false;
    }

    private boolean appointmentAlreadyPaid(String appointmentID) {
        for (String line : FileUtil.readFile("payment.txt")) {
            if (line.split(",")[1].equals(appointmentID)) return true;
        }
        return false;
    }

    private String getServicePrice(String serviceType) {
        for (String line : FileUtil.readFile("prices.txt")) {
            if (line.split(",")[0].equalsIgnoreCase(serviceType)) return line.split(",")[1];
        }
        return "0.00";
    }
}