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

public class DiveCenter implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_SUBTITLE = "subtitle";
    private static String KEY_IMAGE_LOGO = "image_logo";
    private static String KEY_IMAGES = "picture";
    private static String KEY_RATING = "rating";
    private static String KEY_CONTACT = "contact";
    private static String KEY_MEMBERSHIP = "membership";
    private static String KEY_STATUS = "status";

    private String id;
    private String name;
    private String subtitle;
    private String imageLogo;
    private List<String> images;
    private int rating;
    private Contact contact;
    private Membership membership;
    private int status;

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
            if (!obj.isNull(KEY_SUBTITLE)) {
                setSubtitle(obj.getString(KEY_SUBTITLE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_IMAGE_LOGO)) {
                setImageLogo(obj.getString(KEY_IMAGE_LOGO));
            }
        } catch (JSONException e) {e.printStackTrace();}

        /*if(!obj.isNull(KEY_IMAGES)) {
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
        }*/

        if(!obj.isNull(KEY_IMAGES)) {
            try {
                if(obj.get(KEY_IMAGES) instanceof JSONArray) {
                    JSONArray array = obj.getJSONArray(KEY_IMAGES);
                    if (array != null && array.length() > 0) {
                        images = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            String a = o.getString(KEY_IMAGES);
                            images.add(a);
                        }
                    }
                }  else if (obj.get(KEY_IMAGES) instanceof JSONObject) {
                    JSONObject o = obj.getJSONObject(KEY_IMAGES);
                    if(o != null && o.length() > 0) {
                        images = new ArrayList<>();
                        String d = o.getString(KEY_IMAGES);
                        images.add(d);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_CONTACT)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_CONTACT);
                if(o != null && o.length() > 0) {
                    contact = new Contact();
                    contact.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_MEMBERSHIP)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_MEMBERSHIP);
                if(o != null && o.length() > 0) {
                    membership = new Membership();
                    membership.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!obj.isNull(KEY_RATING)) {
                setRating(obj.getInt(KEY_RATING));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_STATUS)) {
                setStatus(obj.getInt(KEY_STATUS));
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
            if (!TextUtils.isEmpty(getSubtitle())) {
                obj.put(KEY_SUBTITLE, getSubtitle());
            } else {
                obj.put(KEY_SUBTITLE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getImageLogo())) {
                obj.put(KEY_IMAGE_LOGO, getImageLogo());
            } else {
                obj.put(KEY_IMAGE_LOGO, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

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

        try{
            if(getContact()!=null){
                obj.put(KEY_CONTACT, getContact());
            } else {
                obj.put(KEY_CONTACT, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getMembership()!=null){
                obj.put(KEY_MEMBERSHIP, getMembership());
            } else {
                obj.put(KEY_MEMBERSHIP, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        try {
            obj.put(KEY_RATING, rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_STATUS, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
