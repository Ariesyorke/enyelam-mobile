package com.nyelam.android.doshoporder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 11/2/2018.
 */
public class DoShopAddressAdapter extends RecyclerView.Adapter<DoShopAddressAdapter.MyViewHolder>  {

    List<DoShopAddress> addresses;
    Activity activity;
    LayoutInflater layoutInflater;

    public DoShopAddressAdapter(Activity activity) {
        super();
        this.addresses = new ArrayList<>();
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    public DoShopAddressAdapter(Activity activity, List<DoShopAddress> addresses) {
        super();
        this.addresses = addresses;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    public void setAddresses(List<DoShopAddress> addresses) {
        this.addresses = addresses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_address, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DoShopAddress address = addresses.get(position);

        if (address != null){
            if (NYHelper.isStringNotEmpty(address.getFullName())) holder.tvName.setText(address.getFullName());
            if (NYHelper.isStringNotEmpty(address.getAddress())) holder.tvAddress.setText(address.getAddress());
            if (NYHelper.isStringNotEmpty(address.getPhoneNumber())) holder.tvPhone.setText(address.getPhoneNumber());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address != null && NYHelper.isStringNotEmpty(address.getAddressId())){
//                    Intent intent = new Intent(activity, DoShopDetailItemActivity.class);
//                    intent.putExtra(NYHelper.ADDRESS, address.toString());
//                    activity.startActivity(intent);
                    Intent intent=new Intent();
                    intent.putExtra(NYHelper.ADDRESS,address.toString());
                    activity.setResult(2,intent);
                    activity.finish();

                    //Toast.makeText(activity, address.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Sorry, this item is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (addresses == null) addresses = new ArrayList<>();
        return addresses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvAddress;
        TextView tvPhone;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
        }
    }


}