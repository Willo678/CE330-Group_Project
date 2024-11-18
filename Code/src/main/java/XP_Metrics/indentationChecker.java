package XP_Metrics;

import java.util.ArrayList;

public class indentationChecker {

    public static void checkIndentation(String path){

        ArrayList<getBracePairs.BracePair> bracePairs = getBracePairs.getBracePairs(path);

        System.out.println(bracePairs);
    }
}
