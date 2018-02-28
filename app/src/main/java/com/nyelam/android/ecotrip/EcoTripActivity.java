package com.nyelam.android.ecotrip;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.dodive.DoDiveFragment;
import com.nyelam.android.dodive.DoDiveSearchActivity;
import com.nyelam.android.dodive.DoDiveSearchResultActivity;
import com.nyelam.android.dodive.TotalDiverSpinnerAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EcoTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SearchResult searchResult;
    private TextView titleTextView, certificateTextView, datetimeTextView, searchTextView, diverTextView;
    private Spinner diverSpinner;
    private com.nyelam.android.view.NYEditTextWarning keywordTextView;
    private CheckBox certificateCheckBox;
    private DatePickerDialog datePickerDialog;
    private String keyword, diverId, type, date, diver = null;
    private SearchService searchService;
    private TotalDiverSpinnerAdapter diverAdapter;
    private LinearLayout diverLinearLayout, datetimeLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_trip);
        initView();
        initDatePicker();
        initExtra();
        initControl();
        initAdapter();
    }


    private void initAdapter() {
        List<String> divers = new ArrayList<>();
        for (int i=1; i <= 10; i++){
            divers.add(String.valueOf(i));
        }

        diverAdapter = new TotalDiverSpinnerAdapter(EcoTripActivity.this);
        diverSpinner.setAdapter(diverAdapter);
        diverAdapter.addDivers(divers);
        diverAdapter.notifyDataSetChanged();

    }

    private void initDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(EcoTripActivity.this, this, year, month, day);
        date = String.valueOf(c.getTimeInMillis()/1000);
        datetimeTextView.setText(String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
    }


    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();

        if (extras != null && intent.hasExtra(NYHelper.SEARCH_RESULT) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SEARCH_RESULT))) {
            try {
                JSONObject obj = new JSONObject(extras.getString(NYHelper.SEARCH_RESULT));
                searchService = new SearchService();
                searchService.parse(obj);

                // TODO: sekarang pakai class object
                if (obj.has("name")){
                    keyword = obj.getString("name");
                    keywordTextView.setText(obj.getString("name"));
                }
                if (obj.has("type"))type = obj.getString("type");
                if (obj.has("id"))diverId = obj.getString("id");

                if (obj.has("license") && obj.getBoolean("license")){
                    certificateCheckBox.setChecked(true);
                    certificateCheckBox.setClickable(false);
                } else {
                    certificateCheckBox.setChecked(false);
                    certificateCheckBox.setClickable(true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (extras != null){

            if (intent.hasExtra(NYHelper.CERTIFICATE)){
                certificateCheckBox.setChecked(intent.getBooleanExtra(NYHelper.CERTIFICATE,false));
            }

            if (intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(intent.getStringExtra(NYHelper.SCHEDULE))){

                date = intent.getStringExtra(NYHelper.SCHEDULE);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(Long.valueOf(date)*1000);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(
                        EcoTripActivity.this, this, year, month, day);
                datetimeTextView.setText(String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
            }

            if (intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(intent.getStringExtra(NYHelper.DIVER))){
                diverTextView.setText(intent.getStringExtra(NYHelper.DIVER));
                diver = intent.getStringExtra(NYHelper.DIVER);
            }


        }

    }

    private void initControl() {
        keywordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EcoTripActivity.this, DoDiveSearchActivity.class);
                intent.putExtra(NYHelper.CERTIFICATE, certificateCheckBox.isChecked());
                intent.putExtra(NYHelper.SCHEDULE, date);
                intent.putExtra(NYHelper.DIVER, diver);
                startActivity(intent);
            }
        });

        datetimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        diverLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYCustomDialog dialog = new NYCustomDialog();
                dialog.showTotalDiverDialog(EcoTripActivity.this);
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: get diver count
                String certificate = "0";
                if (certificateCheckBox.isChecked()){
                    certificate = "1";
                } else {
                    certificate = "0";
                }

                if (!TextUtils.isEmpty(type) && diver != null){

                    // TODO: ganti fragment yg dulu activity & yg dulu EXTRA sekarang BUNDLE
                    Intent intent;
                    if (type.equals("3")){
                        intent = new Intent(EcoTripActivity.this, DiveCenterDetailActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        startActivity(intent);
                    } else if (type.equals("4") || type.equals("5") || type.equals("6")){

                        intent = new Intent(EcoTripActivity.this, DoDiveSearchResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        startActivity(intent);
                    } else {
                        intent = new Intent(EcoTripActivity.this, DoDiveSearchResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        startActivity(intent);
                    }

                } else if (diver == null){
                    Toast.makeText(EcoTripActivity.this, "Pilih jumlah diver", Toast.LENGTH_SHORT).show();
                } else {
                    keywordTextView.isEmpty();
                }
            }
        });
    }

    private void initView() {
        titleTextView = (TextView) findViewById(R.id.title_textView);
        keywordTextView = (com.nyelam.android.view.NYEditTextWarning) findViewById(R.id.keyword_textView);
        diverSpinner = (Spinner) findViewById(R.id.diver_spinner);
        datetimeTextView = (TextView) findViewById(R.id.datetime_textView);
        certificateTextView = (TextView) findViewById(R.id.certificate_textView);
        diverTextView = (TextView) findViewById(R.id.diver_textView);
        searchTextView = (TextView) findViewById(R.id.search_textView);
        certificateCheckBox = (CheckBox) findViewById(R.id.certificate_checkBox);
        diverLinearLayout = (LinearLayout) findViewById(R.id.diver_linearLayout);
        datetimeLinearLayout = (LinearLayout) findViewById(R.id.datetime_linearLayout);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datetimeTextView.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
        cal.set(Calendar.MONTH, view.getMonth());
        cal.set(Calendar.YEAR, view.getYear());

        date = String.valueOf(cal.getTimeInMillis()/1000);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        titleTextView.setText(R.string.eco_trip);
    }

    public void setDiver(String diver){
        diverTextView.setText(diver);
        this.diver = diver;
    }




}
