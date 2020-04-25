package com.vaankdeals.newsapp.Class;

import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

public class DepthPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.95f;
    private static final float MIN_FADE = 0.8f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position < -1) { // [-Infinity,-1)
            view.setScaleX(0f);
            view.setScaleY(0f);
        } else if (position <= 0) { // [-1,0]
            ViewCompat.setTranslationZ(view, position);

            view.setTranslationY(1f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            ViewCompat.setTranslationZ(view, pageHeight *-position);

            // Counteract the default slide transition
            view.setTranslationY(pageHeight *-position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else if(position==0){
            ViewCompat.setTranslationZ(view, 0);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        }
        else{
            view.setScaleX(0f);
            view.setScaleY(0f);
        }
    }
}