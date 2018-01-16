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
import com.nyelam.android.data.Participant;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.LoginStorage;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceActivity extends BasicActivity {


    private LinearLayout particpantContainerLinearLayout;
    private TextView contactNameTextView, contactPhoneNumberTextView, contactEmailTextView;

    int diver = 3;
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
    }

    private void initData() {
        for (int i = 0; i < diver; i++){
            participantList.add(i, new Participant());
        }
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

    private void initView() {
        particpantContainerLinearLayout = (LinearLayout) findViewById(R.id.participant_container_linearLayout);
        contactNameTextView = (TextView) findViewById(R.id.contact_name_textView);
        contactPhoneNumberTextView = (TextView) findViewById(R.id.contact_phone_number_textView);
        contactEmailTextView = (TextView) findViewById(R.id.contact_email_textView);
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
                    Intent intent = new Intent(BookingServiceActivity.this, BookingServiceParticipantActivity.class);
                    intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                    intent.putExtra(NYHelper.PARTICIPANT, participant.toString());
                    intent.putExtra(NYHelper.CONTACT, contact.toString());
                    intent.putExtra(NYHelper.POSITION, String.valueOf(position));
                    startActivity(intent);
                }
            });


            fillLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(BookingServiceActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookingServiceActivity.this, BookingServiceParticipantActivity.class);
                    intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                    intent.putExtra(NYHelper.PARTICIPANT, participant.toString());
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
