
import XP_Metrics.EvaluateXP;
import userInterface.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {



        //programWindow window = new programWindow();

        EvaluateXP evaluator = new EvaluateXP("src/main/java/XP_Metrics/CodeAnalysis.java");
        System.out.println("Averaged scores: " + evaluator.normalisedScore());

        System.out.println(evaluator.scoreIndentation);
        System.out.println(evaluator.scoreClassStructure);
        System.out.println(evaluator.scoreMethodStructure);


    }
}
