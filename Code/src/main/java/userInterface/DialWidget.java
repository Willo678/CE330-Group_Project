package userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialWidget extends JPanel {
    private int angle;

    public DialWidget() {
        this.angle = 0;
        setPreferredSize(new Dimension(350, 350));
    }

    public void setAngle(int angle) {
        this.angle = Math.min(180, Math.max(0, angle));
        repaint();
    }

    public void updateXPdiness(int xpValue) {
        int angle = (int) (xpValue * 1.8);
        setAngle(angle);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;

        GradientPaint gradient = new GradientPaint(
                width / 2 - radius, height / 2 - radius, Color.WHITE,
                width / 2 + radius, height / 2 + radius, Color.LIGHT_GRAY
        );
        g2d.setPaint(gradient);
        g2d.fillOval(width / 2 - radius, height / 2 - radius, radius * 2, radius * 2);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(width / 2 - radius, height / 2 - radius, radius * 2, radius * 2);

        for (int i = 0; i <= 180; i += 30) {
            double rad = Math.toRadians(i - 90);
            int x1 = (int) (width / 2 + radius * Math.cos(rad));
            int y1 = (int) (height / 2 + radius * Math.sin(rad));
            int x2 = (int) (width / 2 + (radius - 20) * Math.cos(rad));
            int y2 = (int) (height / 2 + (radius - 20) * Math.sin(rad));
            g2d.drawLine(x1, y1, x2, y2);
        }

        double pointerRad = Math.toRadians(angle - 90);
        int pointerX = (int) (width / 2 + (radius - 30) * Math.cos(pointerRad));
        int pointerY = (int) (height / 2 + (radius - 30) * Math.sin(pointerRad));
        g2d.setColor(Color.RED);
        g2d.drawLine(width / 2, height / 2, pointerX, pointerY);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String angleText = "Angle: " + angle + "°";
        int textWidth = g2d.getFontMetrics().stringWidth(angleText);
        g2d.drawString(angleText, (width - textWidth) / 2, height / 2 - radius - 10);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dial Widget");
        frame.setLayout(new BorderLayout());

        DialWidget dial = new DialWidget();
        frame.add(dial, BorderLayout.CENTER);

        JSlider slider = new JSlider(0, 180, 0);
        slider.setPreferredSize(new Dimension(300, 50));
        slider.setFont(new Font("Arial", Font.BOLD, 18));
        slider.addChangeListener(e -> dial.setAngle(slider.getValue()));
        frame.add(slider, BorderLayout.SOUTH);

        JFileChooser folderSelect = new JFileChooser();
        folderSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField filePath = new JTextField("Select a project directory:");
        filePath.setFont(new Font("Arial", Font.PLAIN, 18));
        filePath.setPreferredSize(new Dimension(300, 40));
        panel.add(filePath);

        JButton selectButton = new JButton("SELECT");
        selectButton.setFont(new Font("Arial", Font.BOLD, 18));
        selectButton.setPreferredSize(new Dimension(300, 50));
        selectButton.setBackground(Color.CYAN);
        selectButton.addActionListener(e -> {
            int returnVal = folderSelect.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filePath.setText(folderSelect.getSelectedFile().getAbsolutePath());
            }
        });
        panel.add(selectButton);

        JButton confirmButton = new JButton("CONFIRM");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 18));
        confirmButton.setPreferredSize(new Dimension(300, 50));
        confirmButton.setBackground(Color.GREEN);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = filePath.getText();
                if (!path.isEmpty()) {
                    int xpValue = 50;
                    dial.updateXPdiness(xpValue);
                    System.out.println("XP符合度: " + xpValue);
                }
            }
        });
        panel.add(confirmButton);

        frame.add(panel, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
}
