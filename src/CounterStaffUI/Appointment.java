package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import UI.Components.*; 

public class Appointment extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    public Appointment(String counterID) {
        this.counterID = counterID;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(380, 280)); 
        card.setLayout(null);

        JLabel title = new JLabel("APPOINTMENTS", SwingConstants.CENTER);
        title.setBounds(60, 25, 250, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title);

        Icon createIcon = resizeIcon(UIManager.getIcon("FileChooser.newFolderIcon"), 18, 18);
        Icon updateIcon = resizeIcon(UIManager.getIcon("FileView.floppyDriveIcon"), 18, 18);
        Icon assignIcon = resizeIcon(UIManager.getIcon("FileView.computerIcon"), 18, 18);

        JButton createBtn = new ModernButton(" Create Appointment", createIcon);
        JButton updateBtn = new ModernButton(" Update Appointment", updateIcon);
        JButton assignBtn = new ModernButton(" Assign Technician", assignIcon);

        createBtn.setBounds(80, 85, 220, 42);
        updateBtn.setBounds(80, 140, 220, 42);
        assignBtn.setBounds(80, 195, 220, 42);

        card.add(createBtn);
        card.add(updateBtn);
        card.add(assignBtn);

        createBtn.addActionListener(e -> {
            loadNextPage(new CreateAppointment(this.counterID));
        });

        updateBtn.addActionListener(e -> {
            loadNextPage(new UpdateAppointment(this.counterID));
        });

        assignBtn.addActionListener(e -> {
            loadNextPage(new AssignAppointment(this.counterID));
        });

        add(card);
    }

    private void loadNextPage(JPanel page) {
        CounterStaffMenu.instance.rightContainer.add(page, "APPOINTMENT_SUB_PAGE");
        CounterStaffMenu.instance.showRightPage("APPOINTMENT_SUB_PAGE");
    }

    private Icon resizeIcon(Icon icon, int w, int h) {
        Image img = ((ImageIcon) icon).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}