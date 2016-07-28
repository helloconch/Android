package com.android.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheyanxu on 16/7/28.
 */
public class WebActivity extends AppCompatActivity {
    @BindView(R.id.myWebview)
    WebView myWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        myWebview.getSettings().setJavaScriptEnabled(true);
        myWebview.loadUrl("file:///android_asset/104.html");
        //html调用app页面方法
        myWebview.addJavascriptInterface(new JSInterface1(), "baobao");
    }

    @OnClick(R.id.changeColor)
    public void onClick() {
        //改变webview背景色
        String color = "#00ee00";
        //app调用html方法
        myWebview.loadUrl("javascript:changeColor('" + color + "');");
    }

    class JSInterface1 {
        @JavascriptInterface
        public void callAndroidMethod(int a, float b, String c, boolean d) {
            if (d) {
                String str = "-" + (a + 1) + "-" + (b + 1) + "-" + c + "-" + d;
                new AlertDialog.Builder(WebActivity.this).setTitle("title")
                        .setMessage(str).show();
            }
        }
    }

    public void gotoAnyWhere(String url) {
        if (url != null) {
            if (url.startsWith("gotoMovieDetail:")) {

            } else if (url.startsWith("gotoNewsList:")) {

            } else if (url.startsWith("gotoPersonCenter")) {

            }
        }
    }
}
