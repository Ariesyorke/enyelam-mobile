package com.nyelam.android.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;


import com.nyelam.android.R;
import com.nyelam.android.data.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 3/22/2018.
 */

public class ParticipantsSearchAdapter extends ArrayAdapter<Participant> {

    private Context context;
    private List<Participant> items, tempItems, suggestions;

    public ParticipantsSearchAdapter(Context context, int textViewResourceId, List<Participant> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
        if (items != null && items.size() > 0)tempItems = new ArrayList<Participant>(items); // this makes the difference.
        suggestions = new ArrayList<Participant>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_item_participant, parent, false);
        }
        Participant participant = items.get(position);
        if (parent != null) {
            TextView nameTv = (TextView) view.findViewById(R.id.name_textView);
            TextView emailTv = (TextView) view.findViewById(R.id.email_textView);
            if (nameTv != null)
                nameTv.setText(participant.getName());
            if (emailTv != null)
                emailTv.setText(participant.getEmail());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Participant) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Participant participant : tempItems) {
                    if (participant.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(participant);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Participant> filterList = (ArrayList<Participant>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Participant people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };



}
