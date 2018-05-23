package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.EquipmentRent;
import com.nyelam.android.data.EquipmentRentAdded;
import com.nyelam.android.data.EquipmentRentAddedList;
import com.nyelam.android.data.EquipmentRentList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EquipmentRentActivity extends AppCompatActivity {

    private List<EquipmentRent> equipmentRents;
    private List<EquipmentRentAdded> equipmentsRentTempList;
    private TextView applyTextView, clearTextView;
    private ImageView closeImageView;
    private LinearLayout containerLinearLayout;
    private boolean isApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_rent);
        initView();
        initControl();
        initExtras();
        initEquipemntRent();
    }

    private void initExtras() {

        NYLog.e("equip extras init");
        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        if (intent.hasExtra(NYHelper.EQUIPMENT_RENT)){

            NYLog.e("equip extras exist");

            EquipmentRentAddedList equipTemp = null;

            try {

                NYLog.e("equip extras init JSONArray");

                JSONArray arrayCat = new JSONArray(b.getString(NYHelper.EQUIPMENT_RENT));
                equipTemp = new EquipmentRentAddedList();
                equipTemp.parse(arrayCat);

                NYLog.e("equip extras parse "+equipTemp.getList());

                equipmentsRentTempList = equipTemp.getList();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            NYLog.e("equip extras parse NULL");
        }
    }

    public void initEquipemntRent(){

        if (equipmentsRentTempList == null)equipmentsRentTempList = new ArrayList<>();

        equipmentRents = new ArrayList<>();

        NYLog.e("cek equipment 1 ");

        try {

            NYLog.e("cek equipment 2 ");

            JSONArray array = new JSONArray(loadJSONFromAsset(this));

            NYLog.e("cek equipment 3 ");

            EquipmentRentList results = new EquipmentRentList();
            results.parse(array);

            NYLog.e("cek equipment 4 ");

            if (results != null && results.getList() != null){

                NYLog.e("cek equipment 5 ");

                equipmentRents = results.getList();

            } else {

                NYLog.e("cek equipment 6 ");

                /*EquipmentRent equipmentRent1 = new EquipmentRent();
                equipmentRent1.setId("1");
                equipmentRent1.setName("BCD Gear x1");
                equipmentRent1.setNormalPrice(25000);
                equipmentRent1.setSpecialPrice(10000);
                equipmentRent1.setAvailabilityStock(5);
                equipmentRents.add(equipmentRent1);*/

            }

        } catch (JSONException e) {

            NYLog.e("cek equipment 7 ");

            NYLog.e("cek equipment 8 : "+e.getMessage());

            e.printStackTrace();
        }


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
            final TextView countTextView = (TextView) myParticipantsView.findViewById(R.id.count_textView);
            ImageView plusImageView = (ImageView) myParticipantsView.findViewById(R.id.plus_imageView);
            ImageView minusImageView = (ImageView) myParticipantsView.findViewById(R.id.minus_imageView);

            if (equipmentRent != null){

                if (NYHelper.isStringNotEmpty(equipmentRent.getName())) {
                    nameTextView.setText(equipmentRent.getName());
                }

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
                    if (add.getId().equals(equipmentRent.getId())){
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

                            NYLog.e("cek equipment temp 1 : "+equipmentsRentTempList.size());
                            NYLog.e("cek equipment temp 2 : "+equipmentsRentTempList.toString());

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

                            NYLog.e("cek equipment temp 1 : "+equipmentsRentTempList.size());
                            NYLog.e("cek equipment temp 2 : "+equipmentsRentTempList.toString());
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
                equipmentsRentTempList = new ArrayList<EquipmentRentAdded>();
                initEquipemntRent();
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
        clearTextView = (TextView) findViewById(R.id.reset_textView);
        applyTextView = (TextView) findViewById(R.id.apply_textView);
        closeImageView = (ImageView) findViewById(R.id.close_imageView);
        containerLinearLayout = (LinearLayout) findViewById(R.id.equipemnt_rent_container_linearLayout);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
