package com.nyelam.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.uikit.utilities.RecyclerItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.Country;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.Language;
import com.nyelam.android.data.LicenseType;
import com.nyelam.android.data.Nationality;
import com.nyelam.android.data.Organization;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCountryCode;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.profile.CountryListAdapter;
import com.nyelam.android.profile.LanguageListAdapter;
import com.nyelam.android.profile.NationalityListAdapter;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/1/2018.
 */

public class NYDialogAddToCart {

    private Dialog dialog;
    private Listener listener;

    public interface Listener {
        void onShopAgainListener();
        void onPayNowListener(DoShopProduct product);
    }

    public void showAddToCartDialog(final Activity activity, final DoShopProduct product){

        listener = (Listener) activity;

        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_to_cart);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.dialog_linearLayout);
        linearLayout.setMinimumWidth(width*5/6);

        ImageView ivItemImage = (ImageView) dialog.findViewById(R.id.iv_item_image);
        TextView tvItemName = (TextView) dialog.findViewById(R.id.tv_item_name);
        TextView tvPriceStrikethrough = (TextView) dialog.findViewById(R.id.tv_price_strikethrough);
        TextView tvPrice= (TextView) dialog.findViewById(R.id.tv_price);
        final TextView tvShopAgain = (TextView) dialog.findViewById(R.id.tv_shop_again);
        final TextView tvPayNow = (TextView) dialog.findViewById(R.id.tv_pay_now);


        if (product != null){

            if (NYHelper.isStringNotEmpty(product.getProductName()))tvItemName.setText(product.getProductName());
//            if (product.getSpecialPrice() < product.getNormalPrice()){
//                tvPrice.setText(NYHelper.priceFormatter(product.getSpecialPrice()));
//                tvPriceStrikethrough.setText(NYHelper.priceFormatter(product.getNormalPrice()));
//            } else {
//                tvPrice.setText(NYHelper.priceFormatter(product.getNormalPrice()));
//            }

            tvPriceStrikethrough.setVisibility(View.GONE);
            tvPrice.setText(NYHelper.priceFormatter(product.getSpecialPrice()));

            if (NYHelper.isStringNotEmpty(product.getFeaturedImage())) {

                ImageLoader.getInstance().loadImage(product.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });

                ImageLoader.getInstance().displayImage(product.getFeaturedImage(), ivItemImage, NYHelper.getOption());

            } else {
                //ivItemImage.setImageResource(R.mipmap.ic_launcher);
            }

        }

        tvShopAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShopAgainListener();
                dialog.dismiss();
            }
        });

        tvPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPayNowListener(product);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}