package com.nyelam.android.doshop;

import android.content.Context;
import android.content.Intent;
import android.os.storage.StorageManager;
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
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.CategoryList;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.doshoporder.DoShopCheckoutActivity;
import com.nyelam.android.doshoporderhistory.DoShopOrderHistoryActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.http.NYDoShopHomepageRequest;
import com.nyelam.android.http.NYDoShopMasterCategoryRequest;
import com.nyelam.android.storage.DoShopCategoryStorage;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.NYBannerViewPager;
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
import me.relex.circleindicator.CircleIndicator;

public class DoShopActivity extends AppCompatActivity {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);

    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private DoShopAdapter doShopAdapter;
    private ArrayList<Object> objects = new ArrayList<>();

    private DoShopMenuCategoryAdapter menuCategoryAdapter;
    private String tempState = null;

    @BindView(R.id.banner_view_pager)
    NYBannerViewPager bannerViewPager;

    @BindView(R.id.banner_progress_bar)
    ProgressBar bannerProgressBar;

    @BindView(R.id.circle_indicator)
    CircleIndicator circleIndicator;

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
//        DoShopCategory cat = new DoShopCategory();
//        cat.setId("1");
//        cat.setName("Scuba Gear 1");
        Intent intent = new Intent(context, DoShopCategoryActivity.class);
//        intent.putExtra(NYHelper.CATEGORY, cat.toString());
        startActivity(intent);
    }

    @OnClick(R.id.iv_cart) void intentToCart(){
        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin()){
            drawerLayout.closeDrawer(Gravity.START);
            startActivity(new Intent(context, DoShopCheckoutActivity.class));
        } else {
            drawerLayout.closeDrawer(Gravity.START);
            tempState = "cart";
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, NYHelper.LOGIN_REQ);
        }
    }

    @OnClick(R.id.tv_menu_order_history) void intentToOrderHistory(){
        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin()){
            drawerLayout.closeDrawer(Gravity.START);
            startActivity(new Intent(context, DoShopOrderHistoryActivity.class));
        } else {
            drawerLayout.closeDrawer(Gravity.START);
            tempState = "order_history";
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, NYHelper.LOGIN_REQ);
        }
    }

    @OnClick(R.id.tv_menu_cart) void intentToCart2(){
        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin()){
            drawerLayout.closeDrawer(Gravity.START);
            startActivity(new Intent(context, DoShopCheckoutActivity.class));
        } else {
            drawerLayout.closeDrawer(Gravity.START);
            tempState = "cart";
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, NYHelper.LOGIN_REQ);
        }
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

        initBanner();
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

        loadCategoriesStorage();
    }

    private void loadCategoriesStorage() {
        DoShopCategoryStorage storage = new DoShopCategoryStorage(DoShopActivity.this);
        if (storage != null && storage.getCategoryList() != null && storage.getCategoryList().getList() != null
                && storage.getCategoryList().getList().size() > 0)setMenuCategories(storage.getCategoryList());
        loadCategories();
    }

    private void initBanner() {
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getSupportFragmentManager());
        bannerViewPager.setAdapter(bannerViewPagerAdapter);
        circleIndicator.setViewPager(bannerViewPager);

        //craete TEMP data banner
        progressBar.setVisibility(View.VISIBLE);
        BannerList bannerList = new BannerList();
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner("1", "drawable://" + String.valueOf(R.drawable.banner_1), "captio", null));
        banners.add(new Banner("2", "drawable://" + String.valueOf(R.drawable.banner_2), "captio", null));
        banners.add(new Banner("3", "drawable://" + String.valueOf(R.drawable.banner_3), "captio", null));
        bannerList.setList(banners);
        bannerViewPagerAdapter.setBannerList(bannerList);
        bannerViewPagerAdapter.notifyDataSetChanged();
        bannerViewPager.setOffscreenPageLimit(bannerList.getList().size());
        circleIndicator.setViewPager(bannerViewPager);
        progressBar.setVisibility(View.GONE);
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

                setMenuCategories(categoryList);

            }
        };
    }


    private void setMenuCategories(DoShopCategoryList categoryList){
        if (categoryList != null && categoryList.getList() !=null){
            DoShopCategoryStorage storage = new DoShopCategoryStorage(DoShopActivity.this);
            storage.setCategoryList(categoryList);

            menuCategoryAdapter = new DoShopMenuCategoryAdapter( DoShopActivity.this, categoryList.getList());
            menuCategoryAdapter.notifyDataSetChanged();
            listViewMenu.setAdapter(menuCategoryAdapter);

            setCategory(categoryList.getList());
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NYHelper.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                //triggerBook = true;
                if (NYHelper.isStringNotEmpty(tempState) && tempState.equals("cart")){
                    startActivity(new Intent(this, DoShopCheckoutActivity.class));
                } else if (NYHelper.isStringNotEmpty(tempState) && tempState.equals("order_history")){
                    startActivity(new Intent(this, DoShopOrderHistoryActivity.class));
                }
            }
        } else {
            //Toast.makeText(this, "hallo", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
