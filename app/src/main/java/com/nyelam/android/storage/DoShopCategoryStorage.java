package com.nyelam.android.storage;

import android.content.Context;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.data.ModuleList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dantech on 8/11/2017.
 */

public class DoShopCategoryStorage extends AbstractStorage {
    private static final String FILENAME = "nyelam:storage:doshop-categories";
    private static final String KEY_CATEGORIES = "categories";

    private DoShopCategoryList categoryList;

    public DoShopCategoryStorage(Context context) {
        super(context);
    }

    public DoShopCategoryList getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(DoShopCategoryList categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        try {
            if (!obj.isNull(KEY_CATEGORIES)) {
                JSONArray array = obj.getJSONArray(KEY_CATEGORIES);
                categoryList = new DoShopCategoryList();
                categoryList.parse(array);
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();
        try {
            if(categoryList != null && categoryList.getList() != null && !categoryList.getList().isEmpty()) {
                obj.put(KEY_CATEGORIES, categoryList.toJSONArray());
            }
        } catch (JSONException e){e.printStackTrace();}

        return obj;
    }
}