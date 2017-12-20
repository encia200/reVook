package com.jy.revook_1111.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jy.revook_1111.Adapter.TabAdapter;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.R;
import com.jy.revook_1111.model.UserModel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int TAB_COUNT = 5;
    private FirebaseAuth firebaseAuth;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
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

        Intent intent = getIntent();
        ApplicationController.currentUser = new UserModel();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ApplicationController.currentUser = dataSnapshot.getValue(UserModel.class);
                ApplicationController.initFollowers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseReference.addValueEventListener(databaseListener);
        /*ApplicationController.currentUser.email = databaseReference.child("email").getKey();
        ApplicationController.currentUser.password = databaseReference.child("password").getKey();
        ApplicationController.currentUser.profileImageUrl = databaseReference.child("profileImageUrl").getKey();
        ApplicationController.currentUser.userName = databaseReference.child("userName").getKey();*/
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
        passPushTokenToServer();
    }

    void passPushTokenToServer()
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
    }
}