package com.android.testing;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.testing.exception.CrashHandler;

/**
 * Created by cheyanxu on 16/7/25.
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //在代码中加入multidex功能
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getsInstance();
        crashHandler.init(this);
    }
}
