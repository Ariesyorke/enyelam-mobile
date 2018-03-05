package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.OrderReturn;
import com.nyelam.android.data.Summary;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYDoDiveBookingDetailRequest extends NYBasicAuthRequest<OrderReturn> {

    private static String KEY_SUMMARY = "summary";
    private static String POST_BOOKING_DETAIL_ID = "booking_detail_id";

    public NYDoDiveBookingDetailRequest(Context context, String bookingDetailId) throws Exception {
        super(Summary.class, context, context.getResources().getString(R.string.api_path_dodive_booking_detail));

        if (NYHelper.isStringNotEmpty(bookingDetailId)){
            addQuery(POST_BOOKING_DETAIL_ID, bookingDetailId);
        }

    }

    @Override
    protected OrderReturn onProcessSuccessData(JSONObject obj) throws Exception {
        OrderReturn orderReturn = new OrderReturn();
        orderReturn.parse(obj);
        return orderReturn;
    }

}
