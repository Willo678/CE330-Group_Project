package XP_Metrics;

import java.util.ArrayList;

import XP_Metrics.getBracePairs.BracePair;

public class indentationChecker {

    static int idealMethodLength = 20;
    static int idealControlStatementLength = 5;
    static int idealNestedness = 3;


    public static void checkIndentation(String path){

        ArrayList<BracePair> bracePairs = getBracePairs.getBracePairs(path);


        int index = 0;
        int lastMethodNestedness = 0;
        int lastEnd = 0;

        for (BracePair p : bracePairs){


            if (p.type=="METHOD") {
                lastEnd = p.end;
                lastMethodNestedness = p.nestedness;
                if ((p.end-p.start)>idealMethodLength){
                    //Handle method too big
                    //"Sorry your method is too big"
                }
            } else {
                if (p.start>lastEnd){continue;}
                if ((p.nestedness -lastMethodNestedness)> idealNestedness){
                    //Handle method too nested
                    //Tell the programmer to go **** themselves
                }
                if (isControlStatement(p.type) && (((p.end-p.start)>idealControlStatementLength))){
                    //Handle control statement too big
                    //"Why don't you create a method to handle this statement?"
                }
            }


            index++;
        }

    }

    private static boolean isControlStatement(String s){
        return switch (s){
            default -> false;
            case "FOR",
                 "WHILE",
                 "IF",
                 "ELSE",
                 "SWITCH"
                        -> true;
        };
    }





//    private static void checkMethod(ArrayList<BracePair> bracePairs, int index){
//
//        int start = bracePairs.get(index).start;
//        int end = bracePairs.get(index).end;
//
//        int indentLevel = bracePairs.get(index).nestedness;
//
//        int endIndex = index+1;
//
//        for (int i; endIndex<bracePairs.size(); endIndex++){
//            if (bracePairs.get(endIndex).end>=end){break;}
//
//            if ((indentLevel-bracePairs.get(endIndex).nestedness)> idealNestedness) {
//
//            }
//
//
//
//
//        }
//
//        System.out.print("  Start: "+index+" End: "+endIndex);
//    }
}

/*

D:\Documents\Uni\Work\CE320 - Large Scale Systems and Extreme Programming\Project\Code\src\main\java\XP_Metrics\indentationChecker.java

*/
