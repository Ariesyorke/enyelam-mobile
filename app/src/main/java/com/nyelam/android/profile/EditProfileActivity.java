package com.nyelam.android.profile;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.User;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCountryCode;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.general.CountryCodeAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYLoginRequest;
import com.nyelam.android.http.NYUpdateUserProfileRequest;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private EditText firstNameEditText, lastNameEditText, usernameEditText, emailEditText, phoneNumberEditText;
    private TextView updateTextView, countryCodeTextView;
    private Spinner countryCodeSpinner;
    private String countryCodeId = "360";
    private CountryCodeAdapter countryCodeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        initControl();
        initToolbar();
        initProfile();
    }

    private void initProfile() {
        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin()){
            User user = storage.user;
            if (user!= null){

                String fullname = user.getFullname();

                if (NYHelper.isStringNotEmpty(fullname)){
                    String lastName = "";
                    String firstName= "";
                    if(fullname.split("\\w+").length>1){
                        lastName = fullname.substring(fullname.lastIndexOf(" ")+1);
                        firstName = fullname.substring(0, fullname.lastIndexOf(' '));
                    } else{
                        firstName = fullname;
                    }

                    if (NYHelper.isStringNotEmpty(firstName))firstNameEditText.setText(firstName);
                    if (NYHelper.isStringNotEmpty(lastName))lastNameEditText.setText(lastName);
                }

                if (NYHelper.isStringNotEmpty(user.getPhone()))phoneNumberEditText.setText(user.getPhone());
                if (NYHelper.isStringNotEmpty(user.getEmail()))emailEditText.setText(user.getEmail());

                if (NYHelper.isStringNotEmpty(user.getUsername()))usernameEditText.setText(user.getUsername());

                if (user.getCountryCode() != null && NYHelper.isStringNotEmpty(user.getCountryCode().getId()) && NYHelper.isStringNotEmpty(user.getCountryCode().getCountryNumber())){
                    countryCodeId = user.getCountryCode().getId();
                    countryCodeTextView.setText("+ "+user.getCountryCode().getCountryNumber());
                }
            }


            countryCodeAdapter = new CountryCodeAdapter(this);


            DaoSession session = ((NYApplication) getApplicationContext()).getDaoSession();
            List<NYCountryCode> rawProducts = session.getNYCountryCodeDao().queryBuilder().list();
            List<CountryCode> countryCodes = NYHelper.generateList(rawProducts, CountryCode.class);
            if (countryCodes != null && countryCodes.size() > 0){
                NYLog.e("tes isi DAO Country Code : "+countryCodes.toString());
                countryCodeAdapter.addCountryCodes(countryCodes);
            }

            countryCodeSpinner.setAdapter(countryCodeAdapter);

            NYLog.e("CEK INI APA ? = "+storage.user.getCountryCode().getId());

            if (countryCodes != null && countryCodes.size() > 0){
                NYLog.e("CEK INI APA ? 2");
                int pos = 0;
                for (CountryCode countryCode : countryCodes){
                    if (countryCode != null && NYHelper.isStringNotEmpty(countryCode.getId()) &&
                            storage != null && storage.user != null && storage.user.getCountryCode() != null && NYHelper.isStringNotEmpty(storage.user.getCountryCode().getId()) &&
                            countryCode.getId().equals(storage.user.getCountryCode().getId())){
                        countryCodeSpinner.setSelection(pos);
                        countryCodeAdapter.setSelectedPosition(pos);
                        countryCodeAdapter.notifyDataSetChanged();

                        NYLog.e("CEK INI APA ? pos = "+pos);

                        break;
                    }
                    pos++;
                }
            }

        }
    }

    private void initControl() {

        emailEditText.setKeyListener(null);

        updateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String username = null;
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                if (NYHelper.isStringNotEmpty(phoneNumber) && phoneNumber.charAt(0) == '0'){
                    phoneNumber = phoneNumber.substring(1);
                }

                if (!NYHelper.isStringNotEmpty(firstName)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_first_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isStringNotEmpty(email)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isValidEmaillId(email)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_email_not_valid), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isStringNotEmpty(phoneNumber)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_phone_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(firstName+" "+lastName, username, countryCodeId, phoneNumber);
                }
            }
        });
    }


    private void updateProfile(String fullname, String username, String countryCodeId, String phoneNumber){

        try {
            progressDialog.show();
            NYUpdateUserProfileRequest req = new NYUpdateUserProfileRequest(this, fullname, username, countryCodeId, phoneNumber);
            spcMgr.execute(req, onUpdateProfileRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private RequestListener<AuthReturn> onUpdateProfileRequest() {
        return new RequestListener<AuthReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                NYHelper.handleAPIException(EditProfileActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(AuthReturn authReturn) {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                NYHelper.handlePopupMessage(EditProfileActivity.this, getString(R.string.message_update_profile_success), null);

                NYHelper.saveUserData(EditProfileActivity.this, authReturn);

            }
        };
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //make title center
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);

        firstNameEditText = (EditText) findViewById(R.id.first_name_editText);
        lastNameEditText = (EditText) findViewById(R.id.last_name_editText);
        usernameEditText = (EditText) findViewById(R.id.username_editText);
        emailEditText = (EditText) findViewById(R.id.email_editText);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number_editText);
        updateTextView = (TextView) findViewById(R.id.update_textView);
        countryCodeSpinner = (Spinner) findViewById(R.id.country_code_spinner);
        countryCodeTextView = (TextView) findViewById(R.id.country_code_textView);
        countryCodeSpinner.setOnItemSelectedListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
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
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted())spcMgr.shouldStop();;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        countryCodeAdapter.setSelectedPosition(position);
        countryCodeAdapter.notifyDataSetChanged();

        CountryCode countryCode = (CountryCode) countryCodeSpinner.getSelectedItem();
        if (countryCode != null && NYHelper.isStringNotEmpty(countryCode.getId()) && NYHelper.isStringNotEmpty(countryCode.getCountryCode())){
            String countryCodeId = countryCode.getId();
            countryCodeTextView.setText("+ "+countryCode.getCountryNumber());
            this.countryCodeId = countryCodeId;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
