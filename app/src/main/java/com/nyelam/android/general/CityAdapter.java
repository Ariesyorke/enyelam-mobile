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
import com.nyelam.android.data.City;
import com.nyelam.android.data.Province;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/10/2018.
 */

public class CityAdapter extends BaseAdapter implements SpinnerAdapter {
    private List<City> cities;
    private Activity context;
    private int color = Color.BLACK;
    private int selectedPosition = 0;

    public CityAdapter(Activity context) {
        this.context = context;
    }

    public List<City> getCities() {
        return cities;
    }

    public CityAdapter(Activity context, int color) {
        this.context = context;
        this.color = color;
    }
    public void addCities(List<City> cities) {
        if (this.cities == null) {
            this.cities = new ArrayList<>();
        }
        this.cities.addAll(cities);
    }
    public void clear(){
        this.cities = new ArrayList<>();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(this.cities != null && !cities.isEmpty()) {
            count += this.cities.size();
        }
        return count;
    }


    @Override
    public City getItem(int i) {
        if(cities != null && !cities.isEmpty()) {
            return cities.get(i);
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


        City city = cities.get(position);
        if (city != null){
            if (NYHelper.isStringNotEmpty(city.getName()) && NYHelper.isStringNotEmpty(city.getId())){
                tvName.setText(cities.get(position).getName());
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