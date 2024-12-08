package XP_Metrics.evaluators;

import XP_Metrics.Score;
import XP_Metrics.getTokens;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClassChecker {
    public static ArrayList<Score> checkImports(String path, ArrayList<getTokens.Token> tokenList) {
        ArrayList<Score> scores = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(path));
            int index = 0;
            int start = 0;
            String line;

            while (scanner.hasNextLine()) {
                index++;
                line = scanner.nextLine();
                for (getTokens.Token token : tokenList) {
                    if (!token.type.equals("Import")) {
                        start = token.start;
                    }
                }
                if (index < start) {
                    if (line.toLowerCase().startsWith("import")) {
                        scores.add(new Score(0, "Import at top of the class"));
                    }
                }
                if (index >= start) {
                    if (line.toLowerCase().startsWith("import")) {
                        scores.add(new Score(10, "Import not at top of class"));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return scores;
    }

//    public static void main(String[] args) {
//        String path = "M:\\3rd Year\\CE320\\GROUP_PROJECT\\Code\\src\\main\\java\\XP_Metrics\\indentationChecker.java";
//        getTokens tokens = new getTokens();
//        ArrayList<getTokens.Token> tokenList = tokens.getTokens(path);
//        ArrayList<Score> scores = checkImports(path, tokenList);
//        for (Score score : scores) {
//            System.out.println(score);
//        }
//
//    }
}
