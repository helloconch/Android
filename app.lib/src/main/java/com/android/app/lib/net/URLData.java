package com.android.app.lib.net;

public class URLData {
    private String key;
    //用来做数据缓存
    private long expires;
    private String netType;
    private String url;
    private String mockClass;

    public String getMockClass() {
        return mockClass;
    }

    public void setMockClass(String mockClass) {
        this.mockClass = mockClass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
