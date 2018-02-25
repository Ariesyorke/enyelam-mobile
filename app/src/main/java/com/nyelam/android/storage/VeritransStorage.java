package com.nyelam.android.storage;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.Contact;
import com.nyelam.android.dev.NYLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dantech on 11/7/16.
 */
public class VeritransStorage extends AbstractStorage {
    private static final String FILENAME = "nyelam:storage:veritrans-token";
    private static final String KEY_VERITRANS_TOKEN = "veritrans_token";
    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_CART = "cart";

    public String orderId;
    public String veritransToken;
    public Contact contact;
    public Cart cart;

    public VeritransStorage(Context context) {
        super(context);
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {
        NYLog.d("on load login storage = " + obj.toString());

        if (!obj.isNull(KEY_VERITRANS_TOKEN)) {
            veritransToken = obj.getString(KEY_VERITRANS_TOKEN);
        }
        if (!obj.isNull(KEY_ORDER_ID)) {
            orderId = obj.getString(KEY_ORDER_ID);
        }
        if(!obj.isNull(KEY_CONTACT)) {
            JSONObject o = obj.getJSONObject(KEY_CONTACT);
            contact = new Contact();
            contact.parse(o);
        }
        if(!obj.isNull(KEY_CART)) {
            JSONObject o = obj.getJSONObject(KEY_CART);
            cart = new Cart();
            cart.parse(o);
        }
    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();
        if (!TextUtils.isEmpty(veritransToken)) {
            obj.put(KEY_VERITRANS_TOKEN, veritransToken);
        }
        if (!TextUtils.isEmpty(orderId)) {
            obj.put(KEY_ORDER_ID, orderId);
        }
        if(contact != null) {
            obj.put(KEY_CONTACT, new JSONObject(contact.toString()));
        }
        if(cart != null) {
            obj.put(KEY_CART, new JSONObject(cart.toString()));
        }
        return obj;
    }
}
