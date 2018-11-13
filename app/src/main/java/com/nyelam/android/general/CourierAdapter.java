package com.nyelam.android.general;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.Area;
import com.nyelam.android.data.Courier;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/10/2018.
 */

public class CourierAdapter extends BaseAdapter implements SpinnerAdapter {
    private List<Courier> couriers;
    private Activity context;
    private int color = Color.BLACK;
    private int selectedPosition = 0;

    public CourierAdapter(Activity context) {
        this.context = context;
    }

    public List<Courier> getCouriers() {
        return couriers;
    }

    public CourierAdapter(Activity context, int color) {
        this.context = context;
        this.color = color;
    }
    public void addCouriers(List<Courier> couriers) {
        if (this.couriers == null) {
            this.couriers = new ArrayList<>();
        }
        this.couriers.addAll(couriers);
    }
    public void clear(){
        this.couriers = new ArrayList<>();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(this.couriers != null && !couriers.isEmpty()) {
            count += this.couriers.size();
        }
        return count;
    }


    @Override
    public Courier getItem(int i) {
        if(couriers != null && !couriers.isEmpty()) {
            return couriers.get(i);
        }
        return null;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return onCreateCodeView(position, view);
    }

    private View onCreateCodeView(int position, View view) {
        if(view == null) {
            view = View.inflate(context, R.layout.view_country_code, null);
        }
        TextView tvName = (TextView) view.findViewById(R.id.country_number_textView);
        tvName.setTextColor(color);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return onCreateDropDownCode(position, convertView);
    }

    private View onCreateDropDownCode(int position, View view) {
        if (view == null) {
            view = View.inflate(context, R.layout.view_drop_down_country_code, null);
        }
        LinearLayout mainLinearLayout = (LinearLayout) view.findViewById(R.id.main_linearLayout);
        TextView tvName = (TextView)view.findViewById(R.id.country_code_textView);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        tvName.setWidth(width*4/6);


        Courier courier = couriers.get(position);
        if (courier != null){
            if (NYHelper.isStringNotEmpty(courier.getCode())){
                tvName.setText(courier.getCode().toUpperCase());
            }
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