package com.nyelam.android.dodive;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyelam.android.R;

import java.util.Calendar;

public class DoDiveActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView diveSpotTextView, diverTextView;
    private TextView datetimeTextView;
    private LinearLayout plusLinearLayout, minusLinearLayout;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive);
        initToolbar();
        initView();
        initControl();
    }

    private void initControl() {
        diveSpotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoDiveActivity.this, SearchDiveSpotActivity.class);
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
        diveSpotTextView = (TextView) findViewById(R.id.dive_spot_textView);
        diverTextView = (TextView) findViewById(R.id.diver_textView);
        minusLinearLayout = (LinearLayout) findViewById(R.id.minus_linearLayout);
        plusLinearLayout = (LinearLayout) findViewById(R.id.plus_linearLayout);
        datetimeTextView = (TextView) findViewById(R.id.datetime_textView);
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
