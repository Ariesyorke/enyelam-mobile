package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class Review implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_RATING = "rating";
    private static String KEY_DATE = "date";
    private static String KEY_CONTENT = "content";
    private static String KEY_USER = "user";

    private String id;
    private int rating;
    private long date;
    private String content;
    private User user;

    public Review(){}

    public Review(String id, int rating, long date, String content, User user){
        this.id = id;
        this.rating = rating;
        this.date = date;
        this.content = content;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
            if (!obj.isNull(KEY_RATING)) {
                setRating(obj.getInt(KEY_RATING));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DATE)) {
                setDate(obj.getLong(KEY_DATE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CONTENT)) {
                setContent(obj.getString(KEY_CONTENT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(!obj.isNull(KEY_USER)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_USER);
                if(o != null) {
                    user = new User();
                    user.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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
            obj.put(KEY_RATING, getRating());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DATE, getDate());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getContent())) {
                obj.put(KEY_CONTENT, getContent());
            } else {
                obj.put(KEY_CONTENT, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getUser()!=null){
                JSONObject objUser = new JSONObject(getUser().toString());
                obj.put(KEY_USER, objUser);
            } else {
                obj.put(KEY_USER, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}