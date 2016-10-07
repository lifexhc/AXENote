package com.xuhongchuan.axenote.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.adapter.UserInfoFragmentPagerAdapter;
import com.xuhongchuan.axenote.infr.IChangeTheme;
import com.xuhongchuan.axenote.ui.fragment.LoginFragment;
import com.xuhongchuan.axenote.ui.fragment.RegisterFragment;
import com.xuhongchuan.axenote.utils.GlobalValue;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class UserInfoActivity extends BaseActivity implements IChangeTheme {

    private Toolbar mToolbar;
    private UserInfoFragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initElement();
        initTab();
    }

    private void initElement() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initTab() {
        mPagerAdapter = new UserInfoFragmentPagerAdapter(getSupportFragmentManager(), this);
        mPagerAdapter.addFragment(new LoginFragment(), getString(R.string.login));
        mPagerAdapter.addFragment(new RegisterFragment(), getString(R.string.register));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }
}
