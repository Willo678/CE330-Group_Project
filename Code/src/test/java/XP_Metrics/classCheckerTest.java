package XP_Metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import XP_Metrics.getTokens.*;


public class classCheckerTest {
    List<Token> tokens;
    String path = "M:\\3rd Year\\CE320\\GROUP_PROJECT\\Code\\src\\test\\java\\test_sample_data\\sampleCode1.java";

    @BeforeEach
    void setup() {
        tokens = getTokens.getTokens(path);
    }

    @Test
    public void checkBefore() {
        int start = 0;
        int cur = 0;
        for (Token token : tokens) {
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