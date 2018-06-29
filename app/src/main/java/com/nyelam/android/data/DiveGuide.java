package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveGuide implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_FULLNAME = "";
    private static String KEY_BIRTHDATE = "";
    private static String KEY_PICTURE = "";
    private static String KEY_GENDER = "";
    private static String KEY_BIRTHPLACE = "";
    private static String KEY_CERTIFICATE_ORGANIZATION = "certificate_organization";
    private static String KEY_CERTIFICATE_DIVER = "certificate_diver";
    private static String KEY_LANGUAGES = "languages";

    private String id;
    private String fullName;
    private String birthDate;
    private String picture;
    private String gender;
    private String birthPlace;
    private LicenseType certificateOrganization;
    private LicenseType certificateDiver;
    private List<Language> languages;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LicenseType getCertificateOrganization() {
        return certificateOrganization;
    }

    public void setCertificateOrganization(LicenseType certificateOrganization) {
        this.certificateOrganization = certificateOrganization;
    }

    public LicenseType getCertificateDiver() {
        return certificateDiver;
    }

    public void setCertificateDiver(LicenseType certificateDiver) {
        this.certificateDiver = certificateDiver;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ID)) {
                setId(obj.getString(KEY_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_FULLNAME)) {
                setFullName(obj.getString(KEY_FULLNAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_BIRTHDATE)) {
                setBirthDate(obj.getString(KEY_BIRTHDATE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_BIRTHPLACE)) {
                setBirthPlace(obj.getString(KEY_BIRTHPLACE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if(!obj.isNull(KEY_CERTIFICATE_ORGANIZATION)) {
                JSONObject o = obj.getJSONObject(KEY_CERTIFICATE_ORGANIZATION);
                certificateOrganization = new LicenseType();
                certificateOrganization.parse(o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!obj.isNull(KEY_CERTIFICATE_DIVER)) {
                JSONObject o = obj.getJSONObject(KEY_CERTIFICATE_DIVER);
                certificateDiver = new LicenseType();
                certificateDiver.parse(o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!obj.isNull(KEY_LANGUAGES)) {
            try {
                JSONArray array = obj.getJSONArray(KEY_LANGUAGES);
                if(array != null && array.length() > 0) {
                    languages = new ArrayList<>();
                    for(int i = 0; i <array.length(); i++) {
                        Language language = new Language();
                        language.parse(array.getJSONObject(i));
                        languages.add(language);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getId())) {
                obj.put(KEY_ID, getId());
            } else {
                obj.put(KEY_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getFullName())) {
                obj.put(KEY_FULLNAME, getFullName());
            } else {
                obj.put(KEY_FULLNAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getBirthDate())) {
                obj.put(KEY_BIRTHDATE, getBirthDate());
            } else {
                obj.put(KEY_BIRTHDATE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getBirthPlace())) {
                obj.put(KEY_BIRTHPLACE, getBirthPlace());
            } else {
                obj.put(KEY_BIRTHPLACE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getPicture())) {
                obj.put(KEY_PICTURE, getPicture());
            } else {
                obj.put(KEY_PICTURE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getGender())) {
                obj.put(KEY_GENDER, getGender());
            } else {
                obj.put(KEY_GENDER, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}


        try {
            if (getCertificateOrganization() != null) {
                JSONObject o = new JSONObject(getCertificateOrganization().toString());
                obj.put(KEY_CERTIFICATE_ORGANIZATION, o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (getCertificateDiver() != null) {
                JSONObject o = new JSONObject(getCertificateDiver().toString());
                obj.put(KEY_CERTIFICATE_DIVER, o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(languages != null && !languages.isEmpty()) {
            try {
                JSONArray array = new JSONArray();
                for(Language language : languages) {
                    JSONObject o = new JSONObject(language.toString());
                    array.put(o);
                }
                obj.put(KEY_LANGUAGES, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
