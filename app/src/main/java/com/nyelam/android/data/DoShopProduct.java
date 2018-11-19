package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoShopProduct implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_PRODUCT_CARD_ID = "product_cart_id";
    private static String KEY_PRODUCT_NAME = "product_name";
    private static String KEY_FEATURED_IMAGE = "featured_image";
    private static String KEY_IMAGES = "images";
    private static String KEY_SPECIAL_PRICE = "special_price";
    private static String KEY_NORMAL_PRICE = "normal_price";
    private static String KEY_WEIGHT = "weight";
    private static String KEY_STATUS = "status";
    private static String KEY_CATEGORIES = "categories";
    private static String KEY_MERCHANT = "merchant";
    private static String KEY_VARIATIONS = "variations";
    private static String KEY_DESCRIPTION = "product_description";
    private static String KEY_QTY = "qty";

    private String id;
    private String productName;
    private String featuredImage;
    private List<String> images;
    private String weight;
    private double specialPrice;
    private double normalPrice;
    private String status;
    private DoShopMerchant merchant;
    private Variations variations;
    private List<DoShopCategory> categories;
    private String description;
    private int qty;

    public DoShopProduct(){

    }

    public DoShopProduct(String id, String productName, String featuredImage, List<String> images, double specialPrice
            , double normalPrice, String status, List<DoShopCategory> categories, String description){
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

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public double getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(double normalPrice) {
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Variations getVariations() {
        return variations;
    }

    public void setVariations(Variations variations) {
        this.variations = variations;
    }

    public DoShopMerchant getMerchant() {
        return merchant;
    }

    public void setMerchant(DoShopMerchant merchant) {
        this.merchant = merchant;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ID)) {
                setId(obj.getString(KEY_ID));
            } else if (!obj.isNull(KEY_PRODUCT_CARD_ID)) {
                setId(obj.getString(KEY_PRODUCT_CARD_ID));
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
                setSpecialPrice(obj.getDouble(KEY_SPECIAL_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NORMAL_PRICE)) {
                setNormalPrice(obj.getDouble(KEY_NORMAL_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_STATUS)) {
                setStatus(obj.getString(KEY_STATUS));
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

        try {
            if (!obj.isNull(KEY_WEIGHT)) {
                setWeight(obj.getString(KEY_WEIGHT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if(!obj.isNull(KEY_VARIATIONS)) {
                JSONObject o = obj.getJSONObject(KEY_VARIATIONS);
                if(o != null && o.length() > 0) {
                    variations = new Variations();
                    variations.parse(o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!obj.isNull(KEY_MERCHANT)) {
                JSONObject o = obj.getJSONObject(KEY_MERCHANT);
                if(o != null && o.length() > 0) {
                    merchant = new DoShopMerchant();
                    merchant.parse(o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (!obj.isNull(KEY_QTY)) {
                setQty(obj.getInt(KEY_QTY));
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
            if (!TextUtils.isEmpty(getProductName())) {
                obj.put(KEY_PRODUCT_NAME, getProductName());
            } else {
                obj.put(KEY_PRODUCT_NAME, JSONObject.NULL);
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
            obj.put(KEY_SPECIAL_PRICE, getSpecialPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_NORMAL_PRICE, getNormalPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (!TextUtils.isEmpty(getStatus())) {
                obj.put(KEY_STATUS, getStatus());
            } else {
                obj.put(KEY_STATUS, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getDescription())) {
                obj.put(KEY_DESCRIPTION, getDescription());
            } else {
                obj.put(KEY_DESCRIPTION, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}



        if(images != null && !images.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(String a : images) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_IMAGES, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(categories != null && !categories.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(DoShopCategory a : categories) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_CATEGORIES, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!TextUtils.isEmpty(getWeight())) {
                obj.put(KEY_WEIGHT, getWeight());
            } else {
                obj.put(KEY_WEIGHT, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getVariations()!=null){
                JSONObject objVar= new JSONObject(getVariations().toString());
                obj.put(KEY_VARIATIONS, objVar);
            } else {
                obj.put(KEY_VARIATIONS, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getMerchant()!=null){
                JSONObject objMer= new JSONObject(getMerchant().toString());
                obj.put(KEY_MERCHANT, objMer);
            } else {
                obj.put(KEY_MERCHANT, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            obj.put(KEY_QTY, getQty());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }
}