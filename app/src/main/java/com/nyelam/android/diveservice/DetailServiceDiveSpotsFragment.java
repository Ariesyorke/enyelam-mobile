package com.nyelam.android.diveservice;

import android.content.Context;
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

public class DetailServiceDiveSpotsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private TextView noResultTextView;
    private RecyclerView recyclerView;
    private DiveSpotInDetailAdapter adapter;

    public DetailServiceDiveSpotsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailServiceDiveSpotsFragment newInstance() {
        DetailServiceDiveSpotsFragment fragment = new DetailServiceDiveSpotsFragment();
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
        return inflater.inflate(R.layout.fragment_detail_service_dive_spots, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        DetailServiceActivity activity = ((DetailServiceActivity)getActivity());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiveSpotInDetailAdapter(getActivity(), activity.diver, activity.certificate, activity.schedule);
        recyclerView.setAdapter(adapter);
        //setDiveSpot();
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        noResultTextView = (TextView) view.findViewById(R.id.no_result_textView);
    }

    public void setDiveSpot(){
        progressBar.setVisibility(View.GONE);
        if (((DetailServiceActivity)getActivity()).newDiveService != null && ((DetailServiceActivity)getActivity()).newDiveService .getDiveSpots() != null && ((DetailServiceActivity)getActivity()).newDiveService.getDiveSpots().size() > 0){
            adapter.clear();
            adapter.addResults(((DetailServiceActivity)getActivity()).newDiveService.getDiveSpots());
            adapter.notifyDataSetChanged();
            noResultTextView.setVisibility(View.GONE);
        } else {
            noResultTextView.setVisibility(View.VISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();
    }

}
