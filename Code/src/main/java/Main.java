
import XP_Metrics.EvaluateXP;
import userInterface.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {



        //programWindow window = new programWindow();

        EvaluateXP evaluator = new EvaluateXP("src/main/java/XP_Metrics/CodeAnalysis.java");
        System.out.println(evaluator.normalisedScore());

    }
}
