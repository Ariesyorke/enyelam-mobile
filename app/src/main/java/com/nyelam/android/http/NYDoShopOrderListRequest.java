package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopOrderList;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONArray;
import org.json.JSONObject;

public class NYDoShopOrderListRequest extends NYBasicAuthRequest<DoShopOrderList> {

    private final static String KEY_ORDERS = "orders";
    private final static String POST_STATUS = "status";

    public NYDoShopOrderListRequest(Context context, String status) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_order_list));

        if(!TextUtils.isEmpty(status)) {
            addQuery(POST_STATUS, status);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopOrderList onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_ORDERS) && obj.get(KEY_ORDERS) instanceof JSONArray && obj.getJSONArray(KEY_ORDERS) != null){
            DoShopOrderList orderList = new DoShopOrderList();
            orderList.parse(obj.getJSONArray(KEY_ORDERS));
            return orderList;
        } else {
            return null;
        }


    }

}