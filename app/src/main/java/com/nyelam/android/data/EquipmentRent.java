package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 5/21/2018.
 */

public class EquipmentRent implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_NORMAL_PRICE = "normal_price";
    private static String KEY_SPECIAL_PRICE = "special_price";
    private static String KEY_AVAILABILITY_STOCK = "availability_stock";

    private String id;
    private String name;
    private long normalPrice;
    private long specialPrice;
    private int availabilityStock;


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

    public long getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(long normalPrice) {
        this.normalPrice = normalPrice;
    }

    public long getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(long specialPrice) {
        this.specialPrice = specialPrice;
    }

    public int getAvailabilityStock() {
        return availabilityStock;
    }

    public void setAvailabilityStock(int availabilityStock) {
        this.availabilityStock = availabilityStock;
    }


    public void parse(JSONObject obj){

        if (obj == null) return;

        if (!obj.isNull(KEY_ID)){
            try {
                setId(obj.getString(KEY_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!obj.isNull(KEY_NAME)){
            try {
                setName(obj.getString(KEY_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!obj.isNull(KEY_NORMAL_PRICE)){
            try {
                setNormalPrice(obj.getLong(KEY_NORMAL_PRICE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!obj.isNull(KEY_SPECIAL_PRICE)){
            try {
                setSpecialPrice(obj.getLong(KEY_SPECIAL_PRICE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!obj.isNull(KEY_AVAILABILITY_STOCK)){
            try {
                setAvailabilityStock(obj.getInt(KEY_AVAILABILITY_STOCK));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        if (NYHelper.isStringNotEmpty(getId())){
            try {
                obj.put(KEY_ID, getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (NYHelper.isStringNotEmpty(getName())){
            try {
                obj.put(KEY_NAME, getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        try {
            obj.put(KEY_AVAILABILITY_STOCK, getAvailabilityStock());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
