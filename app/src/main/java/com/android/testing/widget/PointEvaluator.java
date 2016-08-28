package com.android.testing.widget;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Created by cheyanxu on 16/8/27.
 */
public class PointEvaluator implements TypeEvaluator {


    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {


        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;

        int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));

        int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));

        return new Point(x, y);
    }
}
