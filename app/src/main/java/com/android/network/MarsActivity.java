package com.android.network;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.network.callback.BaseSend;
import com.android.network.callback.ISend;
import com.android.network.callback.SendCallback;
import com.android.network.model.MarsBean;
import com.android.testing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheyanxu on 16/7/26.
 */
public class MarsActivity extends AppCompatActivity {
    @BindView(R.id.main_bg)
    ImageView mImageView;
    @BindView(R.id.error)
    TextView mTxtError;
    @BindView(R.id.degrees)
    TextView mTxtDegrees;
    @BindView(R.id.weather)
    TextView mTxtWeather;
    ISend baseSend = new BaseSend();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mars);
        ButterKnife.bind(this);
        loadData();
        loadMarsImage();
    }

    private void loadData() {

        baseSend.marsData(new SendCallback() {
            @Override
            public <T> void onSuccessed(T t) {
                if (t instanceof MarsBean) {
                    MarsBean marsBean = (MarsBean) t;
                    mTxtDegrees.setText(marsBean.getDegrees());
                    mTxtWeather.setText(marsBean.getWeather());
                }
            }

            @Override
            public <T> void onError(T t) {
                mTxtError.setVisibility(View.VISIBLE);
            }
        });


    }

    private void loadMarsImage() {
        baseSend.marsImage(new SendCallback() {
            @Override
            public <T> void onSuccessed(T t) {
                if (t instanceof Bitmap) {
                    mImageView.setImageBitmap((Bitmap) t);
                }
            }

            @Override
            public <T> void onError(T t) {
                int mainColor = Color.parseColor("#FF5722");
                mImageView.setBackgroundColor(mainColor);
                Toast.makeText(getApplicationContext(), "load mars image error", Toast.LENGTH_LONG).show();
            }
        });
    }


}
