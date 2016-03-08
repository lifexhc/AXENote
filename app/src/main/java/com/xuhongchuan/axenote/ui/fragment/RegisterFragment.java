package com.xuhongchuan.axenote.ui.fragment;

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
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.impl.IChangeTheme;
import com.xuhongchuan.axenote.ui.dialog.ProgressDialog;
import com.xuhongchuan.axenote.utils.FormatUtils;
import com.xuhongchuan.axenote.utils.GlobalConfig;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class RegisterFragment extends Fragment implements IChangeTheme {
    private Button mBtnRegister;
    private RelativeLayout mRL;

    private EditText mEtMobilePhoneNumber;
    private EditText mEtPwd;
    private EditText mEtSmsCode;
    private Button mBtnGetSmsCode;

    private Prism mPrism;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mBtnRegister = (Button) view.findViewById(R.id.btn_register);
        mEtMobilePhoneNumber = (EditText) view.findViewById(R.id.et_user_name);
        mEtPwd = (EditText) view.findViewById(R.id.et_password);
        mEtSmsCode = (EditText) view.findViewById(R.id.et_sms_code);
        mEtMobilePhoneNumber = (EditText) view.findViewById(R.id.et_user_name);
        mBtnGetSmsCode = (Button) view.findViewById(R.id.btn_get_sms_code);
        mRL = (RelativeLayout) view.findViewById(R.id.rl_fragment_register);

        // 获取手机短信验证码
        mBtnGetSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobilePhoneNumber = mEtMobilePhoneNumber.getText().toString();
                if (FormatUtils.isMobileNum(mobilePhoneNumber)) {
                    AVUser.requestMobilePhoneVerifyInBackground(mobilePhoneNumber, new RequestMobileCodeCallback() {

                        @Override
                        public void done(AVException e) {
                            if (e == null) {

                            } else {

                            }

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 注册
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobilePhoneNumber = mEtMobilePhoneNumber.getText().toString();
                final String pwd = mEtPwd.getText().toString();
                final String smsCode = mEtSmsCode.getText().toString();

                AVUser.verifyMobilePhoneInBackground(smsCode, new AVMobilePhoneVerifyCallback() {

                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            AVUser user = new AVUser();
                            user.setUsername(mobilePhoneNumber);
                            user.setPassword(pwd);
                            user.put("phone", mobilePhoneNumber);

                            final ProgressDialog progressDialog = ProgressDialog.createDialog(getContext());
                            progressDialog.show();

                            user.signUpInBackground(new SignUpCallback() {
                                public void done(AVException e) {
                                    if (e == null) {
                                        // successfully
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // failed
                                    }
                                }
                            });
                        } else {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "短信验证码不正确", Toast.LENGTH_SHORT).show();
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
                .background(mBtnRegister)
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
