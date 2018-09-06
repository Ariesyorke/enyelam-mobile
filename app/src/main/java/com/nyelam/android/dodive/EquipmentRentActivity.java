package com.nyelam.android.dodive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.EquipmentRent;
import com.nyelam.android.data.EquipmentRentAdded;
import com.nyelam.android.data.EquipmentRentAddedList;
import com.nyelam.android.data.EquipmentRentList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.diveservice.DetailServiceDiveCenterFragment;
import com.nyelam.android.diveservice.DetailServiceDiveSpotsFragment;
import com.nyelam.android.diveservice.DetailServiceFragment;
import com.nyelam.android.diveservice.DetailServiceReviewFragment;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveServiceCartRequest;
import com.nyelam.android.http.NYEquipmentRentListRequest;
import com.nyelam.android.http.NYServiceOutOfStockException;
import com.nyelam.android.view.font.NYStrikethroughTextView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EquipmentRentActivity extends AppCompatActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private List<EquipmentRent> equipmentRents;
    private List<EquipmentRentAdded> equipmentsRentTempList;
    private TextView applyTextView, clearTextView, notFoundTextView;
    private ImageView closeImageView;
    private LinearLayout containerLinearLayout, equipmentRentLinearLayout;
    private boolean isApply;
    private ProgressBar progressBar;
    private DiveCenter diveCenter;
    private String schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_rent);
        initView();
        initControl();
        initExtras();
        getEquipmentList();
        //initEquipemntRent();
    }

    private void initExtras() {

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (intent.hasExtra(NYHelper.EQUIPMENT_RENT)){
            EquipmentRentAddedList equipTemp = null;

            try {
                JSONArray arrayCat = new JSONArray(b.getString(NYHelper.EQUIPMENT_RENT));
                equipTemp = new EquipmentRentAddedList();
                equipTemp.parse(arrayCat);
                equipmentsRentTempList = equipTemp.getList();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            NYLog.e("equip extras parse NULL");
        }


        if (intent.hasExtra(NYHelper.DIVE_CENTER)) {
            try {
                JSONObject obj = new JSONObject(intent.getStringExtra(NYHelper.DIVE_CENTER));
                diveCenter = new DiveCenter();
                diveCenter.parse(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(intent.getStringExtra(NYHelper.SCHEDULE))){
            schedule = intent.getStringExtra(NYHelper.SCHEDULE);
        }

    }

    public void initEquipemntRent(boolean isClear){

        if (equipmentsRentTempList == null)equipmentsRentTempList = new ArrayList<>();

        if (equipmentRents == null) equipmentRents = new ArrayList<>();

        /*try {
            JSONArray array = new JSONArray(loadJSONFromAsset(this));
            EquipmentRentList results = new EquipmentRentList();
            results.parse(array);

            if (results != null && results.getList() != null){
                equipmentRents = results.getList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        containerLinearLayout.removeAllViews();
        int pos = 0;
        for (final EquipmentRent equipmentRent : equipmentRents) {

            final int position = pos;

            LayoutInflater linflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myParticipantsView = linflaterAddons.inflate(R.layout.view_item_equipment_rent_add, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(EquipmentRentActivity.this, 10));
            myParticipantsView.setLayoutParams(layoutParamsAddons);

            TextView nameTextView = (TextView) myParticipantsView.findViewById(R.id.name_textView);
            TextView priceTextView = (TextView) myParticipantsView.findViewById(R.id.price_textView);
            NYStrikethroughTextView priceStrikeThroughTextView = (NYStrikethroughTextView) myParticipantsView.findViewById(R.id.price_strikethrough_textView);
            TextView stockTextView = (TextView) myParticipantsView.findViewById(R.id.stock_textView);
            final TextView countTextView = (TextView) myParticipantsView.findViewById(R.id.count_textView);
            ImageView plusImageView = (ImageView) myParticipantsView.findViewById(R.id.plus_imageView);
            ImageView minusImageView = (ImageView) myParticipantsView.findViewById(R.id.minus_imageView);

            if (equipmentRent != null){

                if (NYHelper.isStringNotEmpty(equipmentRent.getName())) {
                    nameTextView.setText(equipmentRent.getName());
                }

                stockTextView.setText("stock : "+String.valueOf(equipmentRent.getAvailabilityStock()));

                if (equipmentRent.getSpecialPrice() < equipmentRent.getNormalPrice() && equipmentRent.getSpecialPrice() > 0){
                    priceTextView.setText("@ " + NYHelper.priceFormatter(equipmentRent.getSpecialPrice()));
                    priceStrikeThroughTextView.setText("@ " + NYHelper.priceFormatter(equipmentRent.getNormalPrice()));
                    priceStrikeThroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText("@ " + NYHelper.priceFormatter(equipmentRent.getNormalPrice()));
                    priceStrikeThroughTextView.setVisibility(View.GONE);
                }

                final int[] total = {0};

                for (EquipmentRentAdded add : equipmentsRentTempList){
                    if (isClear){
                        total[0] = 0;
                    } else if (add.getId().equals(equipmentRent.getId())){
                        total[0] = add.getQuantity();
                        break;
                    }
                }

                countTextView.setText(String.valueOf(total[0]));

                plusImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((total[0]+1) <= equipmentRent.getAvailabilityStock()){
                            total[0]++;
                            countTextView.setText(String.valueOf(total[0]));
                            addEquipment(equipmentRent, total[0]);
                            //equipmentsRentTempList.put(equipmentRent.getId(), total[0]);
                        }
                    }
                });

                minusImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((total[0] - 1) >= 0){
                            total[0]--;
                            countTextView.setText(String.valueOf(total[0]));
                            addEquipment(equipmentRent, total[0]);
                        }
                    }
                });

                pos++;
                containerLinearLayout.addView(myParticipantsView);
            }

        }
    }

    private void initControl() {

        applyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isApply = true;
                onBackPressed();
            }
        });

        clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(EquipmentRentActivity.this, "clear", Toast.LENGTH_SHORT).show();
                equipmentsRentTempList = new ArrayList<EquipmentRentAdded>();
                initEquipemntRent(true);
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isApply = false;
                onBackPressed();
            }
        });

    }

    private void initView() {
        notFoundTextView = (TextView) findViewById(R.id.not_found_textView);
        clearTextView = (TextView) findViewById(R.id.clear_textView);
        applyTextView = (TextView) findViewById(R.id.apply_textView);
        closeImageView = (ImageView) findViewById(R.id.close_imageView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        containerLinearLayout = (LinearLayout) findViewById(R.id.equipemnt_rent_container_linearLayout);
        equipmentRentLinearLayout = (LinearLayout) findViewById(R.id.equipment_rent_linearLayout);
    }


    @Override
    public void onBackPressed() {
        if (isApply){
            Intent intent = new Intent();
            if (equipmentsRentTempList != null){
                intent.putExtra(NYHelper.EQUIPMENT_RENT, equipmentsRentTempList.toString());
            }
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    private void getEquipmentList() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            NYEquipmentRentListRequest req = new NYEquipmentRentListRequest(this, diveCenter.getId(), schedule);
            spcMgr.execute(req, onGetEquipmentRentRequest());
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private RequestListener<EquipmentRentList> onGetEquipmentRentRequest() {
        return new RequestListener<EquipmentRentList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                progressBar.setVisibility(View.GONE);
                equipmentRentLinearLayout.setVisibility(View.GONE);
                notFoundTextView.setVisibility(View.VISIBLE);
                //Toast.makeText(EquipmentRentActivity.this, "nggak ada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(EquipmentRentList results) {
                progressBar.setVisibility(View.GONE);
                if (results != null) {
                    equipmentRents = results.getList();
                    initEquipemntRent(false);
                    equipmentRentLinearLayout.setVisibility(View.VISIBLE);
                    notFoundTextView.setVisibility(View.GONE);
                    //Toast.makeText(EquipmentRentActivity.this, "ada 1", Toast.LENGTH_SHORT).show();
                } else {
                    equipmentRentLinearLayout.setVisibility(View.GONE);
                    notFoundTextView.setVisibility(View.VISIBLE);
                }

                //Toast.makeText(EquipmentRentActivity.this, "ada 2", Toast.LENGTH_SHORT).show();

            }
        };
    }

    public String loadJSONFromAsset( Context context ) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("list_equipment.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }




    public void addEquipment(EquipmentRent equipmentRent, int quantity){

        //NYLog.e("cek equipment add 1 : "+equipmentRentId +" - "+ quantity);

        if (equipmentRent != null && NYHelper.isStringNotEmpty(equipmentRent.getId()) ){


            boolean isExist = false;

            if (equipmentsRentTempList == null) equipmentsRentTempList = new ArrayList<>();

            for (EquipmentRentAdded rentAdded : equipmentsRentTempList){
                if (rentAdded != null && NYHelper.isStringNotEmpty(rentAdded.getId()) && equipmentRent.getId().equals(rentAdded.getId())){

                    EquipmentRentAdded newAdd = rentAdded;

                    NYLog.e("cek equipment add 2 : exist");

                    equipmentsRentTempList.remove(rentAdded);

                    if (quantity > 0){
                        NYLog.e("cek equipment add 3 ");
                        newAdd.setQuantity(quantity);
                        newAdd.setEquipmentRent(equipmentRent);
                        equipmentsRentTempList.add(newAdd);
                    } else {
                        NYLog.e("cek equipment add 4 ");
                    }

                    isExist = true;
                    break;
                }
            }

            if (!isExist){
                NYLog.e("cek equipment add 4 : not exist");
                EquipmentRentAdded rentAdded = new EquipmentRentAdded();
                rentAdded.setId(equipmentRent.getId());
                rentAdded.setEquipmentRent(equipmentRent);
                rentAdded.setQuantity(quantity);
                equipmentsRentTempList.add(rentAdded);
            }

            NYLog.e("cek equipment data : "+equipmentsRentTempList.toString());

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }


}
