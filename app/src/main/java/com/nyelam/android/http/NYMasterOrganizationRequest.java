package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.data.NationalityList;
import com.nyelam.android.data.OrganizationList;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYMasterOrganizationRequest extends NYBasicRequest<OrganizationList> {

    private static final String KEY_ORGANIZATION = "organizations";

    public NYMasterOrganizationRequest(Context context) {
        super(CountryCodeList.class, context, context.getResources().getString(R.string.api_path_master_organization));
    }

    @Override
    protected OrganizationList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_ORGANIZATION) instanceof JSONArray) {
            OrganizationList organizationList = new OrganizationList();
            organizationList.parse(obj.getJSONArray(KEY_ORGANIZATION));
            if (organizationList.getList() != null && organizationList.getList().size() > 0) return organizationList;
        }

        return null;
    }

}