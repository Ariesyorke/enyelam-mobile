package com.nyelam.android.data;

import android.text.TextUtils;

import com.nyelam.android.dev.NYLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveCenter implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_PHONE = "phone";
    private static String KEY_IMAGES = "images";
    private static String KEY_CONTACT_NAME = "contact_name";
    private static String KEY_CONTACT_PHONE = "contact_phone";
    private static String KEY_CONTACT_EMAIL = "contact_email";
    private static String KEY_LOCATION = "location";
    private static String KEY_STATUS_ACTIVE = "status_active";

    private String id;
    private String name;
    private String phone;
    private List<String> images;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private Location location;
    private int statusActive;


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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getStatusActive() {
        return statusActive;
    }

    public void setStatusActive(int statusActive) {
        this.statusActive = statusActive;
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
            if (!obj.isNull(KEY_PHONE)) {
                setPhone(obj.getString(KEY_PHONE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(!obj.isNull(KEY_IMAGES)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_IMAGES);
                if(array != null && array.length() > 0) {
                    images = new ArrayList<>();
                    for(int i = 0; i <array.length(); i++) {
                        String image = array.getString(i);
                        images.add(image);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!obj.isNull(KEY_CONTACT_NAME)) {
                setContactName(obj.getString(KEY_CONTACT_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CONTACT_PHONE)) {
                setContactPhone(obj.getString(KEY_CONTACT_PHONE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CONTACT_EMAIL)) {
                setContactEmail(obj.getString(KEY_CONTACT_EMAIL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(!obj.isNull(KEY_LOCATION)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_LOCATION);
                if(o != null && o.length() > 0) {
                    location = new Location();
                    location.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!obj.isNull(KEY_STATUS_ACTIVE)) {
                setStatusActive(obj.getInt(KEY_STATUS_ACTIVE));
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
            if (!TextUtils.isEmpty(getPhone())) {
                obj.put(KEY_PHONE, getPhone());
            } else {
                obj.put(KEY_PHONE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getContactName())) {
                obj.put(KEY_CONTACT_NAME, getContactName());
            } else {
                obj.put(KEY_CONTACT_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getContactPhone())) {
                obj.put(KEY_CONTACT_PHONE, getContactPhone());
            } else {
                obj.put(KEY_CONTACT_PHONE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getContactEmail())) {
                obj.put(KEY_CONTACT_EMAIL, getContactEmail());
            } else {
                obj.put(KEY_CONTACT_EMAIL, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getLocation()!=null){
                obj.put(KEY_LOCATION, getLocation());
            } else {
                obj.put(KEY_LOCATION, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(images != null && !images.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(String image : images) {
                    array.put(image);
                }
                obj.put(KEY_IMAGES, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        try {
            obj.put(KEY_STATUS_ACTIVE, statusActive);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
