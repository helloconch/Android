package com.android.testing.widget;

import android.animation.TimeInterpolator;

/**
 * Created by cheyanxu on 16/8/28.
 */
public class LgDecelerateInterpolator implements TimeInterpolator {


    private float background;

    public LgDecelerateInterpolator() {
        background = 10;
    }

    @Override
    public float getInterpolation(float input) {
        return (1 - (float) Math.pow(background, -input));
    }
}
