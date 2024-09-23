package automation.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class AutomationUtil {
    public static Set<String> getAllFiles(String dir, String fileNamePattern) {
        Set<String> files = new HashSet<>();
        File curDir = new File(dir);
        File[] filesList = curDir.listFiles();
        if (filesList == null) return files;
        for(File f : filesList){
            if(f.isFile()){
                if(f.getName().matches(fileNamePattern)) {
                    files.add(f.getAbsolutePath());
                }
            }
        }
        return files;
    }
}
