package com.nyelam.android.dodive;

import android.content.Context;
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

import java.util.List;

public class FilterListDiveCenterActivity extends BasicActivity implements NYMasterDataStorage.LoadDataListener<Category> {

    private Toolbar toolbar;
    private NYMasterDataStorage storage;
    private LinearLayout categoriesLinearLayout;
    private boolean isSelf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_dive_center);
        storage = new NYMasterDataStorage(this);
        initView();
        initToolbar(true);
        getCategories();
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
            NYLog.e("Check Category : "+items.toString());

            categoriesLinearLayout.removeAllViews();





            LayoutInflater allInflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View allCategoryView = allInflaterAddons.inflate(R.layout.view_item_category, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams allLayoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            allLayoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(this, 10));
            allCategoryView.setLayoutParams(allLayoutParamsAddons);

            ImageView allCategoryImageView = (ImageView) allCategoryView.findViewById(R.id.category_imageView);
            TextView allCategoryNameTextView = (TextView) allCategoryView.findViewById(R.id.category_name_textView);
            AppCompatCheckBox allCategoryCheckBox = (AppCompatCheckBox) allCategoryView.findViewById(R.id.category_appCompatCheckBox);
            allCategoryNameTextView.setText("All");
            allCategoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(FilterListDiveCenterActivity.this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();

                    for (int i = 1; i < categoriesLinearLayout.getChildCount(); i++) {
                        LinearLayout view = (LinearLayout) categoriesLinearLayout.getChildAt(i);

                        ImageView imageView = (ImageView) view.getChildAt(0);
                        TextView textView = (TextView) view.getChildAt(1);
                        AppCompatCheckBox checkBox = (AppCompatCheckBox) view.getChildAt(2);

                        checkBox.setChecked(isChecked);
                    }

                }
            });
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


                pos++;
                categoriesLinearLayout.addView(myParticipantsView);

            }

        } else {
            NYLog.e("Check Category : NULL");
        }


    }


}
