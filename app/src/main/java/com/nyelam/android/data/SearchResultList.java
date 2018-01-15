package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class SearchResultList extends NYAbstractList<SearchResult> {
    @Override
    public Class<SearchResult> getHandledClass() {
        return SearchResult.class;
    }

    @Override
    public void parse(JSONArray array) throws JSONException {
        setList(new ArrayList<SearchResult>());
        for(int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            int type = o.getInt("type");
            SearchResult result = null;
            if (type == 1) {
                result = new SearchSpot();
                result.parse(o);
            } else if (type == 2 || type == 4 || type == 5 || type == 6){
                result = new SearchService();
                result.parse(o);
            } else if (type == 3){
                result = new SearchDiveCenter();
                result.parse(o);
            }

            getList().add(result);
        }
    }
}