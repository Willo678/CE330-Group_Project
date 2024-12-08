package userInterface;

import userInterface.UI_Panels.codeMetricsUI;
import userInterface.UI_Panels.sourceCodeDisplayUI;
import userInterface.UI_Panels.targetSelectionUI;

import javax.swing.*;
import java.awt.*;

public class programWindow extends JFrame {
    private final JTabbedPane tabbedPane;
    private final userInterface.UI_Panels.targetSelectionUI targetSelectionUI;
    private final userInterface.UI_Panels.sourceCodeDisplayUI sourceCodeDisplayUI;
    private final userInterface.UI_Panels.codeMetricsUI codeMetricsUI;
    private final JPanel statusBar;
    private final JLabel statusLabel;

    public programWindow() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Ready");

        codeMetricsUI = new codeMetricsUI();
        targetSelectionUI = new targetSelectionUI(codeMetricsUI);
        sourceCodeDisplayUI = new sourceCodeDisplayUI();

        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.add(statusLabel);

        tabbedPane.addTab("Select Project", targetSelectionUI);
        tabbedPane.addTab("View Code", sourceCodeDisplayUI);
        tabbedPane.addTab("Metrics", codeMetricsUI);

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index != -1) {
                statusLabel.setText("Current tab: " + tabbedPane.getTitleAt(index));
            }
        });

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
        statusLabel.setText(message);
    }

    public void switchToTab(int index) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(index);
        }
    }
}