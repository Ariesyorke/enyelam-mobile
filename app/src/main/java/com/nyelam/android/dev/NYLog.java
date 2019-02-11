package com.nyelam.android.dev;

import android.util.Log;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYLog {
    public static void e(Object obj){
        if(obj == null){
            obj = "null";
        }
        Log.e("Nyelam", obj.toString());
    }

    public static void d(Object obj){
        if(obj == null){
            obj = "null";
        }
//        Log.d("Nyelam", obj.toString());
    }

    public static void w(Object obj){
        if(obj == null){
            obj = "null";
        }
//        Log.w("Nyelam", obj.toString());
    }
}