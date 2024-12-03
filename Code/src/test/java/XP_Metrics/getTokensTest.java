package XP_Metrics;

import java.util.List;

import XP_Metrics.getTokens.*;

import static org.junit.jupiter.api.Assertions.*;

class getTokensTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        List<Token> tokens = getTokens.getTokens("src/test/java/test_sample_data/sampleCode1.java");
        List<BracePair> braces = getTokens.getBracePairs("src/test/java/test_sample_data/sampleCode1.java");
        List<Token> comments = getTokens.getComments("src/test/java/test_sample_data/sampleCode1.java");
    }



    @org.junit.jupiter.api.Test
    void getBracePairs() {
    }

    @org.junit.jupiter.api.Test
    void getComments() {
    }



    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
}