package com.xuhongchuan.axenote.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.dao.NoteDao;
import com.xuhongchuan.axenote.util.GlobalValue;

import java.util.Date;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class ContentActivity extends BaseActivity {

    private Toolbar mToolbar;
    private EditText mEtContent;

    private int mID; // 便签ID
    private String mContent; // 便签内容
    private long mCreateTime; // 便签创建时间
    private long mUpdateTime; // 便签最后编辑时间

    private int mIndex; // 便签索引

    /**
     * 获取传递进来的便签信息
     */
    private void initContent() {
        Intent intent = getIntent();
        mID = intent.getIntExtra("id", -1);
        mContent = intent.getStringExtra("content");
        mCreateTime = intent.getLongExtra("createTime", 0);
        mUpdateTime = intent.getLongExtra("updateTime", 0);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initContent();
        initElement();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mContent = mEtContent.getText().toString();
        // 保存便签更新到数据库
        NoteDao dao = NoteDao.getInstance();
        dao.updateNote(mID, mContent, new Date().getTime());
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
