package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class Variations implements Parseable {

    private static String KEY_COLOR = "Color";
    private static String KEY_SIZE = "Size";

    private List<Variation> colors;
    private List<Variation> sizes;

    public List<Variation> getColors() {
        return colors;
    }

    public void setColors(List<Variation> colors) {
        this.colors = colors;
    }

    public List<Variation> getSizes() {
        if (sizes == null) sizes = new ArrayList<>();
        return sizes;
    }

    public void setSizes(List<Variation> sizes) {
        this.sizes = sizes;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        if (!obj.isNull(KEY_COLOR)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_COLOR);
                if (array != null && array.length() > 0) {
                    colors = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Variation a = new Variation();
                        a.parse(o);
                        colors.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }

        if (!obj.isNull(KEY_SIZE)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_SIZE);
                if (array != null && array.length() > 0) {
                    sizes = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Variation a = new Variation();
                        a.parse(o);
                        sizes.add(a);
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }


    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        if(colors != null && !colors.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(Variation a : colors) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_COLOR, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(sizes != null && !sizes.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(Variation a : sizes) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_SIZE, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}
