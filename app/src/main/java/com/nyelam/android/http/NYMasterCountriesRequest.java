package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYMasterCountriesRequest extends NYBasicRequest<NYPaginationResult<CountryCodeList>> {
    private static final String KEY_AREA_CODE = "area_code";
    private static final String POST_SORT = "sort";
    private static final String POST_PAGE = "page";

    public NYMasterCountriesRequest(Context context, String page) {
        super(CountryCodeList.class, context, context.getResources().getString(R.string.api_path_master_country));

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        addQuery(POST_SORT, NYHelper.ASC);

    }

    @Override
    protected NYPaginationResult<CountryCodeList> onProcessSuccessData(JSONObject obj) throws Exception {
        NYPaginationResult<CountryCodeList> temp = new NYPaginationResult<CountryCodeList>(CountryCodeList.class) {
            @Override
            protected String getListKey() {
                return KEY_AREA_CODE;
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