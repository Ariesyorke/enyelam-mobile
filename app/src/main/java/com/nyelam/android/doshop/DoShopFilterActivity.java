package com.nyelam.android.doshop;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

public class DoShopFilterActivity extends BasicActivity {


    private String sortBy;
    private double minPrice;
    private double maxPrice;
    private double minPriceDefault=0;
    private double maxPriceDefault=10000000;

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.price_min_textView) TextView tvMinPrice;
    @BindView(R.id.price_max_textView) TextView tvMaxPrice;
    @BindView(R.id.clear_textView) TextView clearTextView;
    @BindView(R.id.apply_textView) TextView applyTextView;

    @BindView(R.id.price_range_seekBar)
    CrystalRangeSeekbar rangeSeekbar;

    @BindView(R.id.lowerPriceRadioButton)
    RadioButton lowerPriceRadioButton;

    @BindView(R.id.highestPriceRadioButton)
    RadioButton highestPriceRadioButton;

    @OnClick(R.id.iv_close) void close(){
        finish();
    }

    @OnClick(R.id.clear_textView) void clearFilter(){
        resetFilter();
    }

    @OnClick(R.id.apply_textView) void applyFilter(){
        String sortBy = "1";
        if (highestPriceRadioButton.isChecked())sortBy="2";

        Intent resultIntent = new Intent();
        resultIntent.putExtra(NYHelper.SORT_BY, sortBy);
        resultIntent.putExtra(NYHelper.MIN_PRICE, minPrice);
        resultIntent.putExtra(NYHelper.MAX_PRICE, maxPrice);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_filter);
        ButterKnife.bind(this);
        initState();
        initControl();
    }

    private void initState() {

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            if (intent.hasExtra(NYHelper.SORT_BY)){
                sortBy = intent.getStringExtra(NYHelper.SORT_BY);
                if (sortBy.equals("1")){
                    lowerPriceRadioButton.setChecked(true);
                    highestPriceRadioButton.setChecked(false);
                } else {
                    lowerPriceRadioButton.setChecked(false);
                    highestPriceRadioButton.setChecked(true);
                }
                //Toast.makeText(this, "sort - "+sortBy, Toast.LENGTH_SHORT).show();
            }
            if (intent.hasExtra(NYHelper.MIN_PRICE)){
                minPrice = intent.getDoubleExtra(NYHelper.MIN_PRICE, minPriceDefault);
                //Toast.makeText(this, "min - "+String.valueOf(minPrice), Toast.LENGTH_SHORT).show();
            }
            if (intent.hasExtra(NYHelper.MAX_PRICE)){
                maxPrice = intent.getDoubleExtra(NYHelper.MAX_PRICE, maxPriceDefault);
                //Toast.makeText(this, "max - "+String.valueOf(maxPrice), Toast.LENGTH_SHORT).show();
            }
        }

        //reset price range
//        minPrice = minPriceDefault;
//        maxPrice = maxPriceDefault;
//        tvMinPrice.setText(NYHelper.priceFormatter(minPriceDefault));
//        tvMaxPrice.setText(NYHelper.priceFormatter(maxPriceDefault));
//        rangeSeekbar.setMinStartValue((float)minPriceDefault);
//        rangeSeekbar.setMaxStartValue((float)maxPriceDefault);
//        rangeSeekbar.apply();



        tvMinPrice.setText(NYHelper.priceFormatter(minPrice));
        tvMaxPrice.setText(NYHelper.priceFormatter(maxPrice));

        rangeSeekbar.setMinValue((float) minPriceDefault);
        rangeSeekbar.setMaxValue((float) maxPriceDefault);
        rangeSeekbar.apply();

        if (minPrice < 0 ){
            rangeSeekbar.setMinStartValue(Float.valueOf(String.valueOf(minPriceDefault)));
        } else {
            rangeSeekbar.setMinStartValue(Float.valueOf(String.valueOf(minPrice)));
        }

        if (maxPrice < 0){
            //Toast.makeText(this, "max price 1 - "+String.valueOf(maxPrice), Toast.LENGTH_SHORT).show();
            rangeSeekbar.setMaxStartValue((float)maxPriceDefault);
        } else {
            //Toast.makeText(this, "max price 2 - "+String.valueOf(maxPrice), Toast.LENGTH_SHORT).show();
            rangeSeekbar.setMaxStartValue((float)maxPrice);
        }
        rangeSeekbar.apply();
    }

    private void initControl() {
        lowerPriceRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = "1";
                    highestPriceRadioButton.setChecked(false);
                }
            }
        });

        highestPriceRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = "2";
                    lowerPriceRadioButton.setChecked(false);
                }
            }
        });

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minPrice = minValue.doubleValue();
                maxPrice = maxValue.doubleValue();
                tvMinPrice.setText(NYHelper.priceFormatter(minValue.doubleValue()));
                tvMaxPrice.setText(NYHelper.priceFormatter(maxValue.doubleValue()));
            }
        });

    }


    public  void resetFilter(){

        //reset radio button
        sortBy = "1";
        lowerPriceRadioButton.setChecked(true);
        highestPriceRadioButton.setChecked(false);

        //reset price range
        minPrice = minPriceDefault;
        maxPrice = maxPriceDefault;
        tvMinPrice.setText(NYHelper.priceFormatter(minPriceDefault));
        tvMaxPrice.setText(NYHelper.priceFormatter(maxPriceDefault));
        rangeSeekbar.setMinStartValue((float)minPriceDefault);
        rangeSeekbar.setMaxStartValue((float)maxPriceDefault);
        rangeSeekbar.apply();
    }



}
