package legacy.userInterface.UI_Widgets;


import legacy.userInterface.MetricsTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static utils.directoryContainsJava.directoryContainsJava;


public class FileSelector extends JComponent {

    private final JTextField filePath;


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


        JFileChooser folderSelect = getjFileChooser(selectFiles);

        GridBagConstraints gbc = new GridBagConstraints();

        //Holds components
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(new EmptyBorder(0, 30, 0, 30));

        gbc.weightx=1; gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(selectionPanel, gbc);


        gbc = new GridBagConstraints();


        //Text field, user can manually enter a path, or select one using the dialogue
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;
        filePath = new hintTextField("Select a project directory:"); selectionPanel.add(filePath, gbc);
        filePath.setBackground(new Color(0xD1D1D1));
        filePath.setForeground(new Color(0x5C5C5C));
        filePath.setBorder(new LineBorder(new Color(0), 1));
        filePath.setFont(filePath.getFont().deriveFont(Font.ITALIC, 10));
        filePath.setMargin(new Insets(3, 10, 3, 0));

        filePath.addActionListener(e->confirm());


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
        confirmButton.addActionListener(e->confirm());

    }

    private static JFileChooser getjFileChooser(boolean selectFiles) {
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
        return folderSelect;
    }

    private void confirm() {
        String path = filePath.getText();
        System.out.println(path);
        if (!path.isEmpty()){
            try {
                if (!Files.exists(Path.of(path))) {throw new InvalidPathException(path, "Path does not exist");}
                if (!directoryContainsJava(new File(path))){
                    JOptionPane.showMessageDialog(this, "Given path must contain at least one '.java' file", "Error - Invalid Path", JOptionPane.WARNING_MESSAGE);
                }

                //Pass on path to be analysed
                MetricsTracker.selectProject(path);
                fireActionPerformed(new ActionEvent(this, 0, "SELECT_PROJECT"));

            } catch (InvalidPathException | NullPointerException e) {
                JOptionPane.showMessageDialog(this, "The path you have entered is invalid; it may not exist, or the program may lack the permissions to access it"+"\n"+e.getMessage(), "Error - Invalid Path", JOptionPane.WARNING_MESSAGE);
            }
        }
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
