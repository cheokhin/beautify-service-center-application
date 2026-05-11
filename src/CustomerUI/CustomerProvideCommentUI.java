package CustomerUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import UI.Components.*; 

public class CustomerProvideCommentUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private String customerID;

    private JComboBox<String> appointmentBox;
    private JComboBox<String> roleBox;
    private JComboBox<String> ratingBox;
    private JTextArea txtComment;

    public CustomerProvideCommentUI(String id) {
        this.customerID = id;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(500, 420)); 
        card.setLayout(null);

        JLabel title = new JLabel("PROVIDE COMMENT", SwingConstants.CENTER);
        title.setBounds(0, 20, 500, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        addLabel(card, "Appointment:", 80);
        appointmentBox = new JComboBox<>();
        appointmentBox.setBounds(180, 80, 240, 30);
        appointmentBox.setBackground(Color.WHITE);
        card.add(appointmentBox);

        addLabel(card, "Feedback For:", 130);
        roleBox = new JComboBox<>(new String[]{"Technician", "CounterStaff"});
        roleBox.setBounds(180, 130, 240, 30);
        roleBox.setBackground(Color.WHITE);
        card.add(roleBox);

        addLabel(card, "Comment:", 180);
        txtComment = new JTextArea();
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        txtComment.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(txtComment);
        scroll.setBounds(180, 180, 240, 80);
        card.add(scroll);

        addLabel(card, "Rating (1-5):", 280);
        ratingBox = new JComboBox<>(new String[]{"5", "4", "3", "2", "1"});
        ratingBox.setBounds(180, 280, 240, 30);
        ratingBox.setBackground(Color.WHITE);
        card.add(ratingBox);

        // Centered Save Button
        JButton saveBtn = new ModernButton(" Submit");
        saveBtn.setBounds(185, 340, 130, 40);
        card.add(saveBtn);

        loadAppointments();
        saveBtn.addActionListener(e -> saveComment());

        add(card);
    }

    private void addLabel(JPanel card, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(50, y, 120, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(label);
    }

    private void loadAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[1].equals(customerID)) {
                    appointmentBox.addItem(data[0]);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveComment() {
        String selectedApp = (String) appointmentBox.getSelectedItem();
        String role = (String) roleBox.getSelectedItem();
        String comment = txtComment.getText().trim();
        String rating = (String) ratingBox.getSelectedItem();

        if (selectedApp == null || comment.isEmpty()) { new ModernDialog("Comment cannot be empty!"); return; }

        String today = LocalDate.now().toString();
        ArrayList<String> list = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(selectedApp) && data[1].equals(customerID) && data[2].equals(role)) {
                    line = selectedApp + "," + customerID + "," + role + "," + comment + "," + rating + "," + today;
                    found = true;
                }
                list.add(line);
            }
        } catch (Exception e) {}

        if (!found) list.add(selectedApp + "," + customerID + "," + role + "," + comment + "," + rating + "," + today);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("comment.txt"))) {
            for (String s : list) { bw.write(s); bw.newLine(); }
            new ModernDialog("Comment Submitted!");
            txtComment.setText("");
        } catch (Exception e) { e.printStackTrace(); }
    }
}