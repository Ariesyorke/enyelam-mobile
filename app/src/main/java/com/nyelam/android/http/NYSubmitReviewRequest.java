package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.OrderReturn;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYSubmitReviewRequest extends NYBasicAuthRequest<Boolean> {

    private static final String KEY_SUCCESS = "success";

    private static final String POST_SERVICE_ID = "service_id";
    private static final String POST_RATING = "rating";
    private static final String POST_REVIEW = "review";

    public NYSubmitReviewRequest(Context context, String serviceId, String rating, String review) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_review_submit));

        if(!TextUtils.isEmpty(serviceId)) {
            addQuery(POST_SERVICE_ID, serviceId);
        }

        if(!TextUtils.isEmpty(rating)) {
            addQuery(POST_RATING, rating);
        }

        if(!TextUtils.isEmpty(review)) {
            addQuery(POST_REVIEW, review);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected Boolean onProcessSuccessData(JSONObject obj) throws Exception {
        return  obj.getBoolean(KEY_SUCCESS);
    }

}

