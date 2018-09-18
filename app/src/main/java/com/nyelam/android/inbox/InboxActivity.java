package com.nyelam.android.inbox;

import android.app.Activity;
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
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danzoye.lib.util.GalleryCameraInvoker;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.InboxDetail;
import com.nyelam.android.data.InboxDetailDataItem;
import com.nyelam.android.data.InboxList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.InboxRecyclerViewAdapter;
import com.nyelam.android.http.NYInboxDetailRequest;
import com.nyelam.android.http.NYInboxRequest;
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
import java.util.List;

public class InboxActivity extends AppCompatActivity implements
        GalleryCameraInvoker.CallbackWithProcessing{

    private static final int REQ_CODE_CAMERA = 100;
    private static final int REQ_CODE_GALLERY = 101;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);

    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText textEt;
    private ImageView attachImageView;
    private ImageView sendImageView;

    private LinearLayout llBottomChat;
    private LinearLayout llAttachment;
    private LinearLayout llCancelAttachment;
    private TextView tvAttachment;

    private List<InboxDetailDataItem> listDataInbox;
    private List<ChatMessage> listMessage;
    private ChatMessageAdapter mAdapter;

    private File file;
    private GalleryCameraInvoker invoker;
    private boolean isPickingPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        context = this;

        initView();
        initToolbar();
        initControl();
        initChat();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llBottomChat = (LinearLayout) findViewById(R.id.ll_bottom_chat);
        llAttachment = (LinearLayout) findViewById(R.id.ll_attachment);
        llCancelAttachment = (LinearLayout) findViewById(R.id.ll_cancel_attachment);
        tvAttachment = (TextView) findViewById(R.id.tv_attachment);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        attachImageView = (ImageView) findViewById(R.id.attach_imageView);
        sendImageView = (ImageView) findViewById(R.id.send_imageView);
        textEt = (EditText) findViewById(R.id.text_et);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    private void initControl() {

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        recyclerView.setAdapter(mAdapter);

        attachImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickingPhoto = false;
                onChangePhoto();
            }
        });

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textEt.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(InboxActivity.this, "Message can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage(message);
                textEt.setText("");
                deleteAttachment();
            }
        });

        llCancelAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAttachment();
            }
        });
    }

    private void initChat(){
        try {
            NYInboxDetailRequest req = new NYInboxDetailRequest(InboxActivity.this);
            spcMgr.execute(req, onChatRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<InboxDetail> onChatRequest() {
        return new RequestListener<InboxDetail>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(InboxActivity.this, spiceException, null);

            }

            @Override
            public void onRequestSuccess(InboxDetail inboxDetail) {
                if(listMessage != null) {
                    listMessage.clear();
                }
                listDataInbox = inboxDetail.getDataInboxDetail().getDataInboxDetailItem();
                for(int i=0; i < listDataInbox.size(); i++){
                    ChatMessage cm = new ChatMessage(listDataInbox.get(i).getSubjectDetail(), true, false);
                    //listMessage.add(cm);
                    mAdapter.add(cm);
                }
/*
                progressBar.setVisibility(View.GONE);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

                //objects.addAll(inboxList.getInboxData());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                if(inboxList != null){
                    inboxAdapter = new InboxRecyclerViewAdapter(getContext(), inboxList.getInboxData());
                    recyclerView.setAdapter(inboxAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }*/
            }
        };
    }

    private void deleteAttachment() {
        if (file != null){
            file.delete();
            file = null;
            llAttachment.setVisibility(View.GONE);
        }else{
            llAttachment.setVisibility(View.GONE);
        }
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

        mimicOtherMessage(message);
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);

        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);

        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
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
                llAttachment.setVisibility(View.VISIBLE);
        }else{
            llAttachment.setVisibility(View.GONE);
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
        spcMgr.start(InboxActivity.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

}
