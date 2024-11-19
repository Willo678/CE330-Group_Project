package userInterface;

import javax.swing.*;

public class programWindow extends JFrame {

    JTabbedPane tabbedPane = new JTabbedPane();
    sourceCodeDisplayUI sourceCodeDisplayUI = new sourceCodeDisplayUI();
    codeMetricsUI codeMetricsUI = new codeMetricsUI();

    public programWindow() {
        this.setTitle("Program Window");
        this.setSize(500, 650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        tabbedPane.addTab("Target Selection", new JPanel());
        tabbedPane.addTab("Source Code Display", new JPanel());
        tabbedPane.addTab("Code Metrics", new JPanel());
        this.add(tabbedPane);
    }

}
