package com.xuhongchuan.axenote.utils;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.xuhongchuan.axenote.BuildConfig;

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

        // Bugly初始化
        CrashReport.UserStrategy buglyStrategy = new CrashReport.UserStrategy(getApplicationContext());
        buglyStrategy.setAppVersion(BuildConfig.VERSION_NAME);
        Bugly.init(getApplicationContext(), "900043504", false, buglyStrategy);

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
