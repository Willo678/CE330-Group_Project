package XP_Metrics;
// written by sj22795
// code cleanliness check first draft


import XP_Metrics.getTokens.Token;

import java.util.ArrayList;
import java.util.List;

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


    public static ArrayList<Score> methodNameAnalysis(List<getTokens.BracePair> bracePairs) {
        ArrayList<Score> scores = new ArrayList<>();
        for (getTokens.BracePair bracePair : bracePairs) {
            if (bracePair.type == "METHOD") {
                String methodName = bracePair.name;
                String[] methodWords = methodName.split("(?=[A-Z])");
                char[] nameChars = methodName.toCharArray();
                boolean upperCaseFound = false;

                if (methodName.length() <= 4 || methodName.length() > 20) {
                    scores.add(new Score(20, "Method name is not between 2-20 characters"));
                }
                if (!(isLowerCase(nameChars[0]))) {
                    scores.add(new Score(20, "First character is not lower case"));
                }

                for (int i = 0; i < nameChars.length; i++) {
                    if (isUpperCase(nameChars[i])) {
                        upperCaseFound = true;
                        break;
                    }
                }

                if (!(upperCaseFound)) {
                    scores.add(new Score(20, "No uppercase found"));
                }

                if (!(methodWords.length >= 2)) {
                    scores.add(new Score(20, "Needs at least 2 words"));
                }

            }

        }
        return scores;
    }

    public static ArrayList<Score> methodCommentAnalysis(List<Token> tokens) {
        ArrayList<Score> scores = new ArrayList<>();
        int tokenIndex = 0;


        for (; tokenIndex < tokens.size(); tokenIndex++) {

            if (tokens.get(tokenIndex).type == "METHOD") {
                boolean commentFound = false;
                boolean endMethod = false;

                while (!commentFound && !endMethod) {
                    tokenIndex++;

                    if (tokens.get(tokenIndex).type == "COMMENT") {
                        commentFound = true;

                        if (tokenIndex + 2 <= tokens.size()) {
                            if (tokens.get(tokenIndex + 1).type == "COMMENT" && tokens.get(tokenIndex + 2).type == "COMMENT") {

                                scores.add(new Score(20, "Method comment block too large"));
                                System.out.println("method comment block");
                            }
                        }
                    }
                    if (tokens.get(tokenIndex).type == "METHOD") {

                        endMethod = true;
                    }
                }
                if (!commentFound) {

                    scores.add(new Score(20, "Method needs at least one comment"));
                    System.out.println("method comment not found");
                }
            }

            if (tokenIndex < tokens.size() - 1) {
                tokenIndex++;
            }

        }

        return scores;
    }

}
