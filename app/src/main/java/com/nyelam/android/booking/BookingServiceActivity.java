package com.nyelam.android.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Participant;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.LoginStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceActivity extends BasicActivity {

    private LinearLayout particpantContainerLinearLayout, nextLinearLayout;
    private TextView serviceNameTextView, scheduleTextView, locationTextView, priceTextView;
    private TextView contactNameTextView, contactPhoneNumberTextView, contactEmailTextView, changeContactTextView;

    private DiveService diveService;
    private int diver = 3;
    private List<Participant> participantList = new ArrayList<>();
    private Contact contact;
    private String cartToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service);
        initToolbar();
        initView();
        initData();
        initParticipantLayout();
        initControl();
    }

    private void initControl() {
        changeContactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingServiceActivity.this, BookingServiceContactActivity.class);
                intent.putExtra(NYHelper.SERVICE, diveService.toString());
                intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                intent.putExtra(NYHelper.CONTACT, contact.toString());
                startActivity(intent);
            }
        });

        nextLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingServiceActivity.this, BookingServiceSummaryActivity.class);
                intent.putExtra(NYHelper.SERVICE, diveService.toString());
                intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                intent.putExtra(NYHelper.CONTACT, contact.toString());
                startActivity(intent);
            }
        });
    }

    private void initData() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //NYLog.e("CEK INI 2 :"+extras.get(NYHelper.SERVICE));

            if (extras.get(NYHelper.SERVICE) != null){

                diveService = new DiveService();

                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.SERVICE));
                    diveService.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (diveService != null){
                    if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());

                    if (diveService.getSpecialPrice() < diveService.getNormalPrice()){
                        priceTextView.setText(NYHelper.priceFormatter(diveService.getSpecialPrice()));
                    } else {
                        priceTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                    }

                    if (diveService.getDiveCenter() != null && diveService.getDiveCenter().getLocation() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getLocation().getCountry())) {
                        Location loc = new Location();
                        loc = diveService.getDiveCenter().getLocation();
                        locationTextView.setText(loc.getCity()+", "+loc.getProvince()+", "+loc.getCountry());
                    }

                    if (diveService.getSchedule() != null){
                        long start = diveService.getSchedule().getStartDate();
                        long end = diveService.getSchedule().getEndDate();
                        scheduleTextView.setText(NYHelper.setMillisToDate(start)+" - "+NYHelper.setMillisToDate(end));
                    }
                }

            }


            if (extras.get(NYHelper.PARTICIPANT) == null){
                for (int i = 0; i < diver; i++){
                    participantList.add(i, new Participant());
                }
            } else {

                JSONArray array = null;
                try {
                    array = new JSONArray(extras.getString(NYHelper.PARTICIPANT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (array != null && array.length() > 0) {
                    participantList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = null;
                        try {
                            o = array.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Participant p = new Participant();
                        p.parse(o);
                        participantList.add(p);
                    }
                } else {
                    for (int i = 0; i < diver; i++){
                        participantList.add(i, new Participant());
                    }
                }

            }

            if (extras.get(NYHelper.CONTACT) != null){

                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.CONTACT));
                    contact = new Contact();
                    contact.parse(obj);

                    if (NYHelper.isStringNotEmpty(contact.getName()))contactNameTextView.setText(contact.getName());
                    if (NYHelper.isStringNotEmpty(contact.getPhoneNumber()))contactPhoneNumberTextView.setText(contact.getPhoneNumber());
                    if (NYHelper.isStringNotEmpty(contact.getEmail()))contactEmailTextView.setText(contact.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                LoginStorage storage = new LoginStorage(this);
                if (storage.isUserLogin() && storage.user != null){
                    contact = new Contact();
                    contact.setName(storage.user.getFullname());
                    contact.setPhoneNumber(storage.user.getPhone());
                    contact.setEmail(storage.user.getEmail());
                    if (NYHelper.isStringNotEmpty(storage.user.getFullname()))contactNameTextView.setText(storage.user.getFullname());
                    if (NYHelper.isStringNotEmpty(storage.user.getPhone()))contactPhoneNumberTextView.setText(storage.user.getPhone());
                    if (NYHelper.isStringNotEmpty(storage.user.getEmail()))contactEmailTextView.setText(storage.user.getEmail());
                }
            }

        }

    }

    private void initView() {

        serviceNameTextView = (TextView) findViewById(R.id.service_name_textView);
        scheduleTextView = (TextView) findViewById(R.id.schedule_textView);
        locationTextView = (TextView) findViewById(R.id.location_textView);
        priceTextView = (TextView) findViewById(R.id.price_textView);

        particpantContainerLinearLayout = (LinearLayout) findViewById(R.id.participant_container_linearLayout);
        contactNameTextView = (TextView) findViewById(R.id.contact_name_textView);
        contactPhoneNumberTextView = (TextView) findViewById(R.id.contact_phone_number_textView);
        contactEmailTextView = (TextView) findViewById(R.id.contact_email_textView);
        changeContactTextView = (TextView) findViewById(R.id.change_contact_textView);
        nextLinearLayout = (LinearLayout) findViewById(R.id.next_linearLayout);

    }

    private void initParticipantLayout() {

        particpantContainerLinearLayout.removeAllViews();
        int pos = 0;
        for (final Participant participant : participantList) {

            final int position = pos;

            LayoutInflater linflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myParticipantsView = linflaterAddons.inflate(R.layout.view_item_participant, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(this, 10));
            myParticipantsView.setLayoutParams(layoutParamsAddons);

            //myViewAddons.setId(0);

            TextView nameTextView = (TextView) myParticipantsView.findViewById(R.id.name_textView);
            TextView changeTextView = (TextView) myParticipantsView.findViewById(R.id.change_textView);
            LinearLayout fillLinearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);

            if (participant != null && NYHelper.isStringNotEmpty(participant.getName())) {
                if (NYHelper.isStringNotEmpty(participant.getName())) nameTextView.setText(participant.getName());
                changeTextView.setVisibility(View.VISIBLE);
            } else {
                nameTextView.setText("Participant "+String.valueOf(position+1));
                fillLinearLayout.setVisibility(View.VISIBLE);
            }

            changeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(BookingServiceActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BookingServiceActivity.this, BookingServiceParticipantActivity.class);
                    intent.putExtra(NYHelper.SERVICE, diveService.toString());
                    intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                    intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                    intent.putExtra(NYHelper.CONTACT, contact.toString());
                    intent.putExtra(NYHelper.POSITION, position);
                    startActivity(intent);
                }
            });


            fillLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(BookingServiceActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(BookingServiceActivity.this, String.valueOf(participantList.size()), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BookingServiceActivity.this, BookingServiceParticipantActivity.class);
                    intent.putExtra(NYHelper.SERVICE, diveService.toString());
                    intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                    intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                    intent.putExtra(NYHelper.CONTACT, contact.toString());
                    intent.putExtra(NYHelper.POSITION, position);
                    startActivity(intent);
                }
            });

            pos++;
            particpantContainerLinearLayout.addView(myParticipantsView);

        }

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
        savedInstanceState.putString("MyString", "Welcome back to Android");
        // etc.
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
