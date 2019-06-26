package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.adapter.UserInfoFragmentPagerAdapter;
import com.xuhongchuan.axenote.infr.IChangeTheme;
import com.xuhongchuan.axenote.ui.fragment.LoginFragment;
import com.xuhongchuan.axenote.ui.fragment.RegisterFragment;

import butterknife.BindView;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class UserInfoActivity extends BaseActivity implements IChangeTheme {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private UserInfoFragmentPagerAdapter pagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initElement();
        initTab();
    }

    private void initElement() {
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initTab() {
        pagerAdapter = new UserInfoFragmentPagerAdapter(getSupportFragmentManager(), this);
        pagerAdapter.addFragment(new LoginFragment(), getString(R.string.login));
        pagerAdapter.addFragment(new RegisterFragment(), getString(R.string.register));

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }
}
