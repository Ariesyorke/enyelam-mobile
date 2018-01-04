package com.nyelam.android.auth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nyelam.android.R;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class GenderAdapter extends ArrayAdapter<String> {

    private List<String> genders;
    private Context context;

    public GenderAdapter(Context context, int resourceId,
                              List<String> genders) {
        super(context, resourceId, genders);
        this.genders = genders;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.spinner_gender, parent, false);
        TextView label=(TextView)row.findViewById(R.id.gender_textView);
        label.setText(genders.get(position));

        /*if (position == 0) {//Special style for dropdown header
            label.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }*/

        return row;
    }

}