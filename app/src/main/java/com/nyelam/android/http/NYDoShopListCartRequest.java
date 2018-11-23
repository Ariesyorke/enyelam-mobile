package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopList;

import org.json.JSONObject;

public class NYDoShopListCartRequest extends NYBasicAuthRequest<DoShopCartReturn> {

    public NYDoShopListCartRequest(Context context) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_cart_list));

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopCartReturn onProcessSuccessData(JSONObject obj) throws Exception {

        DoShopCartReturn doShopCart = new DoShopCartReturn();
        doShopCart.parse(obj);
        return doShopCart;
    }

}