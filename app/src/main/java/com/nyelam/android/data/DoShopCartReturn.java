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

public class DoShopCartReturn implements Parseable {

    private static String KEY_CART_TOKEN = "cart_token";
    private static String KEY_CART = "cart";
    private static String KEY_ADDITIONALS = "additionals";

    private String cartToken;
    private DoShopCart cart;
    private List<Additional> additionals;

    public String getCartToken() {
        return cartToken;
    }

    public void setCartToken(String cartToken) {
        this.cartToken = cartToken;
    }

    public DoShopCart getCart() {
        return cart;
    }

    public void setCart(DoShopCart cart) {
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

        if(!obj.isNull(KEY_CART)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_CART);
                if(o != null && o.length() > 0) {
                    cart = new DoShopCart();
                    cart.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!obj.isNull(KEY_ADDITIONALS)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_ADDITIONALS);
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

        try{
            if(getCart()!=null){
                JSONObject objPay = new JSONObject(getCart().toString());
                obj.put(KEY_CART, objPay);
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
                obj.put(KEY_ADDITIONALS, array);
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
