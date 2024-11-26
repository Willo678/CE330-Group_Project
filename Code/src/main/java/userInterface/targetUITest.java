package userInterface;

import XP_Metrics.EvaluateXP;
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
    private final String title = "XPdiness Analysis and Dashboard";

    private String analyzeJavaFile(File javaFile) {
        try {
            TokeniserTest tokeniser = new TokeniserTest();
            ArrayList<TokeniserTest.Token> tokens = tokeniser.preprocess(javaFile.getAbsolutePath());

            double classScore = classChecker.checkClassStructure(tokens);
            double functionScore = functionChecker.checkFunctionStructure(tokens);
            double indentationScore = indentationChecker.checkIndentation(tokens);
            double XPdiness = (classScore * 0.5) + (functionScore * 0.25) + (indentationScore * 0.25);


            displayDials(classScore, functionScore, indentationScore, XPdiness);

        } catch (Exception e) {
        }
        return "";
    }

    private void displayDials(double classScore, double functionScore, double indentationScore, double totalScore) {
        JFrame dashboardFrame = new JFrame("XPdiness Dashboard");
        dashboardFrame.setSize(600, 600);
        dashboardFrame.setLayout(new GridLayout(4, 1));

        DialPanel classDial = new DialPanel("Class Structure Score");
        DialPanel functionDial = new DialPanel("Function Structure Score");
        DialPanel indentationDial = new DialPanel("Indentation Structure Score");

        dashboardFrame.add(classDial);
        dashboardFrame.add(functionDial);
        dashboardFrame.add(indentationDial);

        JPanel totalPanel = new JPanel();
        JLabel totalScoreLabel = new JLabel("Overall XPdiness Score: " + totalScore);
        totalPanel.add(totalScoreLabel);
        dashboardFrame.add(totalPanel);

        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setVisible(true);

        classDial.setScore(classScore / 100.0);
        functionDial.setScore(functionScore / 100.0);
        indentationDial.setScore(indentationScore / 100.0);

        dashboardFrame.revalidate();
        dashboardFrame.repaint();
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
        folderSelect.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        GridBagConstraints gbc = new GridBagConstraints();

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        JTextField filePath = new JTextField("Select a project directory:");
        selectionPanel.add(filePath, gbc);
        filePath.setBackground(Color.WHITE);
        filePath.setForeground(Color.BLACK);
        filePath.setBorder(new LineBorder(new Color(0), 1));
        filePath.setFont(filePath.getFont().deriveFont(Font.PLAIN, 12));
        filePath.setMargin(new Insets(3, 10, 3, 0));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JButton selectButton = new JButton("SELECT");
        selectionPanel.add(selectButton, gbc);
        selectButton.addActionListener(A -> {
            int returnVal = folderSelect.showOpenDialog(selectionPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filePath.setText(folderSelect.getSelectedFile().getAbsolutePath());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        JButton confirmButton = new JButton("CONFIRM");
        selectionPanel.add(confirmButton, gbc);
        confirmButton.addActionListener(A -> {
            String path = filePath.getText();
            if (!path.isEmpty()) {
                try {
                    File projectFile = new File(path);
                    if (!projectFile.exists()) {
                        throw new InvalidPathException(path, "Path does not exist.");
                    }

                    if (projectFile.isDirectory()) {
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
                                analyzeJavaFile(javaFile); // 仅通过仪表盘显示
                            }
                        }

                    } else if (projectFile.isFile() && projectFile.getName().endsWith(".java")) {
                        analyzeJavaFile(projectFile); // 仅通过仪表盘显示

                    } else {
                        throw new InvalidPathException(path, "Path is not a valid Java file or directory.");
                    }

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
