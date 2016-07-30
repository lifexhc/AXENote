package com.xuhongchuan.axenote.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.infr.IChangeTheme;
import com.xuhongchuan.axenote.utils.GlobalValue;
import com.xuhongchuan.axenote.utils.L;

/**
 * 祖宗Activity
 * Created by xuhongchuan on 15/11/28.
 */
public abstract class BaseActivity extends AppCompatActivity implements IChangeTheme {

    private static final String TAG = BaseActivity.class.getName();

    /**
     * 广播
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GlobalValue.CHANGE_THEME)) {
                changeTheme();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalValue.CHANGE_THEME);
        registerReceiver(mReceiver, filter);

        L.d(TAG, getClass().getName() + " resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

        L.d(TAG, getClass().getName() + " pause");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        // Activity切换效果
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        // Activity切换效果
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected abstract int getLayoutId();

}
