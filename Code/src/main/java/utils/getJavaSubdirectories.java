package utils;

import java.io.File;
import java.util.ArrayList;

public class getJavaSubdirectories {

    public static ArrayList<String> getJavaSubdirectories(File f){
        ArrayList<String> l = new ArrayList<>();

        int dotIndex = f.getName().lastIndexOf('.');
        boolean isJava = (dotIndex==-1) ? false : f.getName().substring(dotIndex).equals(".java");
        if (isJava) {l.add(f.getAbsolutePath());}

        if (f.isDirectory()){
            for (String fName : f.list()){
                l.addAll(getJavaSubdirectories(new File(f, fName)));
            }
        }
        return l;
    }

}
