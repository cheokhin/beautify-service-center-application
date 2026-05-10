package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import UI.Components.*;
import CustomerUI.CustomerUI;
import ManagerUI.ManagerUI;
import CounterStaffUI.CounterStaffMenu;
import TechnicianUI.TechnicianUI;

public class LogInUI extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextField idField, userField;
    JPasswordField passField;

    public LogInUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel();
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(320, 280));
        card.setLayout(null);

        bg.add(card);

        JLabel title = new JLabel(" LOGIN", SwingConstants.CENTER);
        title.setBounds(50, 20, 220, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        card.add(title);

        addLabel(card, "ID:", 70);
        idField = addTextField(card, 70);

        addLabel(card, "Username:", 110);
        userField = addTextField(card, 110);

        addLabel(card, "Password:", 150);

        passField = new JPasswordField();
        passField.setBounds(140, 150, 120, 30);
        styleField(passField);
        card.add(passField);

        // 👁 Show/Hide Button
        JButton eyeBtn = new JButton("...");
        eyeBtn.setBounds(260, 150, 30, 30);
        eyeBtn.setFocusPainted(false);
        eyeBtn.addActionListener(e -> {
            if (passField.getEchoChar() == 0) {
                passField.setEchoChar('•');
            } else {
                passField.setEchoChar((char) 0);
            }
        });
        card.add(eyeBtn);

        JButton loginBtn = createButton(" Login", 190);
        JButton backBtn = createButton(" Back", 230);

        card.add(loginBtn);
        card.add(backBtn);

        // 🔥 ACTIONS
        loginBtn.addActionListener(e -> login());

        backBtn.addActionListener(e -> {
            idField.setText("");
            userField.setText("");
            passField.setText("");
            MainUI.instance.showPage("MAIN_MENU");
        });

        // 🔥 ENTER KEY LISTENER
        KeyAdapter enterAction = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginBtn.doClick(); // Simulates clicking the login button
                }
            }
        };

        // Attach listener to all fields
        idField.addKeyListener(enterAction);
        userField.addKeyListener(enterAction);
        passField.addKeyListener(enterAction);
    }

    private void addLabel(JPanel card, String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(40, y, 100, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(label);
    }

    private JTextField addTextField(JPanel card, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(140, y, 150, 30);
        styleField(tf);
        card.add(tf);
        return tf;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(new Color(60, 60, 80));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private JButton createButton(String text, int y) {
        JButton btn = new ModernButton(text);
        btn.setBounds(40, y, 250, 35);
        return btn;
    }

    // =========================
    // 🔥 LOGIN LOGIC
    // =========================
    public void login() {

        String id = idField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        String fileName = getFileByID(id);

        if (fileName == null) {
            new ModernDialog("Invalid ID format!"); 
            return;
        }

        boolean idFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(id)) {
                    idFound = true;

                    if (!data[2].equals(username)) {
                        new ModernDialog("Username Invalid!");
                        return;
                    }

                    if (!data[3].equals(password)) {
                        new ModernDialog("Password Invalid!");
                        return;
                    }

                    showLoadingAndOpen(id);
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!idFound) {
            new ModernDialog("ID does not exist!");
        }
    }

    private void showLoadingAndOpen(String id) {
        JDialog loading = new JDialog(MainUI.instance, true);
        loading.setUndecorated(true);
        loading.setSize(250, 120);
        loading.setLocationRelativeTo(MainUI.instance);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));

        JLabel label = new JLabel("Loading System...", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setForeground(new Color(0, 200, 255));
        bar.setBackground(new Color(60, 60, 80));

        panel.add(label, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.SOUTH);

        loading.add(panel);

        new Thread(() -> {
            try { Thread.sleep(800); } catch (Exception ignored) {}

            SwingUtilities.invokeLater(() -> {
                UserDashboard dashboard = null;

                if (id.startsWith("M")) dashboard = new ManagerUI();
                else if (id.startsWith("CS")) dashboard = new CounterStaffMenu();
                else if (id.startsWith("T")) dashboard = new TechnicianUI();
                else if (id.startsWith("C")) dashboard = new CustomerUI();

                if (dashboard != null) {
                    dashboard.openMenu(id);
                    
                    if (!id.startsWith("M")) {
                        MainUI.instance.setVisible(false); 
                    }
                }

                loading.dispose();
                
                idField.setText("");
                userField.setText("");
                passField.setText("");
            });

        }).start();

        loading.setVisible(true);
    }

    private String getFileByID(String id) {
        if (id.matches("M\\d{3}")) return "manager.txt";
        if (id.matches("CS\\d{3}")) return "counter.txt";
        if (id.matches("T\\d{3}")) return "technician.txt";
        if (id.matches("C\\d{3}")) return "customer.txt";
        return null;
    }
}