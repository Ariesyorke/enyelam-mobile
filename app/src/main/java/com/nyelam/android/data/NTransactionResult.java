package com.nyelam.android.data;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.VaNumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobi on 3/5/18.
 */

public class NTransactionResult implements Parseable {
    private static final String KEY_STATUS_CODE = "status_code"; //
    private static final String KEY_STATUS_MESSAGE = "status_message"; //
    private static final String KEY_TRANSACTION_ID = "transaction_id"; //
    private static final String KEY_MASKED_CARD = "masked_card"; //
    private static final String KEY_ORDER_ID = "order_id"; //
    private static final String KEY_GROSS_AMOUNT = "gross_amount"; //
    private static final String KEY_PAYMENT_TYPE = "payment_type"; //
    private static final String KEY_TRANSACTION_TIME = "transaction_time"; //
    private static final String KEY_TRANSACTION_STATUS = "transaction_status"; //
    private static final String KEY_FRAUD_STATUS = "fraud_status"; //
    private static final String KEY_APPROVAL_CODE = "approval_code"; //
    private static final String KEY_FROM = "from";

    private String statusCode;
    private String statusMessage;
    private String transactionId;
    private String maskedCard;
    private String orderId;
    private String grossAmount;
    private String paymentType;
    private String transactionTime;
    private String transactionStatus;
    private String fraudStatus;
    private String approvalCode;
    private String from = "mobile";
    private boolean secureToken;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public boolean isSecureToken() {
        return secureToken;
    }

    public void setSecureToken(boolean secureToken) {
        this.secureToken = secureToken;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setData(TransactionResponse response) {
        setStatusCode(response.getStatusCode());
        setApprovalCode(response.getApprovalCode());
        setTransactionTime(response.getTransactionTime());
        setGrossAmount(response.getGrossAmount());
        setOrderId(response.getOrderId());
        setPaymentType(response.getPaymentType());
        setTransactionId(response.getTransactionId());
        setTransactionStatus(response.getTransactionStatus());
        setFraudStatus(response.getFraudStatus());
        setStatusMessage(response.getStatusMessage());
        setMaskedCard(response.getMaskedCard());
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            if(!TextUtils.isEmpty(getStatusCode())) {
                obj.put(KEY_STATUS_CODE, getStatusCode());
            }
            if(!TextUtils.isEmpty(getApprovalCode())) {
                obj.put(KEY_APPROVAL_CODE, getApprovalCode());
            }
            if(!TextUtils.isEmpty(getTransactionTime())) {
                obj.put(KEY_TRANSACTION_TIME, getTransactionTime());
            }
            if(!TextUtils.isEmpty(getGrossAmount())) {
                obj.put(KEY_GROSS_AMOUNT, getGrossAmount());
            }
            if(!TextUtils.isEmpty(getOrderId())) {
                obj.put(KEY_ORDER_ID, getOrderId());
            }
            if(!TextUtils.isEmpty(getPaymentType())) {
                obj.put(KEY_PAYMENT_TYPE, getPaymentType());
            }
            if(!TextUtils.isEmpty(getTransactionId())) {
                obj.put(KEY_TRANSACTION_ID, getTransactionId());
            }
            if(!TextUtils.isEmpty(getTransactionStatus())) {
                obj.put(KEY_TRANSACTION_STATUS, getTransactionStatus());
            }
            if(!TextUtils.isEmpty(getFraudStatus())) {
                obj.put(KEY_FRAUD_STATUS, getFraudStatus());
            }
            if(!TextUtils.isEmpty(getStatusMessage())) {
                obj.put(KEY_STATUS_MESSAGE, getStatusMessage());
            }
            if(!TextUtils.isEmpty(getFrom())){
                obj.put(KEY_FROM, getFrom());
            }
            if(!TextUtils.isEmpty(getMaskedCard())) {
                obj.put(KEY_MASKED_CARD, getMaskedCard());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
    @Override
    public void parse(JSONObject obj) {
        try {
            if (!obj.isNull(KEY_STATUS_CODE)) {
                setStatusCode(obj.getString(KEY_STATUS_CODE));
            }
            if(!obj.isNull(KEY_APPROVAL_CODE)) {
                setApprovalCode(obj.getString(KEY_APPROVAL_CODE));
            }
            if(!obj.isNull(KEY_TRANSACTION_TIME)) {
                setTransactionTime(obj.getString(KEY_TRANSACTION_TIME));
            }
            if(!obj.isNull(KEY_GROSS_AMOUNT)) {
                setGrossAmount(obj.getString(KEY_GROSS_AMOUNT));
            }
            if(!obj.isNull(KEY_ORDER_ID)) {
                setOrderId(obj.getString(KEY_ORDER_ID));
            }
            if(!obj.isNull(KEY_PAYMENT_TYPE)) {
                setPaymentType(obj.getString(KEY_PAYMENT_TYPE));
            }
            if(!obj.isNull(KEY_TRANSACTION_ID)) {
                setTransactionId(obj.getString(KEY_TRANSACTION_ID));
            }
            if(!obj.isNull(KEY_TRANSACTION_STATUS)) {
                setTransactionStatus(obj.getString(KEY_TRANSACTION_STATUS));
            }
            if(!obj.isNull(KEY_FRAUD_STATUS)) {
                setFraudStatus(obj.getString(KEY_FRAUD_STATUS));
            }
            if(!obj.isNull(KEY_STATUS_MESSAGE)) {
                setStatusMessage(obj.getString(KEY_STATUS_MESSAGE));
            }
            if(!obj.isNull(KEY_FROM)) {
                setFrom(obj.getString(KEY_FROM));
            }
            if(!obj.isNull(KEY_MASKED_CARD)) {
                setMaskedCard(obj.getString(KEY_MASKED_CARD));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            if(!TextUtils.isEmpty(getStatusCode())) {
                obj.put(KEY_STATUS_CODE, getStatusCode());
            }
            if(!TextUtils.isEmpty(getApprovalCode())) {
                obj.put(KEY_APPROVAL_CODE, getApprovalCode());
            }
            if(!TextUtils.isEmpty(getTransactionTime())) {
                obj.put(KEY_TRANSACTION_TIME, getTransactionTime());
            }
            if(!TextUtils.isEmpty(getGrossAmount())) {
                obj.put(KEY_GROSS_AMOUNT, getGrossAmount());
            }
            if(!TextUtils.isEmpty(getOrderId())) {
                obj.put(KEY_ORDER_ID, getOrderId());
            }
            if(!TextUtils.isEmpty(getPaymentType())) {
                obj.put(KEY_PAYMENT_TYPE, getPaymentType());
            }
            if(!TextUtils.isEmpty(getTransactionId())) {
                obj.put(KEY_TRANSACTION_ID, getTransactionId());
            }
            if(!TextUtils.isEmpty(getTransactionStatus())) {
                obj.put(KEY_TRANSACTION_STATUS, getTransactionStatus());
            }
            if(!TextUtils.isEmpty(getFraudStatus())) {
                obj.put(KEY_FRAUD_STATUS, getFraudStatus());
            }
            if(!TextUtils.isEmpty(getStatusMessage())) {
                obj.put(KEY_STATUS_MESSAGE, getStatusMessage());
            }
            if(!TextUtils.isEmpty(getFrom())){
                obj.put(KEY_FROM, getFrom());
            }
            if(!TextUtils.isEmpty(getMaskedCard())) {
                obj.put(KEY_MASKED_CARD, getMaskedCard());
            }
            return obj.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
