package com.nyelam.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.home.HomeActivity;


/**
 * Created by Aprilian Nur on 12/6/2017.
 */

public class NYHomepageTabItemView extends FrameLayout implements Checkable {
    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked,
    };

    private HomeActivity mainActivity;
    private boolean checked;
    private int tabItemPosition = -1;
    private TextView textView;
    private ImageView imageView;

    public NYHomepageTabItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NYHomepageTabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NYHomepageTabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        if (mainActivity != null) {
            int checkedTabItemPos = mainActivity.onCheckedChanged(this, this.checked);
            if (checkedTabItemPos > -1)
                mainActivity.movePagerToTabItemPosition(checkedTabItemPos);
        }

        if (tabItemPosition == 0 ){
            imageView.setImageResource(R.drawable.tab_home);
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.navigation_text_color));
        } else if (tabItemPosition == 1 ){
            imageView.setImageResource(R.drawable.tab_social);
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.navigation_text_color));
        } else if (tabItemPosition == 2 ){
            imageView.setImageResource(R.drawable.tab_cart);
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.navigation_text_color));
        } else {
            imageView.setImageResource(R.drawable.tab_user);
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.navigation_text_color));
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
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        } else {
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_2));
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

    public void setMainActivity(HomeActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttrs) {
        View.inflate(context, R.layout.view_homepage_tab_item, this);

        setClickable(true);

        setBackgroundResource(R.drawable.homepage_tab_item_selector);

        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);

        textView.setVisibility(GONE);

        String text = null;
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

        if (mainActivity != null) {
            int checkedTabItemPos = mainActivity.onCheckedChanged(this, this.checked);
            if (checkedTabItemPos > -1)
                mainActivity.movePagerToTabItemPosition(checkedTabItemPos);
        }
    }
}