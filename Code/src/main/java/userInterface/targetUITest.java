package userInterface;

import XP_Metrics_ReferenceVersion.indentationChecker;
import XP_Metrics_ReferenceVersion.classChecker;
import XP_Metrics_ReferenceVersion.functionChecker;
import XP_Metrics_ReferenceVersion.TokeniserTest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.InvalidPathException;

import java.util.ArrayList;

import static utils.directoryContainsJava.directoryContainsJava;
import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class targetUITest extends JFrame {
    private final int sizeX = 350;
    private final int sizeY = 650;
    private final String title = "Project Selection";

    private String analyzeJavaFile(File javaFile) {
        StringBuilder result = new StringBuilder();
        try {

            TokeniserTest tokeniser = new TokeniserTest();
            ArrayList<TokeniserTest.Token> tokens = tokeniser.preprocess(javaFile.getAbsolutePath());

            double classScore = classChecker.checkClassStructure(tokens);
            double functionScore = functionChecker.checkFunctionStructure(tokens);
            double indentationScore = indentationChecker.checkIndentation(tokens);
            double XPdiness = (classScore * 0.5) + (functionScore * 0.25) + (indentationScore * 0.25);

            result.append("File: ").append(javaFile.getName()).append("\n");
            result.append("  Class Structure Score      : ").append(classScore).append("\n");
            result.append("  Function Structure Score   : ").append(functionScore).append("\n");
            result.append("  Indentation Structure Score: ").append(indentationScore).append("\n");
            result.append("  Overall XPdiness Score     : ").append(XPdiness).append("\n\n");
        } catch (Exception e) {
            result.append("Error analyzing file: ").append(javaFile.getName()).append("\n");
            result.append(e.getMessage()).append("\n\n");
        }
        return result.toString();
    }
    public static ArrayList<File> getAllJavaFile(File directory) {
        ArrayList<File> javaFiles = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        javaFiles.addAll(getAllJavaFile(file));
                    } else if (file.getName().endsWith(".java")) {
                        javaFiles.add(file);
                    }
                }
            }
        }
        return javaFiles;
    }

    public targetUITest() {
        this(0, 0);
    }

    public targetUITest(int width, int height) {
        this.setTitle(title);
        this.setSize(sizeX, sizeY);
        this.setLayout(new GridBagLayout());

        JFileChooser folderSelect = new JFileChooser();
        folderSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        GridBagConstraints gbc = new GridBagConstraints();

        // Holds components
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(new EmptyBorder(0, 30, 0, 30));
        if (height <= 0 || width <= 0) {
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            this.add(selectionPanel, gbc);
        } else {
            selectionPanel.setSize(width, height);
            this.add(selectionPanel);
        }

        gbc = new GridBagConstraints();

        // Text field, user can manually enter a path, or select one using the dialogue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        JTextField filePath = new JTextField("Select a project directory:");
        selectionPanel.add(filePath, gbc);
        filePath.setBackground(new Color(0xD1D1D1));
        filePath.setForeground(new Color(0x5C5C5C));
        filePath.setBorder(new LineBorder(new Color(0), 1));
        filePath.setFont(filePath.getFont().deriveFont(Font.ITALIC, 10));
        filePath.setMargin(new Insets(3, 10, 3, 0));

        // Button to trigger the file select dialogue
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton selectButton = new JButton("SELECT");
        selectionPanel.add(selectButton, gbc);
        selectButton.addActionListener(_ -> {
            int returnVal = folderSelect.showOpenDialog(selectionPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filePath.setText(folderSelect.getSelectedFile().getAbsolutePath());
            }
        });

        // Button to confirm selection and pass on the path to other areas of the project
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        JButton confirmButton = new JButton("CONFIRM");
        selectionPanel.add(confirmButton, gbc);
        confirmButton.addActionListener(_ -> {
            String path = filePath.getText();
            if (!path.isEmpty()) {
                try {
                    File projectFile = new File(path);
                    if (!projectFile.exists()) {
                        throw new InvalidPathException(path, "Path does not exist.");
                    }

                    StringBuilder result = new StringBuilder("=== XPdiness Analysis Results ===\n");

                    if (projectFile.isDirectory()) {
                        // If it is a directory, check whether it contains Java files
                        if (!directoryContainsJava(projectFile)) {
                            throw new InvalidPathException(path, "The directory does not contain any Java files.");
                        }

                        ArrayList<String> subdirectories = getJavaSubdirectories(projectFile);
                        if (subdirectories == null || subdirectories.isEmpty()) {
                            JOptionPane.showMessageDialog(this,
                                    "No subdirectories with Java files found.",
                                    "Analysis Result",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        for (String subdirectory : subdirectories) {
                            ArrayList<File> javaFiles = getAllJavaFile(new File(subdirectory));
                            for (File javaFile : javaFiles) {
                                result.append(analyzeJavaFile(javaFile));
                            }
                        }

                    } else if (projectFile.isFile() && projectFile.getName().endsWith(".java")) {
                        result.append(analyzeJavaFile(projectFile));

                    } else {
                        throw new InvalidPathException(path, "Path is not a valid Java file or directory.");
                    }

                    // Show result
                    JOptionPane.showMessageDialog(this, result.toString(), "Analysis Complete", JOptionPane.INFORMATION_MESSAGE);

                } catch (InvalidPathException e) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid path: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "An unexpected error occurred: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new targetUITest().setVisible(true);
    }
}