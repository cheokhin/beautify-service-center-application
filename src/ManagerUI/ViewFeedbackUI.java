package ManagerUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import UI.Components.*;

public class ViewFeedbackUI extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private CardLayout cardLayout;
    private JPanel mainContent;
    
    private JPanel techFeed;
    private JPanel custFeed;

    private Map<String, String> customerCache = new HashMap<>();

    public ViewFeedbackUI() {
        loadCustomerCache();
        buildUI();
    }

    private void buildUI() {
        setOpaque(false); 
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton techBtn = new ModernButton("Technician Logs");
        techBtn.setPreferredSize(new Dimension(240, 45));
        
        JButton custBtn = new ModernButton("Customer Reviews");
        custBtn.setPreferredSize(new Dimension(240, 45));
        
        header.add(techBtn);
        header.add(custBtn);
        add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setOpaque(false);
        
        techFeed = new JPanel();
        techFeed.setLayout(new BoxLayout(techFeed, BoxLayout.Y_AXIS));
        techFeed.setOpaque(false);
        techFeed.setBorder(new EmptyBorder(15, 0, 50, 0)); 
        
        custFeed = new JPanel();
        custFeed.setLayout(new BoxLayout(custFeed, BoxLayout.Y_AXIS));
        custFeed.setOpaque(false);
        custFeed.setBorder(new EmptyBorder(15, 0, 50, 0));

        mainContent.add(createModernScrollPane(techFeed), "TECH");
        mainContent.add(createModernScrollPane(custFeed), "CUST");
        
        add(mainContent, BorderLayout.CENTER);

        techBtn.addActionListener(e -> cardLayout.show(mainContent, "TECH"));
        custBtn.addActionListener(e -> cardLayout.show(mainContent, "CUST"));

        populateTechnicianFeed();
        populateCustomerFeed();
    }
    

    private void populateTechnicianFeed() {
        File file = new File("feedback.txt");

        if (!file.exists()) {
            techFeed.add(createEmptyState("No technician feedback logs found."));
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(",", -1); 
                
                if (data.length >= 7) {
                    String apptId = data[0].trim();
                    String techId = data[1].trim();
                    String notes  = data[2].trim();
                    String before = data[3].trim();
                    String after  = data[4].trim();
                    String rec    = data[5].trim();
                    String date   = data[6].trim();

                    techFeed.add(Box.createRigidArea(new Dimension(0, 15)));
                    techFeed.add(createTechCard(apptId, techId, notes, before, after, rec, date));
                    count++;
                }
            }
            if (count == 0) techFeed.add(createEmptyState("No valid technician feedback found."));
            techFeed.add(Box.createVerticalGlue());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void populateCustomerFeed() {
        File file = new File("comment.txt");
        
        if (!file.exists()) {
            custFeed.add(createEmptyState("No customer comments available."));
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 4) continue;
                
                String apptId = data[0].trim();
                String custId = data[1].trim();
                String role   = data[2].trim();
                
                String comment = data[3];
                String rating = "-";
                String date = "-";

                if (data.length >= 6) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 3; i < data.length - 2; i++) {
                        sb.append(data[i]);
                        if (i < data.length - 3) sb.append(",");
                    }
                    comment = sb.toString();
                    rating = data[data.length - 2].trim();
                    date = data[data.length - 1].trim();
                } else if (data.length == 5) {
                    rating = data[4].trim();
                }

                String custName = customerCache.getOrDefault(custId, custId);
                
                custFeed.add(Box.createRigidArea(new Dimension(0, 15)));
                custFeed.add(createCustCard(apptId, custName, role, comment, rating, date));
                count++;
            }
            if (count == 0) custFeed.add(createEmptyState("No valid customer comments found."));
            custFeed.add(Box.createVerticalGlue());
        } catch (Exception e) { e.printStackTrace(); }
    }


    private JPanel createTechCard(String apptId, String techId, String notes, String before, String after, String rec, String date) {
        JPanel card = new RoundedPanel(20, new Color(45, 45, 65)) {
            private static final long serialVersionUID = 1L;
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(800, getPreferredSize().height);
            }
        };
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("<html><b style='font-size:15px; color:#ffffff;'>Appointment: " + apptId + "</b> &nbsp; <span style='color:#a0a0b9;'>| &nbsp; Technician ID: " + techId + "</span></html>");
        JLabel dateLbl = new JLabel("<html><span style='color:#a0a0b9; font-size:12px;'>" + date + "</span></html>");
        header.add(title, BorderLayout.WEST);
        header.add(dateLbl, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        String html = "<html><div style='width: 700px; font-family: Segoe UI, sans-serif; font-size: 13px; color: #d0d0e0; margin-top: 15px;'>"
            + "<b style='color:#00d2ff;'>Service Notes:</b><br>" + formatNewLines(notes) + "<br><br>"
            + "<b style='color:#ffb400;'>Before Condition:</b><br>" + formatNewLines(before) + "<br><br>"
            + "<b style='color:#00c878;'>After Service:</b><br>" + formatNewLines(after) + "<br><br>"
            + "<b style='color:#ff4646;'>Recommendations:</b><br>" + formatNewLines(rec)
            + "</div></html>";
            
        JLabel body = new JLabel(html);
        card.add(body, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createCustCard(String apptId, String custName, String role, String comment, String rating, String date) {
        JPanel card = new RoundedPanel(20, new Color(45, 45, 65)) {
            private static final long serialVersionUID = 1L;
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(800, getPreferredSize().height);
            }
        };
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        String stars = "";
        try {
            int r = Integer.parseInt(rating);
            for(int i=0; i<r; i++) stars += "&#9733;"; 
            for(int i=r; i<5; i++) stars += "&#9734;"; 
        } catch(Exception e) { stars = rating; }

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("<html><b style='font-size:15px; color:#ffffff;'>" + custName + "</b> &nbsp; <span style='color:#a0a0b9;'>| &nbsp; Appt: " + apptId + " &nbsp; | &nbsp; To: " + role + "</span></html>");
        JLabel dateLbl = new JLabel("<html><span style='color:#ffb400; font-size:15px;'>" + stars + "</span> &nbsp;&nbsp;&nbsp; <span style='color:#a0a0b9; font-size:12px;'>" + date + "</span></html>");
        header.add(title, BorderLayout.WEST);
        header.add(dateLbl, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        String html = "<html><div style='width: 700px; font-family: Segoe UI, sans-serif; font-size: 13px; color: #d0d0e0; margin-top: 15px;'>"
            + formatNewLines(comment) + "</div></html>";
            
        JLabel body = new JLabel(html);
        card.add(body, BorderLayout.CENTER);
        
        return card;
    }


    private JScrollPane createModernScrollPane(JPanel content) {
        JScrollPane sp = new JScrollPane(content);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return sp;
    }
    
    private JPanel createEmptyState(String message) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(800, 100));
        JLabel l = new JLabel(message);
        l.setForeground(new Color(150, 150, 170));
        l.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        p.add(l);
        return p;
    }

    private String formatNewLines(String text) {
        if (text == null || text.isEmpty()) return "-";
        return text.replace("<<BR>>", "<br>")
                   .replace("<<br>>", "<br>")
                   .replace("<br>", "<br>")
                   .replace("\\n", "<br>");
    }

    private void loadCustomerCache() {
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 2) customerCache.put(d[0].trim(), d[1].trim());
            }
        } catch (Exception e) {}
    }
}