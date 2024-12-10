package com.github.tesquo.expplugin.EXPCode.Evaluators;

import com.github.tesquo.expplugin.EXPCode.Score;
import com.github.tesquo.expplugin.EXPCode.getTokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndentationChecker {

    static int idealMethodLength = 30;
    static int idealControlStatementLength = 10;
    static int idealNestedness = 3;


    public static ArrayList<Score> checkIndentation(List<getTokens.BracePair> bracePairs) {

        ArrayList<Score> scores = new ArrayList<>();

        int index = 0;
        int lastMethodNestedness = 0;
        int lastEnd = 0;

        for (getTokens.BracePair p : bracePairs) {


            if (Objects.equals(p.type, "METHOD")) {
                lastEnd = p.end;
                lastMethodNestedness = p.nestedness;
                if (p.type.equals("METHOD") && ((p.end - p.start) > idealMethodLength)) {
                    int difference = p.end - p.start;
                    scores.add(new Score(difference / 5, "Method at line " + p.start + " is too big (" + difference + " lines)", p.start));
                    //Handle method too big
                    //"Sorry your method is too big"
                }
            } else {
                if (p.start > lastEnd) {
                    continue;
                }
                if ((p.nestedness - lastMethodNestedness) > idealNestedness) {
                    int difference = p.nestedness - lastMethodNestedness;
                    scores.add(new Score(difference * 3, "Method too nested, at line " + p.start, p.start));
                    //Handle method too nested
                    //Tell the programmer to go **** themselves
                }
                if (isControlStatement(p.type) && (((p.end - p.start) > idealControlStatementLength))) {
                    int difference = p.end - p.start;
                    scores.add(new Score(difference / 5, "Control statement at line " + p.start + " is too big (" + difference + " lines)", p.start));
                    //Handle control statement too big
                    //"Why don't you create a method to handle this statement?"
                }
            }


            index++;
        }

        return scores;
    }

    private static boolean isControlStatement(String s) {
        return switch (s) {
            default -> false;
            case "FOR",
                 "WHILE",
                 "IF",
                 "ELSE",
                 "SWITCH" -> true;
        };
    }


//        tokenArrayList.sort((a, b)-> ((a.start>b.start) ? 1 : -1));
//        return tokenArrayList;
//    }



}
