package com.nyelam.android.detail;

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
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.SearchDiveCenter;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.data.SearchSpot;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public class DiveSpotInDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DiveSpot> diveSpots;

    public DiveSpotInDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_dive_spot, parent, false);
        return new PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(diveSpots.get(position));
        }
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (diveSpots != null && !diveSpots.isEmpty()) {
            count += diveSpots.size();
        }
        return count;
    }

    public void addResult(DiveSpot diveSpot) {
        if (this.diveSpots == null) {
            this.diveSpots = new ArrayList<>();
        }
        this.diveSpots.add(diveSpot);
    }

    public void addResults(List<DiveSpot> diveSpots) {
        if (this.diveSpots == null) {
            this.diveSpots = new ArrayList<>();
        }
        this.diveSpots.addAll(diveSpots);
    }

    public void clear() {
        this.diveSpots = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView ratingTextView;
        private TextView descriptionTextView;
        private TextView locationTextView;
        private View itemView;
        private DiveSpot diveSpot;

        public PromoViewHolder(View itemView) {
            super(itemView);
            iconImageView = (ImageView) itemView.findViewById(R.id.icon_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description_textView);
            this.itemView = itemView;
        }

        public void setModel(DiveSpot diveSpot) {
            this.diveSpot = diveSpot;

            if (diveSpot.getName() != null && !TextUtils.isEmpty(diveSpot.getName())) nameTextView.setText(diveSpot.getName());
            if (diveSpot.getDescription() != null && !TextUtils.isEmpty(diveSpot.getDescription())) descriptionTextView.setText(diveSpot.getName());
            if (diveSpot.getLocation() != null) locationTextView.setText(diveSpot.getLocation().getCity().getName()+", "+diveSpot.getLocation().getProvince().getName()+", "+diveSpot.getLocation().getCountry());
            ratingTextView.setText("*"+diveSpot.getRating());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(context, DoDiveActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(NYHelper.SEARCH_RESULT, searchResult.toString());
            context.startActivity(intent);*/
        }
    }

}
