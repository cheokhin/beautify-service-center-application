package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import UTILS.FileUtil;

public class CollectPayment extends JFrame {

    private static final long serialVersionUID = 1L;

    // ✅ FIXED counterID
    private String counterID;

    private JTextField txtPaymentID, txtAppointmentID,
            txtCustomerID, txtAmount, txtDate;

    private JComboBox<String> methodBox;
    private JTextArea textArea;

    public CollectPayment(String counterID) {

        // ✅ IMPORTANT
        this.counterID = counterID;

        setTitle("Collect Payment");
        setSize(850, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg);

        JPanel card = new RoundedPanel(30, new Color(35,35,50));
        card.setPreferredSize(new Dimension(760,520));
        card.setLayout(null);

        JLabel title = new JLabel("COLLECT PAYMENT", SwingConstants.CENTER);
        title.setBounds(240,10,280,35);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        card.add(title);

        // ================= LABELS =================

        addLabel(card,"Payment ID:",50);
        txtPaymentID = addField(card,50);

        addLabel(card,"Appointment ID:",100);
        txtAppointmentID = addField(card,100);

        txtAppointmentID.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                autoLoadAppointment();
            }
        });

        addLabel(card,"Customer ID:",150);
        txtCustomerID = addField(card,150);
        txtCustomerID.setEditable(false);

        addLabel(card,"Amount:",200);
        txtAmount = addField(card,200);
        txtAmount.setEditable(false);

        addLabel(card,"Method:",250);

        methodBox = new JComboBox<>(
                new String[]{"Cash","Card","Online Banking"});

        methodBox.setBounds(220,250,220,35);
        methodBox.setBackground(Color.WHITE);
        methodBox.setForeground(Color.BLACK);
        methodBox.setFont(new Font("Segoe UI",Font.PLAIN,13));

        card.add(methodBox);

        addLabel(card,"Date:",300);
        txtDate = addField(card,300);
        txtDate.setText(LocalDate.now().toString());

        // ================= BUTTONS =================

        JButton loadBtn = new ModernButton("Load Appointment");
        loadBtn.setBounds(500,90,190,40);

        JButton collectBtn = new ModernButton("Collect Payment");
        collectBtn.setBounds(500,145,190,40);

        JButton viewBtn = new ModernButton("View Payments");
        viewBtn.setBounds(500,200,190,40);

        JButton backBtn = new ModernButton("Back");
        backBtn.setBounds(500,255,190,40);

        card.add(loadBtn);
        card.add(collectBtn);
        card.add(viewBtn);
        card.add(backBtn);

        // ================= TEXT AREA =================

        textArea = new JTextArea();

        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setBackground(new Color(45,45,60));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);

        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(50,370,650,110);

        card.add(sp);

        // ================= ACTIONS =================

        loadBtn.addActionListener(e -> loadAppointment());

        collectBtn.addActionListener(e -> collectPayment());

        viewBtn.addActionListener(e -> viewPayments());

        // ✅ FIXED counterID ERROR
        backBtn.addActionListener(e -> {
            new PaymentMenu(this.counterID);
            dispose();
        });

        bg.add(card);

        setVisible(true);
    }

    // ================= LABEL =================

    private void addLabel(JPanel panel,String text,int y) {

        JLabel label = new JLabel(text);

        label.setBounds(60,y,140,30);

        label.setForeground(Color.WHITE);

        label.setFont(new Font("Segoe UI",Font.BOLD,14));

        panel.add(label);
    }

    // ================= FIELD =================

    private JTextField addField(JPanel panel,int y) {

        JTextField tf = new JTextField();

        tf.setBounds(220,y,220,35);

        tf.setBackground(new Color(55,55,70));
        tf.setForeground(Color.WHITE);

        tf.setCaretColor(Color.WHITE);

        tf.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        tf.setFont(new Font("Segoe UI",Font.PLAIN,13));

        panel.add(tf);

        return tf;
    }

    // ================= LOAD APPOINTMENT =================

    private void loadAppointment() {

        String appointmentID = txtAppointmentID.getText().trim();

        if (appointmentID.isEmpty()) {
            new ModernDialog(this,"Enter Appointment ID.");
            return;
        }

        List<String> appointments = FileUtil.readFile("appointment.txt");

        for (String line : appointments) {

            String[] data = line.split(",");

            if (data[0].equals(appointmentID)) {

                if (!isAppointmentPayable(data)) return;

                txtCustomerID.setText(data[1]);
                txtAmount.setText(getServicePrice(data[3]));

                new ModernDialog(this,"Appointment loaded.");
                return;
            }
        }

        new ModernDialog(this,"Appointment not found.");
    }

    // ================= AUTO LOAD =================

    private void autoLoadAppointment() {

        String appointmentID = txtAppointmentID.getText().trim();

        if (appointmentID.isEmpty()) return;

        List<String> appointments = FileUtil.readFile("appointment.txt");

        for (String line : appointments) {

            String[] data = line.split(",");

            if (data[0].equals(appointmentID)) {

                if (!isAppointmentPayable(data)) return;

                txtCustomerID.setText(data[1]);
                txtAmount.setText(getServicePrice(data[3]));

                return;
            }
        }
    }

    // ================= STATUS CHECK =================

    private boolean isAppointmentPayable(String[] data) {

        String status = data[4];

        if (status.equalsIgnoreCase("Paid")) {

            new ModernDialog(this,
                    "This appointment is already paid.");

            return false;
        }

        if (status.equalsIgnoreCase("Cancel")) {

            new ModernDialog(this,
                    "Cancelled appointment cannot be paid.");

            return false;
        }

        return true;
    }

    // ================= COLLECT PAYMENT =================

    private void collectPayment() {

        String paymentID = txtPaymentID.getText().trim();
        String appointmentID = txtAppointmentID.getText().trim();
        String customerID = txtCustomerID.getText().trim();
        String amount = txtAmount.getText().trim();
        String method = methodBox.getSelectedItem().toString();
        String date = txtDate.getText().trim();

        if (paymentID.isEmpty() ||
            appointmentID.isEmpty() ||
            customerID.isEmpty() ||
            amount.isEmpty()) {

            new ModernDialog(this,
                    "Please load appointment first.");
            return;
        }

        if (!paymentID.matches("P\\d{3}")) {

            new ModernDialog(this,
                    "Payment ID must be P001 format.");
            return;
        }

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {

            new ModernDialog(this,
                    "Date must be YYYY-MM-DD");
            return;
        }

        if (paymentIDExists(paymentID)) {

            new ModernDialog(this,
                    "Payment ID already exists.");
            return;
        }

        if (appointmentAlreadyPaid(appointmentID)) {

            new ModernDialog(this,
                    "This appointment already paid.");
            return;
        }

        String paymentLine =
                paymentID + "," +
                appointmentID + "," +
                customerID + "," +
                amount + "," +
                method + "," +
                date;

        FileUtil.appendFile("payment.txt", paymentLine);

        updateAppointmentStatus(appointmentID);

        new ModernDialog(this,
                "Payment collected successfully.");

        viewPayments();
    }

    // ================= UPDATE STATUS =================

    private void updateAppointmentStatus(String appointmentID) {

        List<String> appointments =
                FileUtil.readFile("appointment.txt");

        for (int i = 0; i < appointments.size(); i++) {

            String[] data = appointments.get(i).split(",");

            if (data[0].equals(appointmentID)) {

                appointments.set(i,
                        data[0] + "," +
                        data[1] + "," +
                        data[2] + "," +
                        data[3] + ",Paid," +
                        data[5] + "," +
                        data[6] + "," +
                        data[7] + "," +
                        data[8]);

                break;
            }
        }

        FileUtil.writeFile("appointment.txt", appointments);
    }

    // ================= VIEW PAYMENTS =================

    private void viewPayments() {

        List<String> lines = FileUtil.readFile("payment.txt");

        textArea.setText("=== PAYMENTS ===\n\n");

        for (String line : lines) {
            textArea.append(line + "\n");
        }
    }

    // ================= HELPERS =================

    private boolean paymentIDExists(String paymentID) {

        List<String> lines = FileUtil.readFile("payment.txt");

        for (String line : lines) {

            String[] data = line.split(",");

            if (data[0].equals(paymentID))
                return true;
        }

        return false;
    }

    private boolean appointmentAlreadyPaid(String appointmentID) {

        List<String> lines = FileUtil.readFile("payment.txt");

        for (String line : lines) {

            String[] data = line.split(",");

            if (data[1].equals(appointmentID))
                return true;
        }

        return false;
    }

    private String getServicePrice(String serviceType) {

        List<String> services = FileUtil.readFile("prices.txt");

        for (String line : services) {

            String[] data = line.split(",");

            if (data[0].equalsIgnoreCase(serviceType))
                return data[1];
        }

        return "0.00";
    }

    // ================= MODERN DIALOG =================

    class ModernDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        public ModernDialog(JFrame parent, String message) {

            super(parent, true);

            setUndecorated(true);
            setSize(320,160);
            setLocationRelativeTo(parent);
            setOpacity(0f);

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(30,30,30));

            panel.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(0,200,255),2));

            add(panel);

            JLabel msg = new JLabel(
                    "<html>" + message + "</html>");

            msg.setBounds(40,30,240,40);
            msg.setForeground(Color.WHITE);

            msg.setFont(new Font(
                    "Segoe UI", Font.PLAIN, 13));

            panel.add(msg);

            JButton okBtn = new JButton("OK");

            okBtn.setBounds(110,90,100,30);
            okBtn.setFocusPainted(false);
            okBtn.setBackground(new Color(60,60,80));
            okBtn.setForeground(Color.WHITE);

            okBtn.addActionListener(e -> dispose());

            panel.add(okBtn);

            animate();

            setVisible(true);
        }

        private void animate() {

            Timer timer = new Timer(15, null);

            final float[] opacity = {0f};

            timer.addActionListener(e -> {

                if (opacity[0] < 1f) {

                    opacity[0] += 0.08f;

                    setOpacity(Math.min(opacity[0],1f));

                } else {
                    timer.stop();
                }
            });

            timer.start();
        }
    }

    // ================= MODERN BUTTON =================

    class ModernButton extends JButton {

        private static final long serialVersionUID = 1L;

        private boolean hovering = false;
        private boolean pressed = false;

        private Color bgColor = new Color(60,60,80);
        private Color hoverColor = new Color(0,200,255);

        public ModernButton(String text) {

            super(text);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            setForeground(Color.WHITE);

            setFont(new Font("Segoe UI", Font.PLAIN, 14));

            addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hovering = false;
                    pressed = false;
                    repaint();
                }

                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (!pressed) {

                g2.setColor(new Color(0,0,0,80));

                g2.fillRoundRect(4,4,w-4,h-4,20,20);
            }

            if (pressed)
                g2.setColor(bgColor.darker());

            else if (hovering)
                g2.setColor(hoverColor);

            else
                g2.setColor(bgColor);

            g2.fillRoundRect(0,0,w-4,h-4,20,20);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // ================= ROUNDED PANEL =================

    class RoundedPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color color) {

            this.radius = radius;
            this.bgColor = color;

            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,0,getWidth(),getHeight(),
                    radius,radius);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // ================= GRADIENT =================

    class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(
                    0,0,new Color(20,20,40),
                    getWidth(),getHeight(),
                    new Color(0,200,255));

            g2.setPaint(gp);

            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }
}
