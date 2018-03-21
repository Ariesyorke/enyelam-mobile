package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.data.LanguageList;
import com.nyelam.android.data.NationalityList;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYMasterLanguageRequest extends NYBasicRequest<LanguageList> {

    private static final String KEY_LANGUAGE = "language";
    private static final String POST_SORT = "sort";
    private static final String POST_PAGE = "page";

    public NYMasterLanguageRequest(Context context) {
        super(CountryCodeList.class, context, context.getResources().getString(R.string.api_path_master_language));

        /*if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }*/

        addQuery(POST_SORT, NYHelper.ASC);

    }

    @Override
    protected LanguageList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_LANGUAGE) instanceof JSONArray) {
            LanguageList languageList = new LanguageList();
            languageList.parse(obj.getJSONArray(KEY_LANGUAGE));
            if (languageList.getList() != null && languageList.getList().size() > 0) return languageList;
        }

        return null;
    }

}