package com.android.network.callback;

/**
 * 定义回调接口
 */
public interface SendCallback {
    //请求成功回调
    <T> void onSuccessed(T t);

    //请求失败回调
    <T> void onError(T t);
}
