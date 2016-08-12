package com.android.app.lib.utils;


import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.android.app.lib.net.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class BaseUtils {
    public static String UrlEncodeUnicode(final String s) {
        if (s == null) {
            return null;
        }
        final int length = s.length();
        final StringBuilder builder = new StringBuilder(length); // buffer
        for (int i = 0; i < length; i++) {
            final char ch = s.charAt(i);
            if ((ch & 0xff80) == 0) {
                if (BaseUtils.IsSafe(ch)) {
                    builder.append(ch);
                } else if (ch == ' ') {
                    builder.append('+');
                } else {
                    builder.append('%');
                    builder.append(BaseUtils.IntToHex((ch >> 4) & 15));
                    builder.append(BaseUtils.IntToHex(ch & 15));
                }
            } else {
                builder.append("%u");
                builder.append(BaseUtils.IntToHex((ch >> 12) & 15));
                builder.append(BaseUtils.IntToHex((ch >> 8) & 15));
                builder.append(BaseUtils.IntToHex((ch >> 4) & 15));
                builder.append(BaseUtils.IntToHex(ch & 15));
            }
        }
        return builder.toString();
    }

    static char IntToHex(final int n) {
        if (n <= 9) {
            return (char) (n + 0x30);
        }
        return (char) ((n - 10) + 0x61);
    }

    static boolean IsSafe(final char ch) {
        if ((((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) || ((ch >= '0') && (ch <= '9'))) {
            return true;
        }
        switch (ch) {
            case '\'':
            case '(':
            case ')':
            case '*':
            case '-':
            case '.':
            case '_':
            case '!':
                return true;
        }
        return false;
    }

    /**
     * MD5运算
     *
     * @param s
     * @return String 返回密文
     */
    public static String getMd5(final String s) {
        try {
            // Create MD5 Hash
            final MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.trim().getBytes());
            final byte messageDigest[] = digest.digest();
            return BaseUtils.toHexString(messageDigest);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    // MD5相关函数
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 转换为十六进制字符串
     *
     * @param b byte数组
     * @return String byte数组处理后字符串
     */
    public static String toHexString(final byte[] b) {// String to byte
        final StringBuilder sb = new StringBuilder(b.length * 2);
        for (final byte element : b) {
            sb.append(BaseUtils.HEX_DIGITS[(element & 0xf0) >>> 4]);
            sb.append(BaseUtils.HEX_DIGITS[element & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 检查是否安装了sd卡
     *
     * @return false 未安装
     */
    public static boolean sdcardMounted() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        }
        return false;
    }

    /**
     * 获取SD卡剩余空间的大小
     *
     * @return long SD卡剩余空间的大小（单位：byte）
     */
    public static long getSDSize() {
        final String str = Environment.getExternalStorageDirectory().getPath();
        final StatFs localStatFs = new StatFs(str);
        final long blockSize;
        final long blockLong;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = localStatFs.getBlockSizeLong();
            blockLong = localStatFs.getAvailableBlocksLong();
        } else {
            blockSize = localStatFs.getBlockSize();
            blockLong = localStatFs.getAvailableBlocks();
        }

        return blockLong * blockSize;
    }

    public static final void saveObject(String path, Object saveObject) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File f = new File(path);
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(saveObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static final Object restoreObject(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object object = null;
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
            return object;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static final Date getServerTime() {
        return HttpRequest.getServerTime();
    }


//******************************单位换算********************************

    /**
     * 获取屏幕宽高
     */
    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转为 dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    //******************************设备尺寸********************************

    /**
     * 获取设备尺寸
     *
     * @param context
     * @return
     */
    public static int[] getScreenWithAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * @param actualWidth   图片实际的宽度，也就是options.outWidth
     * @param actualHeight  图片实际的高度，也就是options.outHeight
     * @param desiredWidth  你希望图片压缩成为的目的宽度
     * @param desiredHeight 你希望图片压缩成为的目的高度
     * @return
     */
    public static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        //这里我们为什么要寻找 与ratio最接近的2的倍数呢？
        //原因就在于API中对于inSimpleSize的注释：最终的inSimpleSize应该为2的倍数，我们应该向上取与压缩比最接近的2的倍数。
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }

}
