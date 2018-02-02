package com.nyelam.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dodive.DoDiveSearchResultActivity;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/1/2018.
 */

public class NYCustomDialog {

    private Dialog dialog;
    private OnDialogFragmentClickListener listener;

    // interface to handle the dialog click back to the Activity
    public interface OnDialogFragmentClickListener {
        void onChooseListener(int position);
    }

    public void showSortingDialog(final Activity activity, int currentPosition){
        //dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        listener = (OnDialogFragmentClickListener) activity;

        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_sorting);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.dialog_linearLayout);
        linearLayout.setMinimumWidth(width*3/4);

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);

        radioGroup.check(currentPosition);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);

                listener.onChooseListener(index);

                dialog.dismiss();
            }

        });

        dialog.show();

    }



}