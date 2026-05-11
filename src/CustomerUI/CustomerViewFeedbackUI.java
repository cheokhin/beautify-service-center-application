package CustomerUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import UI.Components.*; 

public class CustomerViewFeedbackUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String customerID;
    private JTextArea textArea;

    public CustomerViewFeedbackUI(String id) {
        this.customerID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(650, 420)); 
        card.setLayout(null);

        JLabel title = new JLabel("TECHNICIAN FEEDBACKS", SwingConstants.CENTER);
        title.setBounds(0, 20, 650, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(20, 20, 30));
        textArea.setForeground(new Color(0, 200, 255));
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane sp = new JScrollPane(textArea);
        sp.setBounds(40, 80, 570, 290);
        sp.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        card.add(sp);

        loadFeedbacks();
        add(card);
    }

    private void loadFeedbacks() {
        textArea.setText("");
        boolean hasFeedback = false;

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[1].equals(customerID)) {
                    String appID = data[0];
                    String techID = data[8];
                    String feedback = getFeedback(appID, techID);
                    if (feedback != null) {
                        textArea.append("Appointment : " + appID + "\n");
                        textArea.append("Technician  : " + techID + "\n");
                        textArea.append("Feedback    : " + feedback + "\n");
                        textArea.append("--------------------------------------------------\n");
                        hasFeedback = true;
                    }
                }
            }
            if (!hasFeedback) textArea.append("No feedbacks found for your appointments.");
            textArea.setCaretPosition(0);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String getFeedback(String appID, String techID) {
        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(appID) && data[1].equals(techID)) return data[2];
            }
        } catch (Exception e) {}
        return null;
    }
}