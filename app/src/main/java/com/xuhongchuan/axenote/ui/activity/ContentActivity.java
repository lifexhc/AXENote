package com.xuhongchuan.axenote.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.utils.GlobalDataCache;
import com.xuhongchuan.axenote.utils.GlobalValue;

import java.util.Date;
import com.xuhongchuan.axenote.utils.GlobalConfig;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class ContentActivity extends BaseActivity {

    public static String EXTRA_ID = "id";
    public static String EXTRA_CONTENT = "content";
    public static String EXTRA_CREATE_TIME = "createTime";
    public static String EXTRA_LAST_MODIFIED_TIME = "lastModifiedTime";

    private Toolbar mToolbar;
    private EditText mEtContent;

    private int mId; // 便签ID
    private String mContent; // 便签内容
    private long mCreateTime; // 便签创建时间
    private long mLastModifiedTime; // 便签最后编辑时间

    private Prism mPrism; // 主题切换

    /**
     * 获取传递进来的便签信息
     */
    private void initContent() {
        Intent intent = getIntent();
        mId = intent.getIntExtra(EXTRA_ID, -1);
        mContent = intent.getStringExtra(EXTRA_CONTENT);
        mCreateTime = intent.getLongExtra(EXTRA_CREATE_TIME, 0);
        mLastModifiedTime = intent.getLongExtra(EXTRA_LAST_MODIFIED_TIME, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initElement();
        initTheme();
        initContent();
    }

    /**
     * 初始化元素
     */
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

        mEtContent = (EditText) findViewById(R.id.et_content);
        mEtContent.setText(mContent);
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
        Resources res = getResources();
        EditText etContext = (EditText) findViewById(R.id.et_content);
        if (GlobalConfig.getInstance().isNightMode(ContentActivity.this)) {
            mPrism.setColour(res.getColor(R.color.divider));
            etContext.setBackgroundColor(res.getColor(R.color.bg_night));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
            etContext.setBackgroundColor(res.getColor(R.color.bg_note));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mContent = mEtContent.getText().toString();

        GlobalDataCache.getInstance().updateNote(mId, mContent, new Date().getTime());
        // 发送更新便签列表的广播
        Intent intent = new Intent(GlobalValue.REFRESH_NOTE_LIST);
        sendBroadcast(intent);
    }

    @Override
    public void finish() {
        super.finish();
        mContent = mEtContent.getText().toString();

        GlobalDataCache.getInstance().updateNote(mId, mContent + mId, new Date().getTime());
        // 发送更新便签列表的广播
        Intent intent = new Intent(GlobalValue.REFRESH_NOTE_LIST);
        sendBroadcast(intent);
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
