package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class Cart implements Parseable {

    private static String KEY_SUB_TOTAL = "sub_total";
    private static String KEY_VOUCHER = "vouchers";
    private static String KEY_TOTAL = "total";
    private static String KEY_CURRENCY = "currency";

    private double subTotal;
    private Voucher voucher;
    private double total;
    private String currency;

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

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

        try {
            if (!obj.isNull(KEY_CURRENCY)) {
                setCurrency(obj.getString(KEY_CURRENCY));
            }
        } catch (JSONException e) {e.printStackTrace();}


        if(!obj.isNull(KEY_VOUCHER)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_VOUCHER);
                if(o != null && o.length() > 0) {
                    voucher = new Voucher();
                    voucher.parse(o);
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
            obj.put(KEY_SUB_TOTAL, subTotal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_TOTAL, total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (!TextUtils.isEmpty(getCurrency())) {
                obj.put(KEY_CURRENCY, getCurrency());
            } else {
                obj.put(KEY_CURRENCY, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getVoucher()!=null){
                obj.put(KEY_VOUCHER, getVoucher());
            } else {
                obj.put(KEY_VOUCHER, JSONObject.NULL);
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
