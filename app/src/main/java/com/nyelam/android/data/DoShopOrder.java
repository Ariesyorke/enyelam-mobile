package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class DoShopOrder implements Parseable {


    private static String KEY_ORDER_ID = "order_id";
    private static String KEY_ORDER_STATUS = "order_status";
    private static String KEY_PAYPAL_CURRENCY = "paypal_currency";
    private static String KEY_VERITRANS_TOKEN = "veritrans_token";
    private static String KEY_BILLING_ADDRESS = "billing_address";
    private static String KEY_SHIPPING_ADDRESS = "shipping_address";
    private static String KEY_ADDITIONALS = "additionals";
    private static String KEY_DELIVERY_SERVICE = "delivery_service";
    private static String KEY_CART = "cart";

    private String orderId;
    private String orderStatus;
    private DoShopAddress billingAddress;
    private DoShopAddress shippingAddress;
    private List<Additional> additionals;
    private DeliveryService deliveryService;
    private DoShopCart cart;
    private PaypalCurrency paypalCurrency;
    private VeritransToken veritransToken;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public DoShopAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(DoShopAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public DoShopAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(DoShopAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public DoShopCart getCart() {
        return cart;
    }

    public void setCart(DoShopCart cart) {
        this.cart = cart;
    }

    public List<Additional> getAdditionals() {
        return additionals;
    }

    public void setAdditionals(List<Additional> additionals) {
        this.additionals = additionals;
    }

    public VeritransToken getVeritransToken() {
        return veritransToken;
    }

    public void setVeritransToken(VeritransToken veritransToken) {
        this.veritransToken = veritransToken;
    }

    public PaypalCurrency getPaypalCurrency() {
        return paypalCurrency;
    }

    public void setPaypalCurrency(PaypalCurrency paypalCurrency) {
        this.paypalCurrency = paypalCurrency;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ORDER_ID)) {
                setOrderId(obj.getString(KEY_ORDER_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ORDER_STATUS)) {
                setOrderStatus(obj.getString(KEY_ORDER_STATUS));
            }
        } catch (JSONException e) {e.printStackTrace();}


        if(!obj.isNull(KEY_BILLING_ADDRESS)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_BILLING_ADDRESS);
                if(o != null && o.length() > 0) {
                    billingAddress = new DoShopAddress();
                    billingAddress.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_SHIPPING_ADDRESS)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_SHIPPING_ADDRESS);
                if(o != null && o.length() > 0) {
                    shippingAddress = new DoShopAddress();
                    shippingAddress.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!obj.isNull(KEY_ADDITIONALS)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_ADDITIONALS);
                if (array != null && array.length() > 0) {
                    additionals = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Additional a = new Additional();
                        a.parse(o);
                        additionals.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }

        if(!obj.isNull(KEY_DELIVERY_SERVICE)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_DELIVERY_SERVICE);
                if(o != null && o.length() > 0) {
                    deliveryService = new DeliveryService();
                    deliveryService.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_CART)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_CART);
                if(o != null && o.length() > 0) {
                    cart = new DoShopCart();
                    cart.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_VERITRANS_TOKEN)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_VERITRANS_TOKEN);
                if(o != null && o.length() > 0) {
                    veritransToken = new VeritransToken();
                    veritransToken.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!obj.isNull(KEY_PAYPAL_CURRENCY)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_VERITRANS_TOKEN);
                if(o != null && o.length() > 0) {
                    paypalCurrency = new PaypalCurrency();
                    paypalCurrency.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (NYHelper.isStringNotEmpty(getOrderId())) {
                obj.put(KEY_ORDER_ID, getOrderId());
            } else {
                obj.put(KEY_ORDER_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (NYHelper.isStringNotEmpty(getOrderStatus())) {
                obj.put(KEY_ORDER_STATUS, getOrderStatus());
            } else {
                obj.put(KEY_ORDER_STATUS, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getBillingAddress()!=null){
                JSONObject objVer = new JSONObject(getBillingAddress().toString());
                obj.put(KEY_BILLING_ADDRESS, objVer);
            } else {
                obj.put(KEY_BILLING_ADDRESS, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getShippingAddress()!=null){
                JSONObject objVer = new JSONObject(getShippingAddress().toString());
                obj.put(KEY_SHIPPING_ADDRESS, objVer);
            } else {
                obj.put(KEY_SHIPPING_ADDRESS, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getDeliveryService()!=null){
                JSONObject objVer = new JSONObject(getDeliveryService().toString());
                obj.put(KEY_DELIVERY_SERVICE, objVer);
            } else {
                obj.put(KEY_DELIVERY_SERVICE, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(additionals != null && !additionals.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(Additional a : additionals) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_ADDITIONALS, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try{
            if(getCart()!=null){
                JSONObject objVer = new JSONObject(getCart().toString());
                obj.put(KEY_CART, objVer);
            } else {
                obj.put(KEY_CART, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getVeritransToken()!=null){
                JSONObject objVer = new JSONObject(getVeritransToken().toString());
                obj.put(KEY_VERITRANS_TOKEN, objVer);
            } else {
                obj.put(KEY_VERITRANS_TOKEN, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try{
            if(getPaypalCurrency()!=null){
                JSONObject objPay = new JSONObject(getPaypalCurrency().toString());
                obj.put(KEY_PAYPAL_CURRENCY, objPay);
            } else {
                obj.put(KEY_PAYPAL_CURRENCY, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}
