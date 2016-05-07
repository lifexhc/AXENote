package com.xuhongchuan.axenote.data;

/**
 * 便签数据结构
 * Created by xuhongchuan on 15/10/17.
 */
public class Note {

    private int mId; // id
    private String mTitle; // 标题
    private String mContent; // 内容
    private boolean mHasImage; // 是否包含图片
    private long mCreateTime; // 创建时间
    private long mLastModifiedTime; // 最后修改时间
    private int mPosition; // 便签的位置，用于排序

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public boolean getHasImage() {
        return mHasImage;
    }

    public void setHasImage(boolean mHasImage) {
        this.mHasImage = mHasImage;
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

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

}
