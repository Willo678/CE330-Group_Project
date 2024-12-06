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
import java.nio.file.Paths;
import java.util.ArrayList;

import static utils.directoryContainsJava.directoryContainsJava;
import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class targetSelectionUI extends JPanel {
    private final userInterface.codeMetricsUI metricsUI;
    private final JTextField pathField;
    private final JFileChooser folderSelect;

    public targetSelectionUI(codeMetricsUI metricsUI) {
        this.metricsUI = metricsUI;
        setLayout(new BorderLayout());

        pathField = createPathField();
        folderSelect = new JFileChooser();
        folderSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        add(createSelectionPanel(), BorderLayout.NORTH);
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
        field.setMargin(new Insets(3, 10, 3, 0));
        return field;
    }

    private void addComponents(JPanel panel, GridBagConstraints gbc,
                               JButton selectButton, JButton confirmButton) {
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
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            }
        });
        return button;
    }

    public void processPath(String path) throws InvalidPathException {
        Paths.get(path);
        File directory = new File(path);

        if (!directoryContainsJava(directory)) {
            throw new InvalidPathException(path, "No Java files found");
        }

        ArrayList<String> filePaths = getJavaSubdirectories(directory);
        if (filePaths.isEmpty()) {
            throw new InvalidPathException(path, "No valid Java files found in subdirectories");
        }

        for (String filePath : filePaths) {
            try {
                EvaluateXP evaluator = new EvaluateXP(filePath);
                if (evaluator.bracePairs == null ||
                        evaluator.scoreIndentation == null ||
                        evaluator.scoreClassStructure == null) {
                    continue;  // Skip files that failed to evaluate
                }

                ArrayList<Score> codeAnalysisScores = CodeAnalysis.CodeAnalysis(evaluator.bracePairs);
                if (codeAnalysisScores != null) {
                    metricsUI.updateMetrics(
                            evaluator.scoreIndentation,
                            evaluator.scoreClassStructure,
                            codeAnalysisScores
                    );
                }
            } catch (Exception ex) {
                System.err.println("Error processing file: " + filePath + " - " + ex.getMessage());
                continue;  // Skip problematic files and continue with others
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                "Error: " + message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}