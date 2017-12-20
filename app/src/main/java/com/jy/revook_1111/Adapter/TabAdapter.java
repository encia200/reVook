package com.jy.revook_1111.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jy.revook_1111.Fragment.BookSearchFragment;
import com.jy.revook_1111.Fragment.HomeFragment;
import com.jy.revook_1111.Fragment.NotifyFragment;
import com.jy.revook_1111.Fragment.PeopleFragment;
import com.jy.revook_1111.Fragment.ReviewFragment;
import com.jy.revook_1111.Fragment.SearchFragment;
import com.jy.revook_1111.Fragment.UserInfoFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public TabAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        BookSearchFragment.isSearching = false;
        switch (position) {
            case 0:
                SearchFragment searchFragment = new SearchFragment();
                return searchFragment;
            case 1:
                ReviewFragment reviewFragment = new ReviewFragment();
                return reviewFragment;
            case 2:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 3:
                PeopleFragment peopleFragment = new PeopleFragment();
                return peopleFragment;
            case 4:
                UserInfoFragment userInfoFragment = new UserInfoFragment();
                return userInfoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
