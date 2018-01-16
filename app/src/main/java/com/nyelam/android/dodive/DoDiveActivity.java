package com.nyelam.android.dodive;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.detail.DetailServiceActivity;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DoDiveActivity extends BasicActivity implements DatePickerDialog.OnDateSetListener {

    private SearchResult searchResult;
    private TextView diverTextView, certificateTextView, datetimeTextView, searchTextView;
    private com.nyelam.android.view.NYEditTextWarning keywordTextView;
    private Switch certificateSwitch;
    private LinearLayout plusLinearLayout, minusLinearLayout;
    private DatePickerDialog datePickerDialog;
    private String keyword, diverId, type, date;
    private SearchService searchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive);
        initView();
        initExtra();
        initToolbar();
        initControl();
    }

    private void initExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
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
                    certificateSwitch.setChecked(true);
                    certificateSwitch.setClickable(false);
                } else {
                    certificateSwitch.setChecked(false);
                    certificateSwitch.setClickable(true);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            // and get whatever type user account id is
        }
    }

    private void initControl() {
        keywordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoDiveActivity.this, DoDiveSearchActivity.class);
                startActivity(intent);
            }
        });
        minusLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(diverTextView.getText().toString());
                if (count-1 >= 1){
                    diverTextView.setText(String.valueOf((count-1)));
                }
            }
        });
        plusLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(diverTextView.getText().toString());
                diverTextView.setText(String.valueOf((count+1)));
            }
        });

        datetimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String diver = diverTextView.getText().toString();
                String certificate = "0";
                if (certificateSwitch.isChecked()){
                    certificate = "1";
                } else {
                    certificate = "0";
                }

                /*if (!TextUtils.isEmpty(type) && type.equals("2")){

                    Intent intent = new Intent(DoDiveActivity.this, DetailServiceActivity.class);
                    intent.putExtra(NYHelper.DATE, date);

                    // TODO: parse from SearchService to DiveService
                    DiveService diveService = new DiveService();
                    if (searchService.getId() != null)diveService.setId(searchService.getId());
                    if (searchService.getName() != null)diveService.setName(searchService.getName());
                    if (searchService.getRating() != null)diveService.setRating(Integer.valueOf(searchService.getRating()));
                    intent.putExtra(NYHelper.SERVICE, diveService.toString());
                    *//*intent.putExtra(NYHelper.KEYWORD, keyword);
                    intent.putExtra(NYHelper.ID_DIVER, diverId);
                    intent.putExtra(NYHelper.CERTIFICATE, certificate);
                    intent.putExtra(NYHelper.DIVER, diver);
                    intent.putExtra(NYHelper.TYPE, type);*//*
                    startActivity(intent);

                } else */

                if (!TextUtils.isEmpty(type)){
                    Intent intent = new Intent(DoDiveActivity.this, DoDiveSearchResultActivity.class);
                    intent.putExtra(NYHelper.KEYWORD, keyword);
                    intent.putExtra(NYHelper.ID_DIVER, diverId);
                    intent.putExtra(NYHelper.CERTIFICATE, certificate);
                    intent.putExtra(NYHelper.DATE, date);
                    intent.putExtra(NYHelper.DIVER, diver);
                    intent.putExtra(NYHelper.TYPE, type);
                    startActivity(intent);
                } else {
                    keywordTextView.isEmpty();
                }
            }
        });
    }

    private void initView() {
        keywordTextView = (com.nyelam.android.view.NYEditTextWarning) findViewById(R.id.keyword_textView);
        diverTextView = (TextView) findViewById(R.id.diver_textView);
        minusLinearLayout = (LinearLayout) findViewById(R.id.minus_linearLayout);
        plusLinearLayout = (LinearLayout) findViewById(R.id.plus_linearLayout);
        datetimeTextView = (TextView) findViewById(R.id.datetime_textView);
        certificateTextView = (TextView) findViewById(R.id.certificate_textView);
        searchTextView = (TextView) findViewById(R.id.search_textView);
        certificateSwitch = (Switch) findViewById(R.id.certificate_switch);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(
                this, this, year, month, day);
        date = String.valueOf(c.getTimeInMillis()/1000);
        datetimeTextView.setText(String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
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

}
