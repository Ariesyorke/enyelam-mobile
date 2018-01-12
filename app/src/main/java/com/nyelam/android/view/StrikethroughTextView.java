package com.nyelam.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/12/2018.
 */

public class StrikethroughTextView extends android.support.v7.widget.AppCompatTextView {


    public StrikethroughTextView(Context context) {
        super(context);
        /*Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);*/

        this.setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public StrikethroughTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);*/

        this.setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public StrikethroughTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /*Typeface face= Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);*/

        this.setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}