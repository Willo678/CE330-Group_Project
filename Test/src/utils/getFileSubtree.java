package utils;

import java.io.File;
import java.util.ArrayList;

public class getFileSubtree {
    public static ArrayList<String> getFileSubtree(File f){
        ArrayList<String> l = new ArrayList<>();
        l.add(f.getAbsolutePath());
        if (f.isDirectory()){
            for (String fName : f.list()){
                l.addAll(getFileSubtree(new File(f, fName)));
            }
        }
        return l;
    }
}
