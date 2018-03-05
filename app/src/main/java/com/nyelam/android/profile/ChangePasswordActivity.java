package com.nyelam.android.profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYChangePasswordRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import eu.davidea.flexibleadapter.items.IFilterable;

public class ChangePasswordActivity extends BasicActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private EditText currentPasswordEditText, newPasswordEditText, confirmNewPasswordEditText;
    private TextView changePasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        initControl();
    }

    private void initControl() {
        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

                if (!NYHelper.isStringNotEmpty(currentPassword)){
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.warn_field_current_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(newPassword)){
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.warn_field_new_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isStringNotEmpty(confirmNewPassword)){
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.warn_field_confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmNewPassword)){
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.warn_field_confirm_new_password_didnt_macth), Toast.LENGTH_SHORT).show();
                } else {
                    changePassword(currentPassword, newPassword, confirmNewPassword);
                }
            }
        });
    }


    private void changePassword(String currentPassword, String newPassword, String confirmNewPassword){

        try {
            progressDialog.show();
            NYChangePasswordRequest req = new NYChangePasswordRequest(this, currentPassword, newPassword, confirmNewPassword);
            spcMgr.execute(req, onChangePasswordRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private RequestListener<JSONObject> onChangePasswordRequest() {
        return new RequestListener<JSONObject>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                NYHelper.handleAPIException(ChangePasswordActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(JSONObject objSuccess) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                String KEY_CHANGED = "changed";
                String KEY_REASON = "reason";
                boolean isSuccess = false;

                if (objSuccess.has(KEY_CHANGED)) try {
                    isSuccess = objSuccess.getBoolean(KEY_CHANGED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isSuccess && objSuccess.has(KEY_REASON)) {
                    try {
                        NYHelper.handlePopupMessage(ChangePasswordActivity.this, objSuccess.getString(KEY_REASON), false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (isSuccess){
                    NYHelper.handlePopupMessage(ChangePasswordActivity.this, getString(R.string.message_update_password_success), false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    });
                } else if (!isSuccess && objSuccess.has(KEY_REASON)) {
                    try {
                        NYHelper.handlePopupMessage(ChangePasswordActivity.this,objSuccess.getString(KEY_REASON), null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        NYHelper.handlePopupMessage(ChangePasswordActivity.this, objSuccess.getString(KEY_REASON), null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
    }


    private void initView() {
        currentPasswordEditText = (EditText) findViewById(R.id.current_password_editText);
        newPasswordEditText = (EditText) findViewById(R.id.new_password_editText);
        confirmNewPasswordEditText = (EditText) findViewById(R.id.confirm_new_password_editText);
        changePasswordTextView = (TextView) findViewById(R.id.change_password_textView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

}
