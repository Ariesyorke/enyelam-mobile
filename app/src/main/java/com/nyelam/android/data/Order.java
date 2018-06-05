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
    private static String KEY_ADDITIONAL = "additional";
    private static String KEY_EQUIPMENT_RENTS = "equipment_rents";

    private String orderId;
    private String status;
    private long schedule;
    private Cart cart;
    private List<Additional> additionals;
    private List<EquipmentRent> equipmentRents;

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

    public List<Additional> getAdditionals() {
        return additionals;
    }

    public void setAdditionals(List<Additional> additionals) {
        this.additionals = additionals;
    }

    public List<EquipmentRent> getEquipmentRents() {
        return equipmentRents;
    }

    public void setEquipmentRents(List<EquipmentRent> equipmentRents) {
        this.equipmentRents = equipmentRents;
    }

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

        if (!obj.isNull(KEY_ADDITIONAL)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_ADDITIONAL);
                if (array != null && array.length() > 0) {
                    additionals = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Additional a = new Additional();
                        a.parse(o);
                        additionals.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }


        if (!obj.isNull(KEY_EQUIPMENT_RENTS)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_EQUIPMENT_RENTS);
                if (array != null && array.length() > 0) {
                    equipmentRents = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        EquipmentRent a = new EquipmentRent();
                        a.parse(o);
                        equipmentRents.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }

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
                JSONObject objCart = new JSONObject(getCart().toString());
                obj.put(KEY_CART, objCart);
            } else {
                obj.put(KEY_CART, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(additionals != null && !additionals.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(Additional a : additionals) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_ADDITIONAL, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(equipmentRents != null && !equipmentRents.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(EquipmentRent a : equipmentRents) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_EQUIPMENT_RENTS, array);
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
