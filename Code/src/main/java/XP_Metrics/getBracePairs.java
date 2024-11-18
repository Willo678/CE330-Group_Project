package XP_Metrics;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class getBracePairs {


    public static class BracePair {
        public int start;
        public int end;
        public String type;
        public String name;
        int nestedness;


        public BracePair(int End){
            end = End;
        }

        public BracePair(int Start, String Type, String Name, int Nestedness){
            start = Start;
            type = Type;
            name = Name;
            this.nestedness = Nestedness;
        }

        @Override
        public String toString(){
            return "["+start+","+end+","+type+","+name+","+ nestedness +"]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BracePair bracePair)) return false;
            return end > bracePair.end;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, type);
        }
    }


    //Makes the assumption that each line contains a single statement
    public static ArrayList<BracePair> getBracePairs(String path){
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<BracePair> bracePairArrayList = new ArrayList<>();
        Stack<BracePair> bracePairStack = new Stack<>();


        int lineNum = 0;
        int nestLevel = 0;
        String latestKeyword = null;
        String latestIdentity = null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            line = line.replaceAll("([\"']).*(\\1)+|[^a-zA-Z0-9{}\"']+", " ");
            lineNum++;

            for (String statement : line.split(";")) {
                if (statement.contains("//")) {break;}
                Scanner s = new Scanner(statement);
                while (s.hasNext()) {
                    String word = s.next();

                    latestKeyword = Optional.ofNullable(isControlStatement(word)).orElse(latestKeyword);
                    latestIdentity = Optional.ofNullable(isIdentityStatement(word)).orElse(latestIdentity);

                    if (word.contains("{")) {
                        bracePairStack.push(new BracePair(lineNum, latestKeyword, latestIdentity, nestLevel));
                        latestKeyword=null; latestIdentity=null; nestLevel++;
                    }

                    if (word.contains("}")) {
                        bracePairStack.peek().end = lineNum;
                        bracePairArrayList.add(bracePairStack.pop());
                        nestLevel--;
                    }



                }
            }
        }


        bracePairArrayList.sort((a, b)-> ((a.start>b.start) ? 1 : -1));
        return bracePairArrayList;
    }


    private static String identify(String s){
        s = s.replaceAll("\\W", "");
        if (SourceVersion.isKeyword(s)) {return "KEYWORD";}
        if (SourceVersion.isIdentifier(s)) {return "IDENTIFIER";}
        if (SourceVersion.isName(s)) {return "NAME";}

        return "UNIDENTIFIED";
    }


    private static String isControlStatement(String s){
        s = s.replaceAll("(([\"']).*(\\2)+|\\W)+", "");
        if (SourceVersion.isKeyword(s)) {
            s = s.toUpperCase();
            return switch (s){
                default -> s;
                case "PUBLIC",
                     "PRIVATE",
                     "PROTECTED",
                     "STATIC",
                     "VOID"
                        -> "METHOD";
                case "NULL",
                     "INT",
                     "DOUBLE",
                     "FLOAT",
                     "LONG",
                     "SHORT",
                     "BYTE",
                     "CHAR",
                     "BREAK"
                        -> null;
            };
        }

        return null;
    }


    private static String isIdentityStatement(String s){
        s = s.replaceAll("(([\"']).*(\\2)+|\\W)+", "");
        if (SourceVersion.isIdentifier(s)) {
            return s.toUpperCase();
        }

        return null;
    }


}

// D:\Documents\Uni\Work\CE320 - Large Scale Systems and Extreme Programming\Project\Code\src\main\java\XP_Metrics\getBracePairs.java