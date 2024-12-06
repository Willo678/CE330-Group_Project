package userInterfaceTest;

import XP_Metrics.Score;
import org.junit.Test;
import userInterface.codeMetricsUI;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CodeMetricsUITest {

    @Test
    public void testUpdateMetricsWithNullInput() {
        codeMetricsUI metricsUI = new codeMetricsUI();
        ArrayList<Score> validList = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () ->
                metricsUI.updateMetrics(null, validList, validList));
        assertThrows(IllegalArgumentException.class, () ->
                metricsUI.updateMetrics(validList, null, validList));
        assertThrows(IllegalArgumentException.class, () ->
                metricsUI.updateMetrics(validList, validList, null));
    }

    @Test
    public void testUpdateMetricsWithValidInput() {
        codeMetricsUI metricsUI = new codeMetricsUI();
        ArrayList<Score> indentation = new ArrayList<>();
        ArrayList<Score> classStructure = new ArrayList<>();
        ArrayList<Score> codeAnalysis = new ArrayList<>();

        indentation.add(new Score(10, "Indent error"));
        classStructure.add(new Score(20, "Structure error"));
        codeAnalysis.add(new Score(15, "Analysis error"));

        metricsUI.updateMetrics(indentation, classStructure, codeAnalysis);
        // Add assertions to verify UI state after update
    }

    @Test
    public void testMetricsPanelInitialization() {
        codeMetricsUI metricsUI = new codeMetricsUI();
        assertNotNull("UI components should not be null", metricsUI.getComponents());
    }

    @Test
    public void testMetricsPanelLayout() {
        codeMetricsUI metricsUI = new codeMetricsUI();
        Component[] components = metricsUI.getComponents();
        assertTrue("Should have at least one component", components.length > 0);
    }

    @Test
    public void testUpdateMetricsWithEmptyLists() {
        codeMetricsUI metricsUI = new codeMetricsUI();
        ArrayList<Score> emptyList = new ArrayList<>();
        metricsUI.updateMetrics(emptyList, emptyList, emptyList);
    }
}