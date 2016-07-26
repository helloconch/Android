package com.android.navigator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.android.testing.R;
import com.android.testing.widget.navigation.PagerSlidingTabStrip;
import com.android.testing.widget.navigation.SlideTabView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheyanxu on 16/7/26.
 */
public class NavigatorActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.slideTabs)
    SlideTabView slideTabs;
    private int currentColor = 0xFF666666;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);
        ButterKnife.bind(this);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());

        for (int i = 0; i < 7; i++) {
            tabs.addTextTab(i, "item" + i);
            tabs.setIndicatorColor(currentColor);
        }


        String items[] = new String[]{"全部", "待付款", "待收货", "已完成", "待评价"};
        slideTabs.addTextTab(items);

    }
}
