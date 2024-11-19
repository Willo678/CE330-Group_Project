package XP_Metrics;
// written by sj22795
// code cleanliness check first draft

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
// import a spellcheck library

public class CodeAnalysis {

    public CodeAnalysis() {
        // temporary file
        String[] codeLines = codeConversion("src/CodeAnalysis.java");
        // for each line in file
        // test
        for(String line : codeLines){
            System.out.println(line);
        }

        methodNameAnalysis(codeLines);
        methodCommentAnalysis(codeLines);
        codeLineTypeAnalysis(codeLines);

    }

    public String[] codeConversion(String fileName){
        ArrayList<String> lines = new ArrayList<>();
        try{
            File codeFile = new File(fileName);
            Scanner codeScan = new Scanner(codeFile);
            String line;
            while((codeScan.hasNextLine())){
                line = codeScan.nextLine();
                lines.add(line);
            }
        }catch (IOException e){
            System.out.println(e);
        }
        return lines.toArray(new String[0]);
    }

    public void codeLineTypeAnalysis(String[] code){
        System.out.println("This should check each line and give it a type" +
                "\ne.g. class, comment, method, whitespace");
        String[] lineType = new String[]{"class", "comment", "method", "code", "whitespace"};
        // return hashmap?
        // key = line number
        // object: string from lineType
    }
    public void methodNameAnalysis(String[] code){
        for(String line : code){
            if(line.contains("public") || line.contains("private")){
                System.out.println("Found something: " + line);
            }
        }
    }
    public void methodCommentAnalysis(String[] code){
        for(String line : code){
            if(line.contains("//")){
                System.out.println("Found something: " + line);
            }
        }
    }

}
