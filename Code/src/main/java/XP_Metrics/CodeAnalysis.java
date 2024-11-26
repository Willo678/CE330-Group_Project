package XP_Metrics;
// written by sj22795
// code cleanliness check first draft

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import XP_Metrics.getTokens.Token;
// import a spellcheck library

public class CodeAnalysis {

    public CodeAnalysis(ArrayList<Token> tokenList) {
        // temporary file
        // for each line in file
        // test


        methodNameAnalysis(tokenList.stream().filter(getTokens.BracePair.class::isInstance).map(getTokens.BracePair.class::cast).toList());
//        methodCommentAnalysis(tokenList);
//        codeLineTypeAnalysis(tokenList);

    }


    public void codeLineTypeAnalysis(List<Token> tokens) {
        System.out.println("This should check each line and give it a type" +
                "\ne.g. class, comment, method, whitespace");
        String[] lineType = new String[]{"class", "comment", "method", "code", "whitespace"};

        // return hashmap?
        // key = line number
        // object: string from lineType
    }

    public void methodNameAnalysis(List<getTokens.BracePair> bracePairs) {
        for (getTokens.BracePair bracePair : bracePairs) {
            if (bracePair.type == "METHOD") {
                String methodName = bracePair.name;
                System.out.println(methodName);
            }


        }
    }

    public void methodCommentAnalysis(List<Token> tokens) {

    }

}
