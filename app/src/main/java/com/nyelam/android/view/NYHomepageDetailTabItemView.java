package com.nyelam.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.detail.DetailServiceActivity;
import com.nyelam.android.home.HomeActivity;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYHomepageDetailTabItemView extends FrameLayout implements Checkable {
    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked,
    };

    private DetailServiceActivity detailServiceActivity;
    private boolean checked;
    private int tabItemPosition = -1;
    private TextView textView;
    private ImageView imageView;

    public NYHomepageDetailTabItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NYHomepageDetailTabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NYHomepageDetailTabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @Override
    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        refreshDrawableState();

        if (detailServiceActivity != null) {
            int checkedTabItemPos = detailServiceActivity.onCheckedChanged(this, this.checked);
            if (checkedTabItemPos > -1)
                detailServiceActivity.movePagerToTabItemPosition(checkedTabItemPos);
        }

        if (tabItemPosition == 0 ){
            imageView.setImageResource(R.drawable.tab_home);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.ny_grey2));
        } else if (tabItemPosition == 1 ){
            imageView.setImageResource(R.drawable.tab_home);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.ny_grey2));
        } else if (tabItemPosition == 2 ){
            imageView.setImageResource(R.drawable.tab_home);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.ny_grey2));
        } else {
            imageView.setImageResource(R.drawable.tab_home);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.ny_grey2));
        }

    }

    @Override
    public boolean performClick() {
        if (!isChecked()) {
            toggle();
        }
        return super.performClick();
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.ny_green_blue));
        } else {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.ny_grey2));
        }
        return drawableState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    public int getTabItemPosition() {
        return tabItemPosition;
    }

    public void setDetailServiceActivity(DetailServiceActivity detailServiceActivity) {
        this.detailServiceActivity = detailServiceActivity;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttrs) {
        View.inflate(context, R.layout.view_homepage_tab_item, this);

        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);

        textView.setVisibility(VISIBLE);
        imageView.setVisibility(GONE);

        setClickable(true);
        setBackgroundResource(R.drawable.homepage_tab_item_selector);

        String text = "";
        Drawable icon = null;

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.NYHomepageTabItemView,
                    defStyleAttrs, 0);
            try {
                tabItemPosition = a.getInt(R.styleable.NYHomepageTabItemView_tabItemPosition, -1);
                text = a.getString(R.styleable.NYHomepageTabItemView_tabChecked);
                icon = a.getDrawable(R.styleable.NYHomepageTabItemView_tabSrcIcon);
                checked = a.getBoolean(R.styleable.NYHomepageTabItemView_tabChecked, false);

            } finally {
                a.recycle();
            }
        }


        textView.setText(text);
        imageView.setImageDrawable(icon);
        refreshDrawableState();

        if (detailServiceActivity != null) {
            int checkedTabItemPos = detailServiceActivity.onCheckedChanged(this, this.checked);
            if (checkedTabItemPos > -1)
                detailServiceActivity.movePagerToTabItemPosition(checkedTabItemPos);
        }
    }
}