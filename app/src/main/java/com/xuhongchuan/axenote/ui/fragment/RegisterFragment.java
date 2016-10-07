package com.xuhongchuan.axenote.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/12/2.
 */
public class RegisterFragment extends Fragment {
    private Button mBtnRegister;
    private RelativeLayout mRL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mBtnRegister = (Button) view.findViewById(R.id.btn_register);
        mRL = (RelativeLayout) view.findViewById(R.id.rl_fragment_register);
        return view;
    }

}
