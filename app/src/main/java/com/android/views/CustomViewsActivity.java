package com.android.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.testing.R;
import com.android.testing.widget.stepview.HorizontalStepView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheyanxu on 16/8/3.
 */
public class CustomViewsActivity extends AppCompatActivity {


    HorizontalStepView horizontalStepView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        horizontalStepView = (HorizontalStepView) findViewById(R.id.horizontalStepView);
        List<String> datas = new ArrayList<>();
        datas.add("周一");
        datas.add("周二");
        datas.add("周三");
        datas.add("周四");
        datas.add("周五");
        datas.add("周六");
        datas.add("周日");
        horizontalStepView.setStepViewTexts(datas).setStepsViewIndicatorComplectingPosition(3);
    }
}
