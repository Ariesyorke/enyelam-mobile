package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/19/2018.
 */

public class Summary implements Parseable {

    private static String KEY_ORDER = "order";
    private static String KEY_SERVICE = "service";

    private Order order;
    private DiveService diveService;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DiveService getDiveService() {
        return diveService;
    }

    public void setDiveService(DiveService diveService) {
        this.diveService = diveService;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        if(!obj.isNull(KEY_ORDER)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_ORDER);
                if(o != null) {
                    order = new Order();
                    order.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_SERVICE)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_SERVICE);
                if(o != null && o.length() > 0) {
                    diveService = new DiveService();
                    diveService.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try{
            if(getOrder()!=null){
                obj.put(KEY_ORDER, getOrder());
            } else {
                obj.put(KEY_ORDER, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getDiveService()!=null){
                obj.put(KEY_SERVICE, getDiveService());
            } else {
                obj.put(KEY_SERVICE, JSONObject.NULL);
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
