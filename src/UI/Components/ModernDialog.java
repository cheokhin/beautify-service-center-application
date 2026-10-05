package UI.Components;

import UI.*;
import javax.swing.*;
import java.awt.*;

public class ModernDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private boolean confirmed = false;

    public ModernDialog(String message) {
        this(message, false);
    }

    public ModernDialog(String message, boolean isConfirm) {
        super(MainUI.instance != null ? MainUI.instance : new JFrame(), true);
        buildDialog(message, isConfirm);
    }

    public ModernDialog(JFrame parent, String message) {
        super(parent, true);
        buildDialog(message, false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void buildDialog(String message, boolean isConfirm) {
        setUndecorated(true);
        setSize(320, 140); 
        setLocationRelativeTo(getParent());

        boolean canFade = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);

        if (canFade) setOpacity(0f);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2));
        add(panel);

        Icon icon = UIManager.getIcon(isConfirm ? "OptionPane.questionIcon" : "OptionPane.warningIcon");
        JLabel iconLabel = new JLabel(resizeIcon(icon, 28, 28));
        iconLabel.setBounds(20, 25, 30, 30);
        panel.add(iconLabel);

        JLabel msg = new JLabel("<html>" + message + "</html>");
        msg.setBounds(65, 15, 230, 50);
        msg.setForeground(Color.WHITE);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(msg);

        if (isConfirm) {
            JButton yesBtn = new ModernButton("Yes");
            yesBtn.setBounds(50, 80, 100, 30);
            yesBtn.addActionListener(e -> { confirmed = true; dispose(); });
            panel.add(yesBtn);

            JButton noBtn = new ModernButton("No");
            noBtn.setBounds(170, 80, 100, 30);
            noBtn.setBackground(new Color(60, 60, 80)); 
            noBtn.addActionListener(e -> { confirmed = false; dispose(); });
            panel.add(noBtn);
        } else {
            JButton okBtn = new ModernButton("OK");
            okBtn.setBounds(110, 80, 100, 30);
            okBtn.addActionListener(e -> { confirmed = true; dispose(); });
            panel.add(okBtn);
        }

        animate(canFade);
        setVisible(true);
    }

    private void animate(boolean canFade) {
        if (!canFade) return;
        
        Timer timer = new Timer(10, null);
        final float[] opacity = {0f};

        timer.addActionListener(e -> {
            opacity[0] += 0.06f; 
            if (opacity[0] >= 1f) {
                setOpacity(1f);
                timer.stop();
            } else {
                setOpacity(opacity[0]);
            }
        });
        timer.start();
    }

    private Icon resizeIcon(Icon icon, int w, int h) {
        Image img = ((ImageIcon) icon).getImage();
        return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
}