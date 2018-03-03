package com.nyelam.android.booking;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.BookingContact;
import com.nyelam.android.general.CountryCodeAdapter;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class BookingServiceContactActivity extends AppCompatActivity {

    private BookingContact bookingContact;
    private Bundle extras;
    private TextView saveTextView;
    private TextInputEditText nameEditText, phoneEditText, emailEditText;

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
        phoneEditText = (TextInputEditText) findViewById(R.id.phone_number_contact_editText);
        emailEditText = (TextInputEditText) findViewById(R.id.email_contact_editText);
        saveTextView = (TextView) findViewById(R.id.save_contact_textView);
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
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
