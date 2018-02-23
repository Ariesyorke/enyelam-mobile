package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.bookinghistory.BookingHistoryDetailActivity;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.Participant;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.storage.NYMasterDataStorage;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FilterListDiveCenterActivity extends BasicActivity implements NYMasterDataStorage.LoadDataListener<Category> {

    private Toolbar toolbar;
    private NYMasterDataStorage storage;
    private LinearLayout categoriesLinearLayout;
    private boolean isSelf = false;
    private ArrayList<String> categories;
    protected String keyword, diverId, diver, certificate, date, type;
    private TextView doneTextView;
    private List<Category> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_dive_center);
        storage = new NYMasterDataStorage(this);
        initExtra();
        initView();
        initToolbar(true);
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
                intent.putExtra(NYHelper.ID_DIVER, diverId);
                intent.putExtra(NYHelper.DIVER, diver);
                intent.putExtra(NYHelper.CERTIFICATE, certificate);
                intent.putExtra(NYHelper.SCHEDULE, date);
                intent.putExtra(NYHelper.TYPE, type);
                intent.putStringArrayListExtra(NYHelper.CATEGORIES, categories);
                startActivity(intent);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        categoriesLinearLayout = (LinearLayout) findViewById(R.id.categories_linearLayout);
        doneTextView = (TextView) findViewById(R.id.done_textView);
    }


    private void initExtra() {
        categories = new ArrayList<>();

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(!extras.getString(NYHelper.KEYWORD).equals(null)){
                keyword = extras.getString(NYHelper.KEYWORD);
            }

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

            LinearLayout.LayoutParams allLayoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            allLayoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(this, 10));
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

            categoriesLinearLayout.addView(allCategoryView);

            int pos = 1;
            for (Category category : items) {

                int position = pos;

                LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View myParticipantsView = inflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

                LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(this, 10));
                myParticipantsView.setLayoutParams(layoutParamsAddons);

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
                }

                categoryCheckBox.setTag(category.getId());

                for (String catId : categories){
                    if (catId.equals(category.getId())){
                        categoryCheckBox.setChecked(true);
                        break;
                    }
                }

                pos++;
                categoriesLinearLayout.addView(myParticipantsView);

            }

        } else {
            NYLog.e("Check Category : NULL");
        }


    }


}
