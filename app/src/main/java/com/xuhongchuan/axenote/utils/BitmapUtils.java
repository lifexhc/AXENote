package com.xuhongchuan.axenote.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by xuhongchuan on 16/1/23.
 */
public class BitmapUtils {

    /**
     * 压缩图片
     * @param filePath
     * @param activity
     * @param scale
     * @return
     */
    public static Bitmap compressBitmap(String filePath, Activity activity, float scale) {
        Bitmap bitmap = sampleCompress(filePath, activity, scale); // 采样率压缩
        bitmap = pixelCompress(bitmap, activity, scale); // 像素压缩
        bitmap = qualityCompress(bitmap); // 质量压缩
        return bitmap;
    }

    /**
     * 采样率压缩
     * @param filePath
     * @param activity
     * @param scale
     * @return
     */
    public static Bitmap sampleCompress(String filePath, Activity activity, float scale) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int displayWidth = (int) (dm.widthPixels * scale);

        // 原始图片的宽高
        int realWidth = opts.outWidth;

        // 如果原始图片的宽大于设备宽度的 * scale
        // 计算一个合适的压缩率
        if (realWidth > displayWidth) {
            opts.inSampleSize = realWidth/displayWidth;
        }

        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
        return bitmap;
    }

    /**
     * 像素压缩
     * @param bitmap
     * @param activity
     * @param scale
     * @return
     */
    public static Bitmap pixelCompress(Bitmap bitmap, Activity activity, float scale) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int displayWidth = (int) (dm.widthPixels * scale);

        // 原始图片的宽高
        int realWidth = bitmap.getWidth();
        int realHeight= bitmap.getHeight();
        // 压缩比例
        float pixelScale = 1.0F;

        if (realWidth > displayWidth) {
            pixelScale = (float) displayWidth/realWidth;
        }

        // 创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(pixelScale, pixelScale);

        // 压缩图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, realWidth, realHeight, matrix, true);
        return resizedBitmap;
    }

    /**
     * 质量压缩
     * @param bitmap
     * @return
     */
    public static Bitmap qualityCompress(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (baos.toByteArray().length / 1024 > 128) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        int options = 50;
        //TODO 如果循环次数太多的话太耗时，待优化
        while (baos.toByteArray().length / 1024 > 64) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos); // 这里压缩options%，把压缩后的数据存放到baos中
            if (options == 10) {
                break;
            }
            options -= 10; // 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray()); // 把压缩后的数据baos存放到ByteArrayInputStream中
        bitmap = BitmapFactory.decodeStream(isBm, null, null); // 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
