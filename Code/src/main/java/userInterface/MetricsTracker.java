package userInterface;

import XP_Metrics.XPEvaluator;

import java.util.HashMap;

import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class MetricsTracker {


    protected HashMap<String, XPEvaluator> evaluationList;
    protected String projectPath;
    protected String focusedFile;


    private MetricsTracker(String path) {
        projectPath = path;
        evaluationList = new HashMap<>();
        for (String javaFile : getJavaSubdirectories(path)) {
            evaluationList.put(javaFile, new XPEvaluator(javaFile));
        }
        if (!evaluationList.isEmpty()) {
            focusedFile = evaluationList.keySet().stream().findFirst().get();
        }
    }


    //Singleton methods

    private static MetricsTracker tracker;

    public static void selectProject(String path){
        tracker = new MetricsTracker(path);
    }

    public static MetricsTracker getTracker() {return tracker;}

    public static String getProjectPath() {
        if (tracker!=null) {return tracker.projectPath;}
        return "No project currently tracked";
    }

    public static HashMap<String, XPEvaluator> getEvaluationList() {
        if (tracker!=null) {return tracker.evaluationList;}
        return new HashMap<>();
    }

    public static String getFocusedFile() {
        if (tracker!=null) {return tracker.focusedFile;}
        return "No project currently tracked";
    }

    public static double getOverallIndentationScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::indentationScore).average().getAsDouble();}
        return 0;
    }

    public static double getOverallClassStructureScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::classStructureScore).average().getAsDouble();}
        return 0;
    }

    public static double getOverallMethodStructureScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::methodStructureScore).average().getAsDouble();}
        return 0;
    }

    public static double getOverallScore() {
        if (tracker!=null) {return tracker.evaluationList.values().stream().mapToInt(XPEvaluator::normalisedScore).average().getAsDouble();}
        return 0;
    }
}
