package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class User implements Parseable {

    private static String KEY_USER_ID = "user_id";
    private static String KEY_FULLNAME = "fullname";
    private static String KEY_FIRSTNAME = "firstname";
    private static String KEY_LASTNAME = "lastname";
    private static String KEY_PHONE = "phone_number";
//    private static String KEY_BIRTHDATE = "birthdate";
    private static String KEY_EMAIL = "email";
    private static String KEY_PICTURE = "picture";
    private static String KEY_GENDER = "gender";
    private static String KEY_IS_VERIFIED = "is_verified";
    private static String KEY_REFERRAL_CODE = "referral_code";
    private static String KEY_ADDESS = "address";
    private static String KEY_FILE_PATH = "file_path";
    private static String KEY_BIRTH_PLACE = "birthplace";
    private static String KEY_BIRTH_DATE = "birthdate";
    private static String KEY_CERTIFICATE_NUMBER = "certificate_number";
    private static String KEY_CERTIFICATE_DATE = "certificate_date";
    private static String KEY_USERNAME = "username";
    private static String KEY_COVER = "cover";
    private static String KEY_COUNTRY_CODE = "country_code";
    private static String KEY_SOCIAL_MEDIA = "social_media";

    private String userId, fullname, firstname, lastname,
            phone, email, picture, gender, refferalCode,
            address, birthPlace, certificateNumber;
    private boolean isVerified;
    private Date birthdate;
    private File imageFile;
    private List<SocialMedia> socialMedia;
    private String username, cover;
    private CountryCode countryCode;
    private Date certificateDate;

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Date getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(Date certificateDate) {
        this.certificateDate = certificateDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRefferalCode() {
        return refferalCode;
    }

    public void setRefferalCode(String refferalCode) {
        this.refferalCode = refferalCode;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public List<SocialMedia> getSocialMedia() {
        return socialMedia;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSocialMedia(List<SocialMedia> socialMedia) {
        this.socialMedia = socialMedia;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public void parse (JSONObject obj) {

        if (obj == null) return;


        try {
            if(!obj.isNull(KEY_BIRTH_PLACE)) {
                setBirthPlace(obj.getString(KEY_BIRTH_PLACE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(!obj.isNull(KEY_CERTIFICATE_DATE)) {
                long timestamp = obj.getLong(KEY_CERTIFICATE_DATE);
                certificateDate = new Date(timestamp * 1000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(!obj.isNull(KEY_CERTIFICATE_NUMBER)) {
                setCertificateNumber(obj.getString(KEY_CERTIFICATE_NUMBER));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!obj.isNull(KEY_USER_ID)) {
                setUserId(obj.getString(KEY_USER_ID));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_FULLNAME)) {
                setFullname(obj.getString(KEY_FULLNAME));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_FIRSTNAME)) {
                setFirstname(obj.getString(KEY_FIRSTNAME));
            }
        } catch (JSONException e){e.printStackTrace();}


        try {
            if (!obj.isNull(KEY_LASTNAME)) {
                setLastname(obj.getString(KEY_LASTNAME));
            }
        } catch (JSONException e){e.printStackTrace();}


        try {
            if (!obj.isNull(KEY_PHONE)) {
                setPhone(obj.getString(KEY_PHONE));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_EMAIL)) {
                setEmail(obj.getString(KEY_EMAIL));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_PICTURE)) {
                setPicture(obj.getString(KEY_PICTURE));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_COVER)) {
                setCover(obj.getString(KEY_COVER));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_USERNAME)) {
                setUsername(obj.getString(KEY_USERNAME));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_GENDER)) {
                setGender(obj.getString(KEY_GENDER));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_REFERRAL_CODE)) {
                setRefferalCode(obj.getString(KEY_REFERRAL_CODE));
            }
        } catch (JSONException e){e.printStackTrace();}



        try {
            if (!obj.isNull(KEY_IS_VERIFIED)) {
                setVerified(obj.getBoolean(KEY_IS_VERIFIED));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_BIRTH_DATE)) {
                long timestamp = obj.optLong(KEY_BIRTH_DATE) * 1000;
                setBirthdate(new Date(timestamp));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!obj.isNull(KEY_ADDESS)){
                setAddress(obj.getString(KEY_ADDESS));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        try {
            if(!obj.isNull(KEY_SOCIAL_MEDIA)) {
                socialMedia = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_SOCIAL_MEDIA);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    SocialMedia socMed = new SocialMedia();
                    socMed.parse(o);
                    socialMedia.add(socMed);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!obj.isNull(KEY_FILE_PATH)) {
                String filePath = obj.getString(KEY_FILE_PATH);
                imageFile = new File(filePath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            if(!obj.isNull(KEY_COUNTRY_CODE)) {
                JSONObject o = obj.getJSONObject(KEY_COUNTRY_CODE);
                if(o != null && o.length() > 0) {
                    countryCode = new CountryCode();
                    countryCode.parse(o);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if(!TextUtils.isEmpty(getBirthPlace())) {
                obj.put(KEY_BIRTH_PLACE, getBirthPlace());
            } else {
                obj.put(KEY_BIRTH_PLACE, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(!TextUtils.isEmpty(getCertificateNumber())) {
                obj.put(KEY_CERTIFICATE_NUMBER, getCertificateNumber());
            } else {
                obj.put(KEY_CERTIFICATE_NUMBER, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(getCertificateDate() != null) {
                obj.put(KEY_CERTIFICATE_DATE, getCertificateDate().getTime()/1000);
            } else {
                obj.put(KEY_CERTIFICATE_DATE, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!TextUtils.isEmpty(getUserId())) {
                obj.put(KEY_USER_ID, getUserId());
            } else  {
                obj.put(KEY_USER_ID, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getFullname())) {
                obj.put(KEY_FULLNAME, getFullname());
            } else  {
                obj.put(KEY_FULLNAME, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getFirstname())) {
                obj.put(KEY_FIRSTNAME, getFirstname());
            } else  {
                obj.put(KEY_FIRSTNAME, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getLastname())) {
                obj.put(KEY_LASTNAME,getLastname() );
            } else  {
                obj.put(KEY_LASTNAME, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getPhone())) {
                obj.put(KEY_PHONE, getPhone());
            } else  {
                obj.put(KEY_PHONE, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getUsername())) {
                obj.put(KEY_USERNAME, getUsername());
            } else  {
                obj.put(KEY_USERNAME, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getCover())) {
                obj.put(KEY_COVER, getCover());
            } else  {
                obj.put(KEY_COVER, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getEmail())) {
                obj.put(KEY_EMAIL, getEmail());
            } else  {
                obj.put(KEY_EMAIL, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getPicture())) {
                obj.put(KEY_PICTURE, getPicture() );
            } else  {
                obj.put(KEY_PICTURE, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getGender())) {
                obj.put(KEY_GENDER, getGender());
            } else  {
                obj.put(KEY_GENDER, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getRefferalCode())) {
                obj.put(KEY_REFERRAL_CODE, getRefferalCode() );
            } else  {
                obj.put(KEY_REFERRAL_CODE, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            obj.put(KEY_IS_VERIFIED, isVerified());
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (getBirthdate() != null) {
                long timestamp = getBirthdate().getTime() / 1000;
                obj.put(KEY_BIRTH_DATE, timestamp);
            } else  {
                obj.put(KEY_BIRTH_DATE, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getAddress())) {
                obj.put(KEY_ADDESS, getAddress() );
            } else  {
                obj.put(KEY_ADDESS, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();
        }
        try {
            if (getSocialMedia() != null && !getSocialMedia().isEmpty()) {
                JSONArray array = new JSONArray();
                for (SocialMedia a : getSocialMedia()) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_SOCIAL_MEDIA, array);
            } else  {
                obj.put(KEY_SOCIAL_MEDIA, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if(getImageFile() != null) {
                obj.put(KEY_FILE_PATH, getImageFile().getAbsolutePath());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            if(getCountryCode()!=null){
                JSONObject objCC = new JSONObject(getCountryCode().toString());
                obj.put(KEY_COUNTRY_CODE, objCC);
            } else {
                obj.put(KEY_COUNTRY_CODE, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }
}
