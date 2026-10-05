package UI.Components;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Color colorStart;
    private Color colorEnd;

    public GradientPanel() {
        this.colorStart = new Color(20, 20, 40);
        this.colorEnd = new Color(0, 200, 255);
    }

    public GradientPanel(Color colorStart, Color colorEnd) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        GradientPaint gp = new GradientPaint(
                0, 0, colorStart,
                getWidth(), getHeight(), colorEnd
        );
        
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}