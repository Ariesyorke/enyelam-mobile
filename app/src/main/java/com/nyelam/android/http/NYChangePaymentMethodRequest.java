package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.OrderReturn;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYChangePaymentMethodRequest extends NYBasicAuthRequest<CartReturn> {

    private static final String POST_CART_TOKEN = "cart_token";
    private static final String POST_TYPE = "type";
    private static final String POST_VOUCHER_CODE = "voucher_code";

    public NYChangePaymentMethodRequest(Context context, String cartToken, String type, String voucherCode) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_change_payment_method));

        if(!TextUtils.isEmpty(cartToken)) {
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
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
    protected CartReturn onProcessSuccessData(JSONObject obj) throws Exception {
        CartReturn cartReturn = new CartReturn();
        cartReturn.parse(obj);
        return  cartReturn;
    }

}

