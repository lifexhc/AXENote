package com.xuhongchuan.axenote.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 全局配置
 * Created by xuhongchuan on 15/12/16.
 */
public class GlobalConfig {

    // 当前是否为夜间模式
    private final String SP_KEY_NIGHT_MODE = "nightMode";

    // 单例对象
    private static GlobalConfig mInstance;

    // 构造方法
    private GlobalConfig() {
        super();
    }

    // 获取单例对象
    public static GlobalConfig getInstance() {
        if (mInstance == null) {
            synchronized (GlobalConfig.class) {
                if (mInstance == null) {
                    mInstance = new GlobalConfig();
                }
            }
        }
        return mInstance;
    }

    /**
     * 判断当前是否为夜间模式
     *
     * @param context
     * @return
     */
    public boolean isNightMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(GlobalValue.SHARED_PRE_FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(SP_KEY_NIGHT_MODE, false);
    }

    /**
     * 设置主题
     *
     * @param context
     * @param isNight
     */
    public void setNightMode(Context context, boolean isNight) {
        SharedPreferences sp = context.getSharedPreferences(GlobalValue.SHARED_PRE_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SP_KEY_NIGHT_MODE, isNight);
        editor.commit();
    }
}
