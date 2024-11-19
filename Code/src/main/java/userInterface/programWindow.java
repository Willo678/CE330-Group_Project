package userInterface;

import javax.swing.*;

public class programWindow extends JFrame {

    JTabbedPane tabbedPane = new JTabbedPane();

    public programWindow() {
        JPanel targetSelectionPanel = new JPanel();
        targetSelectionPanel.add(new JLabel("Target Selection Panel"));

        JPanel codeDisplayPanel = new JPanel();
        codeDisplayPanel.add(new JLabel("Code Display Panel"));

        JPanel codeMetricsPanel = new JPanel();
        codeMetricsPanel.add(new JLabel("Code Metrics Panel"));

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
