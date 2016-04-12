package com.xuhongchuan.axenote.utils;

/**
 * Created by xuhongchuan on 15/12/22.
 */
public class GlobalValue {

    // 数据库名
    public static final String DATABASE_NAME = "AXENote.db";

    // 表名
    public static final String TABLE_NAME_NOTE = "note";

    // 列名
    public static final String COLUMN_NAME_ID = "id"; // id
    public static final String COLUMN_NAME_ORDINAL = "ordinal"; // 用于排序
    public static final String COLUMN_NAME_CONTENT = "content"; // 便签内容
    public static final String COLUMN_NAME_CREATE_TIME = "create_time"; // 创建时间
    public static final String COLUMN_NAME_UPDATE_TIME = "update_time"; // 最后更新时间

    // 数据库版本
    public static final int DATABASE_VERSION = 1;

    // 广播action
    public static String REFRESH_NOTE_LIST = "refreshNoteList"; // 刷新便签列表

}