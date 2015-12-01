package com.xuhongchuan.axenote.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.util.L;

/**
 * Created by xuhongchuan on 15/10/21.
 */
public class VersionActivity extends BaseActivity {

    private Button btnCheckUpdate;
    private Toolbar toolbar;

    private void initElement() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d(this, "back");
                onBackPressed();
            }
        });

        btnCheckUpdate = (Button) findViewById(R.id.btn_check_update);

        /**
         * 检查更新
         */
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
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
