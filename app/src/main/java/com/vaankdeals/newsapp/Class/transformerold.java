package com.vaankdeals.newsapp.Class;

import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

public class transformerold implements ViewPager2.PageTransformer {
    public void transformPage(View view, float position) {
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
            ViewCompat.setTranslationZ(view, pageHeight *-position);
            view.setTranslationY(pageHeight *-position);
            float MIN_SCALE = 0.95f;
            if(position>=1){
                MIN_SCALE =0f;
            }
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
