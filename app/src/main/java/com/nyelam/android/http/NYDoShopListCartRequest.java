package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCart;
import com.nyelam.android.data.DoShopCheckout;
import com.nyelam.android.data.DoShopList;

import org.json.JSONObject;

public class NYDoShopListCartRequest extends NYBasicAuthRequest<DoShopCart> {

    private final static String KEY_PRODUCT_CART_ID = "product_cart_id";

    private final static String POST_CART_TOKEN = "cart_token";
    private final static String POST_VOUCHER_CODE = "voucher_code";

    public NYDoShopListCartRequest(Context context, String cartToken, String voucherCode) throws Exception {
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
    protected DoShopCart onProcessSuccessData(JSONObject obj) throws Exception {

        DoShopCart doShopCart = new DoShopCart();
        doShopCart.parse(obj);
        return doShopCart;

    }

}