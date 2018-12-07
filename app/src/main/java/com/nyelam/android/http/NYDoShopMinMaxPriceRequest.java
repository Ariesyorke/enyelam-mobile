package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.Price;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/24/2018.
 */

public class NYDoShopMinMaxPriceRequest extends NYBasicRequest<Price> {

    private static final String KEY_PRICE = "price";

    private static final String POST_CATEGORY_ID = "category_id[]";
    private static final String POST_KEYWORD = "keyword";
    private static final String POST_BRAND_ID = "brand_id";
    private static final String POST_MERCHANT_ID = "merchant_id";

    public NYDoShopMinMaxPriceRequest(Context context, List<String> categories, String keyword, String brandId, String merchantId) {
        super(String.class, context, context.getResources().getString(R.string.api_path_doshop_min_max_price));
        if (categories != null && categories.size() > 0){
            for (String s : categories){
                if(NYHelper.isStringNotEmpty(s)) {
                    addQuery(POST_CATEGORY_ID, s);
                }
            }
        }
        if(!TextUtils.isEmpty(keyword)) {
            addQuery(POST_KEYWORD, keyword);
        }
        if(!TextUtils.isEmpty(brandId)) {
            addQuery(POST_BRAND_ID, brandId);
        }
        if(!TextUtils.isEmpty(merchantId)) {
            addQuery(POST_MERCHANT_ID, merchantId);
        }
    }

    @Override
    protected Price onProcessSuccessData(JSONObject obj) throws Exception {
        Price price = new Price();
        price.parse(obj.getJSONObject(KEY_PRICE));
        return price;
    }
}
