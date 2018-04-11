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
import com.nyelam.android.data.Facilities;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dotrip.DoTripResultActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.NYMasterDataStorage;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.corekit.utilities.Utils.dpToPx;

public class FilterListServiceActivity extends BasicActivity implements NYMasterDataStorage.LoadDataListener<Category>, CompoundButton.OnCheckedChangeListener {

    private NYMasterDataStorage storage;
    private LinearLayout categoriesLinearLayout;
    private boolean isSelf = false;
    private ArrayList<String> categories;
    protected String keyword, diverId, diver, certificate, date, type, activityName;
    private int sortBy = 0;
    private double minPrice, maxPrice;
    private boolean ecotrip;
    private List<String> totalDives;
    private List<Category> items;
    private List<Category> categoryChooseList;
    private List<Facilities> facilitiesChooseList;
    private TextView doneTextView;
    private FlowLayout categoryFlowLayout;
    private FlowLayout facilitiesFlowLayout;
    private ImageView closeImageView;
    private String[] facilitiesTexts;
    private int[] facilitiesDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_service);
        storage = new NYMasterDataStorage(this);
        initExtra();
        initView();
        //initToolbar(false);
        getCategories();
        initControl();
    }

    private void initControl() {

        final RadioButton rbLowerPrice = (RadioButton) findViewById(R.id.lowerPriceRadioButton);
        final RadioButton rbHighestPrice = (RadioButton) findViewById(R.id.highestPriceRadioButton);
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





        /*RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.check(sortBy);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                sortBy = index;
                Toast.makeText(FilterListServiceActivity.this, String.valueOf(sortBy), Toast.LENGTH_SHORT).show();
            }

        });*/

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.price_range_seekBar);

        // get min and max text view
        final TextView tvMin = (TextView) findViewById(R.id.price_min_textView);
        final TextView tvMax = (TextView) findViewById(R.id.price_max_textView);

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minPrice = minValue.doubleValue();
                maxPrice = maxValue.doubleValue();
                tvMin.setText(NYHelper.priceFormatter(minValue.doubleValue()));
                tvMax.setText(NYHelper.priceFormatter(maxValue.doubleValue()));
            }
        });

        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                minPrice = minValue.doubleValue();
                maxPrice = maxValue.doubleValue();
                tvMin.setText(NYHelper.priceFormatter(minValue.doubleValue()));
                tvMax.setText(NYHelper.priceFormatter(maxValue.doubleValue()));
            }
        });

        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categories = new ArrayList<String>();
                // TODO: get id checkbox
                for (int i = 1; i < categoriesLinearLayout.getChildCount(); i++) {
                    LinearLayout view = (LinearLayout) categoriesLinearLayout.getChildAt(i);

                    ImageView imageView = (ImageView) view.getChildAt(0);
                    TextView textView = (TextView) view.getChildAt(1);
                    AppCompatCheckBox checkBox = (AppCompatCheckBox) view.getChildAt(2);

                    if (checkBox.isChecked()){
                        categories.add((String) checkBox.getTag());
                    }
                }

                Intent intent = new Intent(FilterListServiceActivity.this, DoDiveSearchResultActivity.class);
                if (NYHelper.isStringNotEmpty(activityName) && activityName.equals(NYHelper.DOTRIP))
                    intent = new Intent(FilterListServiceActivity.this, DoTripResultActivity.class);
                intent.putExtra(NYHelper.ACTIVITY, this.getClass().getName());
                intent.putExtra(NYHelper.KEYWORD, keyword);
                intent.putExtra(NYHelper.IS_ECO_TRIP, ecotrip);
                intent.putExtra(NYHelper.ID_DIVER, diverId);
                intent.putExtra(NYHelper.DIVER, diver);
                intent.putExtra(NYHelper.CERTIFICATE, certificate);
                intent.putExtra(NYHelper.SCHEDULE, date);
                intent.putExtra(NYHelper.TYPE, type);
                intent.putStringArrayListExtra(NYHelper.CATEGORIES, categories);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

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

        CheckBox check1 = findViewById(R.id.checkbox_one);
        CheckBox check2 = findViewById(R.id.checkbox_two);
        CheckBox check3 = findViewById(R.id.checkbox_three);
        CheckBox check4 = findViewById(R.id.checkbox_more_four);

        check1.setOnCheckedChangeListener(this);
        check2.setOnCheckedChangeListener(this);
        check3.setOnCheckedChangeListener(this);
        check4.setOnCheckedChangeListener(this);
    }

    private void initExtra() {
        categories = new ArrayList<>();
        totalDives = new ArrayList<>();
        facilitiesChooseList = new ArrayList<>();

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.ACTIVITY) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ACTIVITY))){
                activityName = extras.getString(NYHelper.ACTIVITY);
                Toast.makeText(this, activityName, Toast.LENGTH_SHORT).show();
            }
            if(intent.hasExtra(NYHelper.KEYWORD) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.KEYWORD))) keyword = extras.getString(NYHelper.KEYWORD);
            if(intent.hasExtra(NYHelper.IS_ECO_TRIP)) ecotrip = extras.getBoolean(NYHelper.IS_ECO_TRIP);
            if(intent.hasExtra(NYHelper.ID_DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ID_DIVER))) diverId = extras.getString(NYHelper.ID_DIVER);
            if(intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVER))) diver = extras.getString(NYHelper.DIVER);
            if(intent.hasExtra(NYHelper.CERTIFICATE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.CERTIFICATE))) certificate = extras.getString(NYHelper.CERTIFICATE);
            if(intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SCHEDULE))) date = extras.getString(NYHelper.SCHEDULE);
            if(intent.hasExtra(NYHelper.TYPE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.TYPE))){
                type = extras.getString(NYHelper.TYPE);
            }

            if(intent.hasExtra(NYHelper.CATEGORIES) && !extras.get(NYHelper.CATEGORIES).equals(null)){
                categories = extras.getStringArrayList(NYHelper.CATEGORIES);
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

        if (items != null & items.size() > 0){
            this.items = new ArrayList<>();
            this.items = items;

            NYLog.e("Check Category : "+items.toString());

            categoriesLinearLayout.removeAllViews();

            LayoutInflater allInflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View allCategoryView = allInflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams allLayoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            allLayoutParamsAddons.setMargins(0, 0, NYHelper.integerToDP(this, 10), 0);
            allCategoryView.setLayoutParams(allLayoutParamsAddons);

            ImageView allCategoryImageView = (ImageView) allCategoryView.findViewById(R.id.category_imageView);
            TextView allCategoryNameTextView = (TextView) allCategoryView.findViewById(R.id.category_name_textView);
            final AppCompatCheckBox allCategoryCheckBox = (AppCompatCheckBox) allCategoryView.findViewById(R.id.category_appCompatCheckBox);
            allCategoryNameTextView.setText("All");

            allCategoryCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 1; i < categoriesLinearLayout.getChildCount(); i++) {
                        LinearLayout view = (LinearLayout) categoriesLinearLayout.getChildAt(i);

                        ImageView imageView = (ImageView) view.getChildAt(0);
                        TextView textView = (TextView) view.getChildAt(1);
                        AppCompatCheckBox checkBox = (AppCompatCheckBox) view.getChildAt(2);

                        if (allCategoryCheckBox.isChecked()){
                            checkBox.setChecked(true);
                        } else {
                            checkBox.setChecked(false);
                        }

                    }
                }
            });

            // TODO: cek apakah filter semua, jika yaa cehcklist ALL 
            boolean isAll = true;
            for (Category cat : items){
                boolean isSama = false;
                for (String st : categories){
                    if (cat.getId().equals(st)){
                        isSama = true;
                        break;
                    }
                }

                if (!isSama){
                    isAll = false;
                    break;
                }
            }

            if (isAll) allCategoryCheckBox.setChecked(true);

            //categoriesLinearLayout.addView(allCategoryView);
            categoryFlowLayout.addView(allCategoryView);

            int pos = 1;
            for (Category category : items) {

                int position = pos;

                LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View myParticipantsView = inflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

                ImageView categoryImageView = (ImageView) myParticipantsView.findViewById(R.id.category_imageView);
                TextView categoryNameTextView = (TextView) myParticipantsView.findViewById(R.id.category_name_textView);
                AppCompatCheckBox categoryCheckBox = (AppCompatCheckBox) myParticipantsView.findViewById(R.id.category_appCompatCheckBox);

                categoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked){
                            LinearLayout view = (LinearLayout) categoriesLinearLayout.getChildAt(0);
                            AppCompatCheckBox checkBox = (AppCompatCheckBox) view.getChildAt(2);
                            isSelf = false;
                            checkBox.setChecked(false);
                        }
                    }
                });

                if (category.getIconImage() != null){
                    categoryImageView.setImageBitmap(category.getIconImage());
                } else {

                }

                if (NYHelper.isStringNotEmpty(category.getName())){
                    categoryNameTextView.setText(category.getName());
                    NYLog.e("Check Category Name : "+category.getName());
                }

                categoryCheckBox.setTag(category.getId());

                for (String catId : categories){
                    if (catId.equals(category.getId())){
                        categoryCheckBox.setChecked(true);
                        break;
                    }
                }

                pos++;
                //categoriesLinearLayout.addView(myParticipantsView);
                categoryFlowLayout.addView(myParticipantsView);
                NYLog.e("Check Category : ADDED");

            }

        } else {
            NYLog.e("Check Category : NULL");
        }

        facilitiesTexts = new String[]{"Dive Guide", "Food & Drinks", "Towel", "Equipment", "Transportation", "Accomodation"};
        facilitiesDrawable = new int[]{R.drawable.ic_dive_guide_white, R.drawable.ic_food_and_drink_white, R.drawable.ic_towel_white, R.drawable.ic_equipment_white, R.drawable.ic_transportation_white, R.drawable.ic_accomodation_white};

        for (int i = 0; i < facilitiesTexts.length; i++) {
            buildLabel(i);
        }
    }


    private void buildLabel(int pos) {
        LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myParticipantsView = inflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

        LinearLayout linearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.linearLayout);
        ImageView categoryImageView = (ImageView) myParticipantsView.findViewById(R.id.category_imageView);
        TextView categoryNameTextView = (TextView) myParticipantsView.findViewById(R.id.category_name_textView);
        AppCompatCheckBox categoryCheckBox = (AppCompatCheckBox) myParticipantsView.findViewById(R.id.category_appCompatCheckBox);

        linearLayout.setBackground(ContextCompat.getDrawable(this,R.drawable.ny_rectangle_orange));
        linearLayout.setPadding((int)dpToPx(10),(int)dpToPx(10),(int)dpToPx(10),(int)dpToPx(10));
        categoryImageView.setImageResource(facilitiesDrawable[pos]);

        if (NYHelper.isStringNotEmpty(facilitiesTexts[pos])){
            categoryNameTextView.setText(facilitiesTexts[pos]);
        }

        facilitiesFlowLayout.addView(myParticipantsView);
    }

    private void setViewCategory(boolean isChecked, LinearLayout ll, ImageView iv, TextView tv){
        if (isChecked){
            ll.setBackground(ContextCompat.getDrawable(this, R.drawable.ny_rectangle_orange));
            iv.setImageResource(R.drawable.ic_header_nyelam_white);
            tv.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            ll.setBackground(ContextCompat.getDrawable(this, R.drawable.ny_rectangle_transparant_grey_border));
            iv.setImageResource(R.drawable.ic_header_nyelam_white);
            tv.setTextColor(ContextCompat.getColor(this, R.color.ny_grey8));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String data = "tes data";
        Intent intent = new Intent();
        intent.putExtra("MyData", data);
        setResult(RESULT_OK, intent);
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

    }

}
