package com.android.testing.imageloader;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class LruCacheUtils {
    public static void lruCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }
}
