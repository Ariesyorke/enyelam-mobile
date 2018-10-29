package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.DoShopList;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class NYCategoryRequest extends NYBasicRequest<DoShopList> {

    public NYCategoryRequest(Context context){
        //sumber api asal ambil
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_homepage));
    }


//    @Override
//    public DoShopList loadDataFromNetwork() throws Exception {
//        String json = null;
//        try {
//            InputStream is = getContext().getAssets().open("do_shop_list.json");
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
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_GET;
    }

    @Override
    protected DoShopList onProcessSuccessData(JSONObject obj) throws Exception {
        DoShopList doShopList = new DoShopList();
        doShopList.parse(obj);
        return doShopList;
    }

}