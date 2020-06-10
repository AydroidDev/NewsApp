package com.vaankdeals.newsapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Model.AllNewsModel;
import com.vaankdeals.newsapp.R;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Object> mNewsList;
    private static final int NEWS_IMAGE_TYPE = 0;
    private static final int VIDEO_NEWS_TYPE = 5;
    private static final int YT_VIDEO_NEWS_TYPE = 6;
    private YouTubePlayer youTubePlayer;
    YouTubePlayerView youTubePlayerView;

    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";

    private newsOutListenerAll mNewsOutListenerAll;
    private videoClickListenerAll mVideoClickListenerAll;
    private shareClickListenerAll mShareClickListenerAll;
    private whatsClickListenerAll mWhatsClickListenerAll;
    private bookmarkListenerAll mBookmarkListenerAll;
    private actionbarListenerAll mActionbarListenerAll;
    public interface actionbarListenerAll{
        void actionBarViewAll();
    }
    public void setactionbarListenerAll(actionbarListenerAll listener){
        mActionbarListenerAll = listener;
    }
    public interface videoClickListenerAll{
        void videoActivityAll(int position);
    }
    public void setvideoClickListenerAll(videoClickListenerAll listener){
        mVideoClickListenerAll = listener;
    }
    public interface newsOutListenerAll{
        void newsDetailActivityAll(int position);
    }
    public void setnewsOutListenerAll(newsOutListenerAll listener){
        mNewsOutListenerAll = listener;
    }

    public interface shareClickListenerAll{
        void shareNormalAll(int position, Bitmap bitmap);
    }
    public void setshareClickListenerAll(shareClickListenerAll listener){
        mShareClickListenerAll = listener;
    }
    public interface whatsClickListenerAll{
        void shareWhatsAll(int position,Bitmap bitmap);
    }
    public void setwhatsClickListenerAll(whatsClickListenerAll listener){
        mWhatsClickListenerAll = listener;
    }



    public interface bookmarkListenerAll{
        void actionBarViewAll();
    }
    public void setbookmarkListenerAll(bookmarkListenerAll listener){
        mBookmarkListenerAll = listener;
    }

    public AllNewsAdapter(Context context, List<Object> mNewsList) {
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

            case VIDEO_NEWS_TYPE:
                View videoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsvideoitem, viewGroup, false);
                return new NewsVideoViewHolder(videoView);

            case YT_VIDEO_NEWS_TYPE:
                View ytVideoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsytvideoitem, viewGroup, false);
                return new YtNewsVideoViewHolder(ytVideoView);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
            AllNewsModel object = (AllNewsModel) mNewsList.get(position);
            switch (object.getmNewsType()) {
                case "1":
                    return NEWS_IMAGE_TYPE;
                case "5":
                    return VIDEO_NEWS_TYPE;
                case "6":
                    return YT_VIDEO_NEWS_TYPE;
                default:
                    return -1;
            }
    }



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        int viewType = getItemViewType(position);
        switch (viewType) {


            case NEWS_IMAGE_TYPE:
                AllNewsModel object = (AllNewsModel) mNewsList.get(position);
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

            case VIDEO_NEWS_TYPE:
                AllNewsModel video_news = (AllNewsModel) mNewsList.get(position);
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

            case YT_VIDEO_NEWS_TYPE:
                AllNewsModel yt_video_news = (AllNewsModel) mNewsList.get(position);
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
            mNewsHead.setOnClickListener(v -> {
                mNewsOutListenerAll.newsDetailActivityAll(getAdapterPosition());
                mNewsHead.setTextColor(Color.parseColor("#ffa500"));
            });

            mShareButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mShareClickListenerAll.shareNormalAll(position,bitmap);

                }
            });
            mWhatsButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mWhatsClickListenerAll.shareWhatsAll(position,bitmap);

                }
            });
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListenerAll.actionBarViewAll();

                }
            });
            mLayout.setOnClickListener(v -> mActionbarListenerAll.actionBarViewAll());
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

        private NewsVideoViewHolder(@NonNull View itemView) {
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
                    mShareClickListenerAll.shareNormalAll(position,bitmap);

                }
            });
            mWhatsButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mWhatsClickListenerAll.shareWhatsAll(position,bitmap);

                }
            });
            mNewsVideoPlay.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mVideoClickListenerAll.videoActivityAll(position);

                }
            });
            mNewsVideoHead.setOnClickListener(v -> {
                mNewsOutListenerAll.newsDetailActivityAll(getAdapterPosition());
                mNewsVideoHead.setTextColor(Color.parseColor("#ffa500"));
            });
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListenerAll.actionBarViewAll();

                }
            });
            mLayout.setOnClickListener(v -> mActionbarListenerAll.actionBarViewAll());
        }
    }
    public class YtNewsVideoViewHolder extends RecyclerView.ViewHolder{

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

        YtNewsVideoViewHolder(@NonNull View itemView) {
            super(itemView);

            youTubePlayerView =itemView.findViewById(R.id.youtube_player_view);
            mNewsVideoHead = itemView.findViewById(R.id.news_video_head);
            mNewsVideoDesc = itemView.findViewById(R.id.news_video_desc);
            mNewsVideoExtra = itemView.findViewById(R.id.news_video_extra);
            mNewsVideoPlay = itemView.findViewById(R.id.video_play);
            mShareButton = itemView.findViewById(R.id.video_sharecard);
            mWhatsButton = itemView.findViewById(R.id.video_sharewhats);
            mBookmarkButton = itemView.findViewById(R.id.video_bookmark_button);
            mLayout = itemView.findViewById(R.id.news_video_item);

            youTubePlayerView.enableBackgroundPlayback(false);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                    youTubePlayer=initializedYouTubePlayer;
                    youTubePlayer.cueVideo(currentVideoId, 0);
                }

            });
            mShareButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mShareClickListenerAll.shareNormalAll(position,bitmap);

                }
            });
            mWhatsButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mWhatsClickListenerAll.shareWhatsAll(position,bitmap);

                }
            });
            mNewsVideoHead.setOnClickListener(v -> {
                mNewsOutListenerAll.newsDetailActivityAll(getAdapterPosition());
            });
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListenerAll.actionBarViewAll();

                }
            });
            mLayout.setOnClickListener(v -> {

                mActionbarListenerAll.actionBarViewAll();
            });
        }
        void cueVideo(String videoId) {
            currentVideoId = videoId;
            if(youTubePlayer == null)
                return;

            youTubePlayer.cueVideo(videoId, 0);
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
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
        if(youTubePlayer!=null)
            youTubePlayer.pause();
    }
    public void onDestroy() {
        if(youTubePlayerView!=null)
        youTubePlayerView.release();
    }
}
