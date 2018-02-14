package com.nyelam.android.data;

import com.nyelam.android.dev.NYLog;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/14/2018.
 */

public class Module implements Parseable {

    private static String KEY_MODULES_NAME = "module_name";
    private static String KEY_EVENTS = "events";
    private static String KEY_DIVE_SERVICES = "dive_services";
    private static String KEY_DIVE_SPOTS = "dive_spots";

    private String name;
    private List<Object> list;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_MODULES_NAME)) {
                setName(obj.getString(KEY_MODULES_NAME));
            }
        } catch (JSONException e){e.printStackTrace();}


        try {
            if(!obj.isNull(KEY_EVENTS)) {
                list = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_EVENTS);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    DiveService diveService = new DiveService();
                    diveService.parse(o);
                    list.add(diveService);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            if(!obj.isNull(KEY_DIVE_SERVICES)) {
                list = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_DIVE_SERVICES);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    DiveService diveService = new DiveService();
                    diveService.parse(o);
                    list.add(diveService);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            if(!obj.isNull(KEY_DIVE_SPOTS)) {
                list = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_DIVE_SPOTS);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    DiveSpot diveSpot = new DiveSpot();
                    diveSpot.parse(o);
                    list.add(diveSpot);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_MODULES_NAME, getName());
            } else  {
                obj.put(KEY_MODULES_NAME, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}


        NYLog.e("MY NAME IS : "+getList().getClass().getName());

        /*try {
            if (getList() != null && !getList().isEmpty()) {
                JSONArray array = new JSONArray();
                for (SocialMedia a : getList() ) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_SOCIAL_MEDIA, array);
            } else  {
                obj.put(KEY_SOCIAL_MEDIA, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}*/

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }


}
