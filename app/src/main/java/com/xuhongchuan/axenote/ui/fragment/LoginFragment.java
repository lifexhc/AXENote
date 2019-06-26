package com.xuhongchuan.axenote.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class LoginFragment extends Fragment {
    private Button mBtnLogin;
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
        return view;
    }

}
