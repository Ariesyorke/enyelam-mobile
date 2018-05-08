package com.nyelam.android.profile;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.Language;
import com.nyelam.android.data.LicenseType;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class LicenseTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<LicenseType> licenseTypes;
    private LicenseType currentLicenseType;

    public LicenseTypeListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_country, parent, false);
        return new LicenseTypeListAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(licenseTypes.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (licenseTypes != null && !licenseTypes.isEmpty()) {
            count += licenseTypes.size();
        }
        return count;
    }

    public void addResult(LicenseType licenseType) {
        if (this.licenseTypes == null) {
            this.licenseTypes = new ArrayList<>();
        }
        this.licenseTypes.add(licenseType);
    }

    public void addResults(List<LicenseType> licenseTypes, LicenseType currentLicenseType) {
        if (this.licenseTypes == null) {
            this.licenseTypes = new ArrayList<>();
        }
        this.licenseTypes.addAll(licenseTypes);
        this.currentLicenseType = currentLicenseType;
    }

    public void setResults(List<LicenseType> licenseTypes) {
        if (this.licenseTypes == null) {
            this.licenseTypes = new ArrayList<>();
        }
        this.licenseTypes = licenseTypes;
        this.notifyDataSetChanged();
    }


    public LicenseType getItemByPosition(int position) {
        return licenseTypes.get(position);
    }

    public void searchResults(String keyword) {
        if (NYHelper.isStringNotEmpty(keyword)) {

            List<LicenseType> newList = new ArrayList<>();

            for (LicenseType licenseType : licenseTypes){

                if (licenseType != null && !TextUtils.isEmpty(licenseType.getName()) && licenseType.getName().toLowerCase().contains(keyword.toLowerCase())){
                    newList.add(licenseType);
                }

            }


            this.licenseTypes = newList;
            this.notifyDataSetChanged();

        }
    }

    public void clear() {
        this.licenseTypes = new ArrayList<>();
    }


    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout languageLinearLayout;
        private TextView languageNameTextView;
        private View itemView;
        private LicenseType licenseType;

        public PromoViewHolder(View itemView) {
            super(itemView);

            languageNameTextView = (TextView) itemView.findViewById(R.id.country_name_textView);
            languageLinearLayout = (LinearLayout) itemView.findViewById(R.id.main_linearLayout);

            this.itemView = itemView;
        }

        public void setModel(LicenseType licenseType) {
            this.licenseType = licenseType;

            if (licenseType != null){
                languageNameTextView.setText(licenseType.getName());

                if (NYHelper.isStringNotEmpty(licenseType.getId()) && currentLicenseType != null && NYHelper.isStringNotEmpty(currentLicenseType.getId()) && licenseType.getId().equals(currentLicenseType.getId())){
                    languageLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.ny_grey1));
                } else {
                    languageLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
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