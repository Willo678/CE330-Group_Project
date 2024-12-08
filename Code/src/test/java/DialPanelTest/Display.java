package DialPanelTest;

import userInterface.DialPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class Display {

    private DialPanel dialPanel;

    @BeforeEach
    public void setUp() {
        dialPanel = new DialPanel("XP Adherence");
    }

    @Test
    public void testDialPanelCreation() {
        assertNotNull(dialPanel);
    }

    @Test
    public void testScoreSetting() {
        dialPanel.setScore(0.5);

        try {
            Method paintMethod = DialPanel.class.getDeclaredMethod("paintComponent", Graphics.class);
            paintMethod.setAccessible(true);
            BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
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
        assertEquals(new Dimension(300, 300), dialPanel.getPreferredSize());

    }

}
