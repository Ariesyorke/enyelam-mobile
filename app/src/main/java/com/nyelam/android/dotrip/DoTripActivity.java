package com.nyelam.android.dotrip;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.dodive.DoDiveDiveServiceSuggestionAdapter;
import com.nyelam.android.dodive.DoDiveSearchActivity;
import com.nyelam.android.dodive.DoDiveSearchResultActivity;
import com.nyelam.android.dodive.RecyclerViewTouchListener;
import com.nyelam.android.dodive.TotalDiverSpinnerAdapter;
import com.nyelam.android.ecotrip.EcoTripViewPagerAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSuggestionServiceRequest;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import me.relex.circleindicator.CircleIndicator;

public class DoTripActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        NYCustomDialog.OnDialogFragmentClickListener{

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private boolean isDoTripBanner;
    private SearchResult searchResult;
    private TextView datetimeTextView, searchTextView;
    public TextView diverTextView;
    private Spinner diverSpinner;
    private com.nyelam.android.view.font.NYTextView keywordTextView;
    private DatePickerDialog datePickerDialog;
    private String keyword, diverId, type, date, diver = null;
    private SearchService searchService;
    private TotalDiverSpinnerAdapter diverAdapter;
    private LinearLayout diverLinearLayout, datetimeLinearLayout, licenseLinearLayout;
    private NYApplication application;
    private Switch divingLicenseSwitch;
    private TextView divingLicenseTextView;
    private LinearLayout divingLicenseLinearLayout;
    //private Calendar c;

