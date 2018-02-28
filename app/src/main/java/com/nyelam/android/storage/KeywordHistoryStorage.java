package com.nyelam.android.storage;

import android.content.Context;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.SearchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dantech on 8/11/2017.
 */

public class KeywordHistoryStorage extends AbstractStorage {
    private static final String FILENAME = "nyelam:storage:modul-homepage";
    private static final String KEY_KEYWORD_HISTORY= "keyword_history";

    public List<SearchResult> searchResults;

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public KeywordHistoryStorage(Context context) {
        super(context);
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        try {
            if (!obj.isNull(KEY_KEYWORD_HISTORY)) {
                JSONArray array = obj.getJSONArray(KEY_KEYWORD_HISTORY);
                if (array != null && array.length() > 0) {
                    searchResults = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        SearchResult searchResult = new SearchResult() {
                            @Override
                            public String getId() {
                                return super.getId();
                            }

                            @Override
                            public void setId(String id) {
                                super.setId(id);
                            }

                            @Override
                            public String getName() {
                                return super.getName();
                            }

                            @Override
                            public void setName(String name) {
                                super.setName(name);
                            }

                            @Override
                            public String getRating() {
                                return super.getRating();
                            }

                            @Override
                            public void setRating(String rating) {
                                super.setRating(rating);
                            }

                            @Override
                            public Integer getType() {
                                return super.getType();
                            }

                            @Override
                            public void setType(Integer type) {
                                super.setType(type);
                            }

                            @Override
                            public Integer getCount() {
                                return super.getCount();
                            }

                            @Override
                            public void setCount(Integer count) {
                                super.setCount(count);
                            }

                            @Override
                            public void parse(JSONObject obj) {
                                super.parse(obj);
                            }

                            @Override
                            public String toString() {
                                return super.toString();
                            }

                            @Override
                            public int hashCode() {
                                return super.hashCode();
                            }

                            @Override
                            public boolean equals(Object obj) {
                                return super.equals(obj);
                            }

                            @Override
                            protected Object clone() throws CloneNotSupportedException {
                                return super.clone();
                            }

                            @Override
                            protected void finalize() throws Throwable {
                                super.finalize();
                            }
                        };
                        searchResult.parse(o);
                        searchResults.add(searchResult);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();

        try {
            if (searchResults != null) {
                JSONArray array = new JSONArray();
                for (SearchResult a : getSearchResults()) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_KEYWORD_HISTORY, array);
            } else  {
                obj.put(KEY_KEYWORD_HISTORY, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        return obj;
    }
}