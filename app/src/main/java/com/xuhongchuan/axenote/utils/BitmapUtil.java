package com.xuhongchuan.axenote.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by xuhongchuan on 16/1/23.
 */
public class BitmapUtil {

    /**
     * 压缩图片
     *
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
     *
     * @param filePath
     * @param activity
     * @param scale
     * @return
     */
    public static Bitmap sampleCompress(String filePath, Activity activity, float scale) {
        long startTime=System.currentTimeMillis();   // 开始时间

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int displayWidth = (int) (dm.widthPixels * scale);

        // 原始图片的宽高
        int realWidth = opts.outWidth;

        // 如果原始图片的宽大于设备宽度的 * scale
        // 计算一个合适的采样率
        if (realWidth > displayWidth) {
            opts.inSampleSize = realWidth / displayWidth;
        }

        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);

        long endTime=System.currentTimeMillis(); // 结束时间
        L.d("BitmapUtil", "采样率压缩用时：" + (endTime-startTime) + "ms");
        return bitmap;
    }

    /**
     * 像素压缩
     *
     * @param bitmap
     * @param activity
     * @param scale
     * @return
     */
    public static Bitmap pixelCompress(Bitmap bitmap, Activity activity, float scale) {
        long startTime=System.currentTimeMillis();   // 开始时间

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int displayWidth = (int) (dm.widthPixels * scale);

        // 原始图片的宽高
        int realWidth = bitmap.getWidth();
        int realHeight = bitmap.getHeight();
        // 压缩比例
        float pixelScale = 1.0F;

        if (realWidth > displayWidth) {
            pixelScale = (float) displayWidth / realWidth;
        }

        // 创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(pixelScale, pixelScale);

        // 压缩图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, realWidth, realHeight, matrix, true);

        long endTime=System.currentTimeMillis(); // 结束时间
        L.d("BitmapUtil", "像素压缩用时：" + (endTime-startTime) + "ms");
        return resizedBitmap;
    }

    /**
     * 质量压缩
     *
     * @param bitmap
     * @return
     */
    public static Bitmap qualityCompress(Bitmap bitmap) {
        long startTime=System.currentTimeMillis();   // 开始时间

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 图片如果大于400kb,直接把options设为50减少循环次数
        if (baos.toByteArray().length / 1024 > 400) {
            options = 50;
        }
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100 && options > 10) {
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray()); // 把压缩后的数据baos存放到ByteArrayInputStream中
        bitmap = BitmapFactory.decodeStream(isBm, null, null); // 把ByteArrayInputStream数据生成图片

        long endTime=System.currentTimeMillis(); // 结束时间
        L.d("BitmapUtil", "质量压缩用时：" + (endTime-startTime) + "ms");
        return bitmap;
    }

    /**
     * 将bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String toBase64(Bitmap bitmap) {
        long startTime=System.currentTimeMillis();   // 开始时间

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        long endTime=System.currentTimeMillis(); // 结束时间
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        L.d("BitmapUtil", "toBase64用时：" + (endTime-startTime) + "ms");
        return base64;
    }

}
