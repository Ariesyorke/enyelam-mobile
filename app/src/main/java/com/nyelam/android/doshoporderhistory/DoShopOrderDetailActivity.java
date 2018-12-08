package com.nyelam.android.doshoporderhistory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danzoye.lib.util.GalleryCameraInvoker;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.bookinghistory.BookingHistoryDetailActivity;
import com.nyelam.android.data.Additional;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopOrder;
import com.nyelam.android.data.DoShopOrderList;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.Summary;
import com.nyelam.android.doshop.DoShopDetailItemActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveBookingConfirmPaymentRequest;
import com.nyelam.android.http.NYDoShopOrderDetailRequest;
import com.nyelam.android.http.NYDoShopOrderListRequest;
import com.nyelam.android.inbox.NewMessageActivity;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopOrderDetailActivity extends BasicActivity implements GalleryCameraInvoker.CallbackWithProcessing {

    private static final int REQ_CODE_CAMERA = 100;
    private static final int REQ_CODE_GALLERY = 101;
    private static final int REQ_CODE_CHILD = 102;

    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopOrder order;
    private DoShopMerchantItemAdapter adapter;

    private File paymentFile;
    private GalleryCameraInvoker invoker;
    private boolean isPickingPhoto;

    @BindView(R.id.refresh_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.ll_main_container)
    LinearLayout llMainContainer;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_error)
    TextView tvError;

    @BindView(R.id.tv_order_id)
    TextView tvOrderId;

    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;

    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;

    @BindView(R.id.tv_sub_total)
    TextView tvSubTotal;

    @BindView(R.id.tv_shipping_cost)
    TextView tvShippingCost;

    @BindView(R.id.tv_voucher_total)
    TextView tvVoucherTotal;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.tv_billing_name)
    TextView tvBillingName;

    @BindView(R.id.tv_billing_phone)
    TextView tvBillingPhone;

    @BindView(R.id.tv_billing_email)
    TextView tvBillingEmail;

    @BindView(R.id.tv_billing_address)
    TextView tvBillingAddress;

    @BindView(R.id.tv_shipping_name)
    TextView tvShippingName;

    @BindView(R.id.tv_shipping_phone)
    TextView tvShippingPhone;

    @BindView(R.id.tv_shipping_email)
    TextView tvShippingEmail;

    @BindView(R.id.tv_shipping_address)
    TextView tvShippingAddress;

    @BindView(R.id.ll_voucher_container)
    LinearLayout llVoucherContainer;

    @BindView(R.id.ll_additional_container)
    LinearLayout llAdditionalContainer;

    @BindView(R.id.payment_linearLayout)
    LinearLayout paymentLinearLayout;

    @BindView(R.id.transfer_evidence_textView)
    TextView tvTransferEvidence;

    @OnClick(R.id.choose_file_textView) void chooseFile() {
        isPickingPhoto = false;
        onChangePhoto();
    }

    @OnClick(R.id.confirm_linearLayout) void uploadPayment(){
        if (paymentFile != null && order != null && NYHelper.isStringNotEmpty(order.getOrderId())){
            confirmPayment();
        } else {
            Toast.makeText(this, getString(R.string.warn_file_not_found), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_order_detail);
        ButterKnife.bind(this);
        initToolbar();
        initAdapter();
        initExtra();
    }

    private void initAdapter() {
        adapter = new DoShopMerchantItemAdapter(this);

        //ADAPTER SERVICE LIST
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
        recyclerView.setAdapter(adapter);
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.ORDER)) {
                try {
                    JSONObject obj = new JSONObject(intent.getStringExtra(NYHelper.ORDER));
                    order = new DoShopOrder();
                    order.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(intent.hasExtra(NYHelper.ID_ORDER_DO_SHOP)) {
                order = new DoShopOrder();
                order.setOrderId(intent.getStringExtra(NYHelper.ID_ORDER_DO_SHOP));
            }
        }

        if (order != null && NYHelper.isStringNotEmpty(order.getOrderId())){
            getOrderDetail(order.getOrderId());

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    getOrderDetail(order.getOrderId());
                }
            });

        } else {
            dialogOrderNotAvailable();
        }
    }

    private void getOrderDetail(String orderId){
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        llMainContainer.setVisibility(View.GONE);
        NYDoShopOrderDetailRequest req = null;
        try {
            req = new NYDoShopOrderDetailRequest(this, orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onOrderDetailRequest());
    }

    private RequestListener<DoShopOrder> onOrderDetailRequest() {
        return new RequestListener<DoShopOrder>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                //swipeRefreshLayout.setRefreshing(false);
                //llRelatedItem.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                tvError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.GONE);
            }

            @Override
            public void onRequestSuccess(DoShopOrder order) {
                swipeRefreshLayout.setRefreshing(false);
                tvError.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (order != null)initOrderView(order);
                llMainContainer.setVisibility(View.VISIBLE);
            }
        };
    }

    private void initOrderView(DoShopOrder order) {
        if (order != null){

            if (NYHelper.isStringNotEmpty(order.getOrderId()))tvOrderId.setText(order.getOrderId());
            if (NYHelper.isStringNotEmpty(order.getOrderStatus())){
                tvOrderStatus.setText(order.getOrderStatus());
                if (order.getOrderStatus().equalsIgnoreCase("pending") && order.getVeritransToken() == null) {
                    paymentLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    paymentLinearLayout.setVisibility(View.GONE);
                }
            }

            //if (NYHelper.isStringNotEmpty(order.()))tvOrderDate.setText(order.getOrderId());

            if (order.getCart() != null){
                adapter.setData(order.getCart().getMerchants());

                //TOTAL COST
                tvSubTotal.setText(NYHelper.priceFormatter(order.getCart().getSubTotal()));
                tvTotal.setText(NYHelper.priceFormatter(order.getCart().getTotal()));
                if (order.getCart().getVoucher() != null){
                    tvTotal.setText(NYHelper.priceFormatter(order.getCart().getVoucher().getValue()));
                    llVoucherContainer.setVisibility(View.VISIBLE);
                } else {
                    llVoucherContainer.setVisibility(View.GONE);
                }


                if (order.getCart().getMerchants() != null){
                    double totalShippingCost = 0;
                    for (DoShopMerchant merchant : order.getCart().getMerchants()){
                        if (merchant != null && merchant.getDeliveryService() != null)totalShippingCost+=merchant.getDeliveryService().getPrice();
                    }
                    tvShippingCost.setText(NYHelper.priceFormatter(totalShippingCost));
                }

                llAdditionalContainer.removeAllViews();
                if (order.getAdditionals() != null && order.getAdditionals().size() > 0){
                    for (Additional additional : order.getAdditionals()){
                        LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View productView = inflaterAddons.inflate(R.layout.view_item_additional, null);

                        TextView tvLabel = (TextView) productView.findViewById(R.id.additional_label_textView);
                        TextView tvValue = (TextView) productView.findViewById(R.id.additional_value_textView);

                        if (additional != null) {
                            if (NYHelper.isStringNotEmpty(additional.getTitle())) tvLabel.setText(additional.getTitle());
                            tvValue.setText(NYHelper.priceFormatter(additional.getValue()));
                            llAdditionalContainer.addView(productView);
                        }
                    }
                }
            }

            if (order.getBillingAddress() != null){
                if (NYHelper.isStringNotEmpty(order.getBillingAddress().getFullName()))tvBillingName.setText(order.getBillingAddress().getFullName());
                if (NYHelper.isStringNotEmpty(order.getBillingAddress().getPhoneNumber()))tvBillingPhone.setText(order.getBillingAddress().getPhoneNumber());
                if (NYHelper.isStringNotEmpty(order.getBillingAddress().getEmail()))tvBillingEmail.setText(order.getBillingAddress().getEmail());

                String address = "";
                if (NYHelper.isStringNotEmpty(order.getBillingAddress().getAddress())) address+=order.getBillingAddress().getAddress();
                if (order.getBillingAddress().getDistrict() != null && NYHelper.isStringNotEmpty(order.getBillingAddress().getDistrict().getName())) address+=" "+order.getBillingAddress().getDistrict().getName();
                if (order.getBillingAddress().getCity() != null && NYHelper.isStringNotEmpty(order.getBillingAddress().getCity().getName())) address+=" "+order.getBillingAddress().getCity().getName();
                if (order.getBillingAddress().getProvince() != null && NYHelper.isStringNotEmpty(order.getBillingAddress().getProvince().getName())) address+=" "+order.getBillingAddress().getProvince().getName();
                if (NYHelper.isStringNotEmpty(order.getBillingAddress().getZipCode())) address+=", "+order.getBillingAddress().getZipCode();
                if (NYHelper.isStringNotEmpty(address))tvBillingAddress.setText(address);
            }

            if (order.getShippingAddress() != null){
                if (NYHelper.isStringNotEmpty(order.getShippingAddress().getFullName()))tvShippingName.setText(order.getShippingAddress().getFullName());
                if (NYHelper.isStringNotEmpty(order.getShippingAddress().getPhoneNumber()))tvShippingPhone.setText(order.getShippingAddress().getPhoneNumber());
                if (NYHelper.isStringNotEmpty(order.getShippingAddress().getEmail()))tvShippingEmail.setText(order.getShippingAddress().getEmail());

                String address = "";
                if (NYHelper.isStringNotEmpty(order.getShippingAddress().getAddress())) address+=order.getShippingAddress().getAddress();
                if (order.getShippingAddress().getDistrict() != null && NYHelper.isStringNotEmpty(order.getShippingAddress().getDistrict().getName())) address+=" "+order.getShippingAddress().getDistrict().getName();
                if (order.getShippingAddress().getCity() != null && NYHelper.isStringNotEmpty(order.getShippingAddress().getCity().getName())) address+=" "+order.getShippingAddress().getCity().getName();
                if (order.getShippingAddress().getProvince() != null && NYHelper.isStringNotEmpty(order.getShippingAddress().getProvince().getName())) address+=" "+order.getShippingAddress().getProvince().getName();
                if (NYHelper.isStringNotEmpty(order.getShippingAddress().getZipCode())) address+=", "+order.getShippingAddress().getZipCode();
                if (NYHelper.isStringNotEmpty(address))tvShippingAddress.setText(address);
            }

        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    private void confirmPayment() {
        try {
            pDialog.show();
            NYDoDiveBookingConfirmPaymentRequest req = new NYDoDiveBookingConfirmPaymentRequest(this, order.getOrderId(), paymentFile);
            spcMgr.execute(req, onConfirmPaymentRequest());
        } catch (Exception e) {
            pDialog.dismiss();
            e.printStackTrace();
        }
    }

    private RequestListener<Boolean> onConfirmPaymentRequest() {
        return new RequestListener<Boolean>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                pDialog.dismiss();
                NYHelper.handleAPIException(DoShopOrderDetailActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(Boolean success) {
                pDialog.dismiss();
                paymentLinearLayout.setVisibility(View.GONE);
                getOrderDetail(order.getOrderId());
                NYHelper.handlePopupMessage(DoShopOrderDetailActivity.this, getString(R.string.confirmation_payment_success), null);
            }
        };
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

                try {
                    InputStream is = getContentResolver().openInputStream(android.net.Uri.parse(realFile.toURI().toString()));
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

                //Bitmap bitmap = BitmapFactory.decodeFile(realFile.getPath(), bmOptions);
                Bitmap bitmap = null;
                try {
                    InputStream is = getContentResolver().openInputStream(android.net.Uri.parse(realFile.toURI().toString()));
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    @Override
    public void onBitmapResult(File file) {
        if (file != null) {
            this.paymentFile = file;
            if (NYHelper.isStringNotEmpty(file.getName()))
                tvTransferEvidence.setText(file.getName());
        }
    }

    @Override
    public void onCancelGalleryCameraInvoker() {
        isPickingPhoto = false;
    }

    @Override
    public void onProcessing() {

    }

    private void dialogOrderNotAvailable(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order Not Available");
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.warn_order_not_available));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (invoker != null) {
            invoker.onActivityResult(requestCode, resultCode, data);
        }
        if ((requestCode == REQ_CODE_CAMERA || requestCode == REQ_CODE_GALLERY) && resultCode == Activity.RESULT_CANCELED) {
            isPickingPhoto = false;
        }
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
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
    }


}
