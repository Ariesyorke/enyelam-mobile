package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopProduct;

import org.json.JSONObject;

public class NYDoShopProductDetailRequest extends NYBasicRequest<DoShopProduct> {

    private final static String KEY_PRODUCT = "product";
    private final static String POST_PRODUCT_ID = "product_id";

    public NYDoShopProductDetailRequest(Context context, String productId){
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_product_detail));

        if(!TextUtils.isEmpty(productId)) {
            addQuery(POST_PRODUCT_ID, productId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopProduct onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_PRODUCT) && obj.get(KEY_PRODUCT) instanceof JSONObject && obj.getJSONObject(KEY_PRODUCT) != null){
            DoShopProduct product = new DoShopProduct();
            product.parse(obj.getJSONObject(KEY_PRODUCT));
            return product;
        } else {
          return null;
        }

    }

}