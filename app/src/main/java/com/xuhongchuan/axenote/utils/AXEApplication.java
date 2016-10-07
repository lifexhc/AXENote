package com.xuhongchuan.axenote.utils;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.tencent.bugly.Bugly;

/**
 * Application
 * Created by xuhongchuan on 15/12/22.
 */
public class AXEApplication extends Application {

    // 全局对象
    private static AXEApplication mInstance;

    // 获取全局对象
    public static AXEApplication getApplication() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // bugly初始化
        Bugly.init(getApplicationContext(), "900043504", false);

        boolean isNightMode = GlobalConfig.getInstance().isNightMode(getApplication());
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 关闭数据库
        AXEDatabaseHelper.getInstatce().close();
    }
}
