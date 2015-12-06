package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class AboutAuthorActivity extends BaseActivity {

    private Toolbar toolbar;

    private void initElement() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);

        initElement();
    }
}
