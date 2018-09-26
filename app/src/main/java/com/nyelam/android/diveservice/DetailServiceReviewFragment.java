package com.nyelam.android.diveservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.dodive.DoDiveSearchActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYReviewListRequest;
import com.nyelam.android.data.ReviewList;
import com.nyelam.android.inbox.NewMessageActivity;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DetailServiceReviewFragment extends Fragment {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ReviewsAdapter reviewsAdapter;
    private TextView noResultTextView;
    private DetailServiceActivity activity;
    private String ServiceId;

    public DetailServiceReviewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailServiceReviewFragment newInstance() {
        DetailServiceReviewFragment fragment = new DetailServiceReviewFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (DetailServiceActivity)getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        noResultTextView = (TextView) view.findViewById(R.id.no_result_textView);
        initAdapter();
        initExtra();
    }

    private void initExtra() {
        if (activity.getDiveService() != null){
            DiveService diveService = activity.getDiveService();
            ServiceId = diveService.getId().toString();
        }
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding_half);
        int spacing2InPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(spacing2InPixels,0,spacingInPixels,0));

        reviewsAdapter = new ReviewsAdapter(getActivity());
        recyclerView.setAdapter(reviewsAdapter);
    }

    private void getReviewList(){
        NYReviewListRequest req = null;
        try {
            req = new NYReviewListRequest(getActivity(), ServiceId, "1");
            spcMgr.execute(req, onGetReviewList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestListener<ReviewList> onGetReviewList() {
        return new RequestListener<ReviewList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                reviewsAdapter.clear();
                reviewsAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
                //relatedPostLinearLayout.setVisibility(View.GONE);
                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(ReviewList results) {
                if (results != null && results.getReviewList() != null && results.getReviewList().size() > 0){
                    //relatedPostLinearLayout.setVisibility(View.VISIBLE);
                    reviewsAdapter.clear();
                    reviewsAdapter.addResults(results.getReviewList());
                    reviewsAdapter.notifyDataSetChanged();
                    noResultTextView.setVisibility(View.GONE);
                } else {
                    reviewsAdapter.clear();
                    reviewsAdapter.notifyDataSetChanged();
                    noResultTextView.setVisibility(View.VISIBLE);
                    //relatedPostLinearLayout.setVisibility(View.GONE);
                }
            }
        };
    }


    /*private void loadDummyReviews(){
        JSONArray array = null;
        try {
            array = new JSONArray(NYHelper.getJSONFromResource(getActivity(), "list_review.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ReviewList results = new ReviewList();
        try {
            results.parse(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (results != null && results.getReviewList() != null && results.getReviewList().size() > 0){
            noResultTextView.setVisibility(View.GONE);
            reviewsAdapter.clear();
            reviewsAdapter.addResults(results.getReviewList());
            reviewsAdapter.notifyDataSetChanged();
            noResultTextView.setVisibility(View.GONE);
        } else {
            reviewsAdapter.clear();
            reviewsAdapter.notifyDataSetChanged();
            noResultTextView.setVisibility(View.VISIBLE);
        }

    }*/

    public void setReviewList(){
        getReviewList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_service_review, container, false);
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
        spcMgr.start(getContext());
        getReviewList();
        //loadDummyReviews();
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
