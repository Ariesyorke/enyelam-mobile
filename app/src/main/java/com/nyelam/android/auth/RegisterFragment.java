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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYRegisterRequest;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private EditText usernameEditText, emailEditText, phoneNumberEditText, passwordEditText, confirmPasswordEditText;
    private Spinner genderSpinner;
    private TextView registerTextView;

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
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String gender = (String) genderSpinner.getSelectedItem();

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(getActivity(), "Username can't be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), "Email address can't be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(getActivity(), "Phone number can't be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(getActivity(), "Password can't be empty", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6){
                    Toast.makeText(getActivity(), "Password length must be 6 characters or more", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(getActivity(), "Confirm password can't be empty", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)){
                    Toast.makeText(getActivity(), "Confirm password is wrong", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(gender)){
                    Toast.makeText(getActivity(), "Gender can't be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(gender)|| gender.equals("Select Gender")){
                    Toast.makeText(getActivity(), "Please, select your gender", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    NYRegisterRequest req = new NYRegisterRequest(getActivity(), username, email, phoneNumber, password, confirmPassword, gender,  null, null, null, null);
                    spcMgr.execute(req, onRegisterRequest());
                }
            }
        });
    }

    private void initView(View v) {
        usernameEditText = (EditText) v.findViewById(R.id.username_editText);
        emailEditText = (EditText) v.findViewById(R.id.email_editText);
        phoneNumberEditText = (EditText) v.findViewById(R.id.phone_number_editText);
        passwordEditText = (EditText) v.findViewById(R.id.password_editText);
        confirmPasswordEditText = (EditText) v.findViewById(R.id.confirm_password_editText);
        genderSpinner = (Spinner) v.findViewById(R.id.gender_spinner);
        registerTextView = (TextView) v.findViewById(R.id.register_textView);

        List<String> stringList = new ArrayList<>();
        stringList.add(getString(R.string.select_gender));
        stringList.add("Male");
        stringList.add("Female");
        ArrayAdapter<String> myAdapter = new GenderAdapter(getActivity(), R.layout.spinner_gender, stringList);
        genderSpinner.setAdapter(myAdapter);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
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
    }

    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()){
            spcMgr.shouldStop();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void isLoginSuccess(boolean login);
    }
}
