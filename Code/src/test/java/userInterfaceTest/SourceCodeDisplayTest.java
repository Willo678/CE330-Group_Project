//package userInterfaceTest;
//
//import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//import userInterface.UI_Panels.sourceCodeDisplayUI;
//
//import javax.swing.*;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.ArrayList;
//
//
//public class SourceCodeDisplayTest {
//
//    private sourceCodeDisplayUI sourceCodeUI;
//    private File tempFile;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        sourceCodeUI = new sourceCodeDisplayUI(null);
//
//        // Create a temporary file for testing
//        tempFile = File.createTempFile("testFile", ".txt");
//        Files.write(tempFile.toPath(), "Line 1\nLine 2\nLine 3".getBytes());
//    }
//
//    @Test
//    void testSetFileName() {
//        // Test if fileName can be set and retrieved correctly
//        sourceCodeUI.setFileName(tempFile.getAbsolutePath());
//        assertEquals(tempFile.getAbsolutePath(), sourceCodeUI.getFileName());
//    }
//
//    @Test
//    void testSetCurrentFolder() {
//        // Test if currentFolder can be set and retrieved correctly
//        String folderPath = tempFile.getParent();
//        sourceCodeUI.setCurrentFolder(folderPath);
//        assertEquals(folderPath, sourceCodeUI.getCurrentFolder());
//    }
//
//    @Test
//    void testGetCode() throws IOException {
//        // Test if getCode reads file content correctly
//        sourceCodeUI.setFileName(tempFile.getAbsolutePath());
//        ArrayList<String> codeLines = sourceCodeUI.getCode(tempFile.getAbsolutePath());
//
//        assertNotNull(codeLines, "The codeLines should not be null");
//        assertEquals(3, codeLines.size(), "The file should contain 3 lines");
//        assertEquals("Line 1", codeLines.get(0), "The first line should match");
//        assertEquals("Line 2", codeLines.get(1), "The second line should match");
//        assertEquals("Line 3", codeLines.get(2), "The third line should match");
//    }
//
//    @Test
//    void testGetCodeWithNonExistentFile() {
//        // Test behavior when file does not exist
//        String nonExistentFile = "nonexistentfile.txt";
//        ArrayList<String> codeLines = null;
//
//        try {
//            codeLines = sourceCodeUI.getCode(nonExistentFile);
//        } catch (Exception e) {
//            fail("Exception should not be thrown");
//        }
//
//        assertNull(codeLines, "The codeLines should be null for a non-existent file");
//    }
//
//    @Test
//    void testGUIComponents() {
//        JButton chooseSourceButton = (JButton) sourceCodeUI.getComponent(0);
//        JTextArea codeArea = (JTextArea) sourceCodeUI.getComponent(1);
//
//        assertEquals("Choose Source File", chooseSourceButton.getText(), "Button text should match");
//        assertTrue(codeArea instanceof JTextArea, "Second component should be a JTextArea");
//    }
//}