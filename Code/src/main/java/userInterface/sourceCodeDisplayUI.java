package userInterface;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class sourceCodeDisplayUI extends JPanel {
    String fileName;
    String currentFolder;

    public void setFileName(String fileName) {
        // this should be used to select a file from the specified folder
        // however its not yet possible to check that
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);

        this.fileName = fileName;
    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder = currentFolder;
    }

    public sourceCodeDisplayUI() {
        super();

        JButton chooseSource = new JButton("Choose Source File");
        chooseSource.addActionListener(e -> setFileName(fileName));
        this.add(chooseSource);
        this.add(new JTextArea());
        System.out.println(fileName);
    }

    public ArrayList<String> getCode(String fileName) throws FileNotFoundException {

        try {
            ArrayList<String> code = new ArrayList<String>();
            File currentFile = new File(fileName);
            Scanner fileScan = new Scanner(currentFile);
            code.add(fileScan.nextLine());
            return code;
        } catch (FileNotFoundException e) {
            System.out.println("No file");
            return null;
        }
    }

}
