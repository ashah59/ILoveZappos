package com.example.shah.ilovezappos;

import android.view.animation.Interpolator;

/**
 * Created by shah on 2/9/2017.
 */

public class BubbleInterpolator implements Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    BubbleInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {

        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
