package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.Price;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/24/2018.
 */

public class NYGetBannerRequest extends NYBasicRequest<BannerList> {

    private static final String KEY_BANNERS = "banners";

    public NYGetBannerRequest(Context context) throws Exception {
        super(String.class, context, context.getResources().getString(R.string.api_path_banner));
    }

    @Override
    protected BannerList onProcessSuccessData(JSONObject obj) throws Exception {
        if(obj.get(KEY_BANNERS) instanceof JSONArray) {
            BannerList bannerList = new BannerList();
            bannerList.parse(obj.getJSONArray(KEY_BANNERS));
            if (bannerList != null && bannerList.getList() != null && bannerList.getList().size() > 0) return bannerList;
        }
        return null;
    }
}
