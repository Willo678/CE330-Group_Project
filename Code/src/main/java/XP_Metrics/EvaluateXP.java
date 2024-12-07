package XP_Metrics;

import java.util.ArrayList;

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

        int result = 0;
        for (int i = 0; i < weights.length; i++) {
            result += (percentages[i] * weights[i]) / 100;
        }
        return result;
    }


    @Override
    public String toString() {
        return "Indentation score: "+getScorePercentage(scoreIndentation)
                + "ClassStructure score: "+getScorePercentage(scoreClassStructure)
                + "MethodStructure score: "+getScorePercentage(scoreMethodStructure)
                + "Overall score: "+normalisedScore();
    }

    private static int sumScoreArray(ArrayList<Score> scoreArray) {
        return scoreArray.stream().mapToInt(a -> a.score).sum();
    }

    private static int getScorePercentage(ArrayList<Score> scoreArray) {
        return Math.max((100 - sumScoreArray(scoreArray)), 0);
    }

}