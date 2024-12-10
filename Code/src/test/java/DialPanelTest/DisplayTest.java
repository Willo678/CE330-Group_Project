// Fix: Previously, the `AccessibleName` for the panel was not set

package DialPanelTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import userInterface.UI_Widgets.DialPanelWidget;

import java.awt.*;
import java.lang.reflect.Method;
import java.awt.image.BufferedImage;

public class DisplayTest {

    private DialPanelWidget dialPanel;

    @BeforeEach
    public void setUp() {
        dialPanel = new DialPanelWidget("XP Adherence");
    }

    @Test
    public void testDialPanelCreation() {
        assertNotNull(dialPanel);
    }

    @Test
    public void testLabelRendering() {
        assertEquals("XP Adherence", dialPanel.getAccessibleContext().getAccessibleName(),
                "Label should be set correctly.");
    }

    @Test
    public void testScoreSetting() {
        dialPanel.setScore(0.5);

        try {
            Method paintMethod = DialPanelWidget.class.getDeclaredMethod("paintComponent", Graphics.class);
            paintMethod.setAccessible(true);
            BufferedImage image = new BufferedImage(350, 350, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            paintMethod.invoke(dialPanel, g);
        } catch (Exception e) {
            fail("Exception occurred while invoking paintComponent: " + e.getMessage());
        }
    }

    @Test
    public void testScoreBoundaries() {
        dialPanel.setScore(-0.1);
        assertEquals(0.0, dialPanel.getScore());

        dialPanel.setScore(1.1);
        assertEquals(1.0, dialPanel.getScore());
    }

    @Test
    public void testPanelSize() {
        assertEquals(new Dimension(350, 350), dialPanel.getPreferredSize(),
                "Panel size should match the expected dimensions.");
    }
}
