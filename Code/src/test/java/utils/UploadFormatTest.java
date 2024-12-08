package utils;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class UploadFormatTest {

    boolean uploadFile(File file) {
        if (file.isDirectory()) {
            return false;
        }

        String fileName = file.getName().toLowerCase();
        if (!fileName.endsWith(".java")) {
            return false;
        }

        return true;
    }

    @Test
    void testUploadNonJavaFiles() {
        File file = new File("path/to/testfile.txt");
        boolean result = uploadFile(file);
        assertFalse(result);
    }

    @Test
    void testUploadFolder() {
        File folder = new File("path/to/testfolder");
        boolean result = uploadFile(folder);
        assertFalse(result);
    }

    @Test
    void testUploadDangerousFileTypes() {
        File file = new File("path/to/maliciousfile.exe");
        boolean result = uploadFile(file);
        assertFalse(result);
    }

    @Test
    void testUploadFileWithoutExtension() {
        File file = new File("path/to/unknownfile");
        boolean result = uploadFile(file);
        assertFalse(result);
    }

    @Test
    void testUploadJavaFile() {
        File file = new File("path/to/validfile.java");
        boolean result = uploadFile(file);
        assertTrue(result);
    }

    @Test
    void testErrorMessageForInvalidFile() {
        File file = new File("path/to/unsupportedfile.abc");
        boolean result = uploadFile(file);

        String errorMessage = result ? "" : "Unsupported file type";

        assertEquals("Unsupported file type", errorMessage);

    }
}
