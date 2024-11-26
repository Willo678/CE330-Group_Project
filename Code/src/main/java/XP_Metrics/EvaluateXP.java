package XP_Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import XP_Metrics.getTokens.*;

public class EvaluateXP {

    List<Token> bracePairs;
    public ArrayList<Score> scoreIndentation;
    int scoreClassStructure;
    int scoreMethodStructure;


    public EvaluateXP(String path) {
        bracePairs = getTokens.getBracePairs(path).stream().filter(x-> x instanceof BracePair).toList();

        scoreIndentation = indentationChecker.checkIndentation(
                bracePairs
                        .stream()
                        .filter(BracePair.class::isInstance)
                        .map(BracePair.class::cast).toList()
        );

    }

    public int normalisedScore() {
        //Indentation, class structure, method structure (add as necessary)
        int[] weights = new int[]{25,25,25};
        int[] percentages = new int[]{getScorePercentage(scoreIndentation), scoreClassStructure, scoreMethodStructure};


        int weightTotal = Arrays.stream(weights).sum();
        int result = 0;
        for (int i=0; i<weights.length; i++){
            result += percentages[i]*(weights[i]/weightTotal);
        }
        return result;

    }

    private static int sumScoreArray(ArrayList<Score> scoreArray) {
        return scoreArray.stream().mapToInt(a -> a.score).sum();
    }

    private static int getScorePercentage(ArrayList<Score> scoreArray) {
        return Math.max((100-sumScoreArray(scoreArray)), 0);
    }

}
