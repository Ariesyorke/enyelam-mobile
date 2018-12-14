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

public class NYDoShopOrderListRequest extends NYBasicAuthRequest<NYPaginationResult<DoShopOrderList>> {

    private final static String KEY_ORDERS = "orders";

    private final static String POST_PAGE = "page";
    private final static String POST_TYPE = "type";

    public NYDoShopOrderListRequest(Context context, String type, String page) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_order_list));
        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }
    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }
    @Override
    protected NYPaginationResult<DoShopOrderList> onProcessSuccessData(JSONObject obj) throws Exception {
        NYPaginationResult<DoShopOrderList> temp = new NYPaginationResult<DoShopOrderList>(DoShopOrderList.class) {
            @Override
            protected String getListKey() {
                return KEY_ORDERS;
            }
        };
        temp.parse(obj);
        return temp;
    }

}