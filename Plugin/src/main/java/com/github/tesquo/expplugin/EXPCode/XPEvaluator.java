package com.github.tesquo.expplugin.EXPCode;

import com.github.tesquo.expplugin.EXPCode.Evaluators.ClassChecker;
import com.github.tesquo.expplugin.EXPCode.Evaluators.CodeChecker;
import com.github.tesquo.expplugin.EXPCode.Evaluators.IndentationChecker;
import com.github.tesquo.expplugin.EXPCode.getTokens.Token;

import java.util.ArrayList;

public class XPEvaluator {

    public ArrayList<Token> tokenList;

    public ArrayList<Score> scoreIndentation;
    public ArrayList<Score> scoreClassStructure;
    public ArrayList<Score> scoreMethodStructure;

    public String content;

    public XPEvaluator(String content) {
        this.content = content;

        tokenList = getTokens.getTokens(content);

        scoreIndentation = IndentationChecker.checkIndentation(
                tokenList
                        .stream()
                        .filter(getTokens.BracePair.class::isInstance)
                        .map(getTokens.BracePair.class::cast).toList()
        );
        scoreClassStructure = ClassChecker.checkImports(content, tokenList);
        scoreMethodStructure = CodeChecker.CodeAnalysis(tokenList);

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