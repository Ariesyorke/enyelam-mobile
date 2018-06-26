package com.nyelam.android.http;

import com.nyelam.android.data.NYAbstractList;
import com.nyelam.android.data.Review;
import com.nyelam.android.data.Summary;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class ReviewList extends NYAbstractList<Review> {
    @Override
    public Class<Review> getHandledClass() {
        return Review.class;
    }
}