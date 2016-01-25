package com.xuhongchuan.axenote.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.adapter.UserInfoFragmentPagerAdapter;
import com.xuhongchuan.axenote.infr.IChangeTheme;
import com.xuhongchuan.axenote.ui.fragment.LoginFragment;
import com.xuhongchuan.axenote.ui.fragment.RegisterFragment;
import com.xuhongchuan.axenote.utils.GlobalConfig;
import com.xuhongchuan.axenote.utils.GlobalValue;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class UserInfoActivity extends FragmentActivity implements IChangeTheme{

    private Toolbar mToolbar;
    private UserInfoFragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private Prism mPrism; // 主题切换

    /**
     * 广播
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GlobalValue.CHANGE_THEME)) {
                changeTheme();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalValue.CHANGE_THEME);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initElement();
        initTab();
        initTheme();
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

    /**
     * 初始化主题
     */
    @Override
    public void initTheme() {
        mPrism = Prism.Builder.newInstance()
                .background(getWindow())
                .background(mToolbar)
                .build();
        changeTheme();
    }

    /**
     * 修改主题
     */
    @Override
    public void changeTheme() {
        Resources res = getResources();
        if (GlobalConfig.getInstance().isNightMode(UserInfoActivity.this)) {
            mPrism.setColour(res.getColor(R.color.divider));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
        }
    }
}
