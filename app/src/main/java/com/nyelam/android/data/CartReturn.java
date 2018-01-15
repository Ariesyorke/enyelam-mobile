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

public class CartReturn implements Parseable {

    private static String KEY_CART_TOKEN = "cart_token";
    private static String KEY_EXPIRY = "expiry";
    private static String KEY_CART = "cart";
    private static String KEY_ADDITIONAL = "additional";

    private String cartToken;
    private long expiry;
    private Cart cart;
    private List<Additional> additionals;

    public String getCartToken() {
        return cartToken;
    }

    public void setCartToken(String cartToken) {
        this.cartToken = cartToken;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
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

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_CART_TOKEN)) {
                setCartToken(obj.getString(KEY_CART_TOKEN));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_EXPIRY)) {
                setExpiry(obj.getLong(KEY_EXPIRY));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CART)) {
                Cart cart = new Cart();
                cart.parse(obj.getJSONObject(KEY_CART));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ADDITIONAL)) {

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
            }
        } catch (JSONException e) {e.printStackTrace();}


    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getCartToken())) {
                obj.put(KEY_CART_TOKEN, getCartToken());
            } else {
                obj.put(KEY_CART_TOKEN, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_EXPIRY, getExpiry());
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

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}
