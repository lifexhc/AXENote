package com.xuhongchuan.axenote.utils;

import android.app.Application;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

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
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 关闭数据库
        AXEDatabaseHelper.getInstatce().close();
    }
}
