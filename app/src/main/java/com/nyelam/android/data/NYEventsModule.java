package com.nyelam.android.data;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonic on 15/01/2018.
 */

public class NYEventsModule extends NYModule {
    private static final String KEY_PRODUCTS = "product";
    private static final String KEY_POSITION = "position";

    private List<Event> events;
    private int position;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void parse(JSONObject obj) {
        try {
            if(!obj.isNull(KEY_PRODUCTS)) {
                JSONArray array = obj.getJSONArray(KEY_PRODUCTS);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);

                    Event event = new Event();
                    event.parse(o);
                    if (events == null) {
                        events = new ArrayList<>();
                    }
                    events.add(event);

                }
            }

            if(!obj.isNull(KEY_POSITION)) {
                try {
                    setPosition(obj.getInt(KEY_POSITION));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
