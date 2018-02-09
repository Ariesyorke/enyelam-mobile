package com.nyelam.android.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.danzoye.lib.util.GalleryCameraInvoker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.bookinghistory.BookingHistoryDetailActivity;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.User;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveBookingConfirmPaymentRequest;
import com.nyelam.android.http.NYUploadPhotoCoverRequest;
import com.nyelam.android.http.NYUploadPhotoProfileRequest;
import com.nyelam.android.profile.EditProfileActivity;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountFragment extends Fragment implements
        GalleryCameraInvoker.CallbackWithProcessing  {

    private static final int REQ_CODE_CAMERA = 100;
    private static final int REQ_CODE_GALLERY = 101;
    private static final int REQ_CODE_CHILD = 102;

    private GalleryCameraInvoker invoker;
    private boolean isPickingPhoto;
    private boolean isCover = false;

    private OnFragmentInteractionListener mListener;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private RelativeLayout editProfileRelativeLayout, logoutRelativeLayout;
    private File photoProfile, photoCover;
    private ImageView coverImageView, changeCoverImageView;
    private CircleImageView photoProfileImageView;
    private ProgressDialog progressDialog;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance() {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initControl();
        initPhoto();
    }

    private void initPhoto() {

        LoginStorage loginStorage = new LoginStorage(getActivity());
        if (loginStorage != null && loginStorage.isUserLogin()){

            NYLog.e("CEK USER : "+loginStorage.user.toString());

            User user = loginStorage.user;

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

            // foto profile
            if (user.getPicture() == null || TextUtils.isEmpty(user.getPicture())) {
                photoProfileImageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                ImageLoader.getInstance().loadImage(user.getPicture(), NYHelper.getOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        photoProfileImageView.setImageResource(R.mipmap.ic_launcher);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        photoProfileImageView.setImageBitmap(loadedImage);
                        //activity.getCache().put(imageUri, loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        photoProfileImageView.setImageResource(R.mipmap.ic_launcher);
                    }
                });

                ImageLoader.getInstance().displayImage(user.getPicture(), photoProfileImageView, NYHelper.getOption());
            }


            // foto cover
            if (user.getCover() == null || TextUtils.isEmpty(user.getCover())) {
                coverImageView.setImageResource(R.drawable.example_pic);
            } else {
                ImageLoader.getInstance().loadImage(user.getCover(), NYHelper.getOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        coverImageView.setImageResource(R.drawable.example_pic);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        coverImageView.setImageBitmap(loadedImage);
                        //activity.getCache().put(imageUri, loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        coverImageView.setImageResource(R.drawable.example_pic);
                    }
                });

                ImageLoader.getInstance().displayImage(user.getCover(), coverImageView, NYHelper.getOption());
            }
        }



    }

    private void initControl() {
        editProfileRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        logoutRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.logout));
                builder.setMessage(getString(R.string.warn_logout));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        NYHelper.logout(getActivity());
                    }
                });

                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        changeCoverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCover = true;
                onChangePhoto();
            }
        });


        photoProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCover = false;
                onChangePhoto();
            }
        });

    }

    private void initView(View view) {
        editProfileRelativeLayout = (RelativeLayout) view.findViewById(R.id.edit_profile_relativeLayout);
        logoutRelativeLayout = (RelativeLayout) view.findViewById(R.id.logout_relativeLayout);

        coverImageView = (ImageView) view.findViewById(R.id.cover_imageView);
        changeCoverImageView = (ImageView) view.findViewById(R.id.change_cover_imageView);
        photoProfileImageView = (CircleImageView) view.findViewById(R.id.photo_profile_circleImageView);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(true);
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
        invoker.invokeGalleryAndCamera(this, this, REQ_CODE_CAMERA, REQ_CODE_GALLERY, true);
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

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBitmapResult(File file) {

        NYLog.e("CEK IMAGE 1");

        if (file != null){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            /*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);*/

            if (isCover){
                photoCover = file;
                coverImageView.setImageBitmap(bitmap);
                NYLog.e("CEK IMAGE 2");
            } else {
                photoProfile = file;
                photoProfileImageView.setImageBitmap(bitmap);
                NYLog.e("CEK IMAGE 3");
            }

            uploadPhoto(isCover);
        }
    }


    @Override
    public void onCancelGalleryCameraInvoker() {

    }

    @Override
    public void onProcessing() {

    }





    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }

    private void uploadPhoto(boolean isCover){
        try {
            progressDialog.show();
            if (isCover) {
                NYUploadPhotoCoverRequest req = new NYUploadPhotoCoverRequest(getActivity(), photoCover);
                spcMgr.execute(req, onUploadPhotoRequest());
            } else {
                NYUploadPhotoProfileRequest req = new NYUploadPhotoProfileRequest(getActivity(), photoProfile);
                spcMgr.execute(req, onUploadPhotoRequest());
            }

        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    private RequestListener<AuthReturn> onUploadPhotoRequest() {
        return new RequestListener<AuthReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                hideLoading();
                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(AuthReturn authReturn) {
                hideLoading();

                NYLog.e("INI APA WOY ? "+authReturn.toString());

                LoginStorage loginStorage = new LoginStorage(getActivity());
                loginStorage.user = authReturn.getUser();
                loginStorage.nyelamToken = authReturn.getToken();
                loginStorage.save();

                NYHelper.handlePopupMessage(getActivity(), getString(R.string.message_update_profile_success), null);
            }
        };
    }


    private void hideLoading(){
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        spcMgr.start(getActivity());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }
}
