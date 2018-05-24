package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.EquipmentRentList;
import com.nyelam.android.data.SummaryList;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYEquipmentRentListRequest extends NYBasicAuthRequest<EquipmentRentList> {

    private static final String KEY_EQUIPMENT_RENTS = "equipment_rents";

    private static final String POST_DIVE_CENTER_ID = "divecenter_id";
    private static final String POST_DATE = "date";

    public NYEquipmentRentListRequest(Context context, String diveCenterId, String date) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_equipment_rent_list));

        if(!TextUtils.isEmpty(date)) {
            addQuery(POST_DATE, date);
        }

        if(!TextUtils.isEmpty(diveCenterId)) {
            addQuery(POST_DIVE_CENTER_ID, diveCenterId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected EquipmentRentList onProcessSuccessData(JSONObject obj) throws Exception {

        EquipmentRentList equipmentRentList = new EquipmentRentList();
        equipmentRentList.parse(obj.getJSONArray(KEY_EQUIPMENT_RENTS));
        return equipmentRentList;
    }

}

