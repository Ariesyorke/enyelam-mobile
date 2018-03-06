package com.nyelam.android.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYLoginRequest;
import com.nyelam.android.storage.EmailLoginStorage;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class LoginFragment extends Fragment {

    private static int RC_SIGN_IN = 201;

    private ProgressDialog progressDialog;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private OnFragmentInteractionListener mListener;
    private TextView loginTextView, registerTextView, forgotPasswordTextView;
    private EditText emailEditText, passwordEditText;
    private ImageView backgroundImageView;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.background_blur);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        initView(view);
        initControl();
    }

    private void initControl() {
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), "Email can't be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(getActivity(), "Password can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    NYLoginRequest req = new NYLoginRequest(getContext(), email, password);
                    spcMgr.execute(req, onLoginRequest());
                }

            }
        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out);
                transaction.replace(R.id.fragment_container, RegisterFragment.newInstance());
                transaction.addToBackStack(LoginFragment.class.getName());
                transaction.commit();
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, ForgotPasswordFragment.newInstance());
                transaction.addToBackStack(LoginFragment.class.getName());
                transaction.commit();
            }
        });
    }

    private RequestListener<AuthReturn> onLoginRequest() {
        return new RequestListener<AuthReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(AuthReturn authReturn) {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                NYHelper.saveUserData(getActivity(), authReturn);
                if (NYHelper.isStringNotEmpty(emailEditText.getText().toString()))NYHelper.saveEmailUser(getActivity(), emailEditText.getText().toString());
                mListener.isLoginSuccess(true);
            }
        };
    }


    private void initView(View v) {
        emailEditText = (EditText) v.findViewById(R.id.email_editText);
        passwordEditText = (EditText) v.findViewById(R.id.password_editText);
        loginTextView = (TextView) v.findViewById(R.id.login_textView);
        registerTextView = (TextView) v.findViewById(R.id.register_textView);
        forgotPasswordTextView = (TextView) v.findViewById(R.id.forgot_password_textView);
        backgroundImageView = (ImageView) v.findViewById(R.id.background_imageView);
        NYApplication application = (NYApplication)getActivity().getApplication();
        String imageUri = "drawable://"+R.drawable.background_blur;

        if(application.getCache(imageUri) != null) {
            Bitmap bitmap = application.getCache(imageUri);
            backgroundImageView.setImageBitmap(bitmap);
        } else {
            ImageLoader.getInstance().displayImage(imageUri, backgroundImageView, NYHelper.getCompressedOption(getActivity()));
        }

        EmailLoginStorage storage = new EmailLoginStorage(getActivity());
        if (storage != null && NYHelper.isStringNotEmpty(storage.email)) emailEditText.setText(storage.email);

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
        void isLoginSuccess(boolean b);
    }
}
