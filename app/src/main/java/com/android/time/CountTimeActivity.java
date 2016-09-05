package com.android.time;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/5.
 */
public class CountTimeActivity extends AppCompatActivity {

    @BindView(R.id.time)
    TextView time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_count_time);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.click)
    public void onClick() {
        //指定每1000ms做一个反应，直到5000ms结束
        MyCountTime myCountTime = new MyCountTime(5000, 1000);
        myCountTime.start();

    }

    private class MyCountTime extends CountDownTimer {
        public MyCountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            time.setText("left:" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            time.setText("Done");
        }


    }
}
