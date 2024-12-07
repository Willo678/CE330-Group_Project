package userInterface;

import XP_Metrics.Score;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

public class codeMetricsUI extends JPanel {
    private final DialPanel totalScoreDial;
    private final JPanel metricsPanel;
    private final JTextArea detailsArea;
    private final JLabel adherenceLabel;

    public codeMetricsUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 255, 255));

        metricsPanel = createMetricsPanel();
        detailsArea = createDetailsArea();
        totalScoreDial = new DialPanel("Overall Score");
        adherenceLabel = new JLabel("Ready", SwingConstants.CENTER);
        layoutComponents();
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 191, 255), 3),
                "Detailed Scores",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Roboto", Font.BOLD, 18), new Color(0, 191, 255)
        ));
        return panel;
    }

    private JTextArea createDetailsArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Roboto", Font.PLAIN, 16));
        area.setForeground(Color.BLACK);
        area.setBackground(new Color(240, 240, 240));
        area.setCaretColor(Color.BLACK);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private void layoutComponents() {
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 191, 255), 2));

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(new Color(255, 255, 255));
        leftPanel.add(metricsPanel, BorderLayout.CENTER);
        leftPanel.add(scrollPane, BorderLayout.SOUTH);
        leftPanel.add(adherenceLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(totalScoreDial, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(255, 255, 255));
        adherenceLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        adherenceLabel.setForeground(new Color(0, 191, 255));
        bottomPanel.add(adherenceLabel);
        return bottomPanel;
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

        metricsPanel.add(createScorePanel("Blocks & Indentation", indentationScore, new Color(255, 165, 0)));
        metricsPanel.add(createScorePanel("Class Structure", classStructureScore, new Color(0, 255, 0)));
        metricsPanel.add(createScorePanel("Function Length", methodLengthScore, new Color(0, 191, 255)));
        metricsPanel.add(createScorePanel("CamelCase", camelCaseScore, new Color(255, 20, 147)));

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
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(255, 255, 255));

        JLabel scoreLabel = new JLabel(label + ": " + score + "%");
        scoreLabel.setFont(new Font("Roboto", Font.BOLD, 20));
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

    public void updateDetailedScores(List<Map<String, Object>> detailedScores, double totalScore) {
        metricsPanel.removeAll();

        for (Map<String, Object> scoreData : detailedScores) {
            String fileName = (String) scoreData.get("fileName");
            @SuppressWarnings("unchecked")
            ArrayList<Score> indentationScores = (ArrayList<Score>) scoreData.get("indentationScore");
            @SuppressWarnings("unchecked")
            ArrayList<Score> classStructureScores = (ArrayList<Score>) scoreData.get("classStructureScore");
            @SuppressWarnings("unchecked")
            ArrayList<Score> analysisScores = (ArrayList<Score>) scoreData.get("analysisScores");
            double fileTotalScore = (double) scoreData.get("fileTotalScore");

            JPanel filePanel = new JPanel();
            filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
            filePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 191, 255)), fileName));

            filePanel.add(createScoreLabel("Indentation Scores", indentationScores));
            filePanel.add(createScoreLabel("Class Structure Scores", classStructureScores));
            filePanel.add(createScoreLabel("Analysis Scores", analysisScores));
            filePanel.add(new JLabel("File Total Score: " + fileTotalScore));

            metricsPanel.add(filePanel);//
        }

        JLabel totalScoreLabel = new JLabel("Total Score: " + totalScore);
        totalScoreLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        totalScoreLabel.setForeground(new Color(0, 191, 255));

        metricsPanel.add(totalScoreLabel);

        metricsPanel.revalidate();
        metricsPanel.repaint();
    }

    private JPanel createScoreLabel(String label, ArrayList<Score> scores) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(255, 255, 255));

        JLabel titleLabel = new JLabel(label + ": ");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        panel.add(titleLabel);

        JLabel scoresLabel = new JLabel(scores.stream()
                .map(score -> score.getScore() + " (" + score.getReason() + ")")
                .collect(Collectors.joining(", ")));
        scoresLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        panel.add(scoresLabel);

        return panel;
    }
}
