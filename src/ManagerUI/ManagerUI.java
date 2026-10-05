package ManagerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        JPanel bg = new GradientPanel(
            new Color(20, 10, 10), 
            new Color(255, 60, 60)
        );
        bg.setLayout(new BorderLayout());
        add(bg, BorderLayout.CENTER);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(35, 35, 50));
        sidebar.setLayout(null);
        sidebar.setPreferredSize(new Dimension(240, 600));

        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setPreferredSize(new Dimension(240, 0));
        sidebarScroll.setBorder(BorderFactory.createEmptyBorder());
        sidebarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16);
        sidebarScroll.getVerticalScrollBar().setBackground(new Color(35, 35, 50)); 
        sidebarScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        
        bg.add(sidebarScroll, BorderLayout.WEST);

        JPanel avatar = new JPanel() {
            private static final long serialVersionUID = 1L;
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 60, 60, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 60, 60));
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
            public void mouseClicked(MouseEvent e) { showRightPage("DASHBOARD"); }
        });
        sidebar.add(avatar);

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
        roleLabel.setForeground(new Color(255, 60, 60));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(roleLabel);

        JButton manageBtn  = createSidebarButton(" Manage Users",      210);
        JButton priceBtn   = createSidebarButton(" Set Service Prices", 255);
        JButton feedbackBtn= createSidebarButton(" View Feedbacks",     300);
        JButton reportBtn  = createSidebarButton(" Analyzed Reports",   345);
        JButton auditBtn   = createSidebarButton(" Audit Logs",         390);
        JButton profileBtn = createSidebarButton(" Edit Profile",       435);
        JButton logoutBtn  = createSidebarButton(" Log Out",            480);

        sidebar.add(manageBtn);
        sidebar.add(priceBtn);
        sidebar.add(feedbackBtn);
        sidebar.add(reportBtn);
        sidebar.add(auditBtn);
        sidebar.add(profileBtn);
        sidebar.add(logoutBtn);

        
        int startY = logoutBtn.getY() + logoutBtn.getHeight() + 40; 

        JLabel timeLabel = new JLabel();
        timeLabel.setBounds(20, startY, 200, 20); 
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(timeLabel);

        JLabel dateLabel = new JLabel();
        dateLabel.setBounds(20, startY + 20, 200, 20); 
        dateLabel.setForeground(new Color(180, 180, 200));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(dateLabel);

        JLabel hintLabel = new JLabel("Tip: Click avatar for Dashboard");
        hintLabel.setBounds(10, startY + 55, 220, 20); 
        hintLabel.setForeground(new Color(120, 120, 140)); 
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(hintLabel);

        sidebar.setPreferredSize(new Dimension(240, startY + 100));

        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            timeLabel.setText(new SimpleDateFormat("hh:mm:ss a").format(now));
            dateLabel.setText(new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(now)); 
        });
        timer.start();

        rightCardLayout = new CardLayout();
        rightContainer  = new JPanel(rightCardLayout);
        rightContainer.setOpaque(false); 
        bg.add(rightContainer, BorderLayout.CENTER);

        rightContainer.add(createWelcomeScreen(), "DASHBOARD");
        rightCardLayout.show(rightContainer, "DASHBOARD");

        priceBtn.addActionListener(e -> {
            rightContainer.add(new SetPriceUI(this.loggedInId), "SET_PRICE");
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
            rightContainer.add(new AnalyzedReportUI(this.loggedInId), "ANALYZED_REPORT");
            showRightPage("ANALYZED_REPORT");
        });
        profileBtn.addActionListener(e -> {
            rightContainer.add(new EditProfileUI(this.loggedInId), "EDIT_PROFILE");
            showRightPage("EDIT_PROFILE");
        });
        auditBtn.addActionListener(e -> {
            rightContainer.add(new AuditLogUI(), "AUDIT_LOGS");
            showRightPage("AUDIT_LOGS");
        });
        logoutBtn.addActionListener(e -> {
            SystemLogger.log(this.loggedInId, "Manager", "Logged out of the system");
            MainUI.instance.showPage("LOGIN");
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

        double normalPrice = 0, majorPrice = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("prices.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 2) {
                    String type = d[0].trim();
                    double price = Double.parseDouble(d[1].trim());
                    if (type.equalsIgnoreCase("Normal")) normalPrice = price;
                    else if (type.equalsIgnoreCase("Major")) majorPrice = price;
                }
            }
        } catch (Exception ignored) {}

        int upcomingAppts = 0, completedServices = 0, completedThisWeek = 0;
        double pendingPayments = 0;
        int[] weekdayCounts = new int[7]; 

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate weekEnd   = today.with(java.time.DayOfWeek.SUNDAY);

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 6) continue;

                String serviceType   = d[3].trim();  
                String paymentStatus = d[4].trim();  
                String apptStatus    = d[5].trim();  

                if (apptStatus.equalsIgnoreCase("Pending") || apptStatus.equalsIgnoreCase("In Progress"))
                    upcomingAppts++;

                if (apptStatus.equalsIgnoreCase("Done"))
                    completedServices++;

                if (paymentStatus.equalsIgnoreCase("Unpaid")) {
                    if (serviceType.equalsIgnoreCase("Normal"))       pendingPayments += normalPrice;
                    else if (serviceType.equalsIgnoreCase("Major"))   pendingPayments += majorPrice;
                }

                try {
                    String dateStr = d[2].trim(); 
                    LocalDate apptDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (!apptDate.isBefore(weekStart) && !apptDate.isAfter(weekEnd)) {
                        int dow = apptDate.getDayOfWeek().getValue() - 1; 
                        if (dow >= 0 && dow < 7) weekdayCounts[dow]++;
                        if (apptStatus.equalsIgnoreCase("Done")) completedThisWeek++;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

        java.util.Map<String, String> customerNames = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length >= 2) {
                    String name = d[1].trim(); 
                    customerNames.put(d[0].trim(), name);
                }
            }
        } catch (Exception ignored) {}

        List<String[]> allFeedbacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",", 6); 
                if (d.length >= 5) allFeedbacks.add(d);
            }
        } catch (Exception ignored) {}
        List<String[]> feedbacks = allFeedbacks.size() > 3
            ? allFeedbacks.subList(allFeedbacks.size() - 3, allFeedbacks.size())
            : allFeedbacks;
        java.util.Collections.reverse(new ArrayList<>(feedbacks));
        feedbacks = new ArrayList<>(feedbacks);
        java.util.Collections.reverse((ArrayList<String[]>) feedbacks);

        double totalRevenue = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("payment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length >= 6) {
                    try {
                        LocalDate payDate = LocalDate.parse(d[5].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (!payDate.isBefore(weekStart) && !payDate.isAfter(weekEnd)) {
                            totalRevenue += Double.parseDouble(d[3].trim());
                        }
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(null);
        content.setPreferredSize(new Dimension(960, 680));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(80, 24, 500, 38);
        content.add(title);

        JLabel sub = new JLabel("Welcome back, " + getManagerName(loggedInId) + ".");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(new Color(180, 180, 200));
        sub.setBounds(80, 62, 500, 24);
        content.add(sub);

        String payStr = pendingPayments > 0 ? String.format("$%,.0f", pendingPayments) : "$0";
        content.add(buildStatCard("Upcoming Appointments", String.valueOf(upcomingAppts),
                new Color(0, 210, 255),  "calendar", 80,  100, 240, 110));
        content.add(buildStatCard("Pending Payments",       payStr,
                new Color(255, 70,  70), "dollar",   370, 100, 240, 110));
        content.add(buildStatCard("Completed Services",     String.valueOf(completedServices),
                new Color(160, 90, 255), "check",    660, 100, 240, 110));

        JLabel fbTitle = new JLabel("Recent Feedback");
        fbTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        fbTitle.setForeground(Color.WHITE);
        fbTitle.setBounds(80, 228, 300, 26);
        content.add(fbTitle);

        JLabel perfTitle = new JLabel("Performance Summary");
        perfTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        perfTitle.setForeground(Color.WHITE);
        perfTitle.setBounds(500, 228, 380, 26);
        content.add(perfTitle);

        int fbContainerH = 3 * 86 + 2 * 8 + 16; 
        JPanel fbContainer = new RoundedPanel(18, new Color(35, 35, 55));
        fbContainer.setBounds(80, 262, 390, fbContainerH);
        fbContainer.setLayout(null);
        content.add(fbContainer);

        if (feedbacks.isEmpty()) {
            JLabel noFb = new JLabel("No feedback yet.");
            noFb.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noFb.setForeground(new Color(160, 160, 185));
            noFb.setBounds(14, 18, 362, 24);
            fbContainer.add(noFb);
        } else {
            int fbY = 8;
            for (String[] d : feedbacks) {
                String custId  = d[1].trim();
                String role    = d[2].trim();
                String comment = d[3].trim();
                int    rating  = 5;
                String timeStr = "";
                String custName = customerNames.getOrDefault(custId, custId);
                try { rating = Integer.parseInt(d[4].trim()); } catch (Exception ignored) {}
                if (d.length >= 6) timeStr = formatRelativeDate(d[5].trim());

                fbContainer.add(buildFeedbackCard(custName, role, comment, timeStr, rating, 8, fbY, 374, 86));
                fbY += 94;
            }
        }

        JPanel perfCard = new RoundedPanel(18, new Color(45, 45, 65));
        perfCard.setBounds(500, 262, 376, 360);
        perfCard.setLayout(null);
        content.add(perfCard);

        JLabel chartLbl = new JLabel("Weekly Appointments");
        chartLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chartLbl.setForeground(Color.WHITE);
        chartLbl.setBounds(16, 14, 220, 20);
        perfCard.add(chartLbl);

        DateTimeFormatter weekFmt = DateTimeFormatter.ofPattern("d MMM");
        JLabel weekRangeLbl = new JLabel(weekStart.format(weekFmt) + " \u2013 " + weekEnd.format(weekFmt));
        weekRangeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        weekRangeLbl.setForeground(new Color(160, 160, 185));
        weekRangeLbl.setBounds(236, 16, 120, 16);
        weekRangeLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        perfCard.add(weekRangeLbl);

        JPanel chart = buildLineChart(weekdayCounts);
        chart.setBounds(10, 38, 356, 180);
        perfCard.add(chart);

        JPanel revCard = new RoundedPanel(12, new Color(35, 35, 55));
        revCard.setBounds(12, 228, 166, 116);
        revCard.setLayout(null);
        perfCard.add(revCard);

        JLabel revLbl = new JLabel("Revenue This Week");
        revLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        revLbl.setForeground(new Color(180, 180, 200));
        revLbl.setBounds(12, 12, 145, 18);
        revCard.add(revLbl);

        JLabel revVal = new JLabel(String.format("$%,.0f", totalRevenue));
        revVal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        revVal.setForeground(new Color(0, 210, 255));
        revVal.setBounds(12, 36, 145, 40);
        revCard.add(revVal);

        JLabel revSub = new JLabel("this week only");
        revSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        revSub.setForeground(new Color(120, 120, 145));
        revSub.setBounds(12, 74, 145, 16);
        revCard.add(revSub);

        JPanel svcCard = new RoundedPanel(12, new Color(35, 35, 55));
        svcCard.setBounds(192, 228, 172, 116);
        svcCard.setLayout(null);
        perfCard.add(svcCard);

        JLabel svcLbl = new JLabel("Services Completed");
        svcLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        svcLbl.setForeground(new Color(180, 180, 200));
        svcLbl.setBounds(12, 12, 150, 18);
        svcCard.add(svcLbl);

        JLabel svcVal = new JLabel(String.valueOf(completedThisWeek));
        svcVal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        svcVal.setForeground(new Color(160, 90, 255));
        svcVal.setBounds(12, 36, 150, 40);
        svcCard.add(svcVal);

        JLabel svcSub = new JLabel("this week only");
        svcSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        svcSub.setForeground(new Color(120, 120, 145));
        svcSub.setBounds(12, 74, 150, 16);
        svcCard.add(svcSub);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(content, BorderLayout.CENTER);
        return wrapper;
    }

    private String formatRelativeDate(String dateStr) {
        try {
            LocalDate apptDate = LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now      = LocalDate.now();
            long days = ChronoUnit.DAYS.between(apptDate, now);
            if (days == 0)  return "Today";
            if (days == 1)  return "Yesterday";
            if (days < 7)   return days + " days ago";
            long weeks = days / 7;
            return weeks == 1 ? "1 week ago" : weeks + " weeks ago";
        } catch (Exception e) {
            return dateStr;
        }
    }

    private JPanel buildStatCard(String label, String value, Color accent, String iconType,
                                  int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(18, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        JPanel bubble = new JPanel() {
            private static final long serialVersionUID = 1L;
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(accent);
                g2.setStroke(new java.awt.BasicStroke(2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                int cx = getWidth()/2, cy = getHeight()/2;
                if (iconType.equals("calendar")) {
                    g2.drawRoundRect(cx-9, cy-9, 18, 18, 4, 4);
                    g2.drawLine(cx-5, cy-9, cx-5, cy-12);
                    g2.drawLine(cx+5, cy-9, cx+5, cy-12);
                    g2.drawLine(cx-9, cy-3, cx+9, cy-3);
                    g2.fillRect(cx+2, cy+1, 5, 5);
                } else if (iconType.equals("dollar")) {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                    g2.drawString("$", cx-6, cy+7);
                } else if (iconType.equals("check")) {
                    g2.drawOval(cx-9, cy-9, 18, 18);
                    g2.drawLine(cx-5, cy, cx-1, cy+5);
                    g2.drawLine(cx-1, cy+5, cx+6, cy-4);
                }
            }
        };
        bubble.setOpaque(false);
        bubble.setBounds(14, 16, 42, 42);
        p.add(bubble);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(180, 180, 200));
        lbl.setBounds(64, 26, w - 74, 20);
        p.add(lbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 36));
        val.setForeground(accent);
        val.setBounds(64, 52, w - 74, 48);
        p.add(val);

        return p;
    }

    private JPanel buildFeedbackCard(String custName, String role, String comment, String timeStr, int stars,
                                      int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(14, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        final int finalStars = Math.max(0, Math.min(stars, 5));
        JPanel starsPanel = new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 13, gap = 3, cx = 0;
                for (int i = 0; i < 5; i++) {
                    int[] xp = new int[10], yp = new int[10];
                    double r1 = size / 2.0, r2 = size / 4.5;
                    double ox = cx + r1, oy = r1;
                    for (int j = 0; j < 10; j++) {
                        double angle = Math.PI / 2 + j * Math.PI / 5;
                        double r = (j % 2 == 0) ? r1 : r2;
                        xp[j] = (int)(ox - r * Math.cos(angle));
                        yp[j] = (int)(oy - r * Math.sin(angle));
                    }
                    if (i < finalStars) {
                        g2.setColor(new Color(255, 190, 30));
                        g2.fillPolygon(xp, yp, 10);
                    } else {
                        g2.setColor(new Color(100, 100, 120));
                        g2.fillPolygon(xp, yp, 10);
                        g2.setColor(new Color(140, 140, 160));
                        g2.drawPolygon(xp, yp, 10);
                    }
                    cx += size + gap;
                }
            }
        };
        starsPanel.setBounds(14, 8, 90, 16);
        p.add(starsPanel);

        JLabel roleLbl = new JLabel(role);
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLbl.setForeground(new Color(160, 160, 185));
        roleLbl.setBounds(w - 130, 10, 116, 16);
        roleLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(roleLbl);

        JLabel nameLbl = new JLabel(custName);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(new Color(0, 210, 255));
        nameLbl.setBounds(14, 28, w - 28, 18);
        p.add(nameLbl);

        JLabel commentLbl = new JLabel(comment.length() > 52 ? comment.substring(0, 49) + "..." : comment);
        commentLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        commentLbl.setForeground(Color.WHITE);
        commentLbl.setBounds(14, 48, w - 28, 18);
        p.add(commentLbl);

        JLabel dateLbl = new JLabel(timeStr);
        dateLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLbl.setForeground(new Color(160, 160, 185));
        dateLbl.setBounds(14, 66, w - 28, 16);
        p.add(dateLbl);

        return p;
    }

    private JPanel buildLineChart(int[] weekdayCounts) {
        String[] dayLabels = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

        return new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int W = getWidth(), H = getHeight();
                int pL = 28, pR = 10, pT = 10, pB = 28;
                int cW = W - pL - pR, cH = H - pT - pB;
                int n = weekdayCounts.length;

                int maxVal = 1;
                for (int v : weekdayCounts) if (v > maxVal) maxVal = v;
                int minVal = 0;
                int displayMax = maxVal + 2;

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setStroke(new java.awt.BasicStroke(1));
                for (int step = 0; step <= displayMax; step += Math.max(1, displayMax / 4)) {
                    int gy = pT + cH - (int)((step - minVal) / (double)(displayMax - minVal) * cH);
                    g2.setColor(new Color(60, 60, 90));
                    g2.drawLine(pL, gy, pL + cW, gy);
                    g2.setColor(new Color(160, 160, 185));
                    g2.drawString(String.valueOf(step), 0, gy + 4);
                }

                int[] px = new int[n], py = new int[n];
                for (int i = 0; i < n; i++) {
                    px[i] = pL + (int)(i / (double)(n - 1) * cW);
                    py[i] = pT + cH - (int)((weekdayCounts[i] - minVal) / (double)(displayMax - minVal) * cH);
                }

                int[] fx = new int[n+2], fy = new int[n+2];
                System.arraycopy(px, 0, fx, 0, n);
                System.arraycopy(py, 0, fy, 0, n);
                fx[n] = px[n-1]; fy[n] = pT + cH;
                fx[n+1] = px[0]; fy[n+1] = pT + cH;
                g2.setColor(new Color(0, 210, 255, 28));
                g2.fillPolygon(fx, fy, n + 2);

                g2.setColor(new Color(0, 210, 255));
                g2.setStroke(new java.awt.BasicStroke(2.5f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                for (int i = 0; i < n - 1; i++) g2.drawLine(px[i], py[i], px[i+1], py[i+1]);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                for (int i = 0; i < n; i++) {
                    g2.setColor(new Color(0, 210, 255));
                    g2.fillOval(px[i]-4, py[i]-4, 8, 8);
                    if (weekdayCounts[i] > 0) {
                        g2.setColor(Color.WHITE);
                        g2.drawString(String.valueOf(weekdayCounts[i]), px[i]-3, py[i]-8);
                    }
                }

                g2.setColor(new Color(160, 160, 185));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                FontMetrics fm = g2.getFontMetrics();
                for (int i = 0; i < n; i++) {
                    g2.drawString(dayLabels[i], px[i] - fm.stringWidth(dayLabels[i])/2, pT + cH + 18);
                }
            }
        };
    }

    private String getManagerName(String id) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("manager.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) { br.close(); return data[1]; }
            }
            br.close();
        } catch (Exception e) {}
        return "Manager";
    }
}