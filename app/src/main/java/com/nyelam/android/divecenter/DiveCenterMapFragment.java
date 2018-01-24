package com.nyelam.android.divecenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.Location;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;

public class DiveCenterMapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private GoogleMap map;
    private ProgressBar progressBar;
    private TextView noLocationTextView, openMapTextView;
    private SupportMapFragment mapFragment;
    private LinearLayout mapLinearLayout;
    private DiveCenter diveCenter;

    public DiveCenterMapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiveCenterMapFragment newInstance() {
        DiveCenterMapFragment fragment = new DiveCenterMapFragment();
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
        return inflater.inflate(R.layout.fragment_dive_center_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initControl();
    }

    private void initControl() {
        openMapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diveCenter != null){
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                    startActivity(intent);
                }
            }
        });
    }

    private void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        noLocationTextView = (TextView) view.findViewById(R.id.no_location_textView);
        openMapTextView = (TextView) view.findViewById(R.id.open_map_textView);
        mapLinearLayout = (LinearLayout) view.findViewById(R.id.map_linearLayout);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = (int)(width*0.7);
        //params.width = width;
        mapFragment.getView().setLayoutParams(params);

    }


    protected void setContent(DiveCenter diveCenter) {
        this.diveCenter = diveCenter;

        if (diveCenter != null && map != null){
            onMapReady(map);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id  = item.getItemId();

        if (id == android.R.id.home) {
            //your code
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        // Enable MyLocation Button in the Map
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setZoomGesturesEnabled(false);

        // Already two locations
        map.clear();
        // Adding new item to the ArrayList
        if (diveCenter != null){

            LatLng pos2 = new LatLng(0, 0);

            if (diveCenter.getContact() != null && diveCenter.getContact().getLocation() != null && diveCenter.getContact().getLocation().getCoordinate() != null){
                double lat = diveCenter.getContact().getLocation().getCoordinate().getLat();
                double lon = diveCenter.getContact().getLocation().getCoordinate().getLon();
                pos2 = new LatLng(lat,lon);
            }
            //center camera map
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos2, 12.0f));

            // Creating MarkerOptions
            BitmapDescriptor iconPin = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map);

            String name = "";
            String address = "";

            if (NYHelper.isStringNotEmpty(diveCenter.getName())) name = diveCenter.getName();
            if (diveCenter.getContact() != null && diveCenter.getContact().getLocation() != null){
                Location loc = diveCenter.getContact().getLocation();
                if (loc.getCity() != null && NYHelper.isStringNotEmpty(loc.getCity().getName())) address += loc.getCity().getName();
                if (loc.getProvince() != null && NYHelper.isStringNotEmpty(loc.getCity().getName())) address += ", "+loc.getProvince().getName();
                if (NYHelper.isStringNotEmpty(loc.getCountry())) address += ", "+loc.getCountry();
            }

            MarkerOptions optionsClient = new MarkerOptions().position(pos2)
                    .title(name)
                    .snippet(address)
                    .icon(iconPin);



            // Add new marker to the Google Map Android API V2
            Marker marker = map.addMarker(optionsClient);
            marker.showInfoWindow();

            noLocationTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mapLinearLayout.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            noLocationTextView.setVisibility(View.VISIBLE);
            mapLinearLayout.setVisibility(View.VISIBLE);
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
}
