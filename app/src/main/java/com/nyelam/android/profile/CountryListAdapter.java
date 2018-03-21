package com.nyelam.android.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.bookinghistory.BookingHistoryDetailActivity;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.Summary;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class CountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CountryCode> countryCodeList;
    private CountryCode currentCountryCode;

    public CountryListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_country, parent, false);
        return new CountryListAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(countryCodeList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (countryCodeList != null && !countryCodeList.isEmpty()) {
            count += countryCodeList.size();
        }
        return count;
    }

    public void addResult(CountryCode countryCode) {
        if (this.countryCodeList == null) {
            this.countryCodeList = new ArrayList<>();
        }
        this.countryCodeList.add(countryCode);
    }

    public void addResults(List<CountryCode> countryCodeList, CountryCode currentCountryCode) {
        if (this.countryCodeList == null) {
            this.countryCodeList = new ArrayList<>();
        }
        this.countryCodeList.addAll(countryCodeList);
        this.currentCountryCode = currentCountryCode;
    }

    public void setResults(List<CountryCode> countryCodeList) {
        if (this.countryCodeList == null) {
            this.countryCodeList = new ArrayList<>();
        }
        this.countryCodeList = countryCodeList;
        this.notifyDataSetChanged();
    }


    public CountryCode getItemByPosition(int position) {
        return countryCodeList.get(position);
    }

    public void searchResults(String keyword) {
        if (NYHelper.isStringNotEmpty(keyword)) {

            List<CountryCode> newList = new ArrayList<>();

            for (CountryCode countryCode : countryCodeList){

                if (countryCode != null && !TextUtils.isEmpty(countryCode.getCountryName()) && countryCode.getCountryName().toLowerCase().contains(keyword.toLowerCase())){
                    newList.add(countryCode);
                }

            }

            this.countryCodeList = newList;
            this.notifyDataSetChanged();

        }
    }

    public void clear() {
        this.countryCodeList = new ArrayList<>();
    }


    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout countryLinearLayout;
        private TextView countryNameTextView;
        private View itemView;
        private CountryCode countryCode;

        public PromoViewHolder(View itemView) {
            super(itemView);

            countryNameTextView = (TextView) itemView.findViewById(R.id.country_name_textView);
            countryLinearLayout = (LinearLayout) itemView.findViewById(R.id.main_linearLayout);

            this.itemView = itemView;
        }

        public void setModel(CountryCode countryCode) {
            this.countryCode = countryCode;

            if (countryCode != null){
                countryNameTextView.setText(countryCode.getCountryName());

                if (NYHelper.isStringNotEmpty(countryCode.getId()) && currentCountryCode != null && NYHelper.isStringNotEmpty(currentCountryCode.getId()) && countryCode.getId().equals(currentCountryCode.getId())){
                    countryLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.ny_grey1));
                }
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(context, BookingHistoryDetailActivity.class);
            if (summary != null) intent.putExtra(NYHelper.SERVICE, summary.toString());
            if (summary != null && summary.getOrder() != null && NYHelper.isStringNotEmpty(summary.getOrder().getOrderId()))intent.putExtra(NYHelper.ID_ORDER, summary.getOrder().getOrderId());
            context.startActivity(intent);*/
        }


    }

}