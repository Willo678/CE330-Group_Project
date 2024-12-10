package XP_Metrics.evaluators;
// written by sj22795
// Code cleanliness check with improved logic


import XP_Metrics.Score;
import XP_Metrics.XPEvaluator;
import XP_Metrics.getTokens.BracePair;
import XP_Metrics.getTokens.Token;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

public class CodeChecker {

    public static ArrayList<Score> CodeAnalysis(ArrayList<Token> tokenList) {
        ArrayList<Score> scores = new ArrayList<>();
        scores.addAll(methodNameAnalysis(tokenList.stream()
                .filter(BracePair.class::isInstance)
                .map(BracePair.class::cast)
                .toList()));
        scores.addAll(methodCommentAnalysis(tokenList));
        return scores;
    }

    public static ArrayList<Score> methodNameAnalysis(List<BracePair> bracePairs) {
        ArrayList<Score> scores = new ArrayList<>();

        for (BracePair bracePair : bracePairs) {
            if (bracePair.name == null) {
                //System.out.println("Null method name detected for bracePair: " + bracePair);
            }

            if (bracePair.type != null && bracePair.type.equals("METHOD")) {
                String methodName = bracePair.name;
                if (methodName != null) {
                    String[] methodWords = methodName.split("(?=[A-Z])");
                    char[] nameChars = methodName.toCharArray();
                    boolean upperCaseFound = false;
                    //System.out.println(bracePair);
                    //System.out.println(nameChars);

                    if (methodName.length() < 4) {
                        int score = 4 - methodName.length();
                        scores.add(new Score(score, "Method name is too short (minimum 4 characters)"));
                    }
                    if (methodName.length() > 20) {
                        int score = methodName.length() - 20;
                        scores.add(new Score(score, "Method name is too long (maximum 20 characters)"));
                    }

                    if (!isLowerCase(nameChars[0])) {
                        scores.add(new Score(5, "First character of method name is not lowercase"));
                    }

                    for (char c : nameChars) {
                        if (isUpperCase(c)) {
                            upperCaseFound = true;
                            break;
                        }
                    }
                    if (!upperCaseFound) {
                        scores.add(new Score(5, "Method name does not contain any uppercase letter"));
                    }

                    if (methodWords.length < 2) {
                        scores.add(new Score(5, "Method name should contain at least two words"));
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
                    //scores.add(new Score(5, "Method should have at least one comment"));
                }
            }
        }
        return scores;
    }

//    public static void main(String[] args) {
//        XPEvaluator evaluator = new XPEvaluator("Code/src/main/java/XP_Metrics/getTokens.java");
//        evaluator.methodStructureScore();
//        CodeChecker checker = new CodeChecker();
//        ArrayList<Score> scores = checker.CodeAnalysis(evaluator.tokenList);
//        for (Score score : scores) {
//            System.out.println(score);
//        }
//    }
}