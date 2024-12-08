package XP_Metrics;

import XP_Metrics.evaluators.indentationChecker;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;


class indentationCheckerTest {

    String path1 = "src/test/java/test_sample_data/sampleCode1.java";
    String path2 = "src/test/java/test_sample_data/sampleCode2.java";
    List<getTokens.BracePair> braceList;


    @Test
    void checkIndentationSimple() {
        braceList = getTokens.getBracePairs(path1);
        List<Score> scores = indentationChecker.checkIndentation(braceList);
        assertEquals("[]", scores.toString());
    }

    @Test
    void checkIndentationComplex() {
        braceList = getTokens.getBracePairs(path2);
        List<Score> scores = indentationChecker.checkIndentation(braceList);
        assertEquals("[[14, reason=Method at line 83 is too big (72 lines)], [9, reason=Control statement at line 100 is too big (47 lines)], [7, reason=Control statement at line 111 is too big (35 lines)], [4, reason=Control statement at line 123 is too big (22 lines)], [20, reason=Method too nested, at line 131], [20, reason=Method too nested, at line 139], [4, reason=Method at line 177 is too big (24 lines)], [3, reason=Control statement at line 179 is too big (19 lines)], [3, reason=Control statement at line 181 is too big (16 lines)]]", scores.toString());
    }
}