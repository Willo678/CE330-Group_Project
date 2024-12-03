package XP_Metrics;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import XP_Metrics.getTokens.*;
import org.junit.jupiter.api.BeforeEach;

class classCheckerTest {
    List<Token> tokens;
    String path = "M:\\3rd Year\\CE320\\GROUP_PROJECT\\Code\\src\\main\\java\\XP_Metrics\\indentationChecker.java";

    @BeforeEach
    void initialiseLists() {
        tokens = getTokens.getTokens(path);
    }

    @Test
    void checkBefore() {
        int start = 0;
        int cur = 0;
        for (Token token : tokens) {
            if (token.type.equalsIgnoreCase("class")) {
                start = token.start;
            }
            if (cur < start) {
                assertEquals(token.type, "import");
            }
            cur++;
        }
    }

    @Test
    void checkAfter() {
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