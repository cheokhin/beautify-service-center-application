package TechnicianUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import UI.Components.*; 

public class UpdateJobStatusUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField appField;
    private JComboBox<String> statusBox;
    private String technicianID;

    public UpdateJobStatusUI(String id) {
        this.technicianID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(420, 260)); 
        card.setLayout(null);

        JLabel title = new JLabel("UPDATE JOB STATUS", SwingConstants.CENTER);
        title.setBounds(0, 20, 420, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title);

        JLabel appLabel = new JLabel("Appointment ID:");
        appLabel.setBounds(40, 80, 120, 25);
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(appLabel);

        appField = new JTextField();
        appField.setBounds(170, 80, 180, 30);
        styleField(appField);
        card.add(appField);

        JLabel statusLabel = new JLabel("Job Status:");
        statusLabel.setBounds(40, 130, 120, 25);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLabel);

        statusBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Done"});
        statusBox.setBounds(170, 130, 180, 30);
        statusBox.setBackground(Color.WHITE);
        card.add(statusBox);

        // Centered Button
        JButton updateBtn = new ModernButton(" Update");
        updateBtn.setBounds(145, 190, 130, 38);
        card.add(updateBtn);

        updateBtn.addActionListener(e -> updateStatus());
        add(card);
    }

    private void styleField(JTextField tf) {
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
        tf.setCaretColor(Color.BLACK);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void updateStatus() {
        if (appField.getText().trim().isEmpty()) {
            new ModernDialog("Please enter Appointment ID!");
            return;
        }

        ArrayList<String> list = new ArrayList<>();
        boolean found = false;
        String targetID = appField.getText().trim();

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("AppointmentID")) { list.add(line); continue; }
                String[] data = line.split(",");
                if (data[0].equals(targetID) && data[8].equals(technicianID)) {
                    data[5] = statusBox.getSelectedItem().toString();
                    line = String.join(",", data);
                    found = true;
                }
                list.add(line);
            }
        } catch (Exception e) { e.printStackTrace(); }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("appointment.txt"))) {
            for (String s : list) { bw.write(s); bw.newLine(); }
            if (found) new ModernDialog("Status Updated!");
            else new ModernDialog("Appointment Not Found!");
        } catch (Exception e) { e.printStackTrace(); }
    }
}