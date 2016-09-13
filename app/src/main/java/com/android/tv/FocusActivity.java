package com.android.tv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/2.
 */
public class FocusActivity extends AppCompatActivity {

    @BindView(R.id.one)
    Button one;
    @BindView(R.id.two)
    Button two;
    @BindView(R.id.three)
    Button three;
    @BindView(R.id.four)
    Button four;
    @BindView(R.id.five)
    Button five;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        ButterKnife.bind(this);

        // 控件可以获取焦点,调用setFocusable(true)
        one.setFocusable(true);
        one.setFocusableInTouchMode(true);
        // 控件请求并保持焦点,用requestFoucs()方法
        // 监控控件焦点状态的变化，用setOnFocusChangeListener()方法，根据焦点的状态值，来设置控件的背景色

//		two.requestFocus();

        one.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        one.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        showLog("子视图-KEYCODE_DPAD_DOWN");
                        break;
                }
                //传递给Activity onKeyDown()
                return false;
                //不传递给Activity onKeyDown()
//				return true;
            }
        });

        one.setNextFocusDownId(R.id.three);
        three.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                System.out.print("");

                return false;
            }
        });
    }

    private void showLog(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            // 捕获遥控设备下
            case KeyEvent.KEYCODE_DPAD_DOWN:
                showLog("KEYCODE_DPAD_DOWN");
                break;
            // 捕获遥控设备上
            case KeyEvent.KEYCODE_DPAD_UP:
                showLog("KEYCODE_DPAD_UP");
                break;
            // 捕获遥控设备左
            case KeyEvent.KEYCODE_DPAD_LEFT:
                showLog("KEYCODE_DPAD_LEFT");
                break;
            // 捕获遥控设备右边
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                showLog("KEYCODE_DPAD_RIGHT");
                break;
            // 捕获遥控设备中心键
            case KeyEvent.KEYCODE_DPAD_CENTER:
                showLog("KEYCODE_DPAD_CENTER");
                break;
            // 捕获遥控设备返回键
            case KeyEvent.KEYCODE_BACK:
                showLog("KEYCODE_BACK");
                break;

        }
        //事件传递结束，不在继续传递
//		return true;
        //事件继续传递
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one:
                break;
            case R.id.two:
                break;
            case R.id.three:
                break;
            case R.id.four:
                break;
            case R.id.five:
                startActivity(new Intent(FocusActivity.this, DemoTvActivity.class));
                break;
        }
    }
}
