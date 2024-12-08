package legacy.userInterface;

import legacy.userInterface.UI_Panels.codeMetricsUI;
import legacy.userInterface.UI_Panels.sourceCodeDisplayUI;
import legacy.userInterface.UI_Panels.targetSelectionUI;

import javax.swing.*;
import java.awt.*;

public class programWindow extends JFrame {
    private final JTabbedPane tabbedPane;
    private final legacy.userInterface.UI_Panels.targetSelectionUI targetSelectionUI;
    private final legacy.userInterface.UI_Panels.sourceCodeDisplayUI sourceCodeDisplayUI;
    private final legacy.userInterface.UI_Panels.codeMetricsUI codeMetricsUI;
    private final JPanel statusBar;
    private final JLabel statusLabelTab;

    public programWindow() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabelTab = new JLabel("Ready");

        codeMetricsUI = new codeMetricsUI();
        targetSelectionUI = new targetSelectionUI(codeMetricsUI);
        sourceCodeDisplayUI = new sourceCodeDisplayUI();

        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.add(statusLabelTab);

        tabbedPane.addTab("Select Project", targetSelectionUI);
        tabbedPane.addTab("View Code", sourceCodeDisplayUI);
        tabbedPane.addTab("Metrics", codeMetricsUI);

        tabbedPane.addChangeListener(e -> updateStatus());

        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setupWindow();
    }

    private void setupWindow() {
        setTitle("Code Analysis Tool");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }


    public legacy.userInterface.UI_Panels.targetSelectionUI getTargetSelectionUI() {
        return targetSelectionUI;
    }

    public legacy.userInterface.UI_Panels.sourceCodeDisplayUI getSourceCodeDisplayUI() {
        return sourceCodeDisplayUI;
    }

    public legacy.userInterface.UI_Panels.codeMetricsUI getCodeMetricsUI() {
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
        if (index != -1) {
            statusLabelTab.setText("Current tab: " + tabbedPane.getTitleAt(index));
        }
    }
}