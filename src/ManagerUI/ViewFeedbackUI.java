package ManagerUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

import UI.Components.*;

public class ViewFeedbackUI extends JPanel {

    private static final long serialVersionUID = 1L;

    public ViewFeedbackUI() {
        buildUI();
    }

    private void buildUI() {
        setOpaque(false); // 🔥 Transparent background
        setLayout(new BorderLayout());

        JPanel bg = new JPanel();
        bg.setOpaque(false);
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(600, 460));
        card.setLayout(null);
        bg.add(card);

        // ================= TITLE =================
        JLabel title = new JLabel("ALL FEEDBACKS & COMMENTS", SwingConstants.CENTER);
        title.setBounds(150, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        // ================= TEXT AREA =================
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(20, 20, 30));
        textArea.setForeground(new Color(220, 230, 255));
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(30, 65, 540, 310);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        scrollPane.getVerticalScrollBar().setBackground(new Color(35, 35, 50));
        card.add(scrollPane);

        // ================= ACTIONS =================
        loadTechnicianFeedback(textArea);
        loadCustomerComments(textArea);
    }

    // ==========================================
    // 🔥 DATA LOGIC (Unchanged functionally)
    // ==========================================

    private void loadTechnicianFeedback(JTextArea textArea) {
        File file = new File("feedback.txt");
        textArea.append("=== Technician Feedback ===\n\n");

        if (!file.exists()) {
            textArea.append("No technician feedback available.\n\n");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 3) continue;

                textArea.append("Appointment : " + data[0] +
                        "\nTechnician  : " + data[1] +
                        "\nFeedback    : " + data[2] +
                        "\nDate        : " + ((data.length >= 4) ? data[3] : "-") +
                        "\n-----------------------------\n");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadCustomerComments(JTextArea textArea) {
        File file = new File("comment.txt");
        textArea.append("\n=== Technician Comments ===\n\n");

        if (!file.exists()) {
            textArea.append("No customer comments available.\n");
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 4) continue;

                if (data[2].equalsIgnoreCase("Technician")) {
                    String techID = getTechnicianFromAppointment(data[0]);
                    textArea.append("Appointment : " + data[0] +
                            "\nCustomer    : " + data[1] +
                            "\nTo          : Technician (" + techID + ")" +
                            "\nComment     : " + data[3] +
                            "\nRating      : " + ((data.length >= 5) ? data[4] : "-") +
                            "\nDate        : " + ((data.length >= 6) ? data[5] : "-") +
                            "\n-----------------------------\n");
                }
            }
            br.close();

            textArea.append("\n=== Counter Staff Comments ===\n\n");
            BufferedReader br2 = new BufferedReader(new FileReader(file));
            while ((line = br2.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 4) continue;

                if (data[2].equalsIgnoreCase("CounterStaff")) {
                    textArea.append("Appointment : " + data[0] +
                            "\nCustomer    : " + data[1] +
                            "\nTo          : Counter Staff" +
                            "\nComment     : " + data[3] +
                            "\nRating      : " + ((data.length >= 5) ? data[4] : "-") +
                            "\nDate        : " + ((data.length >= 6) ? data[5] : "-") +
                            "\n-----------------------------\n");
                }
            }
            br2.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String getTechnicianFromAppointment(String appID) {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 9 && d[0].trim().equals(appID)) return d[8].trim();
            }
        } catch (Exception e) { }
        return "-";
    }
}