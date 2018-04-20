package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/28/2018.
 */

public class OrderReturn {

    private static String KEY_SUMMARY = "summary";
    private static String KEY_VERITRANS_TOKEN = "veritrans_token";
    private static String KEY_PAYPAL_CURRENCY = "paypal_currency";

    private Summary summary;
    private VeritransToken veritransToken;
    private PaypalCurrency paypalCurrency;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

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

    public void parse(JSONObject obj){

        if (obj == null) return;

        if(!obj.isNull(KEY_SUMMARY)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_SUMMARY);
                if(o != null) {
                    summary = new Summary();
                    summary.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_VERITRANS_TOKEN)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_VERITRANS_TOKEN);
                if(o != null) {
                    veritransToken = new VeritransToken();
                    veritransToken.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_PAYPAL_CURRENCY)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_PAYPAL_CURRENCY);
                if(o != null) {
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
            if(getSummary()!=null){
                JSONObject objSummary = new JSONObject(getSummary().toString());
                obj.put(KEY_SUMMARY, objSummary);
            } else {
                obj.put(KEY_SUMMARY, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getVeritransToken()!=null){
                JSONObject objVeritrans = new JSONObject(getVeritransToken().toString());
                obj.put(KEY_VERITRANS_TOKEN, objVeritrans);
            } else {
                obj.put(KEY_VERITRANS_TOKEN, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getPaypalCurrency() != null){
                JSONObject objPaypal = new JSONObject(getPaypalCurrency().toString());
                obj.put(KEY_PAYPAL_CURRENCY, objPaypal);
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
