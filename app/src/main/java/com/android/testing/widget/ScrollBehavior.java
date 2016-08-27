package com.android.testing.widget;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 一个ScroolView滚动 参考另一个ScroolView
 */
public class ScrollBehavior extends CoordinatorLayout.Behavior<View> {
    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

//        返回值表明这次滑动我们要不要关心，我们要关心什么样的滑动？当然是y轴方向上的。
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;

    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        int leftScrolled = target.getScrollY();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            child.setScrollY(leftScrolled);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {

        ((NestedScrollView) child).fling((int) velocityY);

        return true;
    }
}
