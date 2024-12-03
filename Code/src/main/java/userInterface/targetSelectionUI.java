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

public class targetSelectionUI extends JFrame {
    private final userInterface.codeMetricsUI metricsUI;
    private final JTextField pathField;
    private final JFileChooser folderSelect;

    public targetSelectionUI() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        metricsUI = createMetricsWindow();
        pathField = createPathField();
        folderSelect = new JFileChooser();
        folderSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        add(createSelectionPanel(), BorderLayout.NORTH);
        setupWindow();
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

    private codeMetricsUI createMetricsWindow() {
        codeMetricsUI metricsUI = new userinterface.codeMetricsUI();
        JFrame frame = new JFrame("Code Metrics");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(metricsUI);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return metricsUI;
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

    private void processPath(String path) throws InvalidPathException {
        Paths.get(path);
        File directory = new File(path);

        if (!directoryContainsJava(directory)) {
            throw new InvalidPathException(path, "No Java files found");
        }

        for (String filePath : getJavaSubdirectories(directory)) {
            EvaluateXP evaluator = new EvaluateXP(filePath);
            ArrayList<Score> codeAnalysisScores = CodeAnalysis.CodeAnalysis(evaluator.bracePairs);
            metricsUI.updateMetrics(
                    evaluator.scoreIndentation,
                    evaluator.scoreClassStructure,
                    codeAnalysisScores
            );
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                "Error: " + message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void setupWindow() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}