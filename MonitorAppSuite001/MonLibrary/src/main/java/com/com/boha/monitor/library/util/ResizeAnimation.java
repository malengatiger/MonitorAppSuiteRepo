package com.com.boha.monitor.library.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by aubreyM on 14/12/04.
 */

public class ResizeAnimation extends Animation {
    View view;
    int startH;
    int endH;
    int diff;

    public ResizeAnimation(View v, int newh) {
        view = v;
        startH = v.getLayoutParams().height;
        endH = newh;
        diff = endH - startH;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = startH + (int) (diff * interpolatedTime);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}