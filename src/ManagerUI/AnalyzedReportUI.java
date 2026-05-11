package ManagerUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.*;

import UI.Components.*;

public class AnalyzedReportUI extends JPanel {

    private static final long serialVersionUID = 1L;

    public AnalyzedReportUI() {
        buildUI();
    }

    private void buildUI() {
        setOpaque(false); 
        setLayout(new BorderLayout());

        JPanel bg = new JPanel();
        bg.setOpaque(false);
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(650, 520));
        card.setLayout(null);
        bg.add(card);

        // ================= TITLE =================
        JLabel title = new JLabel("ANALYZED REPORT", SwingConstants.CENTER);
        title.setBounds(175, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        // ================= TEXT AREA =================
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setBackground(new Color(20, 20, 30));
        reportArea.setForeground(new Color(0, 255, 150));
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBounds(30, 65, 590, 380);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        scrollPane.getVerticalScrollBar().setBackground(new Color(35, 35, 50));
        card.add(scrollPane);

        // ================= ACTIONS =================
        reportArea.setText(generateReport());
        reportArea.setCaretPosition(0);
    }

    // ==========================================
    // MAIN REPORT FUNCTION
    // ==========================================
    private String generateReport() {
        int total = 0, completed = 0, pending = 0, cancelled = 0;
        HashMap<String, Integer> serviceCount = new HashMap<>();
        HashMap<String, Integer> techAssigned = new HashMap<>();
        HashMap<String, Integer> techCompleted = new HashMap<>();
        HashMap<String, Integer> techRatingTotal = new HashMap<>();
        HashMap<String, Integer> techRatingCount = new HashMap<>();
        HashMap<String, Double> revenueByService = new HashMap<>();
        int positive = 0, neutral = 0, negative = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 9) continue;
                total++;
                String service = d[3].trim();
                String jobStatus = d[5].trim();
                String techID = d[8].trim();

                serviceCount.put(service, serviceCount.getOrDefault(service, 0) + 1);
                if (jobStatus.equalsIgnoreCase("done")) completed++;
                else if (jobStatus.equalsIgnoreCase("pending")) pending++;
                else if (jobStatus.equalsIgnoreCase("cancelled")) cancelled++;

                techAssigned.put(techID, techAssigned.getOrDefault(techID, 0) + 1);
                if (jobStatus.equalsIgnoreCase("done")) techCompleted.put(techID, techCompleted.getOrDefault(techID, 0) + 1);
            }
        } catch (Exception e) { }

        try (BufferedReader br = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 5) continue;
                String service = getServiceType(d[2].trim());
                if (service != null) revenueByService.put(service, revenueByService.getOrDefault(service, 0.0) + Double.parseDouble(d[4].trim()));
            }
        } catch (Exception e) { }

        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 5) continue;
                int rating = Integer.parseInt(d[4].trim());
                if (rating > 3) positive++;
                else if (rating == 3) neutral++;
                else negative++;
                String techID = getTechnicianFromAppointment(d[0].trim());
                if (techID != null) {
                    techRatingTotal.put(techID, techRatingTotal.getOrDefault(techID, 0) + rating);
                    techRatingCount.put(techID, techRatingCount.getOrDefault(techID, 0) + 1);
                }
            }
        } catch (Exception e) { }

        StringBuilder sb = new StringBuilder();
        sb.append("===============================================================\n");
        sb.append("                      APPOINTMENT REPORT\n");
        sb.append("===============================================================\n");
        sb.append(String.format("%-20s : %d\n", "Total", total));
        sb.append(String.format("%-20s : %d\n", "Completed", completed));
        sb.append(String.format("%-20s : %d\n", "Pending", pending));
        sb.append(String.format("%-20s : %d\n", "Cancelled", cancelled));
        sb.append("\nService Breakdown:\n");
        for (String s : serviceCount.keySet()) sb.append(String.format(" - %-15s : %d\n", s, serviceCount.get(s)));

        sb.append("\n===============================================================\n");
        sb.append("                    TECHNICIAN PERFORMANCE\n");
        sb.append("===============================================================\n");
        for (String tech : techAssigned.keySet()) {
            double avgRating = techRatingCount.containsKey(tech) ? (double) techRatingTotal.get(tech) / techRatingCount.get(tech) : 0;
            sb.append(String.format("Technician: %s\nAssigned: %d | Completed: %d | Avg Rating: %.2f\n---------------------------------------------\n",
                    tech, techAssigned.getOrDefault(tech, 0), techCompleted.getOrDefault(tech, 0), avgRating));
        }

        sb.append("\n===============================================================\n");
        sb.append("                      FINANCIAL REPORT\n");
        sb.append("===============================================================\n");
        double totalRevenue = 0;
        for (String s : revenueByService.keySet()) {
            totalRevenue += revenueByService.get(s);
            sb.append(String.format("%-15s : RM %.2f\n", s, revenueByService.get(s)));
        }
        sb.append(String.format("TOTAL REVENUE   : RM %.2f\n", totalRevenue));

        sb.append("\n===============================================================\n");
        sb.append("                 CUSTOMER FEEDBACK ANALYSIS\n");
        sb.append("===============================================================\n");
        sb.append(String.format("%-20s : %d\n%-20s : %d\n%-20s : %d\n", "Positive (>3)", positive, "Neutral  (=3)", neutral, "Negative (<3)", negative));

        return sb.toString();
    }

    private String getServiceType(String appID) {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 4 && d[0].trim().equals(appID)) return d[3].trim();
            }
        } catch (Exception e) { }
        return null;
    }

    private String getTechnicianFromAppointment(String appID) {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 9 && d[0].trim().equals(appID)) return d[8].trim();
            }
        } catch (Exception e) { }
        return null;
    }
}