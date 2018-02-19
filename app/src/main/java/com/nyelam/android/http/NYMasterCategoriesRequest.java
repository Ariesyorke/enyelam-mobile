package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.CategoryList;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/19/2018.
 */

public class NYMasterCategoriesRequest extends NYBasicRequest<NYPaginationResult<CategoryList>> {
    private static final String KEY_CATEGORIES = "categories";
    private static final String POST_PAGE = "page";


    public NYMasterCategoriesRequest(Context context, String page) {
        super(CountryCodeList.class, context, context.getResources().getString(R.string.api_path_master_category));

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

    }

    /*public NYMasterCategoriesRequest(Context context) throws Exception {
        super(CategoryList.class, context, context.getResources().getString(R.string.api_path_master_category));
    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }*/

    @Override
    protected NYPaginationResult<CategoryList> onProcessSuccessData(JSONObject obj) throws Exception {
        NYPaginationResult<CategoryList> temp = new NYPaginationResult<CategoryList>(CategoryList.class) {
            @Override
            protected String getListKey() {
                return KEY_CATEGORIES;
            }
        };
        temp.parse(obj);
        return temp;
    }

    @Override
    public String getCacheControl() {
        return "max-age:86400, no-transform";
    }

}