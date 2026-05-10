package UI.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernButton extends JButton {
    private static final long serialVersionUID = 1L;
    private boolean hovering = false;
    private boolean pressed = false;
    private Color bgColor = new Color(60, 60, 80);
    private Color hoverColor = new Color(0, 200, 255);

    // Constructor for normal buttons
    public ModernButton(String text) {
        this(text, null);
    }

    // Constructor for buttons with icons (like in MainUI)
    public ModernButton(String text, Icon icon) {
        super(text, icon);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        if (icon != null) {
            setHorizontalAlignment(SwingConstants.LEFT);
            setIconTextGap(12);
        }

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
            public void mouseExited(MouseEvent e) { hovering = false; pressed = false; repaint(); }
            public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
            public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        if (!pressed) {
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(4, 4, w - 4, h - 4, 20, 20);
        }

        if (pressed) g2.setColor(bgColor.darker());
        else if (hovering) g2.setColor(hoverColor);
        else g2.setColor(bgColor);

        g2.fillRoundRect(0, 0, w - 4, h - 4, 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}