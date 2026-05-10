package CustomerUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class CustomerPaymentHistoryUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private String customerID;

    public CustomerPaymentHistoryUI(String id) {

        this.customerID = id;

        setTitle("Payment History");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =========================
        // Background
        // =========================
        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg);

        // =========================
        // Main Card
        // =========================
        JPanel card = new RoundedPanel(30, new Color(35,35,50));
        card.setPreferredSize(new Dimension(820, 400));
        card.setLayout(null);

        // =========================
        // Title
        // =========================
        JLabel title = new JLabel("PAYMENT HISTORY", SwingConstants.CENTER);
        title.setBounds(240, 20, 340, 35);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        Icon titleIcon = resizeIcon(
                UIManager.getIcon("FileView.hardDriveIcon"),
                24,
                24
        );

        title.setIcon(titleIcon);
        title.setIconTextGap(10);

        card.add(title);

        // =========================
        // Table
        // =========================
        String[] column = {
                "ReceiptID",
                "PaymentID",
                "AppointmentID",
                "Amount",
                "Method",
                "Date"
        };

        DefaultTableModel model = new DefaultTableModel(column, 0);

        JTable table = new JTable(model) {

            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // =========================
        // Table Style
        // =========================
        table.setBackground(new Color(45,45,60));
        table.setForeground(Color.WHITE);

        table.setRowHeight(28);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.setGridColor(new Color(70,70,90));

        table.setSelectionBackground(new Color(0,200,255));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();

        header.setBackground(new Color(0,200,255));
        header.setForeground(Color.BLACK);

        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBounds(40, 80, 740, 220);

        scroll.getViewport().setBackground(new Color(45,45,60));

        card.add(scroll);

        // =========================
        // Back Button
        // =========================
        Icon backIcon = resizeIcon(
                UIManager.getIcon("OptionPane.errorIcon"),
                18,
                18
        );

        JButton backBtn = new ModernButton(" Back", backIcon);

        backBtn.setBounds(310, 320, 180, 40);

        card.add(backBtn);

        // =========================
        // Load Data
        // =========================
        loadData(model);

        // =========================
        // Button Action
        // =========================
        backBtn.addActionListener(e -> {

            new CustomerUI(customerID);

            dispose();
        });

        // =========================
        // Add Card
        // =========================
        bg.add(card);

        setVisible(true);
    }

    // =========================================================
    // Load Data
    // =========================================================
    private void loadData(DefaultTableModel model) {

        try (BufferedReader br =
                     new BufferedReader(new FileReader("receipt.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                // Skip Header
                if (line.startsWith("ReceiptID"))
                    continue;

                String[] data = line.split(",");

                // CustomerID = data[3]
                if (data[3].equals(customerID)) {

                    model.addRow(new Object[]{

                            data[0], // ReceiptID
                            data[1], // PaymentID
                            data[2], // AppointmentID
                            data[4], // Amount
                            data[5], // Method
                            data[6]  // Date
                    });
                }
            }

            // No Data
            if (model.getRowCount() == 0) {

                new ModernDialog(
                        this,
                        "No Payment History Found!"
                );
            }

        } catch (Exception e) {

            new ModernDialog(
                    this,
                    "Error loading payment data!"
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // Gradient Background
    // =========================================================
    class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(

                    0,0,new Color(20,20,40),

                    getWidth(),
                    getHeight(),

                    new Color(0,200,255)
            );

            g2.setPaint(gp);

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
        }
    }

    // =========================================================
    // Rounded Panel
    // =========================================================
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
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================================
    // Modern Button
    // =========================================================
    class ModernButton extends JButton {

        private static final long serialVersionUID = 1L;

        private boolean hovering = false;

        private boolean pressed = false;

        private Color bgColor = new Color(60,60,80);

        private Color hoverColor = new Color(0,200,255);

        public ModernButton(String text, Icon icon) {

            super(text, icon);

            setContentAreaFilled(false);

            setFocusPainted(false);

            setBorderPainted(false);

            setForeground(Color.WHITE);

            setFont(new Font("Segoe UI", Font.PLAIN, 14));

            setHorizontalAlignment(SwingConstants.CENTER);

            setIconTextGap(10);

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
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();

            int h = getHeight();

            // Shadow
            if (!pressed) {

                g2.setColor(new Color(0,0,0,80));

                g2.fillRoundRect(
                        4,
                        4,
                        w-4,
                        h-4,
                        20,
                        20
                );
            }

            // Button Color
            if (pressed)
                g2.setColor(bgColor.darker());

            else if (hovering)
                g2.setColor(hoverColor);

            else
                g2.setColor(bgColor);

            g2.fillRoundRect(
                    0,
                    0,
                    w-4,
                    h-4,
                    20,
                    20
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================================
    // Modern Dialog
    // =========================================================
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
                            new Color(0,200,255),
                            2
                    )
            );

            add(panel);

            // =========================
            // Icon
            // =========================
            Icon icon =
                    UIManager.getIcon(
                            "OptionPane.informationIcon"
                    );

            JLabel iconLabel = new JLabel(
                    resizeIcon(icon,30,30)
            );

            iconLabel.setBounds(20,30,30,30);

            panel.add(iconLabel);

            // =========================
            // Message
            // =========================
            JLabel msg = new JLabel(
                    "<html>" + message + "</html>"
            );

            msg.setBounds(60,20,220,40);

            msg.setForeground(Color.WHITE);

            msg.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            panel.add(msg);

            // =========================
            // OK Button
            // =========================
            JButton okBtn = new JButton("OK");

            okBtn.setBounds(70,80,100,30);

            okBtn.setFocusPainted(false);

            okBtn.setBackground(
                    new Color(60,60,80)
            );

            okBtn.setForeground(Color.WHITE);

            okBtn.addActionListener(e -> dispose());

            panel.add(okBtn);

            // =========================
            // Animation
            // =========================
            animate();

            setVisible(true);
        }

        // =====================================================
        // Animation
        // =====================================================
        private void animate() {

            Timer timer = new Timer(15, null);

            final float[] opacity = {0f};

            final double[] scale = {0.8};

            timer.addActionListener(e -> {

                if (opacity[0] < 1f) {

                    opacity[0] += 0.08f;

                    scale[0] += 0.03;

                    setOpacity(
                            Math.min(opacity[0],1f)
                    );

                    int w = (int)(200 * scale[0]);

                    int h = (int)(120 * scale[0]);

                    setSize(w,h);

                    setLocationRelativeTo(getParent());

                } else {

                    timer.stop();
                }
            });

            timer.start();
        }
    }

    // =========================================================
    // Resize Icon
    // =========================================================
    private Icon resizeIcon(Icon icon, int w, int h) {

        Image img = ((ImageIcon) icon)
                .getImage()
                .getScaledInstance(
                        w,
                        h,
                        Image.SCALE_SMOOTH
                );

        return new ImageIcon(img);
    }
}