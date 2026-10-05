package TechnicianUI;

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

public class TechnicianUI extends JPanel implements UserDashboard {

    private static final long serialVersionUID = 1L;
    public static TechnicianUI instance;
    private String techID;
    public CardLayout rightCardLayout;
    public JPanel rightContainer;

    public TechnicianUI() {
        instance = this;
    }

    public TechnicianUI(String id) {
        instance = this;
        openMenu(id);
    }

    public void openMenu(String id) {
        this.techID = id;
        buildUI();
        MainUI.instance.mainContainer.add(this, "TECHNICIAN_DASHBOARD");
        MainUI.instance.showPage("TECHNICIAN_DASHBOARD");
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel bg = new GradientPanel(new Color(30, 25, 10), new Color(255, 200, 0));
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
                g2.setColor(new Color(255, 200, 0, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 200, 0));
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

        String techName = getTechnicianName(this.techID);
        JLabel nameLabel = new JLabel(techName);
        nameLabel.setBounds(20, 120, 200, 25);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(nameLabel);

        JLabel idLabel = new JLabel(this.techID);
        idLabel.setBounds(20, 145, 200, 20);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(idLabel);

        JLabel roleLabel = new JLabel("TECHNICIAN");
        roleLabel.setBounds(20, 170, 200, 25);
        roleLabel.setForeground(new Color(255, 200, 0));
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(roleLabel);

        JButton currentJobBtn  = createSidebarButton(" Current Job",       215);
        JButton feedbackBtn    = createSidebarButton(" Provide Feedback",   265);
        JButton commentBtn     = createSidebarButton(" View Comment",       315);
        JButton inventoryBtn   = createSidebarButton(" Parts Inventory",    365);
        JButton profileBtn     = createSidebarButton(" Edit Profile",       415);
        JButton logoutBtn      = createSidebarButton(" Log Out",            465);

        sidebar.add(currentJobBtn);
        sidebar.add(feedbackBtn);
        sidebar.add(commentBtn);
        sidebar.add(inventoryBtn);
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

        currentJobBtn.addActionListener(e -> {
            rightContainer.add(new TechnicianJobUI(this.techID), "CURRENT_JOB");
            showRightPage("CURRENT_JOB");
        });
        feedbackBtn.addActionListener(e -> {
            rightContainer.add(new ProvideFeedbackUI(this.techID), "PROVIDE_FEEDBACK");
            showRightPage("PROVIDE_FEEDBACK");
        });
        commentBtn.addActionListener(e -> {
            rightContainer.add(new ViewCommentUI(this.techID), "VIEW_COMMENT");
            showRightPage("VIEW_COMMENT");
        });
        inventoryBtn.addActionListener(e -> {
            rightContainer.add(new PartsInventoryUI(this.techID), "PARTS_INVENTORY");
            showRightPage("PARTS_INVENTORY");
        });
        profileBtn.addActionListener(e -> {
            rightContainer.add(new EditTechnicianProfileUI(this.techID), "EDIT_PROFILE");
            showRightPage("EDIT_PROFILE");
        });
        logoutBtn.addActionListener(e -> {
            SystemLogger.log(this.techID, "Technician", "Logged out of the system");
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

        int assignedJobs  = 0;
        int completedJobs = 0;

        java.util.Map<String, String> apptTechMap = new java.util.HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length < 9) continue;
                String apptId  = d[0].trim();
                String techId  = d[8].trim();
                String status  = d[5].trim();
                apptTechMap.put(apptId, techId);
                if (techId.equals(this.techID)) {
                    assignedJobs++;
                    if (status.equalsIgnoreCase("Done")) completedJobs++;
                }
            }
        } catch (Exception ignored) {}

