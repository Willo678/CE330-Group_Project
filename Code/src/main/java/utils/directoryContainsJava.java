package utils;

import java.io.File;

public class directoryContainsJava {
    public static boolean directoryContainsJava(File f){
        int dotIndex = f.getName().lastIndexOf('.');
        boolean result = (dotIndex==-1) ? false : f.getName().substring(dotIndex).equals(".java");

        if (f.isDirectory()) {
            for (String fName : f.list()) {
                result = result || directoryContainsJava(new File(f, fName));
            }
        }
        return result;
    }
}
