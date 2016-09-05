package com.android.bitmap;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.testing.R;
import com.android.testing.utils.AppUtils;

/**
 * Created by cheyanxu on 16/8/12.
 */
public class BitmapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        BitmapFactory.Options options = AppUtils.decodeSameBitmap(this, R.drawable.ad, 500, 300);

        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new ViewGroup.LayoutParams(500, 300));
        iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ad, options));
        linearLayout.addView(iv);

        ImageView iv1 = new ImageView(this);
        iv1.setLayoutParams(new ViewGroup.LayoutParams(500, 300));
        iv1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ad, options));
        linearLayout.addView(iv1);

        ImageView iv2 = new ImageView(this);
        iv2.setLayoutParams(new ViewGroup.LayoutParams(500, 300));
        iv2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ad, options));
        linearLayout.addView(iv2);



        setContentView(linearLayout);
    }
}
