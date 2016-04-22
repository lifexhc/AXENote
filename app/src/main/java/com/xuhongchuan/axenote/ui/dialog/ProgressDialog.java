package com.xuhongchuan.axenote.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.xuhongchuan.axenote.R;

/**
 * 圆形进度条对话框
 * Created by xuhongchuan on 16/2/24.
 */
public class ProgressDialog extends Dialog {

    private static ProgressDialog mProgressDialog;
    private Context mContext;

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public static ProgressDialog createDialog(Context context) {
        mProgressDialog = new ProgressDialog(context, R.style.CommProgressDialog);
        mProgressDialog.setContentView(R.layout.dialog_progress);
        mProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return mProgressDialog;
    }
}
