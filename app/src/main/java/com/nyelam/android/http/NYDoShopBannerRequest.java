package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DoShopBannerList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/24/2018.
 */

public class NYDoShopBannerRequest extends NYBasicRequest<DoShopBannerList> {

    private static final String KEY_BANNERS = "banners";

    public NYDoShopBannerRequest(Context context) throws Exception {
        super(String.class, context, context.getResources().getString(R.string.api_path_doshop_banner));
    }

//    @Override
//    public DoShopBannerList loadDataFromNetwork() throws Exception {
//        String json = null;
//        try {
//            InputStream is = getContext().getAssets().open("do_shop_banners.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//            JSONObject obj = new JSONObject(json);
//            return  onProcessSuccessData(obj);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }

    @Override
    protected DoShopBannerList onProcessSuccessData(JSONObject obj) throws Exception {
        if(obj.get(KEY_BANNERS) instanceof JSONArray) {
            DoShopBannerList bannerList = new DoShopBannerList();
            bannerList.parse(obj.getJSONArray(KEY_BANNERS));
            if (bannerList != null && bannerList.getList() != null && bannerList.getList().size() > 0) return bannerList;
        }
        return null;
    }


}
