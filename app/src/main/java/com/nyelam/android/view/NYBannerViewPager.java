package com.nyelam.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class NYBannerViewPager extends ViewPager {

    private OnItemClickListener mOnItemClickListener;

    private int[] RATIO;
    public NYBannerViewPager(Context context) {
        super(context);
        setup();
        init(context, null, 0);
    }

    public NYBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
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


    // TODO: tambahkan untuk setOnclick Listener POSITION
    private void setup() {
        final GestureDetector tapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getCurrentItem());
            }
            return true;
        }
    }




}
