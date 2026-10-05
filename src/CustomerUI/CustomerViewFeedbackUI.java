package CustomerUI;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import UI.Components.*;

public class CustomerViewFeedbackUI extends JPanel {
    private static final long serialVersionUID = 1L;
    private String customerID;
    private JPanel feedbackPanel;

    public CustomerViewFeedbackUI(String id) {
        this.customerID = id;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(950, 500));
        card.setLayout(null);

        JLabel title = new JLabel("TECHNICIAN FEEDBACKS", SwingConstants.CENTER);
        title.setBounds(0, 20, 950, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
        feedbackPanel.setBackground(new Color(35, 35, 50));
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JScrollPane scroll = new JScrollPane(feedbackPanel);
        scroll.setBounds(20, 70, 910, 410);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(35, 35, 50));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        card.add(scroll);

        loadFeedbacks();
        add(card);
    }

    private void loadFeedbacks() {
        feedbackPanel.removeAll();
        boolean hasFeedback = false;

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[1].trim().equals(customerID)) {
                    String appID = data[0].trim();
                    String techID = data[8].trim();
                    String[] feedback = getFeedback(appID, techID);
                    if (feedback != null) {
                        feedbackPanel.add(createFeedbackCard(appID, techID, feedback));
                        feedbackPanel.add(Box.createVerticalStrut(12));
                        hasFeedback = true;
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (!hasFeedback) {
            JLabel none = new JLabel("No feedbacks found for your appointments.", SwingConstants.CENTER);
            none.setForeground(new Color(150, 150, 170));
            none.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            none.setAlignmentX(Component.CENTER_ALIGNMENT);
            feedbackPanel.add(Box.createVerticalStrut(150));
            feedbackPanel.add(none);
        }
        
        feedbackPanel.add(Box.createVerticalGlue());
        feedbackPanel.revalidate();
        feedbackPanel.repaint();
        
        SwingUtilities.invokeLater(() -> {
            JScrollPane scroll = (JScrollPane) feedbackPanel.getParent().getParent();
            scroll.getVerticalScrollBar().setValue(0);
        });
    }

    private JPanel createFeedbackCard(String appID, String techID, String[] parts) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(28, 28, 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 270));
        wrapper.setPreferredSize(new Dimension(880, 270));
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel headerLeft = new JPanel();
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.setOpaque(false);

        JLabel apptLabel = new JLabel("Appointment  " + appID);
        apptLabel.setForeground(Color.WHITE);
        apptLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerLeft.add(apptLabel);

        JLabel techLabel = new JLabel("Technician: " + techID);
        techLabel.setForeground(new Color(130, 130, 160));
        techLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerLeft.add(techLabel);

        String date = parts.length >= 5 ? parts[4].trim() : "";
        JLabel dateLabel = new JLabel(date);
        dateLabel.setForeground(new Color(30, 111, 217));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        header.add(headerLeft, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(55, 55, 80));

        JPanel grid = new JPanel(new GridLayout(2, 2, 6, 6));
        grid.setOpaque(false);

        String[] titles = {"SERVICE NOTES", "BEFORE CONDITION", "AFTER SERVICE", "RECOMMENDATION"};
        Color[] colors = {
            new Color(255, 180, 0),
            new Color(239, 68, 68),
            new Color(34, 197, 94),
            new Color(129, 140, 248)
        };

        for (int i = 0; i < 4; i++) {
            JPanel section = new JPanel(new BorderLayout(0, 4)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(20, 20, 35));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
            };
            section.setOpaque(false);
            section.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            JLabel secTitle = new JLabel(titles[i]);
            secTitle.setForeground(colors[i]);
            secTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
            section.add(secTitle, BorderLayout.NORTH);

            String content = (i < parts.length && parts[i] != null) ? parts[i].trim() : "-";
            JTextArea secBody = new JTextArea(content);
            secBody.setEditable(false);
            secBody.setOpaque(false);
            secBody.setLineWrap(true);
            secBody.setWrapStyleWord(true);
            secBody.setForeground(new Color(176, 176, 200));
            secBody.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            section.add(secBody, BorderLayout.CENTER);

            grid.add(section);
        }

        JPanel partsSection = new JPanel(new BorderLayout(0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 20, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        partsSection.setOpaque(false);
        partsSection.setPreferredSize(new Dimension(200, 0)); 
        partsSection.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel partsTitle = new JLabel("PARTS USED");
        partsTitle.setForeground(new Color(56, 189, 248));
        partsTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        partsSection.add(partsTitle, BorderLayout.NORTH);

        JTextArea partsBody = new JTextArea(getPartsUsed(appID));
        partsBody.setEditable(false);
        partsBody.setOpaque(false);
        partsBody.setLineWrap(true);
        partsBody.setWrapStyleWord(true);
        partsBody.setForeground(new Color(176, 176, 200));
        partsBody.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        partsSection.add(partsBody, BorderLayout.CENTER);

        JPanel mainContent = new JPanel(new BorderLayout(6, 0));
        mainContent.setOpaque(false);
        mainContent.add(grid, BorderLayout.CENTER);
        mainContent.add(partsSection, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(header);
        content.add(sep);
        content.add(Box.createVerticalStrut(6));
        content.add(mainContent); 

        wrapper.add(content, BorderLayout.CENTER);
        return wrapper;
    }

    private String getPartsUsed(String appID) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].trim().equals(appID)) {
                    sb.append("• ")
                      .append(data[1].trim())
                      .append("  x")
                      .append(data[2].trim())
                      .append("\n");
                }
            }
        } catch (Exception e) {}
        return sb.length() > 0 ? sb.toString().trim() : "No parts recorded.";
    }

    private String[] getFeedback(String appID, String techID) {
        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 7);
                if (data.length >= 7 && data[0].trim().equals(appID) && data[1].trim().equals(techID)) {
                    String[] result = new String[5];
                    result[0] = restore(data[2]);
                    result[1] = restore(data[3]);
                    result[2] = restore(data[4]);
                    result[3] = restore(data[5]);
                    result[4] = data[6];
                    return result;
                }
            }
        } catch (Exception e) {}
        return null;
    }

    private String restore(String text) {
        return text.replace("<<BR>>", "\n");
    }
}