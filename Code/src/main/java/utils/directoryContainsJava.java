package utils;

import java.io.File;

public class directoryContainsJava {


    public static boolean directoryContainsJava(File f){

        boolean result = f.getName().endsWith(".java");

        if (f.isDirectory()) {
            for (String fName : f.list()) {
                result = result || directoryContainsJava(new File(f, fName));
            }
        }
        return result;
    }
}
