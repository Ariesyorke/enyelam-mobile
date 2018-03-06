package com.danzoye.lib.auth.facebook;

import org.json.JSONObject;


public abstract class FacebookProcessor<RESULT extends FBAuthResult> {
    protected RESULT result;

    public FacebookProcessor(Class<RESULT> clazz) {
        try {
            result = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }
    }

    public RESULT getResult() {
        return result;
    }

    public abstract void onProcess(int type, JSONObject value);
}
