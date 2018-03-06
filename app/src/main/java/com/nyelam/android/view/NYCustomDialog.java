package com.nyelam.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dodive.DoDiveFragment;
import com.nyelam.android.dodive.DoDiveSearchResultActivity;
import com.nyelam.android.helper.NYHelper;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/1/2018.
 */

public class NYCustomDialog {

    private Dialog dialog;
    private OnDialogFragmentClickListener listener;

    // interface to handle the dialog click back to the Activity
    public interface OnDialogFragmentClickListener {
        void onChooseListener(Object position);
        void onAcceptAgreementListener();
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

                listener.onChooseListener((Integer) index);

                dialog.dismiss();
            }

        });

        dialog.show();

    }


    public void showAgreementDialog(final Activity activity){

        listener = (OnDialogFragmentClickListener) activity;

        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_agreement);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.dialog_linearLayout);
        linearLayout.setMinimumWidth(width*3/4);

        ImageView closeImageView = (ImageView) dialog.findViewById(R.id.close_imageView);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final LinearLayout checkBoxLinearLayout = (LinearLayout) dialog.findViewById(R.id.checkBox_linearLayout);
        checkBoxLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });


        TextView acceptTextView = (TextView) dialog.findViewById(R.id.accept_textView);

        String agreementText = "<ol type=\"a\">\n" +
                " <br/> <li>Jangan buang sampah sembarangan.</li>\n" +
                " <br/> <li>Hindari menggunakan kemasan kecil seperti sabun sachet.</li>\n" +
                " <br/> <li>Gunakan krim kulit, Sunblock, dan semprotan anti-serangga yang alami dan tidak beracun.</li>\n" +
                " <br/> <li>Membawa botol isi ulang.</li>\n" +
                " <br/> <li>Membawa tas yang dapat digunakan kembali.</li>\n" +
                " <br/> <li>Kurangi pemakaian material yang sekali pakai.</li>\n" +
                " <br/> <li>Jangan memakan spesies yang terancam, hampir punah atau yang dilindungi.</li>\n" +
                " <br/> <li>Hindari membawa dan memakai produk yang mengandung microbeads (seperti sabun yang mengandung scrubs).</li>\n" +
                " <br/> <li>Melatih keseimbangan untuk mencegah menabrak karang.</li>\n" +
                " <br/> <li>Jangan menginjak atau menyentuh karang.</li>\n" +
                " <br/> <li>Hindari mengaduk sedimen/pasir.</li>\n" +
                " <br/> <li>Yakinkan semua peralatan menyelam aman dan tidak terseret.</li>\n" +
                " <br/> <li>Jangan memberi makan, menangkap, menyentuh, mengganggu atau menangkap biota laut.</li>\n" +
                " <br/> <li>Jangan mengoleksi biota laut apapun, bisa saja itu ilegal.</li>\n" +
                " <br/> <li>Laporkan aktivitas ilegal/tidak sepantasnya terhadap lingkungan kepada otoritas setempat.</li>\n" +
                " <br/> <li>Dukung wisata ramah lingkungan.</li>\n" +
                " <br/> <li>Jangan ambil sampah laut yang telah ditumbuhi karang.</li>\n" +
                " <br/> <li>Jangan ragu untuk mengedukasi atau menegur orang lain terkait pelanggaran etika ini.</li>\n" +
                "</ol>";


        HtmlTextView htmlTextView = (HtmlTextView) dialog.findViewById(R.id.agreementTextView);

        // loads html from string and displays http://www.example.com/cat_pic.png from the Internet
        htmlTextView.setHtml(agreementText,
                new HtmlHttpImageGetter(htmlTextView));


        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            acceptTextView.setText(Html.fromHtml(agreementText,Html.FROM_HTML_MODE_LEGACY));
        } else {
            acceptTextView.setText(Html.fromHtml(agreementText));
        }*/


        acceptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked()){
                    listener.onAcceptAgreementListener();
                    dialog.dismiss();
                } else{
                    Toast.makeText(activity, activity.getString(R.string.warn_check_agreement_to_continue), Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();

    }



    public void showTotalDiverDialog(final Activity activity){

        this.listener = (OnDialogFragmentClickListener) activity;

        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_choose_diver);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.dialog_linearLayout);
        linearLayout.setMinimumWidth(width*3/4);

        LinearLayout containerLinearLayout = (LinearLayout) dialog.findViewById(R.id.container_linaerLayout);
        for (int i=1; i<=10; i++){

            LayoutInflater inflaterAddons = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View diverView = inflaterAddons.inflate(R.layout.view_drop_down_country_code, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsAddons.setMargins(0, NYHelper.integerToDP(activity, 5), 0, NYHelper.integerToDP(activity, 5));
            diverView.setLayoutParams(layoutParamsAddons);

            LinearLayout mainLinearLayout = (LinearLayout) diverView.findViewById(R.id.main_linearLayout);
            TextView diverTextView = (TextView) diverView.findViewById(R.id.country_code_textView);
            View lineView = (View) diverView.findViewById(R.id.line_view);

            lineView.setVisibility(View.VISIBLE);
            diverTextView.setGravity(Gravity.CENTER);

            diverTextView.setText(String.valueOf(i));


            final String div = String.valueOf(i);

            mainLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChooseListener(String.valueOf(div));
                    dialog.dismiss();
                    //Toast.makeText(activity, "hallo "+div, Toast.LENGTH_SHORT).show();
                }
            });

            containerLinearLayout.addView(diverView);
        }

        dialog.show();

    }



}