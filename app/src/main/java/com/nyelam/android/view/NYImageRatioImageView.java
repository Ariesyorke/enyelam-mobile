package com.nyelam.android.view;

import android.content.Context;
import android.util.AttributeSet;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class NYImageRatioImageView extends android.support.v7.widget.AppCompatImageView {
    private int[] RATIO;
    public NYImageRatioImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NYImageRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NYImageRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
