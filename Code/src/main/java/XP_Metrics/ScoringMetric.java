package XP_Metrics;

import java.util.ArrayList;

import static XP_Metrics.getBracePairs.*;

public  class ScoringMetric {


    //path = "D:\\Year 3\\Visualising Algorithims\\Project - Vis. DS\\24-25_CE301_flatman_oska/Graphs.java";
    //path = "M:\\year 3\\ce320 project\\Code\\src\\main\\java\\userInterface/targetSelectionUI.java"


    public static int scoreBracePairsIndentation(String path, int indentationThreshold) {
        int indentationScore = 0;

        ArrayList<BracePair> bracePairs = getBracePairs(path);

        for (BracePair bracePair : bracePairs) {
            if (bracePair.indentation > indentationThreshold) {
                System.out.println("BracePair with indentation > " + indentationThreshold + ": " + bracePair);
                indentationScore -= 2;

            } else
                indentationScore += 1;
        }
        System.out.println("score for class indentation: " + indentationScore);
        return indentationScore;
    }

    public static int scoreBracePairsFunLength(String path, int funLengthThreshold) {
        int funScore = 0;
        ArrayList<BracePair> bracePairs = getBracePairs(path);
        for (BracePair bracePair : bracePairs) {
            if (bracePair.end - bracePair.start >= funLengthThreshold) {
                System.out.println("BracePair with function length > " + funLengthThreshold + ": " + bracePair);
                funScore -= 2;

            } else {
                funScore += 1;
            }
        }
        System.out.println("score for function length: " + funScore);
        return funScore;
    }
}


/*
    public static void main(String[] args) {
        ScoringMetric scoringMetric = new ScoringMetric();
        scoringMetric.analyzeBracePairsIndentation("M:\\year 3\\ce320 project\\Code\\src\\main\\java\\userInterface/targetSelectionUI.java",10);
    }


 */



