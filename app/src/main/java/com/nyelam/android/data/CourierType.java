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

public class CourierType implements Parseable {

    private static String KEY_SERVICE = "service";
    private static String KEY_DESCRIPTION = "description";
    private static String KEY_COST = "cost";

    private String service;
    private String description;
    private List<CourierCost> courierCosts;

    public CourierType(){

    }

    public CourierType(String service, String description){
        this.service = service;
        this.description = description;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CourierCost> getCourierCosts() {
        return courierCosts;
    }

    public void setCourierCosts(List<CourierCost> courierCosts) {
        this.courierCosts = courierCosts;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_SERVICE)) {
                setService(obj.getString(KEY_SERVICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DESCRIPTION)) {
                setDescription(obj.getString(KEY_DESCRIPTION));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(!obj.isNull(KEY_COST)) {
            try {
                if(obj.get(KEY_COST) instanceof JSONArray) {
                    JSONArray array = obj.getJSONArray(KEY_COST);
                    if (array != null && array.length() > 0) {
                        courierCosts = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            CourierCost p = new CourierCost();
                            p.parse(o);
                            courierCosts.add(p);
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
            if (!TextUtils.isEmpty(getService())) {
                obj.put(KEY_SERVICE, getService());
            } else {
                obj.put(KEY_SERVICE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getDescription())) {
                obj.put(KEY_DESCRIPTION, getDescription());
            } else {
                obj.put(KEY_DESCRIPTION, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(courierCosts != null && !courierCosts.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(CourierCost p : courierCosts) {
                    JSONObject o = new JSONObject(p.toString());
                    array.put(o);
                }
                obj.put(KEY_COST, array);
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