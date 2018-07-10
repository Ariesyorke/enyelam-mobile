package com.nyelam.android.divecenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveGuide;
import com.nyelam.android.data.Language;
import com.nyelam.android.helper.NYHelper;

public class DiveGuideBioFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private LinearLayout mainLinearLayout;
    private TextView diveGuideCertificationTextView;
    private TextView diveGuideNationalityTextView;
    private TextView diveGuideLanguageTextView;
    private TextView diveGuideAbilitiesTextView;


    public DiveGuideBioFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiveGuideBioFragment newInstance() {
        DiveGuideBioFragment fragment = new DiveGuideBioFragment();
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
        return inflater.inflate(R.layout.fragment_dive_guide_bio, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

        diveGuideCertificationTextView = (TextView) view.findViewById(R.id.dive_guide_certification_textView);
        diveGuideNationalityTextView = (TextView) view.findViewById(R.id.dive_guide_nationality_textView);
        diveGuideLanguageTextView = (TextView) view.findViewById(R.id.dive_guide_language_textView);
        diveGuideAbilitiesTextView = (TextView) view.findViewById(R.id.dive_guide_abilities_textView);

    }


    public void setContent(DiveGuide diveGuide) {

        Toast.makeText(getActivity(), "BIO", Toast.LENGTH_SHORT).show();

        if (diveGuide != null){

            if (diveGuide.getCertificateDiver() != null && NYHelper.isStringNotEmpty(diveGuide.getCertificateDiver().getName()))
                diveGuideCertificationTextView.setText(diveGuide.getCertificateDiver().getName());

//            if (diveGuide.getCertificateDiver() != null && NYHelper.isStringNotEmpty(diveGuide.getCertificateDiver().getName()))
//                diveGuideNationalityTextView.setText(diveGuide.getCertificateDiver().getName());

            String temp = "";
            if (diveGuide.getLanguages() != null && diveGuide.getLanguages().size() > 0)
                for (int i = 0; i < diveGuide.getLanguages().size(); i++){
                    if (0 == i){
                        temp = temp + diveGuide.getLanguages().get(i);
                    } else if (diveGuide.getLanguages().size() == i){
                        temp = temp + ", " +diveGuide.getLanguages().get(i);
                    } else {
                        temp = temp + ", " +diveGuide.getLanguages().get(i);
                    }
                }
            diveGuideLanguageTextView.setText(temp);

//            if (diveGuide.getCertificateDiver() != null && NYHelper.isStringNotEmpty(diveGuide.gEertificateDiver().getaName()))
//                diveGuideAbilitiesTextView.setText(diveGuide.getCertificateDiver().getName());

        }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }
}
