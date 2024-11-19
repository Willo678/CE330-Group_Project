package XP_Metrics;

import utils.Tokeniser;
import java.util.ArrayList;

public class classChecker {

    public static double checkClassStructure(ArrayList<Tokeniser.Token> tokens) {
        boolean importsAtTop = true;
        boolean firstClassFound = false;
        double score = 100.0;

        for (Tokeniser.Token token : tokens) {
            if (token.type == Tokeniser.TokenType.CLASS) {
                firstClassFound = true;
            } else if (token.type == Tokeniser.TokenType.IMPORT) {
                if (firstClassFound) {
                    importsAtTop = false;
                    break;
                }
            }
        }

        if (!importsAtTop) {
            System.out.println("Error: Import statements are not at the top of the file.");
            score -= 25; // Deduct points for misplaced imports
        }

        return score; // Return the score for class structure compliance
    }
}
