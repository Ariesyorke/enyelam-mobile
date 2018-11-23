package com.nyelam.android.doshoporder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.Courier;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYSpinner;

import java.util.ArrayList;
import java.util.List;


public class DoShopCartListAdapter extends RecyclerView.Adapter<DoShopCartListAdapter.MyViewHolder> {

    private List<DoShopProduct> data = new ArrayList<>();
    private Context context;
    private Fragment fragment;

    public DoShopCartListAdapter(Context context) {
        this.context = context;
    }

//    public DoShopCartListAdapter(Context context, List<DoShopProduct> data) {
//        this.context = context;
//        this.data = data;
//    }

    public DoShopCartListAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final DoShopProduct product = data.get(position);
        final boolean[] isFirst = new boolean[1];

        if (product != null){
            if (NYHelper.isStringNotEmpty(product.getProductName())) holder.name.setText(product.getProductName());
            holder.qty.setText(String.valueOf(product.getQty()));

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
            final ArrayAdapter qtyAdapter = new ArrayAdapter(context, R.layout.spinner_quantity, quantities);
            holder.spinnerQuantity.setAdapter(qtyAdapter);
            holder.spinnerQuantity.setSelection(selectedPos);
            holder.spinnerQuantity.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                @Override
                public void onSpinnerOpened(Spinner spinner) {
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
                }

                @Override
                public void onSpinnerClosed(Spinner spinner) {
                    int position = spinner.getSelectedItemPosition();
                    if (product != null && NYHelper.isStringNotEmpty((String) qtyAdapter.getItem(position)) && !((String) qtyAdapter.getItem(position)).equals(String.valueOf(product.getQty()))){
                        ((DoShopCartFragment)fragment).onQuantityChange(product.getId(), (String) qtyAdapter.getItem(position));
                    }
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
                        holder.image.setImageResource(R.drawable.example_pic);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        holder.image.setImageResource(R.drawable.example_pic);
                    }
                });

                ImageLoader.getInstance().displayImage(product.getFeaturedImage(), holder.image, NYHelper.getOption());
            }
        }
        
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Remove item", Toast.LENGTH_SHORT).show();
                if (fragment instanceof DoShopCartFragment && product != null && NYHelper.isStringNotEmpty(product.getId())) ((DoShopCartFragment)fragment).onRemoveItem(product.getId());
            }
        });
    }

    public List<DoShopProduct> getData() {
        if (data == null) data = new ArrayList<>();
        return data;
    }

    public void setData(List<DoShopProduct> data) {
        this.data = data;
    }

    public void addData(List<DoShopProduct> data) {
        if (this.data == null) this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    @Override
    public int getItemCount() {
        if (data == null) data = new ArrayList<>();
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView estimate;
        TextView qty;
        TextView remove;
        NYSpinner spinnerQuantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_item_image);
            name = (TextView) itemView.findViewById(R.id.tv_item_name);
            estimate = (TextView) itemView.findViewById(R.id.tv_estimate_delivery);
            qty = (TextView) itemView.findViewById(R.id.tv_item_qty);
            remove = (TextView) itemView.findViewById(R.id.tv_remove_item);
            spinnerQuantity = (NYSpinner) itemView.findViewById(R.id.spinner_quantity);
        }
    }
}
