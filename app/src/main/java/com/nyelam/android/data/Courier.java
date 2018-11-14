package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Courier implements Parseable {

    private static String KEY_CODE = "code";
    private static String KEY_NAME = "name";
    private static String KEY_COSTS = "costs";

    private String code;
    private String name;
    private List<CourierType> courierTypes;


    public Courier (){}

    public Courier (String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CourierType> getCourierTypes() {
        return courierTypes;
    }

    public void setCourierTypes(List<CourierType> courierTypes) {
        this.courierTypes = courierTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_CODE)) {
                setCode(obj.getString(KEY_CODE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NAME)) {
                setName(obj.getString(KEY_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(!obj.isNull(KEY_COSTS)) {
            try {
                if(obj.get(KEY_COSTS) instanceof JSONArray) {
                    JSONArray array = obj.getJSONArray(KEY_COSTS);
                    if (array != null && array.length() > 0) {
                        courierTypes = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            CourierType p = new CourierType();
                            p.parse(o);
                            courierTypes.add(p);
                        }
                    }
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
            if (!TextUtils.isEmpty(getCode())) {
                obj.put(KEY_CODE, getCode());
            } else {
                obj.put(KEY_CODE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_NAME, getName());
            } else {
                obj.put(KEY_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}


        if(courierTypes != null && !courierTypes.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(CourierType p : courierTypes) {
                    JSONObject o = new JSONObject(p.toString());
                    array.put(o);
                }
                obj.put(KEY_COSTS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}