package com.xuhongchuan.axenote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.utils.AXEDatabaseHelper;
import com.xuhongchuan.axenote.utils.GlobalValue;

import java.util.ArrayList;

/**
 * Note数据库操作封装类
 * Created by xuhongchuan on 15/12/22.
 */
public class NoteDao {

    private SQLiteDatabase mDb;
    private static NoteDao mInstance;

    public NoteDao() {
        mDb = AXEDatabaseHelper.getInstatce().getWritableDatabase();
    }

    /**
     * 获取单例对象
     *
     * @return
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
    public void createNewNote(final String content, final long createTime, final long lastModifiedTime) {
        ContentValues values = new ContentValues();
        values.put(GlobalValue.COLUMN_NAME_ORDINAL, getMaxOrdinal() + 1);
        values.put(GlobalValue.COLUMN_NAME_CONTENT, content);
        values.put(GlobalValue.COLUMN_NAME_CREATE_TIME, createTime);
        values.put(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME, lastModifiedTime);
        mDb.insert(GlobalValue.TABLE_NAME_NOTE, null, values);
        values.clear();
    }

    /**
     * 删除指定id的便签
     *
     * @param id
     */
    public void deleteNote(int id) {
        mDb.delete(GlobalValue.TABLE_NAME_NOTE, GlobalValue.COLUMN_NAME_ID + " = ?", new String[]{id + ""});
    }

    /**
     * 修改数据
     *
     * @param id
     * @param content
     * @param lastModifiedTime
     */
    public void updateNote(int id, String content, long lastModifiedTime) {
        ContentValues values = new ContentValues();
        values.put(GlobalValue.COLUMN_NAME_CONTENT, content);
        values.put(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME, lastModifiedTime);
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
                GlobalValue.COLUMN_NAME_ORDINAL + "," +
                GlobalValue.COLUMN_NAME_CONTENT + "," +
                GlobalValue.COLUMN_NAME_CREATE_TIME + "," +
                GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME +
                " from " + GlobalValue.TABLE_NAME_NOTE + " where id = " + id;
        Cursor cursor = mDb.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ID)));
            note.setOrdinal(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ORDINAL)));
            note.setContent(cursor.getString(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CONTENT)));
            note.setCreateTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CREATE_TIME)));
            note.setLastModifiedTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME)));
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
        ArrayList<Note> noteList = new ArrayList<Note>();
        Cursor cursor = mDb.query(false, GlobalValue.TABLE_NAME_NOTE, new String[]{GlobalValue.COLUMN_NAME_ID, GlobalValue.COLUMN_NAME_ORDINAL, GlobalValue.COLUMN_NAME_CONTENT,
                        GlobalValue.COLUMN_NAME_CREATE_TIME, GlobalValue.COLUMN_NAME_LAST_MODIFIED_TIME},
                null, null, null, null, GlobalValue.COLUMN_NAME_ORDINAL, null);
        if (cursor.moveToLast()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ID)));
                note.setOrdinal(cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ORDINAL)));
                note.setContent(cursor.getString(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CONTENT)));
                note.setCreateTime(cursor.getLong(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_CREATE_TIME)));
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
     * 获取最大排序值
     *
     * @return
     */
    public int getMaxOrdinal() {
        String select = "select max(" + GlobalValue.COLUMN_NAME_ORDINAL + ") from " +
                GlobalValue.TABLE_NAME_NOTE;
        Cursor cursor = mDb.rawQuery(select, null);
        int maxOrd = -1;
        if (cursor.moveToFirst()) {
            maxOrd = cursor.getInt(0);
        }
        cursor.close();
        return maxOrd;
    }

    /**
     * 交换两条便签的排序值
     *
     * @param id1
     * @param id2
     */
    public void swapOrdinal(int id1, int id2) {
        int ordinal1 = 0;
        int ordinal2 = 0;

        String select1 = "select " +
                GlobalValue.COLUMN_NAME_ORDINAL +
                " from " + GlobalValue.TABLE_NAME_NOTE + " where id = " + id1;
        String select2 = "select " +
                GlobalValue.COLUMN_NAME_ORDINAL +
                " from " + GlobalValue.TABLE_NAME_NOTE + " where id = " + id2;

        Cursor cursor = mDb.rawQuery(select1, null);

        if (cursor.moveToFirst()) {
            ordinal1 = cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ORDINAL));
        }

        cursor = mDb.rawQuery(select2, null);
        if (cursor.moveToFirst()) {
            ordinal2 = cursor.getInt(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_ORDINAL));
        }

        ContentValues values = new ContentValues();
        values.put(GlobalValue.COLUMN_NAME_ORDINAL, ordinal1);
        mDb.update(GlobalValue.TABLE_NAME_NOTE, values, GlobalValue.COLUMN_NAME_ID + " = ?", new String[]{id2 + ""});

        values.put(GlobalValue.COLUMN_NAME_ORDINAL, ordinal2);
        mDb.update(GlobalValue.TABLE_NAME_NOTE, values, GlobalValue.COLUMN_NAME_ID + " = ?", new String[]{id1 + ""});
        cursor.close();
    }

}
