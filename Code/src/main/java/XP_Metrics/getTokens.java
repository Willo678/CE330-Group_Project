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
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        Stack<BracePair> bracePairStack = new Stack<>();

        try (Scanner scanner = new Scanner(new File(path))) {
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
                        while (s.hasNext()) {
                            String word = s.next();
                            if (word == null) continue;

                            String keyword = isControlStatement(word);
                            String identity = isIdentityStatement(word);

                            latestKeyword = keyword != null ? keyword : latestKeyword;
                            latestIdentity = identity != null ? identity : latestIdentity;

                            if (word.contains("{") && !bracePairStack.isEmpty()) {
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Collections.sort(tokenArrayList);
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


}

// D:\Documents\Uni\Work\CE320 - Large Scale Systems and Extreme Programming\Project\Code\src\main\java\XP_Metrics\getBracePairs.java