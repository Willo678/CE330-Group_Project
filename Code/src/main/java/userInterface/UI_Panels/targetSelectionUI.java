package userInterface.UI_Panels;

import userInterface.MetricsTracker;
import userInterface.ProgramWindow;
import userInterface.UI_Widgets.DialPanelWidget;
import userInterface.UI_Widgets.FileSelector;
import userInterface.UI_Widgets.ProjectSelector;

import javax.swing.*;
import java.awt.*;

public class targetSelectionUI extends JPanel {
    private final ProgramWindow parent;
    private final FileSelector fileSelector;

    private final JPanel metricsPanel;
    private final JTextArea detailsArea;
    private final JLabel adherenceLabel;
    private final DialPanelWidget totalScoreDial;

    public targetSelectionUI(ProgramWindow parent) {
        this.parent = parent;

        fileSelector = new FileSelector();

        metricsPanel = createMetricsPanel();
        detailsArea = createDetailsArea();
        adherenceLabel = new JLabel("Ready", SwingConstants.CENTER);
        adherenceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalScoreDial = new DialPanelWidget("Averaged Score");

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(250, 250, 245));

        JPanel selectionPanel = createSelectionPanel();
        add(selectionPanel, BorderLayout.NORTH);

        JPanel metricsContainer = createMetricsContainer();
        add(metricsContainer, BorderLayout.CENTER);
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(245, 240, 230)); // 暖灰色背景

        JLabel title = new JLabel("Project Selection");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(90, 90, 90)); // 深灰色字体
        panel.add(title, BorderLayout.NORTH);

        ProjectSelector projectSelector = new ProjectSelector();
        projectSelector.addActionListener(e -> {
            MetricsTracker.selectProject(MetricsTracker.getProjectPath());
            fileSelector.setVisible(MetricsTracker.trackerExists());
        });
        panel.add(projectSelector, BorderLayout.CENTER);

        fileSelector.setVisible(false);
        fileSelector.addActionListener(e -> updateMetrics());
        panel.add(fileSelector, BorderLayout.SOUTH);

        return panel;
    }

    // 创建 Metrics 容器
    private JPanel createMetricsContainer() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(245, 245, 240)); // 浅灰色背景

        JLabel title = new JLabel("Metrics Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(90, 90, 90));
        panel.add(title, BorderLayout.NORTH);

        JPanel metricsInfoPanel = new JPanel(new BorderLayout(10, 10));
        metricsInfoPanel.setBackground(new Color(255, 255, 250)); // 更亮的背景
        metricsInfoPanel.add(metricsPanel, BorderLayout.NORTH);
        metricsInfoPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        metricsInfoPanel.add(adherenceLabel, BorderLayout.SOUTH);

        panel.add(metricsInfoPanel, BorderLayout.CENTER);
        panel.add(totalScoreDial, BorderLayout.EAST);

        return panel;
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 250)); // 柔和白色背景
        panel.setAlignmentX(LEFT_ALIGNMENT);
        return panel;
    }

    private JTextArea createDetailsArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setBackground(new Color(255, 250, 245)); // 带点暖意的背景
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        area.setForeground(new Color(90, 90, 90)); // 深灰色字体
        return area;
    }

    private void updateMetrics() {
        SwingUtilities.invokeLater(() -> {
            metricsPanel.removeAll();

            double indentationScore = MetricsTracker.getTrackedIndentationScore();
            double classStructureScore = MetricsTracker.getTrackedClassStructureScore();
            double methodStructureScore = MetricsTracker.getTrackedMethodStructureScore();
            double averageScore = MetricsTracker.getTrackedAverageScore();

            metricsPanel.add(createScorePanel("Blocks & Indenting", indentationScore, Color.ORANGE));
            metricsPanel.add(createScorePanel("Class Structure", classStructureScore, new Color(70, 150, 70)));
            metricsPanel.add(createScorePanel("Method Structure Length", methodStructureScore, new Color(70, 100, 200)));

            totalScoreDial.setScore(averageScore / 100.0);
            adherenceLabel.setText(averageScore >= 80 ? "High adherence"
                    : averageScore >= 50 ? "Moderate adherence"
                    : "Low adherence");

            StringBuilder details = new StringBuilder("Issue Details:\n\n");
            details.append("Indentation: ").append(indentationScore).append("%\n")
                    .append("Class Structure: ").append(classStructureScore).append("%\n")
                    .append("Method Structure Length: ").append(methodStructureScore).append("%\n");

            detailsArea.setText(details.toString());

            metricsPanel.revalidate();
            metricsPanel.repaint();
        });
    }

    private JPanel createScorePanel(String label, double score, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(255, 255, 250));

        JLabel scoreLabel = new JLabel(label + ": " + String.format("%.1f%%", score));
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setForeground(color);
        panel.add(scoreLabel);

        return panel;
    }
}