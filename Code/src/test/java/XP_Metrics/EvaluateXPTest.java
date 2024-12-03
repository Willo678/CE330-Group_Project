package XP_Metrics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class EvaluateXPTest {


    @Test
    void normalisedScore() {
        int[][] weights = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int weightTotal = Arrays.stream(weights).mapToInt(o -> Arrays.stream(o).sum()).sum();
        int result = 0;
        for (int[] weight : weights) {
            result += (int) (0.25 * (Arrays.stream(weight).sum() / weightTotal));
        }
        assertEquals(11.25, result);


    }


    @BeforeEach
    void setUp() {
        List<getTokens.Token> tokens = getTokens.getTokens("src/test/java/test_sample_data/sampleCode1.java");
        List<getTokens.BracePair> braces = getTokens.getBracePairs("src/test/java/test_sample_data/sampleCode1.java");
        List<getTokens.Token> comments = getTokens.getComments("src/test/java/test_sample_data/sampleCode1.java");
    }
}

