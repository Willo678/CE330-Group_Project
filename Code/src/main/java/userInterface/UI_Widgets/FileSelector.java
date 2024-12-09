package userInterface.UI_Widgets;


import userInterface.MetricsTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.trimFrontText.trimFrontText;


public class FileSelector extends JComponent {

    private final JTextField filePath;
    private int index;


    public FileSelector(){
        this(0,0);
    }

    public FileSelector(boolean selectFiles){
        this(0,0);
    }

    public FileSelector(int width, int height){

        index = 0;

        this.setBackground(new Color(0));

        if (width!=0 && height!=0){this.setMaximumSize(new Dimension(width, height));}
        this.setLayout(new GridBagLayout());



        GridBagConstraints gbc = new GridBagConstraints();

        //Holds components
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(new EmptyBorder(0, 30, 0, 30));

        gbc.weightx=1; gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(selectionPanel, gbc);


        gbc = new GridBagConstraints();


        //Text field, displays selected file
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;
        filePath = new JTextField("Select a project file:"); selectionPanel.add(filePath, gbc);
        filePath.setBackground(new Color(0xD1D1D1)); filePath.setForeground(new Color(0x5C5C5C));
        filePath.setBorder(new LineBorder(new Color(0), 1));
        filePath.setFont(filePath.getFont().deriveFont(Font.ITALIC, 10));
        filePath.setMargin(new Insets(3, 10, 3, 0));
        filePath.setEditable(false);




        //Button to move left
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton leftButton = new JButton("<-"); selectionPanel.add(leftButton, gbc);
        leftButton.addActionListener(e->left());


        //Button to move right
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton rightButton = new JButton("->"); selectionPanel.add(rightButton, gbc);
        rightButton.addActionListener(e->right());



    }



    private void left() {
        index = Math.max(0, index-1);
        updateDisplay();
    }

    private void right() {
        index = Math.min(index+1, MetricsTracker.getProjectSize());
        updateDisplay();
    }


    private void updateDisplay() {
        String displayVal = "No project selected";
        MetricsTracker.setFocusedFile(index);
        if (MetricsTracker.trackerExists()) {
            if (index==0) {
                displayVal = "Focusing: Whole Project";
            } else {
                displayVal = "Focusing: " + trimFrontText(MetricsTracker.getFocusedFile(), 50);
            }
        }
        filePath.setText(displayVal);
        fireActionPerformed(new ActionEvent(this, 0, "UPDATE_TRACKED"));
    }




    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }
    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }
    protected void fireActionPerformed(ActionEvent event) {
        for (ActionListener listener : listenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(event);
        }
    }


}
