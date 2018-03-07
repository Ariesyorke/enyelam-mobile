package com.nyelam.android.profile;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.helper.NYHelper;

/**
 * Created by bobi on 3/7/18.
 */

public class NYGenderSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private static final String[] GENDER_TYPES = {
            "Male",
            "Female"
    };
    private Activity context;
    private int selectedPosition;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public NYGenderSpinnerAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        int count = 2;
        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            view = View.inflate(context, R.layout.view_gender, null);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return onCreateDropDownCountryCode(position, convertView);
    }

    private View onCreateDropDownCountryCode(int position, View view) {
        if (view == null) {
            view = View.inflate(context, R.layout.view_drop_down_gender, null);
        }
        LinearLayout mainLinearLayout = (LinearLayout) view.findViewById(R.id.main_linearLayout);
        TextView genderTextVIew = (TextView) view.findViewById(R.id.gender_textView);
        //TextView countryNumberTextView = (TextView)view.findViewById(R.id.country_number_textView);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        genderTextVIew.setWidth(width * 4 / 6);

        String gender = GENDER_TYPES[position];
        genderTextVIew.setText(gender);

        if (position == selectedPosition) {
            mainLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.ny_grey1));
        } else {
            mainLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        return view;
    }
}
