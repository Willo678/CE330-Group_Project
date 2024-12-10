package userInterfaceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import userInterface.MetricsTracker;
import userInterface.ProgramWindow;
import userInterface.UI_Panels.targetSelectionUI;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TargetSelectionUITest {

    private ProgramWindow testProgramWindow;
    private targetSelectionUI targetUI;

    @BeforeEach
    void setUp() {
        testProgramWindow = new ProgramWindow();
        targetUI = new targetSelectionUI(testProgramWindow);

        MetricsTracker.selectProject(null);
        MetricsTracker.setFocusedFile(0);
    }

    @Test
    void testInitialState() {
        assertNotNull(targetUI);
        assertTrue(targetUI.getLayout() instanceof BorderLayout);

        Component[] components = targetUI.getComponents();
        assertEquals(2, components.length);
        assertTrue(components[0] instanceof JPanel);
        assertTrue(components[1] instanceof JPanel);
    }

    @Test
    void testFileSelectorVisibilityAfterProjectSelection() {
        MetricsTracker.selectProject("test/project/path");
        targetUI.revalidate();
        targetUI.repaint();
        JPanel selectionPanel = (JPanel) targetUI.getComponents()[0];
        Component[] selectionComponents = selectionPanel.getComponents();
        boolean fileSelectorVisible = false;
        for (Component comp : selectionComponents) {
            if (comp instanceof JPanel) {
                for (Component innerComp : ((JPanel) comp).getComponents()) {
                    if (innerComp instanceof JComponent && innerComp.getClass().getSimpleName().equals("FileSelector")) {
                        fileSelectorVisible = innerComp.isVisible();
                    }
                }
            }
        }
        assertTrue(fileSelectorVisible, "File selector should be visible after project selection.");
    }

    @Test
    void testMetricsUpdateLogic() {
        MetricsTracker.selectProject("test/project/path");
        MetricsTracker.setFocusedFile(1);
        targetUI.updateMetrics();
        Component[] components = targetUI.getComponents();
        JPanel metricsContainer = (JPanel) components[1];
        Component[] metricsComponents = metricsContainer.getComponents();
        assertNotNull(metricsComponents[0], "Metrics panel should not be null.");
        assertTrue(metricsComponents.length > 0, "Metrics panel should contain components.");
    }

    @Test
    void testMetricsContentAccuracy() {
        MetricsTracker.selectProject("test/project/path");
        MetricsTracker.setFocusedFile(2);
        targetUI.updateMetrics();
        JPanel metricsContainer = (JPanel) targetUI.getComponents()[1];
        JScrollPane detailsScrollPane = (JScrollPane) metricsContainer.getComponents()[1];
        JTextArea detailsArea = (JTextArea) detailsScrollPane.getViewport().getView();
        String detailsContent = detailsArea.getText();
        assertNotNull(detailsContent, "Details content should not be null.");
        assertTrue(detailsContent.contains("Indentation:"));
        assertTrue(detailsContent.contains("Class Structure:"));
        assertTrue(detailsContent.contains("Method Structure Length:"));
    }

    @Test
    void testAdherenceLabelContent() {
        MetricsTracker.selectProject("test/project/path");
        MetricsTracker.setFocusedFile(3);

        targetUI.updateMetrics();

        JPanel metricsContainer = (JPanel) targetUI.getComponents()[1];
        JLabel adherenceLabel = (JLabel) metricsContainer.getComponents()[2];

        assertNotNull(adherenceLabel);
        assertFalse(adherenceLabel.getText().isEmpty(), "Adherence label should not be empty.");
        assertTrue(adherenceLabel.getText().contains("adherence"));
    }
}