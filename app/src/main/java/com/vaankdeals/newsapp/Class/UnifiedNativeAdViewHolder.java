
package com.vaankdeals.newsapp.Class;


import android.view.View;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.vaankdeals.newsapp.R;

import androidx.recyclerview.widget.RecyclerView;

public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {

    private final UnifiedNativeAdView adView;

    public UnifiedNativeAdView getAdView() {
        return adView;
    }

    public UnifiedNativeAdViewHolder(View view) {
        super(view);
        adView = view.findViewById(R.id.ad_view);

        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
    }
}
