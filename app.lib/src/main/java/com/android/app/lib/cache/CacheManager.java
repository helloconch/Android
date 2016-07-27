package com.android.app.lib.cache;

import android.os.Environment;

import com.android.app.lib.utils.BaseUtils;

import java.io.File;

/**
 * 用于操作读写缓存数据，并判断缓存数据是否过期
 */
public class CacheManager {

    /**
     * 缓存文件路径
     */
    public static final String APP_CACHE_PATH = Environment.getExternalStorageDirectory()
            .getPath() + File.separator + "BasicApp" + File.separator + "appdata" + File.separator;

    /**
     * sdcard 最小空间，如果小于10M，不会再向sdcard里面写入任何数据
     */
    public static final long SDCARD_MIN_SPACE = 1024 * 1024 * 10;

    private static CacheManager instance = null;

    private CacheManager() {

    }

    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    /**
     * 从文件缓存中取出缓存，没有则返回空
     *
     * @param key
     * @return
     */
    public String getFileCache(String key) {
        String md5Key = BaseUtils.getMd5(key);
        if (contains(md5Key)) {
            final CacheItem item = getFromCache(md5Key);
            if (item != null) {
                return item.getData();
            }
        }
        return null;
    }


    public boolean contains(String key) {

        File file = new File(APP_CACHE_PATH + key);
        return file.exists();
    }


    /***
     * 存放缓存数据
     *
     * @param key
     * @param content
     * @param expires
     */
    public void putFileCache(String key, String content, long expires) {

        String md5Key = BaseUtils.getMd5(key);
        CacheItem item = new CacheItem(md5Key, content, expires);
        putIntoCache(item);

    }

    /**
     * 初始化目录
     * Application类中进行
     */
    public void initCacheDir() {


        //sdcard已经挂载，并且剩余空间大于10m，可以写入文件，小于10m，清除缓存

        if (BaseUtils.sdcardMounted()) {
            if (BaseUtils.getSDSize() < SDCARD_MIN_SPACE) {
                clearAllData();
            } else {
                File dir = new File(APP_CACHE_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }

    }

    /**
     * 将CacheItem从磁盘读取出来
     *
     * @param key
     * @return
     */
    synchronized CacheItem getFromCache(String key) {
        CacheItem cacheItem = null;
        Object findItem = BaseUtils.restoreObject(APP_CACHE_PATH + key);
        if (findItem != null) {
            cacheItem = (CacheItem) findItem;
        }
        //缓存不存在
        if (cacheItem == null) {
            return null;
        }

        //缓存过期
        long a = System.currentTimeMillis();
        if (a > cacheItem.getTimeStamp()) {
            return null;
        }
        return cacheItem;

    }

    /**
     * 将CacheItem缓存到磁盘
     *
     * @param item
     * @return
     */
    synchronized boolean putIntoCache(CacheItem item) {
        if (BaseUtils.getSDSize() > SDCARD_MIN_SPACE) {

            BaseUtils.saveObject(APP_CACHE_PATH + item.getKey(), item);

            return true;

        }
        return false;
    }


    /**
     * 清除缓存文件
     */
    void clearAllData() {

        File file = null;
        File[] files = null;
        if (BaseUtils.sdcardMounted()) {
            file = new File(APP_CACHE_PATH);
            files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    file2.delete();
                }
            }
        }

    }
}
