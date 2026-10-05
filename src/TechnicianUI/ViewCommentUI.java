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
        card.setPreferredSize(new Dimension(850, 460)); 
        card.setLayout(null);

        JLabel title = new JLabel("CUSTOMER COMMENTS & RATINGS", SwingConstants.CENTER);
        title.setBounds(0, 20, 850, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"AppointmentID", "CustomerID", "Rating", "Date", "Comment"});

        JTable table = new JTable(model) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table.setBackground(new Color(28, 28, 45));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 80));
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(255, 180, 0));
        table.getTableHeader().setPreferredSize(new Dimension(0, 24));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(255, 180, 0));
        table.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(28, 28, 45));
        centerRenderer.setForeground(Color.WHITE);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 4) {
                table.getColumnModel().getColumn(i).setCellRenderer(new WordWrapRenderer());
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(350);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(40, 80, 770, 330);
        sp.getViewport().setBackground(new Color(28, 28, 45));
        sp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 180, 0), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(sp);

        loadComments();
        add(card);
    }

    private void loadComments() {
        try (BufferedReader commentReader = new BufferedReader(new FileReader("comment.txt"))) {
            String cLine;
            while ((cLine = commentReader.readLine()) != null) {
                String[] cData = cLine.split(",");
                if (cData.length < 6 || !cData[2].equalsIgnoreCase("Technician")) continue;

                try (BufferedReader apReader = new BufferedReader(new FileReader("appointment.txt"))) {
                    String aLine;
                    while ((aLine = apReader.readLine()) != null) {
                        String[] aData = aLine.split(",");
                        if (aData.length >= 9 && aData[0].equals(cData[0]) && aData[8].equals(technicianID)) {
                            
                            String starRating = cData[5] + " \u2605"; 
                            
                            model.addRow(new Object[]{
                                cData[0], 
                                cData[1], 
                                starRating, 
                                (cData.length >= 7) ? cData[6] : "-", 
                                cData[4]
                            });
                            break;
                        }
                    }
                }
            }
            if (model.getRowCount() == 0) new ModernDialog("No Comments Found!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private class WordWrapRenderer extends JTextArea implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        public WordWrapRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(new Color(28, 28, 45));
                setForeground(Color.WHITE);
            }

            int columnWidth = table.getColumnModel().getColumn(column).getWidth();
            setSize(new Dimension(columnWidth, getPreferredSize().height));
            
            int prefHeight = getPreferredSize().height;
            if (table.getRowHeight(row) != prefHeight) {
                table.setRowHeight(row, Math.max(prefHeight, 30));
            }
            
            return this;
        }
    }
}