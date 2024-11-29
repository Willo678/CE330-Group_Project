package userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class DialPanel extends JPanel {
    private double score;
    private final String label;

    public DialPanel(String label) {
        this.label = label;
        this.score = 0.0;
        this.setPreferredSize(new Dimension(300, 300));
    }

    public void setScore(double score) {
        if (score < 0.0) score = 0.0;
        if (score > 1.0) score = 1.0;
        this.score = score;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;
        int centerX = width / 2;
        int centerY = height / 2 + 20;


        setBackground(new Color(20, 20, 20));
        g2d.setColor(new Color(45, 45, 45));
        g2d.fillOval(centerX - radius - 10, centerY - radius - 10, (radius + 10) * 2, (radius + 10) * 2);

        g2d.setColor(new Color(60, 60, 60));
        g2d.setStroke(new BasicStroke(8));
        g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, 180);

        g2d.setColor(new Color(0, 191, 255));
        g2d.setStroke(new BasicStroke(10));
        g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, (int) Math.round(score * 180));

        int pointerAngle = (int) Math.round(score * 180);
        double pointerRad = Math.toRadians(180 - pointerAngle);
        int pointerX = (int) (centerX + radius * 0.8 * Math.cos(pointerRad));
        int pointerY = (int) (centerY - radius * 0.8 * Math.sin(pointerRad));
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(centerX, centerY, pointerX, pointerY);

        g2d.setColor(new Color(45, 45, 45));
        g2d.fillOval(centerX - 10, centerY - 10, 20, 20);

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        int tickRadius = radius + 5;
        for (int i = 0; i <= 10; i++) {
            int tickAngle = (int) (i * 180 / 10);
            double angleRad = Math.toRadians(180 - tickAngle);
            int tickX1 = (int) (centerX + tickRadius * Math.cos(angleRad));
            int tickY1 = (int) (centerY - tickRadius * Math.sin(angleRad));
            int tickX2 = (int) (centerX + (tickRadius - 10) * Math.cos(angleRad));
            int tickY2 = (int) (centerY - (tickRadius - 10) * Math.sin(angleRad));
            g2d.drawLine(tickX1, tickY1, tickX2, tickY2);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics metrics = g2d.getFontMetrics();
        String labelText = label;
        int labelWidth = metrics.stringWidth(labelText);
        g2d.drawString(labelText, centerX - labelWidth / 2, centerY + radius + 30);

        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        String scoreText = String.format("%.1f%%", score * 100);
        int scoreWidth = metrics.stringWidth(scoreText);
        g2d.drawString(scoreText, centerX - scoreWidth / 2, centerY);
    }
}
