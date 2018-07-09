package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.ReviewList;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYReviewListRequest extends NYBasicAuthRequest<ReviewList> {

    private static final String KEY_REVIEWS = "reviews";

    private static final String POST_SEVICE_ID = "service_id";
    private static final String POST_PAGE = "page";


    public NYReviewListRequest(Context context, String serviceId) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_review_list));

        if(!TextUtils.isEmpty(serviceId)) {
            addQuery(POST_SEVICE_ID, serviceId);
        }

//        if(!TextUtils.isEmpty(page)) {
//            addQuery(POST_PAGE, page);
//        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected ReviewList onProcessSuccessData(JSONObject obj) throws Exception {

        ReviewList reviewList = new ReviewList();
        reviewList.parse(obj.getJSONArray(KEY_REVIEWS));
        return reviewList;
    }

}

