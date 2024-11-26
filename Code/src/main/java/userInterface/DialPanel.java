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

        setBackground(Color.WHITE);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;
        int x = (width - radius * 2) / 2;
        int y = (height - radius) / 2;

        g2d.setColor(Color.BLACK);
        int numberOfTicks = 11;
        for (int i = 0; i < numberOfTicks; i++) {
            int tickAngle = (int) (i * 180 / (numberOfTicks - 1));
            double angleRad = Math.toRadians(tickAngle);
            int startX = (int) (width / 2 + radius * Math.cos(angleRad));
            int startY = (int) (height / 2 + radius * Math.sin(angleRad));
            int endX = (int) (width / 2 + (radius + 10) * Math.cos(angleRad));
            int endY = (int) (height / 2 + (radius + 10) * Math.sin(angleRad));
            g2d.drawLine(startX, startY, endX, endY);

            String tickLabel = String.valueOf(i * 10);
            g2d.drawString(tickLabel, endX - 10, endY - 5);
        }

        int pointerAngle = (int) (score * 180);
        double pointerRad = Math.toRadians(pointerAngle);
        int pointerX = (int) (width / 2 + (radius - 10) * Math.cos(pointerRad));
        int pointerY = (int) (height / 2 + (radius - 10) * Math.sin(pointerRad));
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(width / 2, height / 2, pointerX, pointerY);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();
        int labelWidth = metrics.stringWidth(label);
        g2d.drawString(label, (width - labelWidth) / 2, height / 3);

        String scoreText = String.format("%.2f", score * 100);
        int scoreWidth = metrics.stringWidth(scoreText);
        g2d.drawString(scoreText, (width - scoreWidth) / 2, height / 2 + 10);
    }
}
