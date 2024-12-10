package userInterface.UI_Widgets;

import javax.swing.*;
import java.awt.*;

public class DialPanelWidget extends JPanel {
    private double score;
    private final String label;

    public DialPanelWidget(String label) {
        this.label = label;
        this.score = 0.0;
        this.setPreferredSize(new Dimension(350, 350));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 191, 255), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(new Color(30, 30, 30));

        this.getAccessibleContext().setAccessibleName(label);
    }

    public void setScore(double score) {
        this.score = Math.max(0.0, Math.min(score, 1.0));
        repaint();
    }

    public double getScore() {
        return score;
    }

    public String getLabel() {
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 30;
        int centerX = width / 2;
        int centerY = height / 2;

        g2d.setColor(new Color(50, 50, 50));
        g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, 180);

        g2d.setColor(new Color(0, 191, 255));
        g2d.setStroke(new BasicStroke(10));
        g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 180, -(int) (score * 180));

        double angleRad = -Math.toRadians(180 - score * 180);
        int pointerX = (int) (centerX + radius * 0.9 * Math.cos(angleRad));
        int pointerY = (int) (centerY + radius * 0.9 * Math.sin(angleRad));
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(centerX, centerY, pointerX, pointerY);

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        int tickRadius = radius - 10;
        for (int i = 0; i <= 10; i++) {
            double tickAngleRad = Math.toRadians(180 - i * 18);
            int tickX1 = (int) (centerX + tickRadius * Math.cos(tickAngleRad));
            int tickY1 = (int) (centerY - tickRadius * Math.sin(tickAngleRad));
            int tickX2 = (int) (centerX + (tickRadius - 10) * Math.cos(tickAngleRad));
            int tickY2 = (int) (centerY - (tickRadius - 10) * Math.sin(tickAngleRad));
            g2d.drawLine(tickX1, tickY1, tickX2, tickY2);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics metrics = g2d.getFontMetrics();
        int labelWidth = metrics.stringWidth(label);
        g2d.drawString(label, centerX - labelWidth / 2, centerY + radius + 20);

        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        String scoreText = String.format("%.1f%%", score * 100);
        int scoreWidth = metrics.stringWidth(scoreText);
        g2d.drawString(scoreText, centerX - scoreWidth / 2, centerY);
    }
}
