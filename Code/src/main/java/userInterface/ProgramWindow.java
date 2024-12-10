package userInterface;

import userInterface.UI_Panels.sourceCodeDisplayUI;
import userInterface.UI_Panels.targetSelectionUI;

import javax.swing.*;
import java.awt.*;

import static utils.trimFrontText.trimFrontText;

public class ProgramWindow extends JFrame {
    private final JTabbedPane tabbedPane;
    protected final targetSelectionUI targetSelectionUI;
    protected final sourceCodeDisplayUI sourceCodeDisplayUI;

    private final JPanel statusBar;
    private final JLabel statusLabelTab;
    private final JLabel statusLabelProject;
    private final JLabel statusLabelFile;

    public ProgramWindow() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();

        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabelTab = createStatusLabel("Current tab");
        statusLabelProject = createStatusLabel("Selected Project");
        statusLabelFile = createStatusLabel("Selected File");

        statusBar.add(statusLabelTab);
        statusBar.add(createVerticalSeparator(statusBar));
        statusBar.add(statusLabelProject);
        statusBar.add(createVerticalSeparator(statusBar));
        statusBar.add(statusLabelFile);
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        targetSelectionUI = new targetSelectionUI(this);
        sourceCodeDisplayUI = new sourceCodeDisplayUI(this);

        tabbedPane.addTab("Select Project", targetSelectionUI);
        tabbedPane.addTab("View Code", sourceCodeDisplayUI);
        tabbedPane.addChangeListener(e -> updateStatus());

        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setupWindow();
        updateStatus();
    }

    private JLabel createStatusLabel(String text) {
        return new JLabel(text);
    }

    private void setupWindow() {
        setTitle("Code Analysis Tool");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public targetSelectionUI getTargetSelectionUI() {
        return targetSelectionUI;
    }

    public sourceCodeDisplayUI getSourceCodeDisplayUI() {
        return sourceCodeDisplayUI;
    }

    public void setStatus(String message) {
        statusLabelTab.setText(message);
    }

    public void switchToTab(int index) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(index);
        }
    }

    public void updateStatus() {
        updateStatusLabel(statusLabelTab, "Current tab: " + trimFrontText(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()), 20));
        updateStatusLabel(statusLabelProject, "Project: " + trimFrontText(MetricsTracker.getProjectPath(), 50));

        String focusedFile = MetricsTracker.getFocusedFile();
        updateStatusLabel(statusLabelFile, "Current File: " + (focusedFile == null ? "Viewing whole project" : trimFrontText(focusedFile, 50)));
    }

    private void updateStatusLabel(JLabel label, String text) {
        label.setText(text);
    }

    private JSeparator createVerticalSeparator(JComponent parent) {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(1, (int) (parent.getPreferredSize().height * 0.6)));
        return separator;
    }
}
