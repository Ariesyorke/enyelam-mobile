package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoShopCheckPaymentMethodRequest extends NYBasicAuthRequest<DoShopCartReturn> {

    private static final String POST_CART_TOKEN = "cart_token";
    private static final String POST_PAYMENT_METHOD_ID = "payment_method_id";
    private static final String POST_VOUCHER_CODE = "voucher_code";
    private final static String POST_DELIVERY_SERVICE = "delivery_service";

    public NYDoShopCheckPaymentMethodRequest(Context context, String cartToken, String paymentMethodId, String voucherCode, List<DoShopMerchant> merchants) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_doshop_check_payment_method));

        if(!TextUtils.isEmpty(cartToken)) {
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if(!TextUtils.isEmpty(paymentMethodId)) {
            addQuery(POST_PAYMENT_METHOD_ID, paymentMethodId);
        }

        if(!TextUtils.isEmpty(voucherCode)) {
            addQuery(POST_VOUCHER_CODE, voucherCode);
        }
        if(merchants != null && merchants.size() > 0) {
            for (DoShopMerchant merchant : merchants){
                if (merchant != null && NYHelper.isStringNotEmpty(merchant.getId()) && merchant.getDeliveryService() != null && NYHelper.isStringNotEmpty(merchant.getDeliveryService().getName())){
                    String service = merchant.getDeliveryService().getName()+":"+String.valueOf((int) merchant.getDeliveryService().getPrice());
                    addQuery(POST_DELIVERY_SERVICE+"["+merchant.getId()+"]", service);
                }
            }
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopCartReturn onProcessSuccessData(JSONObject obj) throws Exception {
        DoShopCartReturn cartReturn = new DoShopCartReturn();
        cartReturn.parse(obj);
        return  cartReturn;
    }

}

