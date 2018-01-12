package com.nyelam.android.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/12/2018.
 */

public class NYEditTextWarning extends android.support.v7.widget.AppCompatTextView {

    private Context context;

    public NYEditTextWarning(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (getTag() != null) {
            setTextColor(Color.parseColor(getTag().toString()));
        }
    }



    public static Spannable setErrorSpanText(String message) {
        Spannable textToSpan = new SpannableString(message);
        textToSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return textToSpan;
    }

    public void isEmpty(){
        int ecolor = ContextCompat.getColor(context, R.color.colorPrimary);
        String estring = "Please choose keyword first";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
        this.requestFocus();
        this.setError(ssbuilder);
    }

}
