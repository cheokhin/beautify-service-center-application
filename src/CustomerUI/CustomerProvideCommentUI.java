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
    private JTextField txtComment;

    public CustomerProvideCommentUI(String id) {
        this.customerID = id;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(520, 420));
        card.setLayout(null);

        JLabel title = new JLabel("PROVIDE COMMENT", SwingConstants.CENTER);
        title.setBounds(0, 25, 520, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 68, 440, 1);
        sep.setForeground(new Color(70, 70, 90));
        card.add(sep);

        addLabel(card, "Appointment", 90);
        appointmentBox = new JComboBox<>();
        styleCombo(appointmentBox);
        appointmentBox.setBounds(190, 88, 280, 32);
        card.add(appointmentBox);

        addLabel(card, "Feedback For", 140);
        roleBox = new JComboBox<>(new String[]{"Technician", "Counter Staff"});
        styleCombo(roleBox);
        roleBox.setBounds(190, 138, 280, 32);
        card.add(roleBox);

        addLabel(card, "Rating", 190);
        ratingBox = new JComboBox<>(new String[]{"5 - Excellent", "4 - Good", "3 - Average", "2 - Poor", "1 - Terrible"});
        styleCombo(ratingBox);
        ratingBox.setBounds(190, 188, 280, 32);
        card.add(ratingBox);

        addLabel(card, "Short Comment", 243);
        txtComment = new JTextField();
        txtComment.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtComment.setBackground(new Color(28, 28, 45));
        txtComment.setForeground(new Color(180, 180, 200));
        txtComment.setCaretColor(new Color(180, 180, 200));
        txtComment.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 111, 217), 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        txtComment.setBounds(190, 238, 280, 35);
        card.add(txtComment);

        JButton saveBtn = new ModernButton("Submit Comment");
        saveBtn.setBounds(160, 370, 200, 42);
        card.add(saveBtn);

        loadAppointments();
        saveBtn.addActionListener(e -> saveComment());
        add(card);
    }

    private void styleCombo(JComboBox<String> box) {
        Color bgColor  = new Color(28, 28, 45);
        Color fgColor  = new Color(180, 180, 200);
        Color selColor = new Color(50, 50, 70);
        Color borderColor = new Color(30, 111, 217);

        box.setBackground(bgColor);
        box.setForeground(fgColor);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        box.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        box.setFocusable(false);

        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? selColor : bgColor);
                setForeground(fgColor);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });

        box.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(bgColor);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(fgColor);
                        int w = 10, h = 6;
                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        int[] xs = {cx - w/2, cx + w/2, cx};
                        int[] ys = {cy - h/2, cy - h/2, cy + h/2};
                        g2.fillPolygon(xs, ys, 3);
                        g2.dispose();
                    }
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(36, super.getPreferredSize().height);
                    }
                };
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder());
                return btn;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(bgColor);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        SwingUtilities.invokeLater(() -> {
            Object popup = box.getUI().getAccessibleChild(box, 0);
            if (popup instanceof javax.swing.plaf.basic.ComboPopup cp) {
                cp.getList().setBackground(bgColor);
                cp.getList().setForeground(fgColor);
            }
        });
    }

    private void addLabel(JPanel card, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(45, y + 6, 130, 20);
        label.setForeground(new Color(180, 180, 200));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(label);
    }

    private void loadAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[1].equals(customerID) && data[5].trim().equals("Done")) {
                    appointmentBox.addItem(data[0]);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveComment() {
        String selectedApp = (String) appointmentBox.getSelectedItem();
        String role = (String) roleBox.getSelectedItem();
        String comment = txtComment.getText().trim();
        comment = comment.replace("\n", " ").replace("\r", "");
        String rating = String.valueOf(ratingBox.getSelectedItem()).substring(0, 1);
        rating = rating.substring(rating.length() - 1);

        if (selectedApp == null || comment.isEmpty()) { new ModernDialog("Comment cannot be empty!"); return; }

        String staffId = "NA";
        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 1 && data[0].equals(selectedApp)) {
                    if (role.equals("Technician") && data.length >= 9) {
                        staffId = data[8].trim(); 
                    } else if (role.equals("Counter Staff") && data.length >= 10) {
                        staffId = data[9].trim(); 
                    }
                    break;
                }
            }
        } catch (Exception e) {}

        String today = LocalDate.now().toString();
        ArrayList<String> list = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(selectedApp) && data[1].equals(customerID) && data[2].equals(role)) {
                    line = selectedApp + "," + customerID + "," + role + "," + staffId + "," + comment + "," + rating + "," + today;
                    found = true;
                }
                list.add(line);
            }
        } catch (Exception e) {}

        if (!found) list.add(selectedApp + "," + customerID + "," + role + "," + staffId + "," + comment + "," + rating + "," + today);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("comment.txt"))) {
            for (String s : list) { bw.write(s); bw.newLine(); }
            new ModernDialog("Comment Submitted!");
            txtComment.setText("");
        } catch (Exception e) { e.printStackTrace(); }
    }
}