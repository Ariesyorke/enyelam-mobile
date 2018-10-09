package com.nyelam.android.storage;

import android.content.Context;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchResultList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dantech on 8/11/2017.
 */

public class KeywordHistoryStorage extends AbstractStorage {
    private static final String FILENAME = "nyelam:storage:keyword-history";
    private static final String KEY_KEYWORD_HISTORY= "keyword_history";
    private static final String KEY_KEYWORD_HISTORY_DO_COURSE= "keyword_history_do_course";

    private List<SearchResult> searchResults;
    private List<SearchResult> doCourseSearchResults;

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public List<SearchResult> getDoCourseSearchResults() {
        return doCourseSearchResults;
    }

    public void setDoCourseSearchResults(List<SearchResult> doCourseSearchResults) {
        this.doCourseSearchResults = doCourseSearchResults;
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
                    SearchResultList list = new SearchResultList();
                    list.parse(array);
                    searchResults = list.getList();
                }
            }









            if (!obj.isNull(KEY_KEYWORD_HISTORY_DO_COURSE)) {
                JSONArray array = obj.getJSONArray(KEY_KEYWORD_HISTORY_DO_COURSE);
                if (array != null && array.length() > 0) {
                    SearchResultList list = new SearchResultList();
                    list.parse(array);
                    doCourseSearchResults = list.getList();
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



        try {
            if (doCourseSearchResults != null) {
                JSONArray array = new JSONArray();
                for (SearchResult a : getDoCourseSearchResults()) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_KEYWORD_HISTORY_DO_COURSE, array);
            } else  {
                obj.put(KEY_KEYWORD_HISTORY_DO_COURSE, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        return obj;
    }
}