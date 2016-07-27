package com.android.app.lib.cache;


import java.io.Serializable;

public class CacheItem implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 存储key
     */
    private final String key;
    /**
     * JSON字符串
     */
    private String data;
    /**
     * 过期时间的时间戳
     */
    private long timeStamp = 0L;

    public CacheItem(String key, String data, long timeStamp) {
        this.key = key;
        this.data = data;
        this.timeStamp = System.currentTimeMillis() + timeStamp * 1000;
    }

    public String getKey() {
        return key;
    }

    public String getData() {
        return data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
