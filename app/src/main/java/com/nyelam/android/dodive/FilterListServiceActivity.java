package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.midtrans.sdk.corekit.utilities.Utils.dpToPx;

public class FilterListServiceActivity extends BasicActivity implements NYMasterDataStorage.LoadDataListener<Category>, CompoundButton.OnCheckedChangeListener {

    private NYMasterDataStorage storage;
    private LinearLayout categoriesLinearLayout;
    private int sortBy = 2;
    private double minPrice, maxPrice ;
    private double minPriceDefault, maxPriceDefault;
    private List<String> totalDives;
    private List<Category> itemsDefault;
    private List<Category> items;
    private List<Category> categoryChooseList;
    private List<StateFacility> facilitiesItems;
    private List<StateFacility> facilitiesChooseList;
    private TextView doneTextView;
    private FlowLayout categoryFlowLayout;
    private FlowLayout facilitiesFlowLayout;
    private ImageView closeImageView;

    private boolean isApply = false;
    private TextView applyTextView, clearTextView;
    private RadioButton rbLowerPrice;
    private RadioButton rbHighestPrice;
    private CrystalRangeSeekbar rangeSeekbar;
    private TextView tvMinPrice;
    private TextView tvMaxPrice;
    private CheckBox check1, check2, check3, check4;

