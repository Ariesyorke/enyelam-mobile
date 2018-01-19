package com.nyelam.android.bookinghistory;

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
import com.nyelam.android.data.SummaryList;
import com.nyelam.android.http.NYDoDiveBookingHistoryRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class BookingHistoryCompletedFragment extends Fragment {

    private BookingHistoryInprogressFragment.OnFragmentInteractionListener mListener;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private BookingHistoryListAdapter bookingListAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView noResultTextView;

    public BookingHistoryCompletedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BookingHistoryInprogressFragment newInstance() {
        BookingHistoryInprogressFragment fragment = new BookingHistoryInprogressFragment();
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
        return inflater.inflate(R.layout.fragment_booking_history_completed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAdapter();

        View itemView = (View) view.findViewById(R.id.item_view);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), BookingHistoryDetailActivity.class);
                startActivity(intent);

            }
        });

    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        noResultTextView = (TextView) view.findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        bookingListAdapter = new BookingHistoryListAdapter(getActivity());
        recyclerView.setAdapter(bookingListAdapter);

       /* progressBar.setVisibility(View.VISIBLE);
        NYDoDiveBookingHistoryRequest req = new NYDoDiveBookingHistoryRequest(getActivity(), "1", "1");
        spcMgr.execute(req, onSearchKeywordRequest());*/
    }

    private RequestListener<SummaryList> onSearchKeywordRequest() {
        return new RequestListener<SummaryList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                progressBar.setVisibility(View.GONE);
                bookingListAdapter.clear();
                bookingListAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(SummaryList summaryList) {
                /*if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/
                progressBar.setVisibility(View.GONE);
                noResultTextView.setVisibility(View.GONE);
                bookingListAdapter.clear();
                bookingListAdapter.addResults(summaryList.getList());
                bookingListAdapter.notifyDataSetChanged();
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
        if (context instanceof BookingHistoryInprogressFragment.OnFragmentInteractionListener) {
            mListener = (BookingHistoryInprogressFragment.OnFragmentInteractionListener) context;
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
