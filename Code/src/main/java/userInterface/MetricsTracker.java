package userInterface;

import XP_Metrics.XPEvaluator;

import java.util.HashMap;

import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class MetricsTracker {

    protected HashMap<String, XPEvaluator> evaluationMap;
    protected String projectPath;
    protected String trackedFile;

    private MetricsTracker(String path) {
        projectPath = path;
        evaluationMap = new HashMap<>();
        for (String javaFile : getJavaSubdirectories(path)) {
            evaluationMap.put(javaFile, new XPEvaluator(javaFile));
        }
    }


    private static MetricsTracker tracker;

    public static void selectProject(String path) {
        tracker = new MetricsTracker(path);
        if (tracker.evaluationMap.isEmpty()) {
            tracker = null;
        }
    }

    public static MetricsTracker getTracker() {
        return tracker;
    }

    public static boolean trackerExists() {
        return tracker != null;
    }

    public static String getProjectPath() {
        if (trackerExists()) {
            return tracker.projectPath;
        }
        return "No project currently tracked";
    }

    public static int getProjectSize() {
        if (trackerExists()) {
            return tracker.evaluationMap.size();
        }
        return 0;
    }

    public static HashMap<String, XPEvaluator> getEvaluationMap() {
        if (trackerExists()) {
            return tracker.evaluationMap;
        }
        return new HashMap<>();
    }

    public static boolean setFocusedFile(String filePath) {
        if (trackerExists() && (filePath.isEmpty() || tracker.evaluationMap.containsKey(filePath))) {
            tracker.trackedFile = (filePath.isEmpty()) ? null : filePath;
            return true;
        }
        return false;
    }

    public static boolean setFocusedFile(int indexFrom1) {
        if (trackerExists() && (indexFrom1 > 0 || tracker.evaluationMap.size() >= indexFrom1)) {
            tracker.trackedFile = (indexFrom1 == 0) ? null : (String) tracker.evaluationMap.keySet().toArray()[indexFrom1 - 1];
            return true;
        }
        return false;
    }

    public static String getFocusedFile() {
        if (trackerExists()) {
            return tracker.trackedFile;
        }
        return null;
    }

    public static XPEvaluator getTrackedEvaluator() {
        if (trackerExists()) {
            if (getFocusedFile() != null) {
                return tracker.evaluationMap.get(getFocusedFile());
            }
        }
        return null;
    }

    public static double getTrackedIndentationScore() {
        if (trackerExists()) {
            if (tracker.evaluationMap.containsKey(getFocusedFile())) {
                return tracker.evaluationMap.get(getFocusedFile()).indentationScore();
            }
            return getOverallIndentationScore();
        }
        return -1;
    }

    public static double getTrackedClassStructureScore() {
        if (trackerExists()) {
            if (tracker.evaluationMap.containsKey(getFocusedFile())) {
                return tracker.evaluationMap.get(getFocusedFile()).classStructureScore();
            }
            return getOverallClassStructureScore();
        }
        return -1;
    }

    public static double getTrackedMethodStructureScore() {
        if (trackerExists()) {
            if (tracker.evaluationMap.containsKey(getFocusedFile())) {
                return tracker.evaluationMap.get(getFocusedFile()).methodStructureScore();
            }
            return getOverallMethodStructureScore();
        }
        return -1;
    }

    public static double getTrackedAverageScore() {
        if (trackerExists()) {
            if (tracker.evaluationMap.containsKey(getFocusedFile())) {
                return tracker.evaluationMap.get(getFocusedFile()).normalisedScore();
            }
            return getOverallScore();
        }
        return -1;
    }

    public static double getOverallIndentationScore() {
        if (trackerExists()) {
            return tracker.evaluationMap.values().stream().mapToInt(XPEvaluator::indentationScore).average().getAsDouble();
        }
        return -1;
    }

    public static double getOverallClassStructureScore() {
        if (trackerExists()) {
            return tracker.evaluationMap.values().stream().mapToInt(XPEvaluator::classStructureScore).average().getAsDouble();
        }
        return -1;
    }

    public static double getOverallMethodStructureScore() {
        if (trackerExists()) {
            return tracker.evaluationMap.values().stream().mapToInt(XPEvaluator::methodStructureScore).average().getAsDouble();
        }
        return -1;
    }

    public static double getOverallScore() {
        if (trackerExists()) {
            return tracker.evaluationMap.values().stream().mapToInt(XPEvaluator::normalisedScore).average().getAsDouble();
        }
        return -1;
    }
}
