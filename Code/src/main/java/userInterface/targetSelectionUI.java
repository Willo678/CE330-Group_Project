package userInterface;

import XP_Metrics.CodeAnalysis;
import XP_Metrics.EvaluateXP;
import XP_Metrics.Score;
import utils.hintTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class targetSelectionUI extends JPanel {
    private final userInterface.codeMetricsUI metricsUI;
    private final JPanel mainPanel;
    private final JTextField pathField;
    private final JFileChooser folderSelect;
    private final CardLayout cardLayout;

    public targetSelectionUI(codeMetricsUI metricsUI) {
        this.metricsUI = metricsUI;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        setLayout(new BorderLayout());
        pathField = createPathField();
        folderSelect = new JFileChooser();
        folderSelect.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        JPanel selectionPanel = createSelectionPanel();
        mainPanel.add(selectionPanel, "selection");
        mainPanel.add(metricsUI, "metrics");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "selection");
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        JButton selectButton = createSelectButton();
        JButton confirmButton = createConfirmButton();

        addComponents(panel, gbc, selectButton, confirmButton);

        return panel;
    }

    private JTextField createPathField() {
        JTextField field = new hintTextField("Select a project directory:");
        field.setBackground(new Color(0xD1D1D1));
        field.setForeground(new Color(0x5C5C5C));
        field.setBorder(new LineBorder(Color.BLACK, 1));
        field.setFont(field.getFont().deriveFont(Font.ITALIC, 10));
        return field;
    }

    private void addComponents(JPanel panel, GridBagConstraints gbc, JButton selectButton, JButton confirmButton) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(pathField, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(selectButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(confirmButton, gbc);
    }

    private JButton createSelectButton() {
        JButton button = new JButton("SELECT");
        button.addActionListener(e -> {
            if (folderSelect.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                pathField.setText(folderSelect.getSelectedFile().getAbsolutePath());
            }
        });
        return button;
    }

    private JButton createConfirmButton() {
        JButton button = new JButton("CONFIRM");
        button.addActionListener(e -> {
            String path = pathField.getText();
            if (!path.isEmpty()) {
                try {
                    processPath(path);
                    cardLayout.show(mainPanel, "metrics");
                    SwingUtilities.invokeLater(() -> {
                        JScrollPane scrollPane = getMetricsScrollPane();
                        if (scrollPane != null) {
                            scrollPane.getVerticalScrollBar().setValue(0);
                        }
                    });
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            }
        });
        return button;
    }
    private JScrollPane getMetricsScrollPane() {
        for (Component comp : metricsUI.getComponents()) {
            if (comp instanceof JScrollPane) {
                return (JScrollPane) comp;
            }
        }
        return null;
    }

    public void processPath(String path) throws InvalidPathException {
        File file = new File(path);

        List<Map<String, Object>> detailedScores = new ArrayList<>();
        double totalScore = 0.0;

        if (file.isFile()) {
            processFile(file, detailedScores);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles((dir, name) -> name.endsWith(".java"));
            if (files != null) {
                for (File javaFile : files) {
                    processFile(javaFile, detailedScores);
                }
            }
        } else {
            throw new InvalidPathException(path, "Invalid selection");
        }

        totalScore = detailedScores.stream()
                .mapToDouble(data -> (double) data.get("fileTotalScore"))
                .sum();

        metricsUI.updateDetailedScores(detailedScores, totalScore);
    }

    private void processFile(File javaFile, List<Map<String, Object>> detailedScores) {
        try {
            EvaluateXP evaluator = new EvaluateXP(javaFile.getAbsolutePath());
            if (evaluator.bracePairs == null || evaluator.scoreIndentation == null || evaluator.scoreClassStructure == null) {
                return;
            }

            int indentationScoreTotal = evaluator.scoreIndentation.stream().mapToInt(Score::getScore).sum();
            int classStructureScoreTotal = evaluator.scoreClassStructure.stream().mapToInt(Score::getScore).sum();

            ArrayList<Score> codeAnalysisScores = CodeAnalysis.CodeAnalysis(evaluator.bracePairs);
            int codeAnalysisScoreTotal = codeAnalysisScores.stream().mapToInt(Score::getScore).sum();

            double fileTotalScore = indentationScoreTotal + classStructureScoreTotal + codeAnalysisScoreTotal;

            Map<String, Object> scoreData = new HashMap<>();
            scoreData.put("fileName", javaFile.getName());
            scoreData.put("indentationScore", evaluator.scoreIndentation);
            scoreData.put("classStructureScore", evaluator.scoreClassStructure);
            scoreData.put("analysisScores", codeAnalysisScores);
            scoreData.put("fileTotalScore", fileTotalScore);

            detailedScores.add(scoreData);
        } catch (Exception ex) {
            System.err.println("Error processing file: " + javaFile.getAbsolutePath() + " - " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}