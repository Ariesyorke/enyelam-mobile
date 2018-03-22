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
import com.nyelam.android.data.Country;
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
    private List<CountryCode> countryList;
    private Country currentCountry;

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
            vh.setModel(countryList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (countryList != null && !countryList.isEmpty()) {
            count += countryList.size();
        }
        return count;
    }

    public void addResult(CountryCode country) {
        if (this.countryList == null) {
            this.countryList = new ArrayList<>();
        }
        this.countryList.add(country);
    }

    public void addResults(List<CountryCode> countryList, Country currentCountry) {
        if (this.countryList == null) {
            this.countryList = new ArrayList<>();
        }
        this.countryList.addAll(countryList);
        this.currentCountry = currentCountry;
    }

    public void setResults(List<CountryCode> countryList) {
        if (this.countryList == null) {
            this.countryList = new ArrayList<>();
        }
        this.countryList = countryList;
        this.notifyDataSetChanged();
    }


    public Country getItemByPosition(int position) {
        Country temp = new Country();
        temp.setId(countryList.get(position).getId());
        temp.setName(countryList.get(position).getCountryName());
        return temp;
    }

    public void searchResults(String keyword) {
        if (NYHelper.isStringNotEmpty(keyword)) {

            List<CountryCode> newList = new ArrayList<>();

            for (CountryCode country : countryList){

                if (country != null && !TextUtils.isEmpty(country.getCountryName()) && country.getCountryName().toLowerCase().contains(keyword.toLowerCase())){
                    newList.add(country);
                }

            }

            this.countryList = newList;
            this.notifyDataSetChanged();

        }
    }

    public void clear() {
        this.countryList = new ArrayList<>();
    }


    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout countryLinearLayout;
        private TextView countryNameTextView;
        private View itemView;
        private CountryCode country;

        public PromoViewHolder(View itemView) {
            super(itemView);

            countryNameTextView = (TextView) itemView.findViewById(R.id.country_name_textView);
            countryLinearLayout = (LinearLayout) itemView.findViewById(R.id.main_linearLayout);

            this.itemView = itemView;
        }

        public void setModel(CountryCode country) {
            this.country = country;

            if (country != null){
                countryNameTextView.setText(country.getCountryName());

                if (NYHelper.isStringNotEmpty(country.getId()) && currentCountry != null && NYHelper.isStringNotEmpty(currentCountry.getId()) && country.getId().equals(currentCountry.getId())){
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