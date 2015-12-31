package com.xuhongchuan.axenote.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xuhongchuan on 15/12/22.
 */
public class AXEDatabaseHelper extends SQLiteOpenHelper {

    private static AXEDatabaseHelper mInstance;

    /**
     * 获取单例对象
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

    // 创建Note表sql语句
    private static final String SQL_CREATE_NOTE = "create table if not exists " + GlobalValue.TABLE_NAME_NOTE + "(" +
            GlobalValue.COLUMN_NAME_ID + " integer primary key autoincrement," +
            GlobalValue.COLUMN_NAME_ORDINAL + " integer," +
            GlobalValue.COLUMN_NAME_CONTENT + " text," +
            GlobalValue.COLUMN_NAME_CREATE_TIME + " integer," +
            GlobalValue.COLUMN_NAME_UPDATE_TIME + " integer)";

    private static final String SQL_CREATE_NOTE_INDEX = "create index index_name" +
            "on " + GlobalValue.TABLE_NAME_NOTE;

    // 删除Note表sql语句
    public static final String SQL_DELETE_NOTE =
            "drop table if exists " + GlobalValue.TABLE_NAME_NOTE;

    private Context mContent;

    public AXEDatabaseHelper(Context context) {
        super(context, GlobalValue.DATABASE_NAME, null, GlobalValue.DATABASE_VERSION);
        mContent = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTE);
        L.d(this, "create table note");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.d(this, "update db");
    }
}
