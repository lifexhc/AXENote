package com.xuhongchuan.axenote.ui.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.impl.IChangeTheme;
import com.xuhongchuan.axenote.util.GlobalConfig;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class LoginFragment extends Fragment implements IChangeTheme {
    private Button mBtnLogin;
    private Prism mPrism;
    private RelativeLayout mRL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mBtnLogin = (Button) view.findViewById(R.id.btn_login);
        mRL = (RelativeLayout) view.findViewById(R.id.rl_fragment_login);
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
