//
//
////！！The functionality of codeMetricsUI has been integrated into targetSelectionUI！！！
//
//package userInterface.UI_Panels;
//
//import XP_Metrics.Score;
//import XP_Metrics.XPEvaluator;
//import userInterface.MetricsTracker;
//import userInterface.ProgramWindow;
//import userInterface.UI_Widgets.DialPanelWidget;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.util.ArrayList;
//
//public class codeMetricsUI extends JPanel {
//    private final DialPanelWidget totalScoreDial;
//    private final JPanel metricsPanel;
//    private final JTextArea detailsArea;
//    private final JLabel adherenceLabel;
//
//    public codeMetricsUI(ProgramWindow parent) {
//        setLayout(new BorderLayout(10, 10));
//        setBackground(new Color(255, 255, 255));
//
//        metricsPanel = createMetricsPanel();
//        detailsArea = createDetailsArea();
//        totalScoreDial = new DialPanelWidget("Averaged Score");
//        adherenceLabel = new JLabel("Ready", SwingConstants.CENTER);
//        layoutComponents();
//    }
//
//    private JPanel createMetricsPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBackground(new Color(255, 255, 255));
//        panel.setBorder(BorderFactory.createTitledBorder(
//                BorderFactory.createLineBorder(new Color(0, 191, 255), 2),
//                "Detailed Scores",
//                TitledBorder.LEFT, TitledBorder.TOP,
//                new Font("Roboto", Font.BOLD, 16), new Color(0, 191, 255)
//        ));
//        return panel;
//    }
//
//    private JTextArea createDetailsArea() {
//        JTextArea area = new JTextArea();
//        area.setEditable(false);
//        area.setFont(new Font("Roboto", Font.PLAIN, 14));
//        area.setForeground(Color.BLACK);
//        area.setBackground(new Color(255, 255, 255));
//        area.setCaretColor(Color.BLACK);
//        return area;
//    }
//
//    private void layoutComponents() {
//        JScrollPane scrollPane = new JScrollPane(detailsArea);
//        scrollPane.setPreferredSize(new Dimension(350, 150));
//
//        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
//        leftPanel.setBackground(new Color(255, 255, 255));
//        leftPanel.add(metricsPanel, BorderLayout.NORTH);
//        leftPanel.add(scrollPane, BorderLayout.CENTER);
//        leftPanel.add(adherenceLabel, BorderLayout.SOUTH);
//
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBackground(new Color(255, 255, 255));
//        mainPanel.add(leftPanel, BorderLayout.CENTER);
//        mainPanel.add(totalScoreDial, BorderLayout.EAST);
//
//        add(mainPanel, BorderLayout.CENTER);
//        add(createBottomPanel(), BorderLayout.SOUTH);
//    }
//
//    private JPanel createBottomPanel() {
//        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        bottomPanel.setBackground(new Color(255, 255, 255));
//        adherenceLabel.setFont(new Font("Roboto", Font.BOLD, 16));
//        adherenceLabel.setForeground(new Color(0, 191, 255));
//        bottomPanel.add(adherenceLabel);
//        return bottomPanel;
//    }
//
//    public void updateMetrics() {
//
//        SwingUtilities.invokeLater(() -> {
//            updateScorePanels();
//            updateDetailsArea();
//            metricsPanel.revalidate();
//            metricsPanel.repaint();
//        });
//    }
//
//    private void updateScorePanels() {
//        metricsPanel.removeAll();
//
//        double indentationScore = MetricsTracker.getTrackedIndentationScore();
//        double classStructureScore = MetricsTracker.getTrackedClassStructureScore();
//        double methodStructureScore = MetricsTracker.getTrackedMethodStructureScore();
//        double averageScore = MetricsTracker.getTrackedAverageScore();
//        System.out.println(averageScore);
//
//        metricsPanel.add(createScorePanel("Blocks & Indenting", indentationScore, new Color(255, 165, 0)));
//        metricsPanel.add(createScorePanel("Class Structure", classStructureScore, new Color(0, 255, 0)));
//        metricsPanel.add(createScorePanel("Method Structure Length", methodStructureScore, new Color(0, 191, 255)));
//
//
//        totalScoreDial.setScore(averageScore/100);
//
//        String adherenceLevel = averageScore >= 80 ? "High adherence" :
//                averageScore >= 50 ? "Moderate adherence" :
//                        "Low adherence";
//        adherenceLabel.setText(adherenceLevel + " - " + String.format("%.1f%%", averageScore));
//    }
//
//    private int calculateScore(ArrayList<Score> scores) {
//        return Math.max(100 - scores.stream()
//                .mapToInt(s -> s.score)
//                .sum(), 0);
//    }
//
//    private JPanel createScorePanel(String label, double score, Color color) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        panel.setBackground(new Color(255, 255, 255));
//
//        JLabel scoreLabel = new JLabel(label + ": " + String.format("%.1f%%", score) + "%");
//        scoreLabel.setFont(new Font("Roboto", Font.BOLD, 18));
//        scoreLabel.setForeground(color);
//        panel.add(scoreLabel);
//
//        return panel;
//    }
//
//    private void updateDetailsArea() {
//
//
//        StringBuilder details = new StringBuilder("Issue Details:\n\n");
//        if (MetricsTracker.getFocusedFile()!=null) {
//            XPEvaluator evaluator = MetricsTracker.getTrackedEvaluator();
//
//            ArrayList<Score> indentation = evaluator.scoreIndentation;
//            ArrayList<Score> classStructure = evaluator.scoreClassStructure;
//            ArrayList<Score> codeAnalysis = evaluator.scoreMethodStructure;
//
//            appendScoreDetails(details, "Indentation", indentation);
//            appendScoreDetails(details, "Class Structure", classStructure);
//            appendScoreDetails(details, "Code Analysis", codeAnalysis);
//
//        } else {
//            details.append("Select a project file to see a detailed breakdown");
//        }
//        detailsArea.setText(details.toString());
//    }
//
//    private void appendScoreDetails(StringBuilder sb, String category, ArrayList<Score> scores) {
//        if (!scores.isEmpty()) {
//            sb.append(category).append(":\n");
//            for (Score score : scores) {
//                sb.append("- ").append(score.reason)
//                        .append(" (-").append(score.score).append(")\n");
//            }
//            sb.append("\n");
//        }
//    }
//}
