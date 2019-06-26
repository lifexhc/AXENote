package com.xuhongchuan.axenote.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.infr.IChangeTheme;
import com.xuhongchuan.axenote.utils.GlobalConfig;
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
                recreate();
            }
        }
    };

    @Override
    public void changeTheme() {
        boolean isNightMode = GlobalConfig.getInstance().isNightMode(BaseActivity.this);
        if (isNightMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        changeTheme();
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        // Activity切换效果
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        L.d(TAG, getClass().getName() + " onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.d(TAG, getClass().getName() + " onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalValue.CHANGE_THEME);
        registerReceiver(mReceiver, filter);

        L.d(TAG, getClass().getName() + " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

        L.d(TAG, getClass().getName() + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d(TAG, getClass().getName() + " onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.d(TAG, getClass().getName() + " onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d(TAG, getClass().getName() + " onDestroy");
    }

    @Override
    public void finish() {
        super.finish();
        // Activity切换效果
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected abstract int getLayoutId();

}
