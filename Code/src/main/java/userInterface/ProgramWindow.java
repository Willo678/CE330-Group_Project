package userInterface;

import userInterface.UI_Panels.codeMetricsUI;
import userInterface.UI_Panels.sourceCodeDisplayUI;
import userInterface.UI_Panels.targetSelectionUI;

import javax.swing.*;
import java.awt.*;

public class ProgramWindow extends JFrame {
    private final JTabbedPane tabbedPane;
    protected final userInterface.UI_Panels.targetSelectionUI targetSelectionUI;
    protected final userInterface.UI_Panels.sourceCodeDisplayUI sourceCodeDisplayUI;
    protected final userInterface.UI_Panels.codeMetricsUI codeMetricsUI;


    private final JPanel statusBar;
    private final JLabel statusLabelTab;
    private final JLabel statusLabelProject;
    private final JLabel statusLabelFile;

    public ProgramWindow() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();


        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabelTab = new JLabel("Current tab"); statusBar.add(statusLabelTab);
        statusBar.add(new JSeparator(SwingConstants.VERTICAL));
        statusLabelProject = new JLabel("Selected Project"); statusBar.add(statusLabelProject);
        statusBar.add(new JSeparator(SwingConstants.VERTICAL));
        statusLabelFile = new JLabel("Selected File"); statusBar.add(statusLabelFile);


        codeMetricsUI = new codeMetricsUI(this);
        targetSelectionUI = new targetSelectionUI(this);
        sourceCodeDisplayUI = new sourceCodeDisplayUI(this);

        statusBar.setBorder(BorderFactory.createEtchedBorder());


        tabbedPane.addTab("Select Project", targetSelectionUI);
        tabbedPane.addTab("View Code", sourceCodeDisplayUI);
        tabbedPane.addTab("Metrics", codeMetricsUI);

        tabbedPane.addChangeListener(e -> updateStatus());

        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setupWindow();
        updateStatus();
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

    public codeMetricsUI getCodeMetricsUI() {
        return codeMetricsUI;
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
        int index = tabbedPane.getSelectedIndex();
        String text = trimText(tabbedPane.getTitleAt(index),20);
        if (index != -1) {
            statusLabelTab.setText("Current tab: " + text);
        }

        text = trimText(MetricsTracker.getProjectPath(), 50);
        statusLabelProject.setText("Project: "+text);

        text = MetricsTracker.getTrackedFile();
        if (text==null) {text = "Viewing whole project";}
        text = trimText(text, 50);
        statusLabelFile.setText("Current File: "+text);
    }


    private String trimText(String text, int trimLength) {
        if (text.length()>trimLength) {
            text = ".."+text.substring(text.length()-(trimLength+2));
        }
        while (text.length()<trimLength) {
            text+=" ";
        }
        return text;
    }
}