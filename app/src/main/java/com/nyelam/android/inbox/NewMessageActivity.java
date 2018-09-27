package com.nyelam.android.inbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.danzoye.lib.util.GalleryCameraInvoker;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYInboxDetailRequest;
import com.nyelam.android.http.NYInboxPostRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NewMessageActivity extends AppCompatActivity implements
        GalleryCameraInvoker.CallbackWithProcessing {

    private static final int REQ_CODE_CAMERA = 100;
    private static final int REQ_CODE_GALLERY = 101;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);

    private Context context;
    private Toolbar toolbar;
    private Spinner sCategory;
    private TextView tvTitle;
    private TextView tvAttachment;
    private TextView btnSubmit;
    private EditText etSubject;
    private EditText etMessage;
    private LinearLayout llCategory;
    private LinearLayout llAttachment;

    private ContactUsSpinnerAdapter mAdapter;
    private ArrayList<String> mCategorySpinnerArray = new ArrayList<>();

    String title = "";
    String type = "";
    String refId = "";
    private boolean postInbox = false;

    private File file;
    private GalleryCameraInvoker invoker;
    private boolean isPickingPhoto;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        context = this;

        initView();
        initCheckExtras();
        initToolbar();
        initControl();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.title_textView);
        tvAttachment = (TextView) findViewById(R.id.tv_attachment);
        btnSubmit = (TextView) findViewById(R.id.btn_submit);
        etSubject = (EditText) findViewById(R.id.et_subject);
        etMessage = (EditText) findViewById(R.id.et_message);
        sCategory = (Spinner) findViewById(R.id.s_category);
        llCategory = (LinearLayout) findViewById(R.id.ll_category);
        llAttachment = (LinearLayout) findViewById(R.id.ll_attachment);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

    }

    private void initCheckExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean bCategory = extras.getBoolean("category", false);
            if (bCategory) {
                llCategory.setVisibility(View.VISIBLE);
                tvTitle.setText("Contact Us");
            } else {
                if(extras.getString("refId") != null){
                    refId = extras.getString("refId");
                }
            }

            if(extras.getString("title") != null){
                title = extras.getString("title");
                etSubject.setText(title);
                etSubject.setEnabled(false);
            }

            if(extras.getString("type") != null){
                type = extras.getString("type");
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

    private void initControl() {
        //Spinner Category Init
        mCategorySpinnerArray.add("General");
        mCategorySpinnerArray.add("Issue App");
        mCategorySpinnerArray.add("Payment");

        mAdapter = new ContactUsSpinnerAdapter(NewMessageActivity.this, R.layout.spinner_contact_us_row, mCategorySpinnerArray);
        sCategory.setAdapter(mAdapter);
        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String category = mCategorySpinnerArray.get(position).toString();
                etSubject.setText(category);
                if(category.equals("General")){
                    type = "3";
                }else if (category.equals("Issue App")){
                    type = "4";
                }else{
                    type = "5";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // TODO Auto-generated catch block
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(NewMessageActivity.this, "Message can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                postInbox = true;
                postInbox();
            }
        });

        //Attachment Init
        llAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickingPhoto = false;
                onChangePhoto();
            }
        });
    }

    private void postInbox(){
        try {
            if(postInbox){
                progressDialog.show();
                NYInboxPostRequest req = new NYInboxPostRequest(this, etSubject.getText().toString(), etMessage.getText().toString(), file, type, refId);
                spcMgr.execute(req, onPostInboxRequest());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<Boolean> onPostInboxRequest() {
        return new RequestListener<Boolean>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                NYHelper.handleAPIException(context, spiceException, null);
            }

            @Override
            public void onRequestSuccess(Boolean success) {
                postInbox = false;
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(success){
                    SdkUIFlowUtil.showToast(NewMessageActivity.this, "Successfully created");
                    finish();
                }else{
                    SdkUIFlowUtil.showToast(NewMessageActivity.this, "Send failed, please try again later.");
                }
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
    public void onBitmapResult(File file) {
        if (file != null) {
            this.file = file;
            if (NYHelper.isStringNotEmpty(file.getName()))
                tvAttachment.setText(file.getName());
        }
    }

    @Override
    public void onCancelGalleryCameraInvoker() {
        isPickingPhoto = false;
    }

    @Override
    public void onProcessing() {

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
    public void onStart() {
        super.onStart();
        spcMgr.start(NewMessageActivity.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCheckExtras();
    }
}
