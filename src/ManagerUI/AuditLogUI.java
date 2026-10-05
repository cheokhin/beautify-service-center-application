package ManagerUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import UI.Components.*;

public class AuditLogUI extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;

    public AuditLogUI() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(960, 580));
        card.setLayout(null);

        JLabel title = new JLabel("SYSTEM AUDIT LOGS", SwingConstants.CENTER);
        title.setBounds(0, 20, 960, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        card.add(title);

        JLabel searchLbl = new JLabel("Search Logs:");
        searchLbl.setBounds(40, 70, 100, 30);
        searchLbl.setForeground(Color.LIGHT_GRAY);
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(searchLbl);

        searchField = new JTextField();
        searchField.setBounds(130, 70, 300, 30);
        searchField.setBackground(new Color(55, 55, 75));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 50, 80), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        card.add(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
            private void search() {
                String text = searchField.getText().trim();
                if (text.length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        String[] columns = {"TIMESTAMP", "USER ID", "ROLE", "ACTION PERFORMED"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        setupTableStyle();
        
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        table.getColumnModel().getColumn(0).setPreferredWidth(150); 
        table.getColumnModel().getColumn(1).setPreferredWidth(80); 
        table.getColumnModel().getColumn(2).setPreferredWidth(120); 
        table.getColumnModel().getColumn(3).setPreferredWidth(450); 

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(40, 120, 880, 420);
        sp.getViewport().setBackground(new Color(25, 25, 35));
        sp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 50, 80), 2),
            BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        card.add(sp);

        loadAuditData();
        add(card);
    }

    private void setupTableStyle() {
        table.setBackground(new Color(25, 25, 35));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setGridColor(new Color(100, 100, 130));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        table.setSelectionBackground(new Color(180, 35, 55));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(new Color(255, 50, 80)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                return c;
            }
        };
        table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
    }

    private void loadAuditData() {
        tableModel.setRowCount(0);
        File auditFile = new File("audit.txt");
        if (!auditFile.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(auditFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", 4); 
                if (d.length == 4) {
                    tableModel.addRow(new Object[]{d[0], d[1], d[2], d[3]});
                }
            }
        } catch (Exception e) {}
    }
}