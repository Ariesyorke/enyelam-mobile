package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.nex3z.flowlayout.FlowLayout;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.CategoryList;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.data.StateCategory;
import com.nyelam.android.data.StateFacility;
import com.nyelam.android.data.StateFacilityList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dotrip.DoTripResultActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.NYMasterDataStorage;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.midtrans.sdk.corekit.utilities.Utils.dpToPx;

public class FilterListServiceActivity extends BasicActivity implements NYMasterDataStorage.LoadDataListener<Category>, CompoundButton.OnCheckedChangeListener {

    private NYMasterDataStorage storage;
    private LinearLayout categoriesLinearLayout;
    private boolean isSelf = false;
    private int sortBy = 0;
    private double minPrice, maxPrice;
    private boolean ecotrip;
    private List<String> totalDives;
    private List<Category> items;
    private List<Category> categoryChooseList;
    private List<StateFacility> facilitiesItems;
    private List<StateFacility> facilitiesChooseList;
    private TextView doneTextView;
    private FlowLayout categoryFlowLayout;
    private FlowLayout facilitiesFlowLayout;
    private ImageView closeImageView;

    private RadioButton rbLowerPrice;
    private RadioButton rbHighestPrice;
    private CrystalRangeSeekbar rangeSeekbar;
    private TextView tvMinPrice;
    private TextView tvMaxPrice;
    private CheckBox check1, check2, check3, check4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_service);
        storage = new NYMasterDataStorage(this);
        initExtra();
        initView();
        initState();
        //initToolbar(false);
        getCategories();
        initControl();
        Toast.makeText(this, "dua", Toast.LENGTH_SHORT).show();
    }

    private void initState() {

        //sort by
        if (sortBy == 0){
            rbLowerPrice.setChecked(true);
            rbHighestPrice.setChecked(false);
        } else {
            rbLowerPrice.setChecked(false);
            rbHighestPrice.setChecked(true);
        }

        tvMinPrice.setText(NYHelper.priceFormatter(minPrice));
        tvMaxPrice.setText(NYHelper.priceFormatter(maxPrice));

        for (String st : totalDives){
            if (st.equals("1")){
                check1.setChecked(true);
            } else if (st.equals("2")){
                check2.setChecked(true);
            } else if (st.equals("3")){
                check3.setChecked(true);
            } else if (st.equals("<4")){
                check4.setChecked(true);
            }
        }
    }

    private void initControl() {
        rbLowerPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = 0;
                    rbHighestPrice.setChecked(false);
                }
            }
        });

        rbHighestPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = 1;
                    rbLowerPrice.setChecked(false);
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

        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                minPrice = minValue.doubleValue();
                maxPrice = maxValue.doubleValue();
                tvMinPrice.setText(NYHelper.priceFormatter(minValue.doubleValue()));
                tvMaxPrice.setText(NYHelper.priceFormatter(maxValue.doubleValue()));
            }
        });

        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvReset = (TextView) findViewById(R.id.reset_textView);
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });

    }

    private void getCategories() {
        //progressBar.setVisibility(View.VISIBLE);
        if(storage != null) {
            storage.loadCategoriesUseCacheIfAvailable(this, true);
        }
    }


    private void initView() {
        closeImageView = (ImageView) findViewById(R.id.close_imageView);
        categoriesLinearLayout = (LinearLayout) findViewById(R.id.categories_linearLayout);
        doneTextView = (TextView) findViewById(R.id.done_textView);
        categoryFlowLayout = (FlowLayout) findViewById(R.id.category_flowLayout);
        facilitiesFlowLayout = (FlowLayout) findViewById(R.id.facilities_flowLayout);

        check1 = findViewById(R.id.checkbox_one);
        check2 = findViewById(R.id.checkbox_two);
        check3 = findViewById(R.id.checkbox_three);
        check4 = findViewById(R.id.checkbox_more_four);

        rbLowerPrice = (RadioButton) findViewById(R.id.lowerPriceRadioButton);
        rbHighestPrice = (RadioButton) findViewById(R.id.highestPriceRadioButton);

        // get seekbar from view
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.price_range_seekBar);

        // get min and max text view
        tvMinPrice = (TextView) findViewById(R.id.price_min_textView);
        tvMaxPrice = (TextView) findViewById(R.id.price_max_textView);

        check1.setOnCheckedChangeListener(this);
        check2.setOnCheckedChangeListener(this);
        check3.setOnCheckedChangeListener(this);
        check4.setOnCheckedChangeListener(this);
    }

    private void initExtra() {
        totalDives = new ArrayList<>();
        categoryChooseList = new ArrayList<>();
        facilitiesChooseList = new ArrayList<>();
        facilitiesItems = new ArrayList<>();

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            if (intent.hasExtra(NYHelper.SORT_BY)) sortBy = extras.getInt(NYHelper.SORT_BY);
            if (intent.hasExtra(NYHelper.MIN_PRICE))minPrice = extras.getDouble(NYHelper.MIN_PRICE);
            if (intent.hasExtra(NYHelper.MAX_PRICE))maxPrice = extras.getDouble(NYHelper.MAX_PRICE);

            NYLog.e("filterextras sortBy : "+sortBy);
            NYLog.e("filterextras minPrice : "+minPrice);
            NYLog.e("filterextras maxPrice : "+maxPrice);

            if (intent.hasExtra(NYHelper.TOTAL_DIVES)){
                try {
                    JSONArray arrayTotalDives = new JSONArray(extras.getString(NYHelper.TOTAL_DIVES));
                    for (int i=0; i<arrayTotalDives.length(); i++) {
                        totalDives.add(arrayTotalDives.getString(i));
                    }
                    NYLog.e("filterextras totalDives : "+totalDives.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            NYLog.e("filterextras categories : "+extras.getString(NYHelper.CATEGORIES));
            NYLog.e("filterextras facilities : "+extras.getString(NYHelper.FACILITIES));

            if (intent.hasExtra(NYHelper.CATEGORIES)){
                try {
                    JSONArray arrayCat = new JSONArray(extras.getString(NYHelper.CATEGORIES));
                    CategoryList categoryList = new CategoryList();
                    categoryList.parse(arrayCat);
                    categoryChooseList = categoryList.getList();
                    NYLog.e("filterextras categories final : "+categoryList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (intent.hasExtra(NYHelper.FACILITIES)){
                try {
                    JSONArray arrayFac = new JSONArray(extras.getString(NYHelper.FACILITIES));
                    StateFacilityList stateFacilityList = new StateFacilityList();
                    stateFacilityList.parse(arrayFac);
                    facilitiesChooseList = stateFacilityList.getList();
                    NYLog.e("filterextras facilities final : "+stateFacilityList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFailed(Exception e) {

    }

    @Override
    public void onDataLoaded(List<Category> items) {
        this.items = new ArrayList<>();
        this.items = items;
        refreshData();
        Toast.makeText(this, "satu", Toast.LENGTH_SHORT).show();
    }

    public void refreshData(){
        if (items != null & items.size() > 0) {

            categoriesLinearLayout.removeAllViews();

            /*LayoutInflater allInflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View allCategoryView = allInflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

            FancyButton categoryFancyButton = (FancyButton) allCategoryView.findViewById(R.id.btn_category);
            categoryFancyButton.setText("All");*/

            buildLabelCategory(new Category("0","All"));

            for (Category cat : items) {
                buildLabelCategory(cat);
            }

        }

        if (facilitiesItems == null) facilitiesItems = new ArrayList<>();
        facilitiesItems.add(new StateFacility("dive_guide", "Dive Guide", false, R.drawable.ic_dive_guide_white, R.drawable.ic_dive_guide_unactive));
        facilitiesItems.add(new StateFacility("food", "Food", false, R.drawable.ic_food_and_drink_white, R.drawable.ic_food_and_drink_unactive));
        facilitiesItems.add(new StateFacility("towel", "Towel", false, R.drawable.ic_towel_white, R.drawable.ic_towel_unactive));
        facilitiesItems.add(new StateFacility("dive_equipment", "Dive Equipment", false, R.drawable.ic_equipment_white, R.drawable.ic_equipment_unactive));
        facilitiesItems.add(new StateFacility("transportation", "Transportation", false, R.drawable.ic_transportation_white, R.drawable.ic_transportation_unactive));
        facilitiesItems.add(new StateFacility("accomodation", "Accomodation", false, R.drawable.ic_accomodation_white, R.drawable.ic_accomodation_unactive));

        for (StateFacility fac : facilitiesItems) {
            buildLabelFacility(fac);
        }
    }

    private void buildLabelCategory(Category category) {

        if (category != null && NYHelper.isStringNotEmpty(category.getName())){
            LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myParticipantsView = inflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

            final FancyButton fbCategory = (FancyButton) myParticipantsView.findViewById(R.id.btn_category);

            fbCategory.setText(category.getName());
            //fbFacility.setTag(facility);

            // TODO: init state facilities yang sudah dipilih
            setViewCategory(true, category, fbCategory);

            fbCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category ct = (Category) fbCategory.getTag();
                    if (ct.getId().equals("0")){
                        //reset or select All
                    } else {
                        setViewCategory(false, null, fbCategory);
                    }
                }
            });

            categoryFlowLayout.addView(myParticipantsView);
        }

    }


    private void setViewCategory(boolean isInit, Category category, FancyButton fancyButton){
        Category state = new Category();
        boolean isExist = false;
        if (isInit == false){
            state = (Category) fancyButton.getTag();
            // TODO: cek dengan yang facilities yg sudah dipilih
            for (Category cat : categoryChooseList){
                if (cat.getId().equals(state.getId())){
                    state = cat;
                    isExist = true;
                    break;
                }
            }
        } else {
            // TODO: cek dengan yang facilities yg sudah dipilih
            for (Category cat : categoryChooseList){
                if (cat.getId().equals(category.getId())){
                    state = cat;
                    isExist = true;
                    break;
                }
            }
            if(!isExist) state = category;
        }

        if ((isInit && !isExist) || isExist){
            // TODO: state unactive
            fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            fancyButton.setIconResource(R.drawable.ic_transportation_unactive);
            fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
        } else if ((isInit && isExist) || !isExist){
            // TODO: state active
            fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            fancyButton.setIconResource(R.drawable.ic_transportation_white);
            fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey6));
        }
        fancyButton.setTag(state);

        // TODO: isInit == false -> setOnClick
        if(isInit == false){
            // TODO: hilangkan di list jika isCheked = false dan tambahkan jika isCheked = true
            if (isExist){
                categoryChooseList.remove(state);
            } else {
                categoryChooseList.add(state);
            }
        }

        // TODO: cek data di list
        if (categoryChooseList != null && categoryChooseList.size() > 0){
            NYLog.e("cek categories : "+categoryChooseList.toString());
        } else{
            NYLog.e("cek categories : NULL");
        }
    }

    private void buildLabelFacility(StateFacility facility) {

        if (facility != null && NYHelper.isStringNotEmpty(facility.getName())){
            LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myParticipantsView = inflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

            final FancyButton fbFacility = (FancyButton) myParticipantsView.findViewById(R.id.btn_category);

            fbFacility.setText(facility.getName());
            //fbFacility.setTag(facility);

            // TODO: init state facilities yang sudah dipilih
            setViewFacility(true, facility, fbFacility);

            fbFacility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewFacility(false, null, fbFacility);
                }
            });

            facilitiesFlowLayout.addView(myParticipantsView);
        }

    }

    private void setViewFacility(boolean isInit, StateFacility facility, FancyButton fancyButton){
        StateFacility state = new StateFacility();
        if (isInit == false){
             state = (StateFacility) fancyButton.getTag();
        } else {
            // TODO: cek dengan yang facilities yg sudah dipilih
            boolean isExist = false;
            for (StateFacility f : facilitiesChooseList){
                if (f.getTag().equals(facility.getTag())){
                    state = f;
                    isExist = true;
                    break;
                }
            }
            if(!isExist) state = facility;
        }

        if ((isInit && !state.isChecked()) || state.isChecked()){
            if (facility == null)state.setChecked(false);
            // TODO: state unactive 
            fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            fancyButton.setIconResource(ContextCompat.getDrawable(this, state.getUnactiveDrawable()));
            fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
        } else if ((isInit && state.isChecked()) || !state.isChecked()){
            if (facility == null)state.setChecked(true);
            // TODO: state active 
            fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            fancyButton.setIconResource(ContextCompat.getDrawable(this, state.getActiveDrawable()));
            fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey6));
        }
        fancyButton.setTag(state);

        // TODO: isInit == false -> setOnClick
        if(isInit == false){
            // TODO: hilangkan di list jika isCheked = false dan tambahkan jika isCheked = true
            boolean isExist = false;
            for (StateFacility fac : facilitiesChooseList){
                if (fac.getTag().equals(state.getTag())) isExist = true;
            }

            if (isExist && !state.isChecked()){
                facilitiesChooseList.remove(state);
            } else if (!isExist && state.isChecked()){
                facilitiesChooseList.add(state);
            }
        }

        // TODO: cek data di list
        if (facilitiesChooseList != null && facilitiesChooseList.size() > 0){
            NYLog.e("cek facilities : "+facilitiesChooseList.toString());
        } else{
            NYLog.e("cek facilities : NULL");
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, minPrice);
        intent.putExtra(NYHelper.MAX_PRICE, maxPrice);
        intent.putExtra(NYHelper.TOTAL_DIVES, totalDives.toString());
        intent.putExtra(NYHelper.CATEGORIES, categoryChooseList.toString());
        intent.putExtra(NYHelper.FACILITIES, facilitiesChooseList.toString());

        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.checkbox_one:
                setCheckBoxDives("1", isChecked);
                break;
            case R.id.checkbox_two:
                setCheckBoxDives("2", isChecked);
                break;
            case R.id.checkbox_three:
                setCheckBoxDives("3", isChecked);
                break;
            case R.id.checkbox_more_four:
                setCheckBoxDives(">4", isChecked);
                break;
        }
    }

    public void setCheckBoxDives(String pos, boolean isChecked){
        boolean isExist = false;
        for (int i=0; i<totalDives.size();i++){
            if (pos.equals(totalDives.get(i))) isExist = true;
        }

        if (isExist && !isChecked){
            totalDives.remove(pos);
        } else if (!isExist && isChecked){
            totalDives.add(pos);
        }

        if (totalDives != null && totalDives.size() > 0) NYLog.e("cek total dives : "+totalDives.toString());
    }


    public  void resetFilter(){

        categoryFlowLayout.removeAllViews();
        categoryChooseList = new ArrayList<>();

        if (items != null & items.size() > 0) {
            buildLabelCategory(new Category("0","All"));
            for (Category cat : items) {
                buildLabelCategory(cat);
            }
        }

        facilitiesFlowLayout.removeAllViews();
        facilitiesChooseList = new ArrayList<>();
        for (StateFacility fac : facilitiesItems) {
            buildLabelFacility(fac);
        }
    }

}
