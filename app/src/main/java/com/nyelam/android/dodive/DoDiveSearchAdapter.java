package com.nyelam.android.dodive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.SearchDiveCenter;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.data.SearchSpot;
import com.nyelam.android.dotrip.DoTripActivity;
import com.nyelam.android.dotrip.DoTripKeywordActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.KeywordHistoryStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public class DoDiveSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<SearchResult> searchResults;
    private String date, diver;
    private boolean certificate;

    public DoDiveSearchAdapter(Activity context, String date, String diver, boolean certificate) {
        this.context = context;
        this.date = date;
        this.diver = diver;
        this.certificate = certificate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_search, parent, false);
        return new PromoViewHolder(v);
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

    public void addResult(SearchResult searchResult) {
        if (this.searchResults == null) {
            this.searchResults = new ArrayList<>();
        }
        this.searchResults.add(searchResult);
    }

    public void addResults(List<SearchResult> searchResults) {
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
        private SearchResult searchResult;

        public PromoViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.icon_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textView);
            typeTextView = (TextView) itemView.findViewById(R.id.type_textView);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            labelTextView = (TextView) itemView.findViewById(R.id.label_textView);
            this.itemView = itemView;
        }

        public void setModel(SearchResult searchResult) {
            this.searchResult = searchResult;

            if (searchResult.getName() != null && !TextUtils.isEmpty(searchResult.getName())) nameTextView.setText(searchResult.getName());
            if (searchResult.getRating() != null && !TextUtils.isEmpty(searchResult.getRating())) ratingTextView.setText("*"+searchResult.getRating()+"/5");

            if (searchResult.getType() != null) {
                if (searchResult.getType() == 1){

                    iconImageView.setImageResource(R.drawable.ic_search_dive_spot);
                    SearchSpot spot = new SearchSpot();
                    try {
                        JSONObject obj = new JSONObject(searchResult.toString());
                        spot.parse(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    typeTextView.setText("Spot");

                }  else if (searchResult.getType() == 2){

                    iconImageView.setImageResource(R.drawable.ic_search_dive_service);
                    typeTextView.setText("Service Category");

                    SearchService service = new SearchService();
                    try {
                        JSONObject obj = new JSONObject(searchResult.toString());
                        service.parse(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (service.isLicense()) labelTextView.setText("license needed");

                }  else if (searchResult.getType() == 3){
                    iconImageView.setImageResource(R.drawable.ic_search_dive_center);
                    typeTextView.setText("Dive Center");

                    SearchDiveCenter diveCenter = new SearchDiveCenter();
                    try {
                        JSONObject obj = new JSONObject(searchResult.toString());
                        diveCenter.parse(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    labelTextView.setText(diveCenter.getProvince());

                } else if (searchResult.getType() == 4){
                    typeTextView.setText("Dive Service");
                    iconImageView.setImageResource(R.drawable.ic_search_dive_service);
                    nameTextView.setText(searchResult.getName());
                    labelTextView.setVisibility(View.GONE);

                } else {

                    iconImageView.setImageResource(R.drawable.ic_dive_place);
                    nameTextView.setText(searchResult.getName());
                    ratingTextView.setVisibility(View.GONE);
                    labelTextView.setVisibility(View.GONE);

                    String place = "";
                    if (searchResult.getType() == 5){
                        place = context.getString(R.string.province);
                    } else if (searchResult.getType() == 6){
                        place = context.getString(R.string.area);
                    }

                    if (searchResult.getCount() != null){
                        typeTextView.setText(place+" ("+String.valueOf(searchResult.getCount())+")");
                    } else {
                        typeTextView.setText(place);
                    }

                }
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // TODO: ini cache storage
            KeywordHistoryStorage keywordHistoryStorage = new KeywordHistoryStorage(context);
            List<SearchResult> searchResults = keywordHistoryStorage.getSearchResults();
            if (searchResults == null) searchResults = new ArrayList<>();

            // TODO: cehck if object didnt exist add to storage
            for (SearchResult s : searchResults) {
                if (s != null && NYHelper.isStringNotEmpty(s.getId()) && searchResult != null && NYHelper.isStringNotEmpty(searchResult.getId()) && s.getId().equals(searchResult.getId()) && s.getType().equals(searchResult.getType())){
                    searchResults.remove(s);
                    break;
                }
            }

            if (searchResults.size() > 5) searchResults.remove(4);
            searchResults.add(searchResult);

            keywordHistoryStorage.setSearchResults(searchResults);
            keywordHistoryStorage.save();

            /*Intent intent = new Intent(context, DoDiveActivity.class);

            if (context instanceof DoDiveSearchActivity){
                intent = new Intent(context, DoDiveActivity.class);
            } else if (context instanceof DoTripKeywordActivity){
                intent = new Intent(context, DoTripActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(NYHelper.SEARCH_RESULT, searchResult.toString());
            intent.putExtra(NYHelper.CERTIFICATE, certificate);
            intent.putExtra(NYHelper.SCHEDULE, date);
            intent.putExtra(NYHelper.DIVER, diver);

            Activity activity = (Activity)context;
            if (activity.getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
            }

            if (searchResult.getType().equals("1")){
                intent.putExtra(NYHelper.DIVE_SPOT, searchResult.toString());
            }

            context.startActivity(intent);*/

            if (context instanceof DoDiveSearchActivity){
                ((DoDiveSearchActivity)context).setSearchResult(searchResult);
            }

        }
    }



}
