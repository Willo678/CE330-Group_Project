package userInterfaceTest;

import org.junit.jupiter.api.*;
import userInterface.MetricsTracker;
import userInterface.ProgramWindow;
import userInterface.UI_Panels.targetSelectionUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TargetSelectionUITest {

    private ProgramWindow testProgramWindow;
    private targetSelectionUI targetUI;

    @BeforeAll
    void setupEnvironment() {
        File testProject = new File("test/project/path");
        if (!testProject.exists()) {
            assertTrue(testProject.mkdirs(), "Failed to create test project directory.");
        }

        File testFile = new File(testProject, "Example.java");
        try {
            if (!testFile.exists()) {
                assertTrue(testFile.createNewFile(), "Failed to create test Java file.");
            }
        } catch (Exception e) {
            fail("Exception during test file setup: " + e.getMessage());
        }
    }

    @AfterAll
    void cleanupEnvironment() {
        deleteDirectory(new File("test/project/path"));
    }

    @BeforeEach
    void setUp() {
        testProgramWindow = new ProgramWindow();
        targetUI = new targetSelectionUI(testProgramWindow);

        MetricsTracker.selectProject("test/project/path");
        MetricsTracker.setFocusedFile(0);
    }

    @Test
    void testInitialState() {
        assertNotNull(targetUI);
        assertTrue(targetUI.getLayout() instanceof BorderLayout, "Layout should be BorderLayout.");

        Component[] components = targetUI.getComponents();
        assertEquals(2, components.length, "Expected two components: selection panel and metrics container.");

        JPanel selectionPanel = (JPanel) components[0];
        Component[] selectionComponents = selectionPanel.getComponents();
        boolean fileSelectorVisible = false;
        for (Component comp : selectionComponents) {
            if (comp instanceof JComponent && comp.getClass().getSimpleName().equals("FileSelector")) {
                fileSelectorVisible = comp.isVisible();
            }
        }
        assertFalse(fileSelectorVisible, "File selector should be hidden initially.");
    }

    @Test
    void testProjectSelectionShowsFileSelector() {
        MetricsTracker.selectProject("test/project/path");
        targetUI.revalidate();
        targetUI.repaint();

        JPanel selectionPanel = (JPanel) targetUI.getComponents()[0];
        Component[] selectionComponents = selectionPanel.getComponents();
        boolean fileSelectorVisible = false;
        for (Component comp : selectionComponents) {
            if (comp instanceof JComponent && comp.getClass().getSimpleName().equals("FileSelector")) {
                fileSelectorVisible = comp.isVisible();
            }
        }
        assertTrue(fileSelectorVisible, "File selector should be visible after project selection.");
    }

    @Test
    void testMetricsUpdateLogic() {

        MetricsTracker.setFocusedFile(1);

        targetUI.updateMetrics();

        // 检查 Metrics 面板内容
        JPanel metricsContainer = (JPanel) targetUI.getComponents()[1];
        Component[] metricsComponents = metricsContainer.getComponents();
        assertTrue(metricsComponents.length > 0, "Metrics panel should be updated.");
    }

    @Test
    void testMetricsContent() {

        MetricsTracker.setFocusedFile(2);

        targetUI.updateMetrics();


        JPanel metricsContainer = (JPanel) targetUI.getComponents()[1];
        JScrollPane detailsScrollPane = (JScrollPane) metricsContainer.getComponents()[1];
        JTextArea detailsArea = (JTextArea) detailsScrollPane.getViewport().getView();
        String detailsContent = detailsArea.getText();

        assertNotNull(detailsContent, "Details content should not be null.");
        assertTrue(detailsContent.contains("Indentation:"), "Details should include indentation score.");
        assertTrue(detailsContent.contains("Class Structure:"), "Details should include class structure score.");
        assertTrue(detailsContent.contains("Method Structure Length:"), "Details should include method structure score.");
    }

    private void deleteDirectory(File directory) {

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    assertTrue(file.delete(), "Failed to delete file: " + file.getPath());
                }
            }
        }
        assertTrue(directory.delete(), "Failed to delete directory: " + directory.getPath());
    }
}