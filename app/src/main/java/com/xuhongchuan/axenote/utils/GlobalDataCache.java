package com.xuhongchuan.axenote.utils;

import com.xuhongchuan.axenote.dao.NoteDao;
import com.xuhongchuan.axenote.data.Note;

import java.util.Iterator;
import java.util.List;

/**
 * 全局数据缓存
 * Created by xuhongchuan on 16/1/5.
 */
public class GlobalDataCache {

    private static GlobalDataCache mInstance;
    private List<Note> notes;

    public GlobalDataCache() {
        initNotes();
    }

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

    public void initNotes() {
        notes = NoteDao.getInstance().getAllNotes();
    }

    public List<Note> getNotes() {
        return notes;
    }

    /**
     * 获取指定id的便签
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
     * 获取指定排序值的便签
     * @param ordinal
     * @return
     */
    public Note getNoteByOrdinal(int ordinal) {
        for (Note note : notes) {
            if (note.getOrdinal() == ordinal) {
                return note;
            }
        }
        return null;
    }

    /**
     * 插入一条新便签
     * @param note
     */
    public void createNewNote(Note note) {
        notes.add(note);
        NoteDao.getInstance().createNewNote(note.getContent(), note.getCreateTime(), note.getUpdateTime());
    }

    /**
     * 插入一条新便签
     * @param content
     * @param createTime
     * @param updateTime
     */
    public void createNewNote(String content, long createTime, long updateTime) {
        Note note = new Note();
        note.setContent(content);
        note.setCreateTime(createTime);
        note.setUpdateTime(updateTime);

        createNewNote(note);
    }

    /**
     * 删除指定id的便签
     * @param id
     */
    public void deleteNote(int id) {
        Iterator it = notes.iterator();
        Note note = null;
        while (it.hasNext()) {
            note = (Note) it.next();
            if (note.getId() == id) {
                it.remove();
            }
        }
        NoteDao.getInstance().deleteNote(id);
    }

    /**
     * 更新便签
     * @param id
     * @param content
     * @param updateTime
     */
    public void updateNote(int id, String content, long updateTime) {
        Iterator it = notes.iterator();
        Note note = null;
        while (it.hasNext()) {
            note = (Note) it.next();
            if (note.getId() == id) {
                note.setContent(content);
                note.setUpdateTime(updateTime);
            }
        }
        NoteDao.getInstance().updateNote(id, content, updateTime);
    }

    /**
     * 获取最后插入便签的id
     * @return
     */
    public int getLastId() {
        return NoteDao.getInstance().getLastId();
    }

}
