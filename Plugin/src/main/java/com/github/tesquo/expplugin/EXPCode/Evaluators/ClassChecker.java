package com.github.tesquo.expplugin.EXPCode.Evaluators;

import com.github.tesquo.expplugin.EXPCode.Score;
import com.github.tesquo.expplugin.EXPCode.getTokens;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ClassChecker {
    public static ArrayList<Score> checkImports(String content, ArrayList<getTokens.Token> tokenList) {
        ArrayList<Score> scores = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new StringReader(content));
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
                        //scores.add(new Score(0,"Import at top of the class", index));
                        System.out.println(index);
                    }
                }
                if (index >= start) {
                    if (line.toLowerCase().startsWith("import")) {
                        scores.add(new Score(10, "Import not at top of class", index));
                        System.out.println(index);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return scores;
    }
}

