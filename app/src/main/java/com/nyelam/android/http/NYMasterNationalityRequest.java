package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.Nationality;
import com.nyelam.android.data.NationalityList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYMasterNationalityRequest extends NYBasicRequest<NationalityList> {

    private static final String KEY_NATIONALITY = "nationality";
    private static final String POST_COUNTRY_ID = "country_id";
    private static final String POST_SORT = "sort";
    private static final String POST_PAGE = "page";

    public NYMasterNationalityRequest(Context context, String countryId) {
        super(CountryCodeList.class, context, context.getResources().getString(R.string.api_path_master_nationality));

        if(!TextUtils.isEmpty(countryId)) {
            addQuery(POST_COUNTRY_ID, countryId);
        }

        /*if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }*/

        addQuery(POST_SORT, NYHelper.ASC);

    }

    @Override
    protected NationalityList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_NATIONALITY) instanceof JSONArray) {
            NationalityList nationalityList = new NationalityList();
            nationalityList.parse(obj.getJSONArray(KEY_NATIONALITY));
            if (nationalityList.getList() != null && nationalityList.getList().size() > 0) return nationalityList;
        }

        return null;
    }

}