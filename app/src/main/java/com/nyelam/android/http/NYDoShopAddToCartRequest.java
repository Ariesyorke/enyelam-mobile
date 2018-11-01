package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.Variation;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.util.List;

public class NYDoShopAddToCartRequest extends NYBasicAuthRequest<String> {

    private final static String KEY_PRODUCT_CART_ID = "product_cart_id";

    private final static String POST_PRODUCT_ID = "product_id";
    private final static String POST_VARIATIONS = "variations";
    private final static String POST_QTY = "qty";

    public NYDoShopAddToCartRequest(Context context, String productId, List<Variation> variations, String qty) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_add_cart));

        if(!TextUtils.isEmpty(productId)) {
            addQuery(POST_PRODUCT_ID, productId);
        }

        if(variations != null && variations.size() > 0) {
            addQuery(POST_VARIATIONS, variations.toString());
        }

        if(!TextUtils.isEmpty(qty)) {
            addQuery(POST_QTY, qty);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected String onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_PRODUCT_CART_ID) && obj.get(KEY_PRODUCT_CART_ID) instanceof String && NYHelper.isStringNotEmpty(obj.getString(KEY_PRODUCT_CART_ID))){
            return obj.getString(KEY_PRODUCT_CART_ID);
        } else {
          return null;
        }

    }

}