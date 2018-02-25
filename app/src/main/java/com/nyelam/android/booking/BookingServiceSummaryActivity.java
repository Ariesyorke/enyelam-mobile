package com.nyelam.android.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.BookingContact;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Participant;
import com.nyelam.android.data.Summary;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.http.NYDoDiveServiceOrderRequest;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
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
    private String payment = NYHelper.BANK_TRANSFER;
    private List<Participant> participantList = new ArrayList<>();
    private BookingContact bookingContact;
    //private String cartToken;
    private CartReturn cartReturn;

    private LinearLayout particpantContainerLinearLayout, orderLinearLayout;
    private TextView serviceNameTextView, scheduleTextView, locationTextView;
    private TextView contactNameTextView, contactPhoneNumberTextView, contactEmailTextView;
    private TextView detailPriceTextView, subTotalPriceTextView, totalPriceTextView, ratingTextView, visitedTextView;
    private RatingBar ratingBar;

    private RadioGroup radioGroup;
    private RadioButton bankTransferRadioButton, midtransRadioButton;


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
        // SDK initiation for UIflow
        initMidtransSdk();
    }


    private void initControl() {
        orderLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (NYHelper.isStringNotEmpty(cartToken)){
                    Toast.makeText(BookingServiceSummaryActivity.this, cartToken, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookingServiceSummaryActivity.this, "cartToken null", Toast.LENGTH_SHORT).show();
                }*/
                new NYCustomDialog().showAgreementDialog(BookingServiceSummaryActivity.this);
            }
        });

        midtransRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (midtransRadioButton.isChecked()){
                    bankTransferRadioButton.setChecked(false);
                    payment = NYHelper.MIDTRANS;
                }
            }
        });

        bankTransferRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankTransferRadioButton.isChecked()){
                    midtransRadioButton.setChecked(false);
                    payment = NYHelper.BANK_TRANSFER;
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

                    if (diveService.getSpecialPrice() < diveService.getNormalPrice() && diveService.getSpecialPrice() > 0){
                        detailPriceTextView.setText(diver+" x @"+NYHelper.priceFormatter(diveService.getSpecialPrice()));
                    } else {
                        detailPriceTextView.setText(diver+" x @"+NYHelper.priceFormatter(diveService.getNormalPrice()));
                    }

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

                    if (NYHelper.isStringNotEmpty(bookingContact.getName()))contactNameTextView.setText(bookingContact.getName());
                    if (NYHelper.isStringNotEmpty(bookingContact.getPhoneNumber()))contactPhoneNumberTextView.setText(bookingContact.getPhoneNumber());
                    if (NYHelper.isStringNotEmpty(bookingContact.getEmail()))contactEmailTextView.setText(bookingContact.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                LoginStorage storage = new LoginStorage(this);
                if (storage.isUserLogin() && storage.user != null){
                    bookingContact = new BookingContact();
                    bookingContact.setName(storage.user.getFullname());
                    bookingContact.setPhoneNumber(storage.user.getPhone());
                    bookingContact.setEmail(storage.user.getEmail());
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
        detailPriceTextView = (TextView) findViewById(R.id.detail_price_textView);
        subTotalPriceTextView = (TextView) findViewById(R.id.sub_total_price_textView);
        totalPriceTextView = (TextView) findViewById(R.id.total_price_textView);

        particpantContainerLinearLayout = (LinearLayout) findViewById(R.id.participant_container_linearLayout);
        contactNameTextView = (TextView) findViewById(R.id.contact_name_textView);
        contactPhoneNumberTextView = (TextView) findViewById(R.id.contact_phone_number_textView);
        contactEmailTextView = (TextView) findViewById(R.id.contact_email_textView);
        orderLinearLayout = (LinearLayout) findViewById(R.id.order_linearLayout);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingTextView = (TextView) findViewById(R.id.rating_textView);
        visitedTextView = (TextView) findViewById(R.id.visited_textView);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        bankTransferRadioButton = (RadioButton) findViewById(R.id.bankTransferRadioButton);
        midtransRadioButton = (RadioButton) findViewById(R.id.midtransRadioButton);

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
            TextView changeTextView = (TextView) myParticipantsView.findViewById(R.id.change_textView);
            LinearLayout fillLinearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.fill_linearLayout);

            if (participant != null) {
                if (NYHelper.isStringNotEmpty(participant.getName())){
                    nameTextView.setText(participant.getName());
                }  else{
                    nameTextView.setText("Participant "+String.valueOf(position+1));
                }

                if (NYHelper.isStringNotEmpty(participant.getEmail())){
                    emailTextView.setText(participant.getEmail());
                } else {
                    emailTextView.setText("-");
                }

                changeTextView.setVisibility(View.GONE);
                fillLinearLayout.setVisibility(View.GONE);
            } else {
                nameTextView.setText("Participant "+String.valueOf(position+1));
                emailTextView.setText("-");
                changeTextView.setVisibility(View.GONE);
                fillLinearLayout.setVisibility(View.GONE);
            }

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

    private RequestListener<Summary> onCreateOrderServiceRequest() {
        return new RequestListener<Summary>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                NYHelper.handleAPIException(BookingServiceSummaryActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(final Summary result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                NYHelper.handlePopupMessage(BookingServiceSummaryActivity.this, "Your order was successful", false,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (payment.equals(NYHelper.MIDTRANS)){



                                    /*NYGetVeritransToken req = new NYGetVeritransToken(OrderActivity.this, cartReturn.cartToken, voucherCode, contact, ticketDrafts, lastBestLoc);
                                    spcMgr.execute(req, onGetVeritransRequest());*/



                                    /*TransactionRequest transactionRequest = new TransactionRequest(result.getOrder().getOrderId(), result.getOrder().getCart().getTotal());
                                    //ADD ORDERED ITEMS
                                    ArrayList<ItemDetails> itemDetails = new ArrayList<>();
                                    *//*for (int i = 0; i < result.getOrder().getCart().get.getList().size(); i++) {
                                        ItemDetails item = new ItemDetails(tickets.getList().get(i).getId(), (int) (tickets.getList().get(i).getSpecialPrice() > 0 ? tickets.getList().get(i).getSpecialPrice() : tickets.getList().get(i).getPrice()), tickets.getList().get(i).getOrderedTicket(), tickets.getList().get(i).getTicketType());
                                        itemDetails.add(item);
                                    }*//*


                                    for (int i = 0; i < result.getParticipants().size(); i++) {
                                        ItemDetails item = new ItemDetails(result.getDiveService().getId(), (int) (result.getDiveService().getSpecialPrice() > 0 ? result.getDiveService().getSpecialPrice() : result.getDiveService().getNormalPrice()), 1, result.getDiveService().getName());
                                        itemDetails.add(item);
                                    }
                                    transactionRequest.setItemDetails(itemDetails);


                                    Contact contact = result.getContact();
                                    CustomerDetails customerDetails = new CustomerDetails( contact.getName(), null, contact.getEmailAddress(), contact.getPhoneNumber());

                                    transactionRequest.setCustomerDetails(customerDetails);a
                                    MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
                                    MidtransSDK.getInstance().startPaymentUiFlow(BookingServiceSummaryActivity.this);*/


                                    MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                                    NYLog.e("OPO IKI TRANS DONE");
                                    MidtransSDK.getInstance().startPaymentUiFlow(BookingServiceSummaryActivity.this, PaymentMethod.CREDIT_CARD);
                                    NYLog.e("OPO IKI PAYMENT");

                                } else {
                                    Intent intent = new Intent(BookingServiceSummaryActivity.this, HomeActivity.class);
                                    if (result != null && NYHelper.isStringNotEmpty(result.toString()))intent.putExtra(NYHelper.SUMMARY, result.toString());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                            }
                        });
            }
        };
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        spcMgr.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }


    @Override
    public void onChooseListener(Object position) {

    }

    @Override
    public void onAcceptAgreementListener() {

        progressDialog.show();
        NYDoDiveServiceOrderRequest req = null;
        try {
            req = new NYDoDiveServiceOrderRequest(BookingServiceSummaryActivity.this, cartReturn.getCartToken(), bookingContact.toString(), participantList.toString());
            spcMgr.execute(req, onCreateOrderServiceRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private TransactionRequest initTransactionRequest() {
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", 6000);

        //set customer details
        transactionRequestNew.setCustomerDetails(initCustomerDetails());


        // set item details
        ItemDetails itemDetails = new ItemDetails("1", 1000, 1, "Trekking Shoes");
        ItemDetails itemDetails1 = new ItemDetails("2", 1000, 2, "Casual Shoes");
        ItemDetails itemDetails2 = new ItemDetails("3", 1000, 3, "Formal Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(itemDetails1);
        itemDetailsArrayList.add(itemDetails2);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);


        // Create creditcard options for payment
        /*CreditCard creditCard = new CreditCard();

        creditCard.setSaveCard(false); // when using one/two click set to true and if normal set to  false

//        this methode deprecated use setAuthentication instead
//        creditCard.setSecure(true); // when using one click must be true, for normal and two click (optional)

        creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS);

        // noted !! : channel migs is needed if bank type is BCA, BRI or MyBank
//        creditCard.setChannel(CreditCard.MIGS); //set channel migs
        creditCard.setBank(BankType.BCA); //set spesific acquiring bank

        transactionRequestNew.setCreditCard(creditCard);*/

        NYLog.e("OPO IKI INIT TRANSACTION");

        return transactionRequestNew;
    }

    private CustomerDetails initCustomerDetails() {

        NYLog.e("OPO IKI INIT CUSTOMER");

        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("085310102020");
        mCustomerDetails.setFirstName("user fullname");
        mCustomerDetails.setEmail("mail@mail.com");
        return mCustomerDetails;
    }

    private void initMidtransSdk() {
        NYLog.e("OPO IKI INIT");
        SdkUIFlowBuilder.init()
                .setClientKey(getResources().getString(R.string.client_key_development)) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(getResources().getString(R.string.api_veritrans_development)) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .buildSDK();

        /*SdkCoreFlowBuilder.init()
                .setContext(this)
                .enableLog(true)
                .setClientKey(getResources().getString(R.string.client_key_development))
                .setMerchantBaseUrl(getResources().getString(R.string.api_veritrans_development))
                .buildSDK();*/
    }

    @Override
    public void onTransactionFinished(TransactionResult transactionResult) {
        NYLog.e("OPO IKI ");
        Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
