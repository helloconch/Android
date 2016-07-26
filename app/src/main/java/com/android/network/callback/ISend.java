package com.android.network.callback;

/**
 * 定义数据请求的接口
 */
public interface ISend {
    /**
     * 当前网络请求框架
     */
    NetRequestType netRequestType = NetRequestType.VOLLEY;

    /**
     * 获取火星上数据
     *
     * @param
     * @param callback
     */
    void marsData(SendCallback callback);

    /**
     * 获取火星图片
     *
     * @param callback
     */
    void marsImage(SendCallback callback);

}
