package com.nyelam.android.data;

import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCountryCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class CountryCode implements Parseable, DAODataBridge<NYCountryCode> {
    private static final String KEY_ID = "id";
    private static final String KEY_COUNTRY_CODE = "country_code";
    private static final String KEY_COUNTRY_NAME = "country_name";
    private static final String KEY_COUNTRY_NUMBER = "country_number";
    private static final String KEY_COUNTRY_IMAGE = "country_image";

    private String id;
    private String countryCode;
    private String countryName;
    private String countryNumber;
    private String countryImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNumber() {
        return countryNumber;
    }

    public void setCountryNumber(String countryNumber) {
        this.countryNumber = countryNumber;
    }

    public String getCountryImage() {
        return countryImage;
    }

    public void setCountryImage(String countryImage) {
        this.countryImage = countryImage;
    }

    @Override
    public void parse(JSONObject obj) {
        if(obj == null) return;

        if(!obj.isNull(KEY_ID)) {
            try {
                setId(obj.getString(KEY_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_COUNTRY_NAME)) {
            try {
                setCountryName(obj.getString(KEY_COUNTRY_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_COUNTRY_CODE)) {
            try {
                setCountryCode(obj.getString(KEY_COUNTRY_CODE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_COUNTRY_IMAGE)) {
            try {
                setCountryImage(obj.getString(KEY_COUNTRY_IMAGE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_COUNTRY_NUMBER)) {
            try {
                setCountryNumber(obj.getString(KEY_COUNTRY_NUMBER));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try{
            if(getId()!=null){
                obj.put(KEY_ID, getId());
            } else {
                obj.put(KEY_ID, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getCountryCode()!=null){
                obj.put(KEY_COUNTRY_CODE, getCountryCode());
            } else {
                obj.put(KEY_COUNTRY_CODE, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getCountryImage()!=null){
                obj.put(KEY_COUNTRY_IMAGE, getCountryImage());
            } else {
                obj.put(KEY_COUNTRY_IMAGE, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getCountryName()!=null){
                obj.put(KEY_COUNTRY_NAME, getCountryName());
            } else {
                obj.put(KEY_COUNTRY_NAME, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getCountryNumber()!=null){
                obj.put(KEY_COUNTRY_NUMBER, getCountryNumber());
            } else {
                obj.put(KEY_COUNTRY_NUMBER, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.toString();
    }

    @Override
    public void copyFrom(NYCountryCode country) {
        id = country.getId();
        countryCode = country.getCountryCode();
        countryName = country.getCountryName();
        countryImage = country.getCountryImage();
        countryNumber = country.getCountryNumber();
    }

    @Override
    public void copyTo(NYCountryCode country, DaoSession session, Object... args) {
        country.setId(getId());
        country.setCountryNumber(getCountryNumber());
        country.setCountryCode(getCountryCode());
        country.setCountryImage(getCountryImage());
        country.setCountryName(getCountryName());
    }

}