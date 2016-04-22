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
import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.infr.IChangeTheme;
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

            }
        });

        // 注册
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                .background(mBtnGetSmsCode)
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
            mRL.setBackgroundColor(res.getColor(R.color.white));
        }
    }
}
