
import XP_Metrics.EvaluateXP;
import userInterface.programWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            programWindow window = new programWindow();
//            window.setVisible(true);
//        });
//    }//Running

        EvaluateXP evaluator = new EvaluateXP("src/main/java/XP_Metrics/CodeAnalysis.java");
        System.out.println("Averaged scores: " + evaluator.normalisedScore());

        System.out.println(evaluator.scoreIndentation);
        System.out.println(evaluator.scoreClassStructure);
        System.out.println(evaluator.scoreMethodStructure);

    }

}
