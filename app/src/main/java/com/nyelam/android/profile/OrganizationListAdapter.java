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
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class OrganizationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Language> languageList;
    private Language currentLanguage;

    public OrganizationListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_country, parent, false);
        return new OrganizationListAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(languageList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (languageList != null && !languageList.isEmpty()) {
            count += languageList.size();
        }
        return count;
    }

    public void addResult(Language language) {
        if (this.languageList == null) {
            this.languageList = new ArrayList<>();
        }
        this.languageList.add(language);
    }

    public void addResults(List<Language> languageList, Language currentLanguage) {
        if (this.languageList == null) {
            this.languageList = new ArrayList<>();
        }
        this.languageList.addAll(languageList);
        this.currentLanguage = currentLanguage;
    }

    public void setResults(List<Language> languageList) {
        if (this.languageList == null) {
            this.languageList = new ArrayList<>();
        }
        this.languageList = languageList;
        this.notifyDataSetChanged();
    }


    public Language getItemByPosition(int position) {
        return languageList.get(position);
    }

    public void searchResults(String keyword) {
        if (NYHelper.isStringNotEmpty(keyword)) {

            List<Language> newList = new ArrayList<>();

            for (Language language : languageList){

                if (language != null && !TextUtils.isEmpty(language.getName()) && language.getName().toLowerCase().contains(keyword.toLowerCase())){
                    newList.add(language);
                }

            }


            this.languageList = newList;
            this.notifyDataSetChanged();

        }
    }

    public void clear() {
        this.languageList = new ArrayList<>();
    }


    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout languageLinearLayout;
        private TextView languageNameTextView;
        private View itemView;
        private Language language;

        public PromoViewHolder(View itemView) {
            super(itemView);

            languageNameTextView = (TextView) itemView.findViewById(R.id.country_name_textView);
            languageLinearLayout = (LinearLayout) itemView.findViewById(R.id.main_linearLayout);

            this.itemView = itemView;
        }

        public void setModel(Language language) {
            this.language = language;

            if (language != null){
                languageNameTextView.setText(language.getName());

                if (NYHelper.isStringNotEmpty(language.getId()) && currentLanguage != null && NYHelper.isStringNotEmpty(currentLanguage.getId()) && language.getId().equals(currentLanguage.getId())){
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