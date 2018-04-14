package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 4/14/2018.
 */

public class PaypalCurrency implements Parseable {

    private static String KEY_CURRENCY = "currency";
    private static String KEY_AMOUNT = "amount";

    private String currency;
    private Double amount;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_CURRENCY)) {
                setCurrency(obj.getString(KEY_CURRENCY));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_AMOUNT)) {
                setAmount(obj.getDouble(KEY_AMOUNT));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getCurrency())) {
                obj.put(KEY_CURRENCY, getCurrency());
            } else {
                obj.put(KEY_CURRENCY, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (getAmount() != null) {
                obj.put(KEY_AMOUNT, getAmount());
            } else {
                obj.put(KEY_AMOUNT, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
