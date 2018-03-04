package com.nyelam.android.divecenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.divespot.DiveSpotDetailActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchServiceRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class DiveCenterListServiceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private int page = 1;
    private DiveCenter diveCenter;
    private List<DiveService> serviceList;
    private ProgressBar progressBar;
    private TextView noResultTextView;
    private RecyclerView recyclerView;
    private DoDiveSearchServiceAdapter adapter;

    public DiveCenterListServiceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiveCenterListServiceFragment newInstance() {
        DiveCenterListServiceFragment fragment = new DiveCenterListServiceFragment();
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
        return inflater.inflate(R.layout.fragment_dive_center_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels));

        if (getActivity() instanceof  DiveCenterDetailActivity){

            DiveCenterDetailActivity activity = (DiveCenterDetailActivity)getActivity();
            adapter = new DoDiveSearchServiceAdapter(getActivity(), activity.diver, activity.schedule, activity.certificate, activity.diveCenter);

        } else if (getActivity() instanceof DiveSpotDetailActivity){

            DiveSpotDetailActivity activity = (DiveSpotDetailActivity)getActivity();
            adapter = new DoDiveSearchServiceAdapter(getActivity(), activity.getDiver(), activity.getSchedule(), activity.getCerificate(), null);
        }

        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        noResultTextView = (TextView) view.findViewById(R.id.no_result_textView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    protected void setContent(DiveCenter diveCenter) {
        this.diveCenter = diveCenter;

        if (serviceList == null) serviceList = new ArrayList<>();

        /*if (((DiveCenterDetailActivity)getActivity()).diveCenter == null) {
            diveCenter = new DiveCenter();
            diveCenter = ((DiveCenterDetailActivity)getActivity()).diveCenter;
        } else {
            //diveCenter.setId("1");
            diveCenter = new DiveCenter();
            diveCenter = ((DiveCenterDetailActivity)getActivity()).diveCenter;
        }*/

        DiveCenterDetailActivity activity = (DiveCenterDetailActivity)getActivity();
        if (diveCenter != null && !TextUtils.isEmpty(diveCenter.getId())
                && !TextUtils.isEmpty(activity.diver)
                && !TextUtils.isEmpty(activity.certificate)){

            String apiPath = getString(R.string.api_path_dodive_service_list);
            if (NYHelper.isStringNotEmpty(activity.type) && activity.type.equals("1")){
                apiPath = getString(R.string.api_path_dodive_service_list_by_divespot);
            } else if (NYHelper.isStringNotEmpty(activity.type) && activity.type.equals("2")){
                apiPath = getString(R.string.api_path_dodive_service_list_by_category);
            }

            NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(getActivity(), apiPath, String.valueOf(page), diveCenter.getId(), activity.certificate, activity.diver, activity.schedule, activity.type, activity.diverId);
            //NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(getActivity(), getString(R.string.api_path_dodive_search_by_dive_center), String.valueOf(page), diveCenter.getId(), "type", "certificate", "diver", "date");
            spcMgr.execute(req, onGetServiceByDiveCenterRequest());
        }
    }

    private RequestListener<DiveServiceList> onGetServiceByDiveCenterRequest() {
        return new RequestListener<DiveServiceList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null)progressBar.setVisibility(View.GONE);
                //NYHelper.handleAPIException(.this, spiceException, null);
                noResultTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {

                //NYLog.e("WHAT ? "+results.getList().toString());

                if (progressBar != null)progressBar.setVisibility(View.GONE);

                if (serviceList == null) serviceList = new ArrayList<>();
                if (results != null && results.getList().size() > 0){
                    serviceList = results.getList();
                    adapter.addResults(serviceList);
                    adapter.notifyDataSetChanged();
                    noResultTextView.setVisibility(View.GONE);
                } else {
                    noResultTextView.setVisibility(View.VISIBLE);
                }

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
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        spcMgr.start(getActivity());
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }
}
