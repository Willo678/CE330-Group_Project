package XP_Metrics;

import XP_Metrics.getTokens.Token;
import XP_Metrics.getTokens.BracePair;

import java.util.ArrayList;
import java.util.List;

public class CodeAnalysisTest {

    public static void main(String[] args) {
        testMethodNameAnalysis();
        testMethodCommentAnalysis();
    }

    public static void testMethodNameAnalysis() {
        System.out.println("Running testMethodNameAnalysis...");

        List<BracePair> bracePairs = new ArrayList<>();
        bracePairs.add(new BracePair(1, "short", "METHOD", 0, 5)); // Too short
        bracePairs.add(new BracePair(2, "ThisMethodNameIsWayTooLongToBeValid", "METHOD", 0, 5)); // Too long
        bracePairs.add(new BracePair(3, "invalidMethod", "METHOD", 0, 5)); // Missing uppercase
        bracePairs.add(new BracePair(4, "ValidMethodName", "METHOD", 0, 5)); // Valid
        bracePairs.add(new BracePair(5, "alllowercase", "METHOD", 0, 5)); // No uppercase

        ArrayList<Score> scores = CodeAnalysis.methodNameAnalysis(bracePairs);

        for (Score score : scores) {
            System.out.println(score);
        }
    }

    public static void testMethodCommentAnalysis() {
        System.out.println("Running testMethodCommentAnalysis...");

        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(1, "METHOD", "method1", 0));  // Add the int argument
        tokens.add(new Token(2, "CODE", "System.out.println();", 0));
        tokens.add(new Token(3, "COMMENT", "This is a comment.", 0));

        tokens.add(new Token(4, "METHOD", "method2", 0));
        tokens.add(new Token(5, "CODE", "System.out.println();", 0));

        tokens.add(new Token(6, "METHOD", "method3", 0));
        tokens.add(new Token(7, "COMMENT", "Comment line 1", 0));
        tokens.add(new Token(8, "COMMENT", "Comment line 2", 0));
        tokens.add(new Token(9, "COMMENT", "Comment line 3", 0)); // Excessive comments

        ArrayList<Score> scores = CodeAnalysis.methodCommentAnalysis(tokens);

        for (Score score : scores) {
            System.out.println(score);
        }
    }
}
