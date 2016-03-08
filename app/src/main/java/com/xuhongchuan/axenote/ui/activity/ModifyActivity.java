package com.xuhongchuan.axenote.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 16/3/5.
 */
public class ModifyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
    }

    @Override
    public void initTheme() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ModifyActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }
}
