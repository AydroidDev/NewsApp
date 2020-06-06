package com.vaankdeals.newsapp.ViewTypes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.vaankdeals.newsapp.R;

import java.util.Random;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class NativeFragment extends Fragment {

    private CardView cardView;
    private UnifiedNativeAd unifiedNativeAd;

    public NativeFragment() {
        // Required empty public constructor
    }

    public NativeFragment(UnifiedNativeAd unifiedNativeAd) {

        this.unifiedNativeAd = unifiedNativeAd;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_native, container, false);
        cardView = view.findViewById(R.id.ad_container);
        loadAdsNative();
        return view;
    }

    private void loadAdsNative() {

        UnifiedNativeAdView adView = (UnifiedNativeAdView) getActivity().getLayoutInflater().inflate(R.layout.ad_google_new, null);
        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        adView.setBackgroundColor(randomAndroidColor);
        populateNativeAdView(unifiedNativeAd, adView);
        cardView.removeAllViews();
        cardView.addView(adView);

    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
// Some assets are guaranteed to be in every UnifiedNativeAd.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));

        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            adView.getBodyView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }




        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
