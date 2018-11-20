package com.nyelam.android.doshop;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Helper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;
import com.nyelam.android.data.Courier;
import com.nyelam.android.data.CourierList;
import com.nyelam.android.data.CourierType;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.general.CourierAdapter;
import com.nyelam.android.general.CourierTypeAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.view.NYDialogChooseAddress;
import com.nyelam.android.view.NYSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopCheckoutFragment extends BasicFragment implements NYDialogChooseAddress.Listener, AdapterView.OnItemSelectedListener {

    private DoShopCheckoutFragment thisFragment;

    private CheckoutListener listener;
    private DoShopCartReturn cartReturn;
    private DoShopAddress address;
    private List<Courier> couriers;
    private Courier currentCourier;
    private CourierType currentCourierType;

    private CourierAdapter courierAdapter;
    private CourierTypeAdapter courierTypeAdapter;


    private DoShopCartListAdapter adapter;
    @BindView(R.id.ll_voucher_container)
    LinearLayout llVoucherContainer;

    @BindView(R.id.tv_voucher_code)
    TextView tvVoucherCode;

    @BindView(R.id.tv_voucher_total)
    TextView tvVoucherTotal;

    @BindView(R.id.tv_sub_total)
    TextView tvSubTotal;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;







    @BindView(R.id.ll_container_input_personal_information)
    LinearLayout llContainerInputPersonal;

    @BindView(R.id.tv_choose_address)
    TextView tvChooseAddress;

    @BindView(R.id.tv_edit_address)
    TextView tvEditAddress;

    @BindView(R.id.ll_container_personal_information)
    LinearLayout llContainerPersonal;

    @BindView(R.id.tv_address_name)
    TextView tvAddressName;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_address_phone)
    TextView tvAddressPhone;

    @BindView(R.id.ll_container_shipping_address)
    LinearLayout llContainerShippingAddress;

    @BindView(R.id.tv_shipping_address)
    TextView tvShippingAddress;

    @BindView(R.id.tv_shipping_address_name)
    TextView tvShippingAddressName;

    @BindView(R.id.tv_shipping_address_phone)
    TextView tvShippingAddressPhone;



    @BindView(R.id.et_courier)
    EditText etCourier;

    @BindView(R.id.spinner_courier)
    NYSpinner spinnerCourier;

    @BindView(R.id.et_courier_type)
    EditText etCourierType;

    @BindView(R.id.spinner_courier_types)
    NYSpinner spinnerCourierTypes;

