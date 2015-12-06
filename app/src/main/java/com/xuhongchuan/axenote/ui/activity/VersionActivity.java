package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/10/21.
 */
public class VersionActivity extends BaseActivity {

    private Button mBtnCheckUpdate;
    private Toolbar mToolbar;

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
    }
}
