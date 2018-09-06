package com.nyelam.android.booking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.data.BookingContact;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.EquipmentRent;
import com.nyelam.android.data.EquipmentRentList;
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
    private TextView serviceNameTextView, scheduleTextView, diveCenterNameTextView, locationTextView;
    private TextView contactNameTextView, contactPhoneNumberTextView, contactEmailTextView, changeContactTextView;
    private TextView detailPriceTextView, subTotalPriceTextView, totalPriceTextView, ratingTextView, visitedTextView;
    private RatingBar ratingBar;

    private DiveService diveService;
    private int diver = 0;
    private String schedule = "0";
    private String certificate = "0";
    private List<Participant> participantList = new ArrayList<>();
    private BookingContact bookingContact;
    //private String cartToken;
    private CartReturn cartReturn;
    private DiveCenter diveCenter;
    private EquipmentRentList equipmentRents;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NYHelper.REQ_CART_EXPIRED) {
            if(resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    private void initControl() {
        changeContactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingServiceActivity.this, BookingServiceContactActivity.class);
                intent.putExtra(NYHelper.SERVICE, diveService.toString());
                //intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                intent.putExtra(NYHelper.CART_RETURN, cartReturn.toString());
                intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                intent.putExtra(NYHelper.CONTACT, bookingContact.toString());
                intent.putExtra(NYHelper.SCHEDULE, schedule);
                intent.putExtra(NYHelper.DIVER, String.valueOf(diver));
                intent.putExtra(NYHelper.CERTIFICATE, certificate);
                intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                if(equipmentRents != null) {
                    intent.putExtra(NYHelper.EQUIPMENT_RENTS, equipmentRents.toString());
                }
                startActivity(intent);
            }
        });

        nextLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isContactEmpty = false;
                boolean isParticipantEmpty = false;

                if (bookingContact != null){
                    if (!NYHelper.isStringNotEmpty(bookingContact.getName())){
                        isContactEmpty = true;
                    } else if (!isContactEmpty && !NYHelper.isStringNotEmpty(bookingContact.getPhoneNumber())){
                        isContactEmpty = true;
                    } else if (!isContactEmpty && !NYHelper.isStringNotEmpty(bookingContact.getEmail())){
                        isContactEmpty = true;
                    } else if (!isParticipantEmpty){
                        for (Participant p : participantList){
                            if (p == null || !NYHelper.isStringNotEmpty(p.getName()) || !NYHelper.isStringNotEmpty(p.getEmail())){
                                isParticipantEmpty = true;
                                break;
                            }
                        }
                    }
                }

                if (isContactEmpty){
                    Toast.makeText(BookingServiceActivity.this, getString(R.string.warn_field_contact_details_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (isParticipantEmpty){
                    Toast.makeText(BookingServiceActivity.this, getString(R.string.warn_field_participant_details_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(BookingServiceActivity.this, BookingServiceSummaryActivity.class);
                    intent.putExtra(NYHelper.SERVICE, diveService.toString());
                    //intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                    intent.putExtra(NYHelper.CART_RETURN, cartReturn.toString());
                    intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                    intent.putExtra(NYHelper.CONTACT, bookingContact.toString());
                    intent.putExtra(NYHelper.SCHEDULE, schedule);
                    intent.putExtra(NYHelper.DIVER, String.valueOf(diver));
                    intent.putExtra(NYHelper.CERTIFICATE, certificate);
                    intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                    if(equipmentRents != null) {
                        intent.putExtra(NYHelper.EQUIPMENT_RENTS, equipmentRents.toString());
                    }
                    startActivityForResult(intent, NYHelper.REQ_CART_EXPIRED);
                }
            }
        });
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            //NYLog.e("CEK INI 2 :"+extras.get(NYHelper.SERVICE));

            /*if (intent.hasExtra(NYHelper.CART_TOKEN)){
                cartToken = extras.getString(NYHelper.CART_TOKEN);
            }*/

            if (intent.hasExtra(NYHelper.DIVER)){
                diver = Integer.valueOf(intent.getStringExtra(NYHelper.DIVER));
            }

            if (intent.hasExtra(NYHelper.SCHEDULE) && extras.get(NYHelper.SCHEDULE) != null){
                schedule = intent.getStringExtra(NYHelper.SCHEDULE);
            }

            if (intent.hasExtra(NYHelper.CERTIFICATE) && extras.get(NYHelper.CERTIFICATE) != null){
                certificate = intent.getStringExtra(NYHelper.CERTIFICATE);
            }



            if (intent.hasExtra(NYHelper.CART_RETURN) && extras.get(NYHelper.CART_RETURN) != null){
                try {
                    cartReturn = new CartReturn();
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.CART_RETURN));
                    cartReturn.parse(obj);

                    if (cartReturn != null && cartReturn.getCart() != null){
                        Cart cart = cartReturn.getCart();
                        if (cart != null){
                            subTotalPriceTextView.setText(NYHelper.priceFormatter(cart.getSubTotal()));
                            totalPriceTextView.setText(NYHelper.priceFormatter(cart.getTotal()));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(intent.hasExtra(NYHelper.EQUIPMENT_RENTS)) {
                try {
                    String equipmentRentsArray = intent.getStringExtra(NYHelper.EQUIPMENT_RENTS);
                    equipmentRents.parse(new JSONArray(equipmentRentsArray));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (intent.hasExtra(NYHelper.SERVICE) && extras.get(NYHelper.SERVICE) != null){

                diveService = new DiveService();

                NYLog.e("Cek Dive Service di Booking Service : "+diveService.toString());

                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.SERVICE));
                    diveService.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                NYLog.e("Cek Dive Service di Booking Service 2 : "+diveService.toString());

                if (cartReturn != null && cartReturn.getCart() != null){
                    subTotalPriceTextView.setText(NYHelper.priceFormatter(cartReturn.getCart().getCurrency(), cartReturn.getCart().getSubTotal()));
                    totalPriceTextView.setText(NYHelper.priceFormatter(cartReturn.getCart().getCurrency(), cartReturn.getCart().getTotal()));
                }

                if (diveService != null){
                    if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());

                    if (diveService.getSpecialPrice() < diveService.getNormalPrice() && diveService.getSpecialPrice() > 0){
                        detailPriceTextView.setText(diver+" x @"+NYHelper.priceFormatter(diveService.getSpecialPrice()));
                    } else {
                        detailPriceTextView.setText(diver+" x @"+NYHelper.priceFormatter(diveService.getNormalPrice()));
                    }

                    /*if (diveService.getSpecialPrice() < diveService.getNormalPrice()){
                        totalPriceTextView.setText(NYHelper.priceFormatter(diveService.getSpecialPrice()));
                    } else {
                        totalPriceTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                    }*/

                    // TODO: location from where ?
                    /*if (diveService.getDiveCenter() != null && diveService.getDiveCenter().getContact() != null && diveService.getDiveCenter().getContact().getLocation() != null){
                        Location location = diveService.getDiveCenter().getContact().getLocation();
                        String locationText = "";
                        if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locationText=locationText+location.getCity().getName();
                        if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locationText=locationText+", "+location.getCity().getName();
                        if (location.getCountry() != null && NYHelper.isStringNotEmpty(location.getCountry())) locationText=locationText+", "+location.getCountry();
                        locationTextView.setText(locationText);
                    }*/

                    // TODO: location from where ?
                    if (diveService.getDiveSpots() != null && diveService.getDiveSpots().size() > 0 && diveService.getDiveSpots().get(0).getLocation() != null){
                        Location location = diveService.getDiveSpots().get(0).getLocation();
                        String locationText = "";
                        if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locationText=locationText+location.getCity().getName();
                        if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locationText=locationText+", "+location.getCity().getName();
                        if (location.getCountry() != null && NYHelper.isStringNotEmpty(location.getCountry())) locationText=locationText+", "+location.getCountry();
                        locationTextView.setText(locationText);
                    }





                    // TODO: Rating and Visited
                    ratingBar.setRating(diveService.getRating());
                    ratingTextView.setText(String.valueOf(diveService.getRating()));
                    //visitedTextView.setText(String.valueOf(diveService.getVisited()));
                    // TODO: schedule
                    /*if (diveService.getSchedule() != null){
                        long start = diveService.getSchedule().getStartDate();
                        long end = diveService.getSchedule().getEndDate();
                        scheduleTextView.setText(NYHelper.setMillisToDate(start)+" - "+NYHelper.setMillisToDate(end));
                    }*/

                    if (NYHelper.isStringNotEmpty(schedule)) scheduleTextView.setText(NYHelper.setMillisToDate(Long.valueOf(schedule)));
                }

            }

            if (intent.hasExtra(NYHelper.DIVE_CENTER) && extras.get(NYHelper.DIVE_CENTER) != null) {

                diveCenter = new DiveCenter();

                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.DIVE_CENTER));
                    diveCenter.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (diveCenter != null){

                    if (NYHelper.isStringNotEmpty(diveCenter.getName())){
                        diveCenterNameTextView.setText(diveCenter.getName());
                    }

                    if (diveCenter.getContact() != null && diveCenter.getContact().getLocation() != null){
                        Location location = diveCenter.getContact().getLocation();
                        String locString = "";
                        if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locString += location.getCity().getName();
                        if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locString += ", "+location.getProvince().getName();
                        if (NYHelper.isStringNotEmpty(location.getCountry())) locString += ", "+location.getCountry();
                        locationTextView.setText(locString);
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
                    bookingContact = new BookingContact();
                    bookingContact.parse(obj);

                    if (NYHelper.isStringNotEmpty(bookingContact.getName())){
                        contactNameTextView.setText(bookingContact.getName());
                        contactNameTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_black1));
                    } else {
                        contactNameTextView.setText(getString(R.string.full_name));
                        contactNameTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey4));
                    }

                    if (bookingContact.getCountryCode() != null && NYHelper.isStringNotEmpty(bookingContact.getCountryCode().getCountryNumber()) && NYHelper.isStringNotEmpty(bookingContact.getPhoneNumber())) contactPhoneNumberTextView.setText("+"+bookingContact.getCountryCode().getCountryNumber()+bookingContact.getPhoneNumber());
                    if (NYHelper.isStringNotEmpty(bookingContact.getEmail()))contactEmailTextView.setText(bookingContact.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                LoginStorage storage = new LoginStorage(this);
                if (storage.isUserLogin() && storage.user != null){
                    bookingContact = new BookingContact();
                    bookingContact.setName(storage.user.getFullname());
                    if(storage.user.getCountryCode() != null) {
                        CountryCode c = storage.user.getCountryCode();
                        bookingContact.setPhoneNumber(storage.user.getPhone());
                        bookingContact.setCountryCode(c);
                        contactPhoneNumberTextView.setText("+" + c.getCountryNumber() + storage.user.getPhone());
                    }

                    bookingContact.setEmail(storage.user.getEmail());
                    if (NYHelper.isStringNotEmpty(storage.user.getFullname())){
                        contactNameTextView.setText(storage.user.getFullname());
                        contactNameTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_black1));
                    } else {
                        contactNameTextView.setText(getString(R.string.full_name));
                        contactNameTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey4));
                    }

                    if (NYHelper.isStringNotEmpty(storage.user.getEmail()))contactEmailTextView.setText(storage.user.getEmail());
                }
            }

        }

    }

    private void initView() {

        serviceNameTextView = (TextView) findViewById(R.id.service_name_textView);
        scheduleTextView = (TextView) findViewById(R.id.schedule_textView);
        diveCenterNameTextView = (TextView) findViewById(R.id.dive_center_name_textView);
        locationTextView = (TextView) findViewById(R.id.location_textView);
        detailPriceTextView = (TextView) findViewById(R.id.detail_price_textView);
        subTotalPriceTextView = (TextView) findViewById(R.id.sub_total_price_textView);
        totalPriceTextView = (TextView) findViewById(R.id.total_price_textView);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingTextView = (TextView) findViewById(R.id.rating_textView);
        visitedTextView = (TextView) findViewById(R.id.visited_textView);

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
            TextView emailTextView = (TextView) myParticipantsView.findViewById(R.id.email_textView);
            LinearLayout linearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);
            LinearLayout fillLinearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);

            if (participant != null && NYHelper.isStringNotEmpty(participant.getName())) {
                if (NYHelper.isStringNotEmpty(participant.getName())) nameTextView.setText(participant.getName());
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                nameTextView.setText("Diver "+String.valueOf(position+1));
                linearLayout.setVisibility(View.VISIBLE);
            }

            if (participant != null && NYHelper.isStringNotEmpty(participant.getEmail())) {
                if (NYHelper.isStringNotEmpty(participant.getEmail())) emailTextView.setText(participant.getEmail());
            } else {
                emailTextView.setText("diver"+String.valueOf(position+1)+"@email.com");
            }

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(BookingServiceActivity.this, BookingServiceParticipantActivity.class);
                    intent.putExtra(NYHelper.SERVICE, diveService.toString());
                    //intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                    intent.putExtra(NYHelper.CART_RETURN, cartReturn.toString());
                    intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                    intent.putExtra(NYHelper.CONTACT, bookingContact.toString());
                    intent.putExtra(NYHelper.POSITION, position);
                    intent.putExtra(NYHelper.SCHEDULE, schedule);
                    intent.putExtra(NYHelper.DIVER, String.valueOf(diver));
                    intent.putExtra(NYHelper.CERTIFICATE, certificate);
                    intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                    if(equipmentRents != null) {
                        intent.putExtra(NYHelper.EQUIPMENT_RENTS, equipmentRents.toString());
                    }
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
                    //intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                    intent.putExtra(NYHelper.CART_RETURN, cartReturn.toString());
                    intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                    intent.putExtra(NYHelper.CONTACT, bookingContact.toString());
                    intent.putExtra(NYHelper.POSITION, position);
                    intent.putExtra(NYHelper.SCHEDULE, schedule);
                    intent.putExtra(NYHelper.DIVER, String.valueOf(diver));
                    intent.putExtra(NYHelper.CERTIFICATE, certificate);
                    intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                    if(equipmentRents != null) {
                        intent.putExtra(NYHelper.EQUIPMENT_RENTS, equipmentRents.toString());
                    }
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
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
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
