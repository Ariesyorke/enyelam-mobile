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

public class DoShopCart implements Parseable {

    private static String KEY_CART_TOKEN = "cart_token";
    private static String KEY_PRODUCTS = "products";
    private static String KEY_MERCHANTS = "merchants";
    private static String KEY_SUB_TOTAL = "sub_total";
    private static String KEY_ADDITIONALS = "additionals";
    private static String KEY_VOUCHER = "voucher";
    private static String KEY_TOTAL = "total";

    private String cartToken;
    private List<DoShopProduct> doShopProducts;
    private List<Additional> additionals;
    private List<DoShopMerchant> merchants;
    private Voucher voucher;
    private double subTotal;
    private double total;


    public String getCartToken() {
        return cartToken;
    }

    public void setCartToken(String cartToken) {
        this.cartToken = cartToken;
    }

    public List<DoShopProduct> getDoShopProducts() {
        return doShopProducts;
    }

    public void setDoShopProducts(List<DoShopProduct> doShopProducts) {
        this.doShopProducts = doShopProducts;
    }

    public List<Additional> getAdditionals() {
        return additionals;
    }

    public void setAdditionals(List<Additional> additionals) {
        this.additionals = additionals;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DoShopMerchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<DoShopMerchant> merchants) {
        this.merchants = merchants;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_CART_TOKEN)) {
                setCartToken(obj.getString(KEY_CART_TOKEN));
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

        if (!obj.isNull(KEY_MERCHANTS)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_MERCHANTS);
                if (array != null && array.length() > 0) {
                    merchants = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        DoShopMerchant a = new DoShopMerchant();
                        a.parse(o);
                        merchants.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }

        if(!obj.isNull(KEY_VOUCHER)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_VOUCHER);
                if(o != null && o.length() > 0) {
                    Voucher voucher = new Voucher();
                    voucher.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!obj.isNull(KEY_SUB_TOTAL)) {
                setSubTotal(obj.getDouble(KEY_SUB_TOTAL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TOTAL)) {
                setTotal(obj.getDouble(KEY_TOTAL));
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

        if(merchants != null && !merchants.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(DoShopMerchant a : merchants) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_MERCHANTS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try{
            if(getVoucher()!=null){
                JSONObject objVoucher = new JSONObject(getVoucher().toString());
                obj.put(KEY_VOUCHER, objVoucher);
            } else {
                obj.put(KEY_VOUCHER, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            obj.put(KEY_SUB_TOTAL, getSubTotal());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj. put(KEY_TOTAL, getTotal());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}
