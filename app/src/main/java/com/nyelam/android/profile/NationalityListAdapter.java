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
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.Nationality;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NationalityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Nationality> nationalityList;
    private Nationality currentNationality;

    public NationalityListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_country, parent, false);
        return new NationalityListAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(nationalityList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (nationalityList != null && !nationalityList.isEmpty()) {
            count += nationalityList.size();
        }
        return count;
    }

    public void addResult(Nationality nationalityList) {
        if (this.nationalityList == null) {
            this.nationalityList = new ArrayList<>();
        }
        this.nationalityList.add(nationalityList);
    }

    public void addResults(List<Nationality> nationalityList, Nationality currentNationality) {
        if (this.nationalityList == null) {
            this.nationalityList = new ArrayList<>();
        }
        this.nationalityList.addAll(nationalityList);
        this.currentNationality = currentNationality;
    }

    public void setResults(List<Nationality> nationalityList) {
        if (this.nationalityList == null) {
            this.nationalityList = new ArrayList<>();
        }
        this.nationalityList = nationalityList;
        this.notifyDataSetChanged();
    }


    public Nationality getItemByPosition(int position) {
        return nationalityList.get(position);
    }

    public void searchResults(String keyword) {
        if (NYHelper.isStringNotEmpty(keyword)) {

            List<Nationality> newList = new ArrayList<>();

            for (Nationality nationality : nationalityList){

                if (nationality != null && !TextUtils.isEmpty(nationality.getName()) && nationality.getName().toLowerCase().contains(keyword.toLowerCase())){
                    newList.add(nationality);
                }

            }

            this.nationalityList = newList;
            this.notifyDataSetChanged();

        }
    }

    public void clear() {
        this.nationalityList = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout nationalityLinearLayout;
        private TextView nationalityNameTextView;
        private View itemView;
        private Nationality nationality;

        public PromoViewHolder(View itemView) {
            super(itemView);

            nationalityNameTextView = (TextView) itemView.findViewById(R.id.country_name_textView);
            nationalityLinearLayout = (LinearLayout) itemView.findViewById(R.id.main_linearLayout);

            this.itemView = itemView;
        }

        public void setModel(Nationality nationality) {
            this.nationality = nationality;

            if (nationality != null){
                nationalityNameTextView.setText(nationality.getName());
                if (NYHelper.isStringNotEmpty(nationality.getId()) && currentNationality != null && NYHelper.isStringNotEmpty(currentNationality.getId()) && nationality.getId().equals(currentNationality.getId())){
                    nationalityLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.ny_grey1));
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