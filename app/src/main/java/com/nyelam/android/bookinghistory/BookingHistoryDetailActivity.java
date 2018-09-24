package com.nyelam.android.bookinghistory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danzoye.lib.util.GalleryCameraInvoker;
import com.nyelam.android.R;
import com.nyelam.android.StarterActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Additional;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.EquipmentRent;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.OrderReturn;
import com.nyelam.android.data.Participant;
import com.nyelam.android.data.Summary;
import com.nyelam.android.data.Voucher;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveBookingConfirmPaymentRequest;
import com.nyelam.android.http.NYDoDiveBookingDetailRequest;
import com.nyelam.android.http.NYSubmitReviewRequest;
import com.nyelam.android.inbox.NewMessageActivity;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BookingHistoryDetailActivity extends AppCompatActivity implements
        GalleryCameraInvoker.CallbackWithProcessing {

    private static final int REQ_CODE_CAMERA = 100;
    private static final int REQ_CODE_GALLERY = 101;
    private static final int REQ_CODE_CHILD = 102;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String idOrder;
    private boolean isPast;
    private OrderReturn orderReturn;

    private MenuItem itemMsg;

    private File file;
    private GalleryCameraInvoker invoker;
    private boolean isPickingPhoto;

    private LinearLayout mainLinearLayout;
    private TextView chooseFileTextView;
    private TextView transferEvidenceTextView;
    private TextView orderIdTextView;
    private TextView statusTextView;
    private TextView scheduleTextView;
    private TextView serviceNameTextView;
    private TextView locationTextView;
    private TextView diveCenterTextView;
    private TextView subTotalPriceTextView;
    private TextView voucherTextView;
    private TextView totalPriceTextView;
    private TextView contactNameTextView;
    private TextView contactPhoneNumberTextView;
    private TextView contactEmailTextView;
    private LinearLayout participantContainerLinearLayout;
    private LinearLayout paymentLinearLayout;
    private LinearLayout confirmLinearLayout;
    private LinearLayout additionalLinearLayout;

    private LinearLayout reviewLinearLayout;
    private ScaleRatingBar submitRatingBar;
    private EditText reviewEditText;
    private TextView sendReviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_detail);
        initToolbar();
        initView();
        initControl();
        initExtra();
        //Toast.makeText(this, idOrder, Toast.LENGTH_SHORT).show();
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (intent.hasExtra(NYHelper.ORDER_RETURN)) {

                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.ORDER_RETURN));
                    orderReturn = new OrderReturn();
                    orderReturn.parse(obj);
                    setSummaryView();
                    if (orderReturn != null && orderReturn.getSummary() != null && orderReturn.getSummary().getOrder() != null
                            && NYHelper.isStringNotEmpty(orderReturn.getSummary().getOrder().getOrderId()))idOrder = orderReturn.getSummary().getOrder().getOrderId();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (intent.hasExtra(NYHelper.ID_ORDER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ID_ORDER))) {
                idOrder = extras.getString(NYHelper.ID_ORDER);
            }

            if (intent.hasExtra(NYHelper.IS_PAST)) {
                isPast = intent.getBooleanExtra(NYHelper.IS_PAST, false);
            }
        }
    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getOrderDetail(true);
            }
        });

        chooseFileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickingPhoto = false;
                onChangePhoto();
            }
        });

        confirmLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null){
                    confirmPayment();
                } else {
                    Toast.makeText(BookingHistoryDetailActivity.this, getString(R.string.warn_file_not_found), Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendReviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onSubmitReview(String.valueOf(submitRatingBar.getRating()), reviewEditText.getText().toString());
                // TODO: sementara hide review
//                Intent intent = new Intent(BookingHistoryDetailActivity.this, RatingActivity.class);
//                intent.putExtra(NYHelper.ORDER_RETURN, orderReturn.toString());
//                startActivity(intent);
            }
        });

    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_swipeRefreshLayout);
        diveCenterTextView = (TextView) findViewById(R.id.dive_center_name_textView);
        orderIdTextView = (TextView) findViewById(R.id.order_id_textView);
        statusTextView = (TextView) findViewById(R.id.status_textView);
        scheduleTextView = (TextView) findViewById(R.id.schedule_textView);
        serviceNameTextView = (TextView) findViewById(R.id.service_name_textView);
        locationTextView = (TextView) findViewById(R.id.location_textView);
        subTotalPriceTextView = (TextView) findViewById(R.id.sub_total_price_textView);
        voucherTextView = (TextView) findViewById(R.id.voucher_textView);
        totalPriceTextView = (TextView) findViewById(R.id.total_price_textView);
        contactNameTextView = (TextView) findViewById(R.id.contact_name_textView);
        contactPhoneNumberTextView = (TextView) findViewById(R.id.contact_phone_number_textView);
        contactEmailTextView = (TextView) findViewById(R.id.contact_email_textView);
        transferEvidenceTextView = (TextView) findViewById(R.id.transfer_evidence_textView);
        participantContainerLinearLayout = (LinearLayout) findViewById(R.id.participant_container_linearLayout);

        chooseFileTextView = (TextView) findViewById(R.id.choose_file_textView);
        mainLinearLayout = (LinearLayout) findViewById(R.id.main_linearLayout);
        paymentLinearLayout = (LinearLayout) findViewById(R.id.payment_linearLayout);
        confirmLinearLayout = (LinearLayout) findViewById(R.id.confirm_linearLayout);
        additionalLinearLayout = (LinearLayout) findViewById(R.id.additional_linearLayout);

