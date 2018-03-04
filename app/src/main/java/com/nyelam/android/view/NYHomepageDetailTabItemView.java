package com.nyelam.android.view;

import android.app.Activity;
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
import com.nyelam.android.diveservice.DetailServiceActivity;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public class NYHomepageDetailTabItemView extends FrameLayout implements Checkable {
    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked,
    };

    //private DetailServiceActivity detailServiceActivity;
    private Activity activity;
    private boolean checked;
    private int tabItemPosition = -1;
    private TextView textView;
    private ImageView imageView;
    private View lineView;
    private int normalColor;
    private int normalColorWhite;
    private int checkedColor;


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

        if (activity != null) {
            if (activity instanceof DetailServiceActivity){
                int checkedTabItemPos = ((DetailServiceActivity)activity).onCheckedChanged(this, this.checked);
                if (checkedTabItemPos > -1)
                    ((DetailServiceActivity)activity).movePagerToTabItemPosition(checkedTabItemPos);
            }
        }

        if (tabItemPosition == 0 ){
            imageView.setImageResource(R.drawable.tab_home);
        } else if (tabItemPosition == 1 ){
            imageView.setImageResource(R.drawable.tab_home);
        } else if (tabItemPosition == 2 ){
            imageView.setImageResource(R.drawable.tab_home);
        } else {
            imageView.setImageResource(R.drawable.tab_home);
        }

        if (!checked) {
            textView.setTextColor(normalColor);
            lineView.setBackgroundColor(normalColorWhite);
        } else {
            textView.setTextColor(checkedColor);
            lineView.setBackgroundColor(checkedColor);
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

    public void setDetailServiceActivity(Activity activity) {
        this.activity = activity;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttrs) {
        View.inflate(context, R.layout.view_detail_tab_item, this);

        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);
        lineView = (View) findViewById(R.id.line_view);

        textView.setVisibility(VISIBLE);
        imageView.setVisibility(GONE);
        lineView.setVisibility(VISIBLE);

        normalColor = ContextCompat.getColor(context, R.color.colorWhite);
        //normalColorWhite = ContextCompat.getColor(context, R.color.colorWhite);
        normalColorWhite = ContextCompat.getColor(context, android.R.color.transparent);
        checkedColor = ContextCompat.getColor(context, R.color.ny_yellowActive);

        setClickable(true);
        setBackgroundResource(android.R.color.transparent);

        String text = "";
        Drawable icon = null;

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.NYHomepageTabItemView,
                    defStyleAttrs, 0);
            try {
                tabItemPosition = a.getInt(R.styleable.NYHomepageTabItemView_tabItemPosition, -1);
                text = a.getString(R.styleable.NYHomepageTabItemView_tabItemText);
                icon = a.getDrawable(R.styleable.NYHomepageTabItemView_tabSrcIcon);
                checked = a.getBoolean(R.styleable.NYHomepageTabItemView_tabChecked, false);

            } finally {
                a.recycle();
            }
        }

        textView.setText(text);
        imageView.setImageDrawable(icon);
        refreshDrawableState();

        if (activity != null && activity instanceof DetailServiceActivity) {
            int checkedTabItemPos = ((DetailServiceActivity)activity).onCheckedChanged(this, this.checked);
            if (checkedTabItemPos > -1)
                ((DetailServiceActivity)activity).movePagerToTabItemPosition(checkedTabItemPos);
        }

        textView.setText(text);

        if (getTabItemPosition() == 0){
            this.setChecked(true);
        }

    }
}