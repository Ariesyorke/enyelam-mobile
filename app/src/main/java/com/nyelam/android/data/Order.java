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

public class Order implements Parseable {

    private static String KEY_ORDER_ID = "order_id";
    private static String KEY_STATUS = "status";
    private static String KEY_SCHEDULE = "schedule";
    private static String KEY_CART = "cart";
    private static String KEY_TOTAL = "total";
    private static String KEY_CURRENCY = "currency";
    private static String KEY_DIVE_SPOT = "dive_spot";

    private String orderId;
    private String status;
    private long schedule;
    private Cart cart;
    //private long total;
    //private String currency;
    //private DiveSpot diveSpot;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSchedule() {
        return schedule;
    }

    public void setSchedule(long schedule) {
        this.schedule = schedule;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /*public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DiveSpot getDiveSpot() {
        return diveSpot;
    }

    public void setDiveSpot(DiveSpot diveSpot) {
        this.diveSpot = diveSpot;
    }*/

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ORDER_ID)) {
                setOrderId(obj.getString(KEY_ORDER_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}


        try {
            if (!obj.isNull(KEY_STATUS)) {
                setStatus(obj.getString(KEY_STATUS));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SCHEDULE)) {
                setSchedule(obj.getLong(KEY_SCHEDULE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        if(!obj.isNull(KEY_CART)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_CART);
                if(o != null) {
                    cart = new Cart();
                    cart.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*try {
            if (!obj.isNull(KEY_CART)) {
                cart.parse(obj.getJSONObject(KEY_CART));
            }
        } catch (JSONException e) {e.printStackTrace();}*/

        /*try {
            if (!obj.isNull(KEY_TOTAL)) {
                setTotal(obj.getLong(KEY_TOTAL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CURRENCY)) {
                setCurrency(obj.getString(KEY_CURRENCY));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DIVE_SPOT)) {
                diveSpot.parse(obj.getJSONObject(KEY_DIVE_SPOT));
            }
        } catch (JSONException e) {e.printStackTrace();}*/

    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getOrderId())) {
                obj.put(KEY_ORDER_ID, getOrderId());
            } else {
                obj.put(KEY_ORDER_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getStatus())) {
                obj.put(KEY_STATUS, getStatus());
            } else {
                obj.put(KEY_STATUS, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_SCHEDULE, getSchedule());
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getCart()!=null){
                obj.put(KEY_CART, getCart());
            } else {
                obj.put(KEY_CART, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        /*try {
            obj.put(KEY_TOTAL, getTotal());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getCurrency())) {
                obj.put(KEY_CURRENCY, getCurrency());
            } else {
                obj.put(KEY_CURRENCY, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getDiveSpot()!=null){
                obj.put(KEY_DIVE_SPOT, getDiveSpot());
            } else {
                obj.put(KEY_DIVE_SPOT, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }*/

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}
