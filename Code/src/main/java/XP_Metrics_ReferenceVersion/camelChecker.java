package XP_Metrics_ReferenceVersion;

import java.util.ArrayList;

public class camelChecker {

    public static double checkCamelCase(ArrayList<TokeniserTest.Token> tokens) {
        double score = 100.0;

        for (TokeniserTest.Token token : tokens) {
            if (token.type == TokeniserTest.TokenType.METHOD) {
                String methodName = extractMethodName(token.contents);
                if (!isCamelCase(methodName)) {
                    System.out.println("Error: Method name '" + methodName + "' does not follow camelCase conventions.");
                    score -= 10;
                }
            }
        }

        return score;
    }

    private static boolean isCamelCase(String name) {
        return name.matches("^[a-z][a-zA-Z0-9]*$");
    }

    private static String extractMethodName(String line) {
        String[] parts = line.split("\\s+");
        for (String part : parts) {
            if (part.contains("(")) {
                return part.substring(0, part.indexOf("("));
            }
        }
        return "";
    }
}