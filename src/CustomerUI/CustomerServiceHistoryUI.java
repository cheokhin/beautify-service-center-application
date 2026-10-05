package CustomerUI;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import UI.Components.*;

public class CustomerServiceHistoryUI extends JPanel {
    private static final long serialVersionUID = 1L;
    private String customerID;

    private static final Color THEME  = new Color(30, 111, 217);
    private static final Color ROW_BG = new Color(28, 28, 45);
    private static final Color SEL_BG = new Color(20, 80, 165);

    public CustomerServiceHistoryUI(String id) {
        this.customerID = id;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(950, 460));
        card.setLayout(null);

        JLabel title = new JLabel("SERVICE HISTORY", SwingConstants.CENTER);
        title.setBounds(0, 25, 950, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        String[] column = {"AppID", "Date", "Service", "Status", "Payment", "Task", "Technician"};
        DefaultTableModel model = new DefaultTableModel(column, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(Color.WHITE);
        table.setBackground(ROW_BG);
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
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
            .setHorizontalAlignment(JLabel.CENTER);

        int statusCol = 3;
        table.getColumnModel().getColumn(statusCol).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 7));
                panel.setOpaque(true);
                panel.setBackground(isSelected ? SEL_BG : ROW_BG);

                JPanel dot = new JPanel() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        String status = value != null ? value.toString().trim() : "";
                        Color dotColor;
                        switch (status) {
                            case "Done":        dotColor = new Color(34, 197, 94);  break;
                            case "In Progress": dotColor = new Color(251, 191, 36); break;
                            case "Pending":     dotColor = new Color(239, 68, 68);  break;
                            default:            dotColor = Color.GRAY;              break;
                        }
                        g2.setColor(dotColor);
                        g2.fillOval(0, 0, getWidth(), getHeight());
                    }
                };
                dot.setPreferredSize(new Dimension(10, 10));
                dot.setOpaque(false);

                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setForeground(Color.WHITE);
                label.setFont(table.getFont());

                panel.add(dot);
                panel.add(label);
                return panel;
            }
        });

        int paymentCol = 4;
        table.getColumnModel().getColumn(paymentCol).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 7));
                panel.setOpaque(true);
                panel.setBackground(isSelected ? SEL_BG : ROW_BG);

                String text = value != null ? value.toString().trim() : "";

                JLabel rounded = new JLabel(text) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        super.paintComponent(g);
                    }
                };
                rounded.setText(text);
                rounded.setFont(new Font("Segoe UI", Font.BOLD, 11));
                rounded.setHorizontalAlignment(SwingConstants.CENTER);
                rounded.setOpaque(false);
                rounded.setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 12));

                switch (text) {
                    case "Paid":
                        rounded.setBackground(new Color(220, 252, 231));
                        rounded.setForeground(new Color(22, 101, 52));
                        break;
                    case "Unpaid":
                        rounded.setBackground(new Color(254, 226, 226));
                        rounded.setForeground(new Color(153, 27, 27));
                        break;
                    case "Cancelled":
                        rounded.setBackground(new Color(243, 244, 246));
                        rounded.setForeground(new Color(55, 65, 81));
                        break;
                    default:
                        rounded.setBackground(new Color(243, 244, 246));
                        rounded.setForeground(new Color(55, 65, 81));
                        break;
                }

                panel.add(rounded);
                return panel;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 80, 870, 340);
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
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("AppointmentID")) continue;
                String[] data = line.split(",");
                if (data[1].trim().equals(customerID)) {
                    model.addRow(new Object[]{
                        data[0],  
                        data[2],  
                        data[3],  
                        data[5],  
                        data[4],  
                        data[6],  
                        data[8]   
                    });
                }
            }
            if (model.getRowCount() == 0) new ModernDialog("No Service History Found!");
        } catch (Exception e) { new ModernDialog("Error loading history!"); }
    }
}