package TechnicianUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import UI.Components.*; // 🔥 Shared components

public class TechnicianJobUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String technicianID;

    public TechnicianJobUI(String id) {
        this.technicianID = id;

        setOpaque(false); // 🔥 Transparent for SPA
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(850, 460)); // Adjusted height
        card.setLayout(null);

        JLabel title = new JLabel("ASSIGNED JOBS", SwingConstants.CENTER);
        title.setBounds(0, 25, 850, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        String[] column = {"AppID", "CustomerID", "Date", "Service", "Status", "Task", "Duration"};
        DefaultTableModel model = new DefaultTableModel(column, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(Color.BLACK);
        
        // Matches the Technician Yellow Theme
        table.setSelectionBackground(new Color(255, 180, 0));
        table.setSelectionForeground(Color.BLACK);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60, 60, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 80, 770, 340);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 180, 0), 2)); // Yellow border
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