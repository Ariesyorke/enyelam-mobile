package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopOrder;

import org.json.JSONObject;

public class NYDoShopOrderDetailRequest extends NYBasicAuthRequest<DoShopOrder> {

    private final static String KEY_ORDER = "Order";

    private final static String POST_ORDER_ID = "product_id";

    public NYDoShopOrderDetailRequest(Context context, String orderId) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_order_detail));

        if(!TextUtils.isEmpty(orderId)) {
            addQuery(POST_ORDER_ID, orderId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopOrder onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_ORDER) && obj.get(KEY_ORDER) instanceof JSONObject && obj.getJSONObject(KEY_ORDER) != null){
            DoShopOrder order = new DoShopOrder();
            order.parse(obj.getJSONObject(KEY_ORDER));
            return order;
        } else {
          return null;
        }

    }

}