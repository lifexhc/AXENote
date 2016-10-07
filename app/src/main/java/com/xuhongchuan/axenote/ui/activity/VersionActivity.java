package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xuhongchuan.axenote.BuildConfig;
import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/10/21.
 */
public class VersionActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTvVersion;

    private void initElement() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvVersion.setText(getResources().getString((R.string.version_number)) + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initElement();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_version;
    }
}
