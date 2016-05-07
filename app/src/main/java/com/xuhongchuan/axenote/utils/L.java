package com.xuhongchuan.axenote.utils;

import android.util.Log;

/**
 * Log
 * Created by xuhongchuan on 15/7/31.
 */
public class L {

    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;

    public static final int LEVEL = DEBUG; // 过滤等级

    public static void d(Object tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(Object tag, Throwable e) {
        d(tag, "", e);
    }

    public static void d(Object tag, String msg, Throwable e) {
        if (LEVEL <= DEBUG) {
            if (e != null) {
                Log.d(getTag(tag), msg, e);
            } else {
                Log.d(getTag(tag), msg);
            }
        }
    }

    public static void i(Object tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(Object tag, Throwable e) {
        i(tag, "", e);
    }

    public static void i(Object tag, String msg, Throwable e) {
        if (LEVEL <= INFO) {
            if (e != null) {
                Log.i(getTag(tag), msg, e);
            } else {
                Log.i(getTag(tag), msg);
            }
        }
    }

    public static void w(Object tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(Object tag, Throwable e) {
        w(tag, "", e);
    }

    public static void w(Object tag, String msg, Throwable e) {
        if (LEVEL <= WARN) {
            if (e != null) {
                Log.w(getTag(tag), msg, e);
            } else {
                Log.w(getTag(tag), msg);
            }
        }
    }

    public static void e(Object tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(Object tag, Throwable e) {
        e(tag, "", e);
    }

    public static void e(Object tag, String msg, Throwable e) {
        if (LEVEL <= ERROR) {
            if (e != null) {
                Log.e(getTag(tag), msg, e);
            } else {
                Log.e(getTag(tag), msg);
            }
        }
    }

    private static String getTag(Object tag) {
        if (tag.getClass() == String.class) {
            return (String) tag;
        }
        Class<?> clazz = null;
        if (tag instanceof Class<?>) {
            clazz = (Class<?>) tag;
        } else {
            clazz = tag.getClass();
        }
        return clazz.getSimpleName();
    }

}
