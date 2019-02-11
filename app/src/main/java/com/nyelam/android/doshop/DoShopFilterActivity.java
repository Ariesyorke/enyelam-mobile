package com.nyelam.android.doshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.flowlayout.FlowLayout;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.data.Brand;
import com.nyelam.android.data.BrandList;
import com.nyelam.android.data.StateFacility;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DoShopFilterActivity extends BasicActivity {

    private String sortBy;
    private double minPrice;
    private double maxPrice;
    private double minPriceDefault=0;
    private double maxPriceDefault=10000000;
    private BrandList brandList;
    private List<String> selectedBrands;

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.price_min_textView) TextView tvMinPrice;
    @BindView(R.id.price_max_textView) TextView tvMaxPrice;
    @BindView(R.id.clear_textView) TextView clearTextView;
    @BindView(R.id.apply_textView) TextView applyTextView;
    @BindView(R.id.brand_flowLayout)
    FlowLayout brandFlowLayout;
    @BindView(R.id.price_range_seekBar)
    CrystalRangeSeekbar rangeSeekbar;

    @BindView(R.id.rb_latest)
    RadioButton rbLatest;

    @BindView(R.id.rb_lowest)
    RadioButton rbLowest;

    @BindView(R.id.rb_highest)
    RadioButton rbHighest;

    @OnClick(R.id.iv_close) void close(){
        finish();
    }

    @OnClick(R.id.clear_textView) void clearFilter(){
        resetFilter();
    }

    @OnClick(R.id.apply_textView) void applyFilter(){
        String sortBy = "0";
        if (rbLowest.isChecked()){
            sortBy="1";
        } else if (rbHighest.isChecked()){
            sortBy="2";
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(NYHelper.SORT_BY, sortBy);
        resultIntent.putExtra(NYHelper.MIN_PRICE, minPrice);
        resultIntent.putExtra(NYHelper.MAX_PRICE, maxPrice);

        if(selectedBrands != null && !selectedBrands.isEmpty()) {
            String[] brands = new String[selectedBrands.size()];
            brands = selectedBrands.toArray(brands);
            resultIntent.putExtra(NYHelper.SELECTED_BRANDS, brands);
        }

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_filter);
        ButterKnife.bind(this);
        initState();
        initView();
        initControl();
    }

    private void initState() {

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            if (intent.hasExtra(NYHelper.SELECTED_BRANDS)) {
                String[] brans = intent.getStringArrayExtra(NYHelper.SELECTED_BRANDS);
                selectedBrands = new LinkedList<>(Arrays.asList(brans));
            }
            if(intent.hasExtra(NYHelper.FILTER_BRANDS)) {
                String arrayString = intent.getStringExtra(NYHelper.FILTER_BRANDS);
                try {
                    JSONArray array = new JSONArray(arrayString);
                    brandList = new BrandList();
                    brandList.parse(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (intent.hasExtra(NYHelper.SORT_BY)){
                sortBy = intent.getStringExtra(NYHelper.SORT_BY);
                if (sortBy.equals("0")){
                    rbLatest.setChecked(true);
                    rbLowest.setChecked(false);
                    rbHighest.setChecked(false);
                } else if (sortBy.equals("1")){
                    rbLatest.setChecked(false);
                    rbLowest.setChecked(true);
                    rbHighest.setChecked(false);
                } else {
                    rbLatest.setChecked(false);
                    rbLowest.setChecked(false);
                    rbHighest.setChecked(true);
                }
            }
            if (intent.hasExtra(NYHelper.MIN_PRICE_DEAFULT)){
                minPriceDefault = intent.getDoubleExtra(NYHelper.MIN_PRICE_DEAFULT, 0);
            }
            if (intent.hasExtra(NYHelper.MIN_PRICE)){
                minPrice = intent.getDoubleExtra(NYHelper.MIN_PRICE, minPriceDefault);
            }
            if (intent.hasExtra(NYHelper.MAX_PRICE_DEFAULT)){
                maxPriceDefault = intent.getDoubleExtra(NYHelper.MAX_PRICE_DEFAULT, 10000000);
            }
            if (intent.hasExtra(NYHelper.MAX_PRICE)){
                maxPrice = intent.getDoubleExtra(NYHelper.MAX_PRICE, maxPriceDefault);
            }
        }

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
            rangeSeekbar.setMaxStartValue((float)maxPriceDefault);
        } else {
            rangeSeekbar.setMaxStartValue((float)maxPrice);
        }
        rangeSeekbar.apply();
    }

    private void initView() {
        brandFlowLayout.removeAllViews();
        if(brandList != null) {
            for(Brand brand: brandList.getList()) {
                buildBrandView(brand);
            }
        }
    }

    private void buildBrandView(final Brand brand) {
        LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View categoryView = inflaterAddons.inflate(R.layout.view_item_brand, null); //here item is the the layout you want to inflate
        final FancyButton fbBrand = (FancyButton) categoryView.findViewById(R.id.btn_brand);
        fbBrand.setText(brand.getName());

        // TODO: init state facilities yang sudah dipilih
        fbBrand.setTag(brand);
        setViewBrand(true, brand, fbBrand);

        fbBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewBrand(false, brand, fbBrand);
            }
        });

        brandFlowLayout.addView(categoryView);
    }

    private void setViewBrand(boolean isInit, Brand brand, FancyButton fancyButton) {
        boolean isExist = false;

        if(selectedBrands != null && !selectedBrands.isEmpty()) {
            if(selectedBrands.contains(brand.getId())) {
                isExist = true;
            }
        }

        if(isInit) {
            if (!isExist) {
                fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
                fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
                fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            } else {
                fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
                fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
                fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey6));
            }
        } else {
            if (isExist) {
                fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
                fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
                fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            } else {
                fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
                fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
                fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey6));
            }
        }

        if(isExist) {
            if(!isInit) {
                selectedBrands.remove(brand.getId());
            }
        } else {
            if(!isInit) {
                if(selectedBrands == null) selectedBrands = new ArrayList<>();
                selectedBrands.add(brand.getId());
            }
        }
    }

    private void initControl() {
        rbLatest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = "0";
                    rbLowest.setChecked(false);
                    rbHighest.setChecked(false);
                }
            }
        });

        rbLowest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = "1";
                    rbLatest.setChecked(false);
                    rbHighest.setChecked(false);
                }
            }
        });

        rbHighest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = "2";
                    rbLatest.setChecked(false);
                    rbLowest.setChecked(false);
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
        sortBy = "0";
        rbLatest.setChecked(true);
        rbLowest.setChecked(false);
        rbHighest.setChecked(false);

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
