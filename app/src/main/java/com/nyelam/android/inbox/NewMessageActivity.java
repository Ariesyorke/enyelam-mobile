package com.nyelam.android.inbox;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;

public class NewMessageActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private Spinner sCategory;
    private TextView tvTitle;
    private EditText etSubject;
    private LinearLayout llCategory;

    private ContactUsSpinnerAdapter mAdapter;
    private ArrayList<String> mCategorySpinnerArray = new ArrayList<>();

    String title = "";
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        context = this;

        initView();
        initCheckExtras();
        initToolbar();
        initControl();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.title_textView);
        etSubject = (EditText) findViewById(R.id.et_subject);
        sCategory = (Spinner) findViewById(R.id.s_category);
        llCategory = (LinearLayout) findViewById(R.id.ll_category);
    }

    private void initCheckExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean bCategory = extras.getBoolean("category", false);
            if (bCategory) {
                llCategory.setVisibility(View.VISIBLE);
                tvTitle.setText("Contact Us");
            } else {

            }

            if(extras.getString("title") != null){
                title = extras.getString("title");
                etSubject.setText(title);
                etSubject.setEnabled(false);
            }

            if(extras.getString("type") != null){
                type = extras.getString("type");
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    private void initControl() {
        mCategorySpinnerArray.add("General");
        mCategorySpinnerArray.add("Issue App");
        mCategorySpinnerArray.add("Payment");

        mAdapter = new ContactUsSpinnerAdapter(NewMessageActivity.this, R.layout.spinner_contact_us_row, mCategorySpinnerArray);
        sCategory.setAdapter(mAdapter);
        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String category = mCategorySpinnerArray.get(position).toString();
                if(category.equals("General")){
                    type = "3";
                }else if (category.equals("Issue App")){
                    type = "4";
                }else{
                    type = "5";
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // TODO Auto-generated catch block
            }
        });
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
