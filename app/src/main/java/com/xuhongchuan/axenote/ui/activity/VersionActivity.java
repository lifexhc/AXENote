package com.xuhongchuan.axenote.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.util.GlobalConfig;

/**
 * Created by xuhongchuan on 15/10/21.
 */
public class VersionActivity extends BaseActivity {

    private Toolbar mToolbar;
    private Button mBtnCheckUpdate; // 检查更新

    private Prism mPrism; // 主题切换

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

        mBtnCheckUpdate = (Button) findViewById(R.id.btn_check_update);

        /**
         * 检查更新
         */
        mBtnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        initElement();
        initTheme();
    }

    /**
     * 初始化主题
     */
    @Override
    public void initTheme() {
        mPrism = Prism.Builder.newInstance()
                .background(getWindow())
                .background(mToolbar)
                .background(mBtnCheckUpdate)
                .build();
        changeTheme();
    }

    /**
     * 切换主题
     */
    @Override
    public void changeTheme() {
        super.changeTheme();
        Resources res = getResources();
        LinearLayout llVersion = (LinearLayout) findViewById(R.id.ll_version);
        if (GlobalConfig.getInstance().isNightMode(VersionActivity.this)) {
            mPrism.setColour(res.getColor(R.color.divider));
            llVersion.setBackgroundColor(res.getColor(R.color.bg_night));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
            llVersion.setBackgroundColor(res.getColor(R.color.icons));
        }
    }
}
