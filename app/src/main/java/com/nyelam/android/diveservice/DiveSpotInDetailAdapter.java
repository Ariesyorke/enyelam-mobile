package com.nyelam.android.diveservice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.divespot.DiveSpotDetailActivity;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public class DiveSpotInDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DiveSpot> diveSpots;
    private String diver;
    private String certificate;
    private String schedule;


    public DiveSpotInDetailAdapter(Context context, String diver, String certificate, String schedule) {
        this.context = context;
        this.diver = diver;
        this.certificate = certificate;
        this.schedule = schedule;
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
        private RatingBar ratingBar;
        private View itemView;
        private DiveSpot diveSpot;

        public PromoViewHolder(View itemView) {
            super(itemView);
            iconImageView = (ImageView) itemView.findViewById(R.id.icon_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description_textView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            this.itemView = itemView;
        }

        public void setModel(DiveSpot diveSpot) {
            this.diveSpot = diveSpot;

            if (diveSpot != null){
                if (diveSpot.getName() != null && !TextUtils.isEmpty(diveSpot.getName())) nameTextView.setText(diveSpot.getName());
                if (diveSpot.getShortDescription() != null && !TextUtils.isEmpty(diveSpot.getShortDescription())){
                    descriptionTextView.setText(diveSpot.getShortDescription());
                } else {
                    descriptionTextView.setVisibility(View.GONE);
                }
                if (diveSpot.getLocation() != null) locationTextView.setText(diveSpot.getLocation().getCity().getName()+", "+diveSpot.getLocation().getProvince().getName()+", "+diveSpot.getLocation().getCountry());
                ratingTextView.setText("*"+diveSpot.getRating());
                ratingBar.setRating(diveSpot.getRating());

                itemView.setOnClickListener(this);
            }
            
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DiveSpotDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(NYHelper.DIVE_SPOT_ID, diveSpot.getId());
            intent.putExtra(NYHelper.DIVER, diver);
            intent.putExtra(NYHelper.CERTIFICATE, certificate);
            intent.putExtra(NYHelper.SCHEDULE, schedule);
            context.startActivity(intent);
        }
    }

}
