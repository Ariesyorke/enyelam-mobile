package com.nyelam.android.doshop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.doshoporderhistory.DoShopOrderHistoryActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopHomepageRequest;
import com.nyelam.android.http.NYDoShopMasterCategoryRequest;
import com.nyelam.android.storage.DoShopCategoryStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class DoShopActivity extends AppCompatActivity {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private DoShopAdapter doShopAdapter;
    private ArrayList<Object> objects = new ArrayList<>();

    private DoShopMenuCategoryAdapter menuCategoryAdapter;


    @BindView(R.id.list_view_menu)
    ListView listViewMenu;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.el_category)
    ExpandableLayout expandableLayout;

    @OnClick(R.id.iv_menu_categories) void openMenu(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.et_search) void openSearchItem(){
        DoShopCategory cat = new DoShopCategory();
        cat.setId("1");
        cat.setName("Scuba Gear 1");
        Intent intent = new Intent(context, DoShopCategoryActivity.class);
        intent.putExtra(NYHelper.CATEGORY, cat.toString());
        startActivity(intent);
    }

    @OnClick(R.id.iv_cart) void intentToCart(){
        drawerLayout.closeDrawer(Gravity.START);
        startActivity(new Intent(context, DoShopCheckoutActivity.class));
    }

    @OnClick(R.id.tv_menu_order_history) void intentToOrderHistory(){
        drawerLayout.closeDrawer(Gravity.START);
        startActivity(new Intent(context, DoShopOrderHistoryActivity.class));
    }

    @OnClick(R.id.tv_menu_cart) void intentToCart2(){
        drawerLayout.closeDrawer(Gravity.START);
        startActivity(new Intent(context, DoShopCheckoutActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop);
        ButterKnife.bind(this);
        context = this;

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressBar);

        initHomepage();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshs();
            }
        });

        // TODO: load menu
        DoShopCategoryStorage storage = new DoShopCategoryStorage(DoShopActivity.this);
        if (storage != null && storage.getCategoryList() != null){
            menuCategoryAdapter = new DoShopMenuCategoryAdapter( DoShopActivity.this, storage.getCategoryList().getList());
            menuCategoryAdapter.notifyDataSetChanged();
            listViewMenu.setAdapter(menuCategoryAdapter);
        }

        menuCategoryAdapter = new DoShopMenuCategoryAdapter(this);
        listViewMenu.setAdapter(menuCategoryAdapter);
        loadCategories();
    }

    private void initHomepage(){
        NYDoShopHomepageRequest req = new NYDoShopHomepageRequest(context);
        spcMgr.execute(req, onHomepageRequest());
    }

    private RequestListener<DoShopList> onHomepageRequest() {
        return new RequestListener<DoShopList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(context, spiceException, null);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onRequestSuccess(DoShopList doShopList) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                objects.add(doShopList.getCategories());
                objects.add(doShopList.getRecommended());

                final GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

                    @Override
                    public int getSpanSize(int position) {
                        switch (doShopAdapter.getItemViewType(position)){
                            case DoShopAdapter.VIEW_TYPE_CATEGORY:
                                return 2 ;
                            case DoShopAdapter.VIEW_TYPE_RECOMMENDED:
                                return 2;
                            default:
                                return 2;
                        }
                    }
                });

                recyclerView.setLayoutManager(layoutManager);

                doShopAdapter = new DoShopAdapter(context, objects);
                doShopAdapter.addModules(doShopList);
                recyclerView.setAdapter(doShopAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }
        };
    }

    private void loadCategories(){
        NYDoShopMasterCategoryRequest req = new NYDoShopMasterCategoryRequest(context);
        spcMgr.execute(req, onCategoryRequest());
    }

    private RequestListener<DoShopCategoryList> onCategoryRequest() {
        return new RequestListener<DoShopCategoryList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(context, spiceException, null);

            }

            @Override
            public void onRequestSuccess(DoShopCategoryList categoryList) {
                progressBar.setVisibility(View.GONE);

                if (categoryList != null){
                    DoShopCategoryStorage storage = new DoShopCategoryStorage(DoShopActivity.this);
                    storage.setCategoryList(categoryList);

                    menuCategoryAdapter = new DoShopMenuCategoryAdapter( DoShopActivity.this, categoryList.getList());
                    menuCategoryAdapter.notifyDataSetChanged();
                    listViewMenu.setAdapter(menuCategoryAdapter);

                    setCategory(categoryList.getList());
                }

            }
        };
    }

    private void setCategory(List<DoShopCategory> categories) {

        expandableLayout.setRenderer(new ExpandableLayout.Renderer<String, DoShopCategory>() {
            @Override
            public void renderParent(View view, String model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tv_category)).setText(model);
            }

            @Override
            public void renderChild(View view, final DoShopCategory cat, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tv_category)).setText(cat.getName());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cat != null && NYHelper.isStringNotEmpty(cat.getId())){
                            Intent intent = new Intent(context, DoShopCategoryActivity.class);
                            intent.putExtra(NYHelper.CATEGORY, cat.toString());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Sorry, this category is not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        Section<String, DoShopCategory> section = new Section<>();
        //defaut is false
        section.expanded = true;
        section.parent = "Categories";
        for (DoShopCategory category : categories){
            section.children.add(category);
        }

        expandableLayout.addSection(section);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onRefreshs(){
        swipeRefreshLayout.setRefreshing(true);
        objects = new ArrayList<>();
        initHomepage();
    }
}
