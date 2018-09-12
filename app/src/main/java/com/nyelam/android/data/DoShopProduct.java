package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoShopProduct implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_PRODUCT_NAME = "product_name";
    private static String KEY_FEATURED_IMAGE = "featured_image";
    private static String KEY_IMAGES = "images";
    private static String KEY_SPECIAL_PRICE = "special_price";
    private static String KEY_NORMAL_PRICE = "normal_price";
    private static String KEY_STATUS = "status";
    private static String KEY_CATEGORIES = "categories";
    private static String KEY_DESCRIPTION = "description";

    private String id;
    private String productName;
    private String featuredImage;
    private List<String> images;
    private String specialPrice;
    private String normalPrice;
    private String status;
    private List<DoShopCategory> categories;
    private String description;

    public DoShopProduct(){

    }

    public DoShopProduct(String id, String productName, String featuredImage, List<String> images, String specialPrice
            , String normalPrice, String status, List<DoShopCategory> categories, String description){
        this.id = id;
        this.productName = productName;
        this.featuredImage = featuredImage;
        this.images = images;
        this.specialPrice = specialPrice;
        this.normalPrice = normalPrice;
        this.status = status;
        this.categories = categories;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(String normalPrice) {
        this.normalPrice = normalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DoShopCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DoShopCategory> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
            if (!obj.isNull(KEY_PRODUCT_NAME)) {
                setProductName(obj.getString(KEY_PRODUCT_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_FEATURED_IMAGE)) {
                setFeaturedImage(obj.getString(KEY_FEATURED_IMAGE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_IMAGES)) {
                JSONArray array = obj.getJSONArray(KEY_IMAGES);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(images == null) {
                            images = new ArrayList<>();
                        }
                        String uriImages = array.getString(i);
                        images.add(uriImages);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SPECIAL_PRICE)) {
                setFeaturedImage(obj.getString(KEY_SPECIAL_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NORMAL_PRICE)) {
                setFeaturedImage(obj.getString(KEY_NORMAL_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_STATUS)) {
                setFeaturedImage(obj.getString(KEY_STATUS));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CATEGORIES)) {
                JSONArray array = obj.getJSONArray(KEY_CATEGORIES);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(categories == null) {
                            categories = new ArrayList<>();
                        }
                        JSONObject o = array.getJSONObject(i);
                        DoShopCategory category = new DoShopCategory();
                        category.parse(o);
                        categories.add(category);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DESCRIPTION)) {
                setFeaturedImage(obj.getString(KEY_DESCRIPTION));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }
}