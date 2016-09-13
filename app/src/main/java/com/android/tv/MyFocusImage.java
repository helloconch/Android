package com.android.tv;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MyFocusImage extends ImageView {
    ValueAnimator mAnimator;

    public MyFocusImage(Context context) {
        super(context);
    }

    public MyFocusImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void zoom() {

        float width = getWidth();
        float height = getHeight();

        float scaleX = (width + 32) / width;
        float scaleY = (height + 32) / height;


        // 1.设置目标属性名及属性变化的初始值和结束值
        PropertyValuesHolder mPropertyValuesHolderScaleX = PropertyValuesHolder
                .ofFloat("scaleX", 1.0f, scaleX);
        PropertyValuesHolder mPropertyValuesHolderScaleY = PropertyValuesHolder
                .ofFloat("scaleY", 1.0f, scaleY);
        mAnimator = ValueAnimator.ofPropertyValuesHolder(
                mPropertyValuesHolderScaleX, mPropertyValuesHolderScaleY);

        // 2.为目标对象的属性变化设置监听器
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.根据属性名获取属性变化的值分别为ImageView目标对象设置X和Y轴的缩放值
                float animatorValueScaleX = (Float) animation
                        .getAnimatedValue("scaleX");
                float animatorValueScaleY = (Float) animation
                        .getAnimatedValue("scaleY");
                setScaleX(animatorValueScaleX);
                setScaleY(animatorValueScaleY);

            }
        });
        // 4.为ValueAnimator设置自定义的Interpolator
        mAnimator.setInterpolator(new LinearInterpolator());
        // 5.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(300);
        // 6.为ValueAnimator设置目标对象并开始执行动画
        mAnimator.setTarget(this);
        mAnimator.start();
    }

    public void reset() {
        if (mAnimator != null) {
            mAnimator.reverse();
        }
    }
}
