package com.github.tesquo.expplugin.EXPCode;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
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
        public int nestedness;



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
    public static ArrayList<Token> getTokens(String content) {
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        Stack<BracePair> bracePairStack = new Stack<>();

        try (Scanner scanner = new Scanner(new StringReader(content))) {
            int lineNum = 0;
            int nestLevel = 0;
            String latestKeyword = null;
            String latestIdentity = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int indentLevel = (line.length() - line.replaceAll("^(\\t| {4})+", "").length()) / 4;
                lineNum++;

                line = line.replaceAll("([\"']).*(\\1)+|[^a-zA-Z0-9{}\"/']+", " ");

                for (String statement : line.split(";")) {
                    if (statement == null) continue;

                    if (statement.contains("//")) {
                        tokenArrayList.add(new Token(lineNum, "Comment", statement.trim(), indentLevel));
                        continue;
                    }
                    if (statement.contains("import")) {
                        tokenArrayList.add(new Token(lineNum, "Import", statement.trim(), indentLevel));
                        continue;
                    }

                    try (Scanner s = new Scanner(statement)) {
                        boolean isMethodOrClass = false;
                        while (s.hasNext()) {
                            String word = s.next();
                            if (word == null) continue;
                            String keyword = isControlStatement(word);
                            if (keyword != null) {
                                latestKeyword = keyword;
                                isMethodOrClass = keyword.equals("METHOD") || keyword.equals("CLASS");
                            }
                            if (isMethodOrClass && isReturnType(word)) {
                                if (s.hasNext()) {
                                    latestIdentity = s.next();
                                    isMethodOrClass = false;
                                }
                            }

                            if (word.contains("{")) {
                                BracePair bracePair = new BracePair(lineNum,
                                        latestKeyword != null ? latestKeyword : "BLOCK",
                                        latestIdentity,
                                        nestLevel,
                                        indentLevel
                                );
                                bracePairStack.push(bracePair);
                                latestKeyword = null;
                                latestIdentity = null;
                                nestLevel++;
                            }

                            if (word.contains("}") && !bracePairStack.isEmpty()) {
                                BracePair topPair = bracePairStack.peek();
                                if (topPair != null) {
                                    topPair.end = lineNum;
                                    tokenArrayList.add(bracePairStack.pop());
                                    nestLevel = Math.max(0, nestLevel - 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        Collections.sort(tokenArrayList);
        return tokenArrayList;
    }


    public static List<BracePair> getBracePairs(String path) {
        return getTokens(path).stream().filter(BracePair.class::isInstance).map(BracePair.class::cast).toList();
    }

    public static List<Token> getComments(String path) {
        return getTokens(path).stream().filter(x -> (Objects.equals(x.type, "Comment"))).toList();
    }

    //Returns whether a given string is a Java keyword, an identifier, or a name
    private static String identify(String s) {
        s = s.replaceAll("\\W", "");
        if (SourceVersion.isKeyword(s)) {return "KEYWORD";}
        if (SourceVersion.isIdentifier(s)) {return "IDENTIFIER";}
        if (SourceVersion.isName(s)) {return "NAME";}

        return "UNIDENTIFIED";
    }

    private static String isControlStatement(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }

        String cleaned = s.replaceAll("(([\"']).*(\\2)+|\\W)+", "");
        if (cleaned.isEmpty()) {
            return null;
        }

        if (SourceVersion.isKeyword(cleaned)) {
            String upper = cleaned.toUpperCase();
            return switch (upper) {
                default -> upper;
                case "PUBLIC", "PRIVATE", "PROTECTED", "STATIC", "VOID" -> "METHOD";
                case "NULL", "INT", "DOUBLE", "FLOAT", "LONG",
                     "SHORT", "BYTE", "CHAR", "BREAK" -> null;
            };
        }
        return null;
    }

    private static String isIdentityStatement(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }

        String cleaned = s.replaceAll("(([\"']).*(\\2)+|\\W)+", "");
        if (cleaned.isEmpty()) {
            return null;
        }

        return SourceVersion.isIdentifier(cleaned) ? cleaned.toUpperCase() : null;
    }
    private static boolean isReturnType(String s) {
        return s.equalsIgnoreCase("void") || s.equalsIgnoreCase("int") || s.equalsIgnoreCase("double")
                || s.equalsIgnoreCase("float") || s.equalsIgnoreCase("long") || s.equalsIgnoreCase("short")
                || s.equalsIgnoreCase("byte") || s.equalsIgnoreCase("char") || s.equalsIgnoreCase("boolean")
                || s.toLowerCase().contains("list") || s.toLowerCase().contains("arraylist") || s.toLowerCase().contains("set")
                || s.toLowerCase().contains("hashset") || s.toLowerCase().contains("treeset") || s.toLowerCase().contains("map")
                || s.toLowerCase().contains("hashmap") || s.toLowerCase().contains("treemap") || s.toLowerCase().contains("concurrenthashmap")
                || s.toLowerCase().contains("queue") || s.toLowerCase().contains("deque") || s.toLowerCase().contains("arraydeque")
                || SourceVersion.isName(s);
    }


}
