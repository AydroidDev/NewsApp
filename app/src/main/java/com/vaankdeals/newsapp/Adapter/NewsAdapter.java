package com.vaankdeals.newsapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Class.JzvdStdTikTok;
import com.vaankdeals.newsapp.Class.UnifiedNativeAdViewHolder;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Object> mNewsList;
    private static final int NEWS_IMAGE_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    private static final int FULL_IMAGE_TYPE = 2;
    private static final int WEBVIEW_TYPE = 3;
    private static final int CUSTOM_AD_TYPE = 4;
    private static final int VIDEO_NEWS_TYPE = 5;
     private static final int YT_VIDEO_NEWS_TYPE = 6;
     private static final int FILM_REVIEW_TYPE = 7;
     private static final int FULL_VIDEO_TYPE = 8;
    private static final int FULL_YT_TYPE = 9;
    private Lifecycle lifecycle;
    SimpleExoPlayer simpleExoPlayer;
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    private newsOutListener mNewsOutListener;
    private videoClickListener mVideoClickListener;
    private shareClickListener mShareClickListener;
    private whatsClickListener mWhatsClickListener;
    private adClickListener mAdClickListener;
    private bookmarkListener mBookmarkListener;
    private reviewClickListener mReviewClickListener;

     public interface reviewClickListener{
         void reviewClick(int position);
     }
     public void setmReviewClickListener(reviewClickListener listener){
         mReviewClickListener = listener;
     }

    private actionbarListener mActionbarListener;
    public interface actionbarListener{
        void actionBarView();
    }
    public void setactionbarListener(actionbarListener listener){
        mActionbarListener = listener;
    }
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

    public interface shareClickListener{
        void shareNormal(int position,Bitmap bitmap);
    }
    public void setshareClickListener(shareClickListener listener){
        mShareClickListener = listener;
    }
    public interface whatsClickListener{
        void shareWhats(int position,Bitmap bitmap);
    }
    public void setwhatsClickListener(whatsClickListener listener){
        mWhatsClickListener = listener;
    }

    public interface adClickListener{
        void customAdLink(int position);
    }
    public void setadClickListener(adClickListener listener){
        mAdClickListener = listener;
    }

    public interface bookmarkListener{
        void bookmarkAll(int position);
    }
    public void setbookmarkListener(bookmarkListener listener){
        mBookmarkListener = listener;
    }

    public NewsAdapter(Context context, List<Object> mNewsList, Lifecycle lifecycle) {
        this.mNewsList = mNewsList;
        this.mContext = context;
        this.lifecycle=lifecycle;
//added seekbar
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
                        viewGroup.getContext()).inflate(R.layout.ad_google_new,
                        viewGroup, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case FULL_IMAGE_TYPE:
                View imageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fullphotoitem, viewGroup, false);
                return new FullImageViewHolder(imageView);
            case VIDEO_NEWS_TYPE:
                View videoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsvideoitem, viewGroup, false);
                return new NewsVideoViewHolder(videoView);
            case YT_VIDEO_NEWS_TYPE:
                View ytVideoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsytvideoitem, viewGroup, false);
                return new YtNewsVideoViewHolder(ytVideoView);
            case WEBVIEW_TYPE:
                View webView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.webview_item, viewGroup, false);
                return new WebViewViewHolder(webView);

            case FULL_YT_TYPE:
                View fullyt = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fullytitem, viewGroup, false);
                return new YtViewHolder(fullyt);

            case FULL_VIDEO_TYPE:
                View fullVideo = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fullvideoitem, viewGroup, false);
                return new FullPlayerViewHolder(fullVideo);
            case FILM_REVIEW_TYPE:
                View filmReview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
                return new ReviewViewHolder(filmReview);

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
                case "6":
                    return YT_VIDEO_NEWS_TYPE;
                case "7":
                    return FILM_REVIEW_TYPE;
                case "8":
                    return FULL_VIDEO_TYPE;
                case "9":
                    return FULL_YT_TYPE;
                default:
                    return -1;
            }
        }
    }



    @SuppressLint("SetJavaScriptEnabled")
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

                webViewViewHolder.mWebView.getSettings().setDomStorageEnabled(true);
                webViewViewHolder.mWebView.getSettings().setJavaScriptEnabled(true);
                webViewViewHolder.mWebView.loadUrl(web_url);
                break;
            case CUSTOM_AD_TYPE:
                NewsModel customad = (NewsModel) mNewsList.get(position);
                CustomAdViewHolder customAdViewHolder = (CustomAdViewHolder) holder;

                String customadimage = customad.getmNewsImage();
                Glide.with(mContext).load(customadimage).into(customAdViewHolder.mNewsImage);
                break;
            case FULL_YT_TYPE:
                NewsModel ytfull = (NewsModel) mNewsList.get(position);
                YtViewHolder ytViewHolder = (YtViewHolder) holder;

                String yttitle =ytfull.getmNewsHead();
                String ytid=ytfull.getmNewsVideo();
                String vidId=getVideoIdFromYoutubeUrl(ytid);
                String ytimage = ytfull.getmNewsImage();

                String ytDesc = ytfull.getmNewsDesc();
                String ytRating = ytfull.getmNewsSource();
                String ytButtonText = ytfull.getmNewsSource();

                ytViewHolder.mYtTitle.setText(yttitle);
                ytViewHolder.mYtDesc.setText(ytDesc);
                ytViewHolder.mYtRating.setText(ytRating);
                ytViewHolder.mYtButton.setText(ytButtonText);

                if(ytButtonText.isEmpty())
                    ytViewHolder.mYtButton.setVisibility(View.GONE);

                if(ytButtonText.isEmpty()&&yttitle.isEmpty())
                    ytViewHolder.ytOverlay.setVisibility(View.GONE);
                ytViewHolder.cueVideoFull(vidId);
                Glide.with(mContext).load(ytimage).into(ytViewHolder.mYtImage);
                ytViewHolder.mYtTitle.setText(yttitle);
                break;
            case FULL_VIDEO_TYPE:
                NewsModel fullvideo = (NewsModel) mNewsList.get(position);
                FullPlayerViewHolder fullVideoViewHolder = (FullPlayerViewHolder) holder;

                String videoHead=fullvideo.getmNewsHead();
                String videoDesc=fullvideo.getmNewsDesc();
                String videoLink =fullvideo.getmNewsSource();
                String videoRating=fullvideo.getmNewsSource();
                String videoUrl = fullvideo.getmNewsVideo();
                String imageUrl = fullvideo.getmNewsImage();

                simpleExoPlayer = new SimpleExoPlayer.Builder(mContext).build();
                fullVideoViewHolder.playerView.setPlayer(simpleExoPlayer);
                simpleExoPlayer.setPlayWhenReady(true);

                Uri uri = Uri.parse(videoUrl);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                        Util.getUserAgent(mContext, "newsapp"));
