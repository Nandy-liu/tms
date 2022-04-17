package com.example.tms.ui.adaptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdaptor extends FragmentPagerAdapter {
    ArrayList<String> mTitles;
    ArrayList<Fragment> mFragments;

    public PagerAdaptor(@NonNull FragmentManager fm , ArrayList<Fragment> fragments) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mTitles=new ArrayList<String>();
        mTitles.add("签到");
        mTitles.add("提问");
        mTitles.add("讨论");
        mFragments=fragments;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragments.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
