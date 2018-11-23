package com.nyelam.android.doshop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.Variation;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 11/2/2018.
 */
public class SizeSpinAdapter extends ArrayAdapter<Variation> implements SpinnerAdapter {

    private Activity activity;
    private List<Variation> values;

    public SizeSpinAdapter(Activity activity,
                           List<Variation> values) {
        super(activity, R.layout.spinner_size, values);
        this.activity = activity;
        this.values = values;
    }

    @Override
    public int getCount(){
        if (values == null) values = new ArrayList<>();
        return values.size();
    }

    @Override
    public Variation getItem(int position){
        if (values == null) values = new ArrayList<>();
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View spinView;
        if( convertView == null ){
            LayoutInflater inflater = activity.getLayoutInflater();
            spinView = inflater.inflate(R.layout.spinner_size, null);
        } else {
            spinView = convertView;
        }

        TextView tvTitle = spinView.findViewById(R.id.tv_title);
        if (values.get(position) != null){
            if (NYHelper.isStringNotEmpty(values.get(position).getName()))tvTitle.setText(values.get(position).getName());
        }

        return spinView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View spinView;
        if( convertView == null ){
            LayoutInflater inflater = activity.getLayoutInflater();
            spinView = inflater.inflate(R.layout.spinner_size, null);
        } else {
            spinView = convertView;
        }

        TextView tvTitle = spinView.findViewById(R.id.tv_title);
        if (values.get(position) != null){
            if (NYHelper.isStringNotEmpty(values.get(position).getName()))tvTitle.setText(values.get(position).getName());
        }

        return spinView;
    }
}