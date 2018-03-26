package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.general.CountryCodeAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYCustomDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class DoDiveFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private SearchResult searchResult;
    private TextView certificateTextView, datetimeTextView, searchTextView;
    public TextView diverTextView;
    private Spinner diverSpinner;
    private com.nyelam.android.view.font.NYTextView keywordTextView;
    private CheckBox certificateCheckBox;
    private DatePickerDialog datePickerDialog;
    private String keyword, diverId, type, date, diver = null;
    private SearchService searchService;
    private TotalDiverSpinnerAdapter diverAdapter;
    private LinearLayout diverLinearLayout, datetimeLinearLayout, licenseLinearLayout;
    private NYApplication application;
//    private Calendar c;

    private OnFragmentInteractionListener mListener;

    public DoDiveFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DoDiveFragment newInstance() {
        DoDiveFragment fragment = new DoDiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_do_dive, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        application = (NYApplication)getActivity().getApplication();
//        c = application.calendar;
        initView(view);
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

        diverAdapter = new TotalDiverSpinnerAdapter(getActivity());
        diverSpinner.setAdapter(diverAdapter);
        diverAdapter.addDivers(divers);
        diverAdapter.notifyDataSetChanged();

    }


    private void initDatePicker() {
        //final Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        //c.setTime(new Date());
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        /*Calendar mcurrentDate=Calendar.getInstance();
        year=mcurrentDate.get(Calendar.YEAR);
        month=mcurrentDate.get(Calendar.MONTH);
        day=mcurrentDate.get(Calendar.DAY_OF_MONTH);*/

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//        if (Build.VERSION.SDK_INT <= 21) {
//            //this is where the crash happens
//            datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
//        } else {
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//        }

        // TODO Hide Future Date Here
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        // TODO Hide Past Date Here
        //datePickerDialog.show();
        DoDiveActivity activity = (DoDiveActivity)getActivity();
        if(activity.isEcoTrip()) {
            List<Calendar> calendars = new ArrayList<>();
            //List<Calendar> calendarsTwo = new ArrayList<>();
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
                    datetimeTextView.setText(String.valueOf(eDay) + "/" + String.valueOf(eMonth+1) + "/" + String.valueOf(eYear));
                }
            }


            Calendar[] cals = new Calendar[calendars.size()];
            calendars.toArray(cals);
            datePickerDialog.setSelectableDays(cals);

            /*Calendar[] calsTwo = new Calendar[2];
            calsTwo[0] = Calendar.getInstance();
            calsTwo[0].set(2018, 4, 12);
            datePickerDialog.setDisabledDays(calsTwo);*/

            NYLog.e("CEK CALE final : "+cals.length);

        } else {
            datePickerDialog = DatePickerDialog.newInstance(this, year, month, day);
            datePickerDialog.setMinDate(c);
            date = String.valueOf(c.getTimeInMillis()/1000);
            datetimeTextView.setText(String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
        }

    }


    private void initExtra() {
        Intent intent = getActivity().getIntent();
        Bundle extras = getActivity().getIntent().getExtras();
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

                //Toast.makeText(this, "Type : "+type, Toast.LENGTH_SHORT).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            // and get whatever type user account id is
        }

        if (extras != null){

            if (intent.hasExtra(NYHelper.CERTIFICATE)){
                certificateCheckBox.setChecked(intent.getBooleanExtra(NYHelper.CERTIFICATE,false));
            }

            if (intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(intent.getStringExtra(NYHelper.SCHEDULE))){
                Calendar c = Calendar.getInstance();
                date = intent.getStringExtra(NYHelper.SCHEDULE);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DoDiveActivity activity = (DoDiveActivity)getActivity();
                if(activity.isEcoTrip()) {
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
                diverTextView.setText(intent.getStringExtra(NYHelper.DIVER));
                diver = intent.getStringExtra(NYHelper.DIVER);
            }


        }

        //TODO HARCODE ECOTRIP!!
        DoDiveActivity activity = (DoDiveActivity) getActivity();
        if(activity.isEcoTrip()) {
            keywordTextView.setText("Save Our Small Island");
            type = "3";
            diverId = "23";
        }
    }

    private void initControl() {

        keywordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoDiveActivity activity = (DoDiveActivity) getActivity();
                if(!activity.isEcoTrip()) {
                    Intent intent = new Intent(getActivity(), DoDiveSearchActivity.class);
                    intent.putExtra(NYHelper.CERTIFICATE, certificateCheckBox.isChecked());
                    intent.putExtra(NYHelper.SCHEDULE, date);
                    intent.putExtra(NYHelper.DIVER, diver);
                    if (activity.isEcoTrip()) {
                        intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                    }

                    startActivity(intent);
                }
            }
        });


        licenseLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificateCheckBox.setChecked(!certificateCheckBox.isChecked());
            }
        });

        datetimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

            }
        });

        diverLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYCustomDialog dialog = new NYCustomDialog();
                dialog.showTotalDiverDialog(getActivity());
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: get diver count
                //String diver = diverTextView.getText().toString();
                String certificate = "0";
                if (certificateCheckBox.isChecked()){
                    certificate = "1";
                } else {
                    certificate = "0";
                }


                if (!NYHelper.isStringNotEmpty(type)){
                    Toast.makeText(getActivity(), getString(R.string.warn_empty_keyword), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(diver)) {
                    Toast.makeText(getActivity(), getString(R.string.warn_total_diver), Toast.LENGTH_SHORT).show();
                } else {

                    //NYLog.e("CEK DATE 0 : "+date);
                    DoDiveActivity activity = (DoDiveActivity) getActivity();
                    //NYLog.e("HAS EXTRA " + activity.isEcoTrip());

                    // TODO: ganti fragment yg dulu activity & yg dulu EXTRA sekarang BUNDLE
                    Intent intent;
                    if (type.equals("3")){
                        intent = new Intent(getActivity(), DiveCenterDetailActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        if (activity.getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                            intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                        }
                        startActivity(intent);
                    } else if (type.equals("4") || type.equals("5") || type.equals("6")){

                        intent = new Intent(getActivity(), DoDiveSearchResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        if (activity.getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                            intent.putExtra(NYHelper.ECO_TRIP, 1);
                        }
                        startActivity(intent);

                        /*Bundle args = new Bundle();
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        args.putString(NYHelper.DIVE_CENTER, diveCenter.toString());
                        args.putString(NYHelper.KEYWORD, keyword);
                        args.putString(NYHelper.ID_DIVER, diverId);
                        args.putString(NYHelper.CERTIFICATE, certificate);
                        args.putString(NYHelper.SCHEDULE, date);
                        args.putString(NYHelper.DIVER, diver);
                        args.putString(NYHelper.TYPE, type);

                        DoDiveResultDiveSpotsFragment doDiveResultDiveSpotsFragment = new DoDiveResultDiveSpotsFragment();
                        doDiveResultDiveSpotsFragment.setArguments(args);

                        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container,doDiveResultDiveSpotsFragment);
                        fragmentTransaction.addToBackStack(doDiveResultDiveSpotsFragment.getClass().getName());
                        fragmentTransaction.commitAllowingStateLoss();*/

                    } else {
                        intent = new Intent(getActivity(), DoDiveSearchResultActivity.class);
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                        intent.putExtra(NYHelper.KEYWORD, keyword);
                        intent.putExtra(NYHelper.ID_DIVER, diverId);
                        intent.putExtra(NYHelper.CERTIFICATE, certificate);
                        intent.putExtra(NYHelper.SCHEDULE, date);
                        intent.putExtra(NYHelper.DIVER, diver);
                        intent.putExtra(NYHelper.TYPE, type);
                        if (activity.getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
                            intent.putExtra(NYHelper.ECO_TRIP, 1);
                        }
                        startActivity(intent);

                        /*Bundle args = new Bundle();
                        DiveCenter diveCenter = new DiveCenter();
                        diveCenter.setId(diverId);
                        args.putString(NYHelper.DIVE_CENTER, diveCenter.toString());
                        args.putString(NYHelper.KEYWORD, keyword);
                        args.putString(NYHelper.ID_DIVER, diverId);
                        args.putString(NYHelper.CERTIFICATE, certificate);
                        args.putString(NYHelper.SCHEDULE, date);
                        args.putString(NYHelper.DIVER, diver);
                        args.putString(NYHelper.TYPE, type);

                        DoDiveResultDiveCenterFragment doDiveResultDiveCenterFragment = new DoDiveResultDiveCenterFragment();
                        doDiveResultDiveCenterFragment.setArguments(args);

                        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container,doDiveResultDiveCenterFragment);
                        fragmentTransaction.addToBackStack(doDiveResultDiveCenterFragment.getClass().getName());
                        fragmentTransaction.commitAllowingStateLoss();*/
                    }

                }
            }
        });
    }

    private void initView(View v) {
        keywordTextView = (com.nyelam.android.view.font.NYTextView) v.findViewById(R.id.keyword_textView);
        //diverTextView = (TextView) v.findViewById(R.id.diver_textView);
        diverSpinner = (Spinner) v.findViewById(R.id.diver_spinner);
        datetimeTextView = (TextView)v.findViewById(R.id.datetime_textView);
        certificateTextView = (TextView) v.findViewById(R.id.certificate_textView);
        diverTextView = (TextView) v.findViewById(R.id.diver_textView);
        searchTextView = (TextView) v.findViewById(R.id.search_textView);
        certificateCheckBox = (CheckBox) v.findViewById(R.id.certificate_checkBox);
        diverLinearLayout = (LinearLayout) v.findViewById(R.id.diver_linearLayout);
        datetimeLinearLayout = (LinearLayout) v.findViewById(R.id.datetime_linearLayout);
        licenseLinearLayout = (LinearLayout) v.findViewById(R.id.license_linearLayout);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof DoDiveActivity && ((DoDiveActivity) getActivity()).isEcoTrip()){
            ((DoDiveActivity)getActivity()).setTitle(getString(R.string.eco_trip), true, false);
        } else {
            ((DoDiveActivity)getActivity()).setTitle(getString(R.string.do_dive), true, false);
        }
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

        //NYLog.e("TIMESTAMP " + cal.getTimeInMillis());
        date = String.valueOf(cal.getTimeInMillis()/1000);
        //NYLog.e("TIMES STAMP " + cal.getTimeInMillis());
        datetimeTextView.setText(String.valueOf(d) + "/" + String.valueOf(m+1) + "/" + String.valueOf(y));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        //void onDiver(String diver);
    }

    public void setDiver(String diver){
        diverTextView.setText(diver);
        this.diver = diver;
    }


}
