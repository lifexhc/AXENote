package com.xuhongchuan.axenote.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class ContentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etContent;
    private String content; // 便签内容
    private int index; // 便签索引

    private void initElement() {
        content = getIntent().getStringExtra("content");
        index = getIntent().getIntExtra("index", 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etContent = (EditText) findViewById(R.id.et_content);
        etContent.setText(content);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        initElement();
        toolbar.setNavigationIcon(R.mipmap.ic_action_navigation_arrow_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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