    //suggestion
    private DoDiveDiveServiceSuggestionAdapter diveServiceSuggestionAdapter;
    private RecyclerView suggestionRecyclerView;
    private LinearLayout suggestionLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_trip);
        initView();
        //initDatePicker();
        initExtra();
        initControl();
        initAdapter();
    }

    private void initAdapter() {
        List<String> divers = new ArrayList<>();
        for (int i=1; i <= 10; i++){
            divers.add(String.valueOf(i));
        }

        diverAdapter = new TotalDiverSpinnerAdapter(DoTripActivity.this);
        diverSpinner.setAdapter(diverAdapter);
        diverAdapter.addDivers(divers);
        diverAdapter.notifyDataSetChanged();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        suggestionRecyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        suggestionRecyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels,0,spacingInPixels,spacingInPixels));

        diveServiceSuggestionAdapter = new DoDiveDiveServiceSuggestionAdapter(DoTripActivity.this);
        suggestionRecyclerView.setAdapter(diveServiceSuggestionAdapter);

        suggestionRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(DoTripActivity.this, suggestionRecyclerView, new DoDiveDiveServiceSuggestionAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                DiveService diveService = diveServiceSuggestionAdapter.getDiveService(position);
                diverId = diveService.getId();
                keyword = diveService.getName();
                type = "4";
                keywordTextView.setText(keyword);
            }

            @Override
            public void onLongClick(View view, int position) {
                DiveService diveService = diveServiceSuggestionAdapter.getDiveService(position);
                diverId = diveService.getId();
                keyword = diveService.getName();
                type = "4";
                keywordTextView.setText(keyword);
            }
        }));


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
                    setDivingLicense(true);
                    //divingLicenseSwitch.setChecked(true);
                    //certificateCheckBox.setClickable(false);
                } else {
                    setDivingLicense(false);
                    //divingLicenseSwitch.setChecked(false);
                    //certificateCheckBox.setClickable(true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (extras != null){

            if (intent.hasExtra(NYHelper.CERTIFICATE)){
                //certificateCheckBox.setChecked(intent.getBooleanExtra(NYHelper.CERTIFICATE,false));
                //divingLicenseSwitch.setChecked(intent.getBooleanExtra(NYHelper.CERTIFICATE,false));
                setDivingLicense(intent.getBooleanExtra(NYHelper.CERTIFICATE,false));
            }

            if (intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(intent.getStringExtra(NYHelper.SCHEDULE))){
                Calendar c = Calendar.getInstance();
                date = intent.getStringExtra(NYHelper.SCHEDULE);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                if(isDoTripBanner) {
                    List<Calendar> calendars = new ArrayList<>();
                    int divider = 12 - month;

                    for(int i = 0; i < divider; i++) {
                        Calendar a = Calendar.getInstance();
                        a.setTime(new Date());
                        a.add(Calendar.MONTH, i);
                        a.set(Calendar.DAY_OF_WEEK, a.getFirstDayOfWeek());
                        a.set(Calendar.WEEK_OF_MONTH, 3);
                        for(int j = 0; j < 7; j++) {
                            if(a.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                                calendars.add(a);
                                break;
                            } else {
                                a.add(Calendar.DAY_OF_WEEK, 1);
                            }
                        }
                        if(i == 0) {
                            int eYear = a.get(Calendar.YEAR);
                            int eMonth = a.get(Calendar.MONTH);
                            int eDay = a.get(Calendar.DAY_OF_MONTH);
                            date = String.valueOf(a.getTimeInMillis()/1000);
                            datePickerDialog = DatePickerDialog.newInstance(this, eYear, eMonth, eDay);
                            datePickerDialog.setMinDate(a);
                        }
                    }
                    Calendar[] cals = new Calendar[calendars.size()];
                    calendars.toArray(cals);
                    datePickerDialog.setSelectableDays(cals);
                } else {
                    c.setTimeInMillis(Long.valueOf(date)*1000);
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = DatePickerDialog.newInstance(this, year, month, day);
                    //cal.setTime(new Date());
                    datePickerDialog.setMinDate(c);
                }

//                if (Build.VERSION.SDK_INT <= 21) {
//                    //this is where the crash happens
//                    datePickerDialog.setMinDate(cal.getTime() - 100000);
//                } else {
//                    datePickerDialog.setMinDate(Calendar.getInstance());
//                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//                }
                datetimeTextView.setText(String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
            }

            if (intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(intent.getStringExtra(NYHelper.DIVER))){
                diverTextView.setText(intent.getStringExtra(NYHelper.DIVER)+" Diver(s)");
                diver = intent.getStringExtra(NYHelper.DIVER);
            }


        }

        //TODO HARCODE ECOTRIP!!
        if(isDoTripBanner) {
            keyword = "Save Our Small Island";
            keywordTextView.setText(keyword);
            type = "3";
            diverId = "23";
        }
    }

    private void initControl() {

        divingLicenseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDivingLicense(divingLicenseSwitch.isChecked());
            }
        });

        keywordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoTripActivity.this, DoTripKeywordActivity.class);
                intent.putExtra(NYHelper.CERTIFICATE, divingLicenseSwitch.isChecked());
                intent.putExtra(NYHelper.SCHEDULE, date);
                intent.putExtra(NYHelper.DIVER, diver);
                if (isDoTripBanner) {
                    intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                }
                startActivity(intent);
            }
        });

        divingLicenseLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //divingLicenseSwitch.setChecked(!divingLicenseSwitch.isChecked());
                setDivingLicense(!divingLicenseSwitch.isChecked());
            }
        });

        datetimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                openSchedule();
            }
        });

        diverLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYCustomDialog dialog = new NYCustomDialog();
                dialog.showTotalDiverDialog(DoTripActivity.this);
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: get diver count
                //String diver = diverTextView.getText().toString();
                String certificate = "0";
                if (divingLicenseSwitch.isChecked()){
                    certificate = "1";
                } else {
                    certificate = "0";
                }

                if (!NYHelper.isStringNotEmpty(type)){
                            Toast.makeText(getApplicationContext(), getString(R.string.warn_empty_keyword), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(diver)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.warn_total_diver), Toast.LENGTH_SHORT).show();
                } else {

                    // TODO: ganti fragment yg dulu activity & yg dulu EXTRA sekarang BUNDLE
                    Intent intent;
                    if (type.equals("3")){
                        intent = new Intent(getApplicationContext(), DoTripResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        if (getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                            intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                        }
                        startActivity(intent);
                    } else if (type.equals("4") ){

                        intent = new Intent(getApplicationContext(), DetailServiceActivity.class);
                        //intent = new Intent(getApplicationContext(), DoTripDetailActivity.class);

                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());

                        DiveService diveService = new DiveService();
                        diveService.setId(diverId);
                        intent.putExtra(NYHelper.SERVICE, diveService.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        if (getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                            intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                        }
                        startActivity(intent);

                    } else if (type.equals("5") || type.equals("6")){

                        intent = new Intent(getApplicationContext(), DoTripResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        if (getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                            intent.putExtra(NYHelper.ECO_TRIP, 1);
                        }
                        startActivity(intent);

                    } else {
                        intent = new Intent(getApplicationContext(), DoTripResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        if (getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                            intent.putExtra(NYHelper.ECO_TRIP, 1);
                        }
                        startActivity(intent);
                    }

                }
            }
        });
    }


    private void getSuggetionRequest() {
        NYDoDiveSuggestionServiceRequest req = new NYDoDiveSuggestionServiceRequest(getApplicationContext());
        spcMgr.execute(req, onSearchServiceRequest());

        // TODO: load data dummy, to test and waitting for API request
        //loadJSONAsset();
    }

    private RequestListener<DiveServiceList> onSearchServiceRequest() {
        return new RequestListener<DiveServiceList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                diveServiceSuggestionAdapter.clear();
                diveServiceSuggestionAdapter.notifyDataSetChanged();
                suggestionLinearLayout.setVisibility(View.GONE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {
                if (results != null){
                    suggestionLinearLayout.setVisibility(View.VISIBLE);
                    diveServiceSuggestionAdapter.clear();
                    diveServiceSuggestionAdapter.addResults(results.getList());
                    diveServiceSuggestionAdapter.notifyDataSetChanged();
                } else {
                    diveServiceSuggestionAdapter.clear();
                    diveServiceSuggestionAdapter.notifyDataSetChanged();
                    suggestionLinearLayout.setVisibility(View.GONE);
                }

            }
        };
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);

        keywordTextView = (com.nyelam.android.view.font.NYTextView) findViewById(R.id.keyword_textView);
        diverSpinner = (Spinner) findViewById(R.id.diver_spinner);
        datetimeTextView = (TextView) findViewById(R.id.datetime_textView);
        diverTextView = (TextView) findViewById(R.id.diver_textView);
        searchTextView = (TextView) findViewById(R.id.search_textView);
        //certificateCheckBox = (CheckBox) v.findViewById(R.id.certificate_checkBox);
        diverLinearLayout = (LinearLayout) findViewById(R.id.diver_linearLayout);
        datetimeLinearLayout = (LinearLayout) findViewById(R.id.datetime_linearLayout);
        licenseLinearLayout = (LinearLayout) findViewById(R.id.license_linearLayout);

        suggestionRecyclerView = (RecyclerView) findViewById(R.id.suggestion_recyclerView);
        suggestionLinearLayout = (LinearLayout) findViewById(R.id.suggestion_linearLayout);
        divingLicenseLinearLayout = (LinearLayout) findViewById(R.id.diving_license_linearLayout);
        divingLicenseTextView = (TextView) findViewById(R.id.diving_license_textView);
        divingLicenseSwitch = (Switch) findViewById(R.id.diving_license_switch);
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
    protected void onStart() {
        super.onStart();
        spcMgr.start(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(spcMgr.isStarted())spcMgr.shouldStop();
    }


    public void openSchedule(){
        final Calendar today = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);

        final MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(DoTripActivity.this, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                //NYLog.d("selectedMonth : " + selectedMonth + " selectedYear : " + selectedYear);
                //Toast.makeText(DoTripActivity.this, "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
                datetimeTextView.setText(NYHelper.formatMonthYearToString(selectedMonth, selectedYear));
                setScheduleTime(selectedMonth, selectedYear);
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(year)
                .setActivatedYear(year)
                .setMaxYear(year+2)
                .setMinMonth(month+1)
                .setTitle("Select Schedule")
                .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                // .setMaxMonth(Calendar.OCTOBER)
                // .setYearRange(1890, 1890)
                //.setMonthAndYearRange(Calendar.FEBRUARY, Calendar.DECEMBER, 2018, 2020)
                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int selectedMonth) {
                        //Log.d(TAG, "Selected month : " + selectedMonth);
                        Toast.makeText(DoTripActivity.this, " Selected month : " + selectedMonth, Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) {
                        //Log.d(TAG, "Selected year : " + selectedYear);
                        Toast.makeText(DoTripActivity.this, " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
                        if (selectedYear == 2018){
                            builder.setMonthRange(Calendar.APRIL, Calendar.DECEMBER);
                            builder.build();
                        } else {
                            builder.setMonthRange(Calendar.JANUARY, Calendar.DECEMBER);
                            builder.build();
                        }
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT +7"));
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.YEAR, year);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        date = String.valueOf(cal.getTimeInMillis()/1000);
        datetimeTextView.setText(String.valueOf(d) + "/" + String.valueOf(m+1) + "/" + String.valueOf(y));

        Toast.makeText(application, "my date : "+date, Toast.LENGTH_SHORT).show();
    }

    public void setDivingLicense(boolean isTrue){
        if (isTrue){
            divingLicenseTextView.setText(getString(R.string.yes));
            divingLicenseSwitch.setChecked(true);
        } else {
            divingLicenseTextView.setText(getString(R.string.no));
            divingLicenseSwitch.setChecked(false);
        }
    }

    @Override
    public void onChooseListener(Object position) {
        diverTextView.setText((String) position+" Diver(s)");
        this.diver = (String) position;
    }

    @Override
    public void onAcceptAgreementListener() {

    }

    @Override
    public void onCancelUpdate() {

    }

    @Override
    public void doUpdateVersion(String link) {

    }

    public void setScheduleTime(int selectedMonth, int selectedYear){
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("GMT +7"));
        int yearNow = today.get(Calendar.YEAR);
        int monthNow = today.get(Calendar.MONTH);
        int dateNow = today.get(Calendar.DAY_OF_MONTH);

        if (monthNow == selectedMonth && yearNow == selectedYear){
            Calendar c = Calendar.getInstance();
            c.set(selectedYear, selectedMonth,dateNow);
            date = String.valueOf(c.getTimeInMillis());
        } else {
            Calendar c = Calendar.getInstance();
            c.set(selectedYear, selectedMonth,1);
            date = String.valueOf(c.getTimeInMillis());
        }

        if (date != null)NYLog.e("CEK SCHEDULE : "+date);

    }
}
