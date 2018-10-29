package com.nyelam.android.inbox;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nyelam.android.R;

import java.util.ArrayList;

public class ContactUsSpinnerAdapter extends ArrayAdapter<String> {
    private Activity _activity;
    private ArrayList<String> _data;

    public ContactUsSpinnerAdapter(Activity activity, int textViewResourceId, ArrayList<String> data) {
        super(activity, textViewResourceId, data);
        this._activity = activity;
        this._data = data;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = _activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_contact_us_row, parent, false);
        TextView label = (TextView) row.findViewById(R.id.title);
        label.setText(_data.get(position));
        return row;
    }
}
