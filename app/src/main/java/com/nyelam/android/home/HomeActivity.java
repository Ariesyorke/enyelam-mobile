package com.nyelam.android.home;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.bookinghistory.BookingHistoryCompletedFragment;
import com.nyelam.android.bookinghistory.BookingHistoryInprogressFragment;
import com.nyelam.android.view.NYCustomViewPager;
import com.nyelam.android.view.NYHomepageTabItemView;
import com.nyelam.android.view.NYMenuDrawerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BasicActivity implements HomeFragment.OnFragmentInteractionListener,
        SocmedFragment.OnFragmentInteractionListener,
        TransactionFragment.OnFragmentInteractionListener,
        MyAccountFragment.OnFragmentInteractionListener,
        NYMenuDrawerFragment.OnFragmentInteractionListener,
        BookingHistoryInprogressFragment.OnFragmentInteractionListener,
        BookingHistoryCompletedFragment.OnFragmentInteractionListener{

    private List<Frags> tags = Arrays.asList(new Frags(0,"home"), new Frags(1,"timeline"), new Frags(2,"interest"), new Frags(3,"tags"), new Frags(4,"more"));
    private List<Frags> fragses = new ArrayList<>();

    private ImageView menuItemImageView;
    private Toolbar toolbar, statusToolbar;
    private NYMenuDrawerFragment menuDrawerFragment;
    private DrawerLayout drawerLayout;
    public View loadingView;
    private LinearLayout tabManager;
    private NYCustomViewPager viewPager;
    private int checkedId = -1;
    private boolean mProtectFromCheckedChange = false;
    private boolean mProtectFromPagerChange = false;
    private String [] tabMenu;
    private TextView title;
    private View viewTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initTab();
        initControl();
    }

    private void initControl() {
        menuItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuDrawer();
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabManager = (LinearLayout) findViewById(R.id.tab_manager);
        viewPager = (NYCustomViewPager) findViewById(R.id.view_pager);
        title = (TextView)findViewById(R.id.title);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewTabManager = (View) findViewById(R.id.view_tab_manager);
        menuItemImageView = (ImageView) findViewById(R.id.menu_item_imageView);
    }



    public static class NYFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int FRAGMENT_COUNT = 4;
        private Map<Integer, String> fragmentTags;
        private FragmentManager fragmentManager;

        public NYFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragmentManager = fm;
            this.fragmentTags = new HashMap<>();
        }

        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }

        public Fragment getFragment(int position) {
            String tag = fragmentTags.get(position);
            if (tag == null) {
                return null;
            }
            Bundle b = new Bundle();
            fragmentManager.putFragment(b, tag, fragmentManager.findFragmentByTag(tag));
            return fragmentManager.findFragmentByTag(tag);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                HomeFragment fragment = HomeFragment.newInstance();
                return fragment;
            } else if (position == 1) {
                SocmedFragment fragment = SocmedFragment.newInstance();
                return fragment;
            }  else if (position == 2) {
                TransactionFragment fragment = TransactionFragment.newInstance();
                return fragment;
            } else {
                MyAccountFragment fragment = MyAccountFragment.newInstance();
                return fragment;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);

            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                fragmentTags.put(position, tag);
            }

            return obj;
        }
    }


    public void movePagerToTabItemPosition(int tabItemPosition) {

        mProtectFromPagerChange = true;
        if (tabItemPosition > -1) {
            viewPager.setCurrentItem(tabItemPosition, true);
            title.setText(tabMenu[tabItemPosition]);
        }
        mProtectFromPagerChange = false;
    }

    public int onCheckedChanged(NYHomepageTabItemView view, boolean checked) {
        if (mProtectFromCheckedChange) {
            return -1;
        }

        mProtectFromCheckedChange = true;

        int checkedTabItemPosition = -1;

        for (int i = 0; i < tabManager.getChildCount(); i++) {
            View child = tabManager.getChildAt(i);
            if (child instanceof NYHomepageTabItemView) {
                if (child == view) {
                    setCheckedStateForTab(child.getId(), checked);
                    checkedTabItemPosition = ((NYHomepageTabItemView) child).getTabItemPosition();
                } else {
                    setCheckedStateForTab(child.getId(), false);
                }
            }
        }

        mProtectFromCheckedChange = false;
        return checkedTabItemPosition;
    }

    private void setCheckedId(int id) {
        checkedId = id;
    }

    private void setCheckedStateForTab(int id, boolean checked) {
        View checkedView = tabManager.findViewById(id);
        if (checkedView != null && checkedView instanceof Checkable) {
            ((Checkable) checkedView).setChecked(checked);
        }
    }

    public int getLastKey() {
        int lastKey = 0;
        for (Frags entry : fragses) {
            lastKey = entry.getPosition();
        }
        return lastKey;
    }

    public void addFragmentPosition(int pos) {
        if (fragses.contains(tags.get(pos))) {
            fragses.remove(tags.get(pos));
            fragses.add(tags.get(pos));
            /*for (String entry : datas) {
                if (entry.equals(String.valueOf(pos))) {
                    datas.remove(entry);
                }
            }    */
        } else {
            fragses.add(tags.get(pos));
        }

    }

    public void removeLastFragment(int pos){
        Frags oldName = tags.get(pos);
        for ( int i = fragses.size() - 1;  i >= 0; i--){
            if(oldName.equals(fragses.get(i)))
            {
                fragses.remove(i);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (getLastKey() == 0){
            finish();
        } else if (fragses.size() > 1) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                super.onBackPressed();
            } else {
                removeLastFragment(getLastKey());
                viewPager.setCurrentItem(getLastKey());
            }
        } else {
            finish();
        }
    }


    public class Frags {

        private int position;
        private String tag;

        public Frags(int pos, String tag) {
            this.position = pos;
            this.tag = tag;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void closeDrawer(){
            //close navigation drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    private void initTab() {
        tabMenu = getResources().getStringArray(R.array.menu_tab);
        viewTabManager.setAlpha(0.5f);
        //NavDrawer
        setSupportActionBar(toolbar);


        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new NYFragmentPagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                //KTLog.e("CEK 1");
                //Toast.makeText(MainActivity.this, "test 1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //KTLog.e("CEK 2");
                //Always
                //allFragments.contains(KTFragmentPagerAdapter.class.);
                /*if (stack.contains(position)) {
                    stack.pop(position);
                }*/
                //Toast.makeText(MainActivity.this, "test 2 "+String.valueOf(stack.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                //KTLog.e("CEK 3");
                fragses.add(tags.get(position));

                if (mProtectFromPagerChange) {
                    return;
                }

                NYHomepageTabItemView view = (NYHomepageTabItemView) tabManager.getChildAt(position);
                onCheckedChanged(view, true);
                Fragment fragment = ((NYFragmentPagerAdapter) viewPager.getAdapter()).getFragment(position);
                if (position == 0 && fragment != null) {
                    fragment.onResume();
                }

            }
        });

        for (int i = 0; i < tabManager.getChildCount(); i++) {
            View child = tabManager.getChildAt(i);
            if (child instanceof NYHomepageTabItemView) {
                ((NYHomepageTabItemView) child).setMainActivity(this);
            }
        }

    }

    public void openMenuDrawer(){
        drawerLayout.openDrawer(Gravity.END);
    }

    @Override
    public void onHome() {

    }

}
