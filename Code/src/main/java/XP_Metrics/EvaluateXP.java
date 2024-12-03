package XP_Metrics;

import java.util.ArrayList;
import java.util.Arrays;

import XP_Metrics.getTokens.*;

public class EvaluateXP {

    public ArrayList<Token> bracePairs;
    public ArrayList<Score> scoreIndentation;
    public ArrayList<Score> scoreClassStructure;
    int scoreMethodStructure;


    public EvaluateXP(String path) {
        bracePairs = getTokens.getTokens(path);

        scoreIndentation = indentationChecker.checkIndentation(
                bracePairs
                        .stream()
                        .filter(BracePair.class::isInstance)
                        .map(BracePair.class::cast).toList()
        );
        scoreClassStructure = classChecker.checkImports(path, bracePairs);
        ArrayList<Score> codeAnalysisScores = CodeAnalysis.CodeAnalysis(bracePairs);
        scoreMethodStructure = 100 - sumScoreArray(codeAnalysisScores);
    }

    public int normalisedScore() {
        int[] weights = new int[]{25, 25, 25, 25};  //Four parts, 25 points each
        int[] percentages = new int[]{
                getScorePercentage(scoreIndentation),
                getScorePercentage(scoreClassStructure),
                scoreMethodStructure,
                getScorePercentage(CodeAnalysis.CodeAnalysis(bracePairs))
        };

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