package com.nyelam.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
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
        void onChooseListener(int position);
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

                listener.onChooseListener(index);

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
                listener.onAcceptAgreementListener();
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}