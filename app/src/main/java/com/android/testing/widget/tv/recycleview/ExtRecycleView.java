package com.android.testing.widget.tv.recycleview;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class ExtRecycleView extends RecyclerView{
    public ExtRecycleView(Context context) {
        super(context);
    }

    public ExtRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
