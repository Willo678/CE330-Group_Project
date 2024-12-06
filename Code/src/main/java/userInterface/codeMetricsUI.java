package userInterface;


import XP_Metrics.Score;


import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class codeMetricsUI extends JPanel {
    private final DialPanel totalScoreDial;
    private final JPanel metricsPanel;
    private final JTextArea detailsArea;
    private final JLabel adherenceLabel;

    public codeMetricsUI() {
        setLayout(new BorderLayout(10, 10));

        metricsPanel = createMetricsPanel();
        detailsArea = createDetailsArea();
        totalScoreDial = new DialPanel("Overall Score");
        adherenceLabel = new JLabel("Ready", SwingConstants.CENTER);
        layoutComponents();
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Detailed Scores"));
        return panel;
    }

    private JTextArea createDetailsArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        return area;
    }

    private void layoutComponents() {
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(metricsPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(adherenceLabel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.CENTER);
        add(totalScoreDial, BorderLayout.EAST);
    }

    public void updateMetrics(ArrayList<Score> indentation,
                              ArrayList<Score> classStructure,
                              ArrayList<Score> codeAnalysis) {
        if (indentation == null || classStructure == null || codeAnalysis == null) {
            throw new IllegalArgumentException("Score lists cannot be null");
        }

        SwingUtilities.invokeLater(() -> {
            updateScorePanels(indentation, classStructure, codeAnalysis);
            updateDetailsArea(indentation, classStructure, codeAnalysis);
            metricsPanel.revalidate();
            metricsPanel.repaint();
        });
    }

    private void updateScorePanels(ArrayList<Score> indentation,
                                   ArrayList<Score> classStructure,
                                   ArrayList<Score> codeAnalysis) {
        metricsPanel.removeAll();

        int indentationScore = calculateScore(indentation);
        int classStructureScore = calculateScore(classStructure);

        ArrayList<Score> methodScores = new ArrayList<>();
        ArrayList<Score> namingScores = new ArrayList<>();

        for (Score s : codeAnalysis) {
            if (s.reason.contains("too big") || s.reason.contains("nested") || s.reason.contains("statement")) {
                methodScores.add(s);
            } else {
                namingScores.add(s);
            }
        }

        int methodLengthScore = calculateScore(methodScores);
        int camelCaseScore = calculateScore(namingScores);

        metricsPanel.setLayout(new GridLayout(4, 1, 5, 5));
        metricsPanel.add(createScorePanel("Blocks & Indenting", indentationScore));
        metricsPanel.add(createScorePanel("Class Structure", classStructureScore));
        metricsPanel.add(createScorePanel("Function Length", methodLengthScore));
        metricsPanel.add(createScorePanel("CamelCase", camelCaseScore));

        double averageScore = (indentationScore + classStructureScore + methodLengthScore + camelCaseScore) / 4.0 / 100.0;
        totalScoreDial.setScore(averageScore);

        String adherenceLevel = averageScore >= 0.8 ? "High adherence" :
                averageScore >= 0.5 ? "Moderate adherence" :
                        "Low adherence";
        adherenceLabel.setText(adherenceLevel + " - " + String.format("%.1f%%", averageScore * 100));
    }


    private int calculateScore(ArrayList<Score> scores) {
        return Math.max(100 - scores.stream()
                .mapToInt(s -> s.score)
                .sum(), 0);
    }

    private JPanel createScorePanel(String label, int score) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label + ": " + score + "%"));
        return panel;
    }

    private void updateDetailsArea(ArrayList<Score> indentation,
                                   ArrayList<Score> classStructure,
                                   ArrayList<Score> codeAnalysis) {
        StringBuilder details = new StringBuilder("Issue Details:\n\n");
        appendScoreDetails(details, "Indentation", indentation);
        appendScoreDetails(details, "Class Structure", classStructure);
        appendScoreDetails(details, "Code Analysis", codeAnalysis);
        detailsArea.setText(details.toString());
    }

    private void appendScoreDetails(StringBuilder sb, String category, ArrayList<Score> scores) {
        if (!scores.isEmpty()) {
            sb.append(category).append(":\n");
            for (Score score : scores) {
                sb.append("- ").append(score.reason)
                        .append(" (-").append(score.score).append(")\n");
            }
            sb.append("\n");
        }
    }

}