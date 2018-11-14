package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopCheckout;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

public class NYDoShopAddVoucherRequest extends NYBasicAuthRequest<DoShopCartReturn> {

    private final static String KEY_PRODUCT_CART_ID = "product_cart_id";

    private final static String POST_CART_TOKEN = "cart_token";
    private final static String POST_VOUCHER_CODE = "voucher_code";
    private final static String POST_PLATFORM = "platform";

    public NYDoShopAddVoucherRequest(Context context, String cartToken, String voucherCode) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_add_voucher));

        if(!TextUtils.isEmpty(cartToken)) {
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if(!TextUtils.isEmpty(voucherCode)) {
            addQuery(POST_VOUCHER_CODE, voucherCode);
        }

        addQuery(POST_PLATFORM, "0");

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopCartReturn onProcessSuccessData(JSONObject obj) throws Exception {

        DoShopCartReturn cartReturn = new DoShopCartReturn();
        cartReturn.parse(obj);
        return cartReturn;

    }

}