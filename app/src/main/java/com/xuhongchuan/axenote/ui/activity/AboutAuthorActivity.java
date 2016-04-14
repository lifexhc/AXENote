package com.xuhongchuan.axenote.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.util.GlobalConfig;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class AboutAuthorActivity extends BaseActivity {

    private Toolbar mToolbar;
    private Prism mPrism; // 主题切换

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);

        initElement();
        initTheme();
    }

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
        RelativeLayout rlAboutAuthor = (RelativeLayout) findViewById(R.id.rl_about_author);
        if (GlobalConfig.getInstance().isNightMode(AboutAuthorActivity.this)) {
            mPrism.setColour(res.getColor(R.color.divider));
            rlAboutAuthor.setBackgroundColor(res.getColor(R.color.bg_night));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
            rlAboutAuthor.setBackgroundColor(res.getColor(R.color.icons));
        }
    }
}
