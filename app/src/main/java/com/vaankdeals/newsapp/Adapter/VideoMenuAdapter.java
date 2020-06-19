package com.vaankdeals.newsapp.Adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vaankdeals.newsapp.Model.GameModel;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.Model.VideoModel;
import com.vaankdeals.newsapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoMenuAdapter extends RecyclerView.Adapter {
    private ArrayList<VideoModel> VideoList;
    private Context mContext;
    private String mUrl;
    private videoListener mVideoListener;
    public interface videoListener{
        void vidClick(String videoId,String url);
    }
    public void setmVideoListener(videoListener listener){
        mVideoListener = listener;
    }
    public VideoMenuAdapter(Context context, ArrayList<VideoModel> videoList){
        this.VideoList = videoList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_menu_layout, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoModel newsModel= VideoList.get(position);
        VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        mUrl = newsModel.getVideo_type();
        Glide.with(mContext)
                .load(newsModel.getVideo_image())
                .apply(new RequestOptions().placeholder(R.drawable.video_menu_placeholder).centerCrop().override(105,160))
                .into(videoViewHolder.mVideoImage);

    }

    private class VideoViewHolder extends RecyclerView.ViewHolder{

        ImageView mVideoImage;
        VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            mVideoImage=itemView.findViewById(R.id.video_image);
            mVideoImage.setOnClickListener(v -> mVideoListener.vidClick(VideoList.get(getAdapterPosition()).getVideo_id(),mUrl));
        }
    }
    @Override
    public int getItemCount() {
        return VideoList.size();
    }
}
