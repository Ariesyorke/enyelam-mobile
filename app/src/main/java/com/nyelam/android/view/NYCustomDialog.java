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


        TextView acceptTextView = (TextView) dialog.findViewById(R.id.accept_textView);

        String agreementText = "Pada saat Anda mengakses website dan atau layanan online yang Kami sediakan, maka semua informasi dan data pribadi Anda akan Kami kumpulkan dengan ketentuan sebagai berikut :\n" +
                "<br/>\n" +
                "<ol type=\"a\">\n" +
                " <br/> <li>Kami akan mengumpulkan informasi mengenai komputer atau pun media apapun yang Anda gunakan, termasuk IP address, sistem operasi, browser yang digunakan, URL, halaman, lokasi geografis dan waktu akses serta data lainnya terkait dengan penggunaan komputer Anda (“Detail IP”).</li>\n" +
                " <br/> <li>Kami akan meminta Anda untuk mengisi data-data pribadi Anda secara benar, jelas, lengkap, akurat dan tidak menyesatkan, seperti nama, alamat email, nomor telepon, alamat lengkap, informasi yang digunakan untuk pembayaran, informasi kartu kredit (nomor kartu kredit dan masa berlaku kartu kredit) dan data-data lain yang Kami perlukan guna melakukan transaksi melalui website dan layanan online lainnya yang Kami sediakan agar Anda dapat memanfaatkan layanan yang Anda butuhkan. Kami tidak bertanggung jawab atas segala kerugian yang mungkin terjadi karena informasi dan atau data yang tidak benar, jelas, lengkap, akurat dan menyesatkan yang Anda berikan.</li>\n" +
                " <br/> <li>Kami dapat menggunakan data pribadi Anda dan informasi lainnya yang dikumpulkan dengan tujuan pemasaran Media Sosial menggunakan tehnik grafik langsung dan terbuka dan untuk tujuan pemasaran digital konvensional, seperti mengirimkan Anda newsletter secara otomatis melalui surat elektronik untuk memberitahukan informasi produk baru, penawaran khusus atau informasi lainnya yang menurut Kami akan menarik bagi Anda.</li>\n" +
                " <br/> <li>Dalam menggunakan layanan Kami, informasi-informasi yang Anda berikan dapat Kami gunakan dan berikan kepada pihak ketiga yang bekerjasama dengan Kami, sejauh untuk kepentingan transaksi dan penggunaan layanan Kami.</li>\n" +
                " <br/> <li>Segala informasi yang Kami terima dapat Kami gunakan untuk melindungi diri Kami terhadap segala tuntutan dan hukum yang berlaku terkait dengan penggunaan layanan dan pelanggaran yang Anda lakukan pada website Kami atas segala ketentuan sebagaimana diatur dalam persyaratan layanan tiket.com dan pedoman penggunaan produk dan layanan Kami, termasuk dan tidak terbatas apabila dibutuhkan atas perintah Pengadilan dalam proses hukum.</li>\n" +
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
                    Toast.makeText(activity, "Please, read and checklist agreement to order", Toast.LENGTH_SHORT).show();
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