package com.nyelam.android.booking;

import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.BookingContact;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCountryCode;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.general.CountryCodeAdapter;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BookingServiceContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private BookingContact bookingContact;
    private Bundle extras;
    private TextView saveTextView, plusTextView;
    private TextInputEditText nameEditText, emailEditText;
    private EditText phoneEditText;
    private Spinner countryCodeSpinner;
    private EditText phoneNumberEditText;
    private RelativeLayout spinnerRelativeLayout;
    private CountryCodeAdapter countryCodeAdapter;

    private CountryCode countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service_contact);
        initView();
        getExtra();
        initControl();
    }

    private void initControl() {
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();

                if (!NYHelper.isStringNotEmpty(name)){
                    Toast.makeText(BookingServiceContactActivity.this, getString(R.string.warn_field_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(phone)){
                    Toast.makeText(BookingServiceContactActivity.this, getString(R.string.warn_field_phone_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(email)){
                    Toast.makeText(BookingServiceContactActivity.this, getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isValidEmaillId(email)){
                    Toast.makeText(BookingServiceContactActivity.this, getString(R.string.warn_email_not_valid), Toast.LENGTH_SHORT).show();
                } else {
                    name = NYHelper.capitalizeString(name);
                    bookingContact.setName(name);
                    bookingContact.setPhoneNumber(phone);
                    bookingContact.setEmail(email);
                    bookingContact.setCountryCode(countryCode);

                    extras.putString(NYHelper.CONTACT, bookingContact.toString());
                    extras.putBoolean(NYHelper.IS_NOT_NEW, true);

                    Intent intent = new Intent(BookingServiceContactActivity.this, BookingServiceActivity.class);
                    intent.putExtras(extras);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }


    private void initView() {
        nameEditText = (TextInputEditText) findViewById(R.id.name_contact_editText);
        phoneEditText = (EditText) findViewById(R.id.phone_number_editText);
        emailEditText = (TextInputEditText) findViewById(R.id.email_contact_editText);
        emailEditText.setKeyListener(null);
        saveTextView = (TextView) findViewById(R.id.save_contact_textView);
        plusTextView = (TextView) findViewById(R.id.plus_textView);
        countryCodeSpinner = (Spinner) findViewById(R.id.country_code_spinner);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number_editText);
        spinnerRelativeLayout = (RelativeLayout) findViewById(R.id.spinner_relativeLayout);

        countryCodeSpinner = (Spinner) findViewById(R.id.country_code_spinner);
        countryCodeSpinner.setOnItemSelectedListener(this);
    }

    private void getExtra() {
        extras = getIntent().getExtras();
        if (extras != null) {

            try {
                JSONObject obj = new JSONObject(extras.getString(NYHelper.CONTACT));
                bookingContact = new BookingContact();
                bookingContact.parse(obj);

                if (bookingContact != null){
                    if (NYHelper.isStringNotEmpty(bookingContact.getName())) nameEditText.setText(bookingContact.getName());
                    if (NYHelper.isStringNotEmpty(bookingContact.getPhoneNumber())) phoneEditText.setText(bookingContact.getPhoneNumber());
                    if (NYHelper.isStringNotEmpty(bookingContact.getEmail())) emailEditText.setText(bookingContact.getEmail());

                    if (bookingContact.getCountryCode() != null && NYHelper.isStringNotEmpty(bookingContact.getCountryCode().getId()) && NYHelper.isStringNotEmpty(bookingContact.getCountryCode().getCountryNumber())){
                        //countryCodeId = bookingContact.getCountryCode().getId();
                        if (countryCode == null)countryCode = new CountryCode();
                        countryCode = bookingContact.getCountryCode();
                        plusTextView.setText("+ "+bookingContact.getCountryCode().getCountryNumber());
                    }
                }


                // TODO: initialize adapter
                countryCodeAdapter = new CountryCodeAdapter(this);

                DaoSession session = ((NYApplication) getApplicationContext()).getDaoSession();
                List<NYCountryCode> rawProducts = session.getNYCountryCodeDao().queryBuilder().list();
                List<CountryCode> countryCodes = NYHelper.generateList(rawProducts, CountryCode.class);
                if (countryCodes != null && countryCodes.size() > 0){
                    NYLog.e("tes isi DAO Country Code : "+countryCodes.toString());
                    countryCodeAdapter.addCountryCodes(countryCodes);
                }

                countryCodeSpinner.setAdapter(countryCodeAdapter);

                if (countryCodes != null && countryCodes.size() > 0){
                    int pos = 0;
                    for (CountryCode countryCode : countryCodes){
                        if (countryCode != null && NYHelper.isStringNotEmpty(countryCode.getId()) &&
                                bookingContact.getCountryCode() != null && NYHelper.isStringNotEmpty(bookingContact.getCountryCode().getId()) &&
                                countryCode.getId().equals(bookingContact.getCountryCode().getId())){
                            countryCodeSpinner.setSelection(pos);
                            countryCodeAdapter.setSelectedPosition(pos);
                            countryCodeAdapter.notifyDataSetChanged();
                            break;
                        }
                        pos++;
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        countryCodeAdapter.setSelectedPosition(position);
        countryCodeAdapter.notifyDataSetChanged();

        CountryCode countryCode = (CountryCode) countryCodeSpinner.getSelectedItem();
        if (countryCode != null && NYHelper.isStringNotEmpty(countryCode.getId()) && NYHelper.isStringNotEmpty(countryCode.getCountryCode())){
            String countryCodeId = countryCode.getId();
            plusTextView.setText("+ "+countryCode.getCountryNumber());
            //this.countryCodeId = countryCodeId;
            this.countryCode = countryCode;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
