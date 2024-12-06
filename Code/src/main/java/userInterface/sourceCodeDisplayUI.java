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

        File file = fileChooser.getSelectedFile();
        String filePath = file.getAbsolutePath();
        System.out.println(filePath);

        this.fileName = filePath;
    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder = currentFolder;
    }

    public sourceCodeDisplayUI() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS)); // makes layout easier

        JButton chooseSource = new JButton("Choose Source File");
        JTextArea codeArea = new JTextArea();
        chooseSource.addActionListener(e -> setFileName(fileName));


        this.add(chooseSource);
        this.add(codeArea);

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
