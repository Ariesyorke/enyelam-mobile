package com.nyelam.android.doshoporder;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Area;
import com.nyelam.android.data.AreaList;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.general.CityAdapter;
import com.nyelam.android.general.DistrictAdapter;
import com.nyelam.android.general.ProvinceAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopAddAddressRequest;
import com.nyelam.android.http.NYDoShopGetLocationRequest;
import com.nyelam.android.view.NYSpinner;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopAddAddressActivity extends BasicActivity implements AdapterView.OnItemSelectedListener {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private DistrictAdapter districtAdapter;


    private Area currentProvince;
    private Area currentCity;
    private Area currentDistrict;

    @BindView(R.id.et_label)
    EditText etLabel;

    @BindView(R.id.et_full_name)
    EditText etFullName;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_address)
    EditText etAddress;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_zipcode)
    EditText etZipCode;

    @BindView(R.id.et_province)
    EditText etProvince;

    @BindView(R.id.spinner_province)
    NYSpinner spinnerProvince;

    @BindView(R.id.et_city)
    EditText etCity;

    @BindView(R.id.spinner_city)
    NYSpinner spinnerCitye;

    @BindView(R.id.et_district)
    EditText etDistrict;

    @BindView(R.id.spinner_district)
    NYSpinner spinnerDistrict;

    @OnClick(R.id.tv_save) void saveAddress(){

        String label = etLabel.getText().toString();
        String fullName = etFullName.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String zipCode = etZipCode.getText().toString();

        if (!NYHelper.isStringNotEmpty(fullName)){
            Toast.makeText(context, getString(R.string.warn_field_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else if (!NYHelper.isStringNotEmpty(address)){
            Toast.makeText(context, getString(R.string.warn_field_address_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else if (currentProvince == null || currentProvince.getId() == null){
            Toast.makeText(context, getString(R.string.warn_field_selected_province), Toast.LENGTH_SHORT).show();
        } else if (currentCity == null || currentCity.getId() == null){
            Toast.makeText(context, getString(R.string.warn_field_selected_city), Toast.LENGTH_SHORT).show();
        } else if (currentDistrict == null || currentDistrict.getId() == null){
            Toast.makeText(context, getString(R.string.warn_field_selected_district), Toast.LENGTH_SHORT).show();
        } else if (!NYHelper.isStringNotEmpty(phone)){
            Toast.makeText(context, getString(R.string.warn_field_phone_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else if (!NYHelper.isStringNotEmpty(email)){
            Toast.makeText(context, getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else {
            addAddress(label, fullName, address, phone, email, currentProvince, currentCity, currentDistrict, zipCode);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_add_address);
        ButterKnife.bind(this);
        context = getApplicationContext();
        initToolbar();
        provinceAdapter = new ProvinceAdapter(this);
        cityAdapter = new CityAdapter(this);
        districtAdapter = new DistrictAdapter(this);
        //loadProvince();
        loadProvinces();
    }

    private void loadProvinces(){
        NYDoShopGetLocationRequest req = null;
        try {
            req = new NYDoShopGetLocationRequest(context, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onLoadProvincesRequest());
    }

    private RequestListener<AreaList> onLoadProvincesRequest() {
        return new RequestListener<AreaList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(AreaList areaList) {

                provinceAdapter.clear();

                spinnerProvince.setAdapter(provinceAdapter);
                spinnerProvince.setOnItemSelectedListener(DoShopAddAddressActivity.this);
                spinnerProvince.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                    @Override
                    public void onSpinnerOpened(Spinner spinner) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);

                    }

                    @Override
                    public void onSpinnerClosed(Spinner spinner) {

                        int position = spinner.getSelectedItemPosition();
                        Area province = provinceAdapter.getItem(position);

                        if (province != null && NYHelper.isStringNotEmpty(province.getName()) && NYHelper.isStringNotEmpty(province.getId())){


                            if (currentProvince != province && province.getId() != null){
                                currentCity = null;
                                cityAdapter.clear();
                                cityAdapter.notifyDataSetChanged();

                                currentDistrict = null;
                                districtAdapter.clear();
                                districtAdapter.notifyDataSetChanged();

                                loadCities(province.getId());
                            }

                            //Toast.makeText(context, province.getName()+" "+province.getId(), Toast.LENGTH_SHORT).show();
                            etProvince.setText(province.getName());

                            currentProvince = province;
                        }

                    }
                });


                if (areaList!= null && areaList.getList() != null)areaList.getList().add(0, new Area(null, "Choose Province"));

                if (areaList != null && areaList.getList() != null && areaList.getList().size() > 0){
                    provinceAdapter.clear();
                    provinceAdapter.addProvinces(areaList.getList());
                    provinceAdapter.notifyDataSetChanged();

                    int pos = 0;
                    for (Area province : areaList.getList()){
                        if (province != null && NYHelper.isStringNotEmpty(province.getName())){
                            etProvince.setText(province.getName());
                            currentProvince = province;
                            //loadCities(currentProvince.getId());
                            spinnerProvince.setSelection(pos);
                            provinceAdapter.setSelectedPosition(pos);
                            provinceAdapter.notifyDataSetChanged();
                            break;
                        }
                        pos++;
                    }
                }

            }
        };
    }


    private void loadCities(String provinceId){
        NYDoShopGetLocationRequest req = null;
        try {
            //Toast.makeText(context, "mulai", Toast.LENGTH_SHORT).show();
            req = new NYDoShopGetLocationRequest(context, provinceId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onLoadCitiesRequest());
    }

    private RequestListener<AreaList> onLoadCitiesRequest() {
        return new RequestListener<AreaList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {

                //Toast.makeText(context, "gagal", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRequestSuccess(AreaList areaList) {

                //Toast.makeText(context, "berhasil", Toast.LENGTH_SHORT).show();

                cityAdapter.clear();

                spinnerCitye.setAdapter(cityAdapter);
                spinnerCitye.setOnItemSelectedListener(DoShopAddAddressActivity.this);
                spinnerCitye.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                    @Override
                    public void onSpinnerOpened(Spinner spinner) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);

                    }

                    @Override
                    public void onSpinnerClosed(Spinner spinner) {

                        int position = spinner.getSelectedItemPosition();
                        Area city = cityAdapter.getItem(position);



                        if (city != null && NYHelper.isStringNotEmpty(city.getName())){

                            //Toast.makeText(context, city.getName(), Toast.LENGTH_SHORT).show();

                            if (currentCity != city && city.getId() != null){
                                currentCity = city;
                                currentDistrict = null;
                                districtAdapter.clear();
                                districtAdapter.notifyDataSetChanged();
                                loadDistrict(currentProvince.getId(), currentCity.getId());
                                etCity.setText(city.getName());
                            }

                        }


                    }
                });

                if (areaList!= null && areaList.getList() != null)areaList.getList().add(0, new Area(null, "Choose City"));

                if (areaList != null && areaList.getList() != null && areaList.getList().size() > 0){
                    cityAdapter.clear();
                    cityAdapter.addCities(areaList.getList());
                    cityAdapter.notifyDataSetChanged();

                    int pos = 0;
                    for (Area city : areaList.getList()){
                        if (city != null && NYHelper.isStringNotEmpty(city.getName())){
                            etCity.setText(city.getName());
                            currentCity = city;
                            //loadDistrict(currentProvince.getId(), currentCity.getId());
                            spinnerCitye.setSelection(pos);
                            cityAdapter.setSelectedPosition(pos);
                            cityAdapter.notifyDataSetChanged();
                            break;
                        }
                        pos++;
                    }
                }

            }
        };
    }


    private void loadDistrict(String provinceId, String cityId){
        NYDoShopGetLocationRequest req = null;
        try {
            req = new NYDoShopGetLocationRequest(context, provinceId, cityId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onLoadDistrictsRequest());
    }

    private RequestListener<AreaList> onLoadDistrictsRequest() {
        return new RequestListener<AreaList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(AreaList areaList) {

                districtAdapter.clear();

                spinnerDistrict.setAdapter(districtAdapter);
                spinnerDistrict.setOnItemSelectedListener(DoShopAddAddressActivity.this);
                spinnerDistrict.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                    @Override
                    public void onSpinnerOpened(Spinner spinner) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);
                    }

                    @Override
                    public void onSpinnerClosed(Spinner spinner) {

                        int position = spinner.getSelectedItemPosition();
                        Area district = districtAdapter.getItem(position);

                        if (district != null && NYHelper.isStringNotEmpty(district.getName())){
                            //Toast.makeText(context, district.getName(), Toast.LENGTH_SHORT).show();
                            etDistrict.setText(district.getName());
                            currentDistrict = district;
                        }
                    }
                });

                if (areaList!= null && areaList.getList() != null)areaList.getList().add(0, new Area(null, "Choose District"));

                if (areaList != null && areaList.getList() != null && areaList.getList().size() > 0){
                    districtAdapter.clear();
                    districtAdapter.addDistricts(areaList.getList());
                    districtAdapter.notifyDataSetChanged();

                    int pos = 0;
                    for (Area district : areaList.getList()){
                        if (district != null && NYHelper.isStringNotEmpty(district.getName())){
                            etDistrict.setText(district.getName());
                            currentDistrict = district;
                            spinnerDistrict.setSelection(pos);
                            districtAdapter.setSelectedPosition(pos);
                            districtAdapter.notifyDataSetChanged();
                            break;
                        }
                        pos++;
                    }
                }

            }
        };
    }


    private void addAddress(String label, String fullname, String address, String phone, String email, Area province, Area city, Area district, String zipCode){
        pDialog.show();
        NYDoShopAddAddressRequest req = null;
        try {
            req = new NYDoShopAddAddressRequest(context, null, label, fullname, address, email, phone, province, city, district, zipCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onAddAdressRequest());
    }

    private RequestListener<DoShopAddress> onAddAdressRequest() {
        return new RequestListener<DoShopAddress>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (pDialog != null )pDialog.dismiss();
                if (spiceException != null) {
                    NYHelper.handleAPIException(DoShopAddAddressActivity.this, spiceException, null);
                } else {
                    NYHelper.handleErrorMessage(DoShopAddAddressActivity.this, getResources().getString(R.string.warn_no_connection));
                }
            }

            @Override
            public void onRequestSuccess(DoShopAddress address) {
                pDialog.dismiss();
                finish();

                AlertDialog alertDialog = new AlertDialog.Builder(DoShopAddAddressActivity.this)
                        .setMessage(R.string.message_add_address_success)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .create();
                alertDialog.show();

                if (address != null){
//                    DoShopCategoryStorage storage = new DoShopCategoryStorage(DoShopActivity.this);
//                    storage.setCategoryList(categoryList);
//
//                    menuCategoryAdapter = new DoShopMenuCategoryAdapter( DoShopActivity.this, categoryList.getList());
//                    menuCategoryAdapter.notifyDataSetChanged();
//                    listViewMenu.setAdapter(menuCategoryAdapter);
                }

            }
        };
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
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
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
