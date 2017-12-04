package com.jy.revook_1111.Activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jy.revook_1111.R;
import com.jy.revook_1111.Adapter.TabAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int TAB_COUNT = 5;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayout.Tab tab;

    private int[] tabIcons = {
            R.drawable.ic_search_black_24dp,
            R.drawable.review,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_notifications_black_24dp,
            R.drawable.userinfo
    };
    private int[] selected_tabIcons = {
            R.drawable.selected_ic_search_black_24dp,
            R.drawable.selected_review,
            R.drawable.selected_ic_home_black_24dp,
            R.drawable.selected_ic_notifications_black_24dp,
            R.drawable.selected_userinfo
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.main_tab);
        tabLayout.setSelectedTabIndicatorColor(0xff009EFF);
        tabLayout.setTabTextColors(0xffffffff, 0xffffffff);

        tab = tabLayout.newTab();
        tab.setIcon(selected_tabIcons[0]);
        tabLayout.addTab(tab);

        for (int i = 1; i < TAB_COUNT; i++) {
            tab = tabLayout.newTab();
            tab.setIcon(tabIcons[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        // Creating TabPagerAdapter adapter
        TabAdapter pagerAdapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.setIcon(selected_tabIcons[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.setIcon(tabIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
}