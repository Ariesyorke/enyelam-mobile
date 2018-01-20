package com.nyelam.android.detail;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveSpot;

import java.util.List;

/**
 * Created by bobi on 1/20/18.
 */

public class DiveSpotPickerAdapter extends BaseAdapter {
    private List<DiveSpot> diveSpots;
    private int selectionPosition = 0;
    private Context context;

    public DiveSpotPickerAdapter(Context context, List<DiveSpot> diveSpots) {
        this.context = context;
        this.diveSpots = diveSpots;
    }

    @Override
    public int getCount() {
        return diveSpots != null ? diveSpots.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return diveSpots.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setSelection(int position) {
        selectionPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        return createDiveSpotPickerView(position, view);
    }

    private View createDiveSpotPickerView(int position, View view) {
        if(view == null) {
            view = View.inflate(context, R.layout.view_item_dive_spot_picker, null);
        }
        if (position == selectionPosition) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
        DiveSpot diveSpot = diveSpots.get(position);
        ImageView iconImageView = (ImageView) view.findViewById(R.id.icon_imageView);
        TextView nameTextView = (TextView) view.findViewById(R.id.name_textView);
        TextView locationTextView = (TextView) view.findViewById(R.id.location_textView);
        TextView ratingTextView = (TextView) view.findViewById(R.id.rating_textView);

        if (diveSpot.getName() != null && !TextUtils.isEmpty(diveSpot.getName())) nameTextView.setText(diveSpot.getName());
        if (diveSpot.getLocation() != null) locationTextView.setText(diveSpot.getLocation().getCity().getName()+", "+diveSpot.getLocation().getProvince().getName()+", "+diveSpot.getLocation().getCountry());
        ratingTextView.setText("*"+diveSpot.getRating());
        return view;
    }
}
