package XP_Metrics;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class classCheckerTest {

    @Test
    public void testImportsAtTop() {
        String path = "testFiles/ImportsAtTop.java";
        ArrayList<getTokens.Token> tokens = new ArrayList<>();
        tokens.add(new getTokens.Token("Import", 1, 6));
        ArrayList<Score> scores = classChecker.checkImports(path, tokens);

        assertEquals(1, scores.size());
        assertEquals(0, scores.get(0).getScore());
        assertEquals("Import at top of the class", scores.get(0).getMessage());
    }

    @Test
    public void testImportsNotAtTop() {
        String path = "testFiles/ImportsNotAtTop.java";
        ArrayList<getTokens.Token> tokens = new ArrayList<>();
        tokens.add(new getTokens.Token("Import", 5, 8));
        ArrayList<Score> scores = classChecker.checkImports(path, tokens);

        assertEquals(1, scores.size());
        assertEquals(10, scores.get(0).getScore());
        assertEquals("Import not at top of class", scores.get(0).getMessage());
    }

    @Test
    public void testNoImports() {
        // 测试文件中没有 import 语句
        String path = "testFiles/NoImports.java";
        ArrayList<getTokens.Token> tokens = new ArrayList<>();
        ArrayList<Score> scores = classChecker.checkImports(path, tokens);

        assertEquals(0, scores.size());
    }
}
