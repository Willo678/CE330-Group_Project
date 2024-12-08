package legacy.userInterface.UI_Panels;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class sourceCodeDisplayUI extends JPanel {
    private String fileName;  // Make fields private to follow good encapsulation practices
    private String currentFolder;

    public void setFileName(String fileName) {
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
    public String getFileName() {
        return fileName;
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public sourceCodeDisplayUI() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JButton chooseSource = new JButton("Choose Source File");
        JTextArea codeArea = new JTextArea();
        chooseSource.addActionListener(e -> setFileName(fileName));

        this.add(chooseSource);
        this.add(codeArea);
    }

    public ArrayList<String> getCode(String fileName) throws FileNotFoundException {
        try {
            ArrayList<String> code = new ArrayList<>();
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