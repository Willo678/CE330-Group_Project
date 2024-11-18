package XP_Metrics;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
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
        public Token(int Start, String Type, String Name, int Indentation){
            start = Start;
            type = Type;
            name = Name;
            this.indentation = Indentation;
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
        String latestKeyword = null;
        String latestIdentity = null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int indentationLevel = (line.length() - line.replaceAll("^(\t|    )+", "").length())/4;
            line.replaceAll("([\"']).*(\\1)+|[^a-zA-Z0-9{}\"']+", " ");
            lineNum++;

            for (String statement : line.split(";")) {
                if (statement.contains("//")) {break;}
                Scanner s = new Scanner(statement);
                while (s.hasNext()) {
                    String word = s.next();

                    latestKeyword = Optional.ofNullable(isControlStatement(word)).orElse(latestKeyword);
                    latestIdentity = Optional.ofNullable(isIdentityStatement(word)).orElse(latestIdentity);

                    if (word.contains("{")) {
                        tokenStack.push(new Token(lineNum, latestKeyword, latestIdentity, indentationLevel));
                        latestKeyword=null; latestIdentity=null;
                    }

                    if (word.contains("}")) {
                        //if (!tokenStack.isEmpty()) {
                            tokenStack.peek().end = lineNum;
                            tokenArrayList.add(tokenStack.pop());
                        //}
                    }



                }
            }
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
        s = s.replaceAll("(([\"']).*(\\1)+|\\W)+", "");
        if (SourceVersion.isKeyword(s)) {
            s = s.toUpperCase();
            return switch (s){
                default -> s;
                case "PUBLIC",
                     "PRIVATE",
                     "PROTECTED"
                        -> "METHOD";
                case "NULL",
                     "INT",
                     "DOUBLE",
                     "FLOAT",
                     "LONG",
                     "SHORT",
                     "BYTE",
                     "CHAR"
                        -> "CLASS";
            };
        }

        return null;
    }


    private static String isIdentityStatement(String s){
        s = s.replaceAll("(([\"']).*(\\1)+|\\W)+", "");
        if (SourceVersion.isIdentifier(s)) {
            return s.toUpperCase();
        }

        return null;
    }


}

// D:\Documents\Uni\Work\CE320 - Large Scale Systems and Extreme Programming\Project\Code\src\main\java\XP_Metrics\indentationChecker.java