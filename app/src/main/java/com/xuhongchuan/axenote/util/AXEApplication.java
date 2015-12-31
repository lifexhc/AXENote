package com.xuhongchuan.axenote.util;

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
}
