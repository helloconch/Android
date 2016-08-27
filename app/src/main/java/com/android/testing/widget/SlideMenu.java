package com.android.testing.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 实现一个滑动菜单
 */
public class SlideMenu extends ViewGroup {

    //x轴的上一次位置
    private int mLastX;

    //弹性滑动
    private Scroller mScrooler;


    //标记状态，默认true显示的为主界面

    private boolean mIsMain = true;


    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScrooler = new Scroller(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //菜单
        View left = this.getChildAt(0);
        left.measure(left.getLayoutParams().width,
                heightMeasureSpec);
        //主视图
        View main = this.getChildAt(1);
        main.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //先将菜单视图进行隐藏
        View left = this.getChildAt(0);
        left.layout(-left.getLayoutParams().width, 0, 0, b);

        //将主视图进行显示
        View main = this.getChildAt(1);
        main.layout(l, t, r, b);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //getX()返回的相对于当前view左边缘的横轴距离
                //getRawX()返回的是相对于屏幕左边缘的距离
                mLastX = (int) event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();

                //上一次x轴位置减去当前X轴的值当作本次滑动的增量
                int deltaX = mLastX - x;

                Log.i("AABBCC", "deltaX:" + deltaX);
                //加上原本内容在X轴的偏移量得到它将要偏移量
                int scrollX = getScrollX() + deltaX;
                if (scrollX > 0) {
                    //大于0代表侧滑菜单隐藏，直接scrollTo(0,0)
                    scrollTo(0, 0);
                } else if (scrollX < -getChildAt(0).getWidth()) {
                    //小于侧滑菜单宽度的负值时候，代表侧滑菜单已经完全显示出来
                    scrollTo(-getChildAt(0).getWidth(), 0);
                } else {
                    //此时侧滑菜单没有完全隐藏，也没有完全显示，按照增量进行显示
                    scrollBy(deltaX, 0);
                }
                mLastX = x;
                break;

            case MotionEvent.ACTION_UP:
                //获取手指抬起时内容偏移量
                int upX = getScrollX();

                if (upX > -this.getChildAt(0).getWidth() / 2) {
                    //滑动大于左侧菜单的一半，则将主界面显示出来
                    mIsMain = true;
                } else {
                    mIsMain = false;
                }

                switchScreen();

                break;
        }


        return true;
    }


    private void switchScreen() {

        int startX = getScrollX();
        int deltaX;
        if (mIsMain) {
            //显示主界面，则用目标的偏移x点是0
            deltaX = 0 - startX;
        } else {
            // 如果要显示的是菜单界面则目标的偏移x点是菜单的左边界
            deltaX = -getChildAt(0).getLayoutParams().width - startX;
        }

        mScrooler.startScroll(startX, 0, deltaX, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScrooler.computeScrollOffset()) {
            scrollTo(mScrooler.getCurrX(), mScrooler.getCurrY());
            postInvalidate();
        }
    }

    public void switchMenu() {
        mIsMain = !mIsMain;
        switchScreen();
    }
}