    private HashMap<String, Boolean> idCatMap=new HashMap<String, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_service);
        storage = new NYMasterDataStorage(this);
        initExtra();
        initView();
        initState();
        initControl();
        getCategories();
    }

    private void initState() {
        //sort by
        if (sortBy == 2){
            rbLowerPrice.setChecked(true);
            rbHighestPrice.setChecked(false);
        } else {
            rbLowerPrice.setChecked(false);
            rbHighestPrice.setChecked(true);
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
            //Toast.makeText(this, "max price 1 - "+String.valueOf(maxPrice), Toast.LENGTH_SHORT).show();
            rangeSeekbar.setMaxStartValue((float)maxPriceDefault);
        } else {
            //Toast.makeText(this, "max price 2 - "+String.valueOf(maxPrice), Toast.LENGTH_SHORT).show();
            rangeSeekbar.setMaxStartValue((float)maxPrice);
        }
        rangeSeekbar.apply();

        for (String st : totalDives){
            if (st.equals("=1")){
                check1.setChecked(true);
            } else if (st.equals("=2")){
                check2.setChecked(true);
            } else if (st.equals("=3")){
                check3.setChecked(true);
            } else if (st.equals(">=4")){
                check4.setChecked(true);
            }
        }
    }

    private void initControl() {
        rbLowerPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortBy = 2;
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

        /*// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                minPrice = minValue.doubleValue();
                maxPrice = maxValue.doubleValue();
                tvMinPrice.setText(NYHelper.priceFormatter(minValue.doubleValue()));
                tvMaxPrice.setText(NYHelper.priceFormatter(maxValue.doubleValue()));
            }
        });*/

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

        // get min and max text view
        clearTextView = (TextView) findViewById(R.id.clear_textView);
        applyTextView = (TextView) findViewById(R.id.apply_textView);
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
            if (intent.hasExtra(NYHelper.MIN_PRICE_DEAFULT))minPriceDefault = extras.getDouble(NYHelper.MIN_PRICE_DEAFULT);
            if (intent.hasExtra(NYHelper.MAX_PRICE))maxPrice = extras.getDouble(NYHelper.MAX_PRICE);
            if (intent.hasExtra(NYHelper.MAX_PRICE_DEFAULT))maxPriceDefault = extras.getDouble(NYHelper.MAX_PRICE_DEFAULT);
            totalDives = new ArrayList<>();
            if (intent.hasExtra(NYHelper.TOTAL_DIVES)){
                try {
                    JSONArray arrayTotalDives = new JSONArray(extras.getString(NYHelper.TOTAL_DIVES));
                    for (int i=0; i<arrayTotalDives.length(); i++) {
                        totalDives.add(arrayTotalDives.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (totalDives != null && totalDives.size() > 0) NYLog.e("cek total dives INTENT : "+totalDives.toString());


            if (intent.hasExtra(NYHelper.CATEGORIES)){
                try {
                    JSONArray arrayCat = new JSONArray(extras.getString(NYHelper.CATEGORIES));
                    CategoryList categoryList = new CategoryList();
                    categoryList.parse(arrayCat);
                    categoryChooseList = categoryList.getList();

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
        this.itemsDefault = new ArrayList<>();
        this.items = new ArrayList<>();
        //this.items.addAll(items);
        this.itemsDefault = items;
        this.items = items;
        this.items.add(0,new Category("0","All"));

        for (Category ct : items){
            if(ct != null && NYHelper.isStringNotEmpty(ct.getId())) idCatMap.put(ct.getId(), false);
        }

        //cek state
        for (Category cat : categoryChooseList){
            idCatMap.put(cat.getId(), idCatMap.containsKey(cat.getId()) ? true : false);
        }

        Iterator myOwnIterator = idCatMap.keySet().iterator();
        while(myOwnIterator.hasNext()) {
            String key=(String)myOwnIterator.next();
            Boolean value=(Boolean)idCatMap.get(key);
        }

        refreshData();
    }


    public void refreshData(){

        if (items != null & items.size() > 0) {

            categoryFlowLayout.removeAllViews();
            for (Category cat :  items) {
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

    private void buildLabelCategory(final Category category) {

        if (category != null && NYHelper.isStringNotEmpty(category.getName())){
            LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View categoryView = inflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

            final FancyButton fbCategory = (FancyButton) categoryView.findViewById(R.id.btn_category);

            fbCategory.setText(category.getName());

            // TODO: init state facilities yang sudah dipilih
            setViewCategory(category, fbCategory);

            fbCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category ct = (Category) fbCategory.getTag();
                    if (ct.getId().equals("0")){

                        if (idCatMap.get(ct.getId())){
                            idCatMap.put(ct.getId(), false);
                            for (String key : idCatMap.keySet()) {
                                idCatMap.put(key, false);
                            }
                        } else {
                            idCatMap.put(ct.getId(), true);
                            for (String key : idCatMap.keySet()) {
                                idCatMap.put(key, true);
                            }
                        }

                    } else {

                        if (idCatMap.get(ct.getId())){
                            idCatMap.put(ct.getId(), false);

                            for (String key : idCatMap.keySet()) {
                                if (key.equals("0"))idCatMap.put(key, false);
                            }

                        } else {
                            idCatMap.put(ct.getId(), true);
                        }



                        // TODO: check ALL
                        int i = 0;
                        for (String key : idCatMap.keySet()) {
                            if (idCatMap.get(key)){
                                i++;
                            }
                        }

                        if (i == items.size()-1){
                            idCatMap.put("0", true);
                        }

                    }

                    //refresh category
                    categoryFlowLayout.removeAllViews();
                    for (Category cat : items) {
                        buildLabelCategory(cat);
                    }
//                    categoryFlowLayout.setChildSpacing(FlowLayout.SPACING_AUTO);
//                    categoryFlowLayout.setChildSpacingForLastRow(FlowLayout.SPACING_ALIGN);

                }
            });

            categoryFlowLayout.addView(categoryView);
        }

    }


    private void setViewCategory(Category category, FancyButton fancyButton){

        if (!idCatMap.get(category.getId())){
            // TODO: state unactive
            fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            fancyButton.setIconResource(R.drawable.ic_ferry_inactive);
            fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
        } else {
            // TODO: state active
            //NYLog.e("PANGGIL " + category.getName());
            fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            fancyButton.setIconResource(R.drawable.ic_ferry);
            fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
            fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey6));
        }

        fancyButton.setTag(category);

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
            View facilityView = inflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

            final FancyButton fbFacility = (FancyButton) facilityView.findViewById(R.id.btn_category);

            fbFacility.setText(facility.getName());

            // TODO: init state facilities yang sudah dipilih
            setViewFacility(true, facility, fbFacility);

            fbFacility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewFacility(false, null, fbFacility);
                }
            });

            facilitiesFlowLayout.addView(facilityView);
        }

    }

    private void setViewFacility(boolean isInit, StateFacility facility, FancyButton fancyButton){

        //true, facility, fbFacility);

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
            state.setChecked(isExist);
        }

        if ((isInit && !state.isChecked()) || (!isInit && state.isChecked())){
            if (facility == null)state.setChecked(false);
            // TODO: state unactive 
            fancyButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            fancyButton.setIconResource(ContextCompat.getDrawable(this, state.getUnactiveDrawable()));
            fancyButton.setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_grey2));
            fancyButton.setFocusBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ny_orange2));
        } else if ((isInit && state.isChecked()) || (!isInit && !state.isChecked())){
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

        if (isApply){
            categoryChooseList = new ArrayList<>();
            for (String key : idCatMap.keySet()) {
                if (idCatMap.get(key) == true)categoryChooseList.add(new Category(key,""));
            }

            Intent intent = new Intent();
            intent.putExtra(NYHelper.SORT_BY, sortBy);
            intent.putExtra(NYHelper.MIN_PRICE, minPrice);
            intent.putExtra(NYHelper.MAX_PRICE, maxPrice);

            if (totalDives != null && totalDives.size() > 0){

                JSONArray array = new JSONArray();
                for (String st : totalDives){
                    array.put(st);
                }
                intent.putExtra(NYHelper.TOTAL_DIVES, array.toString());
            }

            intent.putExtra(NYHelper.CATEGORIES, categoryChooseList.toString());
            intent.putExtra(NYHelper.FACILITIES, facilitiesChooseList.toString());

            if (totalDives != null && totalDives.size() > 0){
                NYLog.e("cek total dives ONBACKPRESS : "+totalDives.toString());
            } else{
                NYLog.e("cek total dives ONBACKPRESS : null");
            }

            setResult(RESULT_OK, intent);
        }

        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.checkbox_one:
                setCheckBoxDives("=1", isChecked);
                //Toast.makeText(this, "1 - "+String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_two:
                setCheckBoxDives("=2", isChecked);
                //Toast.makeText(this, "2 - "+String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_three:
                setCheckBoxDives("=3", isChecked);
                //Toast.makeText(this, "3 - "+String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_more_four:
                setCheckBoxDives(">=4", isChecked);
                //Toast.makeText(this, "4 - "+String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setCheckBoxDives(String pos, boolean isChecked){
        boolean isExist = false;
        for (int i=0; i<totalDives.size();i++){
            if (pos.equals(totalDives.get(i))){
                isExist = true;
                break;
            }
        }

        if (isExist && !isChecked){

            for (Iterator<String> iterator = totalDives.iterator(); iterator.hasNext(); ) {
                String value = iterator.next();
                if (value.equals(pos)) {
                    iterator.remove();
                }
            }

        } else if (!isExist && isChecked){
            totalDives.add(pos);
        }

        if (totalDives != null && totalDives.size() > 0) NYLog.e("cek total dives ONCLICK: "+totalDives.toString());
    }


    public  void resetFilter(){

        //reset radio button
        sortBy = 2;
        rbLowerPrice.setChecked(true);
        rbHighestPrice.setChecked(false);

        //reset price range
        minPrice = minPriceDefault;
        maxPrice = maxPriceDefault;
        tvMinPrice.setText(NYHelper.priceFormatter(minPriceDefault));
        tvMaxPrice.setText(NYHelper.priceFormatter(maxPriceDefault));
        rangeSeekbar.setMinStartValue((float)minPriceDefault);
        rangeSeekbar.setMaxStartValue((float)maxPriceDefault);
        rangeSeekbar.apply();

        //total dives
        totalDives = new ArrayList<>();
        check1.setChecked(false);
        check2.setChecked(false);
        check3.setChecked(false);
        check4.setChecked(false);

        //reset categories
        for (String key : idCatMap.keySet()) {
            idCatMap.put(key, false);
        }

        //refresh category
        categoryFlowLayout.removeAllViews();
        for (Category cat : items) {
            buildLabelCategory(cat);
        }
//        categoryFlowLayout.setChildSpacing(FlowLayout.SPACING_AUTO);
//        categoryFlowLayout.setChildSpacingForLastRow(FlowLayout.SPACING_ALIGN);
        //categoryFlowLayout.setRowSpacing(R.dimen.row_spacing);


        //reset facilities
        facilitiesFlowLayout.removeAllViews();
        facilitiesChooseList = new ArrayList<>();
        for (StateFacility fac : facilitiesItems) {
            buildLabelFacility(fac);
        }

        facilitiesFlowLayout.setChildSpacing(FlowLayout.SPACING_AUTO);
        facilitiesFlowLayout.setChildSpacingForLastRow(FlowLayout.SPACING_ALIGN);
        facilitiesFlowLayout.setRowSpacing(R.dimen.row_spacing);
        facilitiesFlowLayout.setChildSpacing(R.dimen.row_spacing);
    }


    /*public List<Category> sortingCategory() {
        // not yet sorted
        List<Category> sortedCategory = new ArrayList<Category>(meMap.keySet());
        Collections.sort(sortedCategory, new Comparator<Category>() {

            public int compare(Category o1, Category o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return sortedCategory;
    }*/


}
