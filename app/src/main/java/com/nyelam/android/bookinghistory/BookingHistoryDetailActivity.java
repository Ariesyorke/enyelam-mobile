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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danzoye.lib.util.GalleryCameraInvoker;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.Participant;
import com.nyelam.android.data.Summary;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveBookingConfirmPaymentRequest;
import com.nyelam.android.http.NYDoDiveBookingDetailRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

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
    private Summary summary;

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
    private TextView subTotalPriceTextView;
    private TextView voucherTextView;
    private TextView totalPriceTextView;
    private TextView contactNameTextView;
    private TextView contactPhoneNumberTextView;
    private TextView contactEmailTextView;
    private LinearLayout participantContainerLinearLayout;
    private LinearLayout paymentLinearLayout;
    private LinearLayout confirmLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_detail);
        initToolbar();
        initView();
        initControl();
        initExtra();

        Toast.makeText(this, idOrder, Toast.LENGTH_SHORT).show();
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (intent.hasExtra(NYHelper.ID_ORDER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ID_ORDER))) {
                idOrder = extras.getString(NYHelper.ID_ORDER);
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
                if (file != null) confirmPayment();
            }
        });
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_swipeRefreshLayout);
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





    private void getOrderDetail(Boolean isRefresh){

        try {
            if (isRefresh){
                progressBar.setVisibility(View.GONE);
            } else{
                progressBar.setVisibility(View.VISIBLE);
            }
            NYDoDiveBookingDetailRequest req = new NYDoDiveBookingDetailRequest(this, idOrder);
            spcMgr.execute(req, onGetDetailOrderRequest());
        } catch (Exception e) {
            hideLoadingBar();
            e.printStackTrace();
        }

    }

    private RequestListener<Summary> onGetDetailOrderRequest() {
        return new RequestListener<Summary>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                hideLoadingBar();
                NYHelper.handleAPIException(BookingHistoryDetailActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(Summary newSummary) {
                hideLoadingBar();

                if (newSummary != null){
                    summary = newSummary;

                    mainLinearLayout.setVisibility(View.VISIBLE);

                    /*
                    private TextView locationTextView;
                    private TextView contactNameTextView;
                    private TextView contactPhoneNumberTextView;
                    private TextView contactEmailTextView;
                    private LinearLayout participantContainerLinearLayout;
                    */

                    if (summary.getOrder() != null){

                        Order order = summary.getOrder();

                        if (NYHelper.isStringNotEmpty(order.getStatus())){

                            statusTextView.setText(order.getStatus());
                            if (order.getStatus().toLowerCase().equals("unpaid")){
                                paymentLinearLayout.setVisibility(View.VISIBLE);
                            } else {
                                paymentLinearLayout.setVisibility(View.GONE);
                            }
                        }

                        if (NYHelper.isStringNotEmpty(order.getOrderId()))orderIdTextView.setText("#"+order.getOrderId());
                        scheduleTextView.setText(NYHelper.setMillisToDateMonth(order.getSchedule()));

                        if (order.getCart() != null){
                            Cart cart = order.getCart();
                            subTotalPriceTextView.setText(NYHelper.priceFormatter(cart.getSubTotal()));
                            //voucherTextView.setText(NYHelper.priceFormatter(cart.getVoucher().getValue()));
                            totalPriceTextView.setText(NYHelper.priceFormatter(cart.getTotal()));
                        }


                        if (summary.getParticipants() != null && summary.getParticipants().size() > 0){
                            participantContainerLinearLayout.removeAllViews();
                            int pos = 0;
                            for (final Participant participant : summary.getParticipants()) {

                                final int position = pos;

                                LayoutInflater linflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View myParticipantsView = linflaterAddons.inflate(R.layout.view_item_participant, null); //here item is the the layout you want to inflate

                                LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(BookingHistoryDetailActivity.this, 10));
                                myParticipantsView.setLayoutParams(layoutParamsAddons);

                                TextView nameTextView = (TextView) myParticipantsView.findViewById(R.id.name_textView);
                                TextView changeTextView = (TextView) myParticipantsView.findViewById(R.id.change_textView);
                                LinearLayout fillLinearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);

                                if (participant != null && NYHelper.isStringNotEmpty(participant.getName())) {
                                    if (NYHelper.isStringNotEmpty(participant.getName())) nameTextView.setText(participant.getName());
                                    changeTextView.setVisibility(View.GONE);
                                    fillLinearLayout.setVisibility(View.GONE);
                                } else {
                                    nameTextView.setText("Participant "+String.valueOf(position+1));
                                    changeTextView.setVisibility(View.GONE);
                                    fillLinearLayout.setVisibility(View.GONE);
                                }

                                pos++;
                                participantContainerLinearLayout.addView(myParticipantsView);

                            }
                        }

                    }

                    if (summary.getDiveService() != null){
                        DiveService service = summary.getDiveService();
                        if (NYHelper.isStringNotEmpty(service.getName()))serviceNameTextView.setText(service.getName());
                    }


                }

            }
        };
    }



    private void confirmPayment(){
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
                NYHelper.handlePopupMessage(BookingHistoryDetailActivity.this, getString(R.string.confirmation_payment_success), null);
            }
        };
    }



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


    private void hideLoadingBar(){
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
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

        if (file != null){
            this.file = file;
            if (NYHelper.isStringNotEmpty(file.getName()))transferEvidenceTextView.setText(file.getName());
            Toast.makeText(this, file.getName(), Toast.LENGTH_SHORT).show();
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
