package XP_Metrics;

import java.util.List;
import java.util.Objects;

import XP_Metrics.getTokens.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class getTokensTest {

    List<Token> tokens;

    @BeforeEach
    void setUp() {
         tokens = getTokens.getTokens("src/test/java/test_sample_data/sampleCode1.java");
    }

    @Test
    void getBracePairs() {
        List<BracePair> braces = getTokens.getBracePairs("src/test/java/test_sample_data/sampleCode1.java");
        assertIterableEquals(tokens.stream().filter(BracePair.class::isInstance).toList(), braces);

        assertEquals("sampleCode1".toUpperCase(), braces.getFirst().name);
        assertEquals(3, braces.size());
    }

    @Test
    void getComments() {
        List<Token> comments = getTokens.getComments("src/test/java/test_sample_data/sampleCode1.java");
        assertIterableEquals(tokens.stream().filter(x -> (Objects.equals(((Token) x).type, "Comment"))).toList(), comments);

        assertEquals(1, comments.size());
    }



    @AfterEach
    void tearDown() {
    }
}