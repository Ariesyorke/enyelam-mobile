package com.nyelam.android.booking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.storage.PaymentMethodStorage;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.VeritransNotificationActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Additional;
import com.nyelam.android.data.BookingContact;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.NTransactionResult;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.OrderReturn;
import com.nyelam.android.data.Participant;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.http.NYCartExpiredException;
import com.nyelam.android.http.NYChangePaymentMethodRequest;
import com.nyelam.android.http.NYDoDiveServiceCartRequest;
import com.nyelam.android.http.NYDoDiveServiceOrderRequest;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.storage.VeritransStorage;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceSummaryActivity extends BasicActivity implements NYCustomDialog.OnDialogFragmentClickListener, TransactionFinishedCallback {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private ProgressDialog progressDialog;
    private DiveService diveService;
    private int diver = 0;
    private String schedule = "0";
    private String certificate = "0";
    private String paymentType = "1"; // 1= bank transfer 2 = midtrans
    private String paymentMethod = null; // 1 = virtual account dan 2 = credit card
    private String note;
    private List<Participant> participantList = new ArrayList<>();
    private BookingContact bookingContact;
    //private String cartToken;
    private CartReturn cartReturn;
    private OrderReturn orderReturn;
    private boolean isTranssactionFailed = false;

    private LinearLayout particpantContainerLinearLayout, orderLinearLayout, serviceFeeLinearLayout;
    private TextView serviceNameTextView, scheduleTextView, diveCenterNameTextView, locationTextView;
    private TextView contactNameTextView, contactPhoneNumberTextView, contactEmailTextView, changeContactTextView;
    private TextView diverCountTextView, detailPriceTextView, subTotalPriceTextView, totalPriceTextView, ratingTextView, visitedTextView;
    private RatingBar ratingBar;
    private DiveCenter center;
    private RadioGroup radioGroup;
    private LinearLayout addNoteLinearLayout;
    private EditText noteEditText;
    private RadioButton bankTransferRadioButton, virtualAccountRadioButton, creditCardRadioButton;
    private LinearLayout bankTransferLinearLayout, virtualAccountLinearLayout, creditCardLinearLayout;
    private DiveCenter diveCenter;
    private String veritransToken;
    private TextView expiredDateTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service_summary);
        NYApplication application = (NYApplication) getApplication();
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
                Intent intent = new Intent(BookingServiceSummaryActivity.this, BookingServiceContactActivity.class);
                intent.putExtra(NYHelper.SERVICE, diveService.toString());
                //intent.putExtra(NYHelper.CART_TOKEN, cartToken);
                intent.putExtra(NYHelper.CART_RETURN, cartReturn.toString());
                intent.putExtra(NYHelper.PARTICIPANT, participantList.toString());
                intent.putExtra(NYHelper.CONTACT, bookingContact.toString());
                intent.putExtra(NYHelper.SCHEDULE, schedule);
                intent.putExtra(NYHelper.DIVER, String.valueOf(diver));
                intent.putExtra(NYHelper.CERTIFICATE, certificate);
                intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
                startActivity(intent);
            }
        });

        orderLinearLayout.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(BookingServiceSummaryActivity.this, getString(R.string.warn_field_contact_details_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (isParticipantEmpty){
                    Toast.makeText(BookingServiceSummaryActivity.this, getString(R.string.warn_field_participant_details_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else {
                    if (orderReturn == null && isTranssactionFailed){
                        // TODO: request ulang cart token atau cart return
                        new NYCustomDialog().showAgreementDialog(BookingServiceSummaryActivity.this);
                    } else if (orderReturn == null){
                        new NYCustomDialog().showAgreementDialog(BookingServiceSummaryActivity.this);
                    } else {
                        payUsingVeritrans();
                    }
                }

            }
        });

        virtualAccountRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (virtualAccountRadioButton.isChecked()){
                    creditCardRadioButton.setChecked(false);
                    bankTransferRadioButton.setChecked(false);
                    paymentType = "2";
                    paymentMethod = "1";
                    requestChangePaymentMethod();
                }
            }
        });

        creditCardRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditCardRadioButton.isChecked()){
                    virtualAccountRadioButton.setChecked(false);
                    bankTransferRadioButton.setChecked(false);
                    paymentType = "2";
                    paymentMethod = "2";
                    requestChangePaymentMethod();
                }
            }
        });

        bankTransferRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankTransferRadioButton.isChecked()){
                    virtualAccountRadioButton.setChecked(false);
                    creditCardRadioButton.setChecked(false);
                    paymentType = "1";
                    paymentMethod = null;
                    requestChangePaymentMethod();
                }
            }
        });

        virtualAccountLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                virtualAccountRadioButton.setChecked(true);
                creditCardRadioButton.setChecked(false);
                bankTransferRadioButton.setChecked(false);
                paymentType = "2";
                paymentMethod = "1";
                requestChangePaymentMethod();
            }
        });

        creditCardLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditCardRadioButton.setChecked(true);
                virtualAccountRadioButton.setChecked(false);
                bankTransferRadioButton.setChecked(false);
                paymentType = "2";
                paymentMethod = "2";
                requestChangePaymentMethod();
            }
        });

        bankTransferLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankTransferRadioButton.setChecked(true);
                virtualAccountRadioButton.setChecked(false);
                creditCardRadioButton.setChecked(false);
                paymentType = "1";
                paymentMethod = null;
                requestChangePaymentMethod();
            }
        });

        /*addNoteLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteLinearLayout.setVisibility(View.GONE);
                noteEditText.setVisibility(View.VISIBLE);
            }
        });*/

        /*noteEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    //check if the right key was pressed
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {
                        if (noteEditText.getText().toString().isEmpty()){
                            noteEditText.setVisibility(View.GONE);
                            addNoteLinearLayout.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                }
                return false;
            }
        });*/

        /*noteEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    Toast.makeText(BookingServiceSummaryActivity.this, "hilang", Toast.LENGTH_SHORT).show();
                    // code to execute when EditText loses focus

                } else {
                    Toast.makeText(BookingServiceSummaryActivity.this, "ada", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        /*noteEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (noteEditText.getText().toString().isEmpty()){
                        noteEditText.setVisibility(View.GONE);
                        addNoteLinearLayout.setVisibility(View.VISIBLE);
                    }
                }

                return false;
            }
        });*/

    }

    private void initData() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
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

            if (intent.hasExtra(NYHelper.DIVER)){
                diver = Integer.valueOf(intent.getStringExtra(NYHelper.DIVER));
            }

            if (intent.hasExtra(NYHelper.SCHEDULE) && extras.get(NYHelper.SCHEDULE) != null){
                schedule = intent.getStringExtra(NYHelper.SCHEDULE);
            }

            if (intent.hasExtra(NYHelper.CERTIFICATE) && extras.get(NYHelper.CERTIFICATE) != null){
                certificate = intent.getStringExtra(NYHelper.CERTIFICATE);
            }

            if (intent.hasExtra(NYHelper.NOTE) && NYHelper.isStringNotEmpty(intent.getStringExtra(NYHelper.NOTE))){
                noteEditText.setText(intent.getStringExtra(NYHelper.NOTE));
            }

            if (intent.hasExtra(NYHelper.CART_RETURN)){
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

            if (extras.get(NYHelper.SERVICE) != null){

                diveService = new DiveService();

                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.SERVICE));
                    diveService.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (cartReturn != null && cartReturn.getCart() != null){
                    subTotalPriceTextView.setText(NYHelper.priceFormatter(cartReturn.getCart().getCurrency(), cartReturn.getCart().getSubTotal()));
                    totalPriceTextView.setText(NYHelper.priceFormatter(cartReturn.getCart().getCurrency(), cartReturn.getCart().getTotal()));
                }

                if (diveService != null){
                    if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());

                    // TODO: set text price
                    if (diveService.getSpecialPrice() < diveService.getNormalPrice() && diveService.getSpecialPrice() > 0){
                        detailPriceTextView.setText(NYHelper.priceFormatter(diveService.getSpecialPrice()));
                    } else {
                        detailPriceTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                    }

                    diverCountTextView.setText("Service Trip Package x "+String.valueOf(diver));

                    /*if (diveService.getSpecialPrice() < diveService.getNormalPrice()){
                        priceTextView.setText(NYHelper.priceFormatter( diveService.getSpecialPrice()));
                    } else {
                        priceTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                    }*/

                    // TODO: location from where ?
                    if (diveService.getDiveCenter() != null && diveService.getDiveCenter().getContact() != null && diveService.getDiveCenter().getContact().getLocation() != null){
                        Location location = diveService.getDiveCenter().getContact().getLocation();
                        String locationText = "";
                        if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locationText=locationText+location.getCity().getName();
                        if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locationText=locationText+", "+location.getCity().getName();
                        if (location.getCountry() != null && NYHelper.isStringNotEmpty(location.getCountry())) locationText=locationText+", "+location.getCountry();
                        locationTextView.setText(locationText);
                    }


                    // TODO: Rating and Visited
                    ratingBar.setRating(diveService.getRating());
                    ratingTextView.setText(String.valueOf(diveService.getRating()));

                    // TODO: schedule
                    /*if (diveService.getSchedule() != null){
                        long start = diveService.getSchedule().getStartDate();
                        long end = diveService.getSchedule().getEndDate();
                        scheduleTextView.setText(NYHelper.setMillisToDate(start)+" - "+NYHelper.setMillisToDate(end));
                    }*/

                    if (NYHelper.isStringNotEmpty(schedule)) scheduleTextView.setText(NYHelper.setMillisToDate(Long.valueOf(schedule)));

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
        diveCenterNameTextView = (TextView)findViewById(R.id.dive_center_name_textView);
        locationTextView = (TextView) findViewById(R.id.location_textView);
        diverCountTextView = (TextView) findViewById(R.id.diver_count_textView);
        detailPriceTextView = (TextView) findViewById(R.id.detail_price_textView);
        subTotalPriceTextView = (TextView) findViewById(R.id.sub_total_price_textView);
        totalPriceTextView = (TextView) findViewById(R.id.total_price_textView);

        particpantContainerLinearLayout = (LinearLayout) findViewById(R.id.participant_container_linearLayout);
        contactNameTextView = (TextView) findViewById(R.id.contact_name_textView);
        contactPhoneNumberTextView = (TextView) findViewById(R.id.contact_phone_number_textView);
        contactEmailTextView = (TextView) findViewById(R.id.contact_email_textView);
        changeContactTextView = (TextView) findViewById(R.id.change_contact_textView);
        orderLinearLayout = (LinearLayout) findViewById(R.id.order_linearLayout);
        serviceFeeLinearLayout = (LinearLayout) findViewById(R.id.service_fee_linearLayout);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingTextView = (TextView) findViewById(R.id.rating_textView);
        visitedTextView = (TextView) findViewById(R.id.visited_textView);

        addNoteLinearLayout = (LinearLayout) findViewById(R.id.add_note_linearLayout);
        noteEditText = (EditText) findViewById(R.id.note_editText);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        bankTransferRadioButton = (RadioButton) findViewById(R.id.bankTransferRadioButton);
        virtualAccountRadioButton = (RadioButton) findViewById(R.id.virtualAccountRadioButton);
        creditCardRadioButton = (RadioButton) findViewById(R.id.creditCardRadioButton);

        bankTransferLinearLayout = (LinearLayout) findViewById(R.id.payment_bank_transfer_linearLayout);
        virtualAccountLinearLayout = (LinearLayout) findViewById(R.id.payment_virtual_account_linearLayout);
        creditCardLinearLayout = (LinearLayout) findViewById(R.id.payment_credit_card_linearLayout);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
    }

    private void initParticipantLayout() {

        particpantContainerLinearLayout.removeAllViews();
        int pos = 0;
        for (final Participant participant : participantList) {

            final int position = pos;

            LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myParticipantsView = inflaterAddons.inflate(R.layout.view_item_participant, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(this, 10));
            myParticipantsView.setLayoutParams(layoutParamsAddons);

            TextView nameTextView = (TextView) myParticipantsView.findViewById(R.id.name_textView);
            TextView emailTextView = (TextView) myParticipantsView.findViewById(R.id.email_textView);
            LinearLayout linearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);
            TextView wordingTextView = (TextView) myParticipantsView.findViewById(R.id.wording_textView  );
            LinearLayout fillLinearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);

            if (participant != null && NYHelper.isStringNotEmpty(participant.getName())) {
                if (NYHelper.isStringNotEmpty(participant.getName())){
                    nameTextView.setText(participant.getName());
                    wordingTextView.setText(getResources().getString(R.string.change));
                } else {
                    nameTextView.setText("Diver "+String.valueOf(position+1));
                    wordingTextView.setText(getResources().getString(R.string.fill_in));
                }
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                nameTextView.setText("Diver "+String.valueOf(position+1));
                wordingTextView.setText(getResources().getString(R.string.fill_in));
                fillLinearLayout.setVisibility(View.VISIBLE);
            }

            if (participant != null && NYHelper.isStringNotEmpty(participant.getEmail())) {
                if (NYHelper.isStringNotEmpty(participant.getEmail())) emailTextView.setText(participant.getEmail());
            } else {
                emailTextView.setText("diver"+String.valueOf(position+1)+"@email.com");
            }

            if (participant != null){
                wordingTextView.setText(getString(R.string.change));
            } else {
                wordingTextView.setText(getString(R.string.fill_in));
            }

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(BookingServiceActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BookingServiceSummaryActivity.this, BookingServiceParticipantActivity.class);
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
                    intent.putExtra(NYHelper.NOTE, noteEditText.getText().toString());
                    startActivity(intent);
                }
            });


            fillLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookingServiceSummaryActivity.this, BookingServiceParticipantActivity.class);
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
                    intent.putExtra(NYHelper.NOTE, noteEditText.getText().toString());
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

    private RequestListener<OrderReturn> onCreateOrderServiceRequest() {
        return new RequestListener<OrderReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if(spiceException != null) {
                    if (spiceException.getCause() instanceof NYCartExpiredException) {
                        NYHelper.handlePopupMessage(BookingServiceSummaryActivity.this, "Sorry, Your Cart Session has Expired. Please Re-Order.", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });
                    } else {
                        NYHelper.handleAPIException(BookingServiceSummaryActivity.this, spiceException, false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }
            }


            @Override
            public void onRequestSuccess(final OrderReturn result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                orderReturn = result;
                if (paymentType.equals("2") && result != null && result.getVeritransToken() != null){
                    //TODO KALO TYPE PEMBAYARANNYA MIDTRANS

                    VeritransStorage veritransStorage = new VeritransStorage(BookingServiceSummaryActivity.this);
                    veritransStorage.veritransToken = result.getVeritransToken().getTokenId();
                    veritransStorage.contact = result.getSummary().getContact();
                    veritransStorage.cart = result.getSummary().getOrder().getCart();
                    veritransStorage.order = result.getSummary().getOrder();
                    veritransStorage.totalParticipants = result.getSummary().getParticipants().size();
                    veritransStorage.save();

                    payUsingVeritrans();

                } else if (paymentType.equals("1")){
                    //TODO DISINI HANDLE KALO TRANSAKSI DI BANK TRANSFER SUKSES
                    NYHelper.handlePopupMessage(BookingServiceSummaryActivity.this, getString(R.string.transaction_success), false,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(BookingServiceSummaryActivity.this, HomeActivity.class);
                                        intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
                                        intent.putExtra(NYHelper.ID_ORDER, orderReturn.getSummary().getOrder().getOrderId());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }, getResources().getString(R.string.check_order));
                }
            }
        };
    }




    public void requestCartToken(){
        progressDialog.show();
        try {
            NYDoDiveServiceCartRequest req = new NYDoDiveServiceCartRequest(BookingServiceSummaryActivity.this, diveService.getId(), String.valueOf(diver), schedule, diveCenter.getId());
            spcMgr.execute(req, onCreateCartServiceRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<CartReturn> onCreateCartServiceRequest() {
        return new RequestListener<CartReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onRequestSuccess(final CartReturn result) {
                /*if (progressDialog != null) {
                    progressDialog.dismiss();
                }*/
                //progressDialog.show();

                cartReturn = result;

                if (result != null){
                    NYDoDiveServiceOrderRequest req = null;
                    try {
                        note = noteEditText.getText().toString();
                        req = new NYDoDiveServiceOrderRequest(BookingServiceSummaryActivity.this, cartReturn.getCartToken(), bookingContact.toServer(), participantList.toString(), paymentType, note);
                        spcMgr.execute(req, onCreateOrderServiceRequest());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                }

            }
        };
    }


    public void payUsingVeritrans() {

        SdkUIFlowBuilder.init()
                .setClientKey(getResources().getString(R.string.client_key_development)) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this)// set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(getResources().getString(R.string.api_veritrans_development)) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#0099EE", "#0099EE","#0099EE")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .buildSDK();


        PaymentMethodStorage paymentMethodStorage = new PaymentMethodStorage(this);
        paymentMethodStorage.paymentMethod = paymentMethod;
        paymentMethodStorage.save();

        VeritransStorage veritransStorage = new VeritransStorage(this);
        Contact contact = veritransStorage.contact;
        Order order = veritransStorage.order;
        String token = veritransStorage.veritransToken;
        Cart cart = veritransStorage.cart;
        DiveService service = veritransStorage.service;
        Integer totalParticipants = veritransStorage.totalParticipants;

        if (veritransStorage != null){
            UserDetail userDetail = null;

            if (userDetail == null) {
                userDetail = new UserDetail();
                if (contact != null){
                    userDetail.setUserFullName(contact.getName());
                    userDetail.setEmail(contact.getEmailAddress());
                    userDetail.setPhoneNumber(contact.getPhoneNumber());

                    LoginStorage loginStorage = new LoginStorage(this);
                    userDetail.setUserId(loginStorage.user.getUserId());
                    LocalDataHandler.saveObject("user_details", userDetail);
                }
            }

            UIKitCustomSetting setting = MidtransSDK.getInstance().getUIKitCustomSetting();
            setting.setSkipCustomerDetailsPages(true);
            MidtransSDK.getInstance().setUIKitCustomSetting(setting);

            TransactionRequest transactionRequest;
            if (cart != null){
                transactionRequest = new TransactionRequest(order.getOrderId(), cart.getTotal());
            } else {
                transactionRequest = new TransactionRequest(order.getOrderId(), 0);
            }

            // Create array list and add above item details in it and then set it to transaction request.
            ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
            if (service != null)itemDetailsList.add(new ItemDetails(service.getId(), (int)service.getNormalPrice(), totalParticipants, service.getName()));
            transactionRequest.setItemDetails(itemDetailsList);


            MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
            MidtransSDK.getInstance().startPaymentUiFlow(this, token);
            //MidtransSDK.getInstance().startPaymentUiFlow(this, "eba5b676-abea-4b6d-8f88-3ad1517f2e2e");
        }

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BookingServiceSummaryActivity.this);
        builder.setCancelable(false);
        builder.setTitle(null);
        builder.setMessage(getString(R.string.warn_cancel_booking));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

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
        /*savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
        savedInstanceState.putString("MyString", "Welcome back to Android");*/
        // etc.
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onChooseListener(Object position) {

    }

    @Override
    public void onAcceptAgreementListener() {

        if (isTranssactionFailed){
            requestCartToken();
        } else {
            progressDialog.show();
            note = noteEditText.getText().toString();
            try {
                NYDoDiveServiceOrderRequest req = new NYDoDiveServiceOrderRequest(BookingServiceSummaryActivity.this, cartReturn.getCartToken(), bookingContact.toServer(), participantList.toString(), paymentType, note);
                spcMgr.execute(req, onCreateOrderServiceRequest());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onCancelUpdate() {

    }

    @Override
    public void doUpdateVersion(String link) {
    }

    @Override
    public void onTransactionFinished(final TransactionResult transactionResult) {
        //TODO DISINI HANDLE KALO TRANSAKSI DI MIDTRANS SUKSES

        if (transactionResult != null){
            if (transactionResult.getResponse() != null)NYLog.e("CEK TRANSACTION 1: "+transactionResult.getResponse().getTransactionStatus());
            if (transactionResult.getResponse() != null && NYHelper.isStringNotEmpty(transactionResult.getResponse().getFraudStatus()))NYLog.e("CEK TRANSACTION 2 : "+transactionResult.getResponse().getFraudStatus());
            if (transactionResult.getResponse() != null && NYHelper.isStringNotEmpty(transactionResult.getResponse().getTransactionStatus()))NYLog.e("CEK TRANSACTION 3 : "+transactionResult.getResponse().getTransactionStatus());
        } else {
            NYLog.e("CEK TRANSACTION : null");
        }

        if (transactionResult != null && transactionResult.getResponse() != null && (transactionResult.getResponse().getFraudStatus() != null && transactionResult.getResponse().getFraudStatus().equals(NYHelper.NY_ACCEPT_FRAUD_STATUS) || transactionResult.getResponse().getFraudStatus().equals(NYHelper.TRANSACTION_PENDING)) ){
            NYHelper.handlePopupMessage(BookingServiceSummaryActivity.this, getString(R.string.transaction_success), false,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(BookingServiceSummaryActivity.this, VeritransNotificationActivity.class);
                            //if (gooLocation != null)intent.putExtra(MainActivity.ARG_ADDRESS, gooLocation.toString());
                            if (transactionResult.getResponse().getFraudStatus().equals(NYHelper.NY_ACCEPT_FRAUD_STATUS)) {
                                if(transactionResult.getResponse().getTransactionStatus().equals(NYHelper.NY_TRANSACTION_STATUS_CAPTURE)) {
                                    NTransactionResult result = new NTransactionResult();
                                    result.setData(transactionResult.getResponse());
                                    intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
                                    intent.putExtra(NYHelper.ID_ORDER, transactionResult.getResponse().getOrderId());
                                    intent.putExtra(NYHelper.TRANSACTION_RESPONSE, result.toString());
                                } else if (transactionResult.getResponse().getTransactionStatus().equals(NYHelper.TRANSACTION_PENDING)){
                                    NTransactionResult result = new NTransactionResult();
                                    result.setData(transactionResult.getResponse());
                                    intent.putExtra(NYHelper.TRANSACTION_COMPLETED, false);
                                    intent.putExtra(NYHelper.ID_ORDER, transactionResult.getResponse().getOrderId());
                                    intent.putExtra(NYHelper.TRANSACTION_RESPONSE, result.toString());
                                }
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }, "Check Order");
        } else if (transactionResult != null && transactionResult.getResponse() != null && transactionResult.getResponse().getFraudStatus() != null && transactionResult.getResponse().getFraudStatus().equals(NYHelper.NY_CHALLENGE_FRAUD_STATUS)){

            // TODO: jika transaksi gagal
            orderReturn = null;
            isTranssactionFailed = true;
            virtualAccountLinearLayout.setVisibility(View.VISIBLE);
            bankTransferLinearLayout.setVisibility(View.VISIBLE);

        } else {
            bankTransferLinearLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()){
                hideKeyboard(this);
                if (noteEditText.getText().toString().isEmpty()){
                    noteEditText.setVisibility(View.GONE);
                    addNoteLinearLayout.setVisibility(View.VISIBLE);
                }
            }

        }
        return super.dispatchTouchEvent(ev);
    }*/

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }





    public void requestChangePaymentMethod(){
        progressDialog.show();
        try {
            NYChangePaymentMethodRequest req = new NYChangePaymentMethodRequest(BookingServiceSummaryActivity.this, veritransToken, paymentType);
            spcMgr.execute(req, onChangePaymentMethodRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<CartReturn> onChangePaymentMethodRequest() {
        return new RequestListener<CartReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onRequestSuccess(final CartReturn result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                // TODO: refresh order detail
                // 1 = bank 2 = cc 3 = virtual 4 = paypal
                cartReturn = result;
                if (cartReturn != null && cartReturn.getCart() != null){
                    Cart cart = cartReturn.getCart();
                    if (cart != null){
                        subTotalPriceTextView.setText(NYHelper.priceFormatter(cart.getSubTotal()));
                        totalPriceTextView.setText(NYHelper.priceFormatter(cart.getTotal()));
                    }

                    if (cartReturn != null && cartReturn.getAdditionals() != null && cartReturn.getAdditionals().size() > 0)addAditonalView(cartReturn.getAdditionals());
                }

            }
        };
    }


    public void addAditonalView(List<Additional> additionalList) {
        serviceFeeLinearLayout.removeAllViews();
        for (Additional additional : additionalList) {

            LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View additionalView = inflaterAddons.inflate(R.layout.view_item_additional, null);

            TextView additionalLabelTextView = (TextView) additionalView.findViewById(R.id.additional_label_textView);
            TextView additionalValueTextView = (TextView) additionalView.findViewById(R.id.additional_value_textView);

            if (additional != null) {
                if (NYHelper.isStringNotEmpty(additional.getTitle())) additionalLabelTextView.setText(additional.getTitle());
                additionalValueTextView.setText(NYHelper.priceFormatter(additional.getValue()));
            }

            serviceFeeLinearLayout.addView(additionalView);
        }
    }


}
