package com.nyelam.android.booking;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.Contact;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class BookingServiceContactActivity extends AppCompatActivity {

    private Contact contact;
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
                    nameEditText.setError(getString(R.string.warn_field_name_cannot_be_empty));
                } else if (!NYHelper.isStringNotEmpty(phone)){
                    phoneEditText.setError(getString(R.string.warn_field_phone_cannot_be_empty));
                } else if (!NYHelper.isStringNotEmpty(email)){
                    emailEditText.setError(getString(R.string.warn_field_email_cannot_be_empty));
                } else if (!NYHelper.isValidEmaillId(email)){
                    emailEditText.setError(getString(R.string.warn_email_not_valid));
                } else {
                    contact.setName(name);
                    contact.setPhoneNumber(phone);
                    contact.setEmail(email);

                    extras.putString(NYHelper.CONTACT, contact.toString());
                    extras.putBoolean(NYHelper.IS_NOT_NEW, true);

                    Intent intent = new Intent(BookingServiceContactActivity.this, BookingServiceActivity.class);
                    intent.putExtras(extras);
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
                contact = new Contact();
                contact.parse(obj);

                if (contact != null){
                    if (NYHelper.isStringNotEmpty(contact.getName())) nameEditText.setText(contact.getName());
                    if (NYHelper.isStringNotEmpty(contact.getPhoneNumber())) phoneEditText.setText(contact.getPhoneNumber());
                    if (NYHelper.isStringNotEmpty(contact.getEmail())) emailEditText.setText(contact.getEmail());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
