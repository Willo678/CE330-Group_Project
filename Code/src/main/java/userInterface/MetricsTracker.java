package userInterface;

import XP_Metrics.EvaluateXP;

import java.util.HashMap;

import static utils.getJavaSubdirectories.getJavaSubdirectories;

public class MetricsTracker {


    public HashMap<String, EvaluateXP> evaluationList;

    private MetricsTracker(String path) {
        evaluationList = new HashMap<>();
        for (String javaFile : getJavaSubdirectories(path)) {
            evaluationList.put(javaFile, new EvaluateXP(javaFile));
        }
    }


    //Singleton methods

    private static MetricsTracker tracker;
    public static String projectPath;

    public static void selectProject(String path){
        projectPath = path;
        tracker = new MetricsTracker(path);
    }
}
