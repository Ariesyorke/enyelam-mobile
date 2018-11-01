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

public class DoShopCartReturn extends DoShopCart implements Parseable {

    private static String KEY_VERITRANS_TOKEN = "veritrans_token";
    private static String KEY_PAYPAL_CURRENCY = "paypal_currency";

    private VeritransToken veritransToken;
    private PaypalCurrency paypalCurrency;


    public VeritransToken getVeritransToken() {
        return veritransToken;
    }

    public void setVeritransToken(VeritransToken veritransToken) {
        this.veritransToken = veritransToken;
    }

    public PaypalCurrency getPaypalCurrency() {
        return paypalCurrency;
    }

    public void setPaypalCurrency(PaypalCurrency paypalCurrency) {
        this.paypalCurrency = paypalCurrency;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        if(!obj.isNull(KEY_VERITRANS_TOKEN)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_VERITRANS_TOKEN);
                if(o != null && o.length() > 0) {
                    veritransToken = new VeritransToken();
                    veritransToken.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_PAYPAL_CURRENCY)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_VERITRANS_TOKEN);
                if(o != null && o.length() > 0) {
                    paypalCurrency = new PaypalCurrency();
                    paypalCurrency.parse(o);
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
            if(getVeritransToken()!=null){
                JSONObject objVer = new JSONObject(getVeritransToken().toString());
                obj.put(KEY_VERITRANS_TOKEN, objVer);
            } else {
                obj.put(KEY_VERITRANS_TOKEN, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getPaypalCurrency()!=null){
                JSONObject objPay = new JSONObject(getPaypalCurrency().toString());
                obj.put(KEY_PAYPAL_CURRENCY, objPay);
            } else {
                obj.put(KEY_PAYPAL_CURRENCY, JSONObject.NULL);
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
