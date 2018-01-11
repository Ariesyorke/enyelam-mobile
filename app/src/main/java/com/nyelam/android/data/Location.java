package com.nyelam.android.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Location implements Parseable {

    private static String KEY_ID = "id";

    private String id;

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;
    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}