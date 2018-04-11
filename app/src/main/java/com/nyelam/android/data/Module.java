package com.nyelam.android.data;

import com.nyelam.android.dev.NYLog;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/14/2018.
 */

public abstract class Module implements Parseable {

    protected static String KEY_MODULES_NAME = "module_name";
    protected static String KEY_EVENTS = "events";
    protected static String KEY_DIVE_SERVICES = "dive_services";
    protected static String KEY_DIVE_SPOTS = "dive_spots";

    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_MODULES_NAME)) {
                setName(obj.getString(KEY_MODULES_NAME));
            }
        } catch (JSONException e){e.printStackTrace();}

    }



    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_MODULES_NAME, getName());
            } else  {
                obj.put(KEY_MODULES_NAME, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}


        //NYLog.e("MY NAME IS : "+getList().getClass().getName());

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }


}
