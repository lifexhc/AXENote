package com.xuhongchuan.axenote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.utils.AXEDatabaseHelper;
import com.xuhongchuan.axenote.utils.GlobalValue;
import com.xuhongchuan.axenote.utils.L;

import java.util.ArrayList;

/**
 * Note数据库操作封装类
 * Created by xuhongchuan on 15/12/22.
 */
public class NoteDao {

    // SQLite对象
    private SQLiteDatabase mDb;

    // 单例对象
    private static NoteDao mInstance;

    // 构造方法
    private NoteDao() {
        mDb = AXEDatabaseHelper.getInstatce().getWritableDatabase();
    }

    /**
     * 获取单例对象
     */
    public static NoteDao getInstance() {
        if (mInstance == null) {
            synchronized (NoteDao.class) {
                if (mInstance == null) {
                    mInstance = new NoteDao();
                }
            }
        }
        return mInstance;
    }

    /**
     * 插入一条新便签
     *
     * @param content
     * @param createTime
     * @param lastModifiedTime
     */
    public void createNewNote(String content, boolean hasImage, long createTime, long lastModifiedTime) {
        // 所有便签的index + 1
        String sql = "update " + GlobalValue.TABLE_NAME_NOTE + " set " + GlobalValue.COLUMN_NAME_POSITION
                + " = " + GlobalValue.COLUMN_NAME_POSITION + " + 1";
        L.d("NoteDao", sql);
        mDb.execSQL(sql);

        // 创建新便签
        ContentValues values = new ContentValues();
        String title = ""; // 便签标题
        if (content.length() > GlobalValue.TITLE_LENGTH) {
            title = content.substring(0, GlobalValue.TITLE_LENGTH - 1);
        } else {
            title = content;
        }
        values.put(GlobalValue.COLUMN_NAME_TITLE, title);
        values.put(GlobalValue.COLUMN_NAME_CONTENT, content);
        if (hasImage) {
            values.put(GlobalValue.COLUMN_NAME_HAS_IMAGE, 1);
        } else {
            values.put(GlobalValue.COLUMN_NAME_HAS_IMAGE, 0);
        }
        values.put(GlobalValue.COLUMN_NAME_CREATE_TIME, createTime);
        values.put(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME, lastModifiedTime);
        values.put(GlobalValue.COLUMN_NAME_POSITION, 0);
        mDb.insert(GlobalValue.TABLE_NAME_NOTE, null, values);
    }

    /**
     * 删除指定id的便签
     *
     * @param id
     */
    public void deleteNote(int id) {
        int position = getNote(id).getPosition();
        mDb.delete(GlobalValue.TABLE_NAME_NOTE, GlobalValue.COLUMN_NAME_ID + " = ?", new String[]{id + ""});
        // 在此便签之后的其余便签index - 1
        String sql = "update " + GlobalValue.TABLE_NAME_NOTE + " set " + GlobalValue.COLUMN_NAME_POSITION
                + " = " + GlobalValue.COLUMN_NAME_POSITION + " - 1 " + "where "
                + GlobalValue.COLUMN_NAME_POSITION + " > " + position;
        L.d("NoteDao", sql);
        mDb.execSQL(sql);

    }

    /**
     * 修改数据
     *
     * @param id
     * @param content
     * @param lastModifiedTime
     */
    public void updateNote(int id, String content, boolean hasImage, long lastModifiedTime) {
        ContentValues values = new ContentValues();
        String title = ""; // 便签标题
        if (content.length() > 12) {
            title = content.substring(0, 11);
        } else {
            title = content;
        }
        values.put(GlobalValue.COLUMN_NAME_TITLE, title);
        values.put(GlobalValue.COLUMN_NAME_CONTENT, content);
        values.put(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME, lastModifiedTime);
        if (hasImage) {
            values.put(GlobalValue.COLUMN_NAME_HAS_IMAGE, 1);
        } else {
            values.put(GlobalValue.COLUMN_NAME_HAS_IMAGE, 0);
        }
        mDb.update(GlobalValue.TABLE_NAME_NOTE, values, GlobalValue.COLUMN_NAME_ID + " = ?", new String[]{id + ""});
    }

