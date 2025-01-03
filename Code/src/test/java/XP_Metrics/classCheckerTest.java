package XP_Metrics;



import java.util.ArrayList;



import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class classCheckerTest {
    ArrayList<getTokens.Token> tokens;
    String path = "src/test/java/test_sample_data/sampleCode1.java";

    @BeforeEach
    public void setup() {
        tokens = getTokens.getTokens(path);
    }

    @Test
    public void checkBefore() {
        int start = 0;
        int cur = 0;
        for (getTokens.Token token : tokens) {
            if (token.type.equalsIgnoreCase("class")) {
                start = token.start;
            }
            if (cur < start && token.type.equalsIgnoreCase("import")) {
                assertEquals("IMPORT", token.type);
            }
            cur++;
        }
    }

    @Test
    public void checkAfter() {
        int start = 0;
        int cur = 0;
        for (getTokens.Token token : tokens) {
            if (token.type.equalsIgnoreCase("class")) {
                start = token.start;
            }
            if (cur > start) {
                assertNotEquals(token.type, "import");
            }
            cur++;
        }
    }
}