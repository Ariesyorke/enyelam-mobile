package com.nyelam.android.view;

import android.content.Context;
import android.util.AttributeSet;

import com.nyelam.android.R;

/**
 * Created by bobi on 3/3/18.
 */

public class NYImageRatioHeightImageView extends android.support.v7.widget.AppCompatImageView {
    private int[] RATIO;
    public NYImageRatioHeightImageView(Context context) {
        super(context);
    }

    public NYImageRatioHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NYImageRatioHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int desiredWidthSize = measuredWidthSize * 2/3;
        int desiredWidthSpec = MeasureSpec.makeMeasureSpec(desiredWidthSize,
                MeasureSpec.EXACTLY);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(measuredHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(desiredWidthSpec, desiredHeightSpec);
    }
}
