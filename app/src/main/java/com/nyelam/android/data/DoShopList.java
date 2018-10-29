package com.nyelam.android.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCategory;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoShopList implements Parseable {

    private static final String KEY_CATEGORIES = "categories";
    private static final String KEY_RECOMMENDED = "recomended";

    private List<DoShopCategory> categories;
    private List<DoShopProduct> recommended;

    public DoShopList(){

    }

    public DoShopList(List<DoShopCategory> categories, List<DoShopProduct> recommended){
        this.categories = categories;
        this.recommended = recommended;
    }

    public List<DoShopCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DoShopCategory> categories) {
        this.categories = categories;
    }

    public List<DoShopProduct> getRecommended() {
        return recommended;
    }

    public void setRecommended(List<DoShopProduct> recommended) {
        this.recommended = recommended;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_CATEGORIES)) {
                JSONArray array = obj.getJSONArray(KEY_CATEGORIES);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(categories == null) {
                            categories = new ArrayList<>();
                        }
                        JSONObject o = array.getJSONObject(i);
                        DoShopCategory category = new DoShopCategory();
                        category.parse(o);
                        categories.add(category);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_RECOMMENDED)) {
                JSONArray array = obj.getJSONArray(KEY_RECOMMENDED);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(recommended == null) {
                            recommended = new ArrayList<>();
                        }
                        JSONObject o = array.getJSONObject(i);
                        DoShopProduct product = new DoShopProduct();
                        product.parse(o);
                        recommended.add(product);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
    }

}