//        reviewLinearLayout = (LinearLayout) findViewById(R.id.review_linearLayout);
//        submitRatingBar = (ScaleRatingBar) findViewById(R.id.submitRatingBar);
//        reviewEditText = (EditText) findViewById(R.id.review_editText);
        sendReviewTextView = (TextView) findViewById(R.id.send_review_textView);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
    }

    protected void onChangePhoto() {
        invoker = new GalleryCameraInvoker() {

            @Override
            protected int maxImageWidth() {
                return getResources().getDimensionPixelSize(R.dimen.max_create_place_image_width);
            }

            @Override
            protected File onProcessingImageFromCamera(String path) {
                File realFile = super.onProcessingImageFromCamera(path);

                Bitmap bitmap = null;
                int targetW = getResources().getDimensionPixelSize(R.dimen.create_place_photo_thumb_width);

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(realFile.getPath(), bmOptions);
                int photoW = bmOptions.outWidth;
                int scaleFactor = photoW / targetW;

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                bitmap = BitmapFactory.decodeFile(realFile.getPath(), bmOptions);

                // Log.d("danzoye", "thumb bitmap = " + bitmap);

                ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, imageStream);
                bitmap.recycle();

                String localPath = realFile.getPath();
                String thumbPath = localPath.replace("JPEG", "THUMB");
                File pictureFile = new File(thumbPath);

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(imageStream.toByteArray());
                    fos.close();

                    return realFile;
                } catch (FileNotFoundException e) {
                    // Log.e("danzoye", "File not found: " + e.getMessage());
                } catch (IOException e) {
                    // Log.e("danzoye", "Error accessing file: " + e.getMessage());
                } finally {
                    System.gc();
                }
                return null;
            }

            @Override
            protected File onProcessingImageFromGallery(InputStream inputStream) throws IOException {
                File realFile = super.onProcessingImageFromGallery(inputStream);

                int targetW = getResources().getDimensionPixelSize(R.dimen.create_place_photo_thumb_width);

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(realFile.getPath(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                int targetH = (photoH * targetW) / photoW;

                // Determine how much to scale down the image
                int scaleFactor = photoW / targetW;

                // Decode the image file into a Bitmap sized to fill the
                // View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(realFile.getPath(), bmOptions);

                ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, imageStream);
                bitmap.recycle();

                String localPath = realFile.getPath();
                String thumbPath = localPath.replace("JPEG", "THUMB");
                File pictureFile = new File(thumbPath);

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(imageStream.toByteArray());
                    fos.close();

                    return realFile;
                } catch (FileNotFoundException e) {
                    // Log.e("danzoye", "File not found: " + e.getMessage());
                } catch (IOException e) {
                    // Log.e("danzoye", "Error accessing file: " + e.getMessage());
                } finally {
                    System.gc();
                }
                return null;

            }

            @Override
            protected void onShowOptionList(Context context) {
                super.onShowOptionList(context);
                isPickingPhoto = true;
            }
        };
        invoker.invokeGalleryAndCamera(this, this, REQ_CODE_CAMERA, REQ_CODE_GALLERY, false);
    }


    private void getOrderDetail(Boolean isRefresh) {

        try {
            if (isRefresh) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            NYDoDiveBookingDetailRequest req = new NYDoDiveBookingDetailRequest(this, idOrder);
            spcMgr.execute(req, onGetDetailOrderRequest());
        } catch (Exception e) {
            hideLoadingBar();
            e.printStackTrace();
        }

    }

    private RequestListener<OrderReturn> onGetDetailOrderRequest() {
        return new RequestListener<OrderReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                hideLoadingBar();
                NYHelper.handleAPIException(BookingHistoryDetailActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(OrderReturn newOrderReturn) {
                hideLoadingBar();

                if (newOrderReturn != null) {
                    orderReturn = newOrderReturn;
                    setSummaryView();
                }

            }
        };
    }

    private void setSummaryView() {

        hideLoadingBar();

        if (orderReturn != null && orderReturn.getSummary() != null) {

            mainLinearLayout.setVisibility(View.VISIBLE);

            Summary summary = orderReturn.getSummary();

            if (summary != null) {
                Contact contact = summary.getContact();
                if (NYHelper.isStringNotEmpty(contact.getName()))
                    contactNameTextView.setText(contact.getName());
                if (NYHelper.isStringNotEmpty(contact.getPhoneNumber()))
                    if (NYHelper.isStringNotEmpty(contact.getCountryCode())){
                        contactPhoneNumberTextView.setText(contact.getCountryCode()+contact.getPhoneNumber());
                    } else {
                        contactPhoneNumberTextView.setText(contact.getPhoneNumber());
                    }
                if (NYHelper.isStringNotEmpty(contact.getEmailAddress()))
                    contactEmailTextView.setText(contact.getEmailAddress());

                //code untuk menyembunyikan menu message
                Order order = summary.getOrder();
                String status = order.getStatus().toString();
                if(status.equals("denied") || status.equals("cancel")){
                    itemMsg.setVisible(false);
                }else{
                    itemMsg.setVisible(true);
                }
                if(isPast){
                    itemMsg.setVisible(false);
                }
            }


            additionalLinearLayout.removeAllViews();


            if (summary != null && summary.getOrder() != null
                    && summary.getOrder().getEquipmentRents() != null
                    && summary.getOrder().getEquipmentRents().size() > 0){

                for (EquipmentRent equipmentRent : summary.getOrder().getEquipmentRents()){

                    LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View additionalView = inflaterAddons.inflate(R.layout.view_item_additional, null);

                    TextView additionalLabelTextView = (TextView) additionalView.findViewById(R.id.additional_label_textView);
                    TextView additionalValueTextView = (TextView) additionalView.findViewById(R.id.additional_value_textView);

                    if (equipmentRent != null) {
                        if (NYHelper.isStringNotEmpty(equipmentRent.getName())) additionalLabelTextView.setText(equipmentRent.getName());
                        additionalValueTextView.setText(NYHelper.priceFormatter(equipmentRent.getSpecialPrice()));
                    }

                    additionalLinearLayout.addView(additionalView);
                }
            }

            if (summary != null && summary.getOrder() != null
                    && summary.getOrder().getAdditionals() != null
                    && summary.getOrder().getAdditionals().size() > 0){

                for (Additional additional : summary.getOrder().getAdditionals()){
                    //NYLog.d("TES ADDITIONALS ADD : "+additional.getTitle());

                    LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View additionalView = inflaterAddons.inflate(R.layout.view_item_additional, null);

                    TextView additionalLabelTextView = (TextView) additionalView.findViewById(R.id.additional_label_textView);
                    TextView additionalValueTextView = (TextView) additionalView.findViewById(R.id.additional_value_textView);

                    if (additional != null) {
                        if (NYHelper.isStringNotEmpty(additional.getTitle())) additionalLabelTextView.setText(additional.getTitle());
                        additionalValueTextView.setText(NYHelper.priceFormatter(additional.getValue()));
                    }

                    additionalLinearLayout.addView(additionalView);
                }
            }

            if(summary != null && summary.getOrder() != null && summary.getOrder().getCart() != null && summary.getOrder().getCart().getVoucher() != null) {
                Voucher voucher = summary.getOrder().getCart().getVoucher();
                LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View additionalView = inflaterAddons.inflate(R.layout.view_item_additional, null);

                TextView additionalLabelTextView = (TextView) additionalView.findViewById(R.id.additional_label_textView);
                TextView additionalValueTextView = (TextView) additionalView.findViewById(R.id.additional_value_textView);

                if (voucher != null) {
                    if (NYHelper.isStringNotEmpty(voucher.getCode())) additionalLabelTextView.setText("Voucher(" + voucher.getCode() +")");
                    additionalValueTextView.setText("-" + NYHelper.priceFormatter(voucher.getValue()));
                }

                additionalLinearLayout.addView(additionalView);
            }



            if (summary.getDiveService() != null) {
                DiveCenter diveCenter = summary.getDiveService().getDiveCenter();
                if (diveCenter != null) {
                    diveCenterTextView.setText(diveCenter.getName());
                    if (diveCenter.getLocation() != null) {
                        Location location = diveCenter.getLocation();
                        String locString = "";
                        if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName()))
                            locString += location.getCity().getName();
                        if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName()))
                            locString += ", " + location.getProvince().getName();
                        if (NYHelper.isStringNotEmpty(location.getCountry()))
                            locString += ", " + location.getCountry();
                        locationTextView.setText(locString);

                    }
                }
            }

            if (summary.getOrder() != null) {

                Order order = summary.getOrder();

                if (NYHelper.isStringNotEmpty(order.getStatus())) {

                    statusTextView.setText(order.getStatus());
                    if (!isPast && order.getStatus().equalsIgnoreCase("unpaid") && orderReturn.getVeritransToken() == null) {
                        paymentLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        paymentLinearLayout.setVisibility(View.GONE);
                    }

                    // TODO: sementara hide review
//                    if (isPast && order.getStatus().equalsIgnoreCase("accepted")){
//                        reviewLinearLayout.setVisibility(View.VISIBLE);
//                    }
                }

                if (NYHelper.isStringNotEmpty(order.getOrderId()))
                    orderIdTextView.setText("#" + order.getOrderId());
                scheduleTextView.setText(NYHelper.setMillisToDateMonth(order.getSchedule()));

                if (order.getCart() != null) {
                    Cart cart = order.getCart();
                    subTotalPriceTextView.setText(NYHelper.priceFormatter(cart.getSubTotal()));
                    //voucherTextView.setText(NYHelper.priceFormatter(cart.getVoucher().getValue()));
                    totalPriceTextView.setText(NYHelper.priceFormatter(cart.getTotal()));
                }

                if (summary.getParticipants() != null && summary.getParticipants().size() > 0) {
                    participantContainerLinearLayout.removeAllViews();
                    int pos = 0;
                    for (final Participant participant : summary.getParticipants()) {

                        final int position = pos;

                        LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View myParticipantsView = inflaterAddons.inflate(R.layout.view_item_participant, null); //here item is the the layout you want to inflate

                        LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(BookingHistoryDetailActivity.this, 10));
                        myParticipantsView.setLayoutParams(layoutParamsAddons);

                        TextView nameTextView = (TextView) myParticipantsView.findViewById(R.id.name_textView);
                        TextView emailTextView = (TextView) myParticipantsView.findViewById(R.id.email_textView);
                        LinearLayout linearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);
                        LinearLayout fillLinearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);

                        if (participant != null) {
                            if (NYHelper.isStringNotEmpty(participant.getName())) {
                                nameTextView.setText(participant.getName());
                            } else {
                                nameTextView.setText("Diver " + String.valueOf(position + 1));
                            }

                            if (NYHelper.isStringNotEmpty(participant.getEmail())) {
                                emailTextView.setText(participant.getEmail());
                            } else {
                                emailTextView.setText("-");
                            }

                            linearLayout.setVisibility(View.GONE);
                            fillLinearLayout.setVisibility(View.GONE);
                        } else {
                            nameTextView.setText("Diver " + String.valueOf(position + 1));
                            emailTextView.setText("-");
                            linearLayout.setVisibility(View.GONE);
                            fillLinearLayout.setVisibility(View.GONE);
                        }

                        pos++;
                        participantContainerLinearLayout.addView(myParticipantsView);

                    }
                }

            }

            if (summary.getDiveService() != null) {
                DiveService service = summary.getDiveService();
                if (NYHelper.isStringNotEmpty(service.getName()))
                    serviceNameTextView.setText(service.getName());
            }


            if (!isPast && (orderReturn.getVeritransToken() == null || !NYHelper.isStringNotEmpty(orderReturn.getVeritransToken().getTokenId())) && summary.getOrder() != null && (NYHelper.isStringNotEmpty(summary.getOrder().getStatus()) && summary.getOrder().getStatus().toLowerCase().equals("unpaid"))) {
                paymentLinearLayout.setVisibility(View.VISIBLE);
            } else {
                paymentLinearLayout.setVisibility(View.GONE);
            }


        } else {

        }


    }


    private void confirmPayment() {
        try {
            progressDialog.show();
            NYDoDiveBookingConfirmPaymentRequest req = new NYDoDiveBookingConfirmPaymentRequest(this, idOrder, file);
            spcMgr.execute(req, onConfirmPaymentRequest());
        } catch (Exception e) {
            hideLoadingBar();
            e.printStackTrace();
        }
    }

    private RequestListener<Boolean> onConfirmPaymentRequest() {
        return new RequestListener<Boolean>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                hideLoadingBar();
                NYHelper.handleAPIException(BookingHistoryDetailActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(Boolean success) {
                hideLoadingBar();
                getOrderDetail(true);
                NYHelper.handlePopupMessage(BookingHistoryDetailActivity.this, getString(R.string.confirmation_payment_success), null);
            }
        };
    }


    private void onSubmitReview(String rating, String review) {

        try {
            progressDialog.show();
            NYSubmitReviewRequest req = new NYSubmitReviewRequest(this, orderReturn.getSummary().getDiveService().getId(), rating, review);
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
                NYHelper.handleAPIException(BookingHistoryDetailActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(Boolean success) {
                progressDialog.dismiss();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_booking, menu);
        itemMsg = menu.findItem(R.id.msg_btn);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }else if(item.getItemId() == R.id.msg_btn){
            if(orderReturn != null){
                Summary summary = orderReturn.getSummary();
                Order order = summary.getOrder();
                Intent intent = new Intent(BookingHistoryDetailActivity.this, NewMessageActivity.class);
                intent.putExtra("title", summary.getDiveService().getName().toString() + " #" + NYHelper.setMillisToDateMonth(order.getSchedule()) );
                intent.putExtra("refId", idOrder);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
        getOrderDetail(false);
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


    private void hideLoadingBar() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (invoker != null) {
            invoker.onActivityResult(requestCode, resultCode, data);
            //plusImageView.setVisibility(View.GONE);
            //logoImageView.setVisibility(View.GONE);
            //Toast.makeText(this, "yess", Toast.LENGTH_SHORT).show();
        }
        if ((requestCode == REQ_CODE_CAMERA || requestCode == REQ_CODE_GALLERY) && resultCode == Activity.RESULT_CANCELED) {
            isPickingPhoto = false;
        }

        /*if (requestCode == GAHelper.REQ_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                storage = new LoginStorage(getActivity());
                setCountryCodeIntoAdapter();
//                showLoggedLayout();
                accountContainer.setVisibility(View.VISIBLE);
                initMisc();
                initLoggedData();
                newChild();
            }
        }*/

//
    }


    @Override
    public void onBitmapResult(File file) {

        if (file != null) {
            this.file = file;
            if (NYHelper.isStringNotEmpty(file.getName()))
                transferEvidenceTextView.setText(file.getName());
            //Toast.makeText(this, file.getName(), Toast.LENGTH_SHORT).show();
        }

        /*changedPhotoFile = file;

        isPickingPhoto = false;
        invoker = null;
        if (changedPhotoFile != null && changedPhotoFile.exists()) {
            profileImage.setImageBitmap(
                    ImageHelper.cropBitmapToCircle(getActivity(),
                            BitmapFactory.decodeFile(changedPhotoFile.getAbsolutePath())));
        } else {
            profileImage.setImageBitmap(ImageHelper.cropBitmapToCircle(getActivity(),
                    BitmapFactory.decodeResource(getActivity().getResources(), R.color.ga_green)));
        }*/
    }

    @Override
    public void onCancelGalleryCameraInvoker() {
        isPickingPhoto = false;
    }

    @Override
    public void onProcessing() {

    }


}
