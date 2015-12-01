package com.xuhongchuan.axenote.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/11/28.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activity切换效果
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
