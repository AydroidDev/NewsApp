package com.vaankdeals.newsapp.ViewTypes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.vaankdeals.newsapp.Activity.MainActivity;
import com.vaankdeals.newsapp.Fragment.NewsFragment;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FragmentVideoFull extends Fragment  {

    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;
    private String video_url;
    private int positionVid;
    private ProgressBar mExoProgress;
    private ImageView mExoThumb;


    public FragmentVideoFull() {
        // Required empty public constructor
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
         mExoThumb = view.findViewById(R.id.exo_thumb);
         mExoProgress = view.findViewById(R.id.exo_progress_bar_custom);
        NewsModel model =(NewsModel)getArguments().getSerializable("model");
        MainActivity.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

                if(position==1&& NewsFragment.newsViewpager.getCurrentItem()==positionVid) {
                        startPlayer();
                }
                else
                    pausePlayer();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        assert getArguments() != null;
        positionVid =getArguments().getInt("video_position");
        assert model != null;
        video_url=model.getmNewsVideo();

        Glide.with(requireContext()).load(model.getmNewsImage()).into(mExoThumb);
        String video_dest =model.getmNewslink();
        String video_head=model.getmNewsHead();
        mVideoHead.setText(video_head);
        mVideoDesc.setText(model.getmNewsDesc());
        String video_rating=model.getmNewsSource();
        if(!video_dest.equals("")){
            mVideoLink.setVisibility(View.VISIBLE);
            mVideoLink.setText(video_rating);
            mVideoRating.setText(video_rating);
        }
        else if(video_head.equals("")){
            mVideoOverlay.setVisibility(View.INVISIBLE);
        }
        mVideoLink.setText(model.getmNewsSource());

        mVideoLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_dest));
            startActivity(browserIntent);
        });
        return view;
    }


    private void initializePlayer() {


        /* Instantiate a DefaultLoadControl.Builder. */
        DefaultLoadControl.Builder builder = new
                DefaultLoadControl.Builder();

        /* Maximum amount of media data to buffer (in milliseconds). */
        final long loadControlMaxBufferMs = 15000;

/*Configure the DefaultLoadControl to use our setting for how many
Milliseconds of media data to buffer. */

        builder.setBufferDurationsMs(
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                (int) loadControlMaxBufferMs,
                /* To reduce the startup time, also change the line below */
                2000,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);

        /* Build the actual DefaultLoadControl instance */
        DefaultLoadControl loadControl = builder.createDefaultLoadControl();
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        simpleExoPlayer = new SimpleExoPlayer.Builder(requireContext()).setLoadControl(loadControl).build();
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_BUFFERING){
                    mExoProgress.setVisibility(View.VISIBLE);

                }else if(playbackState == Player.STATE_READY){
                    mExoProgress.setVisibility(View.GONE);
                    mExoThumb.setVisibility(View.GONE);
                }
            }
        });
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setRepeatMode(simpleExoPlayer.REPEAT_MODE_ONE);
        simpleExoPlayer.seekTo(100);
        simpleExoPlayer.setPlayWhenReady(true);

        Uri uri = Uri.parse(video_url);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireContext(),
                Util.getUserAgent(requireContext(), "newsapp"));
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
        if(MainActivity.viewPagerMain.getCurrentItem()==1&& NewsFragment.newsViewpager.getCurrentItem()==positionVid) {
            if (simpleExoPlayer != null)
                startPlayer();
            else
                initializePlayer();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer!=null)
        simpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onStop() {
        super.onStop();
         pausePlayer();
    }




    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }
    private void pausePlayer(){
        if(simpleExoPlayer!=null)
        simpleExoPlayer.setPlayWhenReady(false);
    }
    private void startPlayer(){
        if(simpleExoPlayer!=null) {
            simpleExoPlayer.setPlayWhenReady(true);
        }

    }
}
