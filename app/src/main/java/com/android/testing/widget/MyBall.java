package com.android.testing.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by cheyanxu on 16/8/27.
 */
public class MyBall extends View {
    Paint mPaint;
    float xPoint = 30;
    boolean move = true;
    Point mPoint = new Point(30, 30);
    private int mColor;

    public MyBall(Context context) {
        this(context, null);
    }

    public MyBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mColor = Color.GREEN;
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (move)
            canvas.drawCircle(xPoint, 30, 30, mPaint);
        else
            canvas.drawCircle(mPoint.x
                    , mPoint.y, 30, mPaint);
    }


    public void start() {
        move = true;
        final ValueAnimator animator = ValueAnimator.ofFloat(60, 600);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取到动画每次变得float值，赋值给xpoint
                xPoint = (Float) valueAnimator.getAnimatedValue();

                //通知view重新绘制
                invalidate();

            }
        });
        animator.start();
    }


    public void start1() {
        move = false;
        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(),
                new Point(30, 30), new Point(600, 600));
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取到动画每次变得float值，赋值给xpoint
                mPoint = (Point) valueAnimator.getAnimatedValue();
                //通知view重新绘制
                invalidate();

            }
        });
        animator.start();
    }

    @SuppressWarnings("NewApi")
    public void start2() {

        move = false;
        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(),
                new Point(60, 60), new Point(600, 600));
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取到动画每次变得float值，赋值给xpoint
                mPoint = (Point) valueAnimator.getAnimatedValue();
                //通知view重新绘制
                invalidate();

            }
        });


        ValueAnimator animator1 = ValueAnimator.ofArgb(0xFFF00000, 0xFFFFFF00);
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mColor = (int) valueAnimator.getAnimatedValue();
                mPaint.setColor(mColor);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.setInterpolator(new LgDecelerateInterpolator());
        animatorSet.play(animator).with(animator1);
        animatorSet.start();

    }
}
