package CustomerUI;

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

import UI.*;
import UI.Components.*;

public class CustomerUI extends JPanel implements UserDashboard {

    private static final long serialVersionUID = 1L;

    public static CustomerUI instance;
    private String customerID;

    public CardLayout rightCardLayout;
    public JPanel rightContainer;

    public CustomerUI() {
        instance = this;
    }

    public CustomerUI(String id) {
        instance = this;
        openMenu(id);
    }

    @Override
    public void openMenu(String id) {
        this.customerID = id;
        buildUI();
        MainUI.instance.mainContainer.add(this, "CUSTOMER_DASHBOARD");
        MainUI.instance.showPage("CUSTOMER_DASHBOARD");
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel();
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
            public void mouseClicked(MouseEvent e) { showRightPage("DASHBOARD"); }
        });
        sidebar.add(avatar);

        String custName = getCustomerName(this.customerID);

        JLabel nameLabel = new JLabel(custName);
        nameLabel.setBounds(20, 120, 200, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(this.customerID);
        idLabel.setBounds(20, 145, 200, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("CUSTOMER");
        roleLabel.setBounds(20, 170, 200, 25);
        roleLabel.setForeground(new Color(0, 200, 255));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(roleLabel);

        JButton serviceBtn  = createSidebarButton(" Service History",  215);
        JButton paymentBtn  = createSidebarButton(" Payment History",  265);
        JButton commentBtn  = createSidebarButton(" Provide Comment",  315);
        JButton feedbackBtn = createSidebarButton(" View Feedback",    365);
        JButton profileBtn  = createSidebarButton(" Edit Profile",     415);
        JButton logoutBtn   = createSidebarButton(" Log Out",          465);

        sidebar.add(serviceBtn);
        sidebar.add(paymentBtn);
        sidebar.add(commentBtn);
        sidebar.add(feedbackBtn);
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

        serviceBtn.addActionListener(e -> {
            rightContainer.add(new CustomerServiceHistoryUI(this.customerID), "SERVICE_HISTORY");
            showRightPage("SERVICE_HISTORY");
        });
        paymentBtn.addActionListener(e -> {
            rightContainer.add(new CustomerPaymentHistoryUI(this.customerID), "PAYMENT_HISTORY");
            showRightPage("PAYMENT_HISTORY");
        });
        commentBtn.addActionListener(e -> {
            rightContainer.add(new CustomerProvideCommentUI(this.customerID), "PROVIDE_COMMENT");
            showRightPage("PROVIDE_COMMENT");
        });
        feedbackBtn.addActionListener(e -> {
            rightContainer.add(new CustomerViewFeedbackUI(this.customerID), "VIEW_FEEDBACK");
            showRightPage("VIEW_FEEDBACK");
        });
        profileBtn.addActionListener(e -> {
            rightContainer.add(new CustomerEditProfileUI(this.customerID), "EDIT_PROFILE");
            showRightPage("EDIT_PROFILE");
        });
        logoutBtn.addActionListener(e -> {
            SystemLogger.log(this.customerID, "Customer", "Logged out of the system");
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


        List<String[]> myAppointments = new ArrayList<>();

        String upcomingApptId   = "";
        String upcomingTask     = "";
        String upcomingDate     = "";
        String upcomingType     = "";
        LocalDate oldestPending = null;

        int unpaidCount = 0;

        DateTimeFormatter apptFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 6) continue;
                if (!d[1].trim().equals(this.customerID)) continue;

                myAppointments.add(d);

                String payStatus = d[4].trim();
                String jobStatus = d[5].trim();

                if (payStatus.equalsIgnoreCase("Unpaid")) unpaidCount++;

                if (jobStatus.equalsIgnoreCase("Pending") || jobStatus.equalsIgnoreCase("In Progress")) {
                    try {
                        LocalDate apptDate = LocalDate.parse(d[2].trim(), apptFmt);
                        if (oldestPending == null || apptDate.isBefore(oldestPending)) {
                            oldestPending      = apptDate;
                            upcomingApptId     = d[0].trim();
                            upcomingDate       = d[2].trim();
                            upcomingType       = d[3].trim();
                            upcomingTask       = d.length >= 7 ? d[6].trim() : "";
                        }
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}


        double ratingSum   = 0;
        int    ratingCount = 0;

        List<String[]> myComments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",", 7);
                if (d.length < 6) continue;
                if (!d[1].trim().equals(this.customerID)) continue;
                myComments.add(d);
                try {
                    ratingSum += Double.parseDouble(d[5].trim());
                    ratingCount++;
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

        double avgRating = ratingCount > 0 ? ratingSum / ratingCount : 0.0;
        String ratingStr = ratingCount > 0 ? String.format("%.1f", avgRating) : "N/A";

        java.util.Set<String> myApptIds = new java.util.HashSet<>();
        for (String[] d : myAppointments) myApptIds.add(d[0].trim());

        List<String[]> myServiceHistory = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",", 7);
                if (d.length < 5) continue;
                if (myApptIds.contains(d[0].trim())) {
                    myServiceHistory.add(d);
                }
            }
        } catch (Exception ignored) {}

        List<String[]> recentHistory = myServiceHistory.size() > 3
            ? new ArrayList<>(myServiceHistory.subList(myServiceHistory.size() - 3, myServiceHistory.size()))
            : new ArrayList<>(myServiceHistory);
        java.util.Collections.reverse(recentHistory);

        List<String[]> recentAppts = myAppointments.size() > 3
            ? new ArrayList<>(myAppointments.subList(myAppointments.size() - 3, myAppointments.size()))
            : new ArrayList<>(myAppointments);
        java.util.Collections.reverse(recentAppts);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(null);
        content.setPreferredSize(new Dimension(900, 600));

        JLabel title = new JLabel("Customer Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(80, 24, 520, 38);
        content.add(title);

        JLabel sub = new JLabel("Welcome back, " + getCustomerName(this.customerID) + ".");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(new Color(180, 180, 200));
        sub.setBounds(80, 62, 520, 24);
        content.add(sub);


        content.add(buildUpcomingServiceCard(
            upcomingApptId, upcomingTask, upcomingDate, upcomingType,
            80, 100, 260, 110
        ));

        content.add(buildPaymentStatusCard(
            unpaidCount,
            370, 100, 260, 110
        ));

        content.add(buildStatCard(
            "Average Satisfaction",
            ratingStr,
            new Color(255, 190, 30),
            "star",
            660, 100, 260, 110
        ));

        JLabel apptTitle = new JLabel("Recent Appointments");
        apptTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        apptTitle.setForeground(Color.WHITE);
        apptTitle.setBounds(80, 232, 300, 28);
        content.add(apptTitle);

        JLabel historyTitle = new JLabel("Service History");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        historyTitle.setForeground(Color.WHITE);
        historyTitle.setBounds(490, 232, 380, 28);
        content.add(historyTitle);

        JPanel apptPanel = new RoundedPanel(18, new Color(35, 35, 55));
        apptPanel.setBounds(80, 268, 380, 290);
        apptPanel.setLayout(null);
        content.add(apptPanel);

        if (recentAppts.isEmpty()) {
            JLabel noData = new JLabel("No appointments found.");
            noData.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noData.setForeground(new Color(160, 160, 185));
            noData.setBounds(16, 18, 360, 24);
            apptPanel.add(noData);
        } else {
            int ay = 10;
            for (String[] d : recentAppts) {
                String apptId     = d[0].trim();
                String apptDate   = d[2].trim();
                String serviceTyp = d[3].trim();
                String task       = d.length >= 7 ? d[6].trim() : "-";
                String timeStr    = formatRelativeDate(apptDate);
                apptPanel.add(buildApptRow(apptId, task, serviceTyp, timeStr, 10, ay, 360, 78));
                ay += 88;
            }
        }

        JPanel historyPanel = new RoundedPanel(18, new Color(35, 35, 55));
        historyPanel.setBounds(490, 268, 420, 290);
        historyPanel.setLayout(null);
        content.add(historyPanel);

        if (recentHistory.isEmpty()) {
            JLabel noHist = new JLabel("No service history found.");
            noHist.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noHist.setForeground(new Color(160, 160, 185));
            noHist.setBounds(16, 18, 380, 24);
            historyPanel.add(noHist);
        } else {
            int hy = 12;
            for (String[] d : recentHistory) {
                String apptId         = d[0].trim();
                String afterCondition = d.length >= 5 ? d[4].trim().replace("<<BR>>", " ") : "-";
                String timeStr        = d.length >= 7 ? formatRelativeDate(d[6].trim()) : "";
                historyPanel.add(buildServiceHistoryRow(apptId, afterCondition, timeStr, 12, hy, 396, 72));
                hy += 82;
            }
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildUpcomingServiceCard(String apptId, String task, String date, String type, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(18, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        Color accent = new Color(0, 200, 255);

        JPanel bubble = buildIconBubble(accent, "wrench");
        bubble.setBounds(14, 16, 42, 42);
        p.add(bubble);

        JLabel lbl = new JLabel("Upcoming Service");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(180, 180, 200));
        lbl.setBounds(64, 14, w - 74, 18);
        p.add(lbl);

        if (apptId.isEmpty()) {
            JLabel val = new JLabel("None");
            val.setFont(new Font("Segoe UI", Font.BOLD, 22));
            val.setForeground(accent);
            val.setBounds(64, 34, w - 74, 30);
            p.add(val);
        } else {
            String displayTask = task.isEmpty() ? type : task;
            if (displayTask.length() > 16) displayTask = displayTask.substring(0, 14) + "..";

            JLabel taskLbl = new JLabel(displayTask);
            taskLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            taskLbl.setForeground(accent);
            taskLbl.setBounds(64, 32, w - 74, 26);
            p.add(taskLbl);

            JLabel dateLbl = new JLabel(date + "  ·  " + type);
            dateLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateLbl.setForeground(new Color(180, 180, 200));
            dateLbl.setBounds(64, 60, w - 74, 18);
            p.add(dateLbl);

            JLabel idLbl = new JLabel(apptId);
            idLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            idLbl.setForeground(new Color(130, 130, 160));
            idLbl.setBounds(64, 80, w - 74, 16);
            p.add(idLbl);
        }

        return p;
    }

    /**
     * Payment Status card — "Paid in Full" or "Unpaid (N)"
     */
    private JPanel buildPaymentStatusCard(int unpaidCount, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(18, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        boolean allPaid = unpaidCount == 0;
        Color accent = allPaid ? new Color(0, 220, 150) : new Color(255, 60, 60);
        String iconType = allPaid ? "check" : "alert";

        JPanel bubble = buildIconBubble(accent, iconType);
        bubble.setBounds(14, 16, 42, 42);
        p.add(bubble);

        JLabel lbl = new JLabel("Payment Status");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(180, 180, 200));
        lbl.setBounds(64, 22, w - 74, 18);
        p.add(lbl);

        String statusText = allPaid ? "Paid in Full" : "Unpaid (" + unpaidCount + ")";
        JLabel val = new JLabel(statusText);
        val.setFont(new Font("Segoe UI", Font.BOLD, allPaid ? 20 : 24));
        val.setForeground(accent);
        val.setBounds(64, 46, w - 74, 46);
        p.add(val);

        return p;
    }

    private JPanel buildStatCard(String label, String value, Color accent, String iconType, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(18, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        JPanel bubble = buildIconBubble(accent, iconType);
        bubble.setBounds(14, 16, 42, 42);
        p.add(bubble);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(180, 180, 200));
        lbl.setBounds(64, 22, w - 74, 20);
        p.add(lbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 36));
        val.setForeground(accent);
        val.setBounds(64, 46, w - 74, 50);
        p.add(val);

        return p;
    }

    private JPanel buildIconBubble(Color accent, String iconType) {
        return new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(accent);
                g2.setStroke(new java.awt.BasicStroke(2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2, cy = getHeight() / 2;
                if (iconType.equals("check")) {
                    g2.drawOval(cx - 9, cy - 9, 18, 18);
                    g2.drawLine(cx - 5, cy, cx - 1, cy + 5);
                    g2.drawLine(cx - 1, cy + 5, cx + 6, cy - 4);
                } else if (iconType.equals("star")) {
                    int[] xp = new int[10], yp = new int[10];
                    for (int j = 0; j < 10; j++) {
                        double angle = Math.PI / 2 + j * Math.PI / 5;
                        double r = (j % 2 == 0) ? 10 : 4.5;
                        xp[j] = (int)(cx - r * Math.cos(angle));
                        yp[j] = (int)(cy - r * Math.sin(angle));
                    }
                    g2.fillPolygon(xp, yp, 10);
                } else if (iconType.equals("wrench")) {
                    g2.drawLine(cx - 6, cy + 7, cx + 4, cy - 3);
                    g2.drawOval(cx + 1, cy - 9, 8, 8);
                    g2.drawLine(cx - 9, cy + 4, cx - 3, cy + 8);
                } else if (iconType.equals("alert")) {
                    g2.drawOval(cx - 9, cy - 9, 18, 18);
                    g2.drawLine(cx, cy - 5, cx, cy + 1);
                    g2.fillOval(cx - 1, cy + 4, 3, 3);
                }
            }
        };
    }

    private JPanel buildApptRow(String apptId, String task, String serviceType, String timeStr, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(12, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        JLabel apptIdLbl = new JLabel(apptId);
        apptIdLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        apptIdLbl.setForeground(new Color(0, 200, 255));
        apptIdLbl.setBounds(12, 10, 160, 18);
        p.add(apptIdLbl);

        JLabel timeLbl = new JLabel(timeStr);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLbl.setForeground(new Color(160, 160, 185));
        timeLbl.setBounds(w - 120, 10, 108, 14);
        timeLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(timeLbl);

        String displayTask = task.length() > 40 ? task.substring(0, 37) + "..." : task;
        JLabel taskLbl = new JLabel(displayTask);
        taskLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        taskLbl.setForeground(Color.WHITE);
        taskLbl.setBounds(12, 32, w - 24, 18);
        p.add(taskLbl);

        JLabel typeLbl = new JLabel(serviceType + " Service");
        typeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLbl.setForeground(new Color(180, 180, 200));
        typeLbl.setBounds(12, 54, w - 24, 16);
        p.add(typeLbl);

        return p;
    }

    private JPanel buildServiceHistoryRow(String apptId, String afterCondition, String timeStr, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(12, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        JPanel iconBubble = new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 200, 255, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0, 200, 255));
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.drawRoundRect(cx - 8, cy - 10, 16, 18, 3, 3);
                g2.drawLine(cx - 5, cy - 5, cx + 5, cy - 5);
                g2.drawLine(cx - 5, cy,     cx + 5, cy);
                g2.drawLine(cx - 5, cy + 5, cx + 3, cy + 5);
            }
        };
        iconBubble.setOpaque(false);
        iconBubble.setBounds(12, (h - 36) / 2, 36, 36);
        p.add(iconBubble);

        JLabel apptLbl = new JLabel(apptId);
        apptLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        apptLbl.setForeground(Color.WHITE);
        apptLbl.setBounds(58, 12, 200, 20);
        p.add(apptLbl);

        String displayAfter = afterCondition.length() > 45 ? afterCondition.substring(0, 42) + "..." : afterCondition;
        JLabel afterLbl = new JLabel(displayAfter);
        afterLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        afterLbl.setForeground(new Color(0, 220, 150));
        afterLbl.setBounds(58, 36, w - 70, 18);
        p.add(afterLbl);

        JLabel timeLbl = new JLabel(timeStr);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLbl.setForeground(new Color(160, 160, 185));
        timeLbl.setBounds(w - 140, 12, 128, 18);
        timeLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(timeLbl);

        return p;
    }

    private String formatRelativeDate(String dateStr) {
        try {
            String datePart = dateStr.trim().split(" ")[0];
            LocalDate d   = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            long days = ChronoUnit.DAYS.between(d, now);
            if (days == 0)  return "Today";
            if (days == 1)  return "Yesterday";
            if (days < 0)   return "In " + Math.abs(days) + " days";
            if (days < 7)   return days + " days ago";
            long weeks = days / 7;
            return weeks == 1 ? "1 week ago" : weeks + " weeks ago";
        } catch (Exception e) { return dateStr; }
    }

    private String getCustomerName(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(id)) return data[1].trim();
            }
        } catch (Exception e) {}
        return "Customer";
    }
}