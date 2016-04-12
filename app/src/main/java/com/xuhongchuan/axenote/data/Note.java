package com.xuhongchuan.axenote.data;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class Note {

    private int mId; // 便签id
    private String mContent; // 便签内容
    private long mCreateTime; // 创建时间
    private long mLastModifiedTime; // 最后修改时间
    private int mOrdinal; // 便签的位置，用于排序

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        this.mCreateTime = createTime;
    }

    public long getLastModifiedTime() {
        return mLastModifiedTime;
    }

    public void setLastModifiedTime(long mLastModifiedTime) {
        this.mLastModifiedTime = mLastModifiedTime;
    }

    public int getOrdinal() {
        return mOrdinal;
    }

    public void setOrdinal(int ordinal) {
        this.mOrdinal = ordinal;
    }
}
