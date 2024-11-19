
import XP_Metrics.ScoringMetric;
import userInterface.*;

public class Main {
    public static void main(String[] args) {
        int score = ScoringMetric.scoreBracePairsIndentation( "M:\\year 3\\ce320 project\\Code\\src\\main\\java\\userInterface/targetSelectionUI.java",3);
        int score2 = ScoringMetric.scoreBracePairsFunLength("M:\\year 3\\ce320 project\\Code\\src\\main\\java\\userInterface/targetSelectionUI.java",10);
        targetSelectionUI UI = new targetSelectionUI();

    }
}
