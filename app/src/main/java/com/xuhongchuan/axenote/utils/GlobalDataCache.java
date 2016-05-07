package com.xuhongchuan.axenote.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.xuhongchuan.axenote.dao.NoteDao;
import com.xuhongchuan.axenote.data.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 全局数据缓存
 * Created by xuhongchuan on 16/1/5.
 */
public class GlobalDataCache {

    // 单例对象
    private static GlobalDataCache mInstance;
    // 便签列表
    private List<Note> notes;

    // 构造方法
    private GlobalDataCache() {
        notes = new ArrayList<>();
        syncNotes();
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static GlobalDataCache getInstance() {
        if (mInstance == null) {
            synchronized (GlobalDataCache.class) {
                if (mInstance == null) {
                    mInstance = new GlobalDataCache();
                }
            }
        }
        return mInstance;
    }

    /**
     * 和Sqlite同步便签列表
     */
    public void syncNotes() {
        notes.clear();
        notes.addAll(NoteDao.getInstance().getAllNotes());
    }

    /**
     * 获取所有便签
     *
     * @return
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * 获取指定id的便签
     *
     * @param id
     * @return
     */
    public Note getNoteById(int id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                return note;
            }
        }
        return null;
    }

    /**
     * 插入一条新便签
     *
     * @param content
     * @param hasImage
     * @param createTime
     * @param lastModifiedTime
     */
    public void createNewNote(String content, boolean hasImage, long createTime, long lastModifiedTime) {
        NoteDao.getInstance().createNewNote(content, hasImage, createTime, lastModifiedTime);
        syncNotes();
    }

    /**
     * 插入一条新便签
     *
     * @param note
     */
    public void createNewNote(Note note) {
        notes.add(note);
        createNewNote(note.getContent(), note.getHasImage(), note.getCreateTime(), note.getLastModifiedTime());
    }

    /**
     * 删除指定id的便签
     *
     * @param id
     */
    public void deleteNote(int id) {
        NoteDao.getInstance().deleteNote(id);
        syncNotes();
    }

    /**
     * 更新便签
     *
     * @param id
     * @param content
     * @param hasImage
     * @param lastModifiedTime
     */
    public void updateNote(int id, String content, boolean hasImage, long lastModifiedTime) {
        NoteDao.getInstance().updateNote(id, content, hasImage, lastModifiedTime);
        syncNotes();
    }

    /**
     * 更新便签
     *
     * @param id
     * @param title
     * @param content
     * @param hasImage
     * @param lastModifiedTime
     */
    public void updateNote(int id, String title, String content, boolean hasImage, long lastModifiedTime) {
        NoteDao.getInstance().updateNote(id, title, content, hasImage, lastModifiedTime);
        syncNotes();
    }

    /**
     * 交换两条便签的排序值
     *
     * @param position1
     * @param position2
     */
    public void swapNote(int position1, int position2) {
        Collections.swap(notes, position1, position2);
    }

}
