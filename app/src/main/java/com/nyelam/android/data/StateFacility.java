package com.nyelam.android.data;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 4/12/2018.
 */

public class StateFacility implements Parseable {

    private static String KEY_TAG = "tag";
    private static String KEY_NAME = "name";
    private static String KEY_IS_CHECKED = "is_checked";
    private static String KEY_DRAWABLE_ACTIVE = "drawable_active";
    private static String KEY_DRAWABLE_UNACTIVE = "drawable_unactive";

    private String tag;
    private String name;
    private boolean isChecked;
    private int activeDrawable;
    private int unactiveDrawable;

    public StateFacility(){}

    public StateFacility(String tag, String name, boolean isChecked, int activeDrawable, int unactiveDrawable){
        this.tag = tag;
        this.name = name;
        this.isChecked = isChecked;
        this.activeDrawable = activeDrawable;
        this.unactiveDrawable = unactiveDrawable;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getActiveDrawable() {
        return activeDrawable;
    }

    public void setActiveDrawable(int activeDrawable) {
        this.activeDrawable = activeDrawable;
    }

    public int getUnactiveDrawable() {
        return unactiveDrawable;
    }

    public void setUnactiveDrawable(int unactiveDrawable) {
        this.unactiveDrawable = unactiveDrawable;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_TAG)) {
                setTag(obj.getString(KEY_TAG));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NAME)) {
                setName(obj.getString(KEY_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_IS_CHECKED)) {
                setChecked(obj.getBoolean(KEY_IS_CHECKED));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            setActiveDrawable(obj.getInt(KEY_DRAWABLE_ACTIVE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setUnactiveDrawable(obj.getInt(KEY_DRAWABLE_UNACTIVE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getTag())) {
                obj.put(KEY_TAG, getTag());
            } else {
                obj.put(KEY_TAG, JSONObject.NULL);
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
            obj.put(KEY_IS_CHECKED, isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_DRAWABLE_ACTIVE, getActiveDrawable());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_DRAWABLE_UNACTIVE, getUnactiveDrawable());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}