package com.vaankdeals.newsapp.Class;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.viewpager2.widget.ViewPager2;

public class DepthPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_ALPHA = 0.5f;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void transformPage(View view, float position) {
        int pageWidth = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0f);


        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1f);
            view.setTranslationY(0f);
            view.setTranslationZ(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationY(pageWidth * -position);
            float MIN_SCALE = 0.95f;
            if(position>=1){
                MIN_SCALE =0f;
            }
            // Move it behind the left page
            view.setTranslationZ(-1f);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0f);
        }
    }
}