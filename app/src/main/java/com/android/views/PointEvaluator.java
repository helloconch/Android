package com.android.views;

import android.animation.TypeEvaluator;

/**
 * 自定义PointEvaluator
 */

public class PointEvaluator implements TypeEvaluator {

    /**
     * 第一个参数fraction非常重要，这个参数用于表示动画的完成度的，我们应该根据它来计算当前动画的值应该是多少，第二第三个参数分别表示动画的初始值和结束值。
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {

        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        Point point = new Point(x, y);
        return point;
    }
}
