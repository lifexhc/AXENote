package com.xuhongchuan.axenote.utils;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

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

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,
                GlobalValue.LEAN_CLOUD_APP_ID,
                GlobalValue.LEAN_CLOUD_APP_KEY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 关闭数据库
        AXEDatabaseHelper.getInstatce().close();
    }
}
