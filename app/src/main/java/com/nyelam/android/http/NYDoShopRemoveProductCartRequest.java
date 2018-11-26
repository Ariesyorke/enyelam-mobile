package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.Variation;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.util.List;

public class NYDoShopRemoveProductCartRequest extends NYBasicAuthRequest<DoShopCartReturn> {

    private final static String KEY_PRODUCT_CART_ID = "product_cart_id";

    private final static String POST_IDS = "product_cart_id[]";

    //List<String> productsId

    public NYDoShopRemoveProductCartRequest(Context context, String productCartId) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_remove_product_cart));

//        if (productsId != null && productsId.size() > 0){
//            for (String id : productsId){
//                if (NYHelper.isStringNotEmpty(id))addQuery(POST_IDS, id);
//            }
//        }

        if (NYHelper.isStringNotEmpty(productCartId)){
            addQuery(POST_IDS, productCartId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopCartReturn onProcessSuccessData(JSONObject obj) throws Exception {


        DoShopCartReturn doShopCart = new DoShopCartReturn();
        doShopCart.parse(obj);
        return doShopCart;

//        if (obj.has(KEY_PRODUCT_CART_ID) && obj.get(KEY_PRODUCT_CART_ID) instanceof String && NYHelper.isStringNotEmpty(obj.getString(KEY_PRODUCT_CART_ID))){
//            return obj.getString(KEY_PRODUCT_CART_ID);
//        } else {
//          return null;
//        }

    }

}