package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCheckout;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

public class NYDoShopAddVoucherRequest extends NYBasicAuthRequest<DoShopCheckout> {

    private final static String KEY_PRODUCT_CART_ID = "product_cart_id";

    private final static String POST_CART_TOKEN = "cart_token";
    private final static String POST_VOUCHER_CODE = "voucher_code";

    public NYDoShopAddVoucherRequest(Context context, String cartToken, String voucherCode) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_add_voucher));

        if(!TextUtils.isEmpty(cartToken)) {
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if(!TextUtils.isEmpty(voucherCode)) {
            addQuery(POST_VOUCHER_CODE, voucherCode);
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