package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class DoShopAddress implements Parseable {

    private static String KEY_ADDRESS_ID = "address_id";
    private static String KEY_FULLNAME = "fullname";
    private static String KEY_ADDRESS = "address";
    private static String KEY_ZIP_CODE = "zip_code";
    private static String KEY_PROVINCE = "province";
    private static String KEY_DISTRICT = "district";
    private static String KEY_CITY = "city";
    private static String KEY_PHONE_NUMBER = "phone_number";
    private static String KEY_IS_PICKED = "is_picked";


    private String addressId;
    private String fullName;
    private String address;
    private String zipCode;
    private String phoneNumber;
    private Province province;
    private District district;
    private City city ;
    private boolean isPicked;


    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ADDRESS_ID)) {
                setAddressId(obj.getString(KEY_ADDRESS_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_FULLNAME)) {
                setFullName(obj.getString(KEY_FULLNAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ADDRESS)) {
                setAddress(obj.getString(KEY_ADDRESS));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ZIP_CODE)) {
                setZipCode(obj.getString(KEY_ZIP_CODE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_PHONE_NUMBER)) {
                setPhoneNumber(obj.getString(KEY_PHONE_NUMBER));
            }
        } catch (JSONException e) {e.printStackTrace();}


        if (obj.has(KEY_PROVINCE)){
            try {
                JSONObject o = obj.getJSONObject(KEY_PROVINCE);
                if(o != null) {
                    Province province = new Province();
                    province.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (obj.has(KEY_DISTRICT)){
            try {
                JSONObject o = obj.getJSONObject(KEY_DISTRICT);
                if(o != null) {
                    District district = new District();
                    district.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (obj.has(KEY_CITY)){
            try {
                JSONObject o = obj.getJSONObject(KEY_CITY);
                if(o != null) {
                    City city = new City();
                    city.parse(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!obj.isNull(KEY_IS_PICKED)) {
                setPicked(obj.getBoolean(KEY_IS_PICKED));
            }
        } catch (JSONException e) {e.printStackTrace();}


    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (NYHelper.isStringNotEmpty(getAddressId())) {
                obj.put(KEY_ADDRESS_ID, getAddressId());
            } else {
                obj.put(KEY_ADDRESS_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (NYHelper.isStringNotEmpty(getFullName())) {
                obj.put(KEY_FULLNAME, getFullName());
            } else {
                obj.put(KEY_FULLNAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (NYHelper.isStringNotEmpty(getAddress())) {
                obj.put(KEY_ADDRESS, getAddress());
            } else {
                obj.put(KEY_ADDRESS, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (NYHelper.isStringNotEmpty(getZipCode())) {
                obj.put(KEY_ZIP_CODE, getZipCode());
            } else {
                obj.put(KEY_ZIP_CODE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (NYHelper.isStringNotEmpty(getPhoneNumber())) {
                obj.put(KEY_PHONE_NUMBER, getPhoneNumber());
            } else {
                obj.put(KEY_PHONE_NUMBER, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}


        try {
            obj.put(KEY_IS_PICKED, isPicked);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
