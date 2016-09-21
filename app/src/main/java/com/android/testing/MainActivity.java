package com.android.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.bitmap.BitmapActivity;
import com.android.calltele.CallTelActivity;
import com.android.fresco.FrescoActivity;
import com.android.https.HttpsActivity;
import com.android.launchmode.LaunchA;
import com.android.lazy.LazyFragmentActivity;
import com.android.loader.LoaderActivity;
import com.android.login.LoginActivity;
import com.android.mvp.MVPActivity;
import com.android.navigator.NavigatorActivity;
import com.android.network.MarsActivity;
import com.android.opengl.OpenGLActivity;
import com.android.rxretrofit.RxRetrofitActivity;
import com.android.softinput.SoftInputMethodActivity;
import com.android.time.CountTimeActivity;
import com.android.tv.DemoTvActivity;
import com.android.tv.DemoTvActivity_ViewBinder;
import com.android.tv.FocusActivity;
import com.android.tv.ThreeDAcitvity;
import com.android.views.BottomSheetBehaviorActivity;
import com.android.views.CoordinatorActivity;
import com.android.views.CustomViewsActivity;
import com.android.views.PropertyActivity;
import com.android.webview.MovieActivity;
import com.android.webview.WebActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btnMVP, R.id.btnHttps, R.id.btnNavigator, R.id.btnNetWork, R.id.loader, R.id.login,
            R.id.calltel, R.id.webView, R.id.movieWebView, R.id.customViews, R.id.launchMode, R.id.bitmap,
            R.id.lazyLoadFragment, R.id.coordinatorLayout, R.id.fresco, R.id.tv, R.id.threeD, R.id.softinput
            , R.id.countTime, R.id.bottomSheetBehavior, R.id.rxRetrofit, R.id.openGL, R.id.tvFocus
            , R.id.property})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMVP:
                startActivity(new Intent(MainActivity.this, MVPActivity.class));
                break;
            case R.id.btnHttps:
                startActivity(new Intent(MainActivity.this, HttpsActivity.class));
                break;
            case R.id.btnNavigator:
                startActivity(new Intent(MainActivity.this, NavigatorActivity.class));
                break;
            case R.id.btnNetWork:
                startActivity(new Intent(MainActivity.this, MarsActivity.class));
                break;
            case R.id.loader:
                startActivity(new Intent(MainActivity.this, LoaderActivity.class));
                break;
            case R.id.login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.calltel:
                startActivity(new Intent(MainActivity.this, CallTelActivity.class));
                break;
            case R.id.movieWebView:
                startActivity(new Intent(MainActivity.this, MovieActivity.class));
                break;
            case R.id.webView:
                startActivity(new Intent(MainActivity.this, WebActivity.class));
                break;
            case R.id.customViews:
                startActivity(new Intent(MainActivity.this, CustomViewsActivity.class));
                break;
            case R.id.launchMode:
                startActivity(new Intent(MainActivity.this, LaunchA.class));
                break;
            case R.id.bitmap:
                startActivity(new Intent(MainActivity.this, BitmapActivity.class));
                break;
            case R.id.lazyLoadFragment:
                startActivity(new Intent(MainActivity.this, LazyFragmentActivity.class));
                break;

            case R.id.coordinatorLayout:
                startActivity(new Intent(MainActivity.this, CoordinatorActivity.class));
                break;

            case R.id.bottomSheetBehavior:
                startActivity(new Intent(MainActivity.this, BottomSheetBehaviorActivity.class));
                break;
            case R.id.fresco:
                startActivity(new Intent(MainActivity.this, FrescoActivity.class));
                break;
            case R.id.tv:
                startActivity(new Intent(MainActivity.this, FocusActivity.class));
                break;
            case R.id.threeD:
                startActivity(new Intent(MainActivity.this, ThreeDAcitvity.class));
                break;
            case R.id.softinput:
                startActivity(new Intent(MainActivity.this, SoftInputMethodActivity.class));
                break;

            case R.id.countTime:

                startActivity(new Intent(MainActivity.this, CountTimeActivity.class));
                break;

            case R.id.rxRetrofit:
                startActivity(new Intent(MainActivity.this, RxRetrofitActivity.class));

                break;

            case R.id.openGL:
                startActivity(new Intent(MainActivity.this, OpenGLActivity.class));
                break;
            case R.id.tvFocus:
                startActivity(new Intent(MainActivity.this, DemoTvActivity.class));
                break;

            case R.id.property:
                startActivity(new Intent(MainActivity.this, PropertyActivity.class));
                break;

        }
    }
}
