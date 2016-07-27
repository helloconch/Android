package com.android.app.lib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by cheyanxu on 16/7/27.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    /**
     * 初始化变量，包括Intent带的数据和Activity内的变量
     */
    protected abstract void initVariables();

    /**
     * 加载layout布局文件，初始化控件，为控件挂上事件监听
     *
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 加载数据
     */
    protected abstract void loadData();
}
