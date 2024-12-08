package userInterface;

import XP_Metrics.XPEvaluator;

import java.util.HashMap;

import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class MetricsTracker {


    protected HashMap<String, XPEvaluator> evaluationList;
    protected String projectPath;
    protected String trackedFile;


    private MetricsTracker(String path) {
        projectPath = path;
        evaluationList = new HashMap<>();
        for (String javaFile : getJavaSubdirectories(path)) {
            evaluationList.put(javaFile, new XPEvaluator(javaFile));
        }
    }


    //Singleton methods

    private static MetricsTracker tracker;

    public static void selectProject(String path){
        tracker = new MetricsTracker(path);
        if (tracker.evaluationList.isEmpty()) {tracker = null;}
    }

    public static MetricsTracker getTracker() {return tracker;}

    public static boolean trackerExists() {
        return tracker!=null;
    }

    public static String getProjectPath() {
        if (tracker!=null) {return tracker.projectPath;}
        return "No project currently tracked";
    }

    public static HashMap<String, XPEvaluator> getEvaluationList() {
        if (tracker!=null) {return tracker.evaluationList;}
        return new HashMap<>();
    }

    public static void setFocusedFile(String file) {
        if (tracker!=null && (file.isEmpty() || tracker.evaluationList.containsKey(file))) {tracker.trackedFile = (file.isEmpty()) ? null : file;}
        return;
    }

    public static String getTrackedFile() {
        if (tracker!=null) {return tracker.trackedFile;}
        return null;
    }

    public static XPEvaluator getTrackedEvaluator() {
        if (tracker!=null) {
            if (getTrackedFile()!=null) {
                return tracker.evaluationList.get(getTrackedFile());
            }
        } return null;
    }

    public static double getTrackedIndentationScore() {
        if (tracker!=null) {
            if (tracker.evaluationList.containsKey(getTrackedFile())) {
                return tracker.evaluationList.get(getTrackedFile()).indentationScore();
            } return getOverallIndentationScore();
        }
        return -1;
    }

    public static double getTrackedClassStructureScore() {
        if (tracker!=null) {
            if (tracker.evaluationList.containsKey(getTrackedFile())) {
                return tracker.evaluationList.get(getTrackedFile()).classStructureScore();
            } return getOverallClassStructureScore();
        }
        return -1;
    }

    public static double getTrackedMethodStructureScore() {
        if (tracker!=null) {
            if (tracker.evaluationList.containsKey(getTrackedFile())) {
                return tracker.evaluationList.get(getTrackedFile()).methodStructureScore();
            } return getOverallMethodStructureScore();
        }
        return -1;
    }

    public static double getTrackedAverageScore() {
        if (tracker!=null) {
            if (tracker.evaluationList.containsKey(getTrackedFile())) {
                return tracker.evaluationList.get(getTrackedFile()).normalisedScore();
            } return getOverallScore();
        }
        return -1;
    }

    public static double getOverallIndentationScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::indentationScore).average().getAsDouble();}
        return -1;
    }

    public static double getOverallClassStructureScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::classStructureScore).average().getAsDouble();}
        return -1;
    }

    public static double getOverallMethodStructureScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::methodStructureScore).average().getAsDouble();}
        return -1;
    }

    public static double getOverallScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::normalisedScore).average().getAsDouble();}
        return -1;
    }
}
