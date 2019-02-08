package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Filter implements Parseable {
    private static final String KEY_BRAND = "brands";
    private static final String KEY_PRICE = "price";

    private BrandList brandList;
    private Price price;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public BrandList getBrandList() {
        return brandList;
    }

    public void setBrandList(BrandList brandList) {
        this.brandList = brandList;
    }

    @Override
    public void parse(JSONObject obj) {
        try {
            if (!obj.isNull(KEY_PRICE)) {
                price = new Price();
                price.parse(obj.getJSONObject(KEY_PRICE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(!obj.isNull(KEY_BRAND)) {
                JSONArray array = obj.getJSONArray(KEY_BRAND);
                brandList = new BrandList();
                brandList.parse(array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            if (price != null) {
                obj.put(KEY_PRICE, new JSONObject(price.toString()));
            }
            if(brandList != null && brandList.getList() != null && !brandList.getList().isEmpty()) {
                obj.put(KEY_BRAND, brandList.toJSONArray());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return obj.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
