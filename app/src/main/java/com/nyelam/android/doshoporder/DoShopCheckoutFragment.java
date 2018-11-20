package com.nyelam.android.doshoporder;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.nyelam.android.dev.NYLog;
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


    private String paypalClientId = "AZpSKWx_d3bY8qO23Rr7hUbd5uUappmzGliQ1A2W5VWz4DVP011eNGN9k5NKu_sLhKFFQPvp5qgF4ptJ";
    private Intent paypalIntent;
    private int paypalRequestCode = 999;

    private String paymentType = "1"; // 1= bank transfer 2 = midtrans (credit), 3 (VA), 4 paypal
    private String paymentMethod = null; // 1 = virtual account dan 2 = credit card

    private DoShopCheckoutFragment thisFragment;
    private CheckoutListener listener;
    private DoShopCartReturn cartReturn;
    private DoShopAddress shippingAddress;
    private DoShopAddress billingAddress;
    private List<Courier> couriers;
    private Courier currentCourier;
    private CourierType currentCourierType;

    private CourierAdapter courierAdapter;
    private CourierTypeAdapter courierTypeAdapter;
    private DoShopCheckoutAdapter adapter;


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

    @BindView(R.id.ll_shipping_total_container)
    LinearLayout llShippingTotalContainer;

    @BindView(R.id.tv_shipping_total)
    TextView tvShippingTotal;

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
//        listener.proceedToChoosePayment();
//        NYDialogChooseAddress dialog = new NYDialogChooseAddress();
//        dialog.showChooseAddressDialog(getActivity(), null);
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
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
        adapter = new DoShopCheckoutAdapter(getActivity(), thisFragment);

        //ADAPTER SERVICE LIST
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
        recyclerView.setAdapter(adapter);
    }

    private void initCartReturn(DoShopCartReturn cartReturn){

//        List<DoShopProduct> products = new ArrayList<DoShopProduct>();
//        if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null){
//            for (DoShopMerchant merchant :cartReturn.getCart().getMerchants()){
//                if (merchant != null && merchant.getDoShopProducts() != null){
//                    for (DoShopProduct product : merchant.getDoShopProducts()){
//                        products.add(product);
//                    }
//                }
//            }
//        }

        adapter.setData(cartReturn.getCart().getMerchants());
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

    public void setViewBillingAddress(DoShopAddress billingAddress) {
        if (billingAddress != null){
            // TODO: bind data billing address
            if (NYHelper.isStringNotEmpty(billingAddress.getFullName()))tvAddressName.setText(billingAddress.getFullName());
            if (NYHelper.isStringNotEmpty(billingAddress.getAddress()))tvAddress.setText(billingAddress.getAddress());
            if (NYHelper.isStringNotEmpty(billingAddress.getPhoneNumber()))tvAddressPhone.setText(billingAddress.getPhoneNumber());
            llContainerPersonal.setVisibility(View.VISIBLE);
            tvChooseAddress.setVisibility(View.GONE);
            tvEditAddress.setVisibility(View.VISIBLE);
        }
    }

    public void setViewShippingAddress(DoShopAddress shippingAddress) {
        if (shippingAddress != null){
            // TODO: bind data shipping address
            if (NYHelper.isStringNotEmpty(shippingAddress.getFullName()))tvShippingAddressName.setText(shippingAddress.getFullName());
            if (NYHelper.isStringNotEmpty(shippingAddress.getAddress()))tvShippingAddress.setText(shippingAddress.getAddress());
            if (NYHelper.isStringNotEmpty(shippingAddress.getPhoneNumber()))tvShippingAddressPhone.setText(shippingAddress.getPhoneNumber());
            llContainerShippingAddress.setVisibility(View.VISIBLE);

            //if (address.getDistrict() != null && NYHelper.isStringNotEmpty(address.getDistrict().getId()))loadCourier("501", address.getDistrict().getId(),"1500");
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

                            if (currentCourierType != null && currentCourierType.getCourierCosts() != null && currentCourierType.getCourierCosts().size() > 0 ){
                                tvShippingTotal.setText(NYHelper.priceFormatter(currentCourierType.getCourierCosts().get(0).getValue()));
                                if (cartReturn != null && cartReturn.getCart() != null){
                                    double total = cartReturn.getCart().getTotal()+currentCourierType.getCourierCosts().get(0).getValue();
                                    tvTotal.setText(NYHelper.priceFormatter(total));
                                }
                                llShippingTotalContainer.setVisibility(View.VISIBLE);
                            } else {
                                llShippingTotalContainer.setVisibility(View.GONE);
                            }

                        }
                    });

                    if (couriers.size() > 0){
                        currentCourierType = courierTypes.get(0);
                        etCourierType.setText(currentCourierType.getService());
                        if (currentCourierType != null && currentCourierType.getCourierCosts() != null && currentCourierType.getCourierCosts().size() > 0 ){
                            tvShippingTotal.setText(NYHelper.priceFormatter(currentCourierType.getCourierCosts().get(0).getValue()));
                            if (cartReturn != null && cartReturn.getCart() != null){
                                double total = cartReturn.getCart().getTotal()+currentCourierType.getCourierCosts().get(0).getValue();
                                tvTotal.setText(NYHelper.priceFormatter(total));
                            }
                            llShippingTotalContainer.setVisibility(View.VISIBLE);
                        } else {
                            llShippingTotalContainer.setVisibility(View.GONE);
                        }
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

    public void chooseCourrier(DoShopMerchant merchant){



        if (merchant != null && merchant.getDoShopProducts() != null && merchant.getDoShopProducts().size() > 0 && shippingAddress != null && shippingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(shippingAddress.getDistrict().getId())){

            int totalWeight = 0;
            for (DoShopProduct product : merchant.getDoShopProducts()){
                totalWeight += product.getWeight()*1000;
            }

            Toast.makeText(getActivity(), "Choose courier", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), DoShopChooseCourierActivity.class);
            intent.putExtra(NYHelper.MERCHANT, merchant.toString());
            intent.putExtra(NYHelper.WEIGHT, totalWeight);
            intent.putExtra(NYHelper.DISTRICT_ID, shippingAddress.getDistrict().getId());
            startActivityForResult(intent, 101);
        } else {
            Toast.makeText(getActivity(), "Please, choose your billing and shipping adddress first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.hasExtra(NYHelper.ADDRESS)){
            try {
                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.ADDRESS));
                billingAddress = new DoShopAddress();
                billingAddress.parse(obj);
                // TODO: untuk semetara shipping sama billing address masih sama
                this.shippingAddress = billingAddress;
                setViewBillingAddress(billingAddress);
                setViewShippingAddress(shippingAddress);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//        intent.putExtra(NYHelper.MERCHANT, merchant.toString());
//        intent.putExtra(NYHelper.COURIER, currentCourier.toString());
//        intent.putExtra(NYHelper.COURIER_TYPE, currentCourierType.toString());
//        intent.putExtra(NYHelper.NOTE, note);


        if (cartReturn != null && data != null && data.hasExtra(NYHelper.MERCHANT)){
            try {
                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.MERCHANT));
                DoShopMerchant newMerchant = new DoShopMerchant();
                newMerchant.parse(obj);

                List<DoShopMerchant> merchantList = new ArrayList<DoShopMerchant>();
                for (DoShopMerchant mer : cartReturn.getCart().getMerchants()){
                    if (mer.getId().equals(newMerchant.getId())){
                        merchantList.add(newMerchant);
                    } else {
                        merchantList.add(mer);
                    }
                }

                cartReturn.getCart().setMerchants(merchantList);

                //Toast.makeText(getActivity(), "new cart", Toast.LENGTH_SHORT).show();
                //NYLog.e("cek new cart return : "+cartReturn.toString());

                // TODO: refresh cart
                initCartReturn(cartReturn);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        if (data != null && data.hasExtra(NYHelper.COURIER)){
//            try {
//                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.COURIER));
//                Courier courier = new Courier();
//                courier.parse(obj);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (data != null && data.hasExtra(NYHelper.COURIER_TYPE)){
//            try {
//                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.COURIER_TYPE));
//                CourierType courierType = new CourierType();
//                courierType.parse(obj);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (data != null && data.hasExtra(NYHelper.NOTE)){
//            String note = data.getStringExtra(NYHelper.COURIER_TYPE);
//        }

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


    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.bankTransferRadioButton) RadioButton bankTransferRadioButton;
    @BindView(R.id.virtualAccountRadioButton) RadioButton virtualAccountRadioButton;
    @BindView(R.id.creditCardRadioButton) RadioButton creditCardRadioButton;
    @BindView(R.id.paypalRadioButton) RadioButton paypalRadioButton;

    @BindView(R.id.payment_bank_transfer_linearLayout) LinearLayout bankTransferLinearLayout;
    @BindView(R.id.payment_virtual_account_linearLayout) LinearLayout virtualAccountLinearLayout;
    @BindView(R.id.payment_credit_card_linearLayout) LinearLayout creditCardLinearLayout;
    @BindView(R.id.payment_paypal_linearLayout) LinearLayout paypalLinearLayout;

    @OnClick(R.id.virtualAccountRadioButton) void setPayVA(){
        if (virtualAccountRadioButton.isChecked()){
            creditCardRadioButton.setChecked(false);
            bankTransferRadioButton.setChecked(false);
            paypalRadioButton.setChecked(false);
            paymentType = "3";
            paymentMethod = "1";
            //requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.creditCardRadioButton) void setPayCC(){
        if (creditCardRadioButton.isChecked()){
            virtualAccountRadioButton.setChecked(false);
            bankTransferRadioButton.setChecked(false);
            paypalRadioButton.setChecked(false);
            paymentType = "2";
            paymentMethod = "2";
            //requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.bankTransferRadioButton) void setPayBT(){
        if (bankTransferRadioButton.isChecked()){
            virtualAccountRadioButton.setChecked(false);
            creditCardRadioButton.setChecked(false);
            paypalRadioButton.setChecked(false);
            paymentType = "1";
            paymentMethod = null;
            //requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.payment_virtual_account_linearLayout) void setPayVA2(){
        virtualAccountRadioButton.setChecked(true);
        creditCardRadioButton.setChecked(false);
        bankTransferRadioButton.setChecked(false);
        paypalRadioButton.setChecked(false);
        paymentType = "3";
        paymentMethod = "1";
        //requestChangePaymentMethod();
    }

    @OnClick(R.id.payment_credit_card_linearLayout) void setPayCC2(){
        creditCardRadioButton.setChecked(true);
        virtualAccountRadioButton.setChecked(false);
        bankTransferRadioButton.setChecked(false);
        paypalRadioButton.setChecked(false);
        paymentType = "2";
        paymentMethod = "2";
        //requestChangePaymentMethod();
    }

    @OnClick(R.id.payment_bank_transfer_linearLayout) void setPayBT2(){
        bankTransferRadioButton.setChecked(true);
        virtualAccountRadioButton.setChecked(false);
        creditCardRadioButton.setChecked(false);
        paypalRadioButton.setChecked(false);
        paymentType = "1";
        paymentMethod = null;
        //requestChangePaymentMethod();
    }

    @OnClick(R.id.paypalRadioButton) void setPayPal(){
        if (paypalRadioButton.isChecked()){
            paypalRadioButton.setChecked(true);
            bankTransferRadioButton.setChecked(false);
            virtualAccountRadioButton.setChecked(false);
            creditCardRadioButton.setChecked(false);
            paymentType = "4";
            paymentMethod = null;
            //requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.payment_paypal_linearLayout) void setPayPal2(){
        paypalRadioButton.setChecked(true);
        bankTransferRadioButton.setChecked(false);
        virtualAccountRadioButton.setChecked(false);
        creditCardRadioButton.setChecked(false);
        paymentType = "4";
        paymentMethod = null;
        //requestChangePaymentMethod();
    }


}
