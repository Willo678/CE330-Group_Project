// file viewer ui first draft
// by sj22795
package userInterface.UI_Panels;
import javax.swing.*;
import java.awt.*;

public class fileViewerUI {
    fileViewerUI(){
        JFrame fileViewerFrame = new JFrame("Code Analysis");
        JPanel fileViewerPanel = new JPanel(new BorderLayout());
        JPanel codePanel = new JPanel(new BorderLayout());

        fileViewerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel fileLabel = new JLabel("Folder tab");
        fileViewerPanel.add(fileLabel);
        fileViewerPanel.setSize(new Dimension(200, 800));

        JTextArea codeArea = new JTextArea();
        codeArea.setSize(800, 800);
        codePanel.add(codeArea);

        fileViewerFrame.setSize(1000,800);
        fileViewerFrame.setResizable(true);
        fileViewerFrame.setLocationRelativeTo(null);
        fileViewerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fileViewerFrame.add(fileViewerPanel, BorderLayout.WEST);
        fileViewerPanel.add(codeArea, BorderLayout.EAST);
        fileViewerFrame.setVisible(true);
    }
}
