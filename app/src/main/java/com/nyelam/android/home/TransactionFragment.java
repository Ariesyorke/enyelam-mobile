package com.nyelam.android.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nyelam.android.R;
import com.nyelam.android.bookinghistory.BookingHistoryCompletedFragment;
import com.nyelam.android.bookinghistory.BookingHistoryInprogressFragment;
import com.nyelam.android.storage.LoginStorage;

public class TransactionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public TransactionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance() {
        TransactionFragment fragment = new TransactionFragment();
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
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BookingHistoryInprogressFragment();
                case 1:
                default:
                    return new BookingHistoryCompletedFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.upcoming);
                case 1:
                default:
                    return getResources().getString(R.string.past);
            }
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
        void onHome();
    }


    @Override
    public void onResume() {
        super.onResume();
        LoginStorage loginStorage = new LoginStorage(getActivity());
        if (loginStorage != null && !loginStorage.isUserLogin()){
            //mListener.onHome();
        }
    }

}
