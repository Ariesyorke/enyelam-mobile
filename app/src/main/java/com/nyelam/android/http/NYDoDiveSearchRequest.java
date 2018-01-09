package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.danzoye.lib.util.StringHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchResultList;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public class NYDoDiveSearchRequest extends NYBasicRequest<SearchResultList> {

    private static final String KEY_SEARCH_RESULTS = "search_results";
    private static final String POST_KEYWORD = "keyword";

    public NYDoDiveSearchRequest(Context context, String keyword) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_master_dodive_search));

        if(!TextUtils.isEmpty(keyword)) {
            addQuery(POST_KEYWORD, keyword);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected SearchResultList onProcessSuccessData(JSONObject obj) throws Exception {
        SearchResultList searchResultList = new SearchResultList();
        searchResultList.parse(obj.getJSONArray(KEY_SEARCH_RESULTS));
        if (searchResultList != null && searchResultList.getList() != null && searchResultList.getList().size() > 0) return searchResultList;
        return null;
    }


}
