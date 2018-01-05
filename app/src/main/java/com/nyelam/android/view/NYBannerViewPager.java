package com.nyelam.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class NYBannerViewPager extends ViewPager {

    private int[] RATIO;
    public NYBannerViewPager(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NYBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeightSize = (RATIO[1] * measuredWidthSize) / RATIO[0];

        int desiredWidthSpec = MeasureSpec.makeMeasureSpec(measuredWidthSize,
                MeasureSpec.EXACTLY);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(desiredWidthSpec, desiredHeightSpec);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        RATIO = new int[]{
                context.getResources().getInteger(R.integer.timeline_place_item_image_ratio_width),
                context.getResources().getInteger(R.integer.timeline_place_item_image_ratio_height)
        };
    }

}
