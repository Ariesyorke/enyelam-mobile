package com.nyelam.android.data;

import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Location implements Parseable {

    private static String KEY_COUNTRY = "country";
    private static String KEY_PROVINCE = "province";
    private static String KEY_CITY = "city";
    private static String KEY_COORDINATE = "coordinate";

    private String country;
    private Province province;
    private City city;
    private Coordinate coordinate;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_COUNTRY)) {
                setCountry(obj.getString(KEY_COUNTRY));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(!obj.isNull(KEY_PROVINCE)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_PROVINCE);
                if(o != null && o.length() > 0) {
                    province = new Province();
                    province.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_CITY)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_CITY);
                if(o != null && o.length() > 0) {
                    city = new City();
                    city.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_COORDINATE)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_COORDINATE);
                if(o != null && o.length() > 0) {
                    coordinate = new Coordinate();
                    coordinate.parse(o);
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
            if (!TextUtils.isEmpty(getCountry())) {
                obj.put(KEY_COUNTRY, getCountry());
            } else {
                obj.put(KEY_COUNTRY, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getProvince()!=null){
                obj.put(KEY_PROVINCE, getProvince());
            } else {
                obj.put(KEY_PROVINCE, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getCity()!=null){
                obj.put(KEY_CITY, getCity());
            } else {
                obj.put(KEY_CITY, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getCoordinate()!=null){
                obj.put(KEY_COORDINATE, getCoordinate());
            } else {
                obj.put(KEY_COORDINATE, JSONObject.NULL);
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