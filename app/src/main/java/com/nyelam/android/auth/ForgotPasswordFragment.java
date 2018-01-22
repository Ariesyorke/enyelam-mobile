package com.nyelam.android.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYForgotPasswordRequest;
import com.nyelam.android.storage.ForgotPasswordStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ForgotPasswordFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ProgressDialog pDialog;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private TextView resetTextView;
    private EditText emailEditText;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initControl();
    }

    private void initControl() {
        resetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (!NYHelper.isStringNotEmpty(email)){
                    Toast.makeText(getActivity(), getString(R.string.warn_field_email_cannot_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!NYHelper.isValidEmaillId(email)){
                    Toast.makeText(getActivity(), getString(R.string.warn_email_not_valid), Toast.LENGTH_SHORT).show();
                } else {
                    pDialog.show();
                    NYForgotPasswordRequest req = new NYForgotPasswordRequest(this.getClass(), getContext(), email);
                    spcMgr.execute(req, onResetPasswordRequest(email));
                }
            }
        });
    }

    private RequestListener<Boolean> onResetPasswordRequest(final String email) {
        return new RequestListener<Boolean>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (pDialog != null)pDialog.dismiss();
                if (spiceException != null) {
                    NYHelper.handleAPIException(getActivity(), spiceException, null);
                } else {
                    NYHelper.handleErrorMessage(getActivity(), getActivity().getResources().getString(R.string.warn_no_connection));
                }
            }

            @Override
            public void onRequestSuccess(Boolean aBoolean) {
                if (pDialog != null)pDialog.dismiss();
                /*Intent intent = new Intent(getActivity(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/

                ForgotPasswordStorage storage = new ForgotPasswordStorage(getActivity());
                storage.setEmail(email);
                storage.setForgot(true);
                storage.save();
                NYHelper.handlePopupMessage(getActivity(), getResources().getString(R.string.forgot_password_message), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().onBackPressed();
                    }
                });

            }
        };
    }

    private void initView(View view) {
        emailEditText = (EditText) view.findViewById(R.id.email_editText);
        resetTextView = (TextView) view.findViewById(R.id.reset_textView);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
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
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }
}
