package com.nyelam.android;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.home.HomeActivity;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class OnBoardingActivity extends BasicActivity {

    FragmentPagerAdapter adapterViewPager;
    ViewPager vpPager;
    InkPageIndicator inkPageIndicator;
    TextView skipTextView;
    TextView nextTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        skipTextView = (TextView) findViewById(R.id.skip_textView);
        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnBoardingActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        vpPager = (ViewPager) findViewById(R.id.view_pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //_position = position;
                switch (position) {
                    case 2:
//                        inkPageIndicator.setVisibility(View.GONE);
//                        inkPageIndicatorTwo.setVisibility(View.VISIBLE);
                        break;
                    default:
//                        inkPageIndicatorTwo.setVisibility(View.GONE);
//                        inkPageIndicator.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);



        nextTextView = (TextView) findViewById(R.id.next_textView);
        nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = vpPager.getCurrentItem();
                if (current+1 < adapterViewPager.getCount()){
                    vpPager.setCurrentItem(current+1);
                } else {
                    Intent intent = new Intent(OnBoardingActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

//    @Override
//    public void intentToMainActivity() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    public void intentToAuthActivity() {
//        Intent intent = new Intent(this, AuthActivity.class);
//        startActivity(intent);
//    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return OnBoardingFragment.newInstance(0, R.drawable.onboarding_dodive);
                case 1: // Fragment # 1 - This will show FirstFragment different title
                    return OnBoardingFragment.newInstance(1, R.drawable.onboarding_dotrip);
                case 2: // Fragment # 2 - This will show SecondFragment
                    return OnBoardingFragment.newInstance(2, R.drawable.onboarding_docourse);
                case 3: // Fragment # 3 - This will show Auth and Login Fragment
                    return OnBoardingFragment.newInstance();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}