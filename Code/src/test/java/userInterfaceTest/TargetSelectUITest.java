package userInterfaceTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import userInterface.ProgramWindow;
import userInterface.UI_Panels.targetSelectionUI;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class TargetSelectUITest {
    private targetSelectionUI targetUI;
    private ProgramWindow mockWindow;

    @Before
    public void setUp() {
        mockWindow = new ProgramWindow();
        targetUI = new targetSelectionUI(mockWindow);
    }

    @Test
    public void testInitialState() {
        assertNotNull("targetUI should not be null", targetUI);
        assertEquals("Initial background color should be light cream",
                new Color(250, 250, 245), targetUI.getBackground());

        assertNotNull("Should have a border", targetUI.getBorder());

        try {
            Field adherenceLabelField = targetSelectionUI.class.getDeclaredField("adherenceLabel");
            adherenceLabelField.setAccessible(true);
            JLabel adherenceLabel = (JLabel) adherenceLabelField.get(targetUI);
            assertEquals("Initial adherence label should be 'Ready'", "Ready", adherenceLabel.getText());
        } catch (Exception e) {
            fail("Failed to access adherenceLabel: " + e.getMessage());
        }
    }

    @Test
    public void testMetricsPanelCreation() throws NoSuchFieldException, IllegalAccessException {
        Field metricsPanelField = targetSelectionUI.class.getDeclaredField("metricsPanel");
        metricsPanelField.setAccessible(true);
        JPanel metricsPanel = (JPanel) metricsPanelField.get(targetUI);

        assertNotNull("Metrics panel should not be null", metricsPanel);
        assertEquals("Metrics panel should have BoxLayout",
                BoxLayout.Y_AXIS,
                ((BoxLayout)metricsPanel.getLayout()).getAxis());
        assertEquals("Metrics panel should have correct background",
                new Color(255, 255, 250),
                metricsPanel.getBackground());
    }

    @Test
    public void testDetailsAreaCreation() throws NoSuchFieldException, IllegalAccessException {
        Field detailsAreaField = targetSelectionUI.class.getDeclaredField("detailsArea");
        detailsAreaField.setAccessible(true);
        JTextArea detailsArea = (JTextArea) detailsAreaField.get(targetUI);

        assertNotNull("Details area should not be null", detailsArea);
        assertFalse("Details area should not be editable", detailsArea.isEditable());
        assertEquals("Details area should have correct font size",
                14,
                detailsArea.getFont().getSize());
    }



    @Test
    public void testScorePanelCreation() throws Exception {
        java.lang.reflect.Method createScorePanelMethod =
                targetSelectionUI.class.getDeclaredMethod(
                        "createScorePanel",
                        String.class,
                        double.class,
                        Color.class
                );
        createScorePanelMethod.setAccessible(true);

        JPanel scorePanel = (JPanel) createScorePanelMethod.invoke(
                targetUI,
                "Test Score",
                85.5,
                Color.ORANGE
        );

        assertNotNull("Score panel should not be null", scorePanel);
        assertEquals("Score panel should have FlowLayout",
                FlowLayout.class,
                scorePanel.getLayout().getClass());

        Component[] components = scorePanel.getComponents();
        assertTrue("Score panel should contain at least one component",
                components.length > 0);
        assertTrue("First component should be a JLabel",
                components[0] instanceof JLabel);

        JLabel scoreLabel = (JLabel) components[0];
        assertTrue("Label should contain the score value",
                scoreLabel.getText().contains("85.5%"));
    }

    @Test
    public void testFileSelectorVisibility() throws NoSuchFieldException, IllegalAccessException {
        Field fileSelectorField = targetSelectionUI.class.getDeclaredField("fileSelector");
        fileSelectorField.setAccessible(true);
        JComponent fileSelector = (JComponent) fileSelectorField.get(targetUI);

        assertFalse("File selector should be initially invisible",
                fileSelector.isVisible());
    }

    @Test
    public void testTotalScoreDialInitialization() throws NoSuchFieldException, IllegalAccessException {
        Field totalScoreDialField = targetSelectionUI.class.getDeclaredField("totalScoreDial");
        totalScoreDialField.setAccessible(true);
        assertNotNull("Total score dial should not be null",
                totalScoreDialField.get(targetUI));
    }
}