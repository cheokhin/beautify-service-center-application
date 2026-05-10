package ManagerUI;

import javax.swing.*;
import UI.MainUI;
import java.awt.*;
import java.io.*;

import UI.Components.*;

public class SetPriceUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField normalField, majorField;

    // 🔥 Removed String id parameter
    public SetPriceUI() {
        buildUI();

        // 🔥 SPA Routing
        MainUI.instance.mainContainer.add(this, "SET_PRICE");
        MainUI.instance.showPage("SET_PRICE");
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(360, 300));
        card.setLayout(null);
        bg.add(card);

        // ================= TITLE =================
        JLabel title = new JLabel("UPDATE PRICES", SwingConstants.CENTER);
        title.setBounds(80, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        // ================= INPUT FIELDS =================
        JLabel lblNormal = new JLabel("Normal Service (RM):");
        lblNormal.setBounds(40, 80, 150, 25);
        lblNormal.setForeground(Color.WHITE);
        lblNormal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(lblNormal);

        normalField = new JTextField();
        normalField.setBounds(200, 78, 110, 30);
        styleField(normalField);
        card.add(normalField);

        JLabel lblMajor = new JLabel("Major Service (RM):");
        lblMajor.setBounds(40, 140, 150, 25);
        lblMajor.setForeground(Color.WHITE);
        lblMajor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(lblMajor);

        majorField = new JTextField();
        majorField.setBounds(200, 138, 110, 30);
        styleField(majorField);
        card.add(majorField);

        // ================= BUTTONS =================
        JButton saveBtn = new ModernButton("Save Prices");
        saveBtn.setBounds(50, 210, 120, 35);
        card.add(saveBtn);

        JButton backBtn = new ModernButton("Return");
        backBtn.setBounds(190, 210, 120, 35);
        card.add(backBtn);

        // ================= ACTIONS =================
        loadPrices(); 

        saveBtn.addActionListener(e -> savePrices());
        
        backBtn.addActionListener(e -> {
            MainUI.instance.showPage("MANAGER_DASHBOARD");
            MainUI.instance.mainContainer.remove(this); 
        });
    }

    // ================= LOGIC =================

    private void loadPrices() {
        try (BufferedReader br = new BufferedReader(new FileReader("prices.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals("Normal")) normalField.setText(data[1]);
                if (data[0].equals("Major")) majorField.setText(data[1]);
            }
        } catch (Exception e) {}
    }

    private void savePrices() {
        String normalPrice = normalField.getText().trim();
        String majorPrice = majorField.getText().trim();

        if (normalPrice.isEmpty() || majorPrice.isEmpty()) {
            new ModernDialog("Prices cannot be empty!");
            return;
        }
        
        if (!normalPrice.matches("\\d+(\\.\\d+)?") || !majorPrice.matches("\\d+(\\.\\d+)?")) {
            new ModernDialog("Prices must be valid numbers!");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("prices.txt", false))) {
            bw.write("Normal," + normalPrice);
            bw.newLine();
            bw.write("Major," + majorPrice);
            bw.newLine();
            
            new ModernDialog("Prices updated successfully!");
        } catch (Exception e) {
            new ModernDialog("Error saving prices.");
            e.printStackTrace();
        }
    }

    // ================= UI HELPERS & COMPONENTS =================

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
}