package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.Price;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/24/2018.
 */

public class NYGetMinMaxPriceRequest extends NYBasicRequest<Price> {

    private static final String KEY_PRICE = "price";
    private static final String POST_TYPE = "type";

    public NYGetMinMaxPriceRequest(Context context, String type) throws Exception {
        super(String.class, context, context.getResources().getString(R.string.api_path_get_min_max_price));
        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }
    }

    @Override
    protected Price onProcessSuccessData(JSONObject obj) throws Exception {
        Price price = new Price();
        price.parse(obj.getJSONObject(KEY_PRICE));
        return price;
    }
}
