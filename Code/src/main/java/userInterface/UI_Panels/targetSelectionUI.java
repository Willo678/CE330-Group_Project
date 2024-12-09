package userInterface.UI_Panels;

import userInterface.MetricsTracker;
import userInterface.ProgramWindow;
import userInterface.UI_Widgets.DialPanelWidget;
import userInterface.UI_Widgets.FileSelector;
import userInterface.UI_Widgets.ProjectSelector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
        totalScoreDial = new DialPanelWidget("Averaged Score");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;

        ProjectSelector projectSelector = new ProjectSelector();
        projectSelector.addActionListener(e -> {
            String projectPath = MetricsTracker.getProjectPath();
            System.out.println("Selected Project: " + projectPath);
            parent.updateStatus();
            fileSelector.setVisible(MetricsTracker.trackerExists());
        });
        this.add(projectSelector, gbc);


        gbc.gridy = 1;
        fileSelector.addActionListener(e -> updateMetrics());
        fileSelector.setVisible(false);
        this.add(fileSelector, gbc);


        gbc.gridy = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1;
        JPanel metricsContainer = new JPanel(new BorderLayout(10, 10));
        metricsContainer.add(metricsPanel, BorderLayout.NORTH);
        metricsContainer.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        metricsContainer.add(adherenceLabel, BorderLayout.SOUTH);

        JPanel mainMetricsPanel = new JPanel(new BorderLayout());
        mainMetricsPanel.add(metricsContainer, BorderLayout.CENTER);
        mainMetricsPanel.add(totalScoreDial, BorderLayout.EAST);
        this.add(mainMetricsPanel, gbc);
    }


    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 191, 255), 2),
                "Detailed Scores",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Roboto", Font.BOLD, 16), new Color(0, 191, 255)
        ));
        return panel;
    }


    private JTextArea createDetailsArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Roboto", Font.PLAIN, 14));
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
            metricsPanel.add(createScorePanel("Class Structure", classStructureScore, Color.GREEN));
            metricsPanel.add(createScorePanel("Method Structure Length", methodStructureScore, Color.BLUE));

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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel scoreLabel = new JLabel(label + ": " + String.format("%.1f%%", score));
        scoreLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        scoreLabel.setForeground(color);
        panel.add(scoreLabel);
        return panel;
    }
}