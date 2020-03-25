package com.vaankdeals.newsapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.vaankdeals.newsapp.Class.UnifiedNativeAdViewHolder;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Object> mNewsList =  new ArrayList<>();
    private static final int NEWS_IMAGE_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    private static final int FULL_IMAGE_TYPE = 2;
    private static final int WEBVIEW_TYPE = 3;
    private static final int CUSTOM_AD_TYPE = 4;
    private static final int VIDEO_NEWS_TYPE = 5;

    private newsOutListener mNewsOutListener;
    private videoClickListener mVideoClickListener;

    public interface videoClickListener{
        void videoActivity(int position);
    }
    public void setvideoClickListener(videoClickListener listener){
        mVideoClickListener = listener;
    }
    public interface newsOutListener{
        void newsDetailActivity(int position);
    }
    public void setnewsOutListener(newsOutListener listener){
        mNewsOutListener = listener;
    }




    public NewsAdapter(Context context, List<Object> mNewsList) {
        this.mNewsList = mNewsList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case NEWS_IMAGE_TYPE:
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsitem, viewGroup, false);
// set the view's size, margins, paddings and layout parameters
                return new NewsViewHolder(v);
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.ad_unified_news,
                        viewGroup, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case FULL_IMAGE_TYPE:
                View imageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fullphotoitem, viewGroup, false);
                return new FullImageViewHolder(imageView);
            case VIDEO_NEWS_TYPE:
                View videoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsvideoitem, viewGroup, false);
                return new NewsVideoViewHolder(videoView);
            case WEBVIEW_TYPE:
                View webView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.webview_item, viewGroup, false);
                return new WebViewViewHolder(webView);

            case CUSTOM_AD_TYPE:
                View customAd = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customad, viewGroup, false);
                return new CustomAdViewHolder(customAd);




        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = mNewsList.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        } else {
            NewsModel object = (NewsModel) mNewsList.get(position);
            switch (object.getmNewsType()) {
                case "1":
                    return NEWS_IMAGE_TYPE;
                case "2":
                    return FULL_IMAGE_TYPE;
                case "3":
                    return WEBVIEW_TYPE;
                case "4":
                    return CUSTOM_AD_TYPE;
                case "5":
                    return VIDEO_NEWS_TYPE;
                default:
                    return -1;
            }
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) mNewsList.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
                break;

            case WEBVIEW_TYPE:
                NewsModel webview = (NewsModel) mNewsList.get(position);
                final WebViewViewHolder webViewViewHolder = (WebViewViewHolder) holder;


                String web_url = webview.getmNewslink();


                webViewViewHolder.mWebView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(request.toString());
                        return true;
                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webViewViewHolder.mProgress.setVisibility(View.GONE);
                    }
                });

                webViewViewHolder.mWebView.getSettings().setJavaScriptEnabled(true);
                webViewViewHolder.mWebView.loadUrl(web_url);


                break;
            case CUSTOM_AD_TYPE:
                NewsModel customad = (NewsModel) mNewsList.get(position);
                CustomAdViewHolder customAdViewHolder = (CustomAdViewHolder) holder;

                String customadimage = customad.getmNewsImage();
                Glide.with(mContext).load(customadimage).into(customAdViewHolder.mNewsImage);
                break;
            case FULL_IMAGE_TYPE:
                NewsModel image = (NewsModel) mNewsList.get(position);
                FullImageViewHolder fullImageViewHolder = (FullImageViewHolder) holder;

                String full_image = image.getmNewsImage();
                Glide.with(mContext).load(full_image).into(fullImageViewHolder.mNewsImage);
                break;
            case NEWS_IMAGE_TYPE:
                NewsModel object = (NewsModel) mNewsList.get(position);
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;

                String news_head = object.getmNewsHead();
                String news_desc = object.getmNewsDesc();
                String news_image = object.getmNewsImage();
                String news_source = object.getmNewsSource();
                String news_day = object.getmNewsDay();
                String news_extra = "click on title to read more on " + news_source + " / " + news_day;

                newsViewHolder.mNewsExtra.setText(news_extra);
                newsViewHolder.mNewsHead.setText(news_head);
                newsViewHolder.mNewsDesc.setText(news_desc);
                Glide.with(mContext).load(news_image).into(newsViewHolder.mNewsImage);
                break;

            case VIDEO_NEWS_TYPE:
                NewsModel video_news = (NewsModel) mNewsList.get(position);
                NewsVideoViewHolder newsVideoViewHolder = (NewsVideoViewHolder) holder;

                String news_head_video = video_news.getmNewsHead();
                String news_desc_video = video_news.getmNewsDesc();
                String news_image_video = video_news.getmNewsImage();
                String news_source_video = video_news.getmNewsSource();
                String news_day_video = video_news.getmNewsDay();
                String news_extra_video = "click on title to read more on " + news_source_video + " / " + news_day_video;

                newsVideoViewHolder.mNewsVideoExtra.setText(news_extra_video);
                newsVideoViewHolder.mNewsVideoHead.setText(news_head_video);
                newsVideoViewHolder.mNewsVideoDesc.setText(news_desc_video);
                Glide.with(mContext).load(news_image_video).into(newsVideoViewHolder.mNewsVideoImage);
                break;

        }
    }
    public class WebViewViewHolder extends RecyclerView.ViewHolder{

        public WebView mWebView;
        private ProgressBar mProgress;

        public WebViewViewHolder(@NonNull View itemView) {
            super(itemView);

            mWebView = itemView.findViewById(R.id.webview_item);
            mProgress = itemView.findViewById(R.id.progressBar);
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView mNewsHead;
        public TextView mNewsDesc;
        public ImageView mNewsImage;
        public TextView mNewsExtra;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsHead = itemView.findViewById(R.id.news_head);
            mNewsDesc = itemView.findViewById(R.id.news_desc);
            mNewsImage = itemView.findViewById(R.id.news_image);
            mNewsExtra = itemView.findViewById(R.id.news_extra);
            mNewsHead.setOnClickListener(v -> {
                mNewsOutListener.newsDetailActivity(getAdapterPosition());
                mNewsHead.setTextColor(Color.parseColor("#ffa500"));
            });

        }
    }
    public class NewsVideoViewHolder extends RecyclerView.ViewHolder{

        public TextView mNewsVideoHead;
        public TextView mNewsVideoDesc;
        public ImageView mNewsVideoImage;
        public TextView mNewsVideoExtra;
        public ImageView mNewsVideoPlay;

        public NewsVideoViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsVideoHead = itemView.findViewById(R.id.news_video_head);
            mNewsVideoDesc = itemView.findViewById(R.id.news_video_desc);
            mNewsVideoImage = itemView.findViewById(R.id.news_video_image);
            mNewsVideoExtra = itemView.findViewById(R.id.news_video_extra);
            mNewsVideoPlay = itemView.findViewById(R.id.video_play);

            mNewsVideoPlay.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mVideoClickListener.videoActivity(position);

                }
            });
            mNewsVideoHead.setOnClickListener(v -> {
                mNewsOutListener.newsDetailActivity(getAdapterPosition());
                mNewsVideoHead.setTextColor(Color.parseColor("#ffa500"));
            });
        }

    }
    public class CustomAdViewHolder extends RecyclerView.ViewHolder{

        public ImageView mNewsImage;

        public CustomAdViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsImage = itemView.findViewById(R.id.customadimage);
        }
    }
    public class FullImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView mNewsImage;

        public FullImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsImage = itemView.findViewById(R.id.fullimage);
        }
    }
    @Override
    public int getItemCount() {
        return mNewsList.size();
    }



    private void populateNativeAdView (UnifiedNativeAd nativeAd,
                                       UnifiedNativeAdView adView){
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();


        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
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

}
