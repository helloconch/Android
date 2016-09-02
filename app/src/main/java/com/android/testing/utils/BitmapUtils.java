package com.android.testing.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

/**
 * Created by Administrator on 2016/9/2.
 */
public class BitmapUtils {
    /**
     * 创建一张含有倒影图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap) {

        if (srcBitmap == null)
            return null;

        //指定原图与倒影图之间的间隙

        final int REFLECTION_GAP = 4;

        //原始图片资源的宽高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        //倒影宽高
        int reflectionWidth = srcBitmap.getWidth();
        int refelctionHeight = srcBitmap.getHeight() / 2;

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        Matrix matrix = new Matrix();
        // 1表示放大比例,不放大也不缩小。
        // -1表示在y轴上相反,即旋转180度。
        matrix.preScale(1, -1);

        //创建倒影
        Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0,
                refelctionHeight, reflectionWidth, refelctionHeight, matrix, false);

        if (reflectionBitmap == null) {
            return null;
        }

        //创建一张图包含原图和倒影(此时是空白图像)
        Bitmap bitmapWithReflection = Bitmap.createBitmap(srcWidth,
                srcHeight + refelctionHeight + REFLECTION_GAP,
                Bitmap.Config.ARGB_8888);

        if (bitmapWithReflection == null) {
            return null;
        }

        Canvas canvas = new Canvas(bitmapWithReflection);
        //将原图绘制到画布上
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        //将倒影绘制到画布上
        canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //进行渐变效果阴影
        LinearGradient shader = new LinearGradient(0, srcHeight,
                0, bitmapWithReflection.getWidth() + REFLECTION_GAP,
                0x70FFFFFF,
                0x00FFFFFF,
                Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
        //将渐变效果阴影绘制到画布上
        canvas.drawRect(0, srcHeight,
                srcWidth, bitmapWithReflection.getWidth() + REFLECTION_GAP, paint);

        return bitmapWithReflection;


    }

    /**
     * 创建一张带有边框的图片
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap createBorderBitmap(Bitmap srcBitmap) {

        if (srcBitmap == null)
            return null;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        final int BORDER_WIDTH = 4;
        final int color = Color.BLUE;
        //按照宽高，创建一张新图
        Bitmap newBitmap = Bitmap.createBitmap(srcWidth + 2 * BORDER_WIDTH, srcHeight + 2 * BORDER_WIDTH, srcBitmap.getConfig());
        //绘制到画布上
        Canvas canvas = new Canvas(newBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //将画布进行颜色填充
        canvas.drawColor(color);
        //将原始图绘制到画布上
        canvas.drawBitmap(srcBitmap, BORDER_WIDTH, BORDER_WIDTH, null);
        return newBitmap;

    }

}
