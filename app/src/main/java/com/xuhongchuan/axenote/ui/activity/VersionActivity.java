package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xuhongchuan.axenote.BuildConfig;
import com.xuhongchuan.axenote.R;

import butterknife.BindView;

/**
 * Created by xuhongchuan on 15/10/21.
 */
public class VersionActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_version)
    TextView versionView;
    @BindView(R.id.ic_logo)
    ImageView logoView;

    private void initElement() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        versionView.setText(String.format(getResources().getString(R.string.version_number), BuildConfig.VERSION_NAME));

        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .apply(new RequestOptions().circleCrop())
                .into(logoView);
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
