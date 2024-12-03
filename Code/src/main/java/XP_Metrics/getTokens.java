package XP_Metrics;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class getTokens {



    public static class Token implements Comparable<Token> {
        public int start;
        public String type;
        public String name;
        public int indentationLevel;

        public Token(int start, String type, String name, int indentationLevel) {
            this.start = start;
            this.type = type;
            this.name = name;
            this.indentationLevel = indentationLevel;
        }

        @Override
        public String toString() {
            return "[" + start + "," + type + "," + name + "," + indentationLevel + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Token token)) return false;
            return start == token.start && indentationLevel == token.indentationLevel && Objects.equals(type, token.type) && Objects.equals(name, token.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, type, name, indentationLevel);
        }

        @Override
        public int compareTo(Token o) {
            return start-o.start;
        }
    }

    //Stores the start and end of a set of curly braces,
    //along with the type (method, class, if, while, etc.) and the name
    public static class BracePair extends Token {
        public int end;
        int nestedness;



        public BracePair(int Start, String Type, String Name, int Nestedness, int Indentation) {
            super(Start, Type, Name, Indentation);
            this.nestedness = Nestedness;
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + "," + type + "," + name + "," + nestedness + "," + indentationLevel + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BracePair bracePair)) return false;
            if (!super.equals(o)) return false;
            return end == bracePair.end && nestedness == bracePair.nestedness;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), end, nestedness);
        }

    }


    //Returns an arraylist of Bracepairs for a given class
    public static ArrayList<Token> getTokens(String path) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Token> tokenArrayList = new ArrayList<>();
        Stack<BracePair> bracePairStack = new Stack<>();


        int lineNum = 0;
        int nestLevel = 0;

        String latestKeyword = null;
        String latestIdentity = null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int indentLevel = (line.length() - line.replaceAll("^(\\t| {4})+", "").length()) / 4;
            lineNum++;

            //Removes all non word or brace characters
            line = line.replaceAll("([\"']).*(\\1)+|[^a-zA-Z0-9{}\"/']+", " ");


            //Breaks up each line into statements in the case that a line contains multiple statements
            for (String statement : line.split(";")) {
                if (statement.contains("//")) {
                    tokenArrayList.add(new Token(lineNum, "Comment", statement, indentLevel));
                    continue;
                }
                if (statement.contains("import")) {
                    tokenArrayList.add(new Token(lineNum, "Import", statement, indentLevel));
                    continue;
                }
                Scanner s = new Scanner(statement);

                //Iterates through statement word by word, rather than using .contains("{") to prevent errors if multiple curly brackets occur
                while (s.hasNext()) {
                    String word = s.next();

                    //Updates the most recently detected keyword and identity if not null
                    latestKeyword = Optional.ofNullable(isControlStatement(word)).orElse(latestKeyword);
                    latestIdentity = Optional.ofNullable(isIdentityStatement(word)).orElse(latestIdentity);

                    //If "{" detected, adds new bracepair to the stack
                    if (word.contains("{")) {
                        bracePairStack.push(new BracePair(lineNum, latestKeyword, latestIdentity, nestLevel, indentLevel));
                        latestKeyword = null;
                        latestIdentity = null;
                        nestLevel++;
                    }

                    //If "}" detected, updates the end value of the top item of the stack, and moves it from the stack to arraylist
                    if (word.contains("}")) {
                        bracePairStack.peek().end = lineNum;
                        tokenArrayList.add(bracePairStack.pop());
                        nestLevel--;
                    }

                }
            }
        }

        //Sorts arraylist so that pairs that start earlier are first in the array
        Collections.sort(tokenArrayList);

        //System.out.println(tokenArrayList);

        return tokenArrayList;
    }


    public static List<BracePair> getBracePairs(String path) {
        return getTokens(path).stream().filter(BracePair.class::isInstance).map(BracePair.class::cast).toList();
    }

    public static List<Token> getComments(String path) {
        return getTokens(path).stream().filter(x -> (Objects.equals(((Token) x).type, "Comment"))).toList();
    }

    //Returns whether a given string is a Java keyword, an identifier, or a name
    private static String identify(String s) {
        s = s.replaceAll("\\W", "");
        if (SourceVersion.isKeyword(s)) {return "KEYWORD";}
        if (SourceVersion.isIdentifier(s)) {return "IDENTIFIER";}
        if (SourceVersion.isName(s)) {return "NAME";}

        return "UNIDENTIFIED";
    }

    //Detects if given string is a reserved Java keyword
    private static String isControlStatement(String s) {
        s = s.replaceAll("(([\"']).*(\\2)+|\\W)+", "");
        if (SourceVersion.isKeyword(s)) {
            s = s.toUpperCase();
            return switch (s) {
                default -> s;
                case "PUBLIC", //If any of these words are detected, identifies as method declaration
                     "PRIVATE",
                     "PROTECTED",
                     "STATIC",
                     "VOID" -> "METHOD";
                case "NULL", //Java primitives are also identified as keywords; prevents being identified as such
                     "INT",
                     "DOUBLE",
                     "FLOAT",
                     "LONG",
                     "SHORT",
                     "BYTE",
                     "CHAR",
                     "BREAK" -> null;
            };
        }

        return null;
    }

    //Detects if given string is a valid variable name
    private static String isIdentityStatement(String s) {
        s = s.replaceAll("(([\"']).*(\\2)+|\\W)+", "");
        if (SourceVersion.isIdentifier(s)) {
            return s.toUpperCase();
        }

        return null;
    }


}

// D:\Documents\Uni\Work\CE320 - Large Scale Systems and Extreme Programming\Project\Code\src\main\java\XP_Metrics\getBracePairs.java