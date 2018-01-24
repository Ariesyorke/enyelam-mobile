package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.SummaryList;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveServiceOrderListRequest extends NYBasicAuthRequest<SummaryList> {

    private static final String KEY_SUMMARY = "summary";

    private static final String POST_PAGE = "page";
    private static final String POST_TYPE = "type";

    public NYDoDiveServiceOrderListRequest(Context context, String type, String page) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_order));

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected SummaryList onProcessSuccessData(JSONObject obj) throws Exception {

        SummaryList summaryList = new SummaryList();
        summaryList.parse(obj.getJSONArray(KEY_SUMMARY));
        return summaryList;
    }

}

