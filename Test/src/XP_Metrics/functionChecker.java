package XP_Metrics;

import utils.Tokeniser;
import java.util.ArrayList;

public class functionChecker {

    public static double checkFunctionStructure(ArrayList<Tokeniser.Token> tokens) {
        double score = 100.0;

        for (Tokeniser.Token token : tokens) {
            if (token.type == Tokeniser.TokenType.METHOD) {
                // Check method name for camelCase
                String methodName = extractMethodName(token.contents);
                if (!isCamelCase(methodName)) {
                    System.out.println("Error: Method name '" + methodName + "' does not follow camelCase.");
                    score -= 10; // Deduct points for incorrect naming
                }

                // Check method length
                int methodLength = token.end - token.start + 1;
                if (methodLength > 10) {
                    System.out.println("Error: Method '" + methodName + "' exceeds 10 lines.");
                    score -= 10; // Deduct points for excessive method length
                }
            }
        }

        return score;
    }

    private static boolean isCamelCase(String name) {
        return name.matches("^[a-z][a-zA-Z0-9]*$");
    }

    private static String extractMethodName(String line) {
        // Extract method name by skipping modifiers and return type
        String[] parts = line.split("\\s+");
        for (String part : parts) {
            if (part.contains("(")) {
                return part.substring(0, part.indexOf("("));
            }
        }
        return "";
    }
}