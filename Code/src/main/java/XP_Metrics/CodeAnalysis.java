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

        scores.addAll(methodNameAnalysis(tokenList.stream()
                .filter(getTokens.BracePair.class::isInstance)
                .map(getTokens.BracePair.class::cast)
                .toList()));
        scores.addAll(methodCommentAnalysis(tokenList));

        return scores;
    }


    public static ArrayList<Score> methodNameAnalysis(List<getTokens.BracePair> bracePairs) {
        ArrayList<Score> scores = new ArrayList<>();
        for (getTokens.BracePair bracePair : bracePairs) {
            if (bracePair.name == null) {
                System.out.println("Null method name detected for bracePair: " + bracePair);
            }
            if (bracePair.type != null && bracePair.type.equals("METHOD")) {
                String methodName = bracePair.name;
                if (methodName != null) {
                    String[] methodWords = methodName.split("(?=[A-Z])");
                    char[] nameChars = methodName.toCharArray();
                    boolean upperCaseFound = false;

                    if (methodName.length() < 4) {
                        int score = 4 - methodName.length();
                        scores.add(new Score(score, "Method name is not between 4-20 characters"));
                    }
                    if(methodName.length() > 20){
                        int score = methodName.length() - 20;
                        scores.add(new Score(score, "Method name is not between 4-20 characters"));
                    }
                    if (!(isLowerCase(nameChars[0]))) {
                        scores.add(new Score(5, "First character is not lower case"));
                    }
                    for (char c : nameChars) {
                        if (isUpperCase(c)) {
                            upperCaseFound = true;
                            break;
                        }
                    }
                    if (!(upperCaseFound)) {
                        scores.add(new Score(5, "No uppercase found"));
                    }
                    if (!(methodWords.length >= 2)) {
                        scores.add(new Score(5, "Needs at least 2 words"));
                    }
                }
            }
        }
        return scores;
    }

    public static ArrayList<Score> methodCommentAnalysis(List<Token> tokens) {
        ArrayList<Score> scores = new ArrayList<>();

        for (int tokenIndex = 0; tokenIndex < tokens.size(); tokenIndex++) {
            Token token = tokens.get(tokenIndex);
            if (token.type != null && token.type.equals("METHOD")) {
                boolean commentFound = false;
                boolean endMethod = false;
                int searchIndex = tokenIndex + 1;

                while (!commentFound && !endMethod && searchIndex < tokens.size()) {
                    Token searchToken = tokens.get(searchIndex);
                    if (searchToken.type != null) {
                        if (searchToken.type.equals("COMMENT")) {
                            commentFound = true;
                            if (searchIndex + 2 < tokens.size()) {
                                Token nextToken = tokens.get(searchIndex + 1);
                                Token nextNextToken = tokens.get(searchIndex + 2);
                                if (nextToken.type != null && nextNextToken.type != null &&
                                        nextToken.type.equals("COMMENT") &&
                                        nextNextToken.type.equals("COMMENT")) {
                                    scores.add(new Score(3, "Method comment block too large"));
                                }
                            }
                        } else if (searchToken.type.equals("METHOD")) {
                            endMethod = true;
                        }
                    }
                    searchIndex++;
                }

                if (!commentFound) {
                    scores.add(new Score(5, "Method needs at least one comment"));
                }
            }
        }
        return scores;
    }
}
