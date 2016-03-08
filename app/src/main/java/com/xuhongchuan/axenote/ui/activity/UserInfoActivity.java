package com.xuhongchuan.axenote.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.xuhongchuan.axenote.R;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xuhongchuan on 16/3/3.
 */
public class UserInfoActivity extends BaseActivity {

    CircleImageView mCIVUserHeader;
    TextView mTvEmail;
    Button mBtnLogout;

    private final int PICK_HEADER_FROM_FILE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initElement();
    }

    /**
     * 初始化元素
     */
    private void initElement() {
        mCIVUserHeader = (CircleImageView) findViewById(R.id.ci_user_header);
        mTvEmail = (TextView) findViewById(R.id.tv_email);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);

        mCIVUserHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_HEADER_FROM_FILE);
            }
        });

        mTvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, ModifyActivity.class);
                startActivity(intent);
            }
        });

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut(); // 退出登录
                AVUser currentUser = AVUser.getCurrentUser();
                if (currentUser == null) {
                    Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_HEADER_FROM_FILE:
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mCIVUserHeader.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void initTheme() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
