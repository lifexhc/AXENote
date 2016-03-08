package com.xuhongchuan.axenote.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.impl.IChangeTheme;
import com.xuhongchuan.axenote.ui.activity.UserInfoActivity;
import com.xuhongchuan.axenote.ui.dialog.ProgressDialog;
import com.xuhongchuan.axenote.utils.GlobalConfig;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class LoginFragment extends Fragment implements IChangeTheme {
    private EditText mEtMobilePhoneNumber;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private RelativeLayout mRL;

    private Prism mPrism;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEtMobilePhoneNumber = (EditText) view.findViewById(R.id.et_mobile_phone_number);
        mEtPassword = (EditText) view.findViewById(R.id.et_password);
        mBtnLogin = (Button) view.findViewById(R.id.btn_login);
        mRL = (RelativeLayout) view.findViewById(R.id.rl_fragment_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobilePhoneNumber = mEtMobilePhoneNumber.getText().toString();
                final String pwd = mEtPassword.getText().toString();

                final ProgressDialog progressDialog = ProgressDialog.createDialog(getContext());
                progressDialog.show();

                AVUser.logInInBackground(mobilePhoneNumber, pwd, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        initTheme();
        return view;
    }

    /**
     * 初始化主题
     */
    @Override
    public void initTheme() {
        mPrism = Prism.Builder.newInstance()
                .background(mBtnLogin)
                .build();
        changeTheme();
    }

    /**
     * 修改主题
     */
    @Override
    public void changeTheme() {
        Resources res = getResources();
        if (GlobalConfig.getInstance().isNightMode(getContext())) {
            mPrism.setColour(res.getColor(R.color.divider));
            mRL.setBackgroundColor(res.getColor(R.color.bg_night));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
            mRL.setBackgroundColor(res.getColor(R.color.icons));
        }
    }
}