        List<String[]> allComments = new ArrayList<>();
        double ratingSum   = 0;
        int    ratingCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("comment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",", 7);
                if (d.length < 6) continue;
                String apptId  = d[0].trim();
                String role    = d[2].trim();
                String assignedTech = apptTechMap.getOrDefault(apptId, "");
                if (role.equalsIgnoreCase("Technician") && assignedTech.equals(this.techID)) {
                    allComments.add(d);
                    try {
                        ratingSum += Double.parseDouble(d[5].trim());
                        ratingCount++;
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}

        double avgRating = ratingCount > 0 ? ratingSum / ratingCount : 0.0;
        String ratingStr = ratingCount > 0 ? String.format("%.1f", avgRating) : "N/A";

        List<String[]> recentComments = allComments.size() > 3
            ? new ArrayList<>(allComments.subList(allComments.size() - 3, allComments.size()))
            : new ArrayList<>(allComments);
        java.util.Collections.reverse(recentComments);

        java.util.Map<String, String> customerNames = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length >= 2) customerNames.put(d[0].trim(), d[1].trim());
            }
        } catch (Exception ignored) {}

        List<String[]> allServiceNotes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",", 7);
                if (d.length < 3) continue;
                if (d[1].trim().equals(this.techID)) {
                    allServiceNotes.add(d);
                }
            }
        } catch (Exception ignored) {}

        List<String[]> recentNotes = allServiceNotes.size() > 3
            ? new ArrayList<>(allServiceNotes.subList(allServiceNotes.size() - 3, allServiceNotes.size()))
            : new ArrayList<>(allServiceNotes);
        java.util.Collections.reverse(recentNotes);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(null);
        content.setPreferredSize(new Dimension(900, 580));

        JLabel title = new JLabel("Technician Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(80, 24, 520, 38);
        content.add(title);

        JLabel sub = new JLabel("Welcome back, " + getTechnicianName(this.techID) + ".");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(new Color(180, 180, 200));
        sub.setBounds(80, 62, 520, 24);
        content.add(sub);

        content.add(buildStatCard(
            "Assigned Jobs",
            String.valueOf(assignedJobs),
            new Color(0, 200, 255),
            "wrench",
            80, 100, 260, 110
        ));

        content.add(buildStatCard(
            "Jobs Completed",
            String.valueOf(completedJobs),
            new Color(0, 220, 150),
            "check",
            370, 100, 260, 110
        ));

        content.add(buildStatCard(
            "Customer Ratings",
            ratingStr,
            new Color(255, 190, 30),
            "star",
            660, 100, 260, 110
        ));

        JLabel commentsTitle = new JLabel("Latest Comments");
        commentsTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        commentsTitle.setForeground(Color.WHITE);
        commentsTitle.setBounds(80, 232, 300, 28);
        content.add(commentsTitle);

        JLabel notesTitle = new JLabel("Service Notes");
        notesTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        notesTitle.setForeground(Color.WHITE);
        notesTitle.setBounds(490, 232, 380, 28);
        content.add(notesTitle);

        JPanel commentsPanel = new RoundedPanel(18, new Color(35, 35, 55));
        commentsPanel.setBounds(80, 268, 380, 270);
        commentsPanel.setLayout(null);
        content.add(commentsPanel);

        if (recentComments.isEmpty()) {
            JLabel noData = new JLabel("No comments yet.");
            noData.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noData.setForeground(new Color(160, 160, 185));
            noData.setBounds(16, 18, 360, 24);
            commentsPanel.add(noData);
        } else {
            int cy = 12;
            for (String[] d : recentComments) {
                String custId   = d[1].trim();
                String comment  = d[4].trim();
                int    rating   = 5;
                String timeStr  = "";
                String custName = customerNames.getOrDefault(custId, custId);
                try { rating = Integer.parseInt(d[5].trim()); } catch (Exception ignored) {}
                if (d.length >= 7) timeStr = formatRelativeDate(d[6].trim());
                commentsPanel.add(buildFeedbackRow(custName, d[0].trim(), comment, timeStr, rating, 12, cy, 356, 80));
                cy += 90;
            }
        }

        JPanel notesPanel = new RoundedPanel(18, new Color(35, 35, 55));
        notesPanel.setBounds(490, 268, 420, 270);
        notesPanel.setLayout(null);
        content.add(notesPanel);

        if (recentNotes.isEmpty()) {
            JLabel noNote = new JLabel("No service notes found.");
            noNote.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noNote.setForeground(new Color(160, 160, 185));
            noNote.setBounds(16, 18, 380, 24);
            notesPanel.add(noNote);
        } else {
            int ny = 12;
            for (String[] d : recentNotes) {
                String apptId   = d[0].trim();
                String noteText = d[2].trim().replace("<<BR>>", " ");
                String timeStr  = d.length >= 7 ? formatRelativeDate(d[6].trim()) : "";
                notesPanel.add(buildServiceNoteRow(apptId, noteText, timeStr, 12, ny, 396, 72));
                ny += 82;
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
                    g2.setStroke(new java.awt.BasicStroke(2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx - 6, cy + 7, cx + 4, cy - 3);
                    g2.drawOval(cx + 1, cy - 9, 8, 8);
                    g2.drawLine(cx - 9, cy + 4, cx - 3, cy + 8);
                }
            }
        };
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
                int size = 13, gap = 3, startX = 0;
                for (int i = 0; i < 5; i++) {
                    int[] xp = new int[10], yp = new int[10];
                    double r1 = size / 2.0, r2 = size / 4.5;
                    double ox = startX + r1, oy = r1;
                    for (int j = 0; j < 10; j++) {
                        double angle = Math.PI / 2 + j * Math.PI / 5;
                        double r = (j % 2 == 0) ? r1 : r2;
                        xp[j] = (int)(ox - r * Math.cos(angle));
                        yp[j] = (int)(oy - r * Math.sin(angle));
                    }
                    g2.setColor(i < finalStars ? new Color(255, 190, 30) : new Color(100, 100, 120));
                    g2.fillPolygon(xp, yp, 10);
                    startX += size + gap;
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
        nameLbl.setForeground(new Color(0, 200, 255));
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

    private JPanel buildServiceNoteRow(String apptId, String noteText, String timeStr, int x, int y, int w, int h) {
        JPanel p = new RoundedPanel(12, new Color(45, 45, 65));
        p.setBounds(x, y, w, h);
        p.setLayout(null);

        JPanel iconBubble = new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 200, 0, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(255, 200, 0));
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

        JLabel apptLbl = new JLabel("Appt: " + apptId);
        apptLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        apptLbl.setForeground(Color.WHITE);
        apptLbl.setBounds(58, 12, 200, 20);
        p.add(apptLbl);

        String displayNote = noteText.length() > 48 ? noteText.substring(0, 45) + "..." : noteText;
        JLabel noteLbl = new JLabel(displayNote);
        noteLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        noteLbl.setForeground(new Color(200, 200, 220));
        noteLbl.setBounds(58, 36, w - 70, 18);
        p.add(noteLbl);

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
            if (days == 0) return "Today";
            if (days == 1) return "Yesterday";
            if (days < 7)  return days + " days ago";
            long weeks = days / 7;
            return weeks == 1 ? "1 week ago" : weeks + " weeks ago";
        } catch (Exception e) { return dateStr; }
    }

    private String getTechnicianName(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("technician.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(id)) return data[1].trim();
            }
        } catch (Exception e) {}
        return "Technician";
    }
}