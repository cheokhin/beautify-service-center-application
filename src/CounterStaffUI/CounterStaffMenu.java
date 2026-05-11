package CounterStaffUI;

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

public class CounterStaffMenu extends JPanel implements UserDashboard {

    private static final long serialVersionUID = 1L;

    public static CounterStaffMenu instance; 
    private String counterID;

    public CardLayout rightCardLayout;
    public JPanel rightContainer;

    public CounterStaffMenu() {
        instance = this;
    }

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

        JPanel bg = new GradientPanel(
            new Color(10, 20, 25), 
            new Color(0, 220, 255)
        );
        bg.setLayout(new BorderLayout());
        add(bg, BorderLayout.CENTER);

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(35, 35, 50));
        sidebar.setLayout(null);
        sidebar.setPreferredSize(new Dimension(240, 580)); 

        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setPreferredSize(new Dimension(240, 0));
        sidebarScroll.setBorder(BorderFactory.createEmptyBorder()); 
        sidebarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16); 
        sidebarScroll.getVerticalScrollBar().setBackground(new Color(35, 35, 50)); 
        sidebarScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); 
        
        bg.add(sidebarScroll, BorderLayout.WEST);

        // ================= AVATAR =================
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
            public void mouseClicked(MouseEvent e) {
                showRightPage("DASHBOARD");
            }
        });
        sidebar.add(avatar);

        // ================= LABELS =================
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

        // ================= NAVIGATION BUTTONS =================
        JButton managementBtn = createSidebarButton(" Management", 215);
        JButton appointmentBtn = createSidebarButton(" Appointments", 265);
        JButton paymentBtn = createSidebarButton(" Payment", 315);
        JButton profileBtn = createSidebarButton(" Edit Profile", 365);
        JButton logoutBtn = createSidebarButton(" Log Out", 415);

        sidebar.add(managementBtn);
        sidebar.add(appointmentBtn);
        sidebar.add(paymentBtn);
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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        JPanel card = new RoundedPanel(25, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(400, 200));
        card.setLayout(new GridLayout(2, 1));

        card.setBorder(BorderFactory.createEmptyBorder(45, 0, 45, 0));

        JLabel title = new JLabel("Counter Staff Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        card.add(title);

        JLabel subtitle = new JLabel("Select an option from the sidebar to begin.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(0, 220, 255));
        card.add(subtitle);

        panel.add(card);
        return panel;
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