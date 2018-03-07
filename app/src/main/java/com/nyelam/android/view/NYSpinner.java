package com.nyelam.android.view;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by bobi on 3/7/18.
 */

public class NYSpinner extends AppCompatSpinner {
    public NYSpinner(Context context) {
        super(context);
    }

    public NYSpinner(Context context, int mode) {
        super(context, mode);
    }

    public NYSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NYSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NYSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    /**
     * An interface which a client of this Spinner could use to receive
     * open/closed events for this Spinner.
     */
    public interface OnSpinnerEventsListener {

        /**
         * Callback triggered when the spinner was opened.
         */
        void onSpinnerOpened(Spinner spinner);

        /**
         * Callback triggered when the spinner was closed.
         */
        void onSpinnerClosed(Spinner spinner);

    }

    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;

    // implement the Spinner constructors that you need

    @Override
    public boolean performClick() {
        // register that the Spinner was opened so we have a status
        // indicator for when the container holding this Spinner may lose focus
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened(this);
        }
        return super.performClick();
    }

    /**
     * Register the listener which will listen for events.
     */
    public void setSpinnerEventsListener(
            OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    /**
     * Propagate the closed Spinner event to the listener from outside if needed.
     */
    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed(this);
        }
    }

    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }

    public void onWindowFocusChanged (boolean hasFocus) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent();
        }
    }
}
