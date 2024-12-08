
import XP_Metrics.XPEvaluator;
import userInterface.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            programWindow window = new programWindow();
            window.setVisible(true);
        });
        //Running

        //XPEvaluator evaluator = new XPEvaluator("src/main/java/XP_Metrics/evaluators/CodeChecker.java");
        //System.out.println(evaluator);



    }
}
