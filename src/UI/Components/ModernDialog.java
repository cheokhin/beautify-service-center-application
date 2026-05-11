package UI.Components;

import UI.*;
import javax.swing.*;
import java.awt.*;

public class ModernDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    // Standard constructor (Uses your SPA's MainUI as the parent so it centers perfectly)
    public ModernDialog(String message) {
        super(MainUI.instance != null ? MainUI.instance : new JFrame(), true);
        buildDialog(message);
    }

    // Fallback constructor for pages that load before MainUI (like SignInUI)
    public ModernDialog(JFrame parent, String message) {
        super(parent, true);
        buildDialog(message);
    }

    private void buildDialog(String message) {
        setUndecorated(true);
        setSize(240, 100); // Starts slightly smaller for the "pop-out" scaling effect
        setLocationRelativeTo(getParent());

        // Safely check if the user's OS supports fading windows
        boolean canFade = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);

        if (canFade) setOpacity(0f);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(30, 30, 30));
        // Unified Cyan/Blue border to match the SPA theme
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));
        add(panel);

        Icon icon = UIManager.getIcon("OptionPane.warningIcon");
        JLabel iconLabel = new JLabel(resizeIcon(icon, 28, 28));
        iconLabel.setBounds(20, 25, 30, 30);
        panel.add(iconLabel);

        JLabel msg = new JLabel("<html>" + message + "</html>");
        msg.setBounds(65, 15, 230, 50);
        msg.setForeground(Color.WHITE);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(msg);

        // Uses your newly shared ModernButton!
        JButton okBtn = new ModernButton("OK");
        okBtn.setBounds(110, 80, 100, 30);
        okBtn.addActionListener(e -> dispose());
        panel.add(okBtn);

        animate(canFade);
        setVisible(true);
    }

    private void animate(boolean canFade) {
        Timer timer = new Timer(15, null);
        final float[] opacity = {0f};
        final double[] scale = {0.8};

        timer.addActionListener(e -> {
            if (opacity[0] < 1f) {
                opacity[0] += 0.08f;
                scale[0] += 0.025;
                if (canFade) setOpacity(Math.min(opacity[0], 1f));

                int w = (int) (320 * scale[0]);
                int h = (int) (140 * scale[0]);
                setSize(w, h);
                setLocationRelativeTo(getParent()); // Keep it centered while growing
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    private Icon resizeIcon(Icon icon, int w, int h) {
        Image img = ((ImageIcon) icon).getImage();
        return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
}