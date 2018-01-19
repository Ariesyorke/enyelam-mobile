package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.SummaryList;
import com.nyelam.android.http.NYBasicRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveBookingHistoryRequest extends NYBasicRequest<SummaryList> {

    private static final String KEY_SUMMARY = "summary";

    private static final String POST_PAGE = "page";
    private static final String POST_TYPE = "type";

    public NYDoDiveBookingHistoryRequest(Context context, String page, String type) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_booking_history));

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

        if(obj.get(KEY_SUMMARY) instanceof JSONArray) {
            SummaryList summaryList = new SummaryList();
            summaryList.parse(obj.getJSONArray(KEY_SUMMARY));
            if (summaryList != null && summaryList.getList() != null && summaryList.getList().size() > 0) return summaryList;
        }

        return null;
    }

}

