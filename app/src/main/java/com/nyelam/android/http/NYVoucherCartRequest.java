package com.nyelam.android.http;

import android.content.Context;
import android.content.res.AssetManager;

import com.nyelam.android.R;
import com.nyelam.android.data.Cart;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYVoucherCartRequest extends NYBasicAuthRequest<Cart> {

    private static String KEY_CART = "cart";

    private static String POST_CART_TOKEN = "cart_token";
    private static String POST_VOUCHER_CODE = "voucher_code";

    public NYVoucherCartRequest(Context context, String cartToken, String voucherCode) throws Exception{
        //asal ambil string di path banner
        super(Cart.class, context, context.getResources().getString(R.string.api_path_banner));

        if (NYHelper.isStringNotEmpty(cartToken)){
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if (NYHelper.isStringNotEmpty(voucherCode)){
            addQuery(POST_VOUCHER_CODE, voucherCode);
        }
    }

//    @Override
//    public Cart loadDataFromNetwork() throws Exception {
//        String json = null;
//        try {
//            InputStream is = getContext().getAssets().open("voucher.json");
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
//
//    public static String AssetJSONFile (String filename, Context context) throws IOException {
//        AssetManager manager = context.getAssets();
//        InputStream file = manager.open(filename);
//        byte[] formArray = new byte[file.available()];
//        file.read(formArray);
//        file.close();
//
//        return new String(formArray);
//    }

    @Override
    protected com.nyelam.android.data.Cart onProcessSuccessData(JSONObject obj) throws Exception {
        NYLog.e("JSONOBJCET " + obj);
        Cart cart = null;
        if(!obj.isNull(KEY_CART)){
            cart = new Cart();
            cart.parse(obj.getJSONObject(KEY_CART));
        }
        return cart;
    }

}
