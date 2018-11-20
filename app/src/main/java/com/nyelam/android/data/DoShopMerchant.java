package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class DoShopMerchant implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_PROVINCE_ID = "province_id";
    private static String KEY_CITY_ID = "city_id";
    private static String KEY_DISTRICT_ID = "district_id";
    private static String KEY_POSTAL_CODE = "postal_code";
    private static String KEY_ADDRESS = "address";
    private static String KEY_TOTAL_WEIGHT = "total_weight";
    private static String KEY_PRODUCTS = "products";
    private static String KEY_DELIVERY_SERVICE = "delivery_service";

    private String id;
    private String name;
    private String provinceId;
    private String cityId;
    private String districtId;
    private String postalCode;
    private String address;
    private double totalWeight;
    private List<DoShopProduct> doShopProducts;
    private DeliveryService deliveryService;

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

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<DoShopProduct> getDoShopProducts() {
        return doShopProducts;
    }

    public void setDoShopProducts(List<DoShopProduct> doShopProducts) {
        this.doShopProducts = doShopProducts;
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
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
            if (!obj.isNull(KEY_PROVINCE_ID)) {
                setProvinceId(obj.getString(KEY_PROVINCE_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CITY_ID)) {
                setCityId(obj.getString(KEY_CITY_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DISTRICT_ID)) {
                setDistrictId(obj.getString(KEY_DISTRICT_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_POSTAL_CODE)) {
                setPostalCode(obj.getString(KEY_POSTAL_CODE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ADDRESS)) {
                setAddress(obj.getString(KEY_ADDRESS));
            }
        } catch (JSONException e) {e.printStackTrace();}


        try {
            if (!obj.isNull(KEY_TOTAL_WEIGHT)) {
                setTotalWeight(obj.getDouble(KEY_TOTAL_WEIGHT));
            }
        } catch (JSONException e) {e.printStackTrace();}


        if (!obj.isNull(KEY_PRODUCTS)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_PRODUCTS);
                if (array != null && array.length() > 0) {
                    doShopProducts = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        DoShopProduct a = new DoShopProduct();
                        a.parse(o);
                        doShopProducts.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }


        if(!obj.isNull(KEY_DELIVERY_SERVICE)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_DELIVERY_SERVICE);
                if(o != null && o.length() > 0) {
                    deliveryService = new DeliveryService();
                    deliveryService.parse(o);
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
            if (!TextUtils.isEmpty(getProvinceId())) {
                obj.put(KEY_PROVINCE_ID, getProvinceId());
            } else {
                obj.put(KEY_PROVINCE_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getCityId())) {
                obj.put(KEY_CITY_ID, getCityId());
            } else {
                obj.put(KEY_CITY_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getDistrictId())) {
                obj.put(KEY_DISTRICT_ID, getDistrictId());
            } else {
                obj.put(KEY_DISTRICT_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getPostalCode())) {
                obj.put(KEY_POSTAL_CODE, getPostalCode());
            } else {
                obj.put(KEY_POSTAL_CODE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getAddress())) {
                obj.put(KEY_ADDRESS, getAddress());
            } else {
                obj.put(KEY_ADDRESS, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_TOTAL_WEIGHT, getTotalWeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(doShopProducts != null && !doShopProducts.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(DoShopProduct a : doShopProducts) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_PRODUCTS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try{
            if(getDeliveryService()!=null){
                JSONObject objVer = new JSONObject(getDeliveryService().toString());
                obj.put(KEY_DELIVERY_SERVICE, objVer);
            } else {
                obj.put(KEY_DELIVERY_SERVICE, JSONObject.NULL);
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
