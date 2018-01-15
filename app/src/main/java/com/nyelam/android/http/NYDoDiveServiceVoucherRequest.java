package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.SearchResultList;
import com.nyelam.android.data.Voucher;

import org.json.JSONObject;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveServiceVoucherRequest extends NYBasicRequest<Cart> {

    private static final String KEY_CART = "cart";

    private static final String POST_CART_TOKEN = "cart_token";
    private static final String POST_VOUCHER_CODE = "voucher_code";

    public NYDoDiveServiceVoucherRequest(Context context, String cartToken, String voucherCode) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_master_dodive_search));

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
    protected Cart onProcessSuccessData(JSONObject obj) throws Exception {

        Cart cart = new Cart();
        cart.parse(obj.getJSONObject(KEY_CART));
        return cart;
    }

}

