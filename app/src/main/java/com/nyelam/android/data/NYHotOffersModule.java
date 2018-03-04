package com.nyelam.android.data;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 3/4/2018.
 */

public class NYHotOffersModule extends NYModule {
    private static final String KEY_PRODUCTS = "product";
    private static final String KEY_POSITION = "position";

    private List<DiveSpot> diveSpots;
    private int position;

    public List<DiveSpot> getProducts() {
        return diveSpots;
    }

    public void setProducts(List<DiveSpot> diveSpots) {
        this.diveSpots = diveSpots;
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

                    DiveSpot diveSpot = new DiveSpot();
                    diveSpot.parse(o);
                    if (diveSpots == null) {
                        diveSpots = new ArrayList<>();
                    }
                    diveSpots.add(diveSpot);
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
