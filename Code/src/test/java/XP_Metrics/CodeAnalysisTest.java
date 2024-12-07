package XP_Metrics;

import org.junit.Test;
import static org.junit.Assert.*;

public class CodeAnalysisTest {

    @Test
    public void testMethodCommentAnalysis() {
        int methodCommentCount = analyzeMethodComments("public void testMethod() { // test method }");
        assertEquals(1, methodCommentCount);

        methodCommentCount = analyzeMethodComments("public void anotherMethod() { }");
        assertEquals(0, methodCommentCount);
    }

    @Test
    public void testMethodNameAnalysis() {
        int methodCount = analyzeMethodNames("public void testMethod() { } public void anotherMethod() { }");
        assertEquals(2, methodCount);

        methodCount = analyzeMethodNames("public void method1() { } public void method2() { } public void method3() { } public void method4() { } public void method5() { }");
        assertEquals(5, methodCount);
    }

    private int analyzeMethodComments(String code) {
        int count = 0;
        if (code.contains("//")) {
            count++;
        }
        return count;
    }

    private int analyzeMethodNames(String code) {
        int count = 0;
        String[] methods = code.split("void");
        count = methods.length - 1;
        return count;
    }
}
