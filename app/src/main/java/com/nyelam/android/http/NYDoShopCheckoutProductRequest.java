package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCheckout;
import com.nyelam.android.data.DoShopList;

import org.json.JSONObject;

public class NYDoShopCheckoutProductRequest extends NYBasicAuthRequest<DoShopCheckout> {

    private final static String POST_PRODUCT_CART_ID = "product_cart_id";

    public NYDoShopCheckoutProductRequest(Context context, String productCartId) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_checkout_product));

        if(!TextUtils.isEmpty(productCartId)) {
            addQuery(POST_PRODUCT_CART_ID, productCartId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopCheckout onProcessSuccessData(JSONObject obj) throws Exception {

        DoShopCheckout doShopCheckout = new DoShopCheckout();
        doShopCheckout.parse(obj);
        return doShopCheckout;

    }

}