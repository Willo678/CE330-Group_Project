package userInterface.UI_Panels;

import userInterface.MetricsTracker;
import userInterface.ProgramWindow;
import userInterface.UI_Widgets.FileSelector;

import javax.swing.*;
import java.awt.*;

import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class targetSelectionUI extends JPanel {
    private final ProgramWindow parent;


    public targetSelectionUI(ProgramWindow parent) {
        this.parent = parent;



        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;

        FileSelector fileSelector = new FileSelector();
        fileSelector.addActionListener( e -> {
            parent.getCodeMetricsUI().updateMetrics();
            parent.updateStatus();


            System.out.println(MetricsTracker.getOverallIndentationScore()); //Remove
            System.out.println(MetricsTracker.getOverallClassStructureScore()); //Remove
            System.out.println(MetricsTracker.getOverallMethodStructureScore()); //Remove
            System.out.println(MetricsTracker.getOverallScore()); //Remove
        });


        this.add(fileSelector, gbc);


        gbc.gridy = 100; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1;
        this.add(new JPanel(), gbc);

    }





}