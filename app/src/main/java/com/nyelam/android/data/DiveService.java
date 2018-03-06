package com.nyelam.android.data;

import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveService implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_RATING = "rating";
    private static String KEY_RATING_COUNT = "rating_count";
    private static String KEY_CATEGORY = "category";
    private static String KEY_FEATURED_IMAGE = "featured_image";
    private static String KEY_DIVE_SPOTS = "dive_spot";
    private static String KEY_DAYS = "days";
    private static String KEY_TOTAL_DIVES = "total_dives";
    private static String KEY_MIN_PERSON = "min_person";
    private static String KEY_MAX_PERSON = "max_person";
    private static String KEY_SCHEDULE = "schedule";
    private static String KEY_FACILITIES = "facilities";
    private static String KEY_NORMAL_PRICE = "normal_price";
    private static String KEY_SPECIAL_PRICE = "special_price";
    private static String KEY_DIVE_CENTER = "dive_center";
    private static String KEY_IMAGES = "images";
    private static String KEY_DESCRIPTION = "description";

    private String id;
    private String name;
    private int rating;
    private int ratingCount;
    private List<Category> categories;
    private String featuredImage;
    private List<DiveSpot> diveSpots;
    private int days;
    private int totalDives;
    private int minPerson;
    private int maxPerson;
    private Schedule schedule;
    private Facilities facilities;
    private double normalPrice;
    private double specialPrice;
    private DiveCenter diveCenter;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private List<String> images;

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public List<DiveSpot> getDiveSpots() {
        return diveSpots;
    }

    public void setDiveSpots(List<DiveSpot> diveSpots) {
        this.diveSpots = diveSpots;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getTotalDives() {
        return totalDives;
    }

    public void setTotalDives(int totalDives) {
        this.totalDives = totalDives;
    }

    public int getMinPerson() {
        return minPerson;
    }

    public void setMinPerson(int minPerson) {
        this.minPerson = minPerson;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Facilities getFacilities() {
        return facilities;
    }

    public void setFacilities(Facilities facilities) {
        this.facilities = facilities;
    }

    public double getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(double normalPrice) {
        this.normalPrice = normalPrice;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public DiveCenter getDiveCenter() {
        return diveCenter;
    }

    public void setDiveCenter(DiveCenter diveCenter) {
        this.diveCenter = diveCenter;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;


        try {
            if(!obj.isNull(KEY_DESCRIPTION)) {
                setDescription(obj.getString(KEY_DESCRIPTION));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            if (!obj.isNull(KEY_FEATURED_IMAGE)) {
                setFeaturedImage(obj.getString(KEY_FEATURED_IMAGE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_RATING)) {
                setRating(obj.getInt(KEY_RATING));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_RATING_COUNT)) {
                setRatingCount(obj.getInt(KEY_RATING_COUNT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DAYS)) {
                setDays(obj.getInt(KEY_DAYS));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TOTAL_DIVES)) {
                setTotalDives(obj.getInt(KEY_TOTAL_DIVES));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_MIN_PERSON)) {
                setMinPerson(obj.getInt(KEY_MIN_PERSON));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_MAX_PERSON)) {
                setMaxPerson(obj.getInt(KEY_MAX_PERSON));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NORMAL_PRICE)) {
                setNormalPrice(obj.getInt(KEY_NORMAL_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SPECIAL_PRICE)) {
                setSpecialPrice(obj.getInt(KEY_SPECIAL_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}



        if(!obj.isNull(KEY_SCHEDULE)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_SCHEDULE);
                if(o != null && o.length() > 0) {
                    Schedule schedule = new Schedule();
                    schedule.parse(o);
                    setSchedule(schedule);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(!obj.isNull(KEY_FACILITIES)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_FACILITIES);
                if(o != null && o.length() > 0) {
                    Facilities facilities = new Facilities();
                    facilities.parse(o);
                    setFacilities(facilities);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_CATEGORY)) {
            try {
                if(obj.get(KEY_CATEGORY) instanceof JSONArray) {
                    JSONArray array = obj.getJSONArray(KEY_CATEGORY);
                    if (array != null && array.length() > 0) {
                        List<Category> categories = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            Category a = new Category();
                            a.parse(o);
                            categories.add(a);
                        }
                        setCategories(categories);
                    }
                }  else if (obj.get(KEY_CATEGORY) instanceof JSONObject) {
                    JSONObject o = obj.getJSONObject(KEY_CATEGORY);
                    if(o != null && o.length() > 0) {
                        List<Category> categories = new ArrayList<>();
                        Category d = new Category();
                        d.parse(o);
                        categories.add(d);
                        setCategories(categories);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(!obj.isNull(KEY_DIVE_CENTER)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_DIVE_CENTER);
                if(o != null && o.length() > 0) {
                    DiveCenter diveCenter = new DiveCenter();
                    diveCenter.parse(o);
                    setDiveCenter(diveCenter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(!obj.isNull(KEY_DIVE_SPOTS)) {
            try {
                if(obj.get(KEY_DIVE_SPOTS) instanceof JSONArray) {
                    JSONArray array = obj.getJSONArray(KEY_DIVE_SPOTS);
                    if (array != null && array.length() > 0) {
                        diveSpots = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            DiveSpot a = new DiveSpot();
                            a.parse(o);
                            diveSpots.add(a);
                        }
                    }
                }  else if (obj.get(KEY_DIVE_SPOTS) instanceof JSONObject) {
                    JSONObject o = obj.getJSONObject(KEY_DIVE_SPOTS);
                    if(o != null && o.length() > 0) {
                        List<DiveSpot> diveSpots = new ArrayList<>();
                        DiveSpot d = new DiveSpot();
                        d.parse(o);
                        diveSpots.add(d);
                        setDiveSpots(diveSpots);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_IMAGES)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_IMAGES);
                for(int i = 0; i < array.length(); i++) {
                    if(images == null) {
                        images = new ArrayList<>();
                    }
                    images.add(array.getString(i));
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
            if(!TextUtils.isEmpty(getDescription())) {
                obj.put(KEY_DESCRIPTION, getDescription());
            } else {
                obj.put(KEY_DESCRIPTION, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            if (!TextUtils.isEmpty(getFeaturedImage())) {
                obj.put(KEY_FEATURED_IMAGE, getFeaturedImage());
            } else {
                obj.put(KEY_FEATURED_IMAGE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_RATING, getRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_RATING_COUNT, getRatingCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_DAYS, getDays());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_TOTAL_DIVES, getTotalDives());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_MIN_PERSON, getMinPerson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put(KEY_MAX_PERSON, getMaxPerson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_NORMAL_PRICE, getNormalPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_SPECIAL_PRICE, getSpecialPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            if(getSchedule()!=null){
                JSONObject objSchedule = new JSONObject(getSchedule().toString());
                obj.put(KEY_SCHEDULE, objSchedule);
            } else {
                obj.put(KEY_SCHEDULE, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getFacilities()!=null){
                JSONObject objFac = new JSONObject(getFacilities().toString());
                obj.put(KEY_FACILITIES, objFac);
            } else {
                obj.put(KEY_FACILITIES, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(categories != null && !categories.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(Category c : categories) {
                    JSONObject o = new JSONObject(c.toString());
                    array.put(o);
                }
                obj.put(KEY_CATEGORY, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try{
            if(getDiveCenter()!=null){
                JSONObject objDev = new JSONObject(getDiveCenter().toString());
                obj.put(KEY_DIVE_CENTER, objDev);
            } else {
                obj.put(KEY_DIVE_CENTER, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(diveSpots != null && !diveSpots.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(DiveSpot d : diveSpots) {
                    JSONObject o = new JSONObject(d.toString());
                    array.put(o);
                }
                obj.put(KEY_DIVE_SPOTS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}