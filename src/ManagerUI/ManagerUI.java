package ManagerUI;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import UI.Components.*;
import UI.MainUI;
import UI.UserDashboard;

public class ManagerUI extends JPanel implements UserDashboard {

    private static final long serialVersionUID = 1L;

    public static ManagerUI instance; // 🔥 Shared instance for right-side routing
    private String loggedInId;

    // 🔥 The new nested SPA container
    public CardLayout rightCardLayout;
    public JPanel rightContainer;

    public ManagerUI() {
        instance = this;
    }

    public ManagerUI(String id) {
        instance = this;
        openMenu(id); 
    }

    @Override
    public void openMenu(String id) {
        this.loggedInId = id;
        buildUI(); 
        
        // Adds the whole Manager interface (Sidebar + Right Container) to MainUI
        MainUI.instance.mainContainer.add(this, "MANAGER_DASHBOARD");
        MainUI.instance.showPage("MANAGER_DASHBOARD");
    }

    private void buildUI() {
        setLayout(new BorderLayout()); 

        JPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        add(bg, BorderLayout.CENTER);

        // ================= SIDEBAR (STAYS FIXED) =================
        JPanel sidebar = new RoundedPanel(25, new Color(35, 35, 50));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(null);
        bg.add(sidebar, BorderLayout.WEST);

        // Avatar
        JPanel avatar = new JPanel() {
            private static final long serialVersionUID = 1L;
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 200, 255, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0, 200, 255));
                g2.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                g2.setColor(Color.WHITE);
                g2.fillOval(32, 22, 26, 26);
                g2.fillRoundRect(24, 50, 42, 28, 20, 20);
            }
        };
        avatar.setBounds(55, 30, 90, 90);
        avatar.setOpaque(false);
        sidebar.add(avatar);

        // Labels
        String managerName = getManagerName(this.loggedInId);

        JLabel nameLabel = new JLabel(managerName);
        nameLabel.setBounds(30, 130, 140, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(this.loggedInId);
        idLabel.setBounds(30, 160, 140, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("MANAGER");
        roleLabel.setBounds(20, 195, 160, 25);
        roleLabel.setForeground(new Color(0, 200, 255));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(roleLabel);

        JLabel welcomeLabel = new JLabel("System Control Active");
        welcomeLabel.setBounds(20, 235, 160, 25);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(welcomeLabel);

        JLabel timeLabel = new JLabel();
        timeLabel.setBounds(20, 320, 160, 25);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(timeLabel);

        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
            timeLabel.setText(sdf.format(new Date()));
        });
        timer.start();

        // ================= RIGHT SIDE CONTAINER =================
        rightCardLayout = new CardLayout();
        rightContainer = new JPanel(rightCardLayout);
        rightContainer.setOpaque(false); // Makes it transparent to show the background gradient
        bg.add(rightContainer, BorderLayout.CENTER);

        // Load the 6 buttons into the right side by default
        rightContainer.add(createDashboardMenu(), "DASHBOARD");
        rightCardLayout.show(rightContainer, "DASHBOARD");
    }

    // ================= ROUTER FOR RIGHT SIDE =================
    public void showRightPage(String pageName) {
        rightCardLayout.show(rightContainer, pageName);
    }

    // ================= DASHBOARD MENU (6 BUTTONS) =================
    private JPanel createDashboardMenu() {
        JPanel centerWrapper = new JPanel(new GridBagLayout()); 
        centerWrapper.setOpaque(false);

        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(360, 350));
        card.setLayout(null);
        centerWrapper.add(card);

        JLabel title = new JLabel(" MANAGER DASHBOARD", SwingConstants.CENTER);
        title.setBounds(50, 20, 260, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        JButton manageBtn = createButton(" Manage Users", 70);
        JButton priceBtn = createButton(" Set Service Prices", 115);
        JButton feedbackBtn = createButton(" View Feedbacks", 160);
        JButton reportBtn = createButton(" Analyzed Reports", 205);
        JButton profileBtn = createButton(" Edit Profile", 250);
        JButton logoutBtn = createButton(" Log Out", 295);

        card.add(manageBtn);
        card.add(priceBtn);
        card.add(feedbackBtn);
        card.add(reportBtn);
        card.add(profileBtn);
        card.add(logoutBtn);

        // 🔥 ACTIONS: These now inject the sub-pages directly into the rightContainer!
        priceBtn.addActionListener(e -> {
            rightContainer.add(new SetPriceUI(), "SET_PRICE");
            showRightPage("SET_PRICE");
        });

        manageBtn.addActionListener(e -> {
            rightContainer.add(new ManageUserUI(this.loggedInId), "MANAGE_USERS");
            showRightPage("MANAGE_USERS");
        });

        feedbackBtn.addActionListener(e -> {
            rightContainer.add(new ViewFeedbackUI(), "VIEW_FEEDBACK");
            showRightPage("VIEW_FEEDBACK");
        });

        reportBtn.addActionListener(e -> {
            rightContainer.add(new AnalyzedReportUI(), "ANALYZED_REPORT");
            showRightPage("ANALYZED_REPORT");
        });

        profileBtn.addActionListener(e -> {
            rightContainer.add(new EditProfileUI(this.loggedInId), "EDIT_PROFILE");
            showRightPage("EDIT_PROFILE");
        });

        logoutBtn.addActionListener(e -> {
            MainUI.instance.showPage("MAIN_MENU");
            MainUI.instance.mainContainer.remove(this); 
        });

        return centerWrapper;
    }

    private String getManagerName(String id) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("manager.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) {
                    br.close();
                    return data[1];
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Manager";
    }

    private JButton createButton(String text, int y) {
        JButton btn = new ModernButton(text);
        btn.setBounds(70, y, 220, 35);
        return btn;
    }
}