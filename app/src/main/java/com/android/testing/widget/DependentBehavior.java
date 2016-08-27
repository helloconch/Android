package com.android.testing.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by cheyanxu on 16/8/27.
 */
public class DependentBehavior extends CoordinatorLayout.Behavior<View> {
    public DependentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 依赖在那个视图上layoutDependsOn的返回值决定了一切！
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof ImageView;
    }


    /**
     * @param parent     当前的CoordinatorLayout
     * @param child      设置这个Behavior的View
     * @param dependency 关心的那个View
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //设置好了依赖，接下来就是当这个被依赖View状态发生变化的时候，我们View也发生改变
        int offset = dependency.getTop() - child.getTop();
        ViewCompat.offsetTopAndBottom(child, offset);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
