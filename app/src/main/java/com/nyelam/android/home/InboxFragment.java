package com.nyelam.android.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.InboxData;
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
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class InboxFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private InboxRecyclerViewAdapter inboxAdapter;
    private List<InboxData> inboxDataList = new ArrayList<InboxData>();
    private boolean triggered = false;

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
        initControl();
        initInbox(1);
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    private void initInbox(int page){
        try {
            NYInboxRequest req = new NYInboxRequest(getContext(), page);
            spcMgr.execute(req, onCategoryRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initNext(int page){
        if(triggered){
            try {
                triggered = false;
                NYInboxRequest req = new NYInboxRequest(getContext(), page);
                spcMgr.execute(req, onCategoryRequestNext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initControl() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        inboxAdapter = new InboxRecyclerViewAdapter(recyclerView, getContext());
        recyclerView.setAdapter(inboxAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // do something
                inboxDataList.clear();
                inboxAdapter.clear();
                inboxAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                initInbox(1);

                // after refresh is done, remember to call the following code
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                }
            }
        });
    }

    private RequestListener<InboxList> onCategoryRequest() {
        return new RequestListener<InboxList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(getContext(), spiceException, null);

                progressBar.setVisibility(View.GONE);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                    inboxDataList.clear();
                    inboxAdapter.clear();
                    inboxAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRequestSuccess(final InboxList inboxList) {

                progressBar.setVisibility(View.GONE);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

                //objects.addAll(inboxList.getInboxData());
                //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                if(inboxList != null && inboxList.getInboxData() != null){

                    inboxDataList.clear();

                    List<InboxData> inboxListResult = inboxList.getInboxData();
                    for(int i = 0; i < inboxListResult.size(); i++){
                        boolean duplicateItem = false;
                        InboxData inboxData = inboxListResult.get(i);
                        if(inboxDataList.size() != 0){
                            for(int j =0; j < inboxDataList.size(); j++){
                                if(inboxDataList.get(j).getTicketId() == inboxData.getTicketId()){
                                    duplicateItem = true;
                                }
                            }
                            if(!duplicateItem){
                                inboxDataList.add(inboxData);
                            }
                        }
                    }

                    inboxDataList.addAll(inboxList.getInboxData());

                    inboxAdapter.clear();
                    inboxAdapter.addResults(inboxDataList);
                    inboxAdapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);

                    //set load more listener for the RecyclerView adapter
                    inboxAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if (inboxList.getNext() != null) {
                                inboxAdapter.addScroll();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // after refresh is done, remember to call the following code
                                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                                            swipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                                        }
                                        inboxAdapter.removeScroll();
                                        triggered = true;
                                        initNext(Integer.parseInt(inboxList.getNext()));
                                    }
                                }, 2000);
                            } else {
//                                Toast.makeText(getActivity(), "Loading data completed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{

                }
            }
        };
    }

    private RequestListener<InboxList> onCategoryRequestNext() {
        return new RequestListener<InboxList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(getContext(), spiceException, null);

                progressBar.setVisibility(View.GONE);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                    inboxDataList.clear();
                    inboxAdapter.clear();
                    inboxAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onRequestSuccess(final InboxList inboxList) {

                progressBar.setVisibility(View.GONE);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

                if(inboxList != null && inboxList.getInboxData() != null){
                    List<InboxData> inboxListResult = inboxList.getInboxData();
                    for(int i = 0; i < inboxListResult.size(); i++){
                        boolean duplicateItem = false;
                        InboxData inboxData = inboxListResult.get(i);
                        if(inboxDataList.size() != 0){
                            for(int j =0; j < inboxDataList.size(); j++){
                                if(inboxDataList.get(j).getTicketId() == inboxData.getTicketId()){
                                    duplicateItem = true;
                                }
                            }
                            if(!duplicateItem){
                                inboxDataList.add(inboxData);
                            }
                        }
                    }
                    inboxAdapter.clear();
                    inboxAdapter.addResults(inboxDataList);
                    inboxAdapter.notifyDataSetChanged();
                    inboxAdapter.setLoaded();
                    //set load more listener for the RecyclerView adapter
                    inboxAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if(inboxDataList != null && inboxDataList.size() != 0){
                                if (inboxList.getNext() != null) {
                                    inboxAdapter.addScroll();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // after refresh is done, remember to call the following code
                                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                                                swipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                                            }
                                            inboxAdapter.removeScroll();
                                            triggered = true;
                                            initNext(Integer.parseInt(inboxList.getNext()));
                                        }
                                    }, 2000);
                                } else {
                                    Toast.makeText(getActivity(), "Loading data completed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Loading data completed", Toast.LENGTH_SHORT).show();
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
        //refresh list Inbox
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        initInbox(1);
    }
}
