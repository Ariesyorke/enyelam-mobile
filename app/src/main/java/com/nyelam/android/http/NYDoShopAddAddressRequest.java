package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopAddressList;
import com.nyelam.android.data.DoShopList;

import org.json.JSONArray;
import org.json.JSONObject;

public class NYDoShopAddAddressRequest extends NYBasicAuthRequest<DoShopAddressList> {

    private final static String KEY_ADDRESSES = "addresses";
    private final static String POST_FULLNAME = "fullname";
    private final static String POST_ADDRESS = "address";
    private final static String POST_PHONE_NUMBER = "phone_number";
    private final static String POST_PROVINCE_ID = "province_id";
    private final static String POST_CITY_ID = "city_id";
    private final static String POST_DISTRICT_ID = "district_id";
    private final static String POST_ZIP_CODE = "zip_code";

    public NYDoShopAddAddressRequest(Context context, String fullname, String address, String phoneNumber, String provinceId, String cityId, String districtId, String zipCode) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_add_address));

        if(!TextUtils.isEmpty(fullname)) {
            addQuery(POST_FULLNAME, fullname);
        }

        if(!TextUtils.isEmpty(address)) {
            addQuery(POST_ADDRESS, address);
        }

        if(!TextUtils.isEmpty(phoneNumber)) {
            addQuery(POST_PHONE_NUMBER, phoneNumber);
        }

        if(!TextUtils.isEmpty(provinceId)) {
            addQuery(POST_PROVINCE_ID, provinceId);
        }

        if(!TextUtils.isEmpty(cityId)) {
            addQuery(POST_CITY_ID, cityId);
        }

        if(!TextUtils.isEmpty(districtId)) {
            addQuery(POST_DISTRICT_ID, districtId);
        }

        if(!TextUtils.isEmpty(zipCode)) {
            addQuery(POST_ZIP_CODE, zipCode);
        }


    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopAddressList onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_ADDRESSES) && obj.get(KEY_ADDRESSES) instanceof JSONArray && obj.getJSONArray(KEY_ADDRESSES) != null){
            DoShopAddressList addressList = new DoShopAddressList();
            addressList.parse(obj.getJSONArray(KEY_ADDRESSES));
            return addressList;
        } else {
          return null;
        }

    }

}