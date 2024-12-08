package userInterface.UI_Widgets;


import userInterface.MetricsTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;


import static utils.directoryContainsJava.directoryContainsJava;


public class FileSelector extends JComponent {

    public FileSelector(){
        this(0,0, false);
    }

    public FileSelector(boolean selectFiles){
        this(0,0, selectFiles);
    }

    public FileSelector(int width, int height, boolean selectFiles){

        this.setBackground(new Color(0));

        if (width!=0 && height!=0){this.setMaximumSize(new Dimension(width, height));}
        this.setLayout(new GridBagLayout());


        JFileChooser folderSelect = new JFileChooser();
        if (selectFiles) {
            folderSelect.setFileSelectionMode(JFileChooser.FILES_ONLY);
            folderSelect.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return (f.getName().endsWith(".java"));
            }

            @Override
            public String getDescription() {
                return "Select '.java' Files";
            }
        });} else {folderSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);}

        GridBagConstraints gbc = new GridBagConstraints();

        //Holds components
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(new EmptyBorder(0, 30, 0, 30));

        gbc.weightx=1; gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(selectionPanel, gbc);


        gbc = new GridBagConstraints();


        //Text field, user can manually enter a path, or select one using the dialogue
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;
        JTextField filePath = new hintTextField("Select a project directory:"); selectionPanel.add(filePath, gbc);
        filePath.setBackground(new Color(0xD1D1D1));
        filePath.setForeground(new Color(0x5C5C5C));
        filePath.setBorder(new LineBorder(new Color(0), 1));
        filePath.setFont(filePath.getFont().deriveFont(Font.ITALIC, 10));
        filePath.setMargin(new Insets(3, 10, 3, 0));


        //Button to trigger the file select dialogue
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton selectButton = new JButton("SELECT"); selectionPanel.add(selectButton, gbc);
        selectButton.grabFocus();
        selectButton.addActionListener(_ -> {
            int returnVal = folderSelect.showOpenDialog(selectionPanel);
            if (returnVal==JFileChooser.APPROVE_OPTION) {
                filePath.setText(folderSelect.getSelectedFile().getAbsolutePath());
            } else {
                //Either an error occurred, or user cancelled operation
            }
        });


        //Button to confirm selection and pass on the path to other areas of the project
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridwidth = 2;
        JButton confirmButton = new JButton("CONFIRM"); selectionPanel.add(confirmButton, gbc);
        confirmButton.addActionListener(_ -> {
            String path = filePath.getText();
            System.out.println(path);
            if (!path.isEmpty()){
                try {
                    Paths.get(path);
                    if (!directoryContainsJava(new File(path))){throw new InvalidPathException(path, "Given path does not contain a java file");}
                    System.out.println("Success, pass on to other modules: "+path);

                    //Pass on path to be analysed
                    MetricsTracker.selectProject(path);
                    fireActionPerformed(new ActionEvent(this, 0, "SELECT_PROJECT"));

                } catch (InvalidPathException | NullPointerException e) {
                    System.out.println("Invalid path");
                    e.printStackTrace();
                }
            }
        });

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
