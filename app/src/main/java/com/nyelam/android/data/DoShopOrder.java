package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class DoShopOrder implements Parseable {


    private static String KEY_ORDER_ID = "order_id";
    private static String KEY_ORDER_STATUS = "order_status";
    private static String KEY_PAYPAL_CURRENCY = "paypal_currency";
    private static String KEY_VERITRANS_TOKEN = "veritrans_token";
    private static String KEY_ADDRESS = "address";
    private static String KEY_DELIVERY_SERVICE = "delivery_service";
    private static String KEY_CART = "cart";

    private String orderId;
    private String orderStatus;
    private DoShopAddress address;
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

    public DoShopAddress getAddress() {
        return address;
    }

    public void setAddress(DoShopAddress address) {
        this.address = address;
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


        if(!obj.isNull(KEY_ADDRESS)) {
            try {
                JSONObject o = obj.getJSONObject(KEY_ADDRESS);
                if(o != null && o.length() > 0) {
                    address = new DoShopAddress();
                    address.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            if(getAddress()!=null){
                JSONObject objVer = new JSONObject(getAddress().toString());
                obj.put(KEY_ADDRESS, objVer);
            } else {
                obj.put(KEY_ADDRESS, JSONObject.NULL);
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
