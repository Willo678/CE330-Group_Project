package XP_Metrics;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class XPEvaluator {
    int indentationScore = 80;
    int classStructureScore = 70;
    int methodStructureScore = 60;


    public int indentationScore() {
        return indentationScore;
    }


    public int classStructureScore() {
        return classStructureScore;
    }


    public int methodStructureScore() {
        return methodStructureScore;
    }


    public int normalisedScore() {
        int[] weights = new int[]{33, 33, 33}; // Three parts, 33 points each
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
}

class XPEvaluatorTest {

    @Test
    void testNormalisedScore() {

        XPEvaluator evaluator = new XPEvaluator();

        int expectedScore = 68;

        int actualScore = evaluator.normalisedScore();

        assertEquals(expectedScore, actualScore, "The normalized score calculation is incorrect.");
    }
}











