package userInterface;

import javax.swing.*;

public class sourceCodeDisplayUI extends JPanel {
    String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public sourceCodeDisplayUI() {
        super();
        this.add(new JLabel("Source Code Display"));
        System.out.println(fileName);
    }
}
