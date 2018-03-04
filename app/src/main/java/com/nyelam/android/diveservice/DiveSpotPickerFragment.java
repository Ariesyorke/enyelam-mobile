package com.nyelam.android.diveservice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveSpot;

import java.util.List;

public class DiveSpotPickerFragment extends DialogFragment {
    private List<DiveSpot> diveSpots;

    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private DiveSpotPickerAdapter adapter;
    private Button chooseButton;
    private DiveSpot diveSpot;

    public static DiveSpotPickerFragment newInstance() {
        DiveSpotPickerFragment fragment = new DiveSpotPickerFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dive_spot_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        listView = (ListView)view.findViewById(R.id.list_view);
        chooseButton = (Button)view.findViewById(R.id.choose_button);
        initData();
    }

    private void initData() {
        DetailServiceActivity activity = (DetailServiceActivity)getActivity();
        diveSpots = activity.getDiveService().getDiveSpots();
        diveSpot = diveSpots.get(0);
        adapter = new DiveSpotPickerAdapter(getActivity(), this.diveSpots);
        listView.setAdapter(adapter);
        initListener();
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                diveSpot = diveSpots.get(i);
                adapter.setSelection(i);
            }
        });
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDiveSpotChoosen(diveSpot.getId());
                dismiss();
            }
        });
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
        void onDiveSpotChoosen(String diveSpotId);
    }
}
