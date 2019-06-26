package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class AboutAuthorActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initElement();
    }

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

        ImageView avatarView = (ImageView) findViewById(R.id.ic_author_avatar);
        Glide.with(this)
                .load(R.drawable.xuhongchuan)
                .apply(new RequestOptions().circleCrop())
                .into(avatarView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_author;
    }
}
