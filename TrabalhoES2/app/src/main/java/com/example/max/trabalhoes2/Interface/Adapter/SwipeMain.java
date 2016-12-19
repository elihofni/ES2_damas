package com.example.max.trabalhoes2.Interface.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Max on 17/12/2016.
 */

public class SwipeMain extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private int count;


    public SwipeMain(FragmentManager fm, List<Fragment> fragments, int count) {
        super(fm);
        this.fragments = fragments;
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return count;
    }
}