// This is the MediaSource representing the media to be played.
                MediaSource videoSource =
                        new ProgressiveMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(uri);
                simpleExoPlayer.prepare(videoSource);

                fullVideoViewHolder.mVideoHead.setText(videoHead);
                fullVideoViewHolder.mVideoDesc.setText(videoDesc);
                fullVideoViewHolder.mVideoLink.setText(videoLink);
                fullVideoViewHolder.mVideoRating.setText(videoRating);
                if(videoLink.isEmpty())
                    fullVideoViewHolder.mVideoLink.setVisibility(View.GONE);

                if(videoLink.isEmpty()&&videoHead.isEmpty())
                    fullVideoViewHolder.mVideoOverlay.setVisibility(View.GONE);


                break;
            case FILM_REVIEW_TYPE:
                NewsModel reviewFilm = (NewsModel) mNewsList.get(position);
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;

                String reviewImage = reviewFilm.getmNewsImage();
                String reviewHead = reviewFilm.getmNewsHead();
                String reviewDesc = reviewFilm.getmNewsDesc();
                String reviewRating = reviewFilm.getmNewsVideo();
                String reviewGenre = reviewFilm.getmNewsSource();

                reviewViewHolder.mReviewHead.setText(reviewHead);
                reviewViewHolder.mReviewDesc.setText(reviewDesc);
                reviewViewHolder.mReviewRating.setText(reviewRating);
                reviewViewHolder.mReviewLink.setText(reviewGenre);

                if(reviewGenre.isEmpty())
                    reviewViewHolder.mReviewLink.setVisibility(View.GONE);

                if(reviewHead.isEmpty()&&reviewGenre.isEmpty())
                    reviewViewHolder.mReviewOverlay.setVisibility(View.GONE);

                Glide.with(mContext).load(reviewImage).into(reviewViewHolder.mReviewImage);
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
                String product_id =object.getmNewsId();
                String news_head = object.getmNewsHead();
                String news_desc = object.getmNewsDesc();
                String news_image = object.getmNewsImage();
                String news_source = object.getmNewsSource();
                String news_day = object.getmNewsDay();
                String news_extra = "click on title to read more on " + news_source + " / " + news_day;


                DatabaseHandler db = new DatabaseHandler(mContext);
                String countQuery = "SELECT  * FROM " + TABLE_NEWS + " where " + NEWS_ID +  " = " + product_id;
                SQLiteDatabase dbs = db.getReadableDatabase();
                Cursor cursor = dbs.rawQuery(countQuery, null);
                int recount = cursor.getCount();
                if(recount <= 0){
                    newsViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                }
                else {
                    newsViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button_clicked);
                }
                cursor.close();

                newsViewHolder.mNewsExtra.setText(news_extra);
                newsViewHolder.mNewsHead.setText(news_head);
                newsViewHolder.mNewsDesc.setText(news_desc);
                Glide.with(mContext).load(news_image).into(newsViewHolder.mNewsImage);
                break;
            case YT_VIDEO_NEWS_TYPE:
                NewsModel yt_video_news = (NewsModel) mNewsList.get(position);
                YtNewsVideoViewHolder ytNewsVideoViewHolder = (YtNewsVideoViewHolder) holder;

                String news_video_yt_video =yt_video_news.getmNewsVideo();
                String product_id_yt_video =yt_video_news.getmNewsId();
                String news_head_yt_video = yt_video_news.getmNewsHead();
                String news_desc_yt_video = yt_video_news.getmNewsDesc();
                String news_source_yt_video = yt_video_news.getmNewsSource();
                String news_day_yt_video = yt_video_news.getmNewsDay();
                String news_extra_yt_video = "click on title to read more on " + news_source_yt_video + " / " + news_day_yt_video;

                String vId=getVideoIdFromYoutubeUrl(news_video_yt_video);
                DatabaseHandler dbVideoyt = new DatabaseHandler(mContext);
                String countQueryVideoyt = "SELECT  * FROM " + TABLE_NEWS + " where " + NEWS_ID +  " = " + product_id_yt_video;
                SQLiteDatabase dbsVideoyt = dbVideoyt.getReadableDatabase();
                Cursor cursorVideoyt = dbsVideoyt.rawQuery(countQueryVideoyt, null);
                int recountVideoyt = cursorVideoyt.getCount();
                if(recountVideoyt <= 0){
                    ytNewsVideoViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                }
                else {
                    ytNewsVideoViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button_clicked);
                }
                cursorVideoyt.close();
                ytNewsVideoViewHolder.cueVideo(vId);
                ytNewsVideoViewHolder.mNewsVideoExtra.setText(news_extra_yt_video);
                ytNewsVideoViewHolder.mNewsVideoHead.setText(news_head_yt_video);
                ytNewsVideoViewHolder.mNewsVideoDesc.setText(news_desc_yt_video);
                break;
            case VIDEO_NEWS_TYPE:
                NewsModel video_news = (NewsModel) mNewsList.get(position);
                NewsVideoViewHolder newsVideoViewHolder = (NewsVideoViewHolder) holder;

                String product_id_video =video_news.getmNewsId();
                String news_head_video = video_news.getmNewsHead();
                String news_desc_video = video_news.getmNewsDesc();
                String news_image_video = video_news.getmNewsImage();
                String news_source_video = video_news.getmNewsSource();
                String news_day_video = video_news.getmNewsDay();
                String news_extra_video = "click on title to read more on " + news_source_video + " / " + news_day_video;


                DatabaseHandler dbVideo = new DatabaseHandler(mContext);
                String countQueryVideo = "SELECT  * FROM " + TABLE_NEWS + " where " + NEWS_ID +  " = " + product_id_video;
                SQLiteDatabase dbsVideo = dbVideo.getReadableDatabase();
                Cursor cursorVideo = dbsVideo.rawQuery(countQueryVideo, null);
                int recountVideo = cursorVideo.getCount();
                if(recountVideo <= 0){
                    newsVideoViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                }
                else {
                    newsVideoViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button_clicked);
                }
                cursorVideo.close();

                newsVideoViewHolder.mNewsVideoExtra.setText(news_extra_video);
                newsVideoViewHolder.mNewsVideoHead.setText(news_head_video);
                newsVideoViewHolder.mNewsVideoDesc.setText(news_desc_video);
                Glide.with(mContext).load(news_image_video).into(newsVideoViewHolder.mNewsVideoImage);
                break;

        }
    }
    private class WebViewViewHolder extends RecyclerView.ViewHolder{

        private WebView mWebView;
        private ProgressBar mProgress;

        private WebViewViewHolder(@NonNull View itemView) {
            super(itemView);

            mWebView = itemView.findViewById(R.id.webview_item);
            mProgress = itemView.findViewById(R.id.progressBar);
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView mNewsHead;
        TextView mNewsDesc;
        ImageView mNewsImage;
        TextView mNewsExtra;
        Button mShareButton;
        Button mWhatsButton;
        Button mBookmarkButton;
        LinearLayout mLayout;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsHead = itemView.findViewById(R.id.news_head);
            mNewsDesc = itemView.findViewById(R.id.news_desc);
            mNewsImage = itemView.findViewById(R.id.news_image);
            mNewsExtra = itemView.findViewById(R.id.news_extra);
            mShareButton = itemView.findViewById(R.id.sharecard);
            mWhatsButton = itemView.findViewById(R.id.sharewhats);
            mBookmarkButton = itemView.findViewById(R.id.bookmark_button);
            mLayout = itemView.findViewById(R.id.news_item);
            mNewsHead.setOnClickListener(v -> mNewsOutListener.newsDetailActivity(getAdapterPosition()));

            mShareButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mShareClickListener.shareNormal(position,bitmap);

                }
            });
            mWhatsButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mWhatsClickListener.shareWhats(position,bitmap);

                }
            });
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListener.bookmarkAll(position);

                }
            });
            mLayout.setOnClickListener(v -> mActionbarListener.actionBarView());
        }
    }
    public class NewsVideoViewHolder extends RecyclerView.ViewHolder{

        TextView mNewsVideoHead;
        TextView mNewsVideoDesc;
        ImageView mNewsVideoImage;
        TextView mNewsVideoExtra;
        ImageView mNewsVideoPlay;
        Button mShareButton;
        Button mWhatsButton;
        Button mBookmarkButton;
        LinearLayout mLayout;

        NewsVideoViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsVideoHead = itemView.findViewById(R.id.news_video_head);
            mNewsVideoDesc = itemView.findViewById(R.id.news_video_desc);
            mNewsVideoImage = itemView.findViewById(R.id.news_video_image);
            mNewsVideoExtra = itemView.findViewById(R.id.news_video_extra);
            mNewsVideoPlay = itemView.findViewById(R.id.video_play);
            mShareButton = itemView.findViewById(R.id.video_sharecard);
            mWhatsButton = itemView.findViewById(R.id.video_sharewhats);
            mBookmarkButton = itemView.findViewById(R.id.video_bookmark_button);
            mLayout = itemView.findViewById(R.id.news_video_item);




            mShareButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mShareClickListener.shareNormal(position,bitmap);

                }
            });
            mWhatsButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mWhatsClickListener.shareWhats(position,bitmap);

                }
            });
            mNewsVideoPlay.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mVideoClickListener.videoActivity(position);

                }
            });
            mNewsVideoHead.setOnClickListener(v -> mNewsOutListener.newsDetailActivity(getAdapterPosition()));
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListener.bookmarkAll(position);

                }
            });
            mLayout.setOnClickListener(v -> mActionbarListener.actionBarView());
        }
    }
     public class YtNewsVideoViewHolder extends RecyclerView.ViewHolder {
         TextView mNewsVideoHead;
         TextView mNewsVideoDesc;
         ImageView mNewsVideoImage;
         TextView mNewsVideoExtra;
         ImageView mNewsVideoPlay;
         Button mShareButton;
         Button mWhatsButton;
         Button mBookmarkButton;
         LinearLayout mLayout;
         String currentVideoId;

         YouTubePlayer youTubePlayer;
         YouTubePlayerView youTubePlayerView;

         YtNewsVideoViewHolder(@NonNull View itemView) {
             super(itemView);

             youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
             mNewsVideoHead = itemView.findViewById(R.id.news_video_head);
             mNewsVideoDesc = itemView.findViewById(R.id.news_video_desc);
             mNewsVideoExtra = itemView.findViewById(R.id.news_video_extra);
             mNewsVideoPlay = itemView.findViewById(R.id.video_play);
             mShareButton = itemView.findViewById(R.id.video_sharecard);
             mWhatsButton = itemView.findViewById(R.id.video_sharewhats);
             mBookmarkButton = itemView.findViewById(R.id.video_bookmark_button);
             mLayout = itemView.findViewById(R.id.news_video_item);


             lifecycle.addObserver(youTubePlayerView);
             youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                 @Override
                 public void onReady(YouTubePlayer initializedYoutubePlayer) {
                     youTubePlayer = initializedYoutubePlayer;

                     youTubePlayer.cueVideo(currentVideoId,0);
                 }
             });

             mShareButton.setOnClickListener(v -> {
                 int position = getAdapterPosition();
                 if (position != RecyclerView.NO_POSITION) {
                     BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                     Bitmap bitmap = drawable.getBitmap();
                     mShareClickListener.shareNormal(position, bitmap);
                 }
             });
             mWhatsButton.setOnClickListener(v -> {
                 int position = getAdapterPosition();
                 if (position != RecyclerView.NO_POSITION) {
                     BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                     Bitmap bitmap = drawable.getBitmap();
                     mWhatsClickListener.shareWhats(position, bitmap);

                 }
             });
             mNewsVideoHead.setOnClickListener(v -> mNewsOutListener.newsDetailActivity(getAdapterPosition()));
             mBookmarkButton.setOnClickListener(v -> {
                 int position = getAdapterPosition();
                 if (position != RecyclerView.NO_POSITION) {
                     mBookmarkListener.bookmarkAll(position);

                 }
             });
             mLayout.setOnClickListener(v -> mActionbarListener.actionBarView());
         }

         void cueVideo(String videoId) {
             currentVideoId = videoId;
             if (youTubePlayer == null)
                 return;

             youTubePlayer.cueVideo(currentVideoId,0);

         }
     }

    public class FullPlayerViewHolder extends RecyclerView.ViewHolder{


        PlayerView playerView;
        Button mVideoLink;
        TextView mVideoHead;
        TextView mVideoDesc;
        TextView mVideoRating;
        View mVideoOverlay;

        FullPlayerViewHolder(@NonNull View itemView) {
            super(itemView);

            mVideoLink=itemView.findViewById(R.id.videolink);
            mVideoHead=itemView.findViewById(R.id.videohead);
            mVideoDesc=itemView.findViewById(R.id.videodesc);
            mVideoRating=itemView.findViewById(R.id.videorating);
            playerView = itemView.findViewById(R.id.video_view);
            mVideoOverlay=itemView.findViewById(R.id.full_video_overlay);

            mVideoLink.setOnClickListener(v -> mReviewClickListener.reviewClick(getAdapterPosition()));
        }

    }
    public class CustomAdViewHolder extends RecyclerView.ViewHolder{

         ImageView mNewsImage;
         Button mAdLink;

         CustomAdViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsImage = itemView.findViewById(R.id.customadimage);
            mAdLink = itemView.findViewById(R.id.adlinkout);

            mAdLink.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mAdClickListener.customAdLink(position);

                }
            });
        }
    }

    public class YtViewHolder extends RecyclerView.ViewHolder{

        ImageView mYtImage;
        Button mPlay;
        TextView mYtTitle;
        String currentVideoIdFull;
        ProgressBar ytprogress;
        YouTubePlayerSeekBar youTubePlayerSeekBar;
        YouTubePlayerView youTubePlayerViewFull;
        LinearLayout ytContainer;
        TextView mYtDesc;
        Button mYtButton;
        TextView mYtRating;
        LinearLayout yttextcontainer;
        FrameLayout imageoverlay;
        View ytOverlay;
        YouTubePlayer youTubePlayerFull;
        YtViewHolder(@NonNull View itemView) {
            super(itemView);

            ytOverlay=itemView.findViewById(R.id.yt_overlay);
            yttextcontainer=itemView.findViewById(R.id.yttextcontainer);
            imageoverlay=itemView.findViewById(R.id.image_black);
            ytContainer=itemView.findViewById(R.id.yt_container);
            mYtImage = itemView.findViewById(R.id.ytimage);
            mPlay = itemView.findViewById(R.id.ytplay);
            mYtTitle=itemView.findViewById(R.id.yttitle);
            mYtDesc=itemView.findViewById(R.id.ytdesc);
            mYtButton=itemView.findViewById(R.id.ytbutton);
            mYtRating=itemView.findViewById(R.id.ytrating);

            youTubePlayerViewFull =itemView.findViewById(R.id.yt_view);
            ytprogress=itemView.findViewById(R.id.ytprogress);
            youTubePlayerSeekBar=itemView.findViewById(R.id.yt_seekbar);

            lifecycle.addObserver(youTubePlayerViewFull);
            youTubePlayerViewFull.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer initializedYoutubePlayer) {
                    ytprogress.setVisibility(View.INVISIBLE);
                    mPlay.setVisibility(View.VISIBLE);
                    youTubePlayerFull = initializedYoutubePlayer;
                    youTubePlayerFull.cueVideo(currentVideoIdFull,0);
                    initializedYoutubePlayer.addListener(youTubePlayerSeekBar);
                    youTubePlayerSeekBar.setYoutubePlayerSeekBarListener(initializedYoutubePlayer::seekTo);

                }
            });

            mPlay.setOnClickListener(v -> {
                mPlay.setVisibility(View.INVISIBLE);
                imageoverlay.setForeground(new ColorDrawable(ContextCompat.getColor(mContext, R.color.semiblack)));
                ytContainer.setVisibility(View.VISIBLE);
                youTubePlayerViewFull.getYouTubePlayerWhenReady(YouTubePlayer::play);

            });
            mYtButton.setOnClickListener(v -> mReviewClickListener.reviewClick(getAdapterPosition()));
        }
        void cueVideoFull(String videoId) {
            currentVideoIdFull = videoId;
            if (youTubePlayerFull == null)
                return;

            youTubePlayerFull.cueVideo(currentVideoIdFull,0);
        }
    }
     public class ReviewViewHolder extends RecyclerView.ViewHolder{

         ImageView mReviewImage;
         TextView mReviewHead;
         Button mReviewLink;
         TextView mReviewDesc;
         TextView mReviewRating;
         View mReviewOverlay;

         ReviewViewHolder(@NonNull View itemView) {
             super(itemView);

             mReviewOverlay=itemView.findViewById(R.id.review_overlay);
             mReviewImage = itemView.findViewById(R.id.reviewimage);
             mReviewHead=itemView.findViewById(R.id.reviewhead);
             mReviewDesc=itemView.findViewById(R.id.reviewdesc);
             mReviewRating=itemView.findViewById(R.id.reviewrating);
             mReviewLink = itemView.findViewById(R.id.reviewlink);

             mReviewLink.setOnClickListener(v -> {
                 int position = getAdapterPosition();
                 if (position != RecyclerView.NO_POSITION) {
                     mReviewClickListener.reviewClick(position);

                 }
             });
         }
     }

    public class FullImageViewHolder extends RecyclerView.ViewHolder{

        ImageView mNewsImage;
        Button mShareButton;

        FullImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mNewsImage = itemView.findViewById(R.id.fullimage);
            mShareButton = itemView.findViewById(R.id.sharebuttoncard);

            mShareButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mShareClickListener.shareNormal(position,bitmap);

                }
            });
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

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
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

     private String getVideoIdFromYoutubeUrl(String url){
         String videoId = null;
         String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
         Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
         Matcher matcher = pattern.matcher(url);
         if(matcher.find()){
             videoId = matcher.group(1);
         }
         return videoId;
     }
     public  void pauseYtVid(){
        //video still playing on background after scroll even if autoplay=false

         if(simpleExoPlayer!=null)
             pausePlayer();
     }
    private void pausePlayer(){
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }
    private void startPlayer(){
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
    }
    private MediaSource buildMediaSource(Uri uri) {
        return new ProgressiveMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }
}
