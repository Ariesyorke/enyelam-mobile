package com.nyelam.android.docourse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.booking.BookingServiceSummaryActivity;
import com.nyelam.android.data.CategoryList;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.LicenseType;
import com.nyelam.android.data.LicenseTypeList;
import com.nyelam.android.data.Organization;
import com.nyelam.android.data.OrganizationList;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.data.StateFacilityList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.dodive.DoDiveDiveServiceSuggestionAdapter;
import com.nyelam.android.dodive.DoDiveSearchActivity;
import com.nyelam.android.dodive.RecyclerViewTouchListener;
import com.nyelam.android.dodive.TotalDiverSpinnerAdapter;
import com.nyelam.android.dotrip.DoTripKeywordActivity;
import com.nyelam.android.dotrip.DoTripResultActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoCourseSuggestionServiceRequest;
import com.nyelam.android.http.NYDoTripSuggestionServiceRequest;
import com.nyelam.android.http.NYMasterLicenseTypeRequest;
import com.nyelam.android.http.NYMasterOrganizationRequest;
import com.nyelam.android.http.NYPaypalNotificationRequest;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DoCourseActivity extends BasicActivity implements
        DatePickerDialog.OnDateSetListener,
        NYCustomDialog.OnDialogFragmentClickListener{

    private static int mRequestCode = 100;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private boolean isDoTripBanner;
    private SearchResult searchResult;
    private TextView datetimeTextView, searchTextView;
    public TextView associationTextView, diverTextView;
    private Spinner diverSpinner;
    private com.nyelam.android.view.font.NYTextView keywordTextView;
    private DatePickerDialog datePickerDialog;
    private String keyword, diverId, type, date, diver = "1";
    private SearchService searchService;
    private TotalDiverSpinnerAdapter diverAdapter;
    private LinearLayout diverLinearLayout, datetimeLinearLayout, licenseLinearLayout;
    private NYApplication application;
    private TextView divingLicenseTextView;
    private LinearLayout divingLicenseLinearLayout, associationLinearLayout;
    private  int pickedMonth = -1;
    private  int pickedYear = -1;
    private ScrollView scrollView;

    private LinearLayout associationContainerLinearLayout, divingLicenseContainerLinearLayout;
    private ProgressBar associationProgressBar, divingLicenseProgressBar;

    //suggestion
    private DoCourseSuggestionAdapter doCourseSuggestionAdapter;
    private RecyclerView suggestionRecyclerView;
    private LinearLayout suggestionLinearLayout;

    private OrganizationList organizations;
    private LicenseTypeList licenseTypes;
    private Organization organization;
    private LicenseType licenseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_course);
        initView();
        //initDatePicker();
        initExtra();
        initControl();
        initAdapter();
        getOrganizationRequest();
    }

    private void initAdapter() {
        List<String> divers = new ArrayList<>();
        for (int i=1; i <= 10; i++){
            divers.add(String.valueOf(i));
        }

        diverAdapter = new TotalDiverSpinnerAdapter(DoCourseActivity.this);
        diverSpinner.setAdapter(diverAdapter);
        diverAdapter.addDivers(divers);
        diverAdapter.notifyDataSetChanged();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        suggestionRecyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        suggestionRecyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels,0,spacingInPixels,spacingInPixels));

        doCourseSuggestionAdapter = new DoCourseSuggestionAdapter(DoCourseActivity.this);
        suggestionRecyclerView.setAdapter(doCourseSuggestionAdapter);

        suggestionRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(DoCourseActivity.this, suggestionRecyclerView, new DoDiveDiveServiceSuggestionAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                DiveService diveService = doCourseSuggestionAdapter.getDiveService(position);
                diverId = diveService.getId();
                keyword = diveService.getName();
                type = "4";
                keywordTextView.setText(keyword);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }

            @Override
            public void onLongClick(View view, int position) {
                /*DiveService diveService = diveServiceSuggestionAdapter.getDiveService(position);
                diverId = diveService.getId();
                keyword = diveService.getName();
                type = "4";
                keywordTextView.setText(keyword);
                divingLicenseSwitch.setChecked(diveService.isLicense());
                scrollView.fullScroll(ScrollView.FOCUS_UP);*/
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

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (extras != null){

            if (intent.hasExtra(NYHelper.PICKED_YEAR)) {
                pickedYear = intent.getIntExtra(NYHelper.PICKED_YEAR, -1);
            } else if (intent.hasExtra(NYHelper.PICKED_MONTH)) {
                pickedMonth = intent.getIntExtra(NYHelper.PICKED_MONTH, -1);
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

        keywordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoCourseActivity.this, DoDiveSearchActivity.class);
                intent.putExtra(NYHelper.SCHEDULE, date);
                intent.putExtra(NYHelper.DIVER, diver);
                intent.putExtra(NYHelper.IS_DO_COURSE, true);
                if (isDoTripBanner) {
                    intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                }
                startActivityForResult(intent, mRequestCode);
            }
        });

        associationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //divingLicenseSwitch.setChecked(!divingLicenseSwitch.isChecked());
                //setDivingLicense(!divingLicenseSwitch.isChecked());
                NYCustomDialog dialog = new NYCustomDialog();

                /*Organization organization = new Organization();
                organization.setId("1");
                organization.setName("CMAS");
                organizations.add(organization);*/

                dialog.showAssocitaionDialog(DoCourseActivity.this, organizations.getList(), organization);
            }
        });

        divingLicenseLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //divingLicenseSwitch.setChecked(!divingLicenseSwitch.isChecked());
                //setDivingLicense(!divingLicenseSwitch.isChecked());
                NYCustomDialog dialog = new NYCustomDialog();

                /*LicenseType licenseType = new LicenseType();
                licenseType.setId("1");
                licenseType.setName("CMAS");
                licenseTypes.add(licenseType);*/

                dialog.showLicenseTypeDialog(DoCourseActivity.this, licenseTypes.getList(), licenseType);
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
                dialog.showTotalDiverDialog(DoCourseActivity.this);
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: get diver count
                //String diver = diverTextView.getText().toString();
                String certificate = "0";

                if (!NYHelper.isStringNotEmpty(type)){
                    Toast.makeText(getApplicationContext(), getString(R.string.warn_empty_keyword), Toast.LENGTH_SHORT).show();
                } else if (organization == null || !NYHelper.isStringNotEmpty(organization.getId())){
                    Toast.makeText(getApplicationContext(), getString(R.string.warn_empty_association), Toast.LENGTH_SHORT).show();
                } else if (licenseType == null || !NYHelper.isStringNotEmpty(licenseType.getId())){
                    Toast.makeText(getApplicationContext(), getString(R.string.warn_empty_license_type), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(date)){
                    Toast.makeText(getApplicationContext(), getString(R.string.warn_empty_schedule), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(diver)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.warn_total_diver), Toast.LENGTH_SHORT).show();
                } else {

                    // TODO: ganti fragment yg dulu activity & yg dulu EXTRA sekarang BUNDLE
                    Intent intent;
                    if (type.equals("3")) {
                        intent = new Intent(getApplicationContext(), DoCourseResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.PICKED_MONTH, pickedMonth);
                        intent.putExtra(NYHelper.PICKED_YEAR, pickedYear);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        intent.putExtra(NYHelper.ORGANIZATION, organization.toString());
                        intent.putExtra(NYHelper.LICENSE_TYPE, licenseType.toString());
                        startActivity(intent);
                    } else if (type.equals("4") ){

                        intent = new Intent(getApplicationContext(), DetailServiceActivity.class);
                        //intent = new Intent(getApplicationContext(), DoTripDetailActivity.class);

                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());

                        DiveService diveService = new DiveService();
                        diveService.setId(diverId);
                        intent.putExtra(NYHelper.PICKED_MONTH, pickedMonth);
                        intent.putExtra(NYHelper.PICKED_YEAR, pickedYear);
                        intent.putExtra(NYHelper.SERVICE, diveService.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        intent.putExtra(NYHelper.ORGANIZATION, organization.toString());
                        intent.putExtra(NYHelper.LICENSE_TYPE, licenseType.toString());
                        intent.putExtra(NYHelper.IS_DO_COURSE, true);
                        startActivity(intent);

                    } else if (type.equals("5") || type.equals("6")){

                        intent = new Intent(getApplicationContext(), DoCourseResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.PICKED_MONTH, pickedMonth);
                        intent.putExtra(NYHelper.PICKED_YEAR, pickedYear);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        intent.putExtra(NYHelper.ORGANIZATION, organization.toString());
                        intent.putExtra(NYHelper.LICENSE_TYPE, licenseType.toString());
                        startActivity(intent);

                    } else {
                        intent = new Intent(getApplicationContext(), DoCourseResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.PICKED_MONTH, pickedMonth);
                        intent.putExtra(NYHelper.PICKED_YEAR, pickedYear);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        intent.putExtra(NYHelper.ORGANIZATION, organization.toString());
                        intent.putExtra(NYHelper.LICENSE_TYPE, licenseType.toString());
                        startActivity(intent);
                    }

                }
            }
        });
    }



    private void getOrganizationRequest() {
        setAssociationProgressBar(true);
        NYMasterOrganizationRequest req = new NYMasterOrganizationRequest(getApplicationContext());
        spcMgr.execute(req, onOrganizationRequest());
    }

    private RequestListener<OrganizationList> onOrganizationRequest() {
        return new RequestListener<OrganizationList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*diveServiceSuggestionAdapter.clear();
                diveServiceSuggestionAdapter.notifyDataSetChanged();
                suggestionLinearLayout.setVisibility(View.GONE);*/
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
                setAssociationProgressBar(false);
            }

            @Override
            public void onRequestSuccess(OrganizationList results) {
                organizations = results;
                organization = organizations.getList().get(0);

                setAssociationProgressBar(false);

                if (organization != null && NYHelper.isStringNotEmpty(organization.getName()))
                    associationTextView.setText(organization.getName());

                getLicenseTypeRequest();
            }
        };
    }


    private void getLicenseTypeRequest() {
        setDivingLicenseProgressBar(true);
        NYMasterLicenseTypeRequest req = new NYMasterLicenseTypeRequest(getApplicationContext(), organization.getId());
        spcMgr.execute(req, onLicenseTypeRequest());
    }

    private RequestListener<LicenseTypeList> onLicenseTypeRequest() {
        return new RequestListener<LicenseTypeList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*diveServiceSuggestionAdapter.clear();
                diveServiceSuggestionAdapter.notifyDataSetChanged();
                suggestionLinearLayout.setVisibility(View.GONE);*/
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
                setDivingLicenseProgressBar(false);
            }

            @Override
            public void onRequestSuccess(LicenseTypeList results) {
                licenseTypes = results;
                licenseType = licenseTypes.getList().get(0);

                setDivingLicenseProgressBar(false);

                if (licenseType != null && NYHelper.isStringNotEmpty(licenseType.getName()))
                    divingLicenseTextView.setText(licenseType.getName());
            }
        };
    }



    private void getSuggetionRequest() {
        NYDoCourseSuggestionServiceRequest req = new NYDoCourseSuggestionServiceRequest(getApplicationContext());
        spcMgr.execute(req, onSearchServiceRequest());

        // TODO: load data dummy, to test and waitting for API request
        //loadJSONAsset();
    }

    private RequestListener<DiveServiceList> onSearchServiceRequest() {
        return new RequestListener<DiveServiceList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                doCourseSuggestionAdapter.clear();
                doCourseSuggestionAdapter.notifyDataSetChanged();
                suggestionLinearLayout.setVisibility(View.GONE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {
                if (results != null){
                    suggestionLinearLayout.setVisibility(View.VISIBLE);
                    doCourseSuggestionAdapter.clear();
                    doCourseSuggestionAdapter.addResults(results.getList());
                    doCourseSuggestionAdapter.notifyDataSetChanged();
                } else {
                    doCourseSuggestionAdapter.clear();
                    doCourseSuggestionAdapter.notifyDataSetChanged();
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

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        keywordTextView = (com.nyelam.android.view.font.NYTextView) findViewById(R.id.keyword_textView);
        diverSpinner = (Spinner) findViewById(R.id.diver_spinner);
        datetimeTextView = (TextView) findViewById(R.id.datetime_textView);
        diverTextView = (TextView) findViewById(R.id.diver_textView);
        associationTextView = (TextView) findViewById(R.id.association_textView);
        searchTextView = (TextView) findViewById(R.id.search_textView);
        //certificateCheckBox = (CheckBox) v.findViewById(R.id.certificate_checkBox);
        diverLinearLayout = (LinearLayout) findViewById(R.id.diver_linearLayout);
        datetimeLinearLayout = (LinearLayout) findViewById(R.id.datetime_linearLayout);
        licenseLinearLayout = (LinearLayout) findViewById(R.id.license_linearLayout);

        suggestionRecyclerView = (RecyclerView) findViewById(R.id.suggestion_recyclerView);
        suggestionLinearLayout = (LinearLayout) findViewById(R.id.suggestion_linearLayout);
        divingLicenseLinearLayout = (LinearLayout) findViewById(R.id.diving_license_linearLayout);
        associationLinearLayout = (LinearLayout) findViewById(R.id.association_linearLayout);
        divingLicenseTextView = (TextView) findViewById(R.id.diving_license_textView);

        associationContainerLinearLayout = (LinearLayout) findViewById(R.id.association_container_linearLayout);
        divingLicenseContainerLinearLayout = (LinearLayout) findViewById(R.id.diving_license_container_linearLayout);
        associationProgressBar = (ProgressBar) findViewById(R.id.association_progressBar);
        divingLicenseProgressBar = (ProgressBar) findViewById(R.id.diving_license_progressBar);

        organizations = new OrganizationList();
        licenseTypes = new LicenseTypeList();
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
        getSuggetionRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(spcMgr.isStarted())spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void openSchedule(){
        final Calendar today = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);

        final MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(DoCourseActivity.this, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                datetimeTextView.setText(NYHelper.formatMonthYearToString(selectedMonth, selectedYear));
                setScheduleTime(selectedMonth, selectedYear);
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        if (pickedMonth < 0 && pickedYear < 0) {

            Calendar calendar = Calendar.getInstance();
            int m = calendar.get(Calendar.MONTH);
            builder.setActivatedMonth(NYHelper.getMonth(m));
            builder.setActivatedYear(year);
        } else {
            builder.setActivatedMonth(pickedMonth);
            builder.setActivatedYear(pickedYear);
        }
        builder.setMinYear(year)
                .setMaxYear(year+2)
                .setMinMonth(month+1)
                .setTitle("Select Schedule")
                .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)

                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int selectedMonth) {
                    }
                })
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) {
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
    }


    @Override
    public void onChooseListener(Object object) {

        if (object instanceof String){
            diverTextView.setText((String) object+" Diver(s)");
            this.diver = (String) object;
        } else if (object instanceof Organization){
            associationTextView.setText(((Organization) object).getName());
            this.organization = (Organization) object;
            getLicenseTypeRequest();
        } else if (object instanceof LicenseType){
            if (((LicenseType) object) != null && NYHelper.isStringNotEmpty(((LicenseType) object).getName())) divingLicenseTextView.setText(((LicenseType) object).getName());
            this.licenseType = (LicenseType) object;
        }
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
        pickedMonth = selectedMonth;
        pickedYear = selectedYear;
        if (monthNow == selectedMonth && yearNow == selectedYear){
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT +7"));
            c.set(selectedYear, selectedMonth,dateNow);
            date = String.valueOf(c.getTimeInMillis()/1000);
        } else {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT +7"));
            c.set(selectedYear, selectedMonth,1);
            date = String.valueOf(c.getTimeInMillis()/1000);
        }

        if (date != null) NYLog.e("CEK SCHEDULE : "+date);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras();

            if (data.hasExtra(NYHelper.SEARCH_RESULT)){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(data.getStringExtra(NYHelper.SEARCH_RESULT));
                    searchService = new SearchService();
                    searchService.parse(obj);

                    diverId = searchService.getId();
                    keyword = searchService.getName();
                    type = String.valueOf(searchService.getType());
                    keywordTextView.setText(keyword);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void setAssociationProgressBar(boolean isShow){
        if (isShow){
            associationProgressBar.setVisibility(View.VISIBLE);
            associationContainerLinearLayout.setVisibility(View.GONE);
        } else {
            associationProgressBar.setVisibility(View.GONE);
            associationContainerLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setDivingLicenseProgressBar (boolean isShow){
        if (isShow){
            divingLicenseProgressBar.setVisibility(View.VISIBLE);
            divingLicenseContainerLinearLayout.setVisibility(View.GONE);
        } else {
            divingLicenseProgressBar.setVisibility(View.GONE);
            divingLicenseContainerLinearLayout.setVisibility(View.VISIBLE);
        }
    }

}