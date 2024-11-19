package userInterface;

import utils.hintTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;


import static utils.directoryContainsJava.directoryContainsJava;

public class targetSelectionUI extends JPanel {

    public targetSelectionUI(){
        this(0,0);
    }

    public targetSelectionUI(int width, int height){

        super();
        this.setLayout(new GridBagLayout());

        JFileChooser folderSelect = new JFileChooser();
        folderSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        GridBagConstraints gbc = new GridBagConstraints();

        //Holds components
        this.setBorder(new EmptyBorder(0, 30, 0, 30));
        if (height<=0 || width<=0) {
            gbc.weightx=1; gbc.fill = GridBagConstraints.HORIZONTAL;
        } else {
            this.setSize(width, height);
        }


        gbc = new GridBagConstraints();

        //Text field, user can manually enter a path, or select one using the dialogue
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;
        JTextField filePath = new hintTextField("Select a project directory:"); this.add(filePath, gbc);
        filePath.setBackground(new Color(0xD1D1D1));
        filePath.setForeground(new Color(0x5C5C5C));
        filePath.setBorder(new LineBorder(new Color(0), 1));
        filePath.setFont(filePath.getFont().deriveFont(Font.ITALIC, 10));
        filePath.setMargin(new Insets(3, 10, 3, 0));


        //Button to trigger the file select dialogue
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton selectButton = new JButton("SELECT"); this.add(selectButton, gbc);
        selectButton.grabFocus();
        selectButton.addActionListener((x) -> { //had to add "(x) or the lambda wouldnt work"
            int returnVal = folderSelect.showOpenDialog(this);
            if (returnVal==JFileChooser.APPROVE_OPTION) {
                filePath.setText(folderSelect.getSelectedFile().getAbsolutePath());
            } else {
                //Either an error occurred, or user cancelled operation
            }
        });


        //Button to confirm selection and pass on the path to other areas of the project
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridwidth = 2;
        JButton confirmButton = new JButton("CONFIRM"); this.add(confirmButton, gbc);
        confirmButton.addActionListener((x) -> { //had to add "(x) or the lambda wouldnt work"
            String path = filePath.getText();
            System.out.println(path);
            if (!path.isEmpty()){
                try {
                    Paths.get(path);
                    if (!directoryContainsJava(new File(path))){throw new InvalidPathException(path, "Given path does not contain a java file");}
                    System.out.println("Success, pass on to other modules: "+path);

                    //Pass on path to be analysed

                } catch (InvalidPathException | NullPointerException e) {
                    System.out.println("Invalid path");
                    e.printStackTrace();
                }
            }
        });




        this.setVisible(true);
        //this.setResizable(false);
    }



}
