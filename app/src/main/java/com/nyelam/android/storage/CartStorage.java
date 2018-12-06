package com.nyelam.android.storage;

import android.content.Context;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopMerchantList;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.ParticipantList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dantech on 8/11/2017.
 */

public class CartStorage extends AbstractStorage {

    private static final String FILENAME = "nyelam:storage:cart";
    private static final String KEY_CART = "cart";

    private DoShopMerchantList merchants;

    public CartStorage(Context context) {
        super(context);
    }

    public DoShopMerchantList getMerchants() {
        return merchants;
    }

    public void setMerchants(DoShopMerchantList merchants) {
        this.merchants = merchants;
    }

    public int getSize(){
        if (merchants != null && merchants.getList() != null){
            int total = 0;
            for (DoShopMerchant m : merchants.getList()){
                if (m!=null && m.getDoShopProducts() != null  && m.getDoShopProducts().size() > 0){
                    for (DoShopProduct p : m.getDoShopProducts()){
                        if (p!=null)total+=p.getQty();
                    }
                }
            }
            return total;
        } else {
            return 0;
        }
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        try {
            if (!obj.isNull(KEY_CART)) {
                JSONArray array = obj.getJSONArray(KEY_CART);
                merchants = new DoShopMerchantList();
                merchants.parse(array);
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();
        try {
            if(merchants != null && merchants.getList() != null && !merchants.getList().isEmpty()) {
                obj.put(KEY_CART, merchants.toJSONArray());
            }
        } catch (JSONException e){e.printStackTrace();}

        return obj;
    }
}