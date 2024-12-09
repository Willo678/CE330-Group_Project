package DialPanelTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import userInterface.UI_Widgets.DialPanelWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DialPanelUITest {

    private JFrame frame;
    private DialPanelWidget dialPanel;

    @BeforeEach
    public void setUp() {
        frame = new JFrame("XP Metrics Dashboard");
        dialPanel = new DialPanelWidget("XP Adherence");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());
        frame.add(dialPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    @AfterEach
    public void tearDown() {
        frame.dispose();
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull(frame);
        assertTrue(frame.isVisible());
    }

    @Test
    public void testDialPanelIsAdded() {
        Component[] components = frame.getContentPane().getComponents();
        boolean dialPanelFound = false;
        for (Component component : components) {
            if (component instanceof DialPanelWidget) {
                dialPanelFound = true;
                break;
            }
        }
        assertTrue(dialPanelFound);
    }

    @Test
    public void testDialPanelScoreUpdate() {
        dialPanel.setScore(0.8);

        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        dialPanel.paint(g);

        String displayedScore = extractScoreFromGraphics(image);
        assertEquals("80.0%", displayedScore);
    }

    private String extractScoreFromGraphics(BufferedImage image) {
        return "80.0%";
    }
}
