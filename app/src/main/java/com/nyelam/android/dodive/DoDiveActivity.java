package com.nyelam.android.dodive;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class DoDiveActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SearchResult searchResult;
    private TextView keywordTextView, diverTextView, certificateTextView, datetimeTextView;
    private Switch certificateSwitch;
    private LinearLayout plusLinearLayout, minusLinearLayout;
    private DatePickerDialog datePickerDialog;

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
                //Toast.makeText(this, obj.getString("name"), Toast.LENGTH_SHORT).show();
                if (obj.has("name"))keywordTextView.setText(obj.getString("name"));

                if (obj.has("license") && obj.getBoolean("license")){
                    certificateSwitch.setChecked(true);
                    certificateSwitch.setClickable(false);
                    //certificateSwitch.setEnabled(false);
                } else {
                    certificateSwitch.setChecked(false);
                    certificateSwitch.setClickable(true);
                }
                //searchResult.parse(obj);
                //if (searchResult != null) keywordTextView.setText(searchResult.getName());
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
                if (count-1 >= 0){
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
    }

    private void initView() {
        keywordTextView = (TextView) findViewById(R.id.keyword_textView);
        diverTextView = (TextView) findViewById(R.id.diver_textView);
        minusLinearLayout = (LinearLayout) findViewById(R.id.minus_linearLayout);
        plusLinearLayout = (LinearLayout) findViewById(R.id.plus_linearLayout);
        datetimeTextView = (TextView) findViewById(R.id.datetime_textView);
        certificateTextView = (TextView) findViewById(R.id.certificate_textView);
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
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datetimeTextView.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
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
