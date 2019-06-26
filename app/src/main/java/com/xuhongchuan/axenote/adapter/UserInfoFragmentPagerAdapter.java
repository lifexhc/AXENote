package com.xuhongchuan.axenote.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class UserInfoFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;

    private Context mContext;
    private List<Fragment> mFragment = new ArrayList<>();
    private List<String> mFragTitles = new ArrayList<>();

    public UserInfoFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    public void addFragment(Fragment frag, String title) {
        mFragment.add(frag);
        mFragTitles.add(title);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragTitles.get(position);
    }

}
