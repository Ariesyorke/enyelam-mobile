package com.nyelam.android.data;

import com.nyelam.android.data.NYAbstractList;
import com.nyelam.android.data.Review;
import com.nyelam.android.data.Summary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class ReviewList implements Parseable {
    private static final String KEY_NEXT = "next";
    private static final String KEY_DATA = "reviews";

    private String next;
    private List<Review> reviewList;

    public ReviewList(){

    }

    public ReviewList(String next, List<Review> reviewList){
        this.next = next;
        this.reviewList = reviewList;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {

            if (!obj.isNull(KEY_NEXT)) {
                setNext(obj.getString(KEY_NEXT));
            }

            if (!obj.isNull(KEY_DATA)) {
                JSONArray array = obj.getJSONArray(KEY_DATA);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(reviewList == null) {
                            reviewList = new ArrayList<>();
                        }
                        JSONObject o = array.getJSONObject(i);
                        Review review = new Review();
                        review.parse(o);
                        reviewList.add(review);
                    }
                }
            }

        } catch (JSONException e) {e.printStackTrace();}
    }
}