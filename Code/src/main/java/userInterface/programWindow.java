package userInterface;

import javax.swing.*;
import java.awt.*;

public class programWindow extends JFrame {
    private final JTabbedPane tabbedPane;
    private final targetSelectionUI targetSelectionUI;
    private final sourceCodeDisplayUI sourceCodeDisplayUI;
    private final codeMetricsUI codeMetricsUI;
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
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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