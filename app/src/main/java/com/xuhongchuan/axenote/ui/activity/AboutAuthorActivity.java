package com.xuhongchuan.axenote.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xuhongchuan.axenote.R;

import butterknife.BindView;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class AboutAuthorActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ic_author_avatar)
    ImageView avatarView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initElement();
    }

    private void initElement() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

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
