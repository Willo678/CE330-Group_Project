package userInterfaceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import userInterface.codeMetricsUI;
import userInterface.targetSelectionUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.InvalidPathException;

import static org.junit.jupiter.api.Assertions.*;

class TargetSelectionUITest {

    private targetSelectionUI targetUI;

    @BeforeEach
    void setUp() {
        codeMetricsUI metricsUI = new codeMetricsUI();
        targetUI = new targetSelectionUI(metricsUI);
    }

    @Test
    void testPathFieldInitialization() {
        JTextField pathField = getPathField();
        assertNotNull(pathField, "Path field should be initialized");
        assertEquals("Select a project directory:", pathField.getText(), "Default hint text should match");
    }

    @Test
    void testSelectButtonUpdatesPathField() {
        JTextField pathField = getPathField();
        JButton selectButton = getButtonByText("SELECT");

        // Simulate selecting a valid directory
        File testDir = new File(System.getProperty("java.io.tmpdir"));
        selectButton.doClick();

        // This test assumes manual interaction; modify it as needed for integration tests
        pathField.setText(testDir.getAbsolutePath());
        assertEquals(testDir.getAbsolutePath(), pathField.getText(), "Path field should update with the selected directory");
    }

    @Test
    void testConfirmButtonWithInvalidPath() {
        JTextField pathField = getPathField();
        JButton confirmButton = getButtonByText("CONFIRM");

        // Set an invalid path
        pathField.setText("invalid/path");
        confirmButton.doClick();

        assertThrows(InvalidPathException.class, () -> targetUI.processPath("invalid/path"),
                "processPath should throw InvalidPathException for an invalid path");
    }

    @Test
    void testProcessPathNoJavaFiles() {
        // Create a temporary empty directory
        File emptyDir = new File(System.getProperty("java.io.tmpdir"), "emptyTestDir");
        if (!emptyDir.exists()) {
            assertTrue(emptyDir.mkdir(), "Test directory should be created successfully");
        }

        // Test processPath for a directory with no Java files
        InvalidPathException exception = assertThrows(InvalidPathException.class,
                () -> targetUI.processPath(emptyDir.getAbsolutePath()),
                "Should throw exception if no Java files found");
        assertTrue(exception.getMessage().contains("No Java files found"), "Exception message should indicate no Java files");
    }

    @Test
    void testProcessPathWithValidJavaFiles() {
        // Create a temporary directory and a dummy Java file for testing
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "javaTestDir");
        File tempJavaFile = new File(tempDir, "TestFile.java");

        try {
            assertTrue(tempDir.mkdir(), "Temporary directory should be created successfully");
            assertTrue(tempJavaFile.createNewFile(), "Temporary Java file should be created successfully");

            // Test processing the valid directory
            targetUI.processPath(tempDir.getAbsolutePath());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid Java files: " + e.getMessage());
        } finally {
            // Clean up test resources
            assertTrue(tempJavaFile.delete(), "Temporary Java file should be deleted");
            assertTrue(tempDir.delete(), "Temporary directory should be deleted");
        }
    }

    // Helper methods for accessing private components
    private JTextField getPathField() {
        return getField(targetUI, "pathField", JTextField.class);
    }

    private JButton getButtonByText(String buttonText) {
        for (Component component : targetUI.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(buttonText)) {
                return (JButton) component;
            }
        }
        return null;
    }

    private <T> T getField(Object target, String fieldName, Class<T> fieldType) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return fieldType.cast(field.get(target));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}