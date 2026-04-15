package org.studyeasy.springstrater.util.Apputil;

import java.io.File;

public class AppUtil {
    public static String get_Uploaded_Path(String fileName) {
        String absolutePath = new File("src\\main\\resources\\static\\uploads").getAbsolutePath();
        return absolutePath+File.separator+fileName;
    }
    
}