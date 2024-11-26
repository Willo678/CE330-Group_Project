package userInterface;

import javax.swing.*;

public class programWindow extends JFrame {

    JTabbedPane tabbedPane = new JTabbedPane();
    targetSelectionUI targetSelectionUI = new targetSelectionUI();
    sourceCodeDisplayUI sourceCodeDisplayUI = new sourceCodeDisplayUI();
    codeMetricsUI codeMetricsUI = new codeMetricsUI();

    public userInterface.targetSelectionUI getTargetSelectionUI() {
        return targetSelectionUI;
    }

    public programWindow() {

        tabbedPane.addTab("Target Selection", targetSelectionUI);
        tabbedPane.addTab("Source Code Display",sourceCodeDisplayUI);
        tabbedPane.addTab("Code Metrics", codeMetricsUI);

        this.add(tabbedPane);

        this.setTitle("Program Window");
        this.setSize(500, 650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

}
