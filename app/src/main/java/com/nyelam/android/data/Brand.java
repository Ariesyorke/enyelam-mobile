package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 12/5/2018.
 */
public class Brand implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "brand_name";
    private static String KEY_LOGO = "brand_logo";

    private String id;
    private String name;
    private String logo;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ID)) {
                setId(obj.getString(KEY_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NAME)) {
                setName(obj.getString(KEY_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_LOGO)) {
                setLogo(obj.getString(KEY_LOGO));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getId())) {
                obj.put(KEY_ID, getId());
            } else {
                obj.put(KEY_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_NAME, getName());
            } else {
                obj.put(KEY_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getLogo())) {
                obj.put(KEY_LOGO, getLogo());
            } else {
                obj.put(KEY_LOGO, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}