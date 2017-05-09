package com.android.testing;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.android.app.lib.cache.CacheManager;
import com.android.testing.exception.CrashHandler;
import com.android.testing.utils.FrescoUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by cheyanxu on 16/7/25.
 */
public class App extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = App.class.getName();
    private static App mInstance;
    //Volley请求队列
    RequestQueue requestQueue;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //在代码中加入multidex功能
        MultiDex.install(this);
        CacheManager.getInstance().initCacheDir();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        requestQueue = Volley.newRequestQueue(this);
        FrescoUtils.init(this, 30);
        CrashHandler crashHandler = CrashHandler.getsInstance();
        crashHandler.init(this);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        getRequestQueue().cancelAll(TAG);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
