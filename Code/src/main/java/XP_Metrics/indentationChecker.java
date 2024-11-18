package XP_Metrics;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class indentationChecker {


    public static class Token {
        public int start;
        public int end;
        public String type;
        public String name;
        int indentation;


        public Token(int Start){
            start = Start;
        }
        public Token(int Start, String Type, String Name){
            start = Start;
            type = Type;
            name = Name;
        }

        @Override
        public String toString(){
            return "["+start+","+end+","+type+","+name+","+indentation+"]";
        }


    }

    public static int checkIndentation(String path){
        int score = 100;

        ArrayList<Token> tokenArrayList = getTokens(path);

        System.out.println(tokenArrayList);

        return score;
    }

    //Makes the assumption that each line contains a single statement
    public static ArrayList<Token> getTokens(String path){
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Token> tokenArrayList = new ArrayList<>();
        Stack<Token> tokenStack = new Stack<>();


        int lineNum = 0;
        String latestIdentity = "";
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            line.replaceAll("([\\W\\s]).?(\\1)|[^a-zA-Z0-9_{}_._//_;]+", " ");
            lineNum++;

            for (String statement : line.split(";")){
                if (statement.contains("//")) {break;}
                Scanner s = new Scanner(line);
                while (s.hasNext()) {
                    String word = s.next();

                    if (isControlStatement(word)!="") {latestIdentity = isControlStatement(word);}

                    if (word.contains("{")) {tokenStack.push(new Token(lineNum, latestIdentity, line.trim())); latestIdentity="";}

                    if (word.contains("}")) {if (!tokenStack.isEmpty()) {tokenStack.peek().end = lineNum;tokenArrayList.add(tokenStack.pop());}}

                    System.out.println(word + " : " + isControlStatement(word));

                }
            }



//            String latestIdentity = "";
//            for (String statement : line.split(";")) {
//
//                latestIdentity = (identify(statement).equals("KEYWORD")) ? statement : latestIdentity;
//
//
//                if (statement.isEmpty()) {continue;}
//                if (statement.contains("//")) {break;}
//                if (statement.contains("{")) {tokenStack.push(new Token(lineNum, latestIdentity, line.trim())); latestIdentity="";}
//                if (statement.contains("}")) {if (!tokenStack.isEmpty()) {tokenStack.peek().end = lineNum;tokenArrayList.add(tokenStack.pop());}}
//                System.out.println(statement + "   kw:" + identify(statement));
//            }

        }


        tokenArrayList.sort((a, b)-> ((a.start>b.start) ? 1 : -1));
        return tokenArrayList;
    }


    private static String identify(String s){
        s = s.replaceAll("\\W", "");
        if (SourceVersion.isKeyword(s)) {return "KEYWORD";}
        if (SourceVersion.isIdentifier(s)) {return "IDENTIFIER";}
        if (SourceVersion.isName(s)) {return "NAME";}

        return "UNIDENTIFIED";
    }


    private static String isControlStatement(String s){
        s = s.replaceAll("(([\"']).*(\\1)+|\\W)+", "").toUpperCase();
        if (SourceVersion.isKeyword(s)) {
            return switch (s){
                default -> s;
                case "INT",
                     "DOUBLE",
                     "FLOAT",
                     "LONG",
                     "SHORT"
                        -> "DATATYPE";
            };
        }

        return "";
    }


}

// D:\Documents\Uni\Work\CE320 - Large Scale Systems and Extreme Programming\Project\Code\src\main\java\XP_Metrics\indentationChecker.java