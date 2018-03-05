package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveCenterList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchDiveCenterRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class DoDiveResultDiveCenterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchDiveCenterAdapter diveCenterAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView titleTextView, labelTextView, noResultTextView;
    protected String keyword, diverId, diver, certificate, date, type;
    private int page = 1;

    public DoDiveResultDiveCenterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DoDiveResultDiveCenterFragment newInstance() {
        DoDiveResultDiveCenterFragment fragment = new DoDiveResultDiveCenterFragment();
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
        return inflater.inflate(R.layout.fragment_do_dive_result_dive_center, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initExtra();
        initAdapter();
        initRequest();
    }

    private void initRequest() {
        progressBar.setVisibility(View.VISIBLE);

        String url = "";
        if (type.equals("1")){
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_spot);
        } else if (type.equals("2")){
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_category);
        } else if (type.equals("5")){
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_province);
        } else {
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_city);
        }

        NYDoDiveSearchDiveCenterRequest req = new NYDoDiveSearchDiveCenterRequest(getActivity(),
                url, String.valueOf(page), diverId, type, certificate, diver, date, String.valueOf(0), null);
        spcMgr.execute(req, onSearchServiceRequest());
    }

    private void initExtra() {
        Bundle b = getArguments();
        if (b != null) {
            if (NYHelper.isStringNotEmpty(b.getString(NYHelper.KEYWORD))) {
                keyword = b.getString(NYHelper.KEYWORD);
                titleTextView.setText(keyword);
            }
            if (NYHelper.isStringNotEmpty(b.getString(NYHelper.ID_DIVER)))
                diverId = b.getString(NYHelper.ID_DIVER);
            if (NYHelper.isStringNotEmpty(b.getString(NYHelper.DIVER)))
                diver = b.getString(NYHelper.DIVER);
            if (NYHelper.isStringNotEmpty(b.getString(NYHelper.CERTIFICATE)))
                certificate = b.getString(NYHelper.CERTIFICATE);
            if (NYHelper.isStringNotEmpty(b.getString(NYHelper.SCHEDULE)))
                date = b.getString(NYHelper.SCHEDULE);
            if (NYHelper.isStringNotEmpty(b.getString(NYHelper.TYPE))) {
                type = b.getString(NYHelper.TYPE);
            }

            NYLog.e("CEK DATE 2 : "+date);


        }
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels));

        diveCenterAdapter = new DoDiveSearchDiveCenterAdapter((DoDiveSearchResultActivity) getActivity(), diver, date, certificate, type, diverId);
        recyclerView.setAdapter(diveCenterAdapter);
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        labelTextView = (TextView) v.findViewById(R.id.label_textView);
        titleTextView = (TextView) v.findViewById(R.id.title_textView);
        noResultTextView = (TextView) v.findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
    }

    private RequestListener<DiveCenterList> onSearchServiceRequest() {
        return new RequestListener<DiveCenterList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                diveCenterAdapter.clear();
                diveCenterAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DiveCenterList results) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                if (results != null){
                    noResultTextView.setVisibility(View.GONE);
                    diveCenterAdapter.clear();
                    diveCenterAdapter.addResults(results.getList());
                    diveCenterAdapter.notifyDataSetChanged();
                } else {
                    diveCenterAdapter.clear();
                    diveCenterAdapter.notifyDataSetChanged();
                    noResultTextView.setVisibility(View.VISIBLE);
                }

            }
        };
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
