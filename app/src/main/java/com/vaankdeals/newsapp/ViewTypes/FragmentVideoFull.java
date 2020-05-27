package com.vaankdeals.newsapp.ViewTypes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.vaankdeals.newsapp.Activity.MainActivity;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;

public class FragmentVideoFull extends Fragment  {

    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;
    private String video_url;


    public FragmentVideoFull() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fullvideoitem, container, false);
        Button mVideoLink=view.findViewById(R.id.videolink);
        TextView mVideoHead=view.findViewById(R.id.videohead);
        TextView mVideoDesc=view.findViewById(R.id.videodesc);
        TextView mVideoRating=view.findViewById(R.id.videorating);
        View mVideoOverlay=view.findViewById(R.id.full_video_overlay);
        playerView=view.findViewById(R.id.video_view);

        video_url=getArguments().getString("video_url");
        String video_dest =getArguments().getString("video_dest");
        mVideoHead.setText(getArguments().getString("video_head"));
        mVideoDesc.setText(getArguments().getString("video_desc"));
        String video_rating=getArguments().getString("video_rating");
        if(!video_dest.equals("")){
            mVideoLink.setVisibility(View.VISIBLE);
            mVideoLink.setText(video_rating);
            mVideoRating.setText(video_rating);
        }
        mVideoLink.setText(getArguments().getString("video_rating"));

        mVideoLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_dest));
            startActivity(browserIntent);
        });
        return view;
    }
    private void initializePlayer() {

        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        simpleExoPlayer = new SimpleExoPlayer.Builder(getActivity()).build();
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setRepeatMode(simpleExoPlayer.REPEAT_MODE_ONE);
        simpleExoPlayer.setPlayWhenReady(true);
        Uri uri = Uri.parse(video_url);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "newsapp"));
        MediaSource videoSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
        simpleExoPlayer.prepare(videoSource);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            releasePlayer();
    }



    @Override
    public void onResume() {
        super.onResume();
            if(simpleExoPlayer!=null)
                startPlayer();
            else
                initializePlayer();

    }

    @Override
    public void onPause() {
        super.onPause();
            releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
         releasePlayer();

    }
    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }
    public void pausePlayer(){
        if(simpleExoPlayer!=null)
        simpleExoPlayer.setPlayWhenReady(false);
    }
    private void startPlayer(){
        if(simpleExoPlayer!=null)
            simpleExoPlayer.setPlayWhenReady(true);
    }
}
