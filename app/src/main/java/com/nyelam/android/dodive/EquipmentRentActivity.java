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

import com.nyelam.android.R;
import com.nyelam.android.data.EquipmentRent;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EquipmentRentActivity extends AppCompatActivity {

    private List<EquipmentRent> equipmentRents;
    private HashMap<String, Integer> equipmentsRentTempList;
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
        initEquipemntRent();
    }

    public void initEquipemntRent(){

        equipmentsRentTempList = new HashMap<>();

        equipmentRents = new ArrayList<>();

        EquipmentRent equipmentRent1 = new EquipmentRent();
        equipmentRent1.setId("1");
        equipmentRent1.setName("BCD Gear x1");
        equipmentRent1.setNormalPrice(25000);
        equipmentRent1.setSpecialPrice(10000);
        equipmentRent1.setAvailabilityStock(5);
        equipmentRents.add(equipmentRent1);

        containerLinearLayout.removeAllViews();
        int pos = 0;
        for (final EquipmentRent equipmentRent : equipmentRents) {

            final int position = pos;

            LayoutInflater linflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myParticipantsView = linflaterAddons.inflate(R.layout.view_item_equipment_rent_add, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(EquipmentRentActivity.this, 10));
            myParticipantsView.setLayoutParams(layoutParamsAddons);

            //myViewAddons.setId(0);
            //LinearLayout linearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.linearLayout);
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
                    priceTextView.setText("@" + NYHelper.priceFormatter(equipmentRent.getSpecialPrice()));
                    priceStrikeThroughTextView.setText("@" + NYHelper.priceFormatter(equipmentRent.getNormalPrice()));
                    priceStrikeThroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText("@" + NYHelper.priceFormatter(equipmentRent.getNormalPrice()));
                    priceStrikeThroughTextView.setVisibility(View.GONE);
                }

                final int[] total = {0};
                plusImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((total[0]+1) <= equipmentRent.getAvailabilityStock()){
                            total[0]++;
                            countTextView.setText(String.valueOf(total[0]));
                            equipmentsRentTempList.put(equipmentRent.getId(), total[0]);

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
                            if (total[0] <= 0)equipmentsRentTempList.remove(equipmentRent);

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
                equipmentsRentTempList = new HashMap<String, Integer>();
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



            Iterator it = equipmentsRentTempList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                //System.out.println(pair.getKey() + " = " + pair.getValue());
                //it.remove(); // avoids a ConcurrentModificationException
            }

            Intent intent = new Intent();
            intent.putExtra(NYHelper.EQUIPMENT_RENT, equipmentsRentTempList.toString());
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
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
