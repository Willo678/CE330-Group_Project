package userInterfaceTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import userInterface.UI_Panels.targetSelectionUI;
import userInterface.UI_Panels.codeMetricsUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;



public class TargetSelectUITest {

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
        JButton selectButton = getButtonByText(targetUI, "SELECT");

        assertNotNull(selectButton, "Select button should be initialized");

        File testDir = new File(System.getProperty("java.io.tmpdir"));
        pathField.setText(testDir.getAbsolutePath());
        assertEquals(testDir.getAbsolutePath(), pathField.getText(), "Path field should update with selected directory");
    }

    @Test
    void testConfirmButtonWithInvalidPath() {
        JTextField pathField = getPathField();
        JButton confirmButton = getButtonByText(targetUI, "CONFIRM");

        assertNotNull(confirmButton, "Confirm button should be initialized");

        pathField.setText("invalid/path");
        Exception exception = assertThrows(Exception.class, () -> targetUI.processPath("invalid/path"));
        assertTrue(exception.getMessage().contains("No Java files found"), "Should show error for invalid path");
    }

    private JTextField getPathField() {
        return getField(targetUI, "pathField", JTextField.class);
    }

    private JButton getButtonByText(JPanel panel, String buttonText) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(buttonText)) {
                return (JButton) component;
            } else if (component instanceof JPanel) {
                JButton button = getButtonByText((JPanel) component, buttonText);
                if (button != null) return button;
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