    /**
     * 查询便签数量
     *
     * @return
     */
    public int getNoteCount() {
        String select = "select count(*) from " + GlobalValue.TABLE_NAME_NOTE;
        int count = 0;
        Cursor cursor = mDb.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * 查询指定id的便签
     *
     * @param id
     */
    public Note getNote(int id) {
        Note note = new Note();

        String select = "select " +
                GlobalValue.COLUMN_NAME_ID + "," +
                GlobalValue.COLUMN_NAME_TITLE + "," +
                GlobalValue.COLUMN_NAME_CONTENT + "," +
                GlobalValue.COLUMN_NAME_HAS_IMAGE + "," +
                GlobalValue.COLUMN_NAME_CREATE_TIME + "," +
                GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME + "," +
                GlobalValue.COLUMN_NAME_POSITION +
                " from " + GlobalValue.TABLE_NAME_NOTE + " where id = " + id;
        Cursor cursor = mDb.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_TITLE)));
            note.setContent(cursor.getString(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CONTENT)));
            if (cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_HAS_IMAGE)) == 1) {
                note.setHasImage(true);
            } else {
                note.setHasImage(true);
            }
            note.setCreateTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CREATE_TIME)));
            note.setLastModifiedTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME)));
            note.setPosition(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_POSITION)));
        }
        cursor.close();
        return note;
    }

    /**
     * 查询所有便签
     *
     * @return
     */
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        // 查询所有便签，根据便签位置排序
        Cursor cursor = mDb.query(false, GlobalValue.TABLE_NAME_NOTE, new String[]{GlobalValue.COLUMN_NAME_ID, GlobalValue.COLUMN_NAME_TITLE, GlobalValue.COLUMN_NAME_CONTENT, GlobalValue.COLUMN_NAME_HAS_IMAGE,
                        GlobalValue.COLUMN_NAME_CREATE_TIME, GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME, GlobalValue.COLUMN_NAME_POSITION},
                null, null, null, null, GlobalValue.COLUMN_NAME_POSITION + " DESC", null);
        if (cursor.moveToLast()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CONTENT)));
                if (cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_HAS_IMAGE)) == 1) {
                    note.setHasImage(true);
                } else {
                    note.setHasImage(false);
                }
                note.setCreateTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CREATE_TIME)));
                note.setLastModifiedTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME)));
                note.setPosition(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_POSITION)));
                note.setLastModifiedTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME)));
                noteList.add(note);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return noteList;
    }

    /**
     * 获取最后插入便签的id
     *
     * @return
     */
    public int getLastId() {
        Cursor cursor = mDb.rawQuery("select last_insert_rowid() from" + GlobalValue.TABLE_NAME_NOTE, null);
        int rowid = -1;
        if (cursor.moveToFirst()) {
            rowid = cursor.getInt(0);
        }
        cursor.close();
        return rowid;
    }

    /**
     * 交换两条便签的位置
     *
     * @param position1
     * @param position2
     */
    public void swapIndex(int position1, int position2) {
        // 记录position1的便签的id
        int id = 0;

        String select = "select " + GlobalValue.COLUMN_NAME_ID + " from " + GlobalValue.TABLE_NAME_NOTE
                + " where " + GlobalValue.COLUMN_NAME_POSITION + " = " + position1;
        Cursor cursor = mDb.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ID));
        }

        String sql = "";
        // 调整(position1,position2]之间的便签的position
        if (position1 < position2) { // 向下拖动
            sql = "update " + GlobalValue.TABLE_NAME_NOTE + " set " + GlobalValue.COLUMN_NAME_POSITION
                    + " = " + GlobalValue.COLUMN_NAME_POSITION + " - 1 " + "where "
                    + GlobalValue.COLUMN_NAME_POSITION + " > " + position1 + " AND " + GlobalValue.COLUMN_NAME_POSITION + " <= " + position2;
        }
        // 调整[position2,position1)之间的便签的position
        if (position1 > position2) { // 向上拖动
            sql = "update " + GlobalValue.TABLE_NAME_NOTE + " set " + GlobalValue.COLUMN_NAME_POSITION
                    + " = " + GlobalValue.COLUMN_NAME_POSITION + " + 1 " + "where "
                    + GlobalValue.COLUMN_NAME_POSITION + " < " + position1 + " AND " + GlobalValue.COLUMN_NAME_POSITION + " >= " + position2;
        }
        mDb.execSQL(sql);

        // 把原来position为position1的便签设置为position2
        ContentValues values = new ContentValues();
        values.put(GlobalValue.COLUMN_NAME_POSITION, position2);
        mDb.update(GlobalValue.TABLE_NAME_NOTE, values, GlobalValue.COLUMN_NAME_ID + " = ?", new String[]{id + ""});
        cursor.close();
    }

}
