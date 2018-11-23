package com.nyelam.android.doshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
//import com.veinhorn.scrollgalleryview.MediaInfo;
//import com.veinhorn.scrollgalleryview.ScrollGalleryView;
//import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
//import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
//import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoShopDetailItemImagesActivity extends FragmentActivity {

    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "http://img1.goodfon.ru/original/1920x1080/d/f5/aircraft-jet-su-47-berkut.jpg",
            "http://www.dishmodels.ru/picture/glr/13/13312/g13312_7657277.jpg",
            "http://img2.goodfon.ru/original/1920x1080/b/c9/su-47-berkut-c-37-firkin.jpg"
    ));
    private static final String movieUrl = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";

//    private ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_detail_item_images);


//        List<MediaInfo> infos = new ArrayList<>(images.size());
//        for (String url : images) infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));
//
//        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
//        scrollGalleryView
//                .setThumbnailSize(100)
//                .setZoom(true)
//                .setFragmentManager(getSupportFragmentManager())
//                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.logo_nyelam)))
//                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(toBitmap(R.drawable.logo_nyelam))))
//                .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
//                    @Override public boolean isImage() {
//                        return true;
//                    }
//
//                    @Override public void loadMedia(Context context, ImageView imageView,
//                                                    MediaLoader.SuccessCallback callback) {
//                        imageView.setImageBitmap(toBitmap(R.drawable.logo_nyelam));
//                        callback.onSuccess();
//                    }
//
//                    @Override public void loadThumbnail(Context context, ImageView thumbnailView,
//                                                        MediaLoader.SuccessCallback callback) {
//                        thumbnailView.setImageBitmap(toBitmap(R.drawable.logo_nyelam));
//                        callback.onSuccess();
//                    }
//                }))
//                .addMedia(MediaInfo.mediaLoader(new DefaultVideoLoader(movieUrl, R.mipmap.ic_launcher)))
//                .addMedia(infos);
    }


    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }

}
