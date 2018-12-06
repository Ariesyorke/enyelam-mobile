package com.nyelam.android.doshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;
//import com.veinhorn.scrollgalleryview.MediaInfo;
//import com.veinhorn.scrollgalleryview.ScrollGalleryView;
//import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
//import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
//import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoShopDetailItemImagesActivity extends FragmentActivity {

    private ArrayList<String> images = new ArrayList<>();
    public static final String KEY_IMAGES = "images";
    private int pos = 0;

    private ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_detail_item_images);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (intent.hasExtra(KEY_IMAGES) && intent.getStringArrayListExtra(KEY_IMAGES).size() > 0) {
                images = intent.getStringArrayListExtra(KEY_IMAGES);
            }

            if (intent.hasExtra(NYHelper.POSITION)) {
                pos = intent.getIntExtra(NYHelper.POSITION, 0);
            }
        }

        List<MediaInfo> infos = new ArrayList<>(images.size());
        for (String url : images) infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));

        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);

        scrollGalleryView.setCurrentItem(pos);
    }

}
