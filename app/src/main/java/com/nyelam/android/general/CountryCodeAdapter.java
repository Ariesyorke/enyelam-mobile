package com.nyelam.android.general;

import android.app.Activity;
import android.content.Context;
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
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/10/2018.
 */

public class CountryCodeAdapter extends BaseAdapter implements SpinnerAdapter {
    private List<CountryCode> countryCodes;
    private Activity context;
    private int color = Color.BLACK;
    private int selectedPosition = 0;

    public CountryCodeAdapter(Activity context) {
        this.context = context;
    }

    public List<CountryCode> getCountryCodes() {
        return countryCodes;
    }

    public CountryCodeAdapter(Activity context, int color) {
        this.context = context;
        this.color = color;
    }
    public void addCountryCodes(List<CountryCode> countryCodes) {
        if (this.countryCodes == null) {
            this.countryCodes = new ArrayList<>();
        }
        this.countryCodes.addAll(countryCodes);
    }
    public void clear(){
        this.countryCodes = new ArrayList<>();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(this.countryCodes != null && !countryCodes.isEmpty()) {
            count += this.countryCodes.size();
        }
        return count;
    }


    @Override
    public Object getItem(int i) {
        if(countryCodes != null && !countryCodes.isEmpty()) {
            return countryCodes.get(i);
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
        return onCreateCountryCodeView(position, view);
    }

    private View onCreateCountryCodeView(int position, View view) {
        if(view == null) {
            view = View.inflate(context, R.layout.view_country_code, null);
        }
        TextView countryNumberTextView = (TextView) view.findViewById(R.id.country_number_textView);
//        countryNumberTextView.setText(countryCodes.get(position).getCountryNumber());
        countryNumberTextView.setTextColor(color);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return onCreateDropDownCountryCode(position, convertView);
    }

    private View onCreateDropDownCountryCode(int position, View view) {
        if (view == null) {
            view = View.inflate(context, R.layout.view_drop_down_country_code, null);
        }
        LinearLayout mainLinearLayout = (LinearLayout) view.findViewById(R.id.main_linearLayout);
        TextView countryCodeTextView = (TextView)view.findViewById(R.id.country_code_textView);
        //TextView countryNumberTextView = (TextView)view.findViewById(R.id.country_number_textView);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        countryCodeTextView.setWidth(width*4/6);


        CountryCode countryCode = countryCodes.get(position);
        if (countryCode != null){
            if (NYHelper.isStringNotEmpty(countryCode.getCountryName()) && NYHelper.isStringNotEmpty(countryCode.getId())){
                countryCodeTextView.setText(countryCodes.get(position).getCountryName() + " "+"(+ " + countryCodes.get(position).getCountryNumber() +")");
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