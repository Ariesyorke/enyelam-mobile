package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Country;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.Language;
import com.nyelam.android.data.LicenseType;
import com.nyelam.android.data.Nationality;
import com.nyelam.android.data.Organization;
import com.nyelam.android.data.Summary;
import com.nyelam.android.data.User;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYUpdateUserProfileRequest extends NYBasicAuthRequest<AuthReturn> {

    private static String KEY_USER = "user";

    private static String POST_FULLNAME = "fullname";
    private static String POST_USERNAME = "username";
    private static String POST_COUNTRY_CODE = "country_code";
    private static String POST_PHONE_NUMBER = "phone_number";
    private static String POST_GENDER = "gender";
    private static String POST_BIRTH_DATE = "birth_date";
    private static String POST_CERTIFICATE_DATE = "certificate_date";
    private static String POST_CERTIFICATE_NUMBER = "certificate_number";
    private static String POST_BIRTH_PLACE = "birth_place";

    private static String POST_COUNTRY_ID = "country_id";
    private static String POST_NATIONALITY_ID = "nationality_id";
    private static String POST_LANGUAGE_ID = "language_id";

    private static String POST_CERTIFICATE_DIVER = "certificate_diver";
    private static String POST_CERTIFICATE_ORGANIZATION = "certificate_organization";

    public NYUpdateUserProfileRequest(Context context, String fullname, String username, String countryCode, String phoneNumber,
                                      String gender, String birthDate, String dateCertificate,
                                      String certificateNumber, String birthPlace, Country currentCountry, Nationality currentNationality, Language currentLanguage,
                                      Organization organization, LicenseType licenseType) throws Exception {
        super(Summary.class, context, context.getResources().getString(R.string.api_path_update_profile));

        if (NYHelper.isStringNotEmpty(fullname)){
            addQuery(POST_FULLNAME, fullname);
        }

        if (NYHelper.isStringNotEmpty(username)){
            addQuery(POST_USERNAME, username);
        }

        if (NYHelper.isStringNotEmpty(countryCode)){
            addQuery(POST_COUNTRY_CODE, countryCode);
        }

        if (NYHelper.isStringNotEmpty(phoneNumber)){
            addQuery(POST_PHONE_NUMBER, phoneNumber);
        }

        if(NYHelper.isStringNotEmpty(gender)) {
            addQuery(POST_GENDER, gender);
        }

        if(NYHelper.isStringNotEmpty(birthDate)) {
            addQuery(POST_BIRTH_DATE, birthDate);
        }

        if(NYHelper.isStringNotEmpty(dateCertificate)) {
            addQuery(POST_CERTIFICATE_DATE, dateCertificate);
        }

        if(NYHelper.isStringNotEmpty(certificateNumber)) {
            addQuery(POST_CERTIFICATE_NUMBER, certificateNumber);
        }

        if(NYHelper.isStringNotEmpty(birthPlace)) {
            addQuery(POST_BIRTH_PLACE, birthPlace);
        }

        if(currentCountry != null && NYHelper.isStringNotEmpty(currentCountry.getId())) {
            addQuery(POST_COUNTRY_ID, currentCountry.getId());
        }

        if(currentNationality != null && NYHelper.isStringNotEmpty(currentNationality.getId())) {
            addQuery(POST_NATIONALITY_ID, currentNationality.getId());
        }

        if(currentLanguage != null && NYHelper.isStringNotEmpty(currentLanguage.getId())) {
            addQuery(POST_LANGUAGE_ID, currentLanguage.getId());
        }

        if(organization != null && NYHelper.isStringNotEmpty(organization.getId())) {
            addQuery(POST_CERTIFICATE_ORGANIZATION, organization.getId());
        }

        if(licenseType != null && NYHelper.isStringNotEmpty(licenseType.getId())) {
            addQuery(POST_CERTIFICATE_DIVER, licenseType.getId());
        }

    }

    @Override
    protected AuthReturn onProcessSuccessData(JSONObject obj) throws Exception {
        NYLog.e("USER DATA " + obj.toString(3));
        if (obj.has(KEY_USER) && obj.get(KEY_USER) != null){
            AuthReturn authReturn = new AuthReturn();
            authReturn.parse(obj);
            return authReturn;
        } else{
            return null;
        }
    }

}
