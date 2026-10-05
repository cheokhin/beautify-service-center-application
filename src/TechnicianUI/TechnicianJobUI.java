package TechnicianUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import UI.Components.*;

public class TechnicianJobUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String technicianID;

    private static final Color THEME  = new Color(255, 180, 0);   
    private static final Color ROW_BG = new Color(28, 28, 45);    

    public TechnicianJobUI(String id) {
        this.technicianID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(850, 460));
        card.setLayout(null);

        JLabel title = new JLabel("ASSIGNED JOBS", SwingConstants.CENTER);
        title.setBounds(0, 25, 850, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        JLabel hintLabel = new JLabel("Tip: Double-click 'Status' cells to update job progress", SwingConstants.CENTER);
        hintLabel.setBounds(0, 55, 850, 20);
        hintLabel.setForeground(new Color(160, 160, 180));
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        card.add(hintLabel);

        String[] columnNames = {"AppID", "CustomerID", "Date", "Service", "Status", "Task", "Duration"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(ROW_BG);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(50, 45, 20));
        table.setSelectionForeground(THEME);
        table.setGridColor(new Color(60, 60, 80));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(THEME);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 24));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(isSelected ? new Color(50, 45, 20) : ROW_BG);
                setForeground(isSelected ? THEME : Color.WHITE);
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table.getColumnModel().getColumn(4).setCellRenderer(center);

        String[] statuses = {"Pending", "In Progress", "Done"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setBackground(ROW_BG);
        statusComboBox.setForeground(Color.WHITE);
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        statusComboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public Dimension getPreferredSize() { return new Dimension(16, getParent() != null ? getParent().getHeight() : 20); }
                    @Override
                    public Dimension getMinimumSize()   { return getPreferredSize(); }
                    @Override
                    public Dimension getMaximumSize()   { return getPreferredSize(); }
                    @Override
                    protected void paintComponent(Graphics g) {
                        g.setColor(ROW_BG);
                        g.fillRect(0, 0, getWidth(), getHeight());
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(160, 160, 180));
                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        int[] xs = {cx - 4, cx + 4, cx};
                        int[] ys = {cy - 2, cy - 2, cy + 3};
                        g2.fillPolygon(xs, ys, 3);
                    }
                };
                btn.setOpaque(true);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFocusable(false);
                return btn;
            }
        });

        TableColumn statusColumn = table.getColumnModel().getColumn(4);
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 80, 770, 340);
        scroll.getViewport().setBackground(ROW_BG);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(THEME, 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(scroll);

        loadData(model);

        model.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                if (c == 4) {
                    String appID     = model.getValueAt(r, 0).toString();
                    String newStatus = model.getValueAt(r, 4).toString();
                    autoSaveStatus(appID, newStatus);
                }
            }
        });

        add(card);
    }

    private void autoSaveStatus(String targetID, String newStatus) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("AppointmentID")) {
                    list.add(line);
                    continue;
                }
                String[] data = line.split(",");
                if (data[0].equals(targetID) && data[8].equals(technicianID)) {
                    data[5] = newStatus;
                    line = String.join(",", data);
                }
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("appointment.txt"))) {
            for (String s : list) {
                bw.write(s);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(DefaultTableModel model) {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("AppointmentID")) continue;
                String[] data = line.split(",");
                if (data[8].equals(technicianID)) {
                    model.addRow(new Object[]{data[0], data[1], data[2], data[3], data[5], data[6], data[7]});
                }
            }
            if (model.getRowCount() == 0) new ModernDialog("No Assigned Jobs Found!");
        } catch (Exception e) {
            new ModernDialog("Error loading jobs!");
            e.printStackTrace();
        }
    }
}