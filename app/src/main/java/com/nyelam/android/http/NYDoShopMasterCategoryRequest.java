package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class NYDoShopMasterCategoryRequest extends NYBasicRequest<DoShopCategoryList> {

    private final static String KEY_CATEGORIES = "categories";
    //private final static String POST_PAGE = "page";

    public NYDoShopMasterCategoryRequest(Context context){
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_master_category));

//        if(!TextUtils.isEmpty(page)) {
//            addQuery(POST_PAGE, page);
//        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopCategoryList onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_CATEGORIES) && obj.get(KEY_CATEGORIES) instanceof JSONArray && obj.getJSONArray(KEY_CATEGORIES) != null){
            DoShopCategoryList doShopCategoryList = new DoShopCategoryList();
            doShopCategoryList.parse(obj.getJSONArray(KEY_CATEGORIES));
            return doShopCategoryList;
        } else {
          return null;
        }


    }

}