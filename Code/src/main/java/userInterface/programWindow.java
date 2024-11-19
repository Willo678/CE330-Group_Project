package userInterface;

import javax.swing.*;

public class programWindow extends JFrame {

    JTabbedPane tabbedPane = new JTabbedPane();

    public programWindow() {

        tabbedPane.addTab("Target Selection", new targetSelectionUI());
        tabbedPane.addTab("Source Code Display", new sourceCodeDisplayUI());
        tabbedPane.addTab("Code Metrics", new codeMetricsUI());

        this.add(tabbedPane);

        this.setTitle("Program Window");
        this.setSize(500, 650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

}
