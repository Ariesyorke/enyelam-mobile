package com.nyelam.android.doshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 11/2/2018.
 */
public class DoShopMenuCategoryAdapter extends BaseAdapter {

    List<DoShopCategory> categories;
    Activity activity;
    LayoutInflater layoutInflater;

    public DoShopMenuCategoryAdapter(Activity activity) {
        super();
        this.categories = new ArrayList<>();
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }


    public DoShopMenuCategoryAdapter(Activity activity, List<DoShopCategory> categories) {
        super();
        this.categories = categories;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {

        return categories.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView= layoutInflater.inflate(R.layout.view_item_do_shop_menu_category, null);

        ImageView ivPicture =(ImageView)convertView.findViewById(R.id.iv_picture);
        TextView tvName =(TextView)convertView.findViewById(R.id.tv_name);

        final DoShopCategory cat = categories.get(position);

        if (cat != null){
            if (NYHelper.isStringNotEmpty(cat.getName())) tvName.setText(cat.getName());

            NYApplication application = (NYApplication)activity.getApplication();
            String imageUri = "drawable://"+R.drawable.background_blur;

            if(application.getCache(cat.getImageUri()) != null) {
                Bitmap bitmap = application.getCache(cat.getImageUri());
                ivPicture.setImageBitmap(bitmap);
            } else {
                ImageLoader.getInstance().displayImage(imageUri, ivPicture, NYHelper.getCompressedOption(activity));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((DoShopActivity)activity).drawerLayout.closeDrawer(GravityCompat.START);

                    Intent intent = new Intent(activity, DoShopCategoryActivity.class);
                    intent.putExtra(NYHelper.CATEGORY, cat.toString());
                    activity.startActivity(intent);
                }
            });

        }

        return convertView;
    }
}