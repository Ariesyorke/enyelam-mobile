package com.nyelam.android.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCategory;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class DoShopCategory implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_DESCRIPTION = "description";
    private static String KEY_IMAGE = "image_url";

    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private int drawable;

    public DoShopCategory(){

    }

    public DoShopCategory(String id, String name, String imageUrl){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public DoShopCategory(String id, String name, int drawable){
        this.id = id;
        this.name = name;
        this.drawable = drawable;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
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
            if (!obj.isNull(KEY_DESCRIPTION)) {
                setDescription(obj.getString(KEY_DESCRIPTION));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_IMAGE)) {
                setImageUrl(obj.getString(KEY_IMAGE));
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
            if (!TextUtils.isEmpty(getDescription())) {
                obj.put(KEY_DESCRIPTION, getDescription());
            } else {
                obj.put(KEY_DESCRIPTION, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getImageUri())) {
                obj.put(KEY_IMAGE, getImageUri());
            } else {
                obj.put(KEY_IMAGE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }
}