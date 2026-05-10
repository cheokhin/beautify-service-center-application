package ManagerUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import UI.MainUI;
import java.awt.*;
import java.io.*;
import java.util.*;

import UI.Components.*;

public class AnalyzedReportUI extends JPanel {

    private static final long serialVersionUID = 1L;

    public AnalyzedReportUI() {
        buildUI();

        // 🔥 SPA Routing
        MainUI.instance.mainContainer.add(this, "ANALYZED_REPORT");
        MainUI.instance.showPage("ANALYZED_REPORT");
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel();
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
        reportArea.setForeground(new Color(0, 255, 150)); // Bright green/cyan for terminal vibe
        
        // 🔥 关键：让 report 对齐好看 (Kept Monospaced)
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBounds(30, 65, 590, 380);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        scrollPane.getVerticalScrollBar().setBackground(new Color(35, 35, 50));
        card.add(scrollPane);

        // ================= BUTTONS =================
        JButton backBtn = new ModernButton("Return");
        backBtn.setBounds(265, 460, 120, 35);
        card.add(backBtn);

        // ================= ACTIONS =================
        reportArea.setText(generateReport());

        backBtn.addActionListener(e -> {
            MainUI.instance.showPage("MANAGER_DASHBOARD");
            MainUI.instance.mainContainer.remove(this); // Clean up memory
        });
    }

    // ==========================================
    // 🔥 MAIN REPORT FUNCTION (Logic Unchanged)
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

        // ===== READ APPOINTMENT =====
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

                if (jobStatus.equalsIgnoreCase("done"))
                    completed++;
                else if (jobStatus.equalsIgnoreCase("pending"))
                    pending++;
                else if (jobStatus.equalsIgnoreCase("cancelled"))
                    cancelled++;

                techAssigned.put(techID, techAssigned.getOrDefault(techID, 0) + 1);

                if (jobStatus.equalsIgnoreCase("done")) {
                    techCompleted.put(techID, techCompleted.getOrDefault(techID, 0) + 1);
                }
            }
        } catch (Exception e) { }

        // ===== READ RECEIPT =====
        try (BufferedReader br = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] d = line.split(",");
                if (d.length < 5) continue;

                String appID = d[2].trim();
                double amount = Double.parseDouble(d[4].trim());

                String service = getServiceType(appID);

                if (service != null) {
                    revenueByService.put(service, revenueByService.getOrDefault(service, 0.0) + amount);
                }
            }
        } catch (Exception e) { }

        // ===== READ COMMENT =====
        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] d = line.split(",");
                if (d.length < 5) continue;

                String appID = d[0].trim();
                int rating = Integer.parseInt(d[4].trim());

                if (rating > 3) positive++;
                else if (rating == 3) neutral++;
                else negative++;

                String techID = getTechnicianFromAppointment(appID);

                if (techID != null) {
                    techRatingTotal.put(techID, techRatingTotal.getOrDefault(techID, 0) + rating);
                    techRatingCount.put(techID, techRatingCount.getOrDefault(techID, 0) + 1);
                }
            }
        } catch (Exception e) { }

        // ===== BUILD REPORT =====
        StringBuilder sb = new StringBuilder();

        sb.append("===============================================================\n");
        sb.append("                      APPOINTMENT REPORT\n");
        sb.append("===============================================================\n");

        sb.append(String.format("%-20s : %d\n", "Total", total));
        sb.append(String.format("%-20s : %d\n", "Completed", completed));
        sb.append(String.format("%-20s : %d\n", "Pending", pending));
        sb.append(String.format("%-20s : %d\n", "Cancelled", cancelled));

        sb.append("\nService Breakdown:\n");
        for (String s : serviceCount.keySet()) {
            sb.append(String.format(" - %-15s : %d\n", s, serviceCount.get(s)));
        }

        sb.append("\n===============================================================\n");
        sb.append("                    TECHNICIAN PERFORMANCE\n");
        sb.append("===============================================================\n");

        for (String tech : techAssigned.keySet()) {
            int assigned = techAssigned.getOrDefault(tech, 0);
            int done = techCompleted.getOrDefault(tech, 0);
            double avgRating = 0;
            if (techRatingCount.containsKey(tech)) {
                avgRating = (double) techRatingTotal.get(tech) / techRatingCount.get(tech);
            }
            sb.append(String.format("Technician: %s\nAssigned: %d | Completed: %d | Avg Rating: %.2f\n",
                    tech, assigned, done, avgRating));
            sb.append("---------------------------------------------\n");
        }

        sb.append("\n===============================================================\n");
        sb.append("                      FINANCIAL REPORT\n");
        sb.append("===============================================================\n");

        double totalRevenue = 0;
        for (String s : revenueByService.keySet()) {
            double val = revenueByService.get(s);
            totalRevenue += val;
            sb.append(String.format("%-15s : RM %.2f\n", s, val));
        }
        sb.append(String.format("TOTAL REVENUE   : RM %.2f\n", totalRevenue));

        sb.append("\n===============================================================\n");
        sb.append("                 CUSTOMER FEEDBACK ANALYSIS\n");
        sb.append("===============================================================\n");

        sb.append(String.format("%-20s : %d\n", "Positive (>3)", positive));
        sb.append(String.format("%-20s : %d\n", "Neutral  (=3)", neutral));
        sb.append(String.format("%-20s : %d\n", "Negative (<3)", negative));

        return sb.toString();
    }

    // ===== HELPER 1 =====
    private String getServiceType(String appID) {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 4 && d[0].trim().equals(appID)) {
                    return d[3].trim();
                }
            }
        } catch (Exception e) { }
        return null;
    }

    // ===== HELPER 2 =====
    private String getTechnicianFromAppointment(String appID) {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 9 && d[0].trim().equals(appID)) {
                    return d[8].trim();
                }
            }
        } catch (Exception e) { }
        return null;
    } 
}