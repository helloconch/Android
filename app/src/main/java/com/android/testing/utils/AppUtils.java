package com.android.testing.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.app.lib.utils.BaseUtils;

import java.io.InputStream;

/**
 * Created by cheyanxu on 16/8/3.
 */
public class AppUtils extends BaseUtils {
    /**
     * 通过输入流压缩,获取Bitmap
     *
     * @param context
     * @param is
     * @param reqWidth  需要图片显示宽度
     * @param reqHeight 需要图片显示高度
     * @return
     */
    public static Bitmap decodeStream(Context context, InputStream is, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        //当这个参数为true的时候,
        // 意味着你可以在解析时候不申请内存的情况下去获取Bitmap的宽和高
        //这是调整Bitmap Size一个很重要的参数设置
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        int realHeight = options.outHeight;
        int realWidth = options.outWidth;
        int simpleSize = findBestSampleSize(realWidth, realHeight, reqWidth, reqHeight);
        options.inSampleSize = simpleSize;
        //当你希望得到Bitmap实例的时候
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);

    }

    /**
     * 解析了一次一张某分辨率的图片，并且设置在`options.inBitmap`中，
     * 然后分别decode了同一张图片，并且传入了相同的`options`。
     * 最终只占用一份第一次解析`Bitmap`的内存。
     *
     * @param context
     * @param resourceId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static BitmapFactory.Options decodeSameBitmap(Context context, int resourceId, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        //inBitmap只有当inMutable为true的时候是可用的。
        options.inMutable = true;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        //压缩bitmap,防止OOM
        options.inSampleSize = findBestSampleSize(options.outWidth, options.outHeight,
                reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap reusedBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        options.inBitmap = reusedBitmap;
        return options;

    }


}
