package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

        ImageView avatarView = (ImageView) findViewById(R.id.ic_logo);
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .apply(new RequestOptions().circleCrop())
                .into(avatarView);
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
