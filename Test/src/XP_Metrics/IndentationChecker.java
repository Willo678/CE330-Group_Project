package XP_Metrics;

import utils.Tokeniser;
import java.util.ArrayList;

public class IndentationChecker {

    public static double checkIndentation(ArrayList<Tokeniser.Token> tokens) {
        double score = 100.0;
        int maxIndentLevel = 3;

        for (Tokeniser.Token token : tokens) {
            if (token.type == Tokeniser.TokenType.NULL) { // Process general code lines
                int indentLevel = calculateIndentation(token.contents);

                if (indentLevel > maxIndentLevel) {
                    System.out.println("Error: Line " + token.line + " exceeds maximum indentation level.");
                    score -= 5; // Deduct points for excessive indentation
                }

                if (isIfElseBlock(token.contents) && !containsMethodCall(token.contents)) {
                    System.out.println("Error: Line " + token.line + " contains raw logic in if/else block.");
                    score -= 10; // Deduct points for raw logic in if/else blocks
                }
            }
        }

        return score;
    }

    private static int calculateIndentation(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                count++;
            } else {
                break;
            }
        }
        return count / 4;
    }

    private static boolean isIfElseBlock(String line) {
        return line.trim().startsWith("if") || line.trim().startsWith("else");
    }

    private static boolean containsMethodCall(String line) {
        return line.contains("(") && line.contains(")");
    }
}