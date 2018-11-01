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

public class DoShopCheckout implements Parseable {

    private static String KEY_CHECKOUT_TOKEN = "checkout_token";
    private static String KEY_CART = "cart";
    private static String KEY_ADDRESS = "address";

    private String checkoutToken;
    private Cart cart;
    private DoShopAddress address;

    public String getCheckoutToken() {
        return checkoutToken;
    }

    public void setCheckoutToken(String checkoutToken) {
        this.checkoutToken = checkoutToken;
    }

    public DoShopAddress getAddress() {
        return address;
    }

    public void setAddress(DoShopAddress address) {
        this.address = address;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_CHECKOUT_TOKEN)) {
                setCheckoutToken(obj.getString(KEY_CHECKOUT_TOKEN));
            }
        } catch (JSONException e) {e.printStackTrace();}


        if(!obj.isNull(KEY_ADDRESS)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_ADDRESS);
                if(o != null && o.length() > 0) {
                    address = new DoShopAddress();
                    address.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_CART)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_CART);
                if(o != null && o.length() > 0) {
                    cart = new Cart();
                    cart.parse(o);
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
            if (!TextUtils.isEmpty(getCheckoutToken())) {
                obj.put(KEY_CHECKOUT_TOKEN, getCheckoutToken());
            } else {
                obj.put(KEY_CHECKOUT_TOKEN, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getAddress()!=null){
                JSONObject objAddress = new JSONObject(getAddress().toString());
                obj.put(KEY_ADDRESS, objAddress);
            } else {
                obj.put(KEY_ADDRESS, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

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


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}
