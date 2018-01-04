package com.nyelam.android.helper;

import android.os.Build;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYHelper {

    public static String getDevice() {
        StringBuffer b = new StringBuffer();
        b.append(Build.MANUFACTURER).append("/");
        b.append(Build.MODEL);
        return b.toString();
    }

}
