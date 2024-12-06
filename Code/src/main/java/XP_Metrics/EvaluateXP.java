package XP_Metrics;

import java.util.ArrayList;
import java.util.Arrays;

import XP_Metrics.getTokens.*;

public class EvaluateXP {

    public ArrayList<Token> bracePairs;
    public ArrayList<Score> scoreIndentation;
    public ArrayList<Score> scoreClassStructure;
    public ArrayList<Score> scoreMethodStructure;


    public EvaluateXP(String path) {
        bracePairs = getTokens.getTokens(path);

        scoreIndentation = indentationChecker.checkIndentation(
                bracePairs
                        .stream()
                        .filter(BracePair.class::isInstance)
                        .map(BracePair.class::cast).toList()
        );
        scoreClassStructure = classChecker.checkImports(path, bracePairs);
        scoreMethodStructure = CodeAnalysis.CodeAnalysis(bracePairs);

    }

    public int normalisedScore() {
        int[] weights = new int[]{33, 33, 33};  //Three parts, 33 points each
        int[] percentages = new int[]{
                getScorePercentage(scoreIndentation),
                getScorePercentage(scoreClassStructure),
                getScorePercentage(scoreMethodStructure)
        };

        System.out.println("Indentation score: "+getScorePercentage(scoreIndentation));
        System.out.println("ClassStructure score: "+getScorePercentage(scoreClassStructure));
        System.out.println("MethodStructure score: "+getScorePercentage(scoreMethodStructure));

        int result = 0;
        for (int i = 0; i < weights.length; i++) {
            result += (percentages[i] * weights[i]) / 100;
        }
        return result;
    }

    private static int sumScoreArray(ArrayList<Score> scoreArray) {
        return scoreArray.stream().mapToInt(a -> a.score).sum();
    }

    private static int getScorePercentage(ArrayList<Score> scoreArray) {
        return Math.max((100 - sumScoreArray(scoreArray)), 0);
    }

}