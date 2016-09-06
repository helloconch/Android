package com.android.testing.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MyLineView extends View {

    private final String TAG = MyLineView.class.getSimpleName();
    int width;
    int height;
    //左端点x
    int leftX;
    //右端点X
    int rightX;
    int margin = 32;
    int y;
    //线段长度
    int distance;
    int radius = 16;


    float factor = 1;


    Paint mPaint;
    android.animation.ValueAnimator mAnimator;

    public MyLineView(Context context) {
        super(context);
        init();
    }

    public MyLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            Log.i(TAG, "EXACTLY :width:" + width);
        } else {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
            Log.i(TAG, "NO EXACTLY :width:" + width);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            Log.i(TAG, "EXACTLY :height:" + height);
        } else {
            height = ViewGroup.LayoutParams.MATCH_PARENT;
            Log.i(TAG, "NO EXACTLY :height:" + height);
        }

        leftX = margin;
        rightX = width - margin;

        y = height / 2;

        distance = width - (2 * margin + 2 * radius);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 通过onLayout()方法知道这个控件应该放在哪个位置。同样，是一个自上而下的方法。
     * 一般我们只有在重写ViewGroup的时候需要自己处理onLayout()方法，
     * 因为该方法主要是ViewGroup用于摆放子view位置的（如：水平摆放或者垂直摆放，在这里同学们可以参考一下LinearLayout的onLayout()方法的实现），
     * 一般我们只继承View来定制我们的自定义View的时候，都不需要重写该方法。不过需要注意的一点是，子view的margin属性是否生效就要看parent是否在自身的onLayout方法进行处理，
     * 而view得padding属性是在onDraw方法中生效的。
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制左圆形
        canvas.drawCircle(leftX, y, radius * factor, mPaint);

        //绘制右圆形
        canvas.drawCircle(rightX, y, radius * (1 - factor), mPaint);

        //绘制线段
        canvas.drawLine(margin, y, margin + radius + ((radius + distance) * (1 - factor)), y, mPaint);
    }


    public void startAnimation() {

        mAnimator = android.animation.ValueAnimator.ofFloat(1, 0);
        mAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                factor = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.setDuration(1500);
        // 重复次数 无限循环
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // 重复模式, RESTART: 重新开始 REVERSE:恢复初始状态再开始
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.start();
    }


    public void stopAnimation() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

}
