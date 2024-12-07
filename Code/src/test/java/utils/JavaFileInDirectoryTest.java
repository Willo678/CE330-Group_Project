package utils;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaFileInDirectoryTest {

    private File testDir;

    @BeforeEach
    void setUp() {
        testDir = new File("test_directory");
        testDir.mkdir();
    }

    @AfterEach
    void tearDown() {
        for (File file : testDir.listFiles()) {
            file.delete();
        }
        testDir.delete();
    }

    @Test
    void directoryContainsJavaFile() {
        File javaFile = new File(testDir, "Test.java");
        try {
            assertTrue(javaFile.createNewFile());
        } catch (Exception e) {
            fail("Failed to create .java file");
        }

        boolean containsJava = false;
        for (File file : testDir.listFiles()) {
            if (file.getName().endsWith(".java")) {
                containsJava = true;
                break;
            }
        }

        assertTrue(containsJava, "Directory does not contain a .java file");
    }
}
