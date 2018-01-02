package com.danzoye.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

/**
 * Created by Ramdhany Dwi Nugroho on Jun 2015.
 */
public class SoftKeyboardHandledRelativeLayout extends RelativeLayout {
    private boolean isKeyboardShown;
    private SoftKeyboardVisibilityChangeListener listener;

    public SoftKeyboardHandledRelativeLayout(Context context) {
        super(context);
    }

    public SoftKeyboardHandledRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftKeyboardHandledRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public SoftKeyboardHandledRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // Keyboard is hidden <<< RIGHT
            if (isKeyboardShown) {
                isKeyboardShown = false;
                listener.onSoftKeyboardHide();
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void setOnSoftKeyboardVisibilityChangeListener(SoftKeyboardVisibilityChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();
        if (actualHeight > proposedheight) {
            // Keyboard is shown
            if (!isKeyboardShown) {
                isKeyboardShown = true;
                listener.onSoftKeyboardShow();
            }
        } else {
            // Keyboard is hidden <<< this doesn't work sometimes, so I don't use it
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
