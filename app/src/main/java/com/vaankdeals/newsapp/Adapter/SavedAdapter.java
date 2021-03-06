package com.vaankdeals.newsapp.Adapter;

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
import com.vaankdeals.newsapp.Model.NewsBook;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewsBook> mNewsList;
    private static final int NEWS_IMAGE_TYPE = 0;
    private static final int VIDEO_NEWS_TYPE = 5;
    private static final int YT_VIDEO_NEWS_TYPE = 6;
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    private YouTubePlayer youTubePlayer;

    private videoClickListener mVideoClickListener;
    private newsOutListener mNewsOutListener;
    private shareClickListener mShareClickListener;
    private whatsClickListener mWhatsClickListener;
    private bookmarkListener mBookmarkListener;
    YouTubePlayerView youTubePlayerView;
    private actionbarListenerAll mActionbarListenerAll;;
    public interface actionbarListenerAll{
        void actionBarViewAll();
    }
    public void setactionbarListenerAll(actionbarListenerAll listener){
        mActionbarListenerAll = listener;
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
        void shareNormal(int position);
    }
    public void setshareClickListener(shareClickListener listener){
        mShareClickListener = listener;
    }
    public interface whatsClickListener{
        void shareWhats(int position);
    }
    public void setwhatsClickListener(whatsClickListener listener){
        mWhatsClickListener = listener;
    }


    public interface bookmarkListener{
        void bookmarkAll(int position);
    }
    public void setbookmarkListener(bookmarkListener listener){
        mBookmarkListener = listener;
    }

    public SavedAdapter(Context context, List<NewsBook> mNewsList) {
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

            NewsBook object = (NewsBook) mNewsList.get(position);
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




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        int viewType = getItemViewType(position);
        switch (viewType) {
           
            case NEWS_IMAGE_TYPE:
                NewsBook object = (NewsBook) mNewsList.get(position);
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;

                String product_id = object.getmNewsId();
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
                NewsBook video_news = (NewsBook) mNewsList.get(position);
                NewsVideoViewHolder newsVideoViewHolder = (NewsVideoViewHolder) holder;

                String product_id_video = video_news.getmNewsId();
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
                NewsBook yt_video_news = (NewsBook) mNewsList.get(position);
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

            mLayout = itemView.findViewById(R.id.news_item);
            mNewsHead = itemView.findViewById(R.id.news_head);
            mNewsDesc = itemView.findViewById(R.id.news_desc);
            mNewsImage = itemView.findViewById(R.id.news_image);
            mNewsExtra = itemView.findViewById(R.id.news_extra);
            mShareButton = itemView.findViewById(R.id.sharecard);
            mWhatsButton = itemView.findViewById(R.id.sharewhats);
            mBookmarkButton = itemView.findViewById(R.id.bookmark_button);

            mNewsHead.setOnClickListener(v -> {
                mNewsOutListener.newsDetailActivity(getAdapterPosition());
                mNewsHead.setTextColor(Color.parseColor("#ffa500"));
            });

            mShareButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mShareClickListener.shareNormal(position);

                }
            });
            mWhatsButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mWhatsClickListener.shareWhats(position);

                }
            });
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListener.bookmarkAll(position);

                }
            });
            mLayout.setOnClickListener(v -> {

                mActionbarListenerAll.actionBarViewAll();
            });
        }
    }
    public class NewsVideoViewHolder extends RecyclerView.ViewHolder{

        TextView mNewsVideoHead;
        TextView mNewsVideoDesc;
        ImageView mNewsVideoImage;
        TextView mNewsVideoExtra;
        ImageView mNewsVideoPlay;
        private Button mShareButton;
        private Button mWhatsButton;
        Button mBookmarkButton;
        LinearLayout mLayout;

        public NewsVideoViewHolder(@NonNull View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.news_video_item);
            mNewsVideoHead = itemView.findViewById(R.id.news_video_head);
            mNewsVideoDesc = itemView.findViewById(R.id.news_video_desc);
            mNewsVideoImage = itemView.findViewById(R.id.news_video_image);
            mNewsVideoExtra = itemView.findViewById(R.id.news_video_extra);
            mNewsVideoPlay = itemView.findViewById(R.id.video_play);
            mShareButton = itemView.findViewById(R.id.video_sharecard);
            mWhatsButton = itemView.findViewById(R.id.video_sharewhats);
            mBookmarkButton = itemView.findViewById(R.id.video_bookmark_button);
            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mShareClickListener.shareNormal(position);

                    }
                }
            });
            mWhatsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mWhatsClickListener.shareWhats(position);

                    }
                }
            });
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
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListener.bookmarkAll(position);

                }
            });
            mLayout.setOnClickListener(v -> {

                mActionbarListenerAll.actionBarViewAll();
            });
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
                    mShareClickListener.shareNormal(position);

                }
            });
            mWhatsButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BitmapDrawable drawable = (BitmapDrawable) mNewsVideoImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    mWhatsClickListener.shareWhats(position);

                }
            });
            mNewsVideoHead.setOnClickListener(v -> {
                mNewsOutListener.newsDetailActivity(getAdapterPosition());
            });
            mBookmarkButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBookmarkListener.bookmarkAll(position);

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
    public String getVideoIdFromYoutubeUrl(String url){
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
