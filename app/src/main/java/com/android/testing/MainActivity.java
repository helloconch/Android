package com.android.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.calltele.CallTelActivity;
import com.android.https.HttpsActivity;
import com.android.loader.LoaderActivity;
import com.android.login.LoginActivity;
import com.android.mvp.MVPActivity;
import com.android.navigator.NavigatorActivity;
import com.android.network.MarsActivity;
import com.android.views.CustomViewsActivity;
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
            R.id.calltel, R.id.webView, R.id.movieWebView, R.id.customViews})
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
        }
    }
}
