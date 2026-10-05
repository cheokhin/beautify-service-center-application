package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import UI.Components.*; 

public class ProvideFeedbackUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String technicianID;
    private JComboBox<String> appointmentBox;
    private JTextArea txtServiceNotes;
    private JTextArea txtBefore;
    private JTextArea txtAfter;
    private JTextArea txtRecommendation;

    private final String NOTE_HINT   = "Enter work details...\ne.g.,\n- Replaced brake pads\n- Flushed engine oil";
    private final String BEFORE_HINT = "Describe initial state...\ne.g.,\n- Brake spoiled\n- oil broken\n- tyre spoiled";
    private final String AFTER_HINT  = "Describe final result...\ne.g.,\n- Brake fixed\n- oil changed\n- tyre replaced";
    private final String RECO_HINT   = "Enter recommendation...";

    public ProvideFeedbackUI(String id) {
        this.technicianID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(820, 520));
        card.setLayout(null);

        JLabel lblTitle = new JLabel("PROVIDE DETAILED SERVICE REPORT", SwingConstants.CENTER);
        lblTitle.setBounds(0, 25, 820, 35);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(lblTitle);

        JLabel lblApp = new JLabel("Select Appointment:");
        lblApp.setBounds(225, 80, 150, 30);
        lblApp.setForeground(Color.WHITE);
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(lblApp);

        appointmentBox = new JComboBox<>();
        appointmentBox.setBounds(375, 80, 220, 35);
        appointmentBox.setBackground(new Color(28, 28, 45));
        appointmentBox.setForeground(new Color(200, 216, 240));
        appointmentBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        appointmentBox.setBorder(BorderFactory.createLineBorder(new Color(255, 180, 0)));

        appointmentBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▼");
                btn.setBackground(new Color(28, 28, 45));
                btn.setForeground(new Color(200, 216, 240));
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                return btn;
            }
        });
        card.add(appointmentBox);

        txtServiceNotes   = createLabeledArea(card, "Service Notes",    40,  140, 230, 285, NOTE_HINT);
        txtBefore         = createLabeledArea(card, "Before Condition", 295, 140, 230, 138, BEFORE_HINT);
        txtAfter          = createLabeledArea(card, "After Service",    295, 287, 230, 138, AFTER_HINT);
        txtRecommendation = createLabeledArea(card, "Recommendation",   550, 140, 230, 285, RECO_HINT);

        JButton saveBtn = new ModernButton("Save Report");
        saveBtn.setBounds(335, 450, 150, 42);
        card.add(saveBtn);

        loadAppointments();
        appointmentBox.addActionListener(e -> loadExistingFeedback());
        saveBtn.addActionListener(e -> saveFeedback());

        add(card);
    }

    private JTextArea createLabeledArea(JPanel parent, String title, int x, int y, int w, int h, String placeholder) {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setBounds(x, y, w, 25);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        parent.add(label);

        Color bgColor   = new Color(28, 28, 45);
        Color fgColor   = new Color(200, 216, 240);
        Color hintColor = new Color(160, 160, 180);

        JTextArea area = new JTextArea(placeholder);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setBackground(bgColor);
        area.setForeground(hintColor);
        area.setCaretColor(fgColor);
        area.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(new Color(200, 216, 240));
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText(placeholder);
                    area.setForeground(hintColor);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(x, y + 30, w, h - 30);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 180, 0), 1));
        scroll.setBackground(bgColor);
        scroll.getViewport().setBackground(bgColor);
        parent.add(scroll);

        return area;
    }

    private void loadAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[8].trim().equals(technicianID) && data[5].trim().equalsIgnoreCase("Done")) {
                    appointmentBox.addItem(data[0].trim());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        appointmentBox.setSelectedIndex(-1);
    }

    private void loadExistingFeedback() {
        String selectedApp = (String) appointmentBox.getSelectedItem();
        resetArea(txtServiceNotes, NOTE_HINT);
        resetArea(txtBefore, BEFORE_HINT);
        resetArea(txtAfter, AFTER_HINT);
        resetArea(txtRecommendation, RECO_HINT);
        if (selectedApp == null || selectedApp.isEmpty()) return;

        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 7);
                if (data.length >= 7 && data[0].trim().equals(selectedApp) && data[1].trim().equals(technicianID)) {
                    setAreaText(txtServiceNotes,   restore(data[2]));
                    setAreaText(txtBefore,         restore(data[3]));
                    setAreaText(txtAfter,          restore(data[4]));
                    setAreaText(txtRecommendation, restore(data[5]));
                    return;
                }
            }
        } catch (Exception e) {}
    }

    private void resetArea(JTextArea area, String placeholder) {
        area.setText(placeholder);
        area.setForeground(new Color(160, 160, 180));
    }

    private void setAreaText(JTextArea area, String text) {
        if (text != null && !text.trim().isEmpty()) {
            area.setText(text.trim());
            area.setForeground(new Color(200, 216, 240));
        }
    }

    private String sanitize(String text) {
        return text.replace("\n", "<<BR>>");
    }

    private String restore(String text) {
        return text.replace("<<BR>>", "\n");
    }

    private void saveFeedback() {
    	String selectedApp = (String) appointmentBox.getSelectedItem();
    	if (selectedApp == null || selectedApp.isEmpty()) {
    	    new ModernDialog("Please select an appointment!");
    	    return;
    	}

        String note   = sanitize(getActualText(txtServiceNotes, NOTE_HINT));
        String before = sanitize(getActualText(txtBefore, BEFORE_HINT));
        String after  = sanitize(getActualText(txtAfter, AFTER_HINT));
        String reco   = sanitize(getActualText(txtRecommendation, RECO_HINT));
        String today  = LocalDate.now().toString();

        String newLine = selectedApp + "," + technicianID + "," + note + "," + before + "," + after + "," + reco + "," + today;

        ArrayList<String> list = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 7);
                if (data.length >= 2 && data[0].trim().equals(selectedApp) && data[1].trim().equals(technicianID)) {
                    list.add(newLine); 
                    found = true;
                } else {
                    list.add(line);
                }
            }
        } catch (Exception e) {}

        if (!found) list.add(newLine); 

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("feedback.txt"))) {
            for (String s : list) { bw.write(s); bw.newLine(); }
            new ModernDialog("Service Report Saved Successfully!");
            SystemLogger.log(this.technicianID, "Technician", "Submitted technical service report for Appointment: " + selectedApp);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String getActualText(JTextArea area, String placeholder) {
        String text = area.getText();
        return text.equals(placeholder) ? "" : text.trim();
    }
}