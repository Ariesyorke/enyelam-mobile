package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 3/21/2018.
 */

public class Language implements Parseable {

    private static String TAG_ID = "id";
    private static String TAG_NAME = "name";

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
            if (obj.has(TAG_ID) && NYHelper.isStringNotEmpty(obj.getString(TAG_ID))){
                setId(obj.getString(TAG_ID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (obj.has(TAG_NAME) && NYHelper.isStringNotEmpty(obj.getString(TAG_NAME))){
                setName(obj.getString(TAG_NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (NYHelper.isStringNotEmpty(getId())){
                obj.put(TAG_ID, getId());
            } else {
                obj.put(TAG_ID, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (NYHelper.isStringNotEmpty(getName())){
                obj.put(TAG_NAME, getName());
            } else {
                obj.put(TAG_NAME, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}
        
        return super.toString();
    }


}
