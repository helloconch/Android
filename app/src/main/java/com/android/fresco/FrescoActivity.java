package com.android.fresco;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.testing.R;
import com.android.testing.utils.FrescoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheyanxu on 16/8/27.
 */
public class FrescoActivity extends AppCompatActivity {
    @BindView(R.id.my_image_view)
    SimpleDraweeView myImageView;
    @BindView(R.id.my_image_view2)
    SimpleDraweeView myImageView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);
        ButterKnife.bind(this);

        FrescoUtils.loadUrl(
                "http://d.hiphotos.baidu.com/image/h%3D200/sign=6008b360f336afc3110c38658318eb85/a1ec08fa513d26973aa9f6fd51fbb2fb4316d81c.jpg"
                , myImageView, null, 300, 300, null);


        FrescoUtils.loadUrl("http://img2.imgtn.bdimg.com/it/u=1356561084,1172432264&fm=21&gp=0.jpg"
                , myImageView2, null, 300, 300, null);

        FrescoUtils.setCircle(myImageView2, Color.WHITE);
    }
}
