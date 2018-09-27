package com.nyelam.android.inbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danzoye.lib.util.GalleryCameraInvoker;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.InboxData;
import com.nyelam.android.data.InboxDetail;
import com.nyelam.android.data.InboxDetailDataItem;
import com.nyelam.android.data.InboxList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.InboxRecyclerViewAdapter;
import com.nyelam.android.home.OnLoadMoreListener;
import com.nyelam.android.http.NYInboxAddRequest;
import com.nyelam.android.http.NYInboxDetailRequest;
import com.nyelam.android.http.NYInboxRequest;
import com.nyelam.android.http.NYUserNotFoundException;
import com.nyelam.android.storage.LoginStorage;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class InboxActivity extends AppCompatActivity implements
        GalleryCameraInvoker.CallbackWithProcessing{

    private static final int REQ_CODE_CAMERA = 100;
    private static final int REQ_CODE_GALLERY = 101;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);

    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvTitle;
    private EditText textEt;
    private ImageView attachImageView;
    private ImageView sendImageView;

    private LinearLayout llBottomChat;
    private LinearLayout llAttachment;
    private LinearLayout llCancelAttachment;
    private TextView tvAttachment;

    private List<InboxDetailDataItem> listDataInbox;
    private List<ChatMessage> listMessage = new ArrayList<>();
    private List<ChatMessage> listMessageNext = new ArrayList<>();
    private ChatMessageAdapter mAdapter;

    private File file;
    private GalleryCameraInvoker invoker;
    private boolean isPickingPhoto;

    private ProgressDialog progressDialog;
    private String ticketId = "";
    private String title = "";
    private String status = "";
    private boolean triggered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        context = this;

        initView();
        initToolbar();
        initCheckExtras();
        initControl();
        initChat(Integer.parseInt(ticketId), 1);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llBottomChat = (LinearLayout) findViewById(R.id.ll_bottom_chat);
        llAttachment = (LinearLayout) findViewById(R.id.ll_attachment);
        llCancelAttachment = (LinearLayout) findViewById(R.id.ll_cancel_attachment);
        tvAttachment = (TextView) findViewById(R.id.tv_attachment);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        attachImageView = (ImageView) findViewById(R.id.attach_imageView);
        sendImageView = (ImageView) findViewById(R.id.send_imageView);
        textEt = (EditText) findViewById(R.id.text_et);
        tvTitle = (TextView) findViewById(R.id.title_textView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    private void initCheckExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("ticketid") != null){
                ticketId = extras.getString("ticketid");
            }
            if(extras.getString("title") != null){
                title = extras.getString("title");
            }
            if(extras.getString("status") != null){
                status = extras.getString("status");
            }
        }
    }

    private void initControl() {
        String titleCut = "";
        int lenght = title.length();
        if(lenght > 20){
            titleCut = title.substring(0,20) + "...";
        }else{
            titleCut = title;
        }
        tvTitle.setText(titleCut);
        tvTitle.setSingleLine();
        tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        if(!status.equalsIgnoreCase("open")){
            llBottomChat.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ChatMessageAdapter(recyclerView, context);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // do something
                listDataInbox.clear();
                listMessage.clear();
                listMessageNext.clear();
                mAdapter.clear();
                mAdapter.removeScroll();
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
				progressBar.setVisibility(View.VISIBLE);
                initChat(Integer.parseInt(ticketId), 1);

                // after refresh is done, remember to call the following code
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                }
            }
        });

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
                textEt.setText("");
                SdkUIFlowUtil.hideKeyboard(InboxActivity.this);
                sendMessage(ticketId, message, file);
            }
        });

        llCancelAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAttachment();
            }
        });
    }

    private void initChat(int ticketId, int page){
        try {
            //progressDialog.show();
            NYInboxDetailRequest req = new NYInboxDetailRequest(context, ticketId, page);
            spcMgr.execute(req, onChatRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initNext(int ticketId, int page){
        if(triggered){
            try {
                triggered = false;
                //progressDialog.show();
                NYInboxDetailRequest req = new NYInboxDetailRequest(context, ticketId, page);
                spcMgr.execute(req, onChatRequestNext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private RequestListener<InboxDetail> onChatRequest() {
        return new RequestListener<InboxDetail>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(InboxActivity.this, spiceException, null);
                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
				progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onRequestSuccess(final InboxDetail inboxDetail) {
                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
				progressBar.setVisibility(View.GONE);
                if(listMessage != null) {
                    listMessage.clear();
                }
                if(inboxDetail.getDataInboxDetail().getDataInboxDetailItem() != null){
                    listDataInbox = inboxDetail.getDataInboxDetail().getDataInboxDetailItem();
                    for(int i=0; i < listDataInbox.size(); i++){
                        if(listDataInbox.get(i) != null){

                            String userName = null;
                            String userId = null;
                            int ids = 0;
                            String subjectDetail = null;
                            Boolean mine = false;
                            Boolean image = false;
                            String attachment = null;
                            Date dateInbox = null;

                            LoginStorage loginStorage = new LoginStorage(context);

                            if(loginStorage.isUserLogin()) {
                                userId = loginStorage.user.getUserId();
                            }
                            if(listDataInbox.get(i).getId() != 0 ){
                                ids = listDataInbox.get(i).getId();
                            }
                            if(listDataInbox.get(i).getSubjectDetail() != null ){
                                subjectDetail = listDataInbox.get(i).getSubjectDetail();
                            }
                            if(listDataInbox.get(i).getUserId().equalsIgnoreCase(userId)){
                                mine = true;
                            }
                            if(listDataInbox.get(i).getUserNameDetail() != null ){
                                userName = listDataInbox.get(i).getUserNameDetail();
                            }
                            if(listDataInbox.get(i).getAttachment() != null ){
                                image = true;
                                attachment = listDataInbox.get(i).getAttachment();
                            }
                            if(listDataInbox.get(i).getDateDetail() != null ){
                                dateInbox = listDataInbox.get(i).getDateDetail();
                            }

                            ChatMessage cm = new ChatMessage(ids, userName, subjectDetail, mine, image, attachment, dateInbox);
                            listMessage.add(cm);
                        }
                    }
                    Collections.reverse(listMessage);
                    mAdapter.addResult(listMessage);
                    mAdapter.notifyDataSetChanged();

                    //set load more listener for the RecyclerView adapter
                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if (inboxDetail.getDataInboxDetail().getNext() != null) {
                                mAdapter.addScroll();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // after refresh is done, remember to call the following code
                                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                                            swipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                                        }
                                        mAdapter.removeScroll();
                                        triggered = true;
                                        recyclerView.setVisibility(View.GONE);
										progressBar.setVisibility(View.VISIBLE);
                                        initNext(Integer.parseInt(ticketId), Integer.parseInt(inboxDetail.getDataInboxDetail().getNext()));
                                    }
                                }, 2000);
                            } else {
                                Toast.makeText(context, "Loading data completed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    recyclerView.smoothScrollToPosition(listMessage.size());
                    /*if (listMessage.size() > 2){
                        recyclerView.smoothScrollToPosition(listMessage.size() -2);
                    }*/

                }
            }
        };
    }

    private RequestListener<InboxDetail> onChatRequestNext() {
        return new RequestListener<InboxDetail>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(InboxActivity.this, spiceException, null);
                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
				progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onRequestSuccess(final InboxDetail inboxDetail) {

                if(!listMessage.isEmpty()){
                    Collections.reverse(listMessage);
                    listMessageNext.addAll(listMessage);
                }else{
                    Collections.reverse(listMessageNext);
                }

                if(listMessage != null) {
                    listMessage.clear();
                }

                if(inboxDetail.getDataInboxDetail().getDataInboxDetailItem() != null){
                    listDataInbox = inboxDetail.getDataInboxDetail().getDataInboxDetailItem();
                    for(int i=0; i < listDataInbox.size(); i++){
                        if(listDataInbox.get(i) != null){

                            String userName = null;
                            String userId = null;
                            int ids = 0;
                            String subjectDetail = null;
                            Boolean mine = false;
                            Boolean image = false;
                            String attachment = null;
                            Date dateInbox = null;

                            LoginStorage loginStorage = new LoginStorage(context);

                            if(loginStorage.isUserLogin()) {
                                userId = loginStorage.user.getUserId();
                            }
                            if(listDataInbox.get(i).getSubjectDetail() != null ){
                                subjectDetail = listDataInbox.get(i).getSubjectDetail();
                            }
                            if(listDataInbox.get(i).getUserId().equalsIgnoreCase(userId)){
                                mine = true;
                            }
                            if(listDataInbox.get(i).getId() != 0 ){
                                ids = listDataInbox.get(i).getId();
                            }
                            if(listDataInbox.get(i).getUserNameDetail() != null ){
                                userName = listDataInbox.get(i).getUserNameDetail();
                            }
                            if(listDataInbox.get(i).getAttachment() != null ){
                                image = true;
                                attachment = listDataInbox.get(i).getAttachment();
                            }
                            if(listDataInbox.get(i).getDateDetail() != null ){
                                dateInbox = listDataInbox.get(i).getDateDetail();
                            }

                            ChatMessage cm = new ChatMessage(ids, userName, subjectDetail, mine, image, attachment, dateInbox);
                            listMessage.add(cm);
                        }
                    }

                    for(int j = 0; j < listMessage.size(); j++){
                        boolean duplicateItem = false;
                        ChatMessage chatMessage = listMessage.get(j);
                        if(listMessageNext.size() != 0){
                            for(int k =0; k < listMessageNext.size(); k++){
                                if(listMessageNext.get(j).getId() == chatMessage.getId()){
                                    duplicateItem = true;
                                }
                            }
                            if(!duplicateItem){
                                listMessageNext.add(chatMessage);
                            }
                        }
                    }

                    listMessage.clear();
                    //listMessageNext.addAll(listMessage);
                    Collections.reverse(listMessageNext);
                    mAdapter.clear();
                    mAdapter.addResult(listMessageNext);
                    mAdapter.setLoaded();
                    mAdapter.notifyDataSetChanged();

                    //set load more listener for the RecyclerView adapter
                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if (inboxDetail.getDataInboxDetail().getNext() != null) {
                                mAdapter.addScroll();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // after refresh is done, remember to call the following code
                                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                                            swipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                                        }
                                        mAdapter.removeScroll();
                                        triggered = true;
										recyclerView.setVisibility(View.GONE);
										progressBar.setVisibility(View.VISIBLE);
                                        initNext(Integer.parseInt(ticketId), Integer.parseInt(inboxDetail.getDataInboxDetail().getNext()));
                                    }
                                }, 2000);
                            } else {
                                Toast.makeText(context, "Loading data completed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    /*if (listMessageNext.size() > 2){
                        recyclerView.smoothScrollToPosition(listMessageNext.size() -2);
                    }*/
                }else {
                    Toast.makeText(context, "Loading data completed", Toast.LENGTH_SHORT).show();
                }

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.smoothScrollToPosition(listMessageNext.size());
				/*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
				progressBar.setVisibility(View.GONE);
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

    private void sendMessage(String ticketId, String message, File file) {
        try {
            progressDialog.show();
            NYInboxAddRequest req = new NYInboxAddRequest(InboxActivity.this, ticketId, message, file);
            spcMgr.execute(req, onAddChatRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<Boolean> onAddChatRequest() {
        return new RequestListener<Boolean>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                NYHelper.handleAPIException(context, spiceException, null);
            }

            @Override
            public void onRequestSuccess(final Boolean success) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(success){
                    deleteAttachment();
                    listDataInbox.clear();
                    listMessage.clear();
                    listMessageNext.clear();
                    mAdapter.clear();
                    mAdapter.removeScroll();
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    initChat(Integer.parseInt(ticketId), 1);
                }else{
                    SdkUIFlowUtil.showToast(InboxActivity.this, "Gagal Mengirim Pesan");
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
        if (file != null && file.exists()) {
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
