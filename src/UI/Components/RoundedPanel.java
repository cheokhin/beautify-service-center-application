package UI.Components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private int radius;
    private Color bgColor;

    public RoundedPanel(int radius, Color color) {
        this.radius = radius;
        this.bgColor = color;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}