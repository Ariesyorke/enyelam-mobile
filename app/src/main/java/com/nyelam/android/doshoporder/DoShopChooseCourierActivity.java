package com.nyelam.android.doshoporder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.data.Courier;
import com.nyelam.android.data.CourierList;
import com.nyelam.android.data.CourierType;
import com.nyelam.android.data.DeliveryService;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.general.CourierAdapter;
import com.nyelam.android.general.CourierTypeAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoShopChooseCourierActivity extends BasicActivity implements AdapterView.OnItemSelectedListener {

    private DoShopChooseCourierActivity activity;
    private CourierAdapter courierAdapter;
    private CourierTypeAdapter courierTypeAdapter;
    private List<Courier> couriers;
    private Courier currentCourier;
    private CourierType currentCourierType;

    private String originDistrictId;
    private DoShopMerchant merchant;
    private int weight;

    @BindView(R.id.spinner_courier)
    NYSpinner spinnerCourier;

    @BindView(R.id.et_courier_type)
    EditText etCourierType;

    @BindView(R.id.spinner_courier_types)
    NYSpinner spinnerCourierTypes;

    @BindView(R.id.et_courier)
    EditText etCourier;

    @OnClick(R.id.iv_close) void close(){
        finish();
    }

    @OnClick(R.id.tv_choose) void choose(){

        String note = "";

        if (currentCourier != null && currentCourierType != null && merchant != null && currentCourierType.getCourierCosts() != null && currentCourierType.getCourierCosts().size() > 0){
            DeliveryService deliveryService = new DeliveryService();
            deliveryService.setName(currentCourier.getName()+" "+currentCourierType.getService());
            deliveryService.setPrice(currentCourierType.getCourierCosts().get(0).getValue());
            merchant.setDeliveryService(deliveryService);

            Intent intent=new Intent();
            intent.putExtra(NYHelper.MERCHANT, merchant.toString());
            //intent.putExtra(NYHelper.COURIER, currentCourier.toString());
            //intent.putExtra(NYHelper.COURIER_TYPE, currentCourierType.toString());
            //intent.putExtra(NYHelper.NOTE, note);
            activity.setResult(2,intent);
            activity.finish();
        } else {
            Toast.makeText(activity, "Please, select courier and delivery service", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_courier);
        ButterKnife.bind(this);
        activity = this;
        initExtra();
    }

    private void initExtra() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if(intent.hasExtra(NYHelper.WEIGHT)){
                weight = extras.getInt(NYHelper.WEIGHT);
            } else {
                weight = 1000;
            }

            if (weight == 0) weight=1000;

            if(intent.hasExtra(NYHelper.DISTRICT_ID) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DISTRICT_ID))){
                originDistrictId = extras.getString(NYHelper.DISTRICT_ID);
            }

            if(intent.hasExtra(NYHelper.MERCHANT) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.MERCHANT))){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(extras.getString(NYHelper.MERCHANT));
                    merchant = new DoShopMerchant();
                    merchant.parse(obj);
                    if (merchant != null && NYHelper.isStringNotEmpty(merchant.getDistrictId()))
                        loadCourier(originDistrictId, merchant.getDistrictId(),String.valueOf(weight));

                    //loadCourier(originDistrictId, merchant.getDistrictId(),weight);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void loadCourier(final String originId, final String destinationId, final String weight){

        //Toast.makeText(this, "load courier 1", Toast.LENGTH_SHORT).show();

        //final CourierList courierList = new CourierList();
        final List<Courier> couriers = new ArrayList<Courier>();
        couriers.add(new Courier(null, "Select Courier"));
        couriers.add(new Courier("jne", "JNE"));
        couriers.add(new Courier("tiki", "TIKI"));
        couriers.add(new Courier("jnt", "JNT"));

        this.couriers = couriers;
        courierAdapter = new CourierAdapter(this);
        courierAdapter.addCouriers(couriers);

        if (couriers != null){
            // TODO: masukkan ke spinner
            //Log.d("UI thread", "I am the UI thread");
            //Toast.makeText(this, "ONGKIR ADA "+String.valueOf(couriers.size()), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "load courier 2", Toast.LENGTH_SHORT).show();

            spinnerCourier.setAdapter(courierAdapter);
            spinnerCourier.setOnItemSelectedListener(activity);
            spinnerCourier.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                @Override
                public void onSpinnerOpened(Spinner spinner) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);

                }

                @Override
                public void onSpinnerClosed(Spinner spinner) {

                    int position = spinner.getSelectedItemPosition();
                    courierAdapter.setSelectedPosition(position);
                    Courier courier = courierAdapter.getItem(position);
                    //Toast.makeText(this, courier.getName(), Toast.LENGTH_SHORT).show();

                    if (courier != null && NYHelper.isStringNotEmpty(courier.getName()))
                        etCourier.setText(courier.getName());
                    if (courier.getCode() != null && currentCourier != courier){
                        currentCourier = courier;
                        currentCourierType = null;
                        // TODO: load courierTypes
                        //loadCourierTypes(currentCourier);
                        loadOngkir(originId, destinationId, weight, currentCourier.getCode());
                    } else {
                        currentCourier = courier;
                        currentCourierType = null;

                        List<CourierType> courierTypes = new ArrayList<>();
                        courierTypes.add(new CourierType("Select Service", null));
                        List<Courier>couriers = new ArrayList<>();
                        couriers.add(new Courier(null, "Select Courier", courierTypes));

                        CourierList courierList = new CourierList();
                        courierList.setList(couriers);
                        loadCourierTypes(courierList.getList().get(0));
                    }

                }
            });

            if (couriers.size() > 0){
                currentCourier = couriers.get(0);
                etCourier.setText(currentCourier.getName());
                loadOngkir(originId, destinationId, weight, currentCourier.getCode());
            }

        } else {
            // TODO: ongkir tidak tersedia
        }
    }


    private void loadOngkir(String originId, String destinationId, String weight, final String courier) {

        OkHttpClient client = new OkHttpClient();
        // GET request
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "origin="+originId+"&originType=subdistrict&destination="+destinationId+"&destinationType=subdistrict&weight="+weight+"&courier="+courier);
        Request request = new Request.Builder()
                .url("https://pro.rajaongkir.com/api/cost")
                .post(body)
                .addHeader("key", "f6884fab1a6386a9438b9c541b1d3333")
                .addHeader("content-type", "application/x-www-form-urlencoded")
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

                    //NYLog.e("CEK RAJA ONGKIR : "+jsonData);

                    try {
                        Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONObject("rajaongkir").getJSONArray("results");

                        final CourierList courierList = new CourierList();
                        courierList.parse(Jarray);

                        if (courierList != null && courierList.getList() != null && courierList.getList().size() > 0){

                            // TODO: masukkan ke spinner
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    //Log.d("UI thread", "I am the UI thread");
                                    //Toast.makeText(this, "ONGKIR ADA "+String.valueOf(courierList.getList().size()), Toast.LENGTH_SHORT).show();

                                    CourierType ct = new CourierType("Select Service", null);
                                    List<CourierType> courierTypes = new ArrayList<>();
                                    courierTypes.add(ct);
                                    Courier defaultCourier = new Courier(null, "Select Courier", courierTypes);

                                    // TODO: add selected service
                                    if (courierList != null && courierList.getList() != null && courierList.getList().size() > 0 &&
                                            courierList.getList().get(0).getCourierTypes() != null && courierList.getList().get(0).getCourierTypes().size() > 0){
                                        courierList.getList().get(0).getCourierTypes().add(0, ct);
                                    } else {
                                        List<Courier>couriers = new ArrayList<>();
                                        couriers.add(defaultCourier);
                                        courierList.setList(couriers);
                                    }

                                    loadCourierTypes(courierList.getList().get(0));
                                }
                            });

                        } else {
                            // TODO: ongkir tidak tersedia
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void loadCourierTypes(Courier currentCourier) {
        if (currentCourier != null && currentCourier.getCourierTypes() != null){

            // TODO: masukkan ke spinner
            final List<CourierType> courierTypes = currentCourier.getCourierTypes();

            courierTypeAdapter = new CourierTypeAdapter(this);
            courierTypeAdapter.addCouriers(courierTypes);

            this.runOnUiThread(new Runnable() {
                public void run() {
                    //Log.d("UI thread", "I am the UI thread");
                    spinnerCourierTypes.setAdapter(courierTypeAdapter);
                    spinnerCourierTypes.setOnItemSelectedListener(activity);
                    spinnerCourierTypes.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                        @Override
                        public void onSpinnerOpened(Spinner spinner) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);

                        }

                        @Override
                        public void onSpinnerClosed(Spinner spinner) {

                            int position = spinner.getSelectedItemPosition();
                            courierTypeAdapter.setSelectedPosition(position);
                            CourierType courierType = courierTypeAdapter.getItem(position);
                            currentCourierType = courierType;

                            if (courierType != null && NYHelper.isStringNotEmpty(courierType.getService())){
                                //etCourierType.setText(courierType.getService().toUpperCase());
                                etCourierType.setText(courierType.getService());
                            }
                        }
                    });

                    if (couriers.size() > 0){
                        currentCourierType = courierTypes.get(0);
                        etCourierType.setText(currentCourierType.getService());
                    }

                }
            });

        } else {
            // TODO: ongkir tidak tersedia
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
