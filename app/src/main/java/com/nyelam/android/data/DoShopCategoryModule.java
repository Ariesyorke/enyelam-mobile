package com.nyelam.android.data;

import org.json.JSONException;
import org.json.JSONObject;

public class DoShopCategoryModule extends DoShopList {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_IMAGE = "image_url";

    private String id;
    private String name;
    private String imageUrl;

    public DoShopCategoryModule(){

    }

    public DoShopCategoryModule(String id, String name, String imageUrl){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

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

    public String getImageUri() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
            if (!obj.isNull(KEY_IMAGE)) {
                setImageUrl(obj.getString(KEY_IMAGE));
            }
        } catch (JSONException e) {e.printStackTrace();}
    }
}