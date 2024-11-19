package XP_Metrics;

import java.util.ArrayList;
import XP_Metrics.getBracePairs.*;

public class EvaluateXP {

    ArrayList<BracePair> bracePairs;
    public ArrayList<Score> scoreIndentation;
    int scoreClassStructure;
    int scoreMethodStructure;


    public EvaluateXP(String path) {
        bracePairs = getBracePairs.getBracePairs(path);

        scoreIndentation = indentationChecker.checkIndentation(bracePairs);

    }

    public int normalisedScore() {
        return sumScoreArray(scoreIndentation)
                +scoreClassStructure+scoreMethodStructure;
    }

    private static int sumScoreArray(ArrayList<Score> scoreArray) {
        return scoreArray.stream().mapToInt(a -> ((Score)a).score).sum();
    }

}
