package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.DeliveryService;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopOrder;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.Variation;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.util.List;

public class NYDoShopSubmitOrderRequest extends NYBasicAuthRequest<DoShopOrder> {

    private final static String KEY_ORDER = "order";

    private final static String POST_PAYMENT_METHOD_ID = "payment_method_id";
    private final static String POST_CART_TOKEN = "cart_token";
    private final static String POST_BILLING_ADDRESS_ID = "billing_address_id";
    private final static String POST_SHIPPING_ADDRESS_ID = "shipping_address_id";
    //private final static String POST_DELIVERY_SERVICE = "delivery_service[]";
    private final static String POST_DELIVERY_SERVICE = "delivery_service";
    private final static String POST_TYPE_ID = "type_id";
    private final static String POST_VOUCHER_CODE = "voucher_code";

    public NYDoShopSubmitOrderRequest(Context context, String paymentMethodId, String cartToken, String billingAddressId, String shippingAddressId, List<DeliveryService> deliveryServices, String typeId, String voucherCode) throws Exception {
        super(DoShopList.class, context, context.getResources().getString(R.string.api_path_doshop_submit_order));

        if(!TextUtils.isEmpty(paymentMethodId)) {
            addQuery(POST_PAYMENT_METHOD_ID, paymentMethodId);
        }

        if(!TextUtils.isEmpty(cartToken)) {
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if(!TextUtils.isEmpty(billingAddressId)) {
            addQuery(POST_BILLING_ADDRESS_ID, billingAddressId);
        }

        if(!TextUtils.isEmpty(shippingAddressId)) {
            addQuery(POST_SHIPPING_ADDRESS_ID, shippingAddressId);
        }

        if(deliveryServices != null && deliveryServices.size() > 0) {
            for (DeliveryService deliveryService : deliveryServices){
                if (deliveryService != null && NYHelper.isStringNotEmpty(deliveryService.getName())){
                    //String service = deliveryService.getName()+" "+deliveryService.getTypes().get(0).getName()+":"+String.valueOf(deliveryService.getPrice());
                    //addQuery(POST_DELIVERY_SERVICE+"["+String.valueOf(service)+"]", service);
                    String service = deliveryService.getName()+":"+String.valueOf((int)deliveryService.getPrice());
                    addQuery(POST_DELIVERY_SERVICE+"["+String.valueOf(service.length())+"]", service);
                    //addQuery(POST_DELIVERY_SERVICE, deliveryService.toString());
                }
            }
        }

        if(!TextUtils.isEmpty(typeId)) {
            addQuery(POST_TYPE_ID, typeId);
        }

        if(!TextUtils.isEmpty(voucherCode)) {
            addQuery(POST_VOUCHER_CODE, voucherCode);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DoShopOrder onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_ORDER) && obj.get(KEY_ORDER) instanceof JSONObject && obj.getJSONObject(KEY_ORDER) != null){
            DoShopOrder order = new DoShopOrder();
            order.parse(obj.getJSONObject(KEY_ORDER));
            return order;
        } else {
            return null;
        }
    }

}