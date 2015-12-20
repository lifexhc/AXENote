package com.xuhongchuan.axenote.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 全局配置
 * Created by xuhongchuan on 15/12/16.
 */
public class GlobalConfig {

    private final String SP_KEY_ISNIGHT = "isNight";

    private static GlobalConfig mInstance;

    public static GlobalConfig getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalConfig();
        }
        return mInstance;
    }

    public boolean isNight(Context context) {
        SharedPreferences sp = context.getSharedPreferences(GlobalValue.SHARED_PRE_FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(SP_KEY_ISNIGHT, false);
    }

    public void setIsNight(Context context, boolean isNight) {
        SharedPreferences sp = context.getSharedPreferences(GlobalValue.SHARED_PRE_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SP_KEY_ISNIGHT, isNight);
        editor.commit();
    }
}
