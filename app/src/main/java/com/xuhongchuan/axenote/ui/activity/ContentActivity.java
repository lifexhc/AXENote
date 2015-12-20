package com.xuhongchuan.axenote.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.util.GlobalConfig;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class ContentActivity extends BaseActivity {

    private Toolbar mToolbar;
    private EditText mEtContent;
    private String mContent; // 便签内容
    private int mIndex; // 便签索引

    private Prism mPrism; // 主题切换

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initElement();
        initTheme();
    }

    /**
     * 初始化元素
     */
    private void initElement() {
        mEtContent = (EditText) findViewById(R.id.et_content);
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
    public void initTheme() {
        mPrism = Prism.Builder.newInstance()
                .background(getWindow())
                .background(mToolbar)
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
        EditText etContext = (EditText) findViewById(R.id.et_content);
        if (GlobalConfig.getInstance().isNight(ContentActivity.this)) {
            mPrism.setColour(res.getColor(R.color.divider));
            etContext.setBackgroundColor(res.getColor(R.color.bg_night));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
            etContext.setBackgroundColor(res.getColor(R.color.bg_note));
        }
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
