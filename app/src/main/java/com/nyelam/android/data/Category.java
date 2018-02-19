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

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Category implements Parseable, DAODataBridge<NYCategory> {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_ICON = "icon";

    private String id;
    private String name;
    private String iconUrl;
    private Bitmap iconImage;

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Bitmap getIconImage() {
        return iconImage;
    }

    public void setIconImage(Bitmap iconImage) {
        this.iconImage = iconImage;
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
            if (!obj.isNull(KEY_ICON)) {
                setIconUrl(obj.getString(KEY_ICON));
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
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


    @Override
    public void copyFrom(NYCategory nyCategory) {
        this.id = nyCategory.getId();
        this.name = nyCategory.getName();
        this.iconUrl = nyCategory.getIconUrl();
        if (nyCategory.getIconImage() != null && !TextUtils.isEmpty(nyCategory.getIconImage().toString())){
            this.iconImage = BitmapFactory.decodeByteArray(nyCategory.getIconImage(), 0, nyCategory.getIconImage().length);
        }
    }

    @Override
    public void copyTo(NYCategory nyCategory, DaoSession session, Object... args) {
        nyCategory.setId(id);
        nyCategory.setName(name);
        nyCategory.setIconUrl(iconUrl);

        // Load image, decode it to Bitmap and return Bitmap to callback
        try {

            if (!TextUtils.isEmpty(iconUrl)){
                Bitmap b = ImageLoader.getInstance().loadImageSync(iconUrl);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                nyCategory.setIconImage(stream.toByteArray());
            }

        } catch (Exception e) {

        }
    }


}