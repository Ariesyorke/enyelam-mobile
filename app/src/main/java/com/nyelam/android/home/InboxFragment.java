package com.nyelam.android.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.InboxList;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.doshop.DoShopAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYCategoryRequest;
import com.nyelam.android.http.NYGetBannerRequest;
import com.nyelam.android.http.NYInboxRequest;
import com.nyelam.android.storage.ModulHomepageStorage;
import com.nyelam.android.view.NYBannerViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class InboxFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private InboxRecyclerViewAdapter inboxAdapter;
    private ArrayList<Object> objects = new ArrayList<>();

    public InboxFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InboxFragment newInstance() {
        InboxFragment fragment = new InboxFragment();
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
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initInbox();
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    private void initInbox(){
        NYInboxRequest req = new NYInboxRequest(getContext());
        spcMgr.execute(req, onCategoryRequest());
    }

    private RequestListener<InboxList> onCategoryRequest() {
        return new RequestListener<InboxList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(getContext(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(InboxList inboxList) {

                progressBar.setVisibility(View.GONE);

                //objects.addAll(inboxList.getInboxData());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                inboxAdapter = new InboxRecyclerViewAdapter(getContext(), inboxList.getInboxData());
                recyclerView.setAdapter(inboxAdapter);
                recyclerView.setVisibility(View.VISIBLE);
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
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadDoTrip();
        //loadBanners();
    }
}
