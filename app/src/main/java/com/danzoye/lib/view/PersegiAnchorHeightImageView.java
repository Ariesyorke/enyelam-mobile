package com.danzoye.lib.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class PersegiAnchorHeightImageView extends AppCompatImageView {

    public PersegiAnchorHeightImageView(Context context) {
        super(context);
    }

    public PersegiAnchorHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersegiAnchorHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int desiredWidthSize = measuredHeightSize;

        int desiredWidthSpec = MeasureSpec.makeMeasureSpec(desiredWidthSize,
                MeasureSpec.EXACTLY);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(measuredHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(desiredWidthSpec, desiredHeightSpec);
    }
}
