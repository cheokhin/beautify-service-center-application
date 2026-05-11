package TechnicianUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import UI.Components.*; 

public class ViewCommentUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String technicianID;
    private DefaultTableModel model;

    public ViewCommentUI(String id) {
        this.technicianID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(780, 360)); 
        card.setLayout(null);

        JLabel title = new JLabel("CUSTOMER COMMENTS & RATINGS", SwingConstants.CENTER);
        title.setBounds(0, 20, 780, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"AppointmentID", "CustomerID", "Comment", "Rating", "Date"});

        JTable table = new JTable(model) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // 🔥 Yellow Theme for Header
        table.getTableHeader().setBackground(new Color(255, 180, 0));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(255, 180, 0));
        table.setSelectionForeground(Color.BLACK);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(40, 80, 700, 240);
        sp.getViewport().setBackground(Color.WHITE);
        card.add(sp);

        loadComments();
        add(card);
    }

    private void loadComments() {
        try (BufferedReader commentReader = new BufferedReader(new FileReader("comment.txt"))) {
            String cLine;
            while ((cLine = commentReader.readLine()) != null) {
                String[] cData = cLine.split(",");
                if (cData.length < 5 || !cData[2].equalsIgnoreCase("Technician")) continue;

                try (BufferedReader apReader = new BufferedReader(new FileReader("appointment.txt"))) {
                    String aLine;
                    while ((aLine = apReader.readLine()) != null) {
                        String[] aData = aLine.split(",");
                        if (aData.length >= 9 && aData[0].equals(cData[0]) && aData[8].equals(technicianID)) {
                            model.addRow(new Object[]{cData[0], cData[1], cData[3], cData[4], (cData.length >= 6) ? cData[5] : "-"});
                            break;
                        }
                    }
                }
            }
            if (model.getRowCount() == 0) new ModernDialog("No Comments Found!");
        } catch (Exception e) { e.printStackTrace(); }
    }
}