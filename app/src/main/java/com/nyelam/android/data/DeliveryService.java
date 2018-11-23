package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class DeliveryService implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_TYPES = "types";
    private static String KEY_PRICE = "price";

    private String id;
    private String name;
    private double price;
    private List<DeliveryServiceType> types;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<DeliveryServiceType> getTypes() {
        return types;
    }

    public void setTypes(List<DeliveryServiceType> types) {
        this.types = types;
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
            if (!obj.isNull(KEY_PRICE)) {
                setPrice(obj.getDouble(KEY_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if (!obj.isNull(KEY_TYPES)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_TYPES);
                if (array != null && array.length() > 0) {
                    types = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        DeliveryServiceType a = new DeliveryServiceType();
                        a.parse(o);
                        types.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }


    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (NYHelper.isStringNotEmpty(getId())) {
                obj.put(KEY_ID, getId());
            } else {
                obj.put(KEY_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}


        try {
            if (NYHelper.isStringNotEmpty(getName())) {
                obj.put(KEY_NAME, getName());
            } else {
                obj.put(KEY_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_PRICE, getPrice());
        } catch (JSONException e) {e.printStackTrace();}


        if(types != null && !types.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(DeliveryServiceType a : types) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_TYPES, array);
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
