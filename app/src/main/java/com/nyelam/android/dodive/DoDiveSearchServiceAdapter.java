package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.SearchDiveCenter;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.data.SearchSpot;
import com.nyelam.android.detail.DetailServiceActivity;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoDiveSearchServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DiveService> searchResults;

    public DoDiveSearchServiceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_search, parent, false);
        return new DoDiveSearchServiceAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(searchResults.get(position));
        }
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (searchResults != null && !searchResults.isEmpty()) {
            count += searchResults.size();
        }
        return count;
    }

    public void addResult(DiveService searchResult) {
        if (this.searchResults == null) {
            this.searchResults = new ArrayList<>();
        }
        this.searchResults.add(searchResult);
    }

    public void addResults(List<DiveService> searchResults) {
        if (this.searchResults == null) {
            this.searchResults = new ArrayList<>();
        }
        this.searchResults.addAll(searchResults);
    }

    public void clear() {
        this.searchResults = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView typeTextView;
        private TextView ratingTextView;
        private TextView labelTextView;
        private View itemView;
        private DiveService searchResult;

        public PromoViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.icon_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textView);
            typeTextView = (TextView) itemView.findViewById(R.id.type_textView);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            labelTextView = (TextView) itemView.findViewById(R.id.label_textView);
            this.itemView = itemView;
        }

        public void setModel(DiveService searchResult) {
            this.searchResult = searchResult;

            if (searchResult.getName() != null && !TextUtils.isEmpty(searchResult.getName())) nameTextView.setText(searchResult.getName());
            //if (searchResult.getRating() != null && !TextUtils.isEmpty(searchResult.getRating())) ratingTextView.setText("*"+searchResult.getRating()+"/5");

            iconImageView.setImageResource(R.drawable.ic_search_dive_spot);
            SearchSpot spot = new SearchSpot();
            try {
                JSONObject obj = new JSONObject(searchResult.toString());
                spot.parse(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            typeTextView.setText("Spot ("+spot.getCount()+")");

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailServiceActivity.class);
            intent.putExtra(NYHelper.SEARCH_RESULT, searchResult.toString());
            context.startActivity(intent);
        }
    }

}