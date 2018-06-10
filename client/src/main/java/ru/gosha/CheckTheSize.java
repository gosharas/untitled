package ru.gosha;

import java.io.File;

public class CheckTheSize {

    public boolean moreThan500(File file){
        boolean res = false;
        if (file.exists()){
            final double bytes = file.length();
            final double kilobytes = bytes / 1024;
            final double megabytes = kilobytes / 1024;
            if (megabytes > 500.0)
                res = true;
        }
       return res;
    }
}
