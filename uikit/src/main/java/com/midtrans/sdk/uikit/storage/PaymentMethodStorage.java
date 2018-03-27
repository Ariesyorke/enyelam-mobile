package com.midtrans.sdk.uikit.storage;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class PaymentMethodStorage extends AbstractStorage {

    private static final String FILENAME = "nyelam:storage:payment-method";
    private static final String KEY_PAYMENT_METHOD = "payment_method";

    public String paymentMethod;

    /*public boolean isUserLogin() {
        return user != null && !TextUtils.isEmpty(user.getUserId()) &&
                !TextUtils.isEmpty(nyelamToken);
    }*/

    public PaymentMethodStorage(Context context) {
        super(context);
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        if (!obj.isNull(KEY_PAYMENT_METHOD)) {
            paymentMethod = obj.getString(KEY_PAYMENT_METHOD);
        }

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();

        if (paymentMethod != null) {
            obj.put(KEY_PAYMENT_METHOD, paymentMethod);
        }

        return obj;
    }
}

