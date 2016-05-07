package com.xuhongchuan.axenote.utils;

/**
 * 全局标识
 * Created by xuhongchuan on 15/12/22.
 */
public class GlobalValue {

    // 数据库名
    public static final String DATABASE_NAME = "AXENote.db";

    // 数据库版本
    public static final int DATABASE_VERSION = 2;

    // 表名
    public static final String TABLE_NAME_NOTE = "notes"; // 便签表

    // 列名
    public static final String COLUMN_NAME_ID = "id"; // id
    public static final String COLUMN_NAME_TITLE = "title"; // 标题
    public static final String COLUMN_NAME_CONTENT = "content"; // 内容
    public static final String COLUMN_NAME_HAS_IMAGE = "has_image"; // 是否有图片
    public static final String COLUMN_NAME_CREATE_TIME = "create_time"; // 创建时间
    public static final String COLUMN_NAME_LAST_MODIFIED_TIME = "last_modified_time"; // 最后修改时间
    public static final String COLUMN_NAME_POSITION = "position"; // 便签的位置，用于排序

    // 广播action
    public static String REFRESH_NOTE_LIST = "refreshNoteList"; // 刷新便签列表
    public static String CHANGE_THEME = "changeTheme"; // 更换主题

    // SharedPreferences的文件名
    public static String SHARED_PRE_FILE_NAME = "axenote_data";

    // 其它
    public static int TITLE_LENGTH = 12; // 便签标题长度

}