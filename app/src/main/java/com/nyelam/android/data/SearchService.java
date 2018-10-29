package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public class SearchService extends SearchResult {

    private static String KEY_DIVE_SPOT = "dive_spot";
    private static String KEY_DIVE_SERVICE = "dive_service";
    private static String KEY_LICENSE = "license";
    private static String KEY_LICENSE_TYPE = "license_type";
    private static String KEY_ORGANIZATION = "organization";

    private String diveSpot;
    private String diveService;
    private boolean license;
    private Organization organization;
    private LicenseType licenseType;

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public String getDiveSpot() {
        return diveSpot;
    }

    public void setDiveSpot(String diveSpot) {
        this.diveSpot = diveSpot;
    }

    public String getDiveService() {
        return diveService;
    }

    public void setDiveService(String diveService) {
        this.diveService = diveService;
    }

    public boolean isLicense() {
        return license;
    }

    public void setLicense(boolean license) {
        this.license = license;
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
            if (!obj.isNull(KEY_NAME)) {
                setName(obj.getString(KEY_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_RATING)) {
                setRating(obj.getString(KEY_RATING));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TYPE)) {
                setType(obj.getInt(KEY_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DIVE_SPOT)) {
                setDiveSpot(obj.getString(KEY_DIVE_SPOT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DIVE_SERVICE)) {
                setDiveService(obj.getString(KEY_DIVE_SERVICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_LICENSE)) {
                if (obj.get(KEY_LICENSE) instanceof Integer){
                    if (obj.getInt(KEY_LICENSE) > 0){
                        setLicense(true);
                    } else {
                        setLicense(false);
                    }
                } else if (obj.get(KEY_LICENSE) instanceof  Boolean){
                    setLicense(obj.getBoolean(KEY_LICENSE));
                } else if (obj.get(KEY_LICENSE) instanceof String){
                    if (obj.getString(KEY_LICENSE).equals("1")){
                        setLicense(true);
                    } else {
                        setLicense(false);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
        try {
            if(!obj.isNull(KEY_ORGANIZATION)) {
                JSONObject o = obj.getJSONObject(KEY_ORGANIZATION);
                if(o != null) {
                    organization = new Organization();
                    organization.parse(o);
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
        try {
            if(!obj.isNull(KEY_LICENSE_TYPE)) {
                JSONObject o = obj.getJSONObject(KEY_LICENSE_TYPE);
                if(o != null) {
                    licenseType = new LicenseType();
                    licenseType.parse(o);
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
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
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_NAME, getName());
            } else {
                obj.put(KEY_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getRating())) {
                obj.put(KEY_RATING, getRating());
            } else {
                obj.put(KEY_RATING, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (getType() != null) {
                obj.put(KEY_TYPE, getType());
            } else {
                obj.put(KEY_TYPE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getDiveService())) {
                obj.put(KEY_DIVE_SERVICE, getDiveService());
            } else {
                obj.put(KEY_DIVE_SERVICE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getDiveSpot())) {
                obj.put(KEY_DIVE_SPOT, getDiveSpot());
            } else {
                obj.put(KEY_DIVE_SPOT, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_LICENSE, isLicense());
        } catch (JSONException e){e.printStackTrace();}

        try {
            if(organization != null) {
                JSONObject o = new JSONObject(organization.toString());
                obj.put(KEY_ORGANIZATION, o);
            }
        } catch (JSONException e) {e.printStackTrace();}
        try {
            if(licenseType != null) {
                JSONObject o = new JSONObject(licenseType.toString());
                obj.put(KEY_LICENSE_TYPE, o);
            }
        } catch (JSONException e) {e.printStackTrace();}
        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
