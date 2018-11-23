package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DeliveryServiceList;
import com.nyelam.android.data.DoShopCart;
import com.nyelam.android.data.DoShopList;

import org.json.JSONObject;

public class NYDoShopGetListDeliveryRequest extends NYBasicAuthRequest<DeliveryServiceList> {

    private final static String KEY_DELIVERY_SERVICES = "delivery_services";

    private final static String POST_CITY_ID = "city_id";
    private final static String POST_DISTRICT_ID = "district_id";

    public NYDoShopGetListDeliveryRequest(Context context, String cityId, String districtId) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_get_list_delivery));

        if(!TextUtils.isEmpty(cityId)) {
            addQuery(POST_CITY_ID, cityId);
        }

        if(!TextUtils.isEmpty(districtId)) {
            addQuery(POST_DISTRICT_ID, districtId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DeliveryServiceList onProcessSuccessData(JSONObject obj) throws Exception {

        DeliveryServiceList deliveryServiceList = new DeliveryServiceList();
        deliveryServiceList.parse(obj.getJSONArray(KEY_DELIVERY_SERVICES));
        return deliveryServiceList;

    }

}