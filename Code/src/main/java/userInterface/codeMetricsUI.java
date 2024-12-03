package userInterface;

import javax.swing.*;
import java.awt.*;

public class codeMetricsUI extends JPanel {
    private final DialPanel totalScoreDial;
    private final JPanel metricsPanel;

    public codeMetricsUI() {
        setLayout(new BorderLayout());

        metricsPanel = new JPanel();
        metricsPanel.setLayout(new GridLayout(3, 2, 10, 5));
        metricsPanel.add(new JLabel("Indentation:"));
        metricsPanel.add(new JLabel("0%"));
        metricsPanel.add(new JLabel("Class Structure:"));
        metricsPanel.add(new JLabel("0%"));
        metricsPanel.add(new JLabel("Method Structure:"));
        metricsPanel.add(new JLabel("0%"));

        totalScoreDial = new DialPanel("Total Score");

        add(metricsPanel, BorderLayout.NORTH);
        add(totalScoreDial, BorderLayout.CENTER);
    }

    public void updateMetrics(double indentation, double classStructure, double methodStructure) {
        double totalScore = (indentation + classStructure + methodStructure) / 3.0;

        ((JLabel)metricsPanel.getComponent(1)).setText(String.format("%.1f%%", indentation));
        ((JLabel)metricsPanel.getComponent(3)).setText(String.format("%.1f%%", classStructure));
        ((JLabel)metricsPanel.getComponent(5)).setText(String.format("%.1f%%", methodStructure));

        totalScoreDial.setScore(totalScore / 100.0);
    }
}