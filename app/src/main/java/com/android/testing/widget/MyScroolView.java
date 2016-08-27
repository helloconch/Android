package com.android.testing.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 自定义一个View，通过Scroller控制其滑动
 */
public class MyScroolView extends RelativeLayout {

    Scroller mScroller;

    public MyScroolView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public MyScroolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    /**
     * @param dx 不是终点坐标，而是我们滑动的距离
     * @param dy 不是终点坐标，而是我们滑动的距离
     */
    public void smoothScroll(int dx, int dy) {


        int deltaX = dx - getScrollX();
        int deltaY = dy - getScrollY();

        mScroller.startScroll(getScrollX(),
                getScrollY(), deltaX, deltaY, 1000);


        invalidate();

//        调用invalidate会导致view的重绘在view的draw方法里，
//        我们可以发现它会调用到我们复写的computeScroll方法，
//        而这个方法又会调用postinvalidate方法来进行重绘，这样view的连续滑动就实现了。

    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
