package XP_Metrics;

import java.util.ArrayList;

import static XP_Metrics.getBracePairs.*;

public class ScoringMetric {


    static String path = "D:\\Year 3\\Visualising Algorithims\\Project - Vis. DS\\24-25_CE301_flatman_oska/Graphs.java";


    public static int analyzeBracePairsIndentation(String path, int indentationThreshold) {
        int score = 0;

        ArrayList<BracePair> bracePairs = getBracePairs(path);

        for (BracePair bracePair : bracePairs) {
            if (bracePair.indentation > indentationThreshold) {
                System.out.println("BracePair with indentation > " + indentationThreshold + ": " + bracePair);
                score -= 2;

            } else
                score += 1;
        }
        System.out.println("score for class indentation: " + score);
        return score;
    }
}

/*
    public static void main(String[] args) {
        ScoringMetric scoringMetric = new ScoringMetric();
        scoringMetric.analyzeBracePairsIndentation(path,3);
    }
}


 */