package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class Banner implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_IMAGE_URL = "image_url";
    private static String KEY_CAPTION = "caption";
    private static String KEY_URL = "url";

    private String id;
    private String imageUrl;
    private String caption;
    private String url;

    public Banner(){

    }

    public Banner(String id, String imageUrl, String caption, String url){
        this.id = id;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
            if (!obj.isNull(KEY_IMAGE_URL)) {
                setImageUrl(obj.getString(KEY_IMAGE_URL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CAPTION)) {
                setCaption(obj.getString(KEY_CAPTION));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_URL)) {
                setUrl(obj.getString(KEY_URL));
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
            if (!TextUtils.isEmpty(getImageUrl())) {
                obj.put(KEY_IMAGE_URL, getImageUrl());
            } else {
                obj.put(KEY_IMAGE_URL, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getCaption())) {
                obj.put(KEY_CAPTION, getCaption());
            } else {
                obj.put(KEY_CAPTION, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getUrl())) {
                obj.put(KEY_URL, getUrl());
            } else {
                obj.put(KEY_URL, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }





}