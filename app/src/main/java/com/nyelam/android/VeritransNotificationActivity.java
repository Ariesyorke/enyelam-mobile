package com.nyelam.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.booking.BookingServiceSummaryActivity;
import com.nyelam.android.data.NTransactionResult;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.http.VeritransNotificationRequest;
import com.nyelam.android.view.NYSpinner;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class VeritransNotificationActivity extends AppCompatActivity {
    private NTransactionResult result;
    private String orderId = null;
    private String orderIdDoShop = null;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veritrans_notification);
        if(getIntent() != null) {
            Intent intent = getIntent();
            if(intent.hasExtra(NYHelper.ID_ORDER)) {
                orderId = intent.getStringExtra(NYHelper.ID_ORDER);
            }
            if(intent.hasExtra(NYHelper.ID_ORDER_DO_SHOP)) {
                orderIdDoShop = intent.getStringExtra(NYHelper.ID_ORDER_DO_SHOP);
            }
            if(intent.hasExtra(NYHelper.TRANSACTION_RESPONSE)) {
                try {
                    String responseJson = intent.getStringExtra(NYHelper.TRANSACTION_RESPONSE);
                    result = new NTransactionResult();
                    result.parse(new JSONObject(responseJson));
                    VeritransNotificationRequest req = new VeritransNotificationRequest(this, result.toJSONObject());
                    spcMgr.execute(req, new RequestListener<Boolean>() {
                        @Override
                        public void onRequestFailure(SpiceException spiceException) {
                            Intent intent = new Intent(VeritransNotificationActivity.this, HomeActivity.class);
                            if (NYHelper.isStringNotEmpty(orderId))intent.putExtra(NYHelper.ID_ORDER, orderId);
                            if (NYHelper.isStringNotEmpty(orderIdDoShop))intent.putExtra(NYHelper.ID_ORDER_DO_SHOP, orderIdDoShop);
                            intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onRequestSuccess(Boolean aBoolean) {
                            Intent intent = new Intent(VeritransNotificationActivity.this, HomeActivity.class);
                            if (NYHelper.isStringNotEmpty(orderId))intent.putExtra(NYHelper.ID_ORDER, orderId);
                            if (NYHelper.isStringNotEmpty(orderIdDoShop))intent.putExtra(NYHelper.ID_ORDER_DO_SHOP, orderIdDoShop);
                            intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(spcMgr.isStarted()) {
            spcMgr.shouldStop();
        }
    }
}