//    @OnClick(R.id.tv_next_step_personal_information) void nextStepPersonalInformation(){
//        llContainerInputPersonal.setVisibility(View.GONE);
//        llContainerPersonal.setVisibility(View.VISIBLE);
//        listener.stepView(2);
//    }

    @OnClick(R.id.tv_checkout) void choosePayment(){
        listener.proceedToChoosePayment();
    }

    @OnClick(R.id.tv_choose_address) void chooseAddress(){
        //listener.proceedToChoosePayment();
//        NYDialogChooseAddress dialog = new NYDialogChooseAddress();
//        dialog.showChooseAddressDialog(getActivity(), null);
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, 100);
    }

    @OnClick(R.id.tv_edit_address) void editAddress(){
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
        startActivityForResult(intent, 100);
    }


    public DoShopCheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null && getArguments().get(NYHelper.CART_RETURN) != null){
            try {
                JSONObject obj = new JSONObject(getArguments().getString(NYHelper.CART_RETURN));
                cartReturn = new DoShopCartReturn();
                cartReturn.parse(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (CheckoutListener) getActivity();
        thisFragment = this;
        initAdapter();
        initCartReturn(cartReturn);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_do_shop_checkout;
    }

    private void initAdapter() {
        adapter = new DoShopCartListAdapter(getActivity(), thisFragment);

        //ADAPTER SERVICE LIST
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
        recyclerView.setAdapter(adapter);
    }

    private void initCartReturn(DoShopCartReturn cartReturn){

        List<DoShopProduct> products = new ArrayList<DoShopProduct>();
        if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null){
            for (DoShopMerchant merchant :cartReturn.getCart().getMerchants()){
                if (merchant != null && merchant.getDoShopProducts() != null){
                    for (DoShopProduct product : merchant.getDoShopProducts()){
                        products.add(product);
                    }
                }
            }
        }
        adapter.setData(products);
        adapter.notifyDataSetChanged();


        if (cartReturn != null && cartReturn.getCart() != null){
            tvTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getTotal()));
            tvSubTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getSubTotal()));

            if (cartReturn.getCart().getVoucher() != null){
                if (NYHelper.isStringNotEmpty(cartReturn.getCart().getVoucher().getCode()))tvVoucherCode.setText("Voucher ("+cartReturn.getCart().getVoucher().getCode()+")");
                tvVoucherTotal.setText(" - "+NYHelper.priceFormatter(cartReturn.getCart().getVoucher().getValue()));
                llVoucherContainer.setVisibility(View.VISIBLE);
            } else {
                llVoucherContainer.setVisibility(View.GONE);
            }
        }
    }

    public void setViewAddress(DoShopAddress viewAddress) {
        if (address != null){
            // TODO: bind data billing address
            if (NYHelper.isStringNotEmpty(address.getFullName()))tvAddressName.setText(address.getFullName());
            if (NYHelper.isStringNotEmpty(address.getAddress()))tvAddress.setText(address.getAddress());
            if (NYHelper.isStringNotEmpty(address.getPhoneNumber()))tvAddressPhone.setText(address.getPhoneNumber());
            llContainerPersonal.setVisibility(View.VISIBLE);
            tvChooseAddress.setVisibility(View.GONE);
            tvEditAddress.setVisibility(View.VISIBLE);

            // TODO: bind data shipping address
            if (NYHelper.isStringNotEmpty(address.getFullName()))tvShippingAddressName.setText(address.getFullName());
            if (NYHelper.isStringNotEmpty(address.getAddress()))tvShippingAddress.setText(address.getAddress());
            if (NYHelper.isStringNotEmpty(address.getPhoneNumber()))tvShippingAddressPhone.setText(address.getPhoneNumber());
            llContainerShippingAddress.setVisibility(View.VISIBLE);

            if (address.getDistrict() != null && NYHelper.isStringNotEmpty(address.getDistrict().getId()))loadCourier("501", address.getDistrict().getId(),"1500");
            //loadCourier("501", address.getDistrict().getId(),"1500");
        }
    }


    private void loadCourier(final String originId, final String destinationId, final String weight){

        Toast.makeText(getActivity(), "load courier 1", Toast.LENGTH_SHORT).show();

        listener.stepView(2);
        //final CourierList courierList = new CourierList();
        final List<Courier> couriers = new ArrayList<Courier>();
        couriers.add(new Courier("jne", "JNE"));
        couriers.add(new Courier("tiki", "TIKI"));

        this.couriers = couriers;
        courierAdapter = new CourierAdapter(getActivity());
        courierAdapter.addCouriers(couriers);

        if (couriers != null){
            // TODO: masukkan ke spinner
            //Log.d("UI thread", "I am the UI thread");
            //Toast.makeText(getActivity(), "ONGKIR ADA "+String.valueOf(couriers.size()), Toast.LENGTH_SHORT).show();

            Toast.makeText(getActivity(), "load courier 2", Toast.LENGTH_SHORT).show();

            spinnerCourier.setAdapter(courierAdapter);
            spinnerCourier.setOnItemSelectedListener(thisFragment);
            spinnerCourier.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                @Override
                public void onSpinnerOpened(Spinner spinner) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);

                }

                @Override
                public void onSpinnerClosed(Spinner spinner) {

                    int position = spinner.getSelectedItemPosition();
                    Courier courier = courierAdapter.getItem(position);
                    //Toast.makeText(getActivity(), courier.getName(), Toast.LENGTH_SHORT).show();

                    if (courier != null && NYHelper.isStringNotEmpty(courier.getCode()))
                        etCourier.setText(courier.getCode().toUpperCase());
                    if (currentCourier != courier){
                        currentCourier = courier;
                        currentCourierType = null;
                        // TODO: load courierTypes
                        //loadCourierTypes(currentCourier);
                        loadOngkir(originId, destinationId, weight, currentCourier.getCode());
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


    private void loadOngkir(String originId, String destinationId, String weight, String courier) {

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
                        try {
                            Jobject = new JSONObject(jsonData);
                            JSONArray Jarray = Jobject.getJSONObject("rajaongkir").getJSONArray("results");
//
                            final CourierList courierList = new CourierList();
                            courierList.parse(Jarray);

                            if (courierList != null && courierList.getList() != null && courierList.getList().size() > 0){

                                //courierAdapter = new CourierAdapter(getActivity());
                                //courierAdapter.addCouriers(courierList.getList());

                                // TODO: masukkan ke spinner
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        //Log.d("UI thread", "I am the UI thread");
                                        //Toast.makeText(getActivity(), "ONGKIR ADA "+String.valueOf(courierList.getList().size()), Toast.LENGTH_SHORT).show();
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

            courierTypeAdapter = new CourierTypeAdapter(getActivity());
            courierTypeAdapter.addCouriers(courierTypes);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Log.d("UI thread", "I am the UI thread");
                    spinnerCourierTypes.setAdapter(courierTypeAdapter);
                    spinnerCourierTypes.setOnItemSelectedListener(thisFragment);
                    spinnerCourierTypes.setSpinnerEventsListener(new NYSpinner.OnSpinnerEventsListener() {
                        @Override
                        public void onSpinnerOpened(Spinner spinner) {
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(spinner.getWindowToken(), 0);

                        }

                        @Override
                        public void onSpinnerClosed(Spinner spinner) {

                            int position = spinner.getSelectedItemPosition();
                            CourierType courierType = courierTypeAdapter.getItem(position);
                            currentCourierType = courierType;
                            //Toast.makeText(getActivity(), courierType.getName(), Toast.LENGTH_SHORT).show();

                            if (courierType != null && NYHelper.isStringNotEmpty(courierType.getService()))
                                etCourierType.setText(courierType.getService().toUpperCase());

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

    @Override
    public void onShopAgainListener() {

    }

    @Override
    public void onChoosedAddress(DoShopAddress address) {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.hasExtra(NYHelper.ADDRESS)){
            try {
                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.ADDRESS));
                address = new DoShopAddress();
                address.parse(obj);
                setViewAddress(address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        listener.stepView(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.setTitle("Checkout");
    }
}
