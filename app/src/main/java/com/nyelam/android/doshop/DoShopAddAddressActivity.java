package com.nyelam.android.doshop;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.StarterActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.City;
import com.nyelam.android.data.CityList;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.District;
import com.nyelam.android.data.DistrictList;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.data.DoShopAddressList;
import com.nyelam.android.data.Province;
import com.nyelam.android.data.ProvinceList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.general.CityAdapter;
import com.nyelam.android.general.CountryCodeAdapter;
import com.nyelam.android.general.DistrictAdapter;
import com.nyelam.android.general.ProvinceAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopAddAddressRequest;
import com.nyelam.android.http.NYDoShopAddressListRequest;
import com.nyelam.android.view.NYSpinner;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DoShopAddAddressActivity extends BasicActivity implements AdapterView.OnItemSelectedListener {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private DistrictAdapter districtAdapter;


    private Province currentProvince;
    private City currentCity;
    private District currentDistrict;

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

        String fullName = etFullName.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String zipCode = etZipCode.getText().toString();

//        String provinceId = null;
//        String cityId = null;
//        String districtId = null;

        if (!NYHelper.isStringNotEmpty(fullName)){
            Toast.makeText(context, getString(R.string.warn_field_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else if (!NYHelper.isStringNotEmpty(address)){
            Toast.makeText(context, getString(R.string.warn_field_address_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else if (!NYHelper.isStringNotEmpty(phone)){
            Toast.makeText(context, getString(R.string.warn_field_phone_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else if (!NYHelper.isStringNotEmpty(email)){
            Toast.makeText(context, getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else if (currentProvince == null){
            Toast.makeText(context, getString(R.string.warn_field_selected_province), Toast.LENGTH_SHORT).show();
        } else if (currentCity == null){
            Toast.makeText(context, getString(R.string.warn_field_selected_city), Toast.LENGTH_SHORT).show();
        } else if (currentDistrict == null){
            Toast.makeText(context, getString(R.string.warn_field_selected_district), Toast.LENGTH_SHORT).show();
        } else {
            addAddress(fullName, address, phone, email, currentProvince, currentCity, currentDistrict, zipCode);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_add_address);
        ButterKnife.bind(this);
        context = getApplicationContext();
        provinceAdapter = new ProvinceAdapter(this);
        cityAdapter = new CityAdapter(this);
        districtAdapter = new DistrictAdapter(this);
        loadProvince();
    }

    private void loadProvince() {

        OkHttpClient client = new OkHttpClient();
        // GET request
        Request request = new Request.Builder()
                .url("https://pro.rajaongkir.com/api/province")
                .get()
                .addHeader("key", " f6884fab1a6386a9438b9c541b1d3333")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();
                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONObject("rajaongkir").getJSONArray("results");

                        final ProvinceList provinceList = new ProvinceList();
                        provinceList.parse(Jarray);

                        provinceAdapter.clear();
                        if (provinceList != null && provinceList.getList() != null)provinceAdapter.addProvinces(provinceList.getList());

                        NYLog.e("cek province : "+provinceList.getList().toString());

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Log.d("UI thread", "I am the UI thread");
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
                                        Province province = provinceAdapter.getItem(position);

                                        Toast.makeText(context, province.getName(), Toast.LENGTH_SHORT).show();

                                        if (province != null && NYHelper.isStringNotEmpty(province.getName()))
                                            etProvince.setText(provinceAdapter.getItem(position).getName());
                                        if (currentProvince != province){
                                            currentCity = null;
                                            currentDistrict = null;
                                            loadCity(province.getId());
                                        }

                                    }
                                });


                                if (provinceList != null && provinceList.getList() != null && provinceList.getList().size() > 0){
                                    provinceAdapter.clear();
                                    provinceAdapter.addProvinces(provinceList.getList());
                                    provinceAdapter.notifyDataSetChanged();

                                    int pos = 0;
                                    for (Province province : provinceList.getList()){
                                        if (province != null && NYHelper.isStringNotEmpty(province.getName())){
                                            etProvince.setText(province.getName());
                                            currentProvince = province;
                                            loadCity(currentProvince.getId());
                                            spinnerProvince.setSelection(pos);
                                            provinceAdapter.setSelectedPosition(pos);
                                            provinceAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                        pos++;
                                    }
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void loadCity(String provinceId) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://pro.rajaongkir.com/api/city?province="+provinceId)
                .get()
                .addHeader("key", " f6884fab1a6386a9438b9c541b1d3333")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();
                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONObject("rajaongkir").getJSONArray("results");

                        final CityList cityList = new CityList();
                        cityList.parse(Jarray);

                        cityAdapter.clear();
                        if (cityList != null && cityList.getList() != null)cityAdapter.addCities(cityList.getList());

                        NYLog.e("cek city : "+cityList.getList().toString());

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Log.d("UI thread", "I am the UI thread");
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
                                        City city = cityAdapter.getItem(position);

                                        Toast.makeText(context, city.getName(), Toast.LENGTH_SHORT).show();

                                        if (city != null && NYHelper.isStringNotEmpty(city.getName()))
                                            etCity.setText(cityAdapter.getItem(position).getName());
                                        if (currentCity != city){
                                            currentDistrict = null;
                                            loadDistrict(city.getId());
                                        }

                                    }
                                });


                                if (cityList != null && cityList.getList() != null && cityList.getList().size() > 0){
                                    cityAdapter.clear();
                                    cityAdapter.addCities(cityList.getList());
                                    cityAdapter.notifyDataSetChanged();

                                    int pos = 0;
                                    for (City city : cityList.getList()){
                                        if (city != null && NYHelper.isStringNotEmpty(city.getName())){
                                            etCity.setText(city.getName());
                                            currentCity = city;
                                            loadDistrict(currentCity.getId());
                                            spinnerCitye.setSelection(pos);
                                            cityAdapter.setSelectedPosition(pos);
                                            cityAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                        pos++;
                                    }
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void loadDistrict(String cityId) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://pro.rajaongkir.com/api/subdistrict?city="+cityId)
                .get()
                .addHeader("key", " f6884fab1a6386a9438b9c541b1d3333")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();
                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONObject("rajaongkir").getJSONArray("results");

                        final DistrictList districtList = new DistrictList();
                        districtList.parse(Jarray);

                        districtAdapter.clear();
                        if (districtList != null && districtList.getList() != null)districtAdapter.addDistricts(districtList.getList());

                        NYLog.e("cek district : "+districtList.getList().toString());

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Log.d("UI thread", "I am the UI thread");
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
                                        District district = districtAdapter.getItem(position);

                                        Toast.makeText(context, district.getName(), Toast.LENGTH_SHORT).show();

                                        if (district != null && NYHelper.isStringNotEmpty(district.getName()))
                                            etDistrict.setText(districtAdapter.getItem(position).getName());
                                    }
                                });


                                if (districtList != null && districtList.getList() != null && districtList.getList().size() > 0){
                                    districtAdapter.clear();
                                    districtAdapter.addDistricts(districtList.getList());
                                    districtAdapter.notifyDataSetChanged();

                                    int pos = 0;
                                    for (District district : districtList.getList()){
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
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addAddress(String fullname, String address, String phone, String email, Province province, City city, District district, String zipCode){
        pDialog.show();
        NYDoShopAddAddressRequest req = null;
        try {
            req = new NYDoShopAddAddressRequest(context, fullname, address, email, phone, province, city, district, zipCode);
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
