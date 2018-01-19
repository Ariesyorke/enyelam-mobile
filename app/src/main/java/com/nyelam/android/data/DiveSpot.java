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

public class DiveSpot implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_FEATURED_IMAGE = "featured_image";
    private static String KEY_IMAGES = "images";
    private static String KEY_HIGHLIGHT = "highlight";
    private static String KEY_GREAT_FOR = "great_for";
    private static String KEY_DEPTH_MIN = "depth_min";
    private static String KEY_DEPTH_MAX = "depth_max";
    private static String KEY_VISIBILITY_MIN = "visiblity_min";
    private static String KEY_VISIBILITY_MAX = "visibility_max";
    private static String KEY_CURRENT_STATUS = "current_status";
    private static String KEY_SURFACE_CONDITION = "surface_condition";
    private static String KEY_WATER_TEMPERATURE_MIN = "temperature_min";
    private static String KEY_WATER_TEMPERATURE_MAX = "temperature_max";
    private static String KEY_EXPERIENCE_MIN = "experience_min";
    private static String KEY_EXPERIENCE_MAX = "experience_max";
    private static String KEY_RECOMENDED_STAY_MIN = "recommended_stay_min";
    private static String KEY_RECOMENDED_STAY_MAX = "recommended_stay_max";
    private static String KEY_LOCATION = "location";
    private static String KEY_RATING = "rating";
    private static String KEY_SHORT_DESCRIPTION = "short_description";
    private static String KEY_DESCRIPTION = "description";
    private static String KEY_STATUS_ACTIVE = "status_active";

    private String id;
    private String name;
    private String featuredImage;
    private List<String> images;
    private String highlight;
    private String greatFor;
    private int depthMin;
    private int depthMax;
    private int visiblityMin;
    private int visibilityMax;
    private String currentStatus;
    private String surfaceCondition;
    private int temperatureMin;
    private int temperatureMax;
    private String experienceMin;
    private String experienceMax;
    private int recommendedStayMin;
    private int recommendedStayMax;
    private Location location;
    private int rating;
    private String shortDescription;
    private String description;
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

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getGreatFor() {
        return greatFor;
    }

    public void setGreatFor(String greatFor) {
        this.greatFor = greatFor;
    }

    public int getDepthMin() {
        return depthMin;
    }

    public void setDepthMin(int depthMin) {
        this.depthMin = depthMin;
    }

    public int getDepthMax() {
        return depthMax;
    }

    public void setDepthMax(int depthMax) {
        this.depthMax = depthMax;
    }

    public int getVisiblityMin() {
        return visiblityMin;
    }

    public void setVisiblityMin(int visiblityMin) {
        this.visiblityMin = visiblityMin;
    }

    public int getVisibilityMax() {
        return visibilityMax;
    }

    public void setVisibilityMax(int visibilityMax) {
        this.visibilityMax = visibilityMax;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getSurfaceCondition() {
        return surfaceCondition;
    }

    public void setSurfaceCondition(String surfaceCondition) {
        this.surfaceCondition = surfaceCondition;
    }

    public int getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(int temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public int getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(int temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getExperienceMin() {
        return experienceMin;
    }

    public void setExperienceMin(String experienceMin) {
        this.experienceMin = experienceMin;
    }

    public String getExperienceMax() {
        return experienceMax;
    }

    public void setExperienceMax(String experienceMax) {
        this.experienceMax = experienceMax;
    }

    public int getRecommendedStayMin() {
        return recommendedStayMin;
    }

    public void setRecommendedStayMin(int recommendedStayMin) {
        this.recommendedStayMin = recommendedStayMin;
    }

    public int getRecommendedStayMax() {
        return recommendedStayMax;
    }

    public void setRecommendedStayMax(int recommendedStayMax) {
        this.recommendedStayMax = recommendedStayMax;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
            if (!obj.isNull(KEY_FEATURED_IMAGE)) {
                setFeaturedImage(obj.getString(KEY_FEATURED_IMAGE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_HIGHLIGHT)) {
                setHighlight(obj.getString(KEY_HIGHLIGHT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_GREAT_FOR)) {
                setGreatFor(obj.getString(KEY_GREAT_FOR));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DEPTH_MIN)) {
                setDepthMin(obj.getInt(KEY_DEPTH_MIN));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DEPTH_MAX)) {
                setDepthMax(obj.getInt(KEY_DEPTH_MAX));
            }
        } catch (JSONException e) {e.printStackTrace();}
        try {
            if (!obj.isNull(KEY_VISIBILITY_MIN)) {
                setVisiblityMin(obj.getInt(KEY_VISIBILITY_MIN));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_VISIBILITY_MAX)) {
                setVisibilityMax(obj.getInt(KEY_VISIBILITY_MAX));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CURRENT_STATUS)) {
                setCurrentStatus(obj.getString(KEY_CURRENT_STATUS));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SURFACE_CONDITION)) {
                setSurfaceCondition(obj.getString(KEY_SURFACE_CONDITION));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_WATER_TEMPERATURE_MIN)) {
                setTemperatureMin(obj.getInt(KEY_WATER_TEMPERATURE_MIN));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_WATER_TEMPERATURE_MAX)) {
                setTemperatureMax(obj.getInt(KEY_WATER_TEMPERATURE_MAX));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_EXPERIENCE_MIN)) {
                setExperienceMin(obj.getString(KEY_EXPERIENCE_MIN));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_EXPERIENCE_MAX)) {
                setExperienceMax(obj.getString(KEY_EXPERIENCE_MAX));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_RECOMENDED_STAY_MIN)) {
                setRecommendedStayMin(obj.getInt(KEY_RECOMENDED_STAY_MIN));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_RECOMENDED_STAY_MAX)) {
                setRecommendedStayMax(obj.getInt(KEY_RECOMENDED_STAY_MAX));
            }
        } catch (JSONException e) {e.printStackTrace();}
        try {
            if (!obj.isNull(KEY_RATING)) {
                setRating(obj.getInt(KEY_RATING));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SHORT_DESCRIPTION)) {
                setShortDescription(obj.getString(KEY_SHORT_DESCRIPTION));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DESCRIPTION)) {
                setDescription(obj.getString(KEY_DESCRIPTION));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_STATUS_ACTIVE)) {
                setStatusActive(obj.getInt(KEY_STATUS_ACTIVE));
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
            if (!TextUtils.isEmpty(getFeaturedImage())) {
                obj.put(KEY_FEATURED_IMAGE, getFeaturedImage());
            } else {
                obj.put(KEY_FEATURED_IMAGE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getHighlight())) {
                obj.put(KEY_HIGHLIGHT, getHighlight());
            } else {
                obj.put(KEY_HIGHLIGHT, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getGreatFor())) {
                obj.put(KEY_GREAT_FOR, getGreatFor());
            } else {
                obj.put(KEY_GREAT_FOR, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DEPTH_MIN, getDepthMin());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DEPTH_MAX, getDepthMax());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_VISIBILITY_MIN, getVisiblityMin());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_VISIBILITY_MAX, getVisibilityMax());
        } catch (JSONException e) {e.printStackTrace();}


        try {
            if (!TextUtils.isEmpty(getCurrentStatus())) {
                obj.put(KEY_CURRENT_STATUS, getCurrentStatus());
            } else {
                obj.put(KEY_CURRENT_STATUS, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getSurfaceCondition())) {
                obj.put(KEY_SURFACE_CONDITION, getSurfaceCondition());
            } else {
                obj.put(KEY_SURFACE_CONDITION, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_WATER_TEMPERATURE_MIN, getTemperatureMin());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_WATER_TEMPERATURE_MAX, getTemperatureMax());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getExperienceMin())) {
                obj.put(KEY_EXPERIENCE_MAX, getExperienceMax());
            } else {
                obj.put(KEY_EXPERIENCE_MAX, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_RECOMENDED_STAY_MIN, getRecommendedStayMin());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_RECOMENDED_STAY_MAX, getRecommendedStayMax());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_RATING, getRating());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getShortDescription())) {
                obj.put(KEY_SHORT_DESCRIPTION, getShortDescription());
            } else {
                obj.put(KEY_SHORT_DESCRIPTION, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}


        try{
            if(getLocation()!=null){
                JSONObject objLoc = new JSONObject(getLocation().toString());
                obj.put(KEY_LOCATION, objLoc);
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
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}