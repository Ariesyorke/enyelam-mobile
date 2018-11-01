package com.nyelam.android.http;

import android.content.Context;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopAddressList;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.data.DoShopList;

import org.json.JSONArray;
import org.json.JSONObject;

public class NYDoShopAddressListRequest extends NYBasicAuthRequest<DoShopAddressList> {

    private final static String KEY_ADDRESSES = "addresses";

    public NYDoShopAddressListRequest(Context context) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_address_list));

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