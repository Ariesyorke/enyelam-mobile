package com.nyelam.android.profile;

import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nyelam.android.R;
import com.nyelam.android.dodive.DoDiveFragment;
import com.nyelam.android.home.MyAccountFragment;

public class ProfileActivity extends AppCompatActivity implements MyAccountFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initFragment();
    }

    private void initFragment() {
        /*DoDiveFragment fragmentDoDive = new DoDiveFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragmentDoDive);
        fragmentTransaction.addToBackStack(fragmentDoDive.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();*/

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MyAccountFragment fragment = new MyAccountFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
