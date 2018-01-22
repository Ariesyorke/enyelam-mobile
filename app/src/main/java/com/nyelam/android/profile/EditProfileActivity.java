package com.nyelam.android.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.User;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.LoginStorage;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
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
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstNameEditText = (EditText) findViewById(R.id.first_name_editText);
        lastNameEditText = (EditText) findViewById(R.id.last_name_editText);
        usernameEditText = (EditText) findViewById(R.id.username_editText);
        emailEditText = (EditText) findViewById(R.id.email_editText);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number_editText);
        updateTextView = (TextView) findViewById(R.id.update_textView);
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


}
