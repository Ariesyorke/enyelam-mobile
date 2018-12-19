package com.nyelam.android.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.doshoporder.DoShopCartFragment;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

public class NYCartItemView extends FrameLayout {
    ImageView image;
    TextView name;
    TextView estimate;
    TextView price;
    TextView priceStrike;
    TextView qty;
    TextView remove;
    NYSpinner spinnerQuantity;
    NYCartItemViewListener listener;

    public NYCartItemView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public NYCartItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);

    }

    public NYCartItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttrs) {
        View.inflate(context, R.layout.view_item_cart, this);
        image = (ImageView) findViewById(R.id.iv_item_image);
        name = (TextView) findViewById(R.id.tv_item_name);
        estimate = (TextView) findViewById(R.id.tv_estimate_delivery);
        priceStrike = (TextView) findViewById(R.id.tv_price_strikethrough);
        price = (TextView) findViewById(R.id.tv_price);
        qty = (TextView) findViewById(R.id.tv_item_qty);
        remove = (TextView) findViewById(R.id.tv_remove_item);
        spinnerQuantity = (NYSpinner) findViewById(R.id.spinner_quantity);
    }

    public void initData(final DoShopProduct product, Context context, final NYCartItemViewListener listener) {
        this.listener = listener;
        final boolean[] isFirst = new boolean[1];

        if (product != null){
            if (NYHelper.isStringNotEmpty(product.getProductName())) name.setText(product.getProductName());
            qty.setText(String.valueOf(product.getQty()));

            if (product.getSpecialPrice() < product.getNormalPrice()){
                price.setText(NYHelper.priceFormatter(product.getSpecialPrice()));
                priceStrike.setText(NYHelper.priceFormatter(product.getNormalPrice()));
                priceStrike.setVisibility(View.GONE);
            } else {
                price.setText(NYHelper.priceFormatter(product.getSpecialPrice()));
                priceStrike.setVisibility(View.GONE);
            }


            int maxQty = 10;
            if (maxQty < product.getQty()){
                maxQty = product.getQty();
            }

            List<String> quantities = new ArrayList<>();
            int selectedPos = 0;
            for (int i=1; i<=maxQty;i++){
                quantities.add(String.valueOf(i));
                if (i==product.getQty()){
                    selectedPos=i-1;
                }
            }
            final ArrayAdapter qtyAdapter = new ArrayAdapter(context, R.layout.spinner_quantity_grey, quantities);
            spinnerQuantity.setAdapter(qtyAdapter);
            spinnerQuantity.setSelection(selectedPos);
            spinnerQuantity.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                @Override
                public void onSpinnerOpened(Spinner spinner) {
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
                }

                @Override
                public void onSpinnerClosed(Spinner spinner) {
                    int position = spinner.getSelectedItemPosition();
                    listener.onQuantityChange(product.getProductCartId(), (String)qtyAdapter.getItem(position));
                }

            });


            if (NYHelper.isStringNotEmpty(product.getFeaturedImage())){
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
                ImageLoader.getInstance().loadImage(product.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        image.setImageResource(R.drawable.example_pic);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        image.setImageResource(R.drawable.example_pic);
                    }
                });

                ImageLoader.getInstance().displayImage(product.getFeaturedImage(), image, NYHelper.getOption());
            }
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Remove item", Toast.LENGTH_SHORT).show();
                showConfirmationDialog(product);
            }
        });
    }

    private void showConfirmationDialog(final DoShopProduct product) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                    .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onRemoveItem(product.getProductCartId());
                        }
                    })
                    .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle(R.string.remove_item)
                    .setMessage(R.string.warn_remove_item)
                    .create();
            dialog.show();
        } catch (Exception e) {
            NYLog.e("showDialog:" + e.getMessage());
        }
    }

}
