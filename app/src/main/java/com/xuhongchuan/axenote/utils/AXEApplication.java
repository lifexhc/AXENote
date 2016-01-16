package com.xuhongchuan.axenote.utils;

import android.app.Application;

/**
 * Created by xuhongchuan on 15/12/22.
 */
public class AXEApplication extends Application {

    private static AXEApplication mInstance;

    public static AXEApplication getApplication() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 关闭数据库
        AXEDatabaseHelper.getInstatce().close();
    }
}
