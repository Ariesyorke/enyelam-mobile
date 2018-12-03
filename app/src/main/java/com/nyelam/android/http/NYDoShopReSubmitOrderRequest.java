package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DeliveryService;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopOrder;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.util.List;

public class NYDoShopReSubmitOrderRequest extends NYBasicAuthRequest<DoShopOrder> {

    private final static String KEY_ORDER = "order";

    private final static String POST_PAYMENT_METHOD_ID = "payment_method_id";
    private final static String POST_ORDER_ID = "order_id";

    public NYDoShopReSubmitOrderRequest(Context context, String orderId, String paymentMethodId) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_re_submit_order));

        if(!TextUtils.isEmpty(paymentMethodId)) {
            addQuery(POST_PAYMENT_METHOD_ID, paymentMethodId);
        }

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