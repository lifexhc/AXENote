package com.xuhongchuan.axenote.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite管理类
 * Created by xuhongchuan on 15/12/22.
 */
public class AXEDatabaseHelper extends SQLiteOpenHelper {

    // 删除Note表sql语句
    public static final String SQL_DELETE_NOTE =
            "drop table if exists " + GlobalValue.TABLE_NAME_NOTE;
    // 创建Note表sql语句
    private static final String SQL_CREATE_NOTE = "create table if not exists " + GlobalValue.TABLE_NAME_NOTE + "(" +
            GlobalValue.COLUMN_NAME_ID + " integer primary key autoincrement," +
            GlobalValue.COLUMN_NAME_TITLE + " text," +
            GlobalValue.COLUMN_NAME_CONTENT + " text," +
            GlobalValue.COLUMN_NAME_HAS_IMAGE + " integer," +
            GlobalValue.COLUMN_NAME_CREATE_TIME + " integer," +
            GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME + " integer," +
            GlobalValue.COLUMN_NAME_POSITION + " integer)";
    // 单例对象
    private static AXEDatabaseHelper mInstance;

    // 构造方法
    private AXEDatabaseHelper(Context context) {
        super(context, GlobalValue.DATABASE_NAME, null, GlobalValue.DATABASE_VERSION);
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static AXEDatabaseHelper getInstatce() {
        if (mInstance == null) {
            synchronized (AXEDatabaseHelper.class) {
                if (mInstance == null) {
                    mInstance = new AXEDatabaseHelper(AXEApplication.getApplication().getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTE);
        L.d(this, "create table notes");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(SQL_DELETE_NOTE);
            L.d(this, "delete table note");
            db.execSQL(SQL_CREATE_NOTE);
            L.d(this, "create table notes");
        }
    }
}
