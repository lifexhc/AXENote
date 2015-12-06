package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.adapter.UserInfoFragmentPagerAdapter;
import com.xuhongchuan.axenote.ui.fragment.LoginFragment;
import com.xuhongchuan.axenote.ui.fragment.RegisterFragment;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class UserInfoActivity extends FragmentActivity {

    private Toolbar mToolbar;
    private UserInfoFragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initTab();
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
}
