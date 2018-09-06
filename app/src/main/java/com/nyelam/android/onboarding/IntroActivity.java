package com.nyelam.android.onboarding;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nyelam.android.R;
import com.nyelam.android.StarterActivity;
import com.nyelam.android.home.HomeActivity;

import me.relex.circleindicator.CircleIndicator;

public class IntroActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private View skipButton;
    private View nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        viewPager = findViewById(R.id.view_pager);
        indicator = findViewById(R.id.circle_indicator);
        skipButton = findViewById(R.id.skip_button);
        nextButton = findViewById(R.id.next_button);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new OnboardingSectionAdapter(getSupportFragmentManager()));
        indicator.setViewPager(viewPager);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem() < 2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                } else {
                    Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public class OnboardingSectionAdapter extends FragmentPagerAdapter {

        public OnboardingSectionAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return IntroFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
