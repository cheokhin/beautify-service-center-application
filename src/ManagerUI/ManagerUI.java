package ManagerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import UI.Components.*;
import UI.MainUI;
import UI.UserDashboard;

public class ManagerUI extends JPanel implements UserDashboard {

    private static final long serialVersionUID = 1L;

    public static ManagerUI instance; 
    private String loggedInId;

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
        
        MainUI.instance.mainContainer.add(this, "MANAGER_DASHBOARD");
        MainUI.instance.showPage("MANAGER_DASHBOARD");
    }

    private void buildUI() {
        setLayout(new BorderLayout()); 

        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 20, 40),
                        getWidth(), getHeight(), new Color(0, 200, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new BorderLayout());
        add(bg, BorderLayout.CENTER);

        // ================= SIDEBAR (Now a sharp rectangle) =================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(35, 35, 50));
        sidebar.setLayout(null);
        // 🔥 Set a fixed preferred size so the scrollpane knows when to activate
        sidebar.setPreferredSize(new Dimension(240, 580)); 

        // ================= SIDEBAR SCROLL PANE =================
        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setPreferredSize(new Dimension(240, 0));
        sidebarScroll.setBorder(BorderFactory.createEmptyBorder()); // Removes ugly white border
        sidebarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling speed
        sidebarScroll.getVerticalScrollBar().setBackground(new Color(35, 35, 50)); 
        sidebarScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Thin sleek scrollbar
        
        bg.add(sidebarScroll, BorderLayout.WEST);

        // ================= AVATAR =================
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
        avatar.setBounds(75, 20, 90, 90); 
        avatar.setOpaque(false);
        
        avatar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        avatar.setToolTipText("Return to Dashboard");
        avatar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showRightPage("DASHBOARD");
            }
        });
        sidebar.add(avatar);

        // ================= LABELS =================
        String managerName = getManagerName(this.loggedInId);

        JLabel nameLabel = new JLabel(managerName);
        nameLabel.setBounds(20, 120, 200, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(this.loggedInId);
        idLabel.setBounds(20, 145, 200, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("MANAGER");
        roleLabel.setBounds(20, 170, 200, 25);
        roleLabel.setForeground(new Color(0, 200, 255));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(roleLabel);

        // ================= NAVIGATION BUTTONS =================
        JButton manageBtn = createSidebarButton(" Manage Users", 215);
        JButton priceBtn = createSidebarButton(" Set Service Prices", 265);
        JButton feedbackBtn = createSidebarButton(" View Feedbacks", 315);
        JButton reportBtn = createSidebarButton(" Analyzed Reports", 365);
        JButton profileBtn = createSidebarButton(" Edit Profile", 415);
        JButton logoutBtn = createSidebarButton(" Log Out", 465);

        sidebar.add(manageBtn);
        sidebar.add(priceBtn);
        sidebar.add(feedbackBtn);
        sidebar.add(reportBtn);
        sidebar.add(profileBtn);
        sidebar.add(logoutBtn);

        JLabel timeLabel = new JLabel();
        timeLabel.setBounds(20, 530, 200, 25);
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
        rightContainer.setOpaque(false); 
        bg.add(rightContainer, BorderLayout.CENTER);

        rightContainer.add(createWelcomeScreen(), "DASHBOARD");
        rightCardLayout.show(rightContainer, "DASHBOARD");

        // ================= ACTIONS =================
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
    }

    public void showRightPage(String pageName) {
        rightCardLayout.show(rightContainer, pageName);
    }

    private JButton createSidebarButton(String text, int y) {
        JButton btn = new ModernButton(text);
        btn.setBounds(20, y, 200, 40);
        return btn;
    }

    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(400, 200));
        card.setLayout(new GridLayout(2, 1));

        JLabel title = new JLabel("Welcome to Manager Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        JLabel subtitle = new JLabel("Select an option from the sidebar to begin.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(0, 200, 255));
        card.add(subtitle);

        panel.add(card);
        return panel;
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
        } catch (Exception e) {}
        return "Manager";
    }
}