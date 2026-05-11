package CounterStaffUI;

import javax.swing.*;
import java.awt.*;
import UI.Components.*; 

public class PaymentMenu extends JPanel {

    private static final long serialVersionUID = 1L;
    private String counterID;

    public PaymentMenu(String counterID) {
        this.counterID = counterID;

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(30, new Color(35,35,50));
        card.setPreferredSize(new Dimension(350,230)); 
        card.setLayout(null);

        JLabel title = new JLabel("PAYMENT MENU", SwingConstants.CENTER);
        title.setBounds(60,25,220,35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title);

        Icon paymentIcon = resizeIcon(UIManager.getIcon("FileView.floppyDriveIcon"), 18, 18);
        Icon receiptIcon = resizeIcon(UIManager.getIcon("FileView.fileIcon"), 18, 18);

        JButton collectBtn = new ModernButton(" Collect Payment", paymentIcon);
        JButton receiptBtn = new ModernButton(" Generate Receipt", receiptIcon);

        collectBtn.setBounds(65,85,220,42);
        receiptBtn.setBounds(65,145,220,42);

        card.add(collectBtn);
        card.add(receiptBtn);

        collectBtn.addActionListener(e -> loadNextPage(new CollectPayment(this.counterID)));
        receiptBtn.addActionListener(e -> loadNextPage(new Receipt(this.counterID)));

        add(card);
    }

    private void loadNextPage(JPanel page) {
        CounterStaffMenu.instance.rightContainer.add(page, "PAYMENT_SUB_PAGE");
        CounterStaffMenu.instance.showRightPage("PAYMENT_SUB_PAGE");
    }

    private Icon resizeIcon(Icon icon, int w, int h) {
        Image img = ((ImageIcon) icon).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}