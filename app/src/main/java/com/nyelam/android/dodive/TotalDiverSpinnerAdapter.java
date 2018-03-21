package com.nyelam.android.dodive;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/10/2018.
 */

public class TotalDiverSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private List<String> diverList;
    private Activity context;
    private int color = Color.BLACK;
    private int selectedPosition = 0;

    public TotalDiverSpinnerAdapter(Activity context) {
        this.context = context;
    }

    public List<String> getDivers() {
        return diverList;
    }

    public TotalDiverSpinnerAdapter(Activity context, int color) {
        this.context = context;
        this.color = color;
    }
    public void addDivers(List<String> divers) {
        if (this.diverList == null) {
            this.diverList = new ArrayList<>();
        }
        this.diverList.addAll(divers);
    }
    public void clear(){
        this.diverList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(this.diverList != null && !diverList.isEmpty()) {
            count += this.diverList.size();
        }
        return count;
    }


    @Override
    public Object getItem(int i) {
        if(diverList != null && !diverList.isEmpty()) {
            return diverList.get(i);
        }
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return onCreateDiverView(position, view);
    }

    private View onCreateDiverView(int position, View view) {
        if(view == null) {
            view = View.inflate(context, R.layout.view_country_code, null);
        }
        TextView diverTextView = (TextView) view.findViewById(R.id.country_number_textView);
        diverTextView.setText(diverList.get(position));
        //countryNumberTextView.setTextColor(color);
        diverTextView.setGravity(Gravity.CENTER);
        diverTextView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return onCreateDropDownDiver(position, convertView);
    }

    private View onCreateDropDownDiver(int position, View view) {
        if (view == null) {
            view = View.inflate(context, R.layout.view_drop_down_country_code, null);
        }
        LinearLayout mainLinearLayout = (LinearLayout) view.findViewById(R.id.main_linearLayout);
        TextView diverTextView = (TextView)view.findViewById(R.id.country_code_textView);
        View lineView = (View)view.findViewById(R.id.line_view);

        lineView.setVisibility(View.VISIBLE);
        diverTextView.setGravity(Gravity.CENTER);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        diverTextView.setWidth(width*4/6);


        String diver = diverList.get(position);
        if (NYHelper.isStringNotEmpty(diver)){
            diverTextView.setText(diverList.get(position));
        }

        if (position == selectedPosition){
            mainLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.ny_grey1));
        } else {
            mainLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        return view;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


}