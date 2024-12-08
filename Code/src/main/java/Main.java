
import XP_Metrics.EvaluateXP;
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

        EvaluateXP evaluator = new EvaluateXP("src/main/java/XP_Metrics/CodeAnalysis.java");
        System.out.println(evaluator);



    }
}
