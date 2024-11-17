package XP_Metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class indentationChecker {


    public static class Token {
        public int start;
        public int end;
        public String name;
        public String type;

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
            return "["+start+","+end+","+type+","+name+"]";
        }


    }

    public static int checkIndentation(String path){
        int score = 100;

        ArrayList<Token> tokenArrayList = getTokenated(path);

        System.out.println(tokenArrayList);

        return score;
    }


    public static ArrayList<Token> getTokenated(String path){
        Scanner s;
        try {
            s = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Token> tokenArrayList = new ArrayList<>();
        Stack<Token> tokenStack = new Stack<>();


        int lineNum = 0;
        while (s.hasNextLine()){
            String line = s.nextLine();
            lineNum++;

            if (line.contains("{")){
                tokenStack.push(new Token(lineNum,"" , line.trim()));
            }
            if (line.contains("}")){
                if (!tokenStack.isEmpty()) {
                    tokenStack.peek().end = lineNum;
                    tokenArrayList.add(tokenStack.pop());
                }
            }


        }


        tokenArrayList.sort((a, b)-> ((a.start>b.start) ? 1 : -1));
        return tokenArrayList;
    }



}

// D:\Documents\Uni\Work\CE320 - Large Scale Systems and Extreme Programming\Project\Code\src\main\java\XP_Metrics\indentationChecker.java