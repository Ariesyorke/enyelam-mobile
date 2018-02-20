package com.nyelam.android.view.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/12/2018.
 */

public class NYTextView extends android.support.v7.widget.AppCompatTextView {

    private int textPosition = -1;

    public NYTextView(Context context) {
        super(context);
        /*Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);

        this.setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);*/
    }

    public NYTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.NYTextView,
                    0, 0);
            try {
                textPosition = a.getInt(R.styleable.NYTextView_fontTextStyle, -1);
            } finally {
                a.recycle();
            }
        }

        String fontStyle = "FiraSans_Regular.ttf";

        switch (textPosition)
        {
            case 0:
                fontStyle = "FiraSans_Black.ttf";
                break;
            case 1:
                fontStyle = "FiraSans_BlackItalic.ttf";
                break;
            case 2:
                fontStyle = "FiraSans_Bold.ttf";
                break;
            case 3:
                fontStyle = "FiraSans_BoldItalic.ttf";
                break;
            case 4:
                fontStyle = "FiraSans_ExtraBold.ttf";
                break;
            case 5:
                fontStyle = "FiraSans_ExtraBoldItalic.ttf";
                break;
            case 6:
                fontStyle = "FiraSans_ExtraLight.ttf";
                break;
            case 7:
                fontStyle = "FiraSans_ExtraLightItalic.ttf";
                break;
            case 8:
                fontStyle = "FiraSans_Italic.ttf";
                break;
            case 9:
                fontStyle = "FiraSans_Light.ttf";
                break;
            case 10:
                fontStyle = "FiraSans_LightItalic.ttf";
                break;
            case 11:
                fontStyle = "FiraSans_Medium.ttf";
                break;
            case 12:
                fontStyle = "FiraSans_MediumItalic.ttf";
                break;
            case 13:
                fontStyle = "FiraSans_Regular.ttf";
                break;
            case 14:
                fontStyle = "FiraSans_SemiBold.ttf";
                break;
            case 15:
                fontStyle = "FiraSans_SemiBoldItalic.ttf";
                break;
            case 16:
                fontStyle = "FiraSans_Thin.ttf";
                break;
            case 17:
                fontStyle = "FiraSans_ThinItalic.ttf";
                break;
            default:
                break;
        }

        Typeface face= Typeface.createFromAsset(context.getAssets(), "fonts/"+fontStyle);
        this.setTypeface(face);
    }

    public NYTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.NYTextView,
                    defStyle, 0);
            try {
                textPosition = a.getInt(R.styleable.NYTextView_fontTextStyle, -1);
            } finally {
                a.recycle();
            }
        }

        String fontStyle = "FiraSans_Regular.ttf";

        switch (textPosition)
        {
            case 0:
                fontStyle = "FiraSans_Black.ttf";
                break;
            case 1:
                fontStyle = "FiraSans_BlackItalic.ttf";
                break;
            case 2:
                fontStyle = "FiraSans_Bold.ttf";
                break;
            case 3:
                fontStyle = "FiraSans_BoldItalic.ttf";
                break;
            case 4:
                fontStyle = "FiraSans_ExtraBold.ttf";
                break;
            case 5:
                fontStyle = "FiraSans_ExtraBoldItalic.ttf";
                break;
            case 6:
                fontStyle = "FiraSans_ExtraLight.ttf";
                break;
            case 7:
                fontStyle = "FiraSans_ExtraLightItalic.ttf";
                break;
            case 8:
                fontStyle = "FiraSans_Italic.ttf";
                break;
            case 9:
                fontStyle = "FiraSans_Light.ttf";
                break;
            case 10:
                fontStyle = "FiraSans_LightItalic.ttf";
                break;
            case 11:
                fontStyle = "FiraSans_Medium.ttf";
                break;
            case 12:
                fontStyle = "FiraSans_MediumItalic.ttf";
                break;
            case 13:
                fontStyle = "FiraSans_Regular.ttf";
                break;
            case 14:
                fontStyle = "FiraSans_SemiBold.ttf";
                break;
            case 15:
                fontStyle = "FiraSans_SemiBoldItalic.ttf";
                break;
            case 16:
                fontStyle = "FiraSans_Thin.ttf";
                break;
            case 17:
                fontStyle = "FiraSans_ThinItalic.ttf";
                break;
            default:
                break;
        }

        Typeface face= Typeface.createFromAsset(context.getAssets(), "fonts/"+fontStyle);
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}