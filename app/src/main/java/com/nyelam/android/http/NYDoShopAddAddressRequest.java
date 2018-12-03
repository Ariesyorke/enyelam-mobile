package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.Area;
import com.nyelam.android.data.City;
import com.nyelam.android.data.District;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.data.DoShopAddressList;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.Province;

import org.json.JSONArray;
import org.json.JSONObject;

public class NYDoShopAddAddressRequest extends NYBasicAuthRequest<DoShopAddress> {

    private final static String KEY_ADDRESS = "address";

    private final static String POST_ADDRESS_ID = "address_id";
    private final static String POST_LABEL = "label";
    private final static String POST_FULLNAME = "fullname";
    private final static String POST_EMAIL = "email";
    private final static String POST_ADDRESS = "address";
    private final static String POST_PHONE_NUMBER = "phone_number";
    private final static String POST_PROVINCE_ID = "province_id";
    private final static String POST_PROVINCE_NAME = "province_name";
    private final static String POST_CITY_ID = "city_id";
    private final static String POST_CITY_NAME = "city_name";
    private final static String POST_DISTRICT_ID = "district_id";
    private final static String POST_DISTRICT_NAME = "district_name";
    private final static String POST_ZIP_CODE = "zip_code";

    public NYDoShopAddAddressRequest(Context context, String addressId, String label, String fullname, String address, String email, String phoneNumber, Area province, Area city, Area district, String zipCode) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_add_address));

        if(!TextUtils.isEmpty(addressId)) {
            addQuery(POST_ADDRESS_ID, addressId);
        }

        if(!TextUtils.isEmpty(label)) {
            addQuery(POST_LABEL, label);
        }

        if(!TextUtils.isEmpty(fullname)) {
            addQuery(POST_FULLNAME, fullname);
        }

        if(!TextUtils.isEmpty(address)) {
            addQuery(POST_ADDRESS, address);
        }

        if(!TextUtils.isEmpty(phoneNumber)) {
            addQuery(POST_PHONE_NUMBER, phoneNumber);
        }

        if(province != null && !TextUtils.isEmpty(province.getId())) {
            addQuery(POST_PROVINCE_ID, province.getId());
        }

        if(province != null && !TextUtils.isEmpty(province.getName())) {
            addQuery(POST_PROVINCE_NAME, province.getName());
        }

        if(city != null && !TextUtils.isEmpty(city.getId())) {
            addQuery(POST_CITY_ID, city.getId());
        }

        if(city != null && !TextUtils.isEmpty(city.getName())) {
            addQuery(POST_CITY_NAME, city.getName());
        }

        if(district != null && !TextUtils.isEmpty(district.getId())) {
            addQuery(POST_DISTRICT_ID, district.getId());
        }

        if(district != null && !TextUtils.isEmpty(district.getName())) {
            addQuery(POST_DISTRICT_NAME, district.getName());
        }

        if(!TextUtils.isEmpty(zipCode)) {
            addQuery(POST_ZIP_CODE, zipCode);
        }

        if(!TextUtils.isEmpty(email)) {
            addQuery(POST_EMAIL, email);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopAddress onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_ADDRESS) && obj.get(KEY_ADDRESS) instanceof JSONObject && obj.getJSONObject(KEY_ADDRESS) != null){
            DoShopAddress address = new DoShopAddress();
            address.parse(obj.getJSONObject(KEY_ADDRESS));
            return address;
        } else {
          return null;
        }

    }

}