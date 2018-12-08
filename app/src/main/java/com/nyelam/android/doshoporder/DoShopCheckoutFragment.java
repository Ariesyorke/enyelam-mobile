package com.nyelam.android.doshoporder;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.storage.PaymentMethodStorage;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;


import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;
import com.nyelam.android.VeritransNotificationActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.booking.BookingServiceSummaryActivity;
import com.nyelam.android.data.Additional;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.DeliveryService;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.data.DoShopAddressList;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopMerchantList;
import com.nyelam.android.data.DoShopOrder;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.NTransactionResult;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.OrderReturn;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.http.NYCartExpiredException;
import com.nyelam.android.http.NYChangePaymentMethodRequest;
import com.nyelam.android.http.NYDoDiveServiceOrderResubmitRequest;
import com.nyelam.android.http.NYDoShopAddressListRequest;
import com.nyelam.android.http.NYDoShopCheckPaymentMethodRequest;
import com.nyelam.android.http.NYDoShopReSubmitOrderRequest;
import com.nyelam.android.http.NYDoShopSubmitOrderRequest;
import com.nyelam.android.storage.CartStorage;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.storage.VeritransStorage;
import com.nyelam.android.view.NYCustomDialog;
import com.nyelam.android.view.NYDialogChooseAddress;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopCheckoutFragment extends BasicFragment implements
        NYDialogChooseAddress.Listener,
        AdapterView.OnItemSelectedListener,
        TransactionFinishedCallback {

    //Client ID Paypal
    //development
    private String paypalClientId = "AesXhJkhDyCXfFEiuR31DCeLPH4UqHB6nNTrjpvOmgh2VfRYzJTX-Cfq8X4h2GVvyyBoc81rXm8D8-1Z";
    //production
//    private String paypalClientId = "AZpSKWx_d3bY8qO23Rr7hUbd5uUappmzGliQ1A2W5VWz4DVP011eNGN9k5NKu_sLhKFFQPvp5qgF4ptJ";

    private PayPalConfiguration payPalConfiguration;
    private Intent paypalIntent;
    private int paypalRequestCode = 999;
    private boolean isTranssactionFailed = false;
    private boolean isTranssactionCanceled = false;


    //DATA YANG AKAN DIKIRIM
    private DoShopOrder orderReturn;
    private DoShopCartReturn cartReturn;
    private DoShopAddress shippingAddress;
    private DoShopAddress billingAddress;
    private String paymentType = "1"; // 1= bank transfer 2 = midtrans (credit), 3 (VA), 4 paypal
    private String paymentMethod = null; // 1 = virtual account dan 2 = credit card

    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopCheckoutFragment thisFragment;
    private CheckoutListener listener;
    private DoShopCheckoutAdapter adapter;

    @BindView(R.id.ll_voucher_container)
    LinearLayout llVoucherContainer;

    @BindView(R.id.tv_voucher_code)
    TextView tvVoucherCode;

    @BindView(R.id.tv_voucher_total)
    TextView tvVoucherTotal;

    @BindView(R.id.ll_additional_container)
    LinearLayout llAdditionalContainer;

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

    @BindView(R.id.tv_choose_billing_address)
    TextView tvChooseBillingAddress;

    @BindView(R.id.tv_edit_billing_address)
    TextView tvEditBillingAddress;

    @BindView(R.id.ll_container_billing_address)
    LinearLayout llContainerBillingAddress;

    @BindView(R.id.tv_address_name)
    TextView tvAddressName;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_address_phone)
    TextView tvAddressPhone;

    @BindView(R.id.ll_container_shipping_address)
    LinearLayout llContainerShippingAddress;

    @BindView(R.id.tv_choose_shipping_address)
    TextView tvChooseShippingAddress;

    @BindView(R.id.tv_edit_shipping_address)
    TextView tvEditShippingAddress;

    @BindView(R.id.tv_shipping_address)
    TextView tvShippingAddress;

    @BindView(R.id.tv_shipping_address_name)
    TextView tvShippingAddressName;

    @BindView(R.id.tv_shipping_address_phone)
    TextView tvShippingAddressPhone;

    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @OnClick(R.id.tv_checkout) void choosePayment(){
        //listener.proceedToChoosePayment();
        //req = new NYDoShopSubmitOrderRequest(getActivity(), paymentMethod, cartToken, billingAddress.getAddressId(), shippingAddress.getAddressId(), deliveryServices, null, null);

        // TODO: check delivery service
        List<DeliveryService> deliveryServices = new ArrayList<>();
        int merchantSize = 0;
        if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null){
            for (DoShopMerchant merchant : cartReturn.getCart().getMerchants()){
                if (merchant.getDeliveryService() != null)deliveryServices.add(merchant.getDeliveryService());
                merchantSize++;
            }
        }

        // TODO: check parameter yang akan dikirim
        if (cartReturn == null || !NYHelper.isStringNotEmpty(cartReturn.getCartToken())){
            Toast.makeText(getActivity(), "Please, choose billing address first", Toast.LENGTH_SHORT).show();
        } else if (billingAddress == null || !NYHelper.isStringNotEmpty(billingAddress.getAddressId())){
            Toast.makeText(getActivity(), "Please, choose billing address first", Toast.LENGTH_SHORT).show();
        } else if (shippingAddress == null || !NYHelper.isStringNotEmpty(shippingAddress.getAddressId())){
            Toast.makeText(getActivity(), "Please, choose shipping address first", Toast.LENGTH_SHORT).show();
        } else if(deliveryServices == null || deliveryServices.size() < merchantSize){
            Toast.makeText(getActivity(), "Please, choose delivery services first", Toast.LENGTH_SHORT).show();
        } else if (!NYHelper.isStringNotEmpty(paymentType)){
            Toast.makeText(getActivity(), "Please, choose payment method first", Toast.LENGTH_SHORT).show();
        } else if (!checkBox.isChecked()){
            Toast.makeText(getActivity(), "Please, checklist agreements", Toast.LENGTH_SHORT).show();
        } else {

            //getSubmitOrder(paymentMethod, cartReturn.getCartToken(), billingAddress.getAddressId(), shippingAddress.getAddressId(), deliveryServices, null, null);

            if (isTranssactionCanceled){
                // TODO: resubmit order
                onResubmitOrder();
                //getSubmitOrder(paymentType, cartReturn.getCartToken(), billingAddress.getAddressId(), shippingAddress.getAddressId(), deliveryServices, null, null);
            } else if (orderReturn == null && isTranssactionFailed){
                // TODO: request ulang cart token atau cart return
                //new NYCustomDialog().showAgreementDialog(getActivity());
                //getSubmitOrder(paymentType, cartReturn.getCartToken(), billingAddress.getAddressId(), shippingAddress.getAddressId(), deliveryServices, null, null);
                getSubmitOrder(paymentType, cartReturn.getCartToken(), billingAddress.getAddressId(), shippingAddress.getAddressId(), cartReturn.getCart().getMerchants(), null, null);
            } else if (orderReturn == null){
                //new NYCustomDialog().showAgreementDialog(getActivity());
                //getSubmitOrder(paymentType, cartReturn.getCartToken(), billingAddress.getAddressId(), shippingAddress.getAddressId(), deliveryServices, null, null);
                getSubmitOrder(paymentType, cartReturn.getCartToken(), billingAddress.getAddressId(), shippingAddress.getAddressId(), cartReturn.getCart().getMerchants(), null, null);
            } else {
                payUsingVeritrans();
            }

        }

    }

    private void onResubmitOrder() {
        pDialog.show();
        try {
            NYDoShopReSubmitOrderRequest req = new NYDoShopReSubmitOrderRequest(getActivity(), orderReturn.getOrderId(),  paymentType);
            spcMgr.execute(req, onResubmitOrderRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<DoShopOrder> onResubmitOrderRequest() {
        return new RequestListener<DoShopOrder>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }

                if(spiceException != null) {
                    if (spiceException.getCause() instanceof NYCartExpiredException) {
                        NYHelper.handlePopupMessage(getActivity(), "Sorry, Your Cart Session has Expired. Please Re-Order.", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            }
                        });
                    } else {
                        NYHelper.handleAPIException(getActivity(), spiceException, false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onRequestSuccess(final DoShopOrder result) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }

                //Toast.makeText(BookingServiceSummaryActivity.this, "cancel success", Toast.LENGTH_SHORT).show();

                orderReturn = result;

                if (orderReturn != null){

                    NYLog.e("re-submit order : "+orderReturn.toString());
                    NYLog.e("payment Type : " + paymentType);

                    if ((paymentType.equals("2") || paymentType.equals("3")) && result != null && result.getVeritransToken() != null && NYHelper.isStringNotEmpty(result.getVeritransToken().getTokenId())){

                        //Toast.makeText(BookingServiceSummaryActivity.this, "success veritrans", Toast.LENGTH_SHORT).show();

                        //TODO KALO TYPE PEMBAYARANNYA MIDTRANS
                        VeritransStorage veritransStorage = new VeritransStorage(getActivity());
                        veritransStorage.veritransToken = result.getVeritransToken().getTokenId();

                        Contact contact = new Contact();
                        contact.setName(billingAddress.getFullName());
                        contact.setEmailAddress(billingAddress.getEmail());
                        contact.setPhoneNumber(billingAddress.getPhoneNumber());
                        veritransStorage.contact = contact;

                        Cart cart = new Cart();
                        cart.setSubTotal(result.getCart().getSubTotal());
                        cart.setTotal(result.getCart().getTotal());
                        cart.setVoucher(result.getCart().getVoucher());
                        cart.setCurrency(null);
                        veritransStorage.cart = cart;

                        Order order = new Order();
                        order.setOrderId(result.getOrderId());
                        order.setAdditionals(result.getAdditionals());
                        order.setCart(cart);
                        order.setStatus(order.getStatus());
                        veritransStorage.order = order;

                        veritransStorage.totalParticipants = 1;
                        veritransStorage.save();

                        payUsingVeritrans();

                    } else if (paymentType.equals("1")){

                        //Toast.makeText(BookingServiceSummaryActivity.this, "success bank transfer", Toast.LENGTH_SHORT).show();
                        //TODO DISINI HANDLE KALO TRANSAKSI DI BANK TRANSFER SUKSES
                        NYHelper.handlePopupMessage(getActivity(), getString(R.string.transaction_success), false,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
                                        intent.putExtra(NYHelper.ID_ORDER_DO_SHOP, orderReturn.getOrderId());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }, getResources().getString(R.string.check_order));
                    } else if (paymentType.equals("4")){

                        //Toast.makeText(BookingServiceSummaryActivity.this, "success paypal", Toast.LENGTH_SHORT).show();
                        //TODO DISINI HANDLE KALO TRANSAKSI DI PAYPAL SUKSES
                        payUsingPaypal();
                    } else {
                        //Toast.makeText(BookingServiceSummaryActivity.this, "success else", Toast.LENGTH_SHORT).show();
                    }

                }


                // TODO: hapus cart di cache
                CartStorage storage = new CartStorage(getActivity());
                storage.setMerchants(null);
                storage.save();

            }
        };
    }


    @OnClick(R.id.tv_choose_billing_address) void chooseBillingAddress(){
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
        intent.putExtra(NYHelper.TAG, "billing");
        startActivityForResult(intent, 100);
    }

    @OnClick(R.id.tv_edit_billing_address) void editBillingAddress(){
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
        intent.putExtra(NYHelper.TAG, "billing");
        startActivityForResult(intent, 100);
    }

    @OnClick(R.id.tv_choose_shipping_address) void chooseShippingAddress(){
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
        intent.putExtra(NYHelper.TAG, "shipping");
        startActivityForResult(intent, 101);
    }

    @OnClick(R.id.tv_edit_shipping_address) void editShippingAddress(){
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
        intent.putExtra(NYHelper.TAG, "shipping");
        startActivityForResult(intent, 101);
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
        loadAddress();
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


        adapter.setData(cartReturn.getCart().getMerchants());
        adapter.notifyDataSetChanged();


        if (cartReturn != null && cartReturn.getCart() != null){

            double totalShipping = 0;
            if (cartReturn.getCart().getMerchants() != null){
                for (DoShopMerchant merchant : cartReturn.getCart().getMerchants()){
                    if (merchant != null && merchant.getDeliveryService() != null)totalShipping += merchant.getDeliveryService().getPrice();
                }
            }

            tvShippingTotal.setText(NYHelper.priceFormatter(totalShipping));
            llShippingTotalContainer.setVisibility(View.VISIBLE);

            double additionalsFee = 0;
            llAdditionalContainer.removeAllViews();
            if (cartReturn.getAdditionals() != null && cartReturn.getAdditionals().size() > 0){
                for (Additional additional : cartReturn.getAdditionals()){
                    LayoutInflater inflaterAddons = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View additionalView = inflaterAddons.inflate(R.layout.view_item_additional_doshop, null);

                    TextView tvLabel = (TextView) additionalView.findViewById(R.id.additional_label_textView);
                    TextView tvValue = (TextView) additionalView.findViewById(R.id.additional_value_textView);

                    if (additional != null) {
                        additionalsFee+=additional.getValue();
                        if (NYHelper.isStringNotEmpty(additional.getTitle())) tvLabel.setText(additional.getTitle());
                        tvValue.setText(NYHelper.priceFormatter(additional.getValue()));
                        llAdditionalContainer.addView(additionalView);
                    }
                }
            }


            tvSubTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getSubTotal()));
            // TODO: total price + shipping + payment fee
            tvTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getTotal()+totalShipping+additionalsFee));

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
            if (NYHelper.isStringNotEmpty(billingAddress.getPhoneNumber()))tvAddressPhone.setText(billingAddress.getPhoneNumber());
            String addressString = "";
            if (NYHelper.isStringNotEmpty(billingAddress.getAddress())) addressString+=billingAddress.getAddress();
            if (billingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(billingAddress.getDistrict().getName())) addressString+=", "+billingAddress.getDistrict().getName();
            if (billingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(billingAddress.getCity().getName())) addressString+=", "+billingAddress.getCity().getName();
            if (billingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(billingAddress.getProvince().getName())) addressString+=", "+billingAddress.getProvince().getName();
            if (billingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(billingAddress.getZipCode())) addressString+=", "+billingAddress.getZipCode();
            if (NYHelper.isStringNotEmpty(addressString)) tvAddress.setText(addressString);

            llContainerBillingAddress.setVisibility(View.VISIBLE);
            tvChooseBillingAddress.setVisibility(View.GONE);
            tvEditBillingAddress.setVisibility(View.VISIBLE);
        }
    }

    public void setViewShippingAddress(DoShopAddress shippingAddress) {
        if (shippingAddress != null){

            // TODO: bind data shipping address
            if (NYHelper.isStringNotEmpty(shippingAddress.getFullName()))tvShippingAddressName.setText(shippingAddress.getFullName());
            if (NYHelper.isStringNotEmpty(shippingAddress.getPhoneNumber()))tvShippingAddressPhone.setText(shippingAddress.getPhoneNumber());

            String addressString = "";
            if (NYHelper.isStringNotEmpty(shippingAddress.getAddress())) addressString+=shippingAddress.getAddress();
            if (shippingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(shippingAddress.getDistrict().getName())) addressString+=", "+shippingAddress.getDistrict().getName();
            if (shippingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(shippingAddress.getCity().getName())) addressString+=", "+shippingAddress.getCity().getName();
            if (shippingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(shippingAddress.getProvince().getName())) addressString+=", "+shippingAddress.getProvince().getName();
            if (shippingAddress.getDistrict() != null && NYHelper.isStringNotEmpty(shippingAddress.getZipCode())) addressString+=", "+shippingAddress.getZipCode();
            if (NYHelper.isStringNotEmpty(addressString)) tvShippingAddress.setText(addressString);

            llContainerShippingAddress.setVisibility(View.VISIBLE);
            tvChooseShippingAddress.setVisibility(View.GONE);
            tvEditShippingAddress.setVisibility(View.VISIBLE);
        }
    }

    //private void getSubmitOrder(String paymentMethod, String cartToken, String billingAddressId, String shippingAddressId, List<DeliveryService> deliveryServices, String typeId, String voucher){
    private void getSubmitOrder(String paymentMethod, String cartToken, String billingAddressId, String shippingAddressId, List<DoShopMerchant> merchants, String typeId, String voucher){
        pDialog.show();
        NYDoShopSubmitOrderRequest req = null;
        try {
            req = new NYDoShopSubmitOrderRequest(getActivity(), paymentMethod, cartToken, billingAddressId, shippingAddressId, merchants, typeId, voucher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onSubmitOrderRequest());
    }

    private RequestListener<DoShopOrder> onSubmitOrderRequest() {
        return new RequestListener<DoShopOrder>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                pDialog.dismiss();
                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(DoShopOrder result) {
                pDialog.dismiss();
                //thisFragment.cartReturn = cartReturn;
                //initCartReturn(cartReturn);

                //kasih flag jika nanti setelah masuk payment dibatalkan
                isTranssactionCanceled = true;

                orderReturn = result;

                if (orderReturn != null){

                    if ((paymentType.equals("2") || paymentType.equals("3")) && result != null && result.getVeritransToken() != null){

                        //TODO KALO TYPE PEMBAYARANNYA MIDTRANS
                        VeritransStorage veritransStorage = new VeritransStorage(getActivity());
                        veritransStorage.veritransToken = result.getVeritransToken().getTokenId();

                        Contact contact = new Contact();
                        contact.setName(billingAddress.getFullName());
                        contact.setEmailAddress(billingAddress.getEmail());
                        contact.setPhoneNumber(billingAddress.getPhoneNumber());
                        veritransStorage.contact = contact;

                        Cart cart = new Cart();
                        cart.setSubTotal(result.getCart().getSubTotal());
                        cart.setTotal(result.getCart().getTotal());
                        cart.setVoucher(result.getCart().getVoucher());
                        cart.setCurrency(null);
                        veritransStorage.cart = cart;

                        Order order = new Order();
                        order.setOrderId(result.getOrderId());
                        order.setAdditionals(result.getAdditionals());
                        order.setCart(cart);
                        order.setStatus(order.getStatus());
                        veritransStorage.order = order;

                        veritransStorage.totalParticipants = 1;
                        veritransStorage.save();

                        payUsingVeritrans();

                    } else if (paymentType.equals("1")){
                        //TODO DISINI HANDLE KALO TRANSAKSI DI BANK TRANSFER SUKSES
                        NYHelper.handlePopupMessage(getActivity(), getString(R.string.transaction_success), false,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
                                        intent.putExtra(NYHelper.ID_ORDER_DO_SHOP, orderReturn.getOrderId());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }, getResources().getString(R.string.check_order));
                    } else if (paymentType.equals("4")){
                        //TODO DISINI HANDLE KALO TRANSAKSI DI PAYPAL SUKSES
                        payUsingPaypal();
                    }

                }


                // TODO: hapus cart di cache
                CartStorage storage = new CartStorage(getActivity());
                storage.setMerchants(null);
                storage.save();

            }
        };
    }


    private void loadAddress(){
        pDialog.show();
        NYDoShopAddressListRequest req = null;
        try {
            req = new NYDoShopAddressListRequest(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onLoadAdressRequest());
    }

    private RequestListener<DoShopAddressList> onLoadAdressRequest() {
        return new RequestListener<DoShopAddressList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                pDialog.dismiss();
                // TODO: ini cart after get address
                initCartReturn(cartReturn);
            }

            @Override
            public void onRequestSuccess(DoShopAddressList addressList) {
                pDialog.dismiss();
                if (addressList != null && addressList.getList() != null){
                    for (DoShopAddress address : addressList.getList()){
                        if (address != null && address.getDefaultBilling() == 1){
                            billingAddress = address;
                            setViewBillingAddress(billingAddress);
                        }

                        if (address != null && address.getDefaultShipping() == 1){
                            shippingAddress = address;
                            setViewShippingAddress(shippingAddress);
                        }
                    }
                }
                // TODO: ini cart after get address
                initCartReturn(cartReturn);
            }
        };
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

            double totalWeight = 0;
            for (DoShopProduct product : merchant.getDoShopProducts()){
                totalWeight += product.getWeight()*1000;
            }

            Intent intent = new Intent(getActivity(), DoShopChooseCourierActivity.class);
            intent.putExtra(NYHelper.MERCHANT, merchant.toString());
            intent.putExtra(NYHelper.WEIGHT, (int)totalWeight);
            intent.putExtra(NYHelper.DISTRICT_ID, shippingAddress.getDistrict().getId());

            startActivityForResult(intent, 101);
        } else {
            Toast.makeText(getActivity(), "Please, choose your billing and shipping adddress first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.hasExtra(NYHelper.ADDRESS) && data.hasExtra(NYHelper.TAG) && data.getStringExtra(NYHelper.TAG).equals("billing")){

            //Toast.makeText(getActivity(), data.getStringExtra(NYHelper.TAG), Toast.LENGTH_SHORT).show();

            try {
                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.ADDRESS));
                billingAddress = new DoShopAddress();
                billingAddress.parse(obj);
                setViewBillingAddress(billingAddress);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (data != null && data.hasExtra(NYHelper.ADDRESS) && data.hasExtra(NYHelper.TAG) && data.getStringExtra(NYHelper.TAG).equals("shipping")){

            //Toast.makeText(getActivity(), data.getStringExtra(NYHelper.TAG), Toast.LENGTH_SHORT).show();

            try {
                resetDeliveryService(); //reset delivery service
                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.ADDRESS));
                shippingAddress = new DoShopAddress();
                shippingAddress.parse(obj);
                setViewShippingAddress(shippingAddress);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (data != null && data.hasExtra(NYHelper.ADDRESS)){

            //Toast.makeText(getActivity(), data.getStringExtra(NYHelper.TAG), Toast.LENGTH_SHORT).show();

            try {
                resetDeliveryService(); //reset delivery service
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

                // TODO: refresh cart
                initCartReturn(cartReturn);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void resetDeliveryService(){
        // TODO: reset delivery service
        List<DoShopMerchant> merchants = new ArrayList<>();
        if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null){
            for (DoShopMerchant merchant : cartReturn.getCart().getMerchants()){
                if (merchant != null){
                    DoShopMerchant m = merchant;
                    m.setDeliveryService(null);
                    merchants.add(m);
                }
            }
        }
        cartReturn.getCart().setMerchants(merchants);
        initCartReturn(cartReturn);
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.stepView(1);
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
            requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.creditCardRadioButton) void setPayCC(){
        if (creditCardRadioButton.isChecked()){
            virtualAccountRadioButton.setChecked(false);
            bankTransferRadioButton.setChecked(false);
            paypalRadioButton.setChecked(false);
            paymentType = "2";
            paymentMethod = "2";
            requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.bankTransferRadioButton) void setPayBT(){
        if (bankTransferRadioButton.isChecked()){
            virtualAccountRadioButton.setChecked(false);
            creditCardRadioButton.setChecked(false);
            paypalRadioButton.setChecked(false);
            paymentType = "1";
            paymentMethod = null;
            requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.payment_virtual_account_linearLayout) void setPayVA2(){
        virtualAccountRadioButton.setChecked(true);
        creditCardRadioButton.setChecked(false);
        bankTransferRadioButton.setChecked(false);
        paypalRadioButton.setChecked(false);
        paymentType = "3";
        paymentMethod = "1";
        requestChangePaymentMethod();
    }

    @OnClick(R.id.payment_credit_card_linearLayout) void setPayCC2(){
        creditCardRadioButton.setChecked(true);
        virtualAccountRadioButton.setChecked(false);
        bankTransferRadioButton.setChecked(false);
        paypalRadioButton.setChecked(false);
        paymentType = "2";
        paymentMethod = "2";
        requestChangePaymentMethod();
    }

    @OnClick(R.id.payment_bank_transfer_linearLayout) void setPayBT2(){
        bankTransferRadioButton.setChecked(true);
        virtualAccountRadioButton.setChecked(false);
        creditCardRadioButton.setChecked(false);
        paypalRadioButton.setChecked(false);
        paymentType = "1";
        paymentMethod = null;
        requestChangePaymentMethod();
    }

    @OnClick(R.id.paypalRadioButton) void setPayPal(){
        if (paypalRadioButton.isChecked()){
            paypalRadioButton.setChecked(true);
            bankTransferRadioButton.setChecked(false);
            virtualAccountRadioButton.setChecked(false);
            creditCardRadioButton.setChecked(false);
            paymentType = "4";
            paymentMethod = null;
            requestChangePaymentMethod();
        }
    }

    @OnClick(R.id.payment_paypal_linearLayout) void setPayPal2(){
        paypalRadioButton.setChecked(true);
        bankTransferRadioButton.setChecked(false);
        virtualAccountRadioButton.setChecked(false);
        creditCardRadioButton.setChecked(false);
        paymentType = "4";
        paymentMethod = null;
        requestChangePaymentMethod();
    }

    public void requestChangePaymentMethod(){
        pDialog.show();
        try {
            String voucherCode = null;
            if(cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getVoucher() != null) {
                voucherCode = cartReturn.getCart().getVoucher().getCode();
            }

            NYDoShopCheckPaymentMethodRequest req = null;
            if (cartReturn != null && NYHelper.isStringNotEmpty(cartReturn.getCartToken())){
                req = new NYDoShopCheckPaymentMethodRequest(getActivity(), cartReturn.getCartToken(), paymentType, voucherCode);
            }
            spcMgr.execute(req, onChangePaymentMethodRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<DoShopCartReturn> onChangePaymentMethodRequest() {
        return new RequestListener<DoShopCartReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onRequestSuccess(DoShopCartReturn result) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }

                // TODO: refresh order detail
                // 1 = bank 2 = cc 3 = virtual 4 = paypal
                //cartReturn = result;
                // TODO: ambil additionalsnya aja
                cartReturn.setAdditionals(result.getAdditionals());

                if (cartReturn != null && cartReturn.getCart() != null){
                    // TODO: init cart, tes lagi apakah ongkir ilang atau tidak
                    initCartReturn(cartReturn);
                }

            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (pDialog != null) pDialog.cancel();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.setTitle("Checkout");
        if (!spcMgr.isStarted()) spcMgr.start(getActivity());
    }

    public void payUsingVeritrans() {

        SdkUIFlowBuilder.init()
                .setClientKey(getResources().getString(R.string.client_key_development)) // client_key is mandatory
                .setContext(getActivity()) // context is mandatory
                .setTransactionFinishedCallback(thisFragment)// set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(getResources().getString(R.string.api_veritrans_development)) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#0099EE", "#0099EE","#0099EE")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .buildSDK();

        PaymentMethodStorage paymentMethodStorage = new PaymentMethodStorage(getActivity());
        paymentMethodStorage.paymentMethod = paymentMethod;
        paymentMethodStorage.save();

        VeritransStorage veritransStorage = new VeritransStorage(getActivity());
        Contact contact = veritransStorage.contact;
        Order order = veritransStorage.order;
        String token = veritransStorage.veritransToken;
        Cart cart = veritransStorage.cart;
        DiveService service = veritransStorage.service;
        Integer totalParticipants = veritransStorage.totalParticipants;

        if (veritransStorage != null){
            UserDetail userDetail = null;

            if (userDetail == null) {
                userDetail = new UserDetail();
                if (contact != null){
                    userDetail.setUserFullName(contact.getName());
                    userDetail.setEmail(contact.getEmailAddress());
                    userDetail.setPhoneNumber(contact.getPhoneNumber());

                    LoginStorage loginStorage = new LoginStorage(getActivity());
                    userDetail.setUserId(loginStorage.user.getUserId());
                    LocalDataHandler.saveObject("user_details", userDetail);
                }
            }

            UIKitCustomSetting setting = MidtransSDK.getInstance().getUIKitCustomSetting();
            setting.setSkipCustomerDetailsPages(true);
            MidtransSDK.getInstance().setUIKitCustomSetting(setting);

            TransactionRequest transactionRequest;
            if (cart != null){
                transactionRequest = new TransactionRequest(order.getOrderId(), cart.getTotal());
            } else {
                transactionRequest = new TransactionRequest(order.getOrderId(), 0);
            }

            // Create array list and add above item details in it and then set it to transaction request.
            ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
            if (service != null)itemDetailsList.add(new ItemDetails(service.getId(), (int)service.getNormalPrice(), totalParticipants, service.getName()));
            transactionRequest.setItemDetails(itemDetailsList);


            MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
            MidtransSDK.getInstance().startPaymentUiFlow(getActivity(), token);
            //MidtransSDK.getInstance().startPaymentUiFlow(this, "eba5b676-abea-4b6d-8f88-3ad1517f2e2e");
        }

    }



    public void payUsingPaypal() {

        //CONFIGURASI PAYPAL
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(paypalClientId);
        paypalIntent = new Intent(getActivity(), PayPalService.class);
        paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        getActivity().startService(paypalIntent);

        if (orderReturn != null && orderReturn.getPaypalCurrency() != null
                && NYHelper.isStringNotEmpty(orderReturn.getPaypalCurrency().getCurrency())
                && orderReturn.getPaypalCurrency().getAmount() != null
                && NYHelper.isStringNotEmpty(orderReturn.getOrderId())
                && orderReturn.getCart() != null){

            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(orderReturn.getPaypalCurrency().getAmount()), orderReturn.getPaypalCurrency().getCurrency(), "#"+orderReturn.getOrderId(), PayPalPayment.PAYMENT_INTENT_SALE);
//            PayPalItem item = new PayPalItem(diveService.getName(), 1, new BigDecimal(orderReturn.getPaypalCurrency().getAmount()), orderReturn.getPaypalCurrency().getCurrency(), PayPalPayment.PAYMENT_INTENT_SALE);
//            payPalPayment.items(new PayPalItem[]{item});
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, paypalRequestCode);

        } else {
            NYHelper.handlePopupMessage(getActivity(), getString(R.string.warn_paymet_using_paypal_failed), false,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, getResources().getString(R.string.ok));
        }
    }


    @Override
    public void onTransactionFinished(final TransactionResult transactionResult) {
        //TODO DISINI HANDLE KALO TRANSAKSI DI MIDTRANS SUKSES

        if (transactionResult != null){
            if (transactionResult.getResponse() != null) NYLog.e("CEK TRANSACTION 1: "+transactionResult.getResponse().getTransactionStatus());
            if (transactionResult.getResponse() != null && NYHelper.isStringNotEmpty(transactionResult.getResponse().getFraudStatus()))NYLog.e("CEK TRANSACTION 2 : "+transactionResult.getResponse().getFraudStatus());
            if (transactionResult.getResponse() != null && NYHelper.isStringNotEmpty(transactionResult.getResponse().getTransactionStatus()))NYLog.e("CEK TRANSACTION 3 : "+transactionResult.getResponse().getTransactionStatus());
        } else {
        }

        if (transactionResult != null && transactionResult.getResponse() != null && (transactionResult.getResponse().getFraudStatus() != null && transactionResult.getResponse().getFraudStatus().equals(NYHelper.NY_ACCEPT_FRAUD_STATUS) || transactionResult.getResponse().getFraudStatus().equals(NYHelper.TRANSACTION_PENDING)) ){
            NYHelper.handlePopupMessage(getActivity(), getString(R.string.transaction_success), false,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), VeritransNotificationActivity.class);
                            //if (gooLocation != null)intent.putExtra(MainActivity.ARG_ADDRESS, gooLocation.toString());
                            if (transactionResult.getResponse().getFraudStatus().equals(NYHelper.NY_ACCEPT_FRAUD_STATUS)) {
                                if(transactionResult.getResponse().getTransactionStatus().equals(NYHelper.NY_TRANSACTION_STATUS_CAPTURE)) {
                                    NTransactionResult result = new NTransactionResult();
                                    result.setData(transactionResult.getResponse());
                                    intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
                                    intent.putExtra(NYHelper.ID_ORDER_DO_SHOP, transactionResult.getResponse().getOrderId());
                                    intent.putExtra(NYHelper.TRANSACTION_RESPONSE, result.toString());
                                } else if (transactionResult.getResponse().getTransactionStatus().equals(NYHelper.TRANSACTION_PENDING)){
                                    NTransactionResult result = new NTransactionResult();
                                    result.setData(transactionResult.getResponse());
                                    intent.putExtra(NYHelper.TRANSACTION_COMPLETED, false);
                                    intent.putExtra(NYHelper.ID_ORDER_DO_SHOP, transactionResult.getResponse().getOrderId());
                                    intent.putExtra(NYHelper.TRANSACTION_RESPONSE, result.toString());
                                }
                            }
                            intent.putExtra(NYHelper.TAG, "product");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }, "Check Order");
        } else if (transactionResult != null && transactionResult.getResponse() != null && transactionResult.getResponse().getFraudStatus() != null && transactionResult.getResponse().getFraudStatus().equals(NYHelper.NY_CHALLENGE_FRAUD_STATUS)){

            // TODO: jika transaksi gagal
            orderReturn = null;
            isTranssactionFailed = true;
            virtualAccountLinearLayout.setVisibility(View.VISIBLE);
            bankTransferLinearLayout.setVisibility(View.VISIBLE);

        } else {
            //bankTransferLinearLayout.setVisibility(View.GONE);
        }

    }

}
