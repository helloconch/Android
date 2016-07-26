package com.android.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.https.HttpsActivity;
import com.android.mvp.MVPActivity;
import com.android.navigator.NavigatorActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btnMVP, R.id.btnHttps,R.id.btnNavigator})
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
        }
    }
}
