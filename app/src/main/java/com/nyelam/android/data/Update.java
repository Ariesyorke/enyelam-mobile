package com.nyelam.android.data;

import android.text.TextUtils;
import com.nyelam.android.helper.NYHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 3/12/2018.
 */

public class Update implements Parseable {

    private static String KEY_LATEST_VERSION = "latest_version";
    private static String KEY_WORDING = "wording";
    private static String KEY_LINK = "link";
    private static String KEY_IS_MUST = "is_must";

    private Integer latestVersion;
    private String wording;
    private String link;
    private boolean isMust;

    public Integer getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(Integer latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (obj.has(KEY_LATEST_VERSION) && obj.get(KEY_LATEST_VERSION) instanceof String){
                setLatestVersion(Integer.valueOf(obj.getString(KEY_LATEST_VERSION)));
            } else if (obj.has(KEY_LATEST_VERSION) && obj.get(KEY_LATEST_VERSION) instanceof Integer){
                setLatestVersion(obj.getInt(KEY_LATEST_VERSION));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (obj.has(KEY_WORDING) && NYHelper.isStringNotEmpty(obj.getString(KEY_WORDING))){
                setWording(obj.getString(KEY_WORDING));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (obj.has(KEY_LINK) && NYHelper.isStringNotEmpty(obj.getString(KEY_LINK))){
                setLink(obj.getString(KEY_LINK));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (obj.has(KEY_IS_MUST) && obj.get(KEY_IS_MUST) instanceof String){
                if (Integer.valueOf(obj.getString(KEY_IS_MUST)) > 0){
                    setMust(true);
                } else {
                    setMust(false);
                }
            } else if (obj.has(KEY_IS_MUST) && obj.get(KEY_IS_MUST) instanceof Integer){
                if (obj.getInt(KEY_IS_MUST) > 0){
                    setMust(true);
                } else {
                    setMust(false);
                }
            } else if (obj.has(KEY_IS_MUST) && obj.get(KEY_IS_MUST) instanceof Boolean){
                setMust(obj.getBoolean(KEY_IS_MUST));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            obj.put(KEY_LATEST_VERSION, getLatestVersion());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!TextUtils.isEmpty(getWording())) {
                obj.put(KEY_WORDING, getWording());
            } else {
                obj.put(KEY_WORDING, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!TextUtils.isEmpty(getLink())) {
                obj.put(KEY_LINK, getLink());
            } else {
                obj.put(KEY_LINK, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_IS_MUST, isMust());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }



}
