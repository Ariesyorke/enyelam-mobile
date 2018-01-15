package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public abstract class SearchResult implements Parseable {

    protected static String KEY_ID = "id";
    protected static String KEY_NAME = "name";
    protected static String KEY_RATING = "rating";
    protected static String KEY_TYPE = "type";
    protected static String KEY_COUNT = "count";

    protected String id;
    protected String name;
    protected String rating;
    protected Integer type;
    protected Integer count;

    public SearchResult(){}

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
            if (!obj.isNull(KEY_RATING)) {
                setRating(obj.getString(KEY_RATING));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TYPE)) {
                setType(obj.getInt(KEY_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_COUNT)) {
                setCount(obj.getInt(KEY_COUNT));
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
            if (!TextUtils.isEmpty(getRating())) {
                obj.put(KEY_RATING, getRating());
            } else {
                obj.put(KEY_RATING, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (getType() != null) {
                obj.put(KEY_TYPE, getType());
            } else {
                obj.put(KEY_TYPE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (getCount() != null) {
                obj.put(KEY_COUNT, getCount());
            } else {
                obj.put(KEY_COUNT, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}


        return super.toString();
    }



}
