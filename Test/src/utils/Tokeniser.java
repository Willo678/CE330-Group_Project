package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/*
QUICK NOTES:
I basically rewrote wills initial tokeniser for indentation as an all encompassing tokeniser
for an entire file. Tried to be as thorough as I could (It's currently 4am and my brain is fried
working out the logic for this) but there are improvements to be made. For example, we could just
refactor slightly to have 1 callable method from this class that preprocesses everything when called
from the GUI.

ALSO CURRENTLY HAS AN ISSUE WITH NOT TOKENISING INNER CLASSES, SUCH AS THE TOKEN INNER CLASS IN THIS FILE
*/


public class Tokeniser {
    public ArrayList<Token> preprocess(String path) {
        ArrayList<Token> rawTokens = tokenisation(path);
        ArrayList<Token> processedTokens = typeTokens(rawTokens);
        addSubTokens(rawTokens, processedTokens);

        // 打印 Token 列表以调试
        for (Token token : processedTokens) {
            System.out.println(token);
        }

        return processedTokens;
    }

    public enum TokenType {
        IMPORT,
        CLASS,
        METHOD,
        COMMENT,
        NULL
    }

    public static class Token {
        public int line;
        public TokenType type;
        public String contents;
        public int start;
        public int end;
        public ArrayList<Token> subTokens;

        public Token(int line, TokenType type, String contents) {
            this.line = line;
            this.type = type;
            this.contents = contents;
            this.subTokens = new ArrayList<>();
        }

        public Token(int start, int end, TokenType type, String contents) {
            this.start = start;
            this.end = end;
            this.type = type;
            this.contents = contents;
            this.subTokens = new ArrayList<>();
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(start).append(",").append(end).append(",").append(type).append(",").append(contents).append("\n");
            for (Token subToken : subTokens) {
                sb.append(" SUBTOKEN: Line ").append(subToken.line).append(": ").append(subToken.contents).append("\n");
            }
            return sb.toString();
        }
    }

    private String[] classIdentifiers = {"public class", "private class", "protected class", "public static class",
            "private static class", "protected static class", "static class", "class" };
    private String[] identifiers = {"void", "int", "integer", "double", "float", "long", "char", "string", "boolean", "list", "arraylist", "hashmap", "byte", "array"};

    public ArrayList<Token> tokenisation(String path) {
        Scanner s;
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        int lineCount = 0;
        try {
            s = new Scanner(new File(path));
            while (s.hasNextLine()) {
                String line = s.nextLine();
                lineCount++;
                if (!line.trim().isEmpty()) {
                    Token t = new Token(lineCount, TokenType.NULL, line);
                    tokenArrayList.add(t);
                    //System.out.println(t.line + " " + t.type + " " + t.contents);
                }
            }
            s.close();
        }
        catch (FileNotFoundException e) {throw new RuntimeException(e);}
        return tokenArrayList;
    }

    public ArrayList<Token> typeTokens(ArrayList<Token> tokenArrayList) {
        ArrayList<Token> processedTokens = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        int[] scopeCounters = new int[2];
        for (Token t : tokenArrayList) {
            if (t.contents.startsWith("import ")) {
                t.type = TokenType.IMPORT;
                processedTokens.add(t);
            } else if (t.contents.startsWith("//")) {
                t.type = TokenType.COMMENT;
                processedTokens.add(t);
            }else if (classChecker(t)) {
                if(!stack.isEmpty() && stack.peek().type == TokenType.CLASS) {
                    if (scopeCounters[0] == 1) {
                        Token top = stack.pop();
                        top.end = t.line - 1;
                        processedTokens.add(top);
                    }
                }
                Token classToken = new Token(t.line, t.line, TokenType.CLASS, t.contents);
                stack.push(classToken);
                scopeCounters[0]++;
            } else if (methodChecker(t)) {
                if (!stack.isEmpty() && stack.peek().type == TokenType.METHOD) {
                    Token top = stack.pop();
                    top.end = t.line - 1;
                    processedTokens.add(top);
                }
                Token methodToken = new Token(t.line, t.line, TokenType.METHOD, t.contents);
                stack.push(methodToken);
                scopeCounters[1]++;
            } else if (t.contents.contains("{")) {
                if (scopeCounters[0] > 0) {scopeCounters[0]++;}
                if (scopeCounters[1] > 0) {scopeCounters[1]++;}
            } else if (t.contents.contains("}")) {
                if (scopeCounters[0] > 0) {
                    scopeCounters[0]--;
                    if (scopeCounters[0] == 0 && !stack.isEmpty() && stack.peek().type == TokenType.CLASS) {
                        Token top = stack.pop();
                        top.end = t.line;
                        processedTokens.add(top);
                    }
                }
                if (scopeCounters[1] > 0) {
                    scopeCounters[1]--;
                    if (scopeCounters[1] == 0 && !stack.isEmpty() && stack.peek().type == TokenType.METHOD) {
                        Token top = stack.pop();
                        top.end = t.line;
                        processedTokens.add(top);
                    }
                }
            }
        }
        while (!stack.isEmpty()) {
            Token top = stack.pop();
            top.end = tokenArrayList.get(tokenArrayList.size() -1).line;
            processedTokens.add(top);
        }
        return processedTokens;
    }

    //Needs to be improved. Currently counts constructors as methods as they typically contain an identifier as well as parenthesis
    private boolean methodChecker(Token token) {
        for (String id : identifiers) {
            if (token.contents.toLowerCase().contains(id) && token.contents.contains("(") && token.contents.contains(")") && !token.contents.contains("=") && !token.contents.contains("for (") && token.contents.contains("{")) {
                return true;
            }
        }
        return false;
    }

    private boolean classChecker(Token token) {
        for (String id : classIdentifiers) {
            if (token.contents.toLowerCase().startsWith(id)) {
                return true;
            }
        }
        return false;
    }

    public void addSubTokens(ArrayList<Token> tokenArrayList, ArrayList<Token> processedTokens) {
        for (Token processed : processedTokens) {
            for (Token original : tokenArrayList) {
                if (original.line >= processed.start && original.line <= processed.end && processed.type != original.type) {
                    Token subToken = new Token(original.line, original.type, original.contents);
                    processed.subTokens.add(subToken);
                }
            }
        }
    }

    public static void main(String[] args) {
        Tokeniser tk = new Tokeniser();
        ArrayList<Token> tokens = tk.tokenisation("Code/src/main/java/utils/Tokeniser.java");
        ArrayList<Token> processedTokens = tk.typeTokens(tokens);
        tk.addSubTokens(tokens, processedTokens);
        for (Token token : processedTokens) {
            System.out.println(token);
        }
    }
}
