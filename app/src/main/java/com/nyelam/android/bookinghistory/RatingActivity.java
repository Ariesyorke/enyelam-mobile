package com.nyelam.android.bookinghistory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.OrderReturn;
import com.nyelam.android.dotrip.DoTripDetailActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYSubmitReviewRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.willy.ratingbar.ScaleRatingBar;


import org.json.JSONException;
import org.json.JSONObject;

import io.techery.properratingbar.ProperRatingBar;

public class RatingActivity extends AppCompatActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private OrderReturn orderReturn;
    private String idService = null;

    private LinearLayout reviewLinearLayout;
    private ProperRatingBar submitRatingBar;
    private EditText reviewEditText;
    private TextView sendReviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        init();
        initView();
        initToolbar();
        initControl();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));


        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (intent.hasExtra(NYHelper.ORDER_RETURN)) {

                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.ORDER_RETURN));
                    orderReturn = new OrderReturn();
                    orderReturn.parse(obj);
                    if (orderReturn != null && orderReturn.getSummary() != null && orderReturn.getSummary().getDiveService() != null
                            && NYHelper.isStringNotEmpty(orderReturn.getSummary().getDiveService().getId())){
                        idService = orderReturn.getSummary().getDiveService().getId();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void initControl() {
        sendReviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitRatingBar.getRating() < 1){
                    Toast.makeText(RatingActivity.this, "Please, give at least 1 star", Toast.LENGTH_SHORT).show();
                } else if (NYHelper.isStringNotEmpty(idService )){
                    onSubmitReview(idService,String.valueOf(submitRatingBar.getRating()), reviewEditText.getText().toString());
                } else {
                    NYHelper.handlePopupMessage(RatingActivity.this, "Sorry, something wrong", false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                }

            }
        });
    }

    private void initView() {
        reviewLinearLayout = (LinearLayout) findViewById(R.id.review_linearLayout);
        submitRatingBar = (ProperRatingBar) findViewById(R.id.submitRatingBar);
        reviewEditText = (EditText) findViewById(R.id.review_editText);
        sendReviewTextView = (TextView) findViewById(R.id.send_review_textView);
    }

    private void onSubmitReview(String idOrder, String rating, String review) {
        try {
            progressDialog.show();
            NYSubmitReviewRequest req = new NYSubmitReviewRequest(this, idOrder, rating, review);
            spcMgr.execute(req, onSubmitReview());
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }

    private RequestListener<Boolean> onSubmitReview() {
        return new RequestListener<Boolean>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                progressDialog.dismiss();
                //Toast.makeText(RatingActivity.this, "GAGAL POST", Toast.LENGTH_SHORT).show();
                NYHelper.handleAPIException(RatingActivity.this, spiceException, null);

            }

            @Override
            public void onRequestSuccess(Boolean success) {
                progressDialog.dismiss();
                /*Intent intent = new Intent(RatingActivity.this, BookingHistoryDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (orderReturn != null) intent.putExtra(NYHelper.ORDER_RETURN, orderReturn.toString());
                startActivity(intent);*/
                finish();
            }
        };
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }




}
