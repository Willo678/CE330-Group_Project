package XP_Metrics;
// written by sj22795
// code cleanliness check first draft

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import XP_Metrics.getTokens.Token;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
// import a spellcheck library

public class CodeAnalysis {

    public static ArrayList<Score> CodeAnalysis(ArrayList<Token> tokenList) {

        ArrayList<Score> scores = new ArrayList<>();

        scores.addAll(methodNameAnalysis(tokenList.stream().filter(getTokens.BracePair.class::isInstance).map(getTokens.BracePair.class::cast).toList()));
        scores.addAll(methodCommentAnalysis(tokenList));
//        codeLineTypeAnalysis(tokenList);
        return new ArrayList<>();
    }


    public static ArrayList<Score> codeLineTypeAnalysis(List<Token> tokens) {
        ArrayList<Score> scores = new ArrayList<>();
        System.out.println("This should check each line and give it a type" +
                "\ne.g. class, comment, method, whitespace");
        String[] lineType = new String[]{"class", "comment", "method", "code", "whitespace"};

        // return hashmap?
        // key = line number
        // object: string from lineType
        return scores;
    }

    public static ArrayList<Score> methodNameAnalysis(List<getTokens.BracePair> bracePairs) {
        ArrayList<Score> scores = new ArrayList<>();
        for (getTokens.BracePair bracePair : bracePairs) {
            if (bracePair.type == "METHOD") {
                String methodName = bracePair.name;
                String[] methodWords = methodName.split("(?=[A-Z])");
                char[] nameChars = methodName.toCharArray();
                boolean upperCaseFound = false;

                if (methodName.length() <= 4 || methodName.length() > 20){
                    scores.add(new Score(20, "Method name is not between 2-20 characters"));
                }
                if (!(isLowerCase(nameChars[0]))){
                    scores.add(new Score(20, "First character is not lower case"));
                }

                for (int i = 0; i < nameChars.length; i++){
                    if (isUpperCase(nameChars[i])){
                        upperCaseFound = true;
                        break;
                    }
                }

                if (!(upperCaseFound)){
                    scores.add(new Score(20, "No uppercase found"));
                }

                if (!(methodWords.length >= 2)){
                    scores.add(new Score(20, "Needs at least 2 words"));
                }

            }

        }
        return scores;
    }

    public static ArrayList<Score> methodCommentAnalysis(List<Token> tokens) {
        ArrayList<Score> scores = new ArrayList<>();
        return scores;
    }

}
