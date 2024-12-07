
import XP_Metrics.EvaluateXP;
import userInterface.*;



public class Main {
    public static void main(String[] args) {

        EvaluateXP evaluator = new EvaluateXP("src/main/java/XP_Metrics/CodeAnalysis.java");
        System.out.println(evaluator);

        programWindow window = new programWindow();




    }
}
