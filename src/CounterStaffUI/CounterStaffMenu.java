package CounterStaffUI;

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

public class CounterStaffMenu extends JPanel implements UserDashboard {

    private static final long serialVersionUID = 1L;
    public static CounterStaffMenu instance;
    private String counterID;

    public CardLayout rightCardLayout;
    public JPanel rightContainer;

    public CounterStaffMenu() { instance = this; }

    public CounterStaffMenu(String id) {
        instance = this;
        openMenu(id);
    }

    @Override
    public void openMenu(String id) {
        this.counterID = id;
        buildUI();
        MainUI.instance.mainContainer.add(this, "COUNTER_DASHBOARD");
        MainUI.instance.showPage("COUNTER_DASHBOARD");
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel(new Color(10, 20, 25), new Color(0, 220, 255));
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
                g2.setColor(new Color(0, 220, 255, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0, 220, 255));
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

        String counterName = getCounterName(this.counterID);

        JLabel nameLabel = new JLabel(counterName);
        nameLabel.setBounds(20, 120, 200, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(this.counterID);
        idLabel.setBounds(20, 145, 200, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("COUNTER STAFF");
        roleLabel.setBounds(20, 170, 200, 25);
        roleLabel.setForeground(new Color(0, 220, 255));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(roleLabel);

        JButton managementBtn  = createSidebarButton(" Management",   215);
        JButton appointmentBtn = createSidebarButton(" Appointments", 265);
        JButton paymentBtn     = createSidebarButton(" Payment",      315);
        JButton profileBtn     = createSidebarButton(" Edit Profile", 365);
        JButton logoutBtn      = createSidebarButton(" Log Out",      415);

        sidebar.add(managementBtn);
        sidebar.add(appointmentBtn);
        sidebar.add(paymentBtn);
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

        managementBtn.addActionListener(e -> {
            rightContainer.add(new ManagementMenu(this.counterID), "MANAGEMENT");
            showRightPage("MANAGEMENT");
        });
        appointmentBtn.addActionListener(e -> {
            rightContainer.add(new Appointment(this.counterID), "APPOINTMENT");
            showRightPage("APPOINTMENT");
        });
        paymentBtn.addActionListener(e -> {
            rightContainer.add(new PaymentMenu(this.counterID), "PAYMENT");
            showRightPage("PAYMENT");
        });
        profileBtn.addActionListener(e -> {
            rightContainer.add(new EditCounterProfile(this.counterID), "EDIT_PROFILE");
            showRightPage("EDIT_PROFILE");
        });
        logoutBtn.addActionListener(e -> {
            SystemLogger.log(this.counterID, "Counter Staff", "Logged out of the system");
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

        LocalDate today = LocalDate.now();
        DateTimeFormatter apptFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int todayAppts      = 0;
        int unpaidCount     = 0;
        double unpaidAmount = 0;
        int completedPaid   = 0;

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

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 6) continue;

                String dateStr     = d[2].trim();
                String serviceType = d[3].trim();
                String payStatus   = d[4].trim();

                try {
                    LocalDate apptDate = LocalDate.parse(dateStr, apptFmt);
                    if (apptDate.equals(today)) todayAppts++;
                } catch (Exception ignored) {}

                if (payStatus.equalsIgnoreCase("Unpaid")) {
                    unpaidCount++;
                    if (serviceType.equalsIgnoreCase("Normal"))     unpaidAmount += normalPrice;
                    else if (serviceType.equalsIgnoreCase("Major")) unpaidAmount += majorPrice;
                }

                if (payStatus.equalsIgnoreCase("Paid")) completedPaid++;
            }
        } catch (Exception ignored) {}

        java.util.Map<String, String> customerNames = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length >= 2) customerNames.put(d[0].trim(), d[1].trim());
            }
        } catch (Exception ignored) {}

        List<String[]> allReceipts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length >= 5) allReceipts.add(d);
            }
        } catch (Exception ignored) {}
        List<String[]> recentReceipts = allReceipts.size() > 3
            ? new ArrayList<>(allReceipts.subList(allReceipts.size() - 3, allReceipts.size()))
            : new ArrayList<>(allReceipts);
        java.util.Collections.reverse(recentReceipts);

        List<String[]> allFeedbacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",", 7);
                if (d.length >= 6 && d[2].trim().equals("Counter Staff") && d[3].trim().equals(this.counterID)) {
                    allFeedbacks.add(d);
                }
            }
        } catch (Exception ignored) {}
        List<String[]> recentFeedbacks = allFeedbacks.size() > 3
            ? new ArrayList<>(allFeedbacks.subList(allFeedbacks.size() - 3, allFeedbacks.size()))
            : new ArrayList<>(allFeedbacks);
        java.util.Collections.reverse(recentFeedbacks);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(null);
        content.setPreferredSize(new Dimension(900, 580));

        JLabel title = new JLabel("Counter Staff Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(80, 24, 520, 38);
        content.add(title);

        JLabel sub = new JLabel("Welcome back, " + getCounterName(this.counterID) + ".");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(new Color(180, 180, 200));
        sub.setBounds(80, 62, 520, 24);
        content.add(sub);

        content.add(buildStatCard(
            "Today's Appointments",
            String.valueOf(todayAppts),
            new Color(255, 180, 0),
            "calendar",
            80, 100, 260, 110
        ));

        String unpaidLabel = unpaidCount + " appt" + (unpaidCount != 1 ? "s" : "");
        String unpaidVal   = unpaidCount > 0 ? String.format("$%,.0f", unpaidAmount) : "$0";
        content.add(buildDualStatCard(
            "Unpaid Appointments",
            unpaidLabel,
            unpaidVal,
            new Color(255, 60, 60),
            "dollar",
            370, 100, 260, 110
        ));

        content.add(buildStatCard(
            "Completed Transactions",
            String.valueOf(completedPaid),
            new Color(0, 200, 120),
            "check",
            660, 100, 260, 110
        ));

        JLabel receiptTitle = new JLabel("Recent Receipts");
        receiptTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        receiptTitle.setForeground(Color.WHITE);
        receiptTitle.setBounds(80, 232, 300, 28);
        content.add(receiptTitle);

        JLabel feedbackTitle = new JLabel("Customer Feedback");
        feedbackTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        feedbackTitle.setForeground(Color.WHITE);
        feedbackTitle.setBounds(490, 232, 380, 28);
        content.add(feedbackTitle);

        JPanel receiptPanel = new RoundedPanel(18, new Color(35, 35, 55));
        receiptPanel.setBounds(80, 268, 380, 270);
        receiptPanel.setLayout(null);
        content.add(receiptPanel);

        if (recentReceipts.isEmpty()) {
            JLabel noData = new JLabel("No receipts found.");
            noData.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noData.setForeground(new Color(160, 160, 185));
            noData.setBounds(16, 18, 380, 24);
            receiptPanel.add(noData);
        } else {
            int ry = 12;
            for (String[] d : recentReceipts) {
                String receiptId = d[0].trim();
                double amount = 0;
                try { amount = Double.parseDouble(d[4].trim()); } catch (Exception ignored) {}
                String timeStr = d.length >= 7 ? formatRelativeDate(d[6].trim()) : "";

                receiptPanel.add(buildReceiptRow(receiptId, amount, timeStr, 12, ry, 356, 72));
                ry += 82;
            }
        }

        JPanel feedbackPanel = new RoundedPanel(18, new Color(35, 35, 55));
        feedbackPanel.setBounds(490, 268, 420, 270);
        feedbackPanel.setLayout(null);
        content.add(feedbackPanel);

        if (recentFeedbacks.isEmpty()) {
            JLabel noFb = new JLabel("No feedback yet.");
            noFb.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noFb.setForeground(new Color(160, 160, 185));
            noFb.setBounds(16, 18, 380, 24);
            feedbackPanel.add(noFb);
        } else {
            int fy = 12;
            for (String[] d : recentFeedbacks) {
                String apptId   = d[0].trim();
                String custId   = d[1].trim();
                String comment  = d[4].trim();
                int    rating   = 5;
                String timeStr  = "";
                String custName = customerNames.getOrDefault(custId, custId);
                try { rating = Integer.parseInt(d[5].trim()); } catch (Exception ignored) {}
                if (d.length >= 7) timeStr = formatRelativeDate(d[6].trim());

                feedbackPanel.add(buildFeedbackRow(custName, apptId, comment, timeStr, rating, 12, fy, 396, 80));
                fy += 90;
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

    private JPanel buildDualStatCard(String label, String subLabel, String value, Color accent, String iconType, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(18, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        JPanel bubble = buildIconBubble(accent, iconType);
        bubble.setBounds(14, 16, 42, 42);
        p.add(bubble);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(180, 180, 200));
        lbl.setBounds(64, 14, w - 74, 18);
        p.add(lbl);

        JLabel countLbl = new JLabel(subLabel);
        countLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLbl.setForeground(new Color(220, 220, 220));
        countLbl.setBounds(64, 34, w - 74, 18);
        p.add(countLbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 30));
        val.setForeground(accent);
        val.setBounds(64, 52, w - 74, 44);
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
                if (iconType.equals("calendar")) {
                    g2.drawRoundRect(cx - 9, cy - 9, 18, 18, 4, 4);
                    g2.drawLine(cx - 5, cy - 9, cx - 5, cy - 12);
                    g2.drawLine(cx + 5, cy - 9, cx + 5, cy - 12);
                    g2.drawLine(cx - 9, cy - 3, cx + 9, cy - 3);
                    g2.fillRect(cx + 2, cy + 1, 5, 5);
                } else if (iconType.equals("dollar")) {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                    g2.drawString("$", cx - 6, cy + 7);
                } else if (iconType.equals("check")) {
                    g2.drawOval(cx - 9, cy - 9, 18, 18);
                    g2.drawLine(cx - 5, cy, cx - 1, cy + 5);
                    g2.drawLine(cx - 1, cy + 5, cx + 6, cy - 4);
                }
            }
        };
    }

    private JPanel buildReceiptRow(String receiptId, double amount, String timeStr, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(12, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        JPanel iconBubble = new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 200, 120, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0, 200, 120));
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

        JLabel idLbl = new JLabel("Receipt #" + receiptId);
        idLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLbl.setForeground(Color.WHITE);
        idLbl.setBounds(58, 12, 200, 20);
        p.add(idLbl);

        JLabel amtLbl = new JLabel(String.format("- $%.2f", amount));
        amtLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        amtLbl.setForeground(new Color(255, 70, 70));
        amtLbl.setBounds(58, 36, 160, 20);
        p.add(amtLbl);

        JLabel timeLbl = new JLabel(timeStr);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLbl.setForeground(new Color(160, 160, 185));
        timeLbl.setBounds(w - 140, 26, 128, 18);
        timeLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(timeLbl);

        return p;
    }

    private JPanel buildFeedbackRow(String custName, String apptId, String comment, String timeStr, int stars, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(12, new Color(45, 45, 65));
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
                    g2.setColor(i < finalStars ? new Color(255, 190, 30) : new Color(100, 100, 120));
                    g2.fillPolygon(xp, yp, 10);
                    if (i >= finalStars) {
                        g2.setColor(new Color(140, 140, 160));
                        g2.drawPolygon(xp, yp, 10);
                    }
                    cx += size + gap;
                }
            }
        };
        starsPanel.setBounds(12, 10, 88, 14);
        p.add(starsPanel);

        JLabel timeLbl = new JLabel(timeStr);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLbl.setForeground(new Color(160, 160, 185));
        timeLbl.setBounds(w - 120, 10, 108, 14);
        timeLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(timeLbl);

        JLabel nameLbl = new JLabel(custName + "  (" + apptId + ")");
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(new Color(0, 210, 255));
        nameLbl.setBounds(12, 28, w - 24, 18);
        p.add(nameLbl);

        String displayComment = comment.length() > 55 ? comment.substring(0, 52) + "..." : comment;
        JLabel commentLbl = new JLabel(displayComment);
        commentLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        commentLbl.setForeground(Color.WHITE);
        commentLbl.setBounds(12, 50, w - 24, 18);
        p.add(commentLbl);

        return p;
    }

    private String formatRelativeDate(String dateStr) {
        try {
            LocalDate d   = LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            long days = ChronoUnit.DAYS.between(d, now);
            if (days == 0) return "Today";
            if (days == 1) return "Yesterday";
            if (days < 7)  return days + " days ago";
            long weeks = days / 7;
            return weeks == 1 ? "1 week ago" : weeks + " weeks ago";
        } catch (Exception e) { return dateStr; }
    }

    private String getCounterName(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("counter.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) return data[1];
            }
        } catch (Exception e) {}
        return "Counter Staff";
    }
}