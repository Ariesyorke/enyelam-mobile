package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.data.LicenseTypeList;
import com.nyelam.android.data.NationalityList;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYMasterLicenseTypeRequest extends NYBasicRequest<LicenseTypeList> {

    private static final String KEY_LICENSE_TYPE = "lisence_types";
    private static final String POST_ORGANIZATION_ID = "organization_id";

    public NYMasterLicenseTypeRequest(Context context, String organizationId) {
        super(CountryCodeList.class, context, context.getResources().getString(R.string.api_path_master_license_type));

        if(!TextUtils.isEmpty(organizationId)) {
            addQuery(POST_ORGANIZATION_ID, organizationId);
        }

    }

    @Override
    protected LicenseTypeList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_LICENSE_TYPE) instanceof JSONArray) {
            LicenseTypeList licenseTypeList = new LicenseTypeList();
            licenseTypeList.parse(obj.getJSONArray(KEY_LICENSE_TYPE));
            if (licenseTypeList.getList() != null && licenseTypeList.getList().size() > 0) return licenseTypeList;
        }

        return null;
    }

}