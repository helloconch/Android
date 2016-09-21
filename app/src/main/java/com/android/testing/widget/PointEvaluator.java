package com.android.testing.widget;

import android.animation.TypeEvaluator;

import com.android.views.Point;


/**
 * Created by cheyanxu on 16/8/27.
 */
public class PointEvaluator implements TypeEvaluator {


    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {


        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;

        int x = (int) (startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX()));

        int y = (int) (startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY()));

        return new Point(x, y);
    }
}
