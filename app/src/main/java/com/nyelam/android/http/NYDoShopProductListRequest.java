package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONArray;
import org.json.JSONObject;

public class NYDoShopProductListRequest extends NYBasicRequest<NYPaginationResult<DoShopProductList>> {

    private final static String KEY_PRODUCTS = "products";
    private final static String POST_PAGE = "page";
    private final static String POST_KEYWORD = "keyword";
    private final static String POST_CATEGORY_ID = "category_id";
    private final static String POST_PRICE_MIN = "price_min";
    private final static String POST_PRICE_MAX = "price_max";
    private final static String POST_SORT_BY = "sort_by";

    public NYDoShopProductListRequest(Context context, String page, String keyword, String categoryId, String priceMin, String priceMax, String sortBy){
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_product_list));

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        if(!TextUtils.isEmpty(keyword)) {
            addQuery(POST_KEYWORD, keyword);
        }

        if(!TextUtils.isEmpty(categoryId)) {
            addQuery(POST_CATEGORY_ID, categoryId);
        }

        if(!TextUtils.isEmpty(priceMin)) {
            addQuery(POST_PRICE_MIN, priceMin);
        }

        if(!TextUtils.isEmpty(priceMax)) {
            addQuery(POST_PRICE_MAX, priceMax);
        }

        if(!TextUtils.isEmpty(sortBy)) {
            addQuery(POST_SORT_BY, sortBy);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected NYPaginationResult<DoShopProductList> onProcessSuccessData(JSONObject obj) throws Exception {

//        if (obj.has(KEY_PRODUCTS) && obj.get(KEY_PRODUCTS) instanceof JSONArray && obj.getJSONArray(KEY_PRODUCTS) != null){
//            DoShopProductList doShopList = new DoShopProductList();
//            doShopList.parse(obj.getJSONArray(KEY_PRODUCTS));
//            return doShopList;
//        } else {
//          return null;
//        }

        NYPaginationResult<DoShopProductList> temp = new NYPaginationResult<DoShopProductList>(DoShopProductList.class) {
            @Override
            protected String getListKey() {
                return KEY_PRODUCTS;
            }
        };
        temp.parse(obj);
        return temp;

    }

}