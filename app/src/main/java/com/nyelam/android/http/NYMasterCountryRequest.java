package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.data.CountryList;
import com.nyelam.android.data.NationalityList;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYMasterCountryRequest extends NYBasicRequest<CountryList> {

    private static final String KEY_COUNTRY = "country";
    private static final String POST_SORT = "sort";
    private static final String POST_PAGE = "page";

    public NYMasterCountryRequest(Context context) {
        super(CountryCodeList.class, context, context.getResources().getString(R.string.api_path_master_country));

        addQuery(POST_SORT, NYHelper.ASC);

    }

    @Override
    protected CountryList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_COUNTRY) instanceof JSONArray) {
            CountryList countryList = new CountryList();
            countryList.parse(obj.getJSONArray(KEY_COUNTRY));
            if (countryList.getList() != null && countryList.getList().size() > 0) return countryList;
        }

        return null;
    }

}