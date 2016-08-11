package com.android.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.testing.R;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheyanxu on 16/8/10.
 */
public class LaunchA extends AppCompatActivity {
    private final String TAG = LaunchA.class.getSimpleName();
    private MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_launcher_mode_a);
        ButterKnife.bind(this);
        myHandler.postDelayed(sRunnable, 1000 * 60 * 10);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
//        创建一个静态Handler内部类，然后对 Handler 持有的对象使用弱引用，这样在回收时也可以回收
//        Handler 持有的对象，但是这样做虽然避免了 Activity 泄漏，
//        不过 Looper 线程的消息队列中还是可能会有待处理的消息，所以我们在 Activity 的
//        Destroy 时或者 Stop 时应该移除消息队列 MessageQueue 中的消息。
//        myHandler.removeCallbacks();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    @OnClick(R.id.test)
    public void onClick() {
        Intent intent = new Intent(this, LaunchB.class);
        startActivity(intent);
    }


    private static final Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
        }
    };

    private static class MyHandler extends Handler {

        private final WeakReference<LaunchA> reference;

        public MyHandler(LaunchA activity) {
            super();
            reference = new WeakReference<LaunchA>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LaunchA launchA = reference.get();
            if (launchA != null) {

            }
        }
    }
}
