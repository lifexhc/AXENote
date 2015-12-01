package com.xuhongchuan.axenote.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class ContentActivity extends BaseActivity {

    private Toolbar mToolbar;
    private EditText mEtContent;
    private String mContent; // 便签内容
    private int mIndex; // 便签索引

    private void initElement() {
        mContent = getIntent().getStringExtra("content");
        mIndex = getIntent().getIntExtra("index", 0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mEtContent.setText(mContent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        initElement();
        mToolbar.setNavigationIcon(R.mipmap.ic_action_navigation_arrow_back);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.insert_image) { // 插入图片

        }

        return super.onOptionsItemSelected(item);
    }
}
