package com.vaankdeals.newsapp.Class;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {


    private static final float MIN_SCALE = 0.80f;

    @Override
    public void transformPage(View page, float position) {

        if (position < -1) {    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 0) {    //// [-1,0]
            page.setAlpha(1);
            page.setTranslationX(page.getWidth()*-position);
            page.setTranslationY(page.getHeight() * position);
            page.setScaleX(1);
            page.setScaleY(1);

        } else if (position <= 1) {    // (0,1]

            page.setAlpha(1-position);
            page.setTranslationX(page.getWidth()*-position);
            page.setTranslationY(0);
            float scaleFactor = MIN_SCALE + (1-MIN_SCALE)*(1-Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else {    // (1,+Infinity]
            // This page is way off-screen to the right.

            page.setAlpha(0);
        }

    }
}
