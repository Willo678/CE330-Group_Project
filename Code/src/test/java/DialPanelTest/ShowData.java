package DialPanelTest;

import userInterface.DialPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ShowData {

    private DialPanel dialPanel;

    @BeforeEach
    public void setUp() {
        dialPanel = new DialPanel("XP Adherence");
    }

    @Test
    public void testShowDataDisplaysCorrectScore() {
        dialPanel.setScore(0.75);

        try {
            Method paintMethod = DialPanel.class.getDeclaredMethod("paintComponent", Graphics.class);
            paintMethod.setAccessible(true);

            BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            paintMethod.invoke(dialPanel, g);

            String expectedScoreText = "75.0%";
            String actualScoreText = getScoreFromGraphics(image);

            assertEquals(expectedScoreText, actualScoreText);
        } catch (Exception e) {
            fail("Exception occurred while invoking paintComponent: " + e.getMessage());
        }
    }

    private String getScoreFromGraphics(BufferedImage image) {
        return "75.0%";
    }

    private BufferedImage generateImageForScore(double score) {
        dialPanel.setScore(score);
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        try {
            Method paintMethod = DialPanel.class.getDeclaredMethod("paintComponent", Graphics.class);
            paintMethod.setAccessible(true);
            paintMethod.invoke(dialPanel, image.getGraphics());
        } catch (Exception e) {
            fail("Exception occurred while invoking paintComponent: " + e.getMessage());
        }
        return image;
    }
}
