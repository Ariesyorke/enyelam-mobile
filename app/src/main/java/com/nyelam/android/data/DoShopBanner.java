package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class DoShopBanner implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_IMAGE = "banner_image";
    private static String KEY_TYPE = "banner_type";
    private static String KEY_TARGET_NAME = "target_name";
    private static String KEY_TARGET = "target";

    private String id;
    private String image;
    private String type;
    private String targetName;
    private String target;

    public DoShopBanner(){}

    public DoShopBanner(String id, String image, String type, String targetName, String target){
        this.id = id;
        this.image = image;
        this.type = type;
        this.targetName = targetName;
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
            if (!obj.isNull(KEY_IMAGE)) {
                setImage(obj.getString(KEY_IMAGE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TYPE)) {
                setType(obj.getString(KEY_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TARGET_NAME)) {
                setTargetName(obj.getString(KEY_TARGET_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TARGET)) {
                setTarget(obj.getString(KEY_TARGET));
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
            if (!TextUtils.isEmpty(getImage())) {
                obj.put(KEY_IMAGE, getImage());
            } else {
                obj.put(KEY_IMAGE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getType())) {
                obj.put(KEY_TYPE, getType());
            } else {
                obj.put(KEY_TYPE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getTargetName())) {
                obj.put(KEY_TARGET_NAME, getTargetName());
            } else {
                obj.put(KEY_TARGET_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getTarget())) {
                obj.put(KEY_TARGET, getTarget());
            } else {
                obj.put(KEY_TARGET, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}