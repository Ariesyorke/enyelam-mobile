package com.nyelam.android.profile;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.User;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYLoginRequest;
import com.nyelam.android.http.NYUpdateUserProfileRequest;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class EditProfileActivity extends AppCompatActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private EditText firstNameEditText, lastNameEditText, usernameEditText, emailEditText, phoneNumberEditText;
    private TextView updateTextView;

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

                String name = user.getFullname();
                String lastName = "";
                String firstName= "";
                if(name.split("\\w+").length>1){
                    lastName = name.substring(name.lastIndexOf(" ")+1);
                    firstName = name.substring(0, name.lastIndexOf(' '));
                } else{
                    firstName = name;
                }

               if (NYHelper.isStringNotEmpty(firstName))firstNameEditText.setText(firstName);
               if (NYHelper.isStringNotEmpty(lastName))lastNameEditText.setText(lastName);
               if (NYHelper.isStringNotEmpty(user.getPhone()))phoneNumberEditText.setText(user.getPhone());
               if (NYHelper.isStringNotEmpty(user.getEmail()))emailEditText.setText(user.getEmail());
            }
        }
    }

    private void initControl() {
        updateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();


                if (!NYHelper.isStringNotEmpty(firstName)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_first_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isStringNotEmpty(lastName)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isStringNotEmpty(username)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_username_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isStringNotEmpty(email)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isValidEmaillId(email)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_email_not_valid), Toast.LENGTH_SHORT).show();
                } else  if (!NYHelper.isStringNotEmpty(phoneNumber)){
                    Toast.makeText(EditProfileActivity.this, getString(R.string.warn_field_phone_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(firstName+" "+lastName, username, "62", phoneNumber);
                }
            }
        });
    }


    private void updateProfile(String fullname, String username, String countryCode, String phoneNumber){

        try {
            progressDialog.show();
            NYUpdateUserProfileRequest req = new NYUpdateUserProfileRequest(this, fullname, username, countryCode, phoneNumber);
            spcMgr.execute(req, onUpdateProfileRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private RequestListener<User> onUpdateProfileRequest() {
        return new RequestListener<User>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                NYHelper.handleAPIException(EditProfileActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(User user) {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }
        };
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstNameEditText = (EditText) findViewById(R.id.first_name_editText);
        lastNameEditText = (EditText) findViewById(R.id.last_name_editText);
        usernameEditText = (EditText) findViewById(R.id.username_editText);
        emailEditText = (EditText) findViewById(R.id.email_editText);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number_editText);
        updateTextView = (TextView) findViewById(R.id.update_textView);

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
}
