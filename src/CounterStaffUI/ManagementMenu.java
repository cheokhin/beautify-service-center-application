package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import UI.Components.*; 

public class ManagementMenu extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    public ManagementMenu(String counterID) {
        this.counterID = counterID;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35, 35, 50));
        card.setPreferredSize(new Dimension(380, 310)); 
        card.setLayout(null);

        JLabel title = new JLabel("CUSTOMER MANAGEMENT", SwingConstants.CENTER);
        title.setBounds(40, 25, 300, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title);

        Icon createIcon = resizeIcon(UIManager.getIcon("FileChooser.newFolderIcon"), 18, 18);
        Icon readIcon   = resizeIcon(UIManager.getIcon("FileView.directoryIcon"), 18, 18);
        Icon updateIcon = resizeIcon(UIManager.getIcon("FileView.floppyDriveIcon"), 18, 18);
        Icon deleteIcon = resizeIcon(UIManager.getIcon("OptionPane.errorIcon"), 18, 18);

        JButton createBtn = new ModernButton(" Create", createIcon);
        JButton readBtn   = new ModernButton(" Read", readIcon);
        JButton updateBtn = new ModernButton(" Update", updateIcon);
        JButton deleteBtn = new ModernButton(" Delete", deleteIcon);

        createBtn.setBounds(80, 85, 220, 42);
        readBtn.setBounds(80, 135, 220, 42);
        updateBtn.setBounds(80, 185, 220, 42);
        deleteBtn.setBounds(80, 235, 220, 42);

        card.add(createBtn);
        card.add(readBtn);
        card.add(updateBtn);
        card.add(deleteBtn);

        createBtn.addActionListener(e -> loadNextPage(new CreateCustomer(this.counterID)));
        
        readBtn.addActionListener(e -> loadNextPage(new ReadCustomer(this.counterID)));
        
        updateBtn.addActionListener(e -> loadNextPage(new UpdateCustomer(this.counterID)));
        
        deleteBtn.addActionListener(e -> loadNextPage(new DeleteCustomer(this.counterID)));

        add(card);
    }


    private void loadNextPage(JPanel page) {
        CounterStaffMenu.instance.rightContainer.add(page, "CUSTOMER_SUB_PAGE");
        CounterStaffMenu.instance.showRightPage("CUSTOMER_SUB_PAGE");
    }


    private Icon resizeIcon(Icon icon, int w, int h) {
        if (icon instanceof ImageIcon) {
            Image img = ((ImageIcon) icon).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return icon; 
    }
}