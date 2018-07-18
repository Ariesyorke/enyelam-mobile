package com.nyelam.android.profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.auth.RegisterFragment;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Country;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.CountryList;
import com.nyelam.android.data.Language;
import com.nyelam.android.data.LanguageList;
import com.nyelam.android.data.LicenseType;
import com.nyelam.android.data.LicenseTypeList;
import com.nyelam.android.data.Nationality;
import com.nyelam.android.data.NationalityList;
import com.nyelam.android.data.Organization;
import com.nyelam.android.data.OrganizationList;
import com.nyelam.android.data.User;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCountryCode;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.docourse.DoCourseActivity;
import com.nyelam.android.general.CountryCodeAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYLoginRequest;
import com.nyelam.android.http.NYMasterCountryRequest;
import com.nyelam.android.http.NYMasterLanguageRequest;
import com.nyelam.android.http.NYMasterLicenseTypeRequest;
import com.nyelam.android.http.NYMasterNationalityRequest;
import com.nyelam.android.http.NYMasterOrganizationRequest;
import com.nyelam.android.http.NYUpdateUserProfileRequest;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.NYCountryDialogFragment;
import com.nyelam.android.view.NYCustomDialog;
import com.nyelam.android.view.NYSpinner;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NYCustomDialog.OnDialogFragmentClickListener {
    private static final String DISPLAY_DATE_FORMAT = "dd MMM yyyy";

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private EditText firstNameEditText, lastNameEditText, usernameEditText, emailEditText,
            phoneNumberEditText, birthPlaceEditText, birthDateEditText,
            certificateNumberEditText, certificateDateEditText, genderEditText, organizationEditText, licenseTypeEditText;
    private TextView updateTextView, countryCodeTextView;
    private View birthDateButton, certificateDateButton;
    private NYSpinner countryCodeSpinner, genderSpinner;
    private NYGenderSpinnerAdapter genderSpinnerAdapter;
    private String countryCodeId = "360";
    private CountryCodeAdapter countryCodeAdapter;
    private Date certificateDate, dateBirth;

    private TextInputLayout countryInputLayout, nationalityInputLayout, languageInputLayout, organizationInputLayout, licenseTypeInputLayout;
    private EditText countryEditText, nationalityEditText, languageEditText;
    private CountryCode country;
    //private Nationality nationality;
    private ProgressBar countryProgressBar, nationalityProgressBar, languageProgressBar, organizationProgressBar, licenseTypeProgressBar;
    //private CountryList countryList;
    private Country currentCountry;
    private NationalityList nationalityList;
    private Nationality currentNationality;
    private LanguageList languageList;
    private Language currentLanguage;
    private OrganizationList organizationList;
    private Organization organization;
    private LicenseTypeList licenseTypeList;
    private LicenseType licenseType;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        initProfile();
        initControl();
        initToolbar();
    }

    private void initProfile() {

        dateBirth = new Date();
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
                    } else {
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

                if (user.getCountry() != null && NYHelper.isStringNotEmpty(user.getCountry().getName()) ){
                    currentCountry = user.getCountry();
                    countryEditText.setText(currentCountry.getName());
                }

                if (user.getNationality() != null && NYHelper.isStringNotEmpty(user.getNationality().getName()) ){
                    currentNationality = user.getNationality();
                    nationalityEditText.setText(currentNationality.getName());
                }

                if (user.getLanguage() != null && NYHelper.isStringNotEmpty(user.getLanguage().getName()) ){
                    currentLanguage = user.getLanguage();
                    languageEditText.setText(currentLanguage.getName());
                    //Toast.makeText(this, currentLanguage.getName(), Toast.LENGTH_SHORT).show();
                }

                if (user.getOrganization() != null){
                       organization = user.getOrganization();
                }

                if (user.getLicenseType() != null){
                    licenseType = user.getLicenseType();
                }

            }

            countryCodeAdapter = new CountryCodeAdapter(this);

            genderSpinnerAdapter = new NYGenderSpinnerAdapter(this);

            DaoSession session = ((NYApplication) getApplicationContext()).getDaoSession();
            List<NYCountryCode> rawProducts = session.getNYCountryCodeDao().queryBuilder().list();
            List<CountryCode> countryCodes = NYHelper.generateList(rawProducts, CountryCode.class);
            if (countryCodes != null && countryCodes.size() > 0){
                countryCodeAdapter.addCountryCodes(countryCodes);
            }

            countryCodeSpinner.setAdapter(countryCodeAdapter);
            genderSpinner.setAdapter(genderSpinnerAdapter);
            if(user != null) {
                if(!TextUtils.isEmpty(user.getBirthPlace())) {
                    birthPlaceEditText.setText(user.getBirthPlace());
                }
                if(!TextUtils.isEmpty(user.getCertificateNumber())) {
                    certificateNumberEditText.setText(user.getCertificateNumber());
                }
                dateBirth = user.getBirthdate();
                certificateDate = user.getCertificateDate();
                if(dateBirth != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    birthDateEditText.setText(sdf.format(dateBirth));
                }
                if(certificateDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    certificateDateEditText.setText(sdf.format(certificateDate));
                }
                if (!TextUtils.isEmpty(user.getGender())) {
                    String gender = user.getGender();
                    if (gender.equalsIgnoreCase("Male")) {
                        genderEditText.setText("Male");
                        genderSpinner.setSelection(0);
                    } else {
                        genderEditText.setText("Female");
                        genderSpinner.setSelection(1);
                    }
                } else {
                    genderEditText.setText("Male");
                    genderSpinner.setSelection(0);
                }
            }  else {
                genderEditText.setText("Male");
                genderSpinner.setSelection(0);
            }
            if (countryCodes != null && countryCodes.size() > 0){
                int pos = 0;
                for (CountryCode countryCode : countryCodes){
                    if (countryCode != null && NYHelper.isStringNotEmpty(countryCode.getId()) &&
                            storage != null && storage.user != null && storage.user.getCountryCode() != null && NYHelper.isStringNotEmpty(storage.user.getCountryCode().getId()) &&
                            countryCode.getId().equals(storage.user.getCountryCode().getId())){
                        countryCodeSpinner.setSelection(pos);
                        countryCodeAdapter.setSelectedPosition(pos);
                        countryCodeAdapter.notifyDataSetChanged();

                        break;
                    }
                    pos++;
                }
            }

        }
    }


    private void initControl() {

        organizationInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (organizationList != null && organizationList.getList().size() > 0){
                    NYCustomDialog dialog = new NYCustomDialog();
                    dialog.showAssocitaionDialog(EditProfileActivity.this, organizationList.getList(), organization);
                }
            }
        });

        organizationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (organizationList != null && organizationList.getList().size() > 0){
                    NYCustomDialog dialog = new NYCustomDialog();
                    dialog.showAssocitaionDialog(EditProfileActivity.this, organizationList.getList(), organization);
                }
            }
        });

        licenseTypeInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (licenseTypeList != null && licenseTypeList.getList().size() > 0){
                    NYCustomDialog dialog = new NYCustomDialog();
                    dialog.showLicenseTypeDialog(EditProfileActivity.this, licenseTypeList.getList(), licenseType);
                }
            }
        });

        licenseTypeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (licenseTypeList != null && licenseTypeList.getList().size() > 0){
                    NYCustomDialog dialog = new NYCustomDialog();
                    dialog.showLicenseTypeDialog(EditProfileActivity.this, licenseTypeList.getList(), licenseType);
                }
            }
        });

        countryInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NYCustomDialog().showCountryDialog(EditProfileActivity.this, currentCountry);
            }
        });

        countryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NYCustomDialog().showCountryDialog(EditProfileActivity.this, currentCountry);
            }
        });

        nationalityInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nationalityList != null && nationalityList.getList().size() > 0){
                    new NYCustomDialog().showNationalityDialog(EditProfileActivity.this, nationalityList.getList(), currentNationality);
                } else {

                }
            }
        });

        nationalityEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nationalityList != null && nationalityList.getList().size() > 0){
                    new NYCustomDialog().showNationalityDialog(EditProfileActivity.this, nationalityList.getList(), currentNationality);
                } else {

                }
            }
        });

        languageInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageList != null && languageList.getList().size() > 0){
                    new NYCustomDialog().showLanguageDialog(EditProfileActivity.this, languageList.getList(), currentLanguage);
                }
            }
        });

        languageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageList != null && languageList.getList().size() > 0){
                    new NYCustomDialog().showLanguageDialog(EditProfileActivity.this, languageList.getList(), currentLanguage);
                }
            }
        });

        birthDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)EditProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                showBirthdatePicker(dateBirth);
            }
        });

        certificateDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)EditProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                showCertificatePicker(new Date());
            }
        });

        countryCodeSpinner.setOnItemSelectedListener(this);
        genderSpinner.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened(Spinner spinner) {
                InputMethodManager imm = (InputMethodManager)EditProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
            }

            @Override
            public void onSpinnerClosed(Spinner spinner) {

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    genderEditText.setText("Male");
                } else {
                    genderEditText.setText("Female");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        countryCodeSpinner.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened(Spinner spinner) {
                InputMethodManager imm = (InputMethodManager)EditProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
            }

            @Override
            public void onSpinnerClosed(Spinner spinner) {

            }
        });


        updateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String username = null;
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String gender = String.valueOf(genderSpinner.getSelectedItemPosition()+1);
                String birthDate = dateBirth != null? String.valueOf(dateBirth.getTime()/1000) : null;
                String dateCertificate = certificateDate != null? String.valueOf(certificateDate.getTime()/1000): null;
                String certificateNumber = certificateNumberEditText.getText().toString();
                String birthPlace = birthPlaceEditText.getText().toString();
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
                    updateProfile(firstName+" "+lastName, username, countryCodeId, phoneNumber, gender, birthDate, dateCertificate, certificateNumber, birthPlace, currentCountry, currentNationality, currentLanguage);
                }
            }
        });


    }


    private void updateProfile(String fullname, String username, String countryCodeId, String phoneNumber, String gender, String birthDate, String dateCertificate, String certificateNumber, String birthPlace, Country currentCountry, Nationality currentNationality, Language currentLanguage){
        try {
            progressDialog.show();
            NYUpdateUserProfileRequest req = new NYUpdateUserProfileRequest(this, fullname, username, countryCodeId, phoneNumber, gender, birthDate, dateCertificate, certificateNumber, birthPlace, currentCountry, currentNationality, currentLanguage, organization, licenseType);
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

                NYHelper.handlePopupMessage(EditProfileActivity.this, getString(R.string.message_update_profile_success), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                });

                NYHelper.saveUserData(EditProfileActivity.this, authReturn);

            }
        };
    }



    /*private void getCountry(){
        try {
            //progressDialog.show();
            countryProgressBar.setVisibility(View.VISIBLE);
            countryEditText.setVisibility(View.GONE);
            NYMasterCountryRequest req = new NYMasterCountryRequest(this);
            spcMgr.execute(req, onGetCountry());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private RequestListener<CountryList> onGetCountry() {
        return new RequestListener<CountryList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                *//*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*//*

                NYHelper.handleAPIException(EditProfileActivity.this, spiceException, null);

                countryProgressBar.setVisibility(View.GONE);
                countryEditText.setVisibility(View.VISIBLE);
                currentCountry = null;
                countryEditText.setText("");

            }

            @Override
            public void onRequestSuccess(CountryList result) {
                *//*if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*//*

                countryProgressBar.setVisibility(View.GONE);
                countryEditText.setVisibility(View.VISIBLE);

                countryList = result;

                if (countryList != null && nationalityList.getList() != null && nationalityList.getList().size() > 0){
                    currentCountry = countryList.getList().get(0);
                    if (currentCountry != null && NYHelper.isStringNotEmpty(currentCountry.getName()))nationalityEditText.setText(currentCountry.getName());
                } else {
                    currentCountry = null;
                    countryEditText.setText("");
                }

            }
        };
    }*/



    private void getNationality(String countryId){
        try {
            //progressDialog.show();
            nationalityProgressBar.setVisibility(View.VISIBLE);
            nationalityEditText.setVisibility(View.GONE);
            NYMasterNationalityRequest req = new NYMasterNationalityRequest(this, countryId);
            spcMgr.execute(req, onGetNationality());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private RequestListener<NationalityList> onGetNationality() {
        return new RequestListener<NationalityList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/

                NYHelper.handleAPIException(EditProfileActivity.this, spiceException, null);

                nationalityProgressBar.setVisibility(View.GONE);
                nationalityEditText.setVisibility(View.VISIBLE);
                currentNationality = null;
                nationalityEditText.setText("");

            }

            @Override
            public void onRequestSuccess(NationalityList result) {
                /*if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                nationalityProgressBar.setVisibility(View.GONE);
                nationalityEditText.setVisibility(View.VISIBLE);

                nationalityList = result;

                if (nationalityList != null && nationalityList.getList() != null && nationalityList.getList().size() > 0){
                    currentNationality = nationalityList.getList().get(0);
                    if (currentNationality != null && NYHelper.isStringNotEmpty(currentNationality.getName()))nationalityEditText.setText(currentNationality.getName());
                } else {
                    currentNationality = null;
                    nationalityEditText.setText("");
                }

            }
        };
    }


    private void getLanguage(){
        try {
            //progressDialog.show();
            languageProgressBar.setVisibility(View.VISIBLE);
            languageEditText.setVisibility(View.GONE);
            NYMasterLanguageRequest req = new NYMasterLanguageRequest(this);
            spcMgr.execute(req, onGetLanguage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private RequestListener<LanguageList> onGetLanguage() {
        return new RequestListener<LanguageList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                //NYHelper.handleAPIException(EditProfileActivity.this, spiceException, null);

                languageProgressBar.setVisibility(View.GONE);
                languageEditText.setVisibility(View.VISIBLE);
                currentLanguage = null;
                languageEditText.setText("");

            }

            @Override
            public void onRequestSuccess(LanguageList result) {
                /*if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                languageProgressBar.setVisibility(View.GONE);
                languageEditText.setVisibility(View.VISIBLE);

                languageList = result;

                if (languageList != null && languageList.getList() != null && languageList.getList().size() > 0 && currentLanguage != null){
                    if (currentLanguage != null && NYHelper.isStringNotEmpty(currentLanguage.getName()))languageEditText.setText(currentLanguage.getName());
                } else if (languageList != null && languageList.getList() != null && languageList.getList().size() > 0){
                    currentLanguage = languageList.getList().get(0);
                    if (currentLanguage != null && NYHelper.isStringNotEmpty(currentLanguage.getName()))languageEditText.setText(currentLanguage.getName());
                } else {
                    currentLanguage = null;
                    languageEditText.setText("");
                }


                /*if (languageList != null && languageList.getList() != null && languageList.getList().size() > 0){
                    currentLanguage = languageList.getList().get(0);
                    if (currentLanguage != null && NYHelper.isStringNotEmpty(currentLanguage.getName()))languageEditText.setText(currentLanguage.getName());
                } else {
                    currentLanguage = null;
                    languageEditText.setText("");
                }*/

            }
        };
    }






    private void getOrganizationRequest() {
        setAssociationProgressBar(true);
        NYMasterOrganizationRequest req = new NYMasterOrganizationRequest(getApplicationContext());
        spcMgr.execute(req, onOrganizationRequest());
    }

    private RequestListener<OrganizationList> onOrganizationRequest() {
        return new RequestListener<OrganizationList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*diveServiceSuggestionAdapter.clear();
                diveServiceSuggestionAdapter.notifyDataSetChanged();
                suggestionLinearLayout.setVisibility(View.GONE);*/
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
                setAssociationProgressBar(false);
            }

            @Override
            public void onRequestSuccess(OrganizationList results) {
                organizationList = results;

                if (!isFirst || (isFirst && organization == null)){
                    organization = organizationList.getList().get(0);
                }

                setAssociationProgressBar(false);

                if (organization != null && NYHelper.isStringNotEmpty(organization.getName()))
                    organizationEditText.setText(organization.getName());

                getLicenseTypeRequest();
            }
        };
    }


    private void getLicenseTypeRequest() {
        setDivingLicenseProgressBar(true);
        NYMasterLicenseTypeRequest req = new NYMasterLicenseTypeRequest(getApplicationContext(), organization.getId());
        spcMgr.execute(req, onLicenseTypeRequest());
    }

    private RequestListener<LicenseTypeList> onLicenseTypeRequest() {
        return new RequestListener<LicenseTypeList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*diveServiceSuggestionAdapter.clear();
                diveServiceSuggestionAdapter.notifyDataSetChanged();
                suggestionLinearLayout.setVisibility(View.GONE);*/
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
                setDivingLicenseProgressBar(false);
            }

            @Override
            public void onRequestSuccess(LicenseTypeList results) {
                licenseTypeList = results;

                if (!isFirst || (isFirst && licenseType == null) && licenseTypeList != null && licenseTypeList.getList() != null && licenseTypeList.getList().size() > 0){
                    licenseType = licenseTypeList.getList().get(0);
                }

                isFirst = false;

                setDivingLicenseProgressBar(false);

                if (licenseType != null && NYHelper.isStringNotEmpty(licenseType.getName()))
                    licenseTypeEditText.setText(licenseType.getName());
            }
        };
    }


    private void setAssociationProgressBar(boolean isShow){
        if (isShow){
            organizationProgressBar.setVisibility(View.VISIBLE);
            organizationInputLayout.setVisibility(View.GONE);
        } else {
            organizationProgressBar.setVisibility(View.GONE);
            organizationInputLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setDivingLicenseProgressBar (boolean isShow){
        if (isShow){
            licenseTypeProgressBar.setVisibility(View.VISIBLE);
            licenseTypeInputLayout.setVisibility(View.GONE);
        } else {
            licenseTypeProgressBar.setVisibility(View.GONE);
            licenseTypeInputLayout.setVisibility(View.VISIBLE);
        }
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
        countryCodeSpinner = (NYSpinner) findViewById(R.id.country_code_spinner);
        countryCodeTextView = (TextView) findViewById(R.id.country_code_textView);
        birthPlaceEditText = (EditText)findViewById(R.id.birth_place_editText);
        birthDateEditText = (EditText) findViewById(R.id.birth_date_editText);
        certificateNumberEditText = (EditText)findViewById(R.id.certificate_number_editText);
        certificateDateEditText = (EditText)findViewById(R.id.ceritificate_date_editText);
        genderEditText = (EditText)findViewById(R.id.gender_editText);
        genderSpinner = (NYSpinner)findViewById(R.id.gender_spinner);
        birthDateButton = findViewById(R.id.birth_date_button);
        certificateDateButton = findViewById(R.id.ceritifcate_date_button);

        countryInputLayout = (TextInputLayout) findViewById(R.id.country_input_layout);
        nationalityInputLayout = (TextInputLayout) findViewById(R.id.nationality_input_layout);
        languageInputLayout = (TextInputLayout) findViewById(R.id.language_input_layout);
        organizationInputLayout = (TextInputLayout) findViewById(R.id.organization_input_layout);
        licenseTypeInputLayout = (TextInputLayout) findViewById(R.id.license_type_input_layout);

        countryEditText = (EditText) findViewById(R.id.country_editText);
        nationalityEditText = (EditText) findViewById(R.id.nationality_editText);
        languageEditText = (EditText) findViewById(R.id.language_editText);
        organizationEditText = (EditText) findViewById(R.id.organization_editText);
        licenseTypeEditText = (EditText) findViewById(R.id.license_type_editText);

        nationalityProgressBar = (ProgressBar) findViewById(R.id.nationality_progressBar);
        languageProgressBar = (ProgressBar) findViewById(R.id.language_progressBar);
        countryProgressBar = (ProgressBar) findViewById(R.id.country_progressBar);
        organizationProgressBar = (ProgressBar) findViewById(R.id.country_progressBar);
        licenseTypeProgressBar = (ProgressBar) findViewById(R.id.license_type_progressBar);

        emailEditText.setKeyListener(null);
        countryEditText.setKeyListener(null);
        nationalityEditText.setKeyListener(null);
        languageEditText.setKeyListener(null);
        organizationEditText.setKeyListener(null);
        licenseTypeEditText.setKeyListener(null);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }

    private void showCertificatePicker(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (Build.VERSION.SDK_INT >= 21) {
            final Dialog dialog = new Dialog(this);
            dialog.setTitle(new SimpleDateFormat(DISPLAY_DATE_FORMAT).format(date));
            dialog.setContentView(R.layout.view_date_picker);
            final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
            View setButton = dialog.findViewById(R.id.set_button);
            View cancelButton = dialog.findViewById(R.id.cancel_button);
            dialog.setCancelable(true);
            dialog.show();
            datePicker.setMinDate(NYHelper.getMinimumBirthdate());
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    dialog.setTitle(new SimpleDateFormat(DISPLAY_DATE_FORMAT).format(cal.getTime()));
                }
            });
            setButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    SimpleDateFormat format = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    Date birthday = cal.getTime();
                    certificateDate = cal.getTime();
                    certificateDateEditText.setText(format.format(birthday));
                    dialog.dismiss();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    Date birthday = cal.getTime();
                    certificateDate = cal.getTime();
                    certificateDateEditText.setText(format.format(birthday));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dialog.show();
            dialog.setCancelable(true);
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.setMinDate(NYHelper.getMinimumBirthdate());
            dialog.getDatePicker().setCalendarViewShown(false);
            dialog.getDatePicker().setSpinnersShown(true);
        }
    }

    private void showBirthdatePicker(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (Build.VERSION.SDK_INT >= 21) {
            final Dialog dialog = new Dialog(this);
            dialog.setTitle(new SimpleDateFormat(DISPLAY_DATE_FORMAT).format(date));
            dialog.setContentView(R.layout.view_date_picker);
            final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
            View setButton = dialog.findViewById(R.id.set_button);
            View cancelButton = dialog.findViewById(R.id.cancel_button);
            dialog.setCancelable(true);
            dialog.show();
            datePicker.setMinDate(NYHelper.getMinimumBirthdate());
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    dialog.setTitle(new SimpleDateFormat(DISPLAY_DATE_FORMAT).format(cal.getTime()));
                }
            });
            setButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    SimpleDateFormat format = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    Date birthday = cal.getTime();
                    dateBirth = cal.getTime();
                    birthDateEditText.setText(format.format(birthday));
                    dialog.dismiss();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    Date birthday = cal.getTime();
                    dateBirth = cal.getTime();
                    birthDateEditText.setText(format.format(birthday));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dialog.show();
            dialog.setCancelable(true);
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.setMinDate(NYHelper.getMinimumBirthdate());
            dialog.getDatePicker().setCalendarViewShown(false);
            dialog.getDatePicker().setSpinnersShown(true);
        }
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
        getLanguage();
        getOrganizationRequest();
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

    @Override
    public void onChooseListener(Object object) {

        if (object instanceof Country){
            //Toast.makeText(this, ((Country) object).getName(), Toast.LENGTH_SHORT).show();
            currentCountry = (Country) object;
            if (currentCountry != null && !TextUtils.isEmpty(currentCountry.getName())){
                countryEditText.setText(currentCountry.getName());
                getNationality(currentCountry.getId());
            }
        } else if (object instanceof Nationality){
            //Toast.makeText(this, ((Nationality) object).getName(), Toast.LENGTH_SHORT).show();
            currentNationality = (Nationality) object;
            if (currentNationality != null && !TextUtils.isEmpty(currentNationality.getName())){
                nationalityEditText.setText(currentNationality.getName());
            }
        } else if (object instanceof Language){
            //Toast.makeText(this, ((Nationality) object).getName(), Toast.LENGTH_SHORT).show();
            currentLanguage = (Language) object;
            if (currentLanguage != null && !TextUtils.isEmpty(currentLanguage.getName())){
                languageEditText.setText(currentLanguage.getName());
            }
        } else if (object instanceof Organization){
            organizationEditText.setText(((Organization) object).getName());
            this.organization = (Organization) object;
            getLicenseTypeRequest();
        } else if (object instanceof LicenseType){
            if (((LicenseType) object) != null && NYHelper.isStringNotEmpty(((LicenseType) object).getName())) licenseTypeEditText.setText(((LicenseType) object).getName());
            this.licenseType = (LicenseType) object;
        }

    }

    @Override
    public void onAcceptAgreementListener() {

    }

    @Override
    public void onCancelUpdate() {

    }

    @Override
    public void doUpdateVersion(String link) {

    }


}
