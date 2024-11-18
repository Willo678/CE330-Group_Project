package XP_Metrics;

import java.util.ArrayList;

import static XP_Metrics.getBracePairs.*;

public class ScoringMetric {

    String path = "D:\\Year 3\\Visualising Algorithims\\Project - Vis. DS\\24-25_CE301_flatman_oska/Graphs.java";
    int indentationThreshold = 3;

    public void analyzeBracePairs() {

        ArrayList<BracePair> bracePairs = getBracePairs(path);

        for (BracePair bracePair : bracePairs) {
            if (bracePair.indentation > indentationThreshold) {
                System.out.println("BracePair with indentation > " + indentationThreshold + ": " + bracePair);
            }
        }
    }


    public static void main(String[] args) {
        ScoringMetric scoringMetric = new ScoringMetric();
        scoringMetric.analyzeBracePairs();
    }
}
