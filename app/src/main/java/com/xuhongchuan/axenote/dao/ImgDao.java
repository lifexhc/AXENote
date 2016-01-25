package com.xuhongchuan.axenote.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xuhongchuan.axenote.utils.AXEDatabaseHelper;
import com.xuhongchuan.axenote.utils.GlobalValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Img数据库操作封装类
 * Created by xuhongchuan on 16/1/22.
 */
public class ImgDao {

    private SQLiteDatabase mDB;
    private static ImgDao mInstance;

    public ImgDao() {
        mDB = AXEDatabaseHelper.getInstatce().getWritableDatabase();
    }

    /**
     * 获取单例对象
     * @return
     */
    public static ImgDao getInstance() {
        if (mInstance == null) {
            synchronized (NoteDao.class) {
                if (mInstance == null) {
                    mInstance = new ImgDao();
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存一张图片
     * @param bitmap
     */
    public void insertImg(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        ContentValues cv = new  ContentValues();
        cv.put(GlobalValue.COLUMN_NAME_IMG_VALUES, byteArray);
        mDB.insert(GlobalValue.TABLE_NAME_IMG, null, cv);
    }

    public Bitmap getImg(int imgId) {
        String select = "select " +
                GlobalValue.COLUMN_NAME_IMG_VALUES +
                " from " + GlobalValue.TABLE_NAME_IMG + " where " + GlobalValue.COLUMN_NAME_IMG_ID + " = " + imgId;
        Cursor cursor = mDB.rawQuery(select, null);
        byte[] imageByteArray = null;
        if (cursor.moveToFirst()) {
            imageByteArray = cursor.getBlob(cursor.getColumnIndex(GlobalValue.COLUMN_NAME_IMG_VALUES));
        } else {
        }
        cursor.close();

        Bitmap bitmap = null;
        if (imageByteArray != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
            bitmap = BitmapFactory.decodeStream(imageStream);
        }
        return bitmap;
    }

    /**
     * 获取最后插入图片的id
     * @return
     */
    public int getLastId() {
        Cursor cursor = mDB.rawQuery("select last_insert_rowid() from" + GlobalValue.TABLE_NAME_IMG, null);
        int rowid = -1;
        if(cursor.moveToFirst()) {
            rowid = cursor.getInt(0);
        }
        cursor.close();
        return rowid;
    }
}
