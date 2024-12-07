package userInterface;

import XP_Metrics.Score;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

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
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBackground(new Color(40, 40, 40));  // Dark background for contrast
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2),
                "Detailed Scores",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16), Color.CYAN
        ));
        return panel;
    }

    private JTextArea createDetailsArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Verdana", Font.PLAIN, 14));
        area.setForeground(Color.WHITE);
        area.setBackground(new Color(30, 30, 30));
        area.setCaretColor(Color.WHITE);
        return area;
    }

    private void layoutComponents() {
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(new Color(50, 50, 50));
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

        metricsPanel.add(createScorePanel("Blocks & Indenting", indentationScore, new Color(255, 165, 0)));  // Orange color
        metricsPanel.add(createScorePanel("Class Structure", classStructureScore, new Color(0, 255, 0)));  // Green color
        metricsPanel.add(createScorePanel("Function Length", methodLengthScore, new Color(0, 191, 255)));  // Blue color
        metricsPanel.add(createScorePanel("CamelCase", camelCaseScore, new Color(255, 20, 147)));  // Deep pink

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

    private JPanel createScorePanel(String label, int score, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(40, 40, 40));
        JLabel scoreLabel = new JLabel(label + ": " + score + "%");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(color);
        panel.add(scoreLabel);
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
