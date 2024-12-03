package userInterface;

import XP_Metrics.Score;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class codeMetricsUI extends JPanel {
    private final DialPanel totalScoreDial;
    private final JPanel metricsPanel;
    private final JTextArea detailsArea;

    public codeMetricsUI() {
        setLayout(new BorderLayout(10, 10));

        metricsPanel = new JPanel();
        metricsPanel.setLayout(new GridLayout(3, 1, 5, 5));
        metricsPanel.setBorder(BorderFactory.createTitledBorder("Detailed Scores"));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        totalScoreDial = new DialPanel("Overall Score");

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(metricsPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.CENTER);
        add(totalScoreDial, BorderLayout.EAST);
    }

    public void updateMetrics(ArrayList<Score> indentation, ArrayList<Score> classStructure, ArrayList<Score> codeAnalysis) {
        metricsPanel.removeAll();
        detailsArea.setText("");

        int indentationScore = Math.max(100 - sumScores(indentation), 0);
        int classStructureScore = Math.max(100 - sumScores(classStructure), 0);
        int codeAnalysisScore = Math.max(100 - sumScores(codeAnalysis), 0);

        metricsPanel.add(createScorePanel("Indentation", indentationScore, indentation));
        metricsPanel.add(createScorePanel("Class Structure", classStructureScore, classStructure));
        metricsPanel.add(createScorePanel("Code Analysis", codeAnalysisScore, codeAnalysis));

        double averageScore = (indentationScore + classStructureScore + codeAnalysisScore) / 300.0;
        totalScoreDial.setScore(averageScore);

        StringBuilder details = new StringBuilder("Issue Details:\n\n");
        appendScoreDetails(details, "Indentation", indentation);
        appendScoreDetails(details, "Class Structure", classStructure);
        appendScoreDetails(details, "Code Analysis", codeAnalysis);
        detailsArea.setText(details.toString());

        revalidate();
        repaint();
    }

    private JPanel createScorePanel(String label, int score, ArrayList<Score> details) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label + ": " + score + "%"));
        return panel;
    }

    private void appendScoreDetails(StringBuilder sb, String category, ArrayList<Score> scores) {
        if (!scores.isEmpty()) {
            sb.append(category).append(":\n");
            for (Score score : scores) {
                sb.append("- ").append(score.reason).append(" (-").append(score.score).append(")\n");
            }
            sb.append("\n");
        }
    }

    private int sumScores(ArrayList<Score> scores) {
        return scores.stream().mapToInt(s -> s.score).sum();
    }
}