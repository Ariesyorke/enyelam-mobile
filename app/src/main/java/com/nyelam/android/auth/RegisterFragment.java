package com.nyelam.android.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCountryCode;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.general.CountryCodeAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYRegisterRequest;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;
    protected Spinner countryCodeSpinner;
    private CountryCodeAdapter countryCodeAdapter;
    private EditText emailEditText, phoneNumberEditText, passwordEditText, confirmPasswordEditText;
    private TextView registerTextView, loginTextView, plusTextView;

    public RegisterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initControl();
    }

    private void initControl() {
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                CountryCode countryCode = (CountryCode) countryCodeSpinner.getSelectedItem();
                //CountryCode countryCode = (CountryCode) countryCodeAdapter.getItem(countryCodeSpinner.getSelectedItemPosition()) ;
                String countryCodeId = "360";
                if (countryCode != null)  countryCodeId = countryCode.getId();

                if (NYHelper.isStringNotEmpty(phoneNumber) && phoneNumber.charAt(0) == '0'){
                    phoneNumber = phoneNumber.substring(1);
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isValidEmaillId(email)){
                    Toast.makeText(getActivity(), getString(R.string.warn_email_not_valid), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(countryCodeId)){
                    Toast.makeText(getActivity(), getString(R.string.warn_email_not_valid), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(getActivity(), getString(R.string.warn_field_phone_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(getActivity(), getString(R.string.warn_field_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6){
                    Toast.makeText(getActivity(), getString(R.string.warn_field_password_length), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(getActivity(), getString(R.string.warn_field_confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)){
                    Toast.makeText(getActivity(), getString(R.string.warn_field_confirm_password_didnt_match), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    NYRegisterRequest req = new NYRegisterRequest(getActivity(), null, email, phoneNumber,countryCodeId, password, confirmPassword, null,  null, null, null, null);
                    spcMgr.execute(req, onRegisterRequest());
                }
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initView(View v) {
        emailEditText = (EditText) v.findViewById(R.id.email_editText);
        plusTextView = (TextView) v.findViewById(R.id.plus_textView);
        phoneNumberEditText = (EditText) v.findViewById(R.id.phone_number_editText);
        passwordEditText = (EditText) v.findViewById(R.id.password_editText);
        confirmPasswordEditText = (EditText) v.findViewById(R.id.confirm_password_editText);
        registerTextView = (TextView) v.findViewById(R.id.register_textView);
        loginTextView = (TextView) v.findViewById(R.id.login_textView);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        countryCodeAdapter = new CountryCodeAdapter(getActivity());

        DaoSession session = ((NYApplication) getActivity().getApplicationContext()).getDaoSession();
        List<NYCountryCode> rawProducts = session.getNYCountryCodeDao().queryBuilder().list();
        List<CountryCode> countryCodes = NYHelper.generateList(rawProducts, CountryCode.class);

        countryCodeSpinner = (Spinner)v.findViewById(R.id.country_code_spinner);
        countryCodeSpinner.setAdapter(countryCodeAdapter);
        countryCodeSpinner.setOnItemSelectedListener(this);

        if (countryCodes != null && countryCodes.size() > 0){
            NYLog.e("tes isi DAO Country Code : "+countryCodes.toString());
            countryCodeAdapter.addCountryCodes(countryCodes);
            countryCodeAdapter.notifyDataSetChanged();

            int pos = 0;
            for (CountryCode countryCode : countryCodes){
                if (countryCode != null && NYHelper.isStringNotEmpty(countryCode.getCountryName()) && countryCode.getCountryName().toLowerCase().equals("indonesia")){
                    countryCodeSpinner.setSelection(pos);
                    countryCodeAdapter.setSelectedPosition(pos);
                    countryCodeAdapter.notifyDataSetChanged();
                    break;
                }
                pos++;
            }
        }



    }


    private RequestListener<AuthReturn> onRegisterRequest() {
        return new RequestListener<AuthReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null && progressDialog.isShowing())progressDialog.dismiss();
                if (spiceException != null) {
                    NYHelper.handleAPIException(getActivity(), spiceException, null);
                } else {
                    NYHelper.handleErrorMessage(getActivity(), getActivity().getResources().getString(R.string.warn_no_connection));
                }
            }
            @Override
            public void onRequestSuccess(AuthReturn authReturn) {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                NYHelper.saveUserData(getActivity(), authReturn);
                mListener.isLoginSuccess(true);
            }
        };
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        spcMgr.start(getActivity());

        emailEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()){
            spcMgr.shouldStop();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        countryCodeAdapter.setSelectedPosition(position);
        countryCodeAdapter.notifyDataSetChanged();
        plusTextView.setText("+"+((CountryCode)countryCodeAdapter.getItem(position)).getCountryNumber());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void isLoginSuccess(boolean login);
    }
}
