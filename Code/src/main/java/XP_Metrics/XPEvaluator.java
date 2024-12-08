package XP_Metrics;

import java.util.ArrayList;

import XP_Metrics.evaluators.CodeChecker;
import XP_Metrics.evaluators.classChecker;
import XP_Metrics.evaluators.indentationChecker;
import XP_Metrics.getTokens.*;

public class XPEvaluator {

    public ArrayList<Token> bracePairs;
    public ArrayList<Score> scoreIndentation;
    public ArrayList<Score> scoreClassStructure;
    public ArrayList<Score> scoreMethodStructure;


    public XPEvaluator(String path) {
        bracePairs = getTokens.getTokens(path);

        scoreIndentation = indentationChecker.checkIndentation(
                bracePairs
                        .stream()
                        .filter(BracePair.class::isInstance)
                        .map(BracePair.class::cast).toList()
        );
        scoreClassStructure = classChecker.checkImports(path, bracePairs);
        scoreMethodStructure = CodeChecker.CodeAnalysis(bracePairs);

    }

    public int indentationScore() {
        return getScorePercentage(scoreIndentation);
    }

    public int classStructureScore() {
        return getScorePercentage(scoreClassStructure);
    }

    public int methodStructureScore() {
        return getScorePercentage(scoreMethodStructure);
    }

    public int normalisedScore() {
        int[] weights = new int[]{33, 33, 33};  //Three parts, 33 points each
        int[] percentages = new int[]{
                indentationScore(),
                classStructureScore(),
                methodStructureScore()
        };

        int result = 0;
        for (int i = 0; i < weights.length; i++) {
            result += (percentages[i] * weights[i]) / 100;
        }
        return result;
    }


    @Override
    public String toString() {
        return "Indentation score = "+getScorePercentage(scoreIndentation) + " : " + scoreIndentation + "\n"
                + "ClassStructure score = "+getScorePercentage(scoreClassStructure) + " : " + scoreClassStructure + "\n"
                + "MethodStructure score = "+getScorePercentage(scoreMethodStructure) + " : " + scoreMethodStructure + "\n"
                + "Overall score = "+normalisedScore();
    }

    private static int sumScoreArray(ArrayList<Score> scoreArray) {
        return scoreArray.stream().mapToInt(a -> a.score).sum();
    }

    private static int getScorePercentage(ArrayList<Score> scoreArray) {
        return Math.max((100 - sumScoreArray(scoreArray)), 0);
    }

}