package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import UI.Components.*; 

public class ProvideFeedbackUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String technicianID;
    private JComboBox<String> appointmentBox;
    private JTextArea txtFeedback;

    public ProvideFeedbackUI(String id) {
        this.technicianID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(420, 320)); 
        card.setLayout(null);

        JLabel lblTitle = new JLabel("PROVIDE FEEDBACK", SwingConstants.CENTER);
        lblTitle.setBounds(0, 20, 420, 35);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(lblTitle);

        JLabel lblApp = new JLabel("Appointment:");
        lblApp.setBounds(40, 80, 120, 30);
        lblApp.setForeground(Color.WHITE);
        lblApp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(lblApp);

        appointmentBox = new JComboBox<>();
        appointmentBox.setBounds(160, 80, 200, 32);
        appointmentBox.setBackground(Color.WHITE);
        card.add(appointmentBox);

        JLabel lblFeedback = new JLabel("Feedback:");
        lblFeedback.setBounds(40, 130, 120, 30);
        lblFeedback.setForeground(Color.WHITE);
        lblFeedback.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(lblFeedback);

        txtFeedback = new JTextArea();
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);
        txtFeedback.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(txtFeedback);
        scroll.setBounds(160, 130, 200, 100);
        card.add(scroll);

        // Centered Button
        JButton saveBtn = new ModernButton(" Save");
        saveBtn.setBounds(145, 250, 130, 42);
        card.add(saveBtn);

        loadAppointments();
        appointmentBox.addActionListener(e -> loadExistingFeedback());
        saveBtn.addActionListener(e -> saveFeedback());

        add(card);
    }

    private void loadAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[8].equals(technicianID) && data[5].equalsIgnoreCase("Done")) {
                    appointmentBox.addItem(data[0]);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadExistingFeedback() {
        String selectedApp = (String) appointmentBox.getSelectedItem();
        txtFeedback.setText("");
        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(selectedApp) && data[1].equals(technicianID)) {
                    txtFeedback.setText(data[2]);
                    return;
                }
            }
        } catch (Exception e) {}
    }

    private void saveFeedback() {
        String selectedApp = (String) appointmentBox.getSelectedItem();
        String feedback = txtFeedback.getText().trim();
        if (selectedApp == null || feedback.isEmpty()) { new ModernDialog("Feedback cannot be empty!"); return; }

        String today = LocalDate.now().toString();
        ArrayList<String> list = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(selectedApp) && data[1].equals(technicianID)) {
                    line = selectedApp + "," + technicianID + "," + feedback + "," + today;
                    found = true;
                }
                list.add(line);
            }
        } catch (Exception e) {}

        if (!found) list.add(selectedApp + "," + technicianID + "," + feedback + "," + today);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("feedback.txt"))) {
            for (String s : list) { bw.write(s); bw.newLine(); }
            new ModernDialog("Feedback Saved!");
        } catch (Exception e) { e.printStackTrace(); }
    }
}