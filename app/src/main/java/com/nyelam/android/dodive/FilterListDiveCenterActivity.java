package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.flowlayout.FlowLayout;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.bookinghistory.BookingHistoryDetailActivity;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.data.Participant;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.NYMasterDataStorage;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.corekit.utilities.Utils.dpToPx;

public class FilterListDiveCenterActivity extends BasicActivity implements NYMasterDataStorage.LoadDataListener<Category> {

    private NYMasterDataStorage storage;
    private LinearLayout categoriesLinearLayout;
    private boolean isSelf = false;
    private ArrayList<String> categories;
    protected String keyword, diverId, diver, certificate, date, type;
    private boolean ecotrip;
    private TextView doneTextView;
    private List<Category> items;
    private List<Category> categoryChooseList;
    private List<Facilities> facilitiesChooseList;
    private FlowLayout categoryFlowLayout;
    private FlowLayout facilitiesFlowLayout;
    private ImageView closeImageView;
    private String[] facilitiesTexts;
    private int[] facilitiesDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_dive_center);
        storage = new NYMasterDataStorage(this);
        initExtra();
        initView();
        //initToolbar(false);
        getCategories();
        initControl();
    }

    private void initControl() {
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

                Intent intent = new Intent(FilterListDiveCenterActivity.this, DoDiveSearchResultActivity.class);
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
    }


    private void initExtra() {
        categories = new ArrayList<>();

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
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

                /*LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(this, 10));
                myParticipantsView.setLayoutParams(layoutParamsAddons);*/

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
            //TextView textView = buildLabel(text);
            //facilitiesFlowLayout.addView(textView);
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

    /*private TextView buildLabel(int pos) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setPadding((int)dpToPx(16), (int)dpToPx(8), (int)dpToPx(16), (int)dpToPx(8));
        textView.setBackgroundResource(R.drawable.ny_rectangle_orange);
        return textView;
    }*/


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


}
