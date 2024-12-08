package userInterface.UI_Panels;

import userInterface.MetricsTracker;
import userInterface.UI_Widgets.FileSelector;

import javax.swing.*;
import java.awt.*;

import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class targetSelectionUI extends JPanel {
    private final codeMetricsUI metricsUI;


    public targetSelectionUI(codeMetricsUI metricsUI) {
        this.metricsUI = metricsUI;



        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;

        FileSelector fileSelector = new FileSelector();
        fileSelector.addActionListener( e -> {
            System.out.println("Updated project path to: "+ MetricsTracker.getProjectPath());
            System.out.println(MetricsTracker.getOverallIndentationScore());
            System.out.println(MetricsTracker.getOverallClassStructureScore());
            System.out.println(MetricsTracker.getOverallMethodStructureScore());
            System.out.println(MetricsTracker.getOverallScore());
        });


        this.add(fileSelector, gbc);


        gbc.gridy = 100; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1;
        this.add(new JPanel(), gbc);

    }





}