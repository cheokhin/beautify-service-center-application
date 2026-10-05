package CustomerUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import UI.Components.*;

public class CustomerPaymentHistoryUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String customerID;

    private static final Color THEME  = new Color(30, 111, 217);
    private static final Color ROW_BG = new Color(28, 28, 45);
    private static final Color SEL_BG = new Color(20, 80, 165);

    public CustomerPaymentHistoryUI(String id) {
        this.customerID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(850, 460));
        card.setLayout(null);

        JLabel title = new JLabel("PAYMENT HISTORY", SwingConstants.CENTER);
        title.setBounds(0, 25, 850, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        String[] column = {"PaymentID", "AppID", "Amount (RM)", "Method", "Date"};
        DefaultTableModel model = new DefaultTableModel(column, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(ROW_BG);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(SEL_BG);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 80));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(THEME);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(isSelected ? SEL_BG : ROW_BG);
                setForeground(Color.WHITE);
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
            .setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                String text = value != null ? value.toString().trim() : "";

                String emoji;
                Color circleColor;
                switch (text) {
                    case "Cash":           emoji = "💵"; circleColor = new Color(34, 139, 80);  break;
                    case "Card":           emoji = "💳"; circleColor = new Color(30, 111, 217); break;
                    case "Online Banking": emoji = "📱"; circleColor = new Color(140, 60, 200); break;
                    default:               emoji = "💰"; circleColor = new Color(180, 120, 20); break;
                }

                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
                panel.setOpaque(true);
                panel.setBackground(isSelected ? SEL_BG : ROW_BG);

                JPanel circle = new JPanel() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                            RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(circleColor);
                        g2.fillOval(0, 0, getWidth(), getHeight());
                    }
                };
                circle.setOpaque(false);
                circle.setPreferredSize(new Dimension(26, 26));
                circle.setLayout(new BorderLayout());

                JLabel iconLabel = new JLabel(emoji, SwingConstants.CENTER);
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
                circle.add(iconLabel, BorderLayout.CENTER);

                JLabel textLabel = new JLabel(text);
                textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                textLabel.setForeground(Color.WHITE);

                panel.add(circle);
                panel.add(textLabel);
                return panel;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 80, 770, 340);
        scroll.getViewport().setBackground(ROW_BG);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(THEME, 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(scroll);

        loadData(model);
        add(card);
    }

    private void loadData(DefaultTableModel model) {
        try (BufferedReader br = new BufferedReader(new FileReader("payment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[2].equals(customerID)) {
                    model.addRow(new Object[]{data[0], data[1], data[3], data[4], data[5]});
                }
            }
            if (model.getRowCount() == 0) new ModernDialog("No Payment History Found!");
        } catch (Exception e) { new ModernDialog("Error loading history!"); }
    